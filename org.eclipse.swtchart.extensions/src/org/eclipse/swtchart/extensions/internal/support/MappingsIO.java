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
package org.eclipse.swtchart.extensions.internal.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.MappingsType;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;

public class MappingsIO {

	private static final String DELIMITER = "\t";

	public static Map<String, ISeriesSettings> importSettings(File file) {

		Map<String, ISeriesSettings> mappings = new HashMap<>();
		//
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line = null;
			while((line = bufferedReader.readLine()) != null) {
				/*
				 * 12 items =>
				 * mapping, key, ...
				 */
				String[] values = line.split(DELIMITER);
				if(values.length == 12) {
					ISeriesSettings seriesSettings = MappingsSupport.createSeriesSettings(values[0]);
					if(seriesSettings != null) {
						String key = values[1];
						//
						seriesSettings.setDescription(values[2]);
						seriesSettings.setVisible(Boolean.parseBoolean(values[3]));
						seriesSettings.setVisibleInLegend(Boolean.parseBoolean(values[4]));
						MappingsSupport.setColor(seriesSettings, ResourceSupport.getColor(values[5]));
						MappingsSupport.setEnableArea(seriesSettings, Boolean.parseBoolean(values[6]));
						//
						ISeriesSettings seriesSettingsHighlight = seriesSettings.getSeriesSettingsHighlight();
						seriesSettingsHighlight.setDescription(values[7]);
						seriesSettingsHighlight.setVisible(Boolean.parseBoolean(values[8]));
						seriesSettingsHighlight.setVisibleInLegend(Boolean.parseBoolean(values[9]));
						MappingsSupport.setColor(seriesSettingsHighlight, ResourceSupport.getColor(values[10]));
						MappingsSupport.setEnableArea(seriesSettingsHighlight, Boolean.parseBoolean(values[11]));
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
				MappingsType mappingsType = MappingsSupport.getMappingsType(seriesSettings);
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

	private static void exportSeriesSetting(PrintWriter printWriter, ISeriesSettings seriesSetting) {

		printWriter.print(seriesSetting.getDescription());
		printWriter.print(DELIMITER);
		printWriter.print(seriesSetting.isVisible());
		printWriter.print(DELIMITER);
		printWriter.print(seriesSetting.isVisibleInLegend());
		printWriter.print(DELIMITER);
		printWriter.print(ResourceSupport.getColor(MappingsSupport.getColor(seriesSetting)));
		printWriter.print(DELIMITER);
		printWriter.print(isEnableArea(seriesSetting));
	}

	private static boolean isEnableArea(ISeriesSettings seriesSetting) {

		if(seriesSetting instanceof ILineSeriesSettings) {
			ILineSeriesSettings lineSeriesSettings = (ILineSeriesSettings)seriesSetting;
			return lineSeriesSettings.isEnableArea();
		} else {
			return false;
		}
	}
}