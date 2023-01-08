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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.MappedSeriesSettings;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.SeriesMapper;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;

public class MappingsIO {

	public static final String VERSION_IDENTIFIER = "Version=";
	public static final String LINE_DELIMITER = "\r\n";
	public static final String VALUE_DELIMITER = "\t";

	public static Map<String, ISeriesSettings> readSettings(String content) {

		Map<String, ISeriesSettings> mappings = new HashMap<>();
		String[] lines = content.split(LINE_DELIMITER);
		if(lines.length > 0) {
			String version = getVersion(lines[0]);
			switch(version) {
				case Mappings_v1000.VERSION_NUMBER:
					Mappings_v1000 mappingsIO = new Mappings_v1000();
					mappings.putAll(mappingsIO.readSettings(lines));
					break;
				default:
					break;
			}
		}
		//
		return mappings;
	}

	public static String saveSettings(List<MappedSeriesSettings> mappings) {

		/*
		 * Always use the latest version to save the settings.
		 */
		Mappings_v1000 mappingsIO = new Mappings_v1000();
		return mappingsIO.saveSettings(mappings);
	}

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

	protected static void appendVersion(StringBuilder builder, String version) {

		builder.append(MappingsIO.VERSION_IDENTIFIER);
		builder.append(version);
		builder.append(LINE_DELIMITER);
	}

	private static String getVersion(String line) {

		String version;
		String[] parts = line.trim().split("=");
		if(parts.length == 2) {
			version = parts[1].trim();
		} else {
			version = Mappings_v1000.VERSION_NUMBER;
		}
		//
		return version;
	}
}