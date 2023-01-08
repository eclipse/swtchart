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
package org.eclipse.swtchart.extensions.internal.mappings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;

public class Mappings_v1000 {

	public static final String VERSION_NUMBER = "v1000";

	public Map<String, ISeriesSettings> readSettings(String[] lines) {

		Map<String, ISeriesSettings> mappings = new HashMap<>();
		for(String line : lines) {
			try {
				if(!line.startsWith(MappingsIO.VERSION_IDENTIFIER)) {
					String[] values = line.split(MappingsIO.VALUE_DELIMITER);
					importMapping(values, mappings);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		//
		return mappings;
	}

	public String saveSettings(List<MappedSeriesSettings> mappings) {

		StringBuilder builder = new StringBuilder();
		MappingsIO.appendVersion(builder, VERSION_NUMBER);
		//
		for(MappedSeriesSettings mapping : mappings) {
			/*
			 * Always use the latest version to save the settings.
			 */
			List<Object> values = new ArrayList<>();
			exportMapping(values, mapping);
			builder.append(create(values));
		}
		//
		return builder.toString();
	}

	/*
	 * 2 - Header
	 * 4 - Series Settings
	 * . - dynamic specific settings
	 */
	private void exportMapping(List<Object> values, MappedSeriesSettings mapping) {

		ISeriesSettings seriesSettings = mapping.getSeriesSettings();
		values.add(mapping.getMappingsType());
		values.add(clean(mapping.getIdentifier()));
		exportSeriesSetting(values, seriesSettings);
		exportSeriesSetting(values, seriesSettings.getSeriesSettingsHighlight());
	}

	private void importMapping(String[] values, Map<String, ISeriesSettings> mappings) {

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

	private void exportSeriesSetting(List<Object> values, ISeriesSettings seriesSettings) {

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

	private int importSeriesSetting(String[] values, int index, ISeriesSettings seriesSettings) {

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

	private void exportBarSeriesSetting(List<Object> values, IBarSeriesSettings barSeriesSettings) {

		values.add(ResourceSupport.getColor(barSeriesSettings.getBarColor()));
		values.add(barSeriesSettings.getBarPadding());
		values.add(barSeriesSettings.getBarWidth());
		values.add(barSeriesSettings.isBarOverlay());
		values.add(barSeriesSettings.getBarWidthStyle().name());
		values.add(barSeriesSettings.isEnableStack());
	}

	private int importBarSeriesSetting(String[] values, int index, IBarSeriesSettings barSeriesSettings) {

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

	private void exportCircularSeriesSetting(List<Object> values, ICircularSeriesSettings circularSeriesSettings) {

		values.add(ResourceSupport.getColor(circularSeriesSettings.getSliceColor()));
		values.add(ResourceSupport.getColor(circularSeriesSettings.getBorderColor()));
		values.add(circularSeriesSettings.getBorderWidth());
		values.add(circularSeriesSettings.getBorderStyle().name());
	}

	private int importCircularSeriesSetting(String[] values, int index, ICircularSeriesSettings circularSeriesSettings) {

		if(values.length >= (index + 4)) {
			circularSeriesSettings.setSliceColor(ResourceSupport.getColor(values[index++]));
			circularSeriesSettings.setBorderColor(ResourceSupport.getColor(values[index++]));
			circularSeriesSettings.setBorderWidth(Integer.parseInt(values[index++]));
			circularSeriesSettings.setBorderStyle(LineStyle.valueOf(values[index++]));
		}
		//
		return index;
	}

	private void exportLineSeriesSetting(List<Object> values, ILineSeriesSettings lineSeriesSettings) {

		exportPointSeriesSetting(values, lineSeriesSettings);
		values.add(lineSeriesSettings.getLineStyle().name());
		values.add(lineSeriesSettings.getLineWidth());
		values.add(ResourceSupport.getColor(lineSeriesSettings.getLineColor()));
		values.add(lineSeriesSettings.getAntialias());
		values.add(lineSeriesSettings.isEnableArea());
		values.add(lineSeriesSettings.isEnableStack());
		values.add(lineSeriesSettings.isEnableStep());
	}

	private int importLineSeriesSetting(String[] values, int index, ILineSeriesSettings lineSeriesSettings) {

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

	private void exportScatterSeriesSetting(List<Object> values, IScatterSeriesSettings scatterSeriesSettings) {

		exportPointSeriesSetting(values, scatterSeriesSettings);
	}

	private int importScatterSeriesSetting(String[] values, int index, IScatterSeriesSettings scatterSeriesSettings) {

		index = importPointSeriesSetting(values, index, scatterSeriesSettings);
		//
		return index;
	}

	private void exportPointSeriesSetting(List<Object> values, IPointSeriesSettings pointSeriesSettings) {

		values.add(pointSeriesSettings.getSymbolType().name());
		values.add(pointSeriesSettings.getSymbolSize());
		values.add(ResourceSupport.getColor(pointSeriesSettings.getSymbolColor()));
	}

	private int importPointSeriesSetting(String[] values, int index, IPointSeriesSettings pointSeriesSettings) {

		if(values.length >= (index + 3)) {
			pointSeriesSettings.setSymbolType(PlotSymbolType.valueOf(values[index++]));
			pointSeriesSettings.setSymbolSize(Integer.valueOf(values[index++]));
			pointSeriesSettings.setSymbolColor(ResourceSupport.getColor(values[index++]));
		}
		//
		return index;
	}

	private String create(List<Object> values) {

		StringBuilder builder = new StringBuilder();
		Iterator<Object> iterator = values.iterator();
		while(iterator.hasNext()) {
			builder.append(iterator.next());
			if(iterator.hasNext()) {
				builder.append(MappingsIO.VALUE_DELIMITER);
			}
		}
		builder.append(MappingsIO.LINE_DELIMITER);
		//
		return builder.toString();
	}

	private final String clean(String value) {

		return value.replace(MappingsIO.VALUE_DELIMITER, "");
	}
}