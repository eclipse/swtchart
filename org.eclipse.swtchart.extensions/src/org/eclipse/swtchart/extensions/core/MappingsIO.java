/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesSettings;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesSettings;

public class MappingsIO {

	private static final String DELIMITER = "\t";

	public static void transferSettings(ISeriesSettings seriesSettingsSource, ISeriesSettings seriesSettingsSink) {

		if(seriesSettingsSource != null && seriesSettingsSink != null) {
			seriesSettingsSink.setDescription(seriesSettingsSource.getDescription());
			seriesSettingsSink.setVisible(seriesSettingsSource.isVisible());
			seriesSettingsSink.setVisibleInLegend(seriesSettingsSource.isVisible());
			setColor(seriesSettingsSink, getColor(seriesSettingsSource));
		}
	}

	public static Map<String, ISeriesSettings> importSettings(File file) {

		Map<String, ISeriesSettings> mappings = new HashMap<>();
		//
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line = null;
			while((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(DELIMITER);
				if(values.length == 10) {
					ISeriesSettings seriesSettings = getSeriesSettings(values[0]);
					if(seriesSettings != null) {
						String key = values[1];
						//
						seriesSettings.setDescription(values[2]);
						seriesSettings.setVisible(Boolean.parseBoolean(values[3]));
						seriesSettings.setVisibleInLegend(Boolean.parseBoolean(values[4]));
						setColor(seriesSettings, ResourceSupport.getColor(values[5]));
						//
						ISeriesSettings seriesSettingsHighlight = seriesSettings.getSeriesSettingsHighlight();
						seriesSettingsHighlight.setDescription(values[6]);
						seriesSettingsHighlight.setVisible(Boolean.parseBoolean(values[7]));
						seriesSettingsHighlight.setVisibleInLegend(Boolean.parseBoolean(values[8]));
						setColor(seriesSettingsHighlight, ResourceSupport.getColor(values[9]));
						//
						mappings.put(key, seriesSettings);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		//
		return mappings;
	}

	public static boolean exportSettings(File file, Set<Map.Entry<String, ISeriesSettings>> mappings) {

		boolean success = false;
		try (PrintWriter printWriter = new PrintWriter(file)) {
			for(Map.Entry<String, ISeriesSettings> mapping : mappings) {
				/*
				 * Type
				 */
				ISeriesSettings seriesSettings = mapping.getValue();
				MappingsType mappingsType = getMappingsType(seriesSettings);
				//
				if(!MappingsType.NONE.equals(mappingsType)) {
					printWriter.print(mappingsType.name());
					printWriter.print(DELIMITER);
					printWriter.print(mapping.getKey());
					printWriter.print(DELIMITER);
					exportSeriesSetting(printWriter, seriesSettings);
					printWriter.print(DELIMITER);
					exportSeriesSetting(printWriter, seriesSettings.getSeriesSettingsHighlight());
					printWriter.println();
				}
			}
			success = true;
		} catch(Exception e1) {
			e1.printStackTrace();
		}
		//
		return success;
	}

	private static MappingsType getMappingsType(ISeriesSettings seriesSettings) {

		if(seriesSettings instanceof IBarSeriesSettings) {
			return MappingsType.BAR;
		} else if(seriesSettings instanceof ILineSeriesSettings) {
			return MappingsType.LINE;
		} else if(seriesSettings instanceof IScatterSeriesSettings) {
			return MappingsType.SCATTER;
		} else {
			return MappingsType.NONE;
		}
	}

	private static ISeriesSettings getSeriesSettings(String name) {

		ISeriesSettings seriesSettings = null;
		try {
			MappingsType mappingsType = MappingsType.valueOf(name);
			switch(mappingsType) {
				case BAR:
					seriesSettings = new BarSeriesSettings();
					break;
				case LINE:
					seriesSettings = new LineSeriesSettings();
					break;
				case SCATTER:
					seriesSettings = new ScatterSeriesSettings();
					break;
				default:
					seriesSettings = null;
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		//
		return seriesSettings;
	}

	private static void exportSeriesSetting(PrintWriter printWriter, ISeriesSettings seriesSetting) {

		printWriter.print(seriesSetting.getDescription());
		printWriter.print(DELIMITER);
		printWriter.print(seriesSetting.isVisible());
		printWriter.print(DELIMITER);
		printWriter.print(seriesSetting.isVisibleInLegend());
		printWriter.print(DELIMITER);
		printWriter.print(ResourceSupport.getColor(getColor(seriesSetting)));
	}

	private static Color getColor(ISeriesSettings seriesSettings) {

		if(seriesSettings instanceof IBarSeriesSettings) {
			IBarSeriesSettings barSeriesSettings = (IBarSeriesSettings)seriesSettings;
			return barSeriesSettings.getBarColor();
		} else if(seriesSettings instanceof ILineSeriesSettings) {
			ILineSeriesSettings lineSeriesSettings = (ILineSeriesSettings)seriesSettings;
			return lineSeriesSettings.getLineColor();
		} else {
			return ResourceSupport.getColorDefault();
		}
	}

	private static void setColor(ISeriesSettings seriesSettings, Color color) {

		if(color != null) {
			if(seriesSettings instanceof IBarSeriesSettings) {
				IBarSeriesSettings barSeriesSettings = (IBarSeriesSettings)seriesSettings;
				barSeriesSettings.setBarColor(color);
			} else if(seriesSettings instanceof ILineSeriesSettings) {
				ILineSeriesSettings lineSeriesSettings = (ILineSeriesSettings)seriesSettings;
				lineSeriesSettings.setLineColor(color);
			}
		}
	}
}