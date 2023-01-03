/*******************************************************************************
 * Copyright (c) 2021, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.internal.support;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.IPointSeriesSettings;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.MappedSeriesSettings;
import org.eclipse.swtchart.extensions.core.MappingsSupport;
import org.eclipse.swtchart.extensions.core.MappingsType;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.SeriesLabelProvider;
import org.eclipse.swtchart.extensions.core.SeriesMapper;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;

public class MappingsIO {

	private static final String VALUE_DELIMITER = "\t";
	private static final String LINE_DELIMITER = "\r\n";

	public static Map<String, ISeriesSettings> importSettings(File file) {

		Map<String, ISeriesSettings> mappings = new HashMap<>();
		try {
			String content = Files.readString(Path.of(file.getAbsolutePath()));
			mappings.putAll(readSettings(content));
		} catch(IOException e) {
			e.printStackTrace();
		}
		//
		return mappings;
	}

	public static boolean exportSettings(File file, List<MappedSeriesSettings> mappings) {

		boolean success = false;
		try (PrintWriter printWriter = new PrintWriter(file)) {
			printWriter.print(saveSettings(mappings));
			success = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		//
		return success;
	}

	public static Map<String, ISeriesSettings> readSettings(String content) {

		Map<String, ISeriesSettings> mappings = new HashMap<>();
		//
		String[] lines = content.split(LINE_DELIMITER);
		for(String line : lines) {
			try {
				String[] values = line.split(VALUE_DELIMITER);
				importMapping(values, mappings);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		//
		return mappings;
	}

	public static String saveSettings(List<MappedSeriesSettings> mappings) {

		StringBuilder builder = new StringBuilder();
		for(MappedSeriesSettings mapping : mappings) {
			List<Object> values = new ArrayList<>();
			exportMapping(values, mapping);
			builder.append(create(values));
		}
		//
		return builder.toString();
	}

	public static void restoreSettings() {

		IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
		String settings = preferenceStore.getString(PreferenceConstants.P_SERIES_MAPPINGS);
		String content = new String(Base64.getDecoder().decode(settings));
		Map<String, ISeriesSettings> mappings = readSettings(content);
		for(Map.Entry<String, ISeriesSettings> mapping : mappings.entrySet()) {
			ISeriesSettings seriesSettings = mapping.getValue();
			SeriesMapper.put(mapping.getKey(), seriesSettings);
		}
	}

	public static void persistsSettings(List<MappedSeriesSettings> mappings) {

		try {
			IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
			String content = saveSettings(mappings);
			String settings = Base64.getEncoder().encodeToString(content.getBytes());
			preferenceStore.setValue(PreferenceConstants.P_SERIES_MAPPINGS, settings);
			ResourceSupport.savePreferenceStore();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 2 - Header
	 * 4 - Series Settings
	 * . - dynamic specific settings
	 */
	private static void exportMapping(List<Object> values, MappedSeriesSettings mapping) {

		ISeriesSettings seriesSettings = mapping.getSeriesSettings();
		values.add(mapping.getMappingsType());
		values.add(clean(mapping.getIdentifier()));
		exportSeriesSetting(values, seriesSettings);
		exportSeriesSetting(values, seriesSettings.getSeriesSettingsHighlight());
	}

	private static void importMapping(String[] values, Map<String, ISeriesSettings> mappings) {

		int index = 0;
		if(values.length >= 2) {
			MappingsType mappingsType = MappingsSupport.getMappingsType(values[index++]);
			ISeriesSettings seriesSettings = MappingsSupport.createSeriesSettings(mappingsType);
			if(seriesSettings != null) {
				String id = values[index++];
				String key = SeriesMapper.getKey(mappingsType, id);
				index = importSeriesSetting(values, index, seriesSettings);
				index = importSeriesSetting(values, index, seriesSettings.getSeriesSettingsHighlight());
				mappings.put(key, seriesSettings);
			}
		}
	}

	private static void exportSeriesSetting(List<Object> values, ISeriesSettings seriesSettings) {

		values.add(clean(seriesSettings.getDescription()));
		values.add(seriesSettings.isVisible());
		values.add(seriesSettings.isVisibleInLegend());
		values.add(ResourceSupport.getColor(SeriesLabelProvider.getColor(seriesSettings)));
		//
		if(seriesSettings instanceof IBarSeriesSettings) {
			exportBarSeriesSetting(values, (IBarSeriesSettings)seriesSettings);
		} else if(seriesSettings instanceof ICircularSeriesSettings) {
			exportCircularSeriesSetting(values, (ICircularSeriesSettings)seriesSettings);
		} else if(seriesSettings instanceof ILineSeriesSettings) {
			exportLineSeriesSetting(values, (ILineSeriesSettings)seriesSettings);
		} else if(seriesSettings instanceof IScatterSeriesSettings) {
			exportScatterSeriesSetting(values, (IScatterSeriesSettings)seriesSettings);
		}
	}

	private static int importSeriesSetting(String[] values, int index, ISeriesSettings seriesSettings) {

		if(values.length >= (index + 4)) {
			seriesSettings.setDescription(values[index++]);
			seriesSettings.setVisible(Boolean.parseBoolean(values[index++]));
			seriesSettings.setVisibleInLegend(Boolean.parseBoolean(values[index++]));
			SeriesLabelProvider.setColor(seriesSettings, ResourceSupport.getColor(values[index++]));
		}
		//
		if(seriesSettings instanceof IBarSeriesSettings) {
			index = importBarSeriesSetting(values, index, (IBarSeriesSettings)seriesSettings);
		} else if(seriesSettings instanceof ICircularSeriesSettings) {
			index = importCircularSeriesSetting(values, index, (ICircularSeriesSettings)seriesSettings);
		} else if(seriesSettings instanceof ILineSeriesSettings) {
			index = importLineSeriesSetting(values, index, (ILineSeriesSettings)seriesSettings);
		} else if(seriesSettings instanceof IScatterSeriesSettings) {
			index = importScatterSeriesSetting(values, index, (IScatterSeriesSettings)seriesSettings);
		}
		//
		return index;
	}

	private static void exportBarSeriesSetting(List<Object> values, IBarSeriesSettings barSeriesSettings) {

		values.add(ResourceSupport.getColor(barSeriesSettings.getBarColor()));
		values.add(barSeriesSettings.getBarPadding());
		values.add(barSeriesSettings.getBarWidth());
		values.add(barSeriesSettings.isBarOverlay());
		values.add(barSeriesSettings.getBarWidthStyle().name());
		values.add(barSeriesSettings.isEnableStack());
	}

	private static int importBarSeriesSetting(String[] values, int index, IBarSeriesSettings barSeriesSettings) {

		if(values.length >= (index + 6)) {
			barSeriesSettings.setBarColor(ResourceSupport.getColor(values[index++]));
			barSeriesSettings.setBarPadding(Integer.parseInt(values[index++]));
			barSeriesSettings.setBarWidth(Integer.parseInt(values[index++]));
			barSeriesSettings.setBarOverlay(Boolean.parseBoolean(values[index++]));
			barSeriesSettings.setBarWidthStyle(BarWidthStyle.valueOf(values[index++]));
			barSeriesSettings.setEnableStack(Boolean.parseBoolean(values[index++]));
		}
		//
		return index;
	}

	private static void exportCircularSeriesSetting(List<Object> values, ICircularSeriesSettings circularSeriesSettings) {

		values.add(ResourceSupport.getColor(circularSeriesSettings.getSliceColor()));
	}

	private static int importCircularSeriesSetting(String[] values, int index, ICircularSeriesSettings circularSeriesSettings) {

		if(values.length >= (index + 1)) {
			circularSeriesSettings.setSliceColor(ResourceSupport.getColor(values[index++]));
		}
		//
		return index;
	}

	private static void exportLineSeriesSetting(List<Object> values, ILineSeriesSettings lineSeriesSettings) {

		exportPointSeriesSetting(values, lineSeriesSettings);
		values.add(lineSeriesSettings.getLineStyle().name());
		values.add(lineSeriesSettings.getLineWidth());
		values.add(ResourceSupport.getColor(lineSeriesSettings.getLineColor()));
		values.add(lineSeriesSettings.getAntialias());
		values.add(lineSeriesSettings.isEnableArea());
		values.add(lineSeriesSettings.isEnableStack());
		values.add(lineSeriesSettings.isEnableStep());
	}

	private static int importLineSeriesSetting(String[] values, int index, ILineSeriesSettings lineSeriesSettings) {

		index = importPointSeriesSetting(values, index, lineSeriesSettings);
		if(values.length >= (index + 7)) {
			lineSeriesSettings.setLineStyle(LineStyle.valueOf(values[index++]));
			lineSeriesSettings.setLineWidth(Integer.parseInt(values[index++]));
			lineSeriesSettings.setLineColor(ResourceSupport.getColor(values[index++]));
			lineSeriesSettings.setAntialias(Integer.valueOf(values[index++]));
			lineSeriesSettings.setEnableArea(Boolean.valueOf(values[index++]));
			lineSeriesSettings.setEnableStack(Boolean.valueOf(values[index++]));
			lineSeriesSettings.setEnableStep(Boolean.valueOf(values[index++]));
		}
		//
		return index;
	}

	private static void exportScatterSeriesSetting(List<Object> values, IScatterSeriesSettings scatterSeriesSettings) {

		exportPointSeriesSetting(values, scatterSeriesSettings);
	}

	private static int importScatterSeriesSetting(String[] values, int index, IScatterSeriesSettings scatterSeriesSettings) {

		index = importPointSeriesSetting(values, index, scatterSeriesSettings);
		//
		return index;
	}

	private static void exportPointSeriesSetting(List<Object> values, IPointSeriesSettings pointSeriesSettings) {

		values.add(pointSeriesSettings.getSymbolType().name());
		values.add(pointSeriesSettings.getSymbolSize());
		values.add(ResourceSupport.getColor(pointSeriesSettings.getSymbolColor()));
	}

	private static int importPointSeriesSetting(String[] values, int index, IPointSeriesSettings pointSeriesSettings) {

		if(values.length >= (index + 3)) {
			pointSeriesSettings.setSymbolType(PlotSymbolType.valueOf(values[index++]));
			pointSeriesSettings.setSymbolSize(Integer.valueOf(values[index++]));
			pointSeriesSettings.setSymbolColor(ResourceSupport.getColor(values[index++]));
		}
		//
		return index;
	}

	private static String create(List<Object> values) {

		StringBuilder builder = new StringBuilder();
		Iterator<Object> iterator = values.iterator();
		while(iterator.hasNext()) {
			builder.append(iterator.next());
			if(iterator.hasNext()) {
				builder.append(VALUE_DELIMITER);
			}
		}
		builder.append(LINE_DELIMITER);
		//
		return builder.toString();
	}

	private static final String clean(String value) {

		return value.replace(VALUE_DELIMITER, "");
	}
}