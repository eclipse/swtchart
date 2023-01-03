/*******************************************************************************
 * Copyright (c) 2020, 2023 Lablicate GmbH.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;

/*
 * Only static methods are used.
 */
public class SeriesMapper {

	private static final String KEY_DELIMITER = "_";
	private static final Map<String, ISeriesSettings> MAPPINGS = new HashMap<>();

	private SeriesMapper() {

	}

	public static String getKey(MappingsType mappingsType, String id) {

		return mappingsType.name() + KEY_DELIMITER + id;
	}

	public static void clear() {

		MAPPINGS.clear();
	}

	/**
	 * Removes the mapping identified by the key.
	 * 
	 * @param key
	 */
	public static void remove(String key) {

		MAPPINGS.remove(key);
	}

	/**
	 * Removes the mapping identified by the id.
	 * 
	 * @param mappingsType
	 * @param id
	 */
	public static void remove(MappingsType mappingsType, String id) {

		remove(getKey(mappingsType, id));
	}

	/**
	 * Put the instance to the settings map.
	 * 
	 * @param mappingsType
	 * @param id
	 * @param seriesSettings
	 */
	public static void put(MappingsType mappingsType, String id, ISeriesSettings seriesSettings) {

		if(id != null) {
			if(isValidMappingsType(mappingsType)) {
				put(getKey(mappingsType, id), seriesSettings);
			}
		}
	}

	public static void put(String key, ISeriesSettings seriesSettings) {

		if(seriesSettings != null) {
			MAPPINGS.put(key, seriesSettings);
		}
	}

	/**
	 * Returns the mapped series setting or null if none is available.
	 * 
	 * @param series
	 * @param baseChart
	 * @return {@link ISeriesSettings}
	 */
	public static ISeriesSettings get(ISeries<?> series, BaseChart baseChart) {

		String id = series.getId();
		ISeriesSettings seriesSettings = baseChart.getSeriesSettings(id);
		ISeriesSettings seriesSettingsMapped = null;
		//
		MappingsType mappingsType = MappingsSupport.getMappingsType(seriesSettings);
		if(isValidMappingsType(mappingsType)) {
			String key = getKey(mappingsType, id);
			seriesSettingsMapped = MAPPINGS.get(key);
		}
		//
		return seriesSettingsMapped;
	}

	/**
	 * Maps the settings.
	 * If the default setting is null, it will be not mapped.
	 * 
	 * @param series
	 * @param baseChart
	 */
	public static void map(ISeries<?> series, BaseChart baseChart) {

		String id = series.getId();
		ISeriesSettings seriesSettings = baseChart.getSeriesSettings(id);
		ISeriesSettings seriesSettingsCopy = MappingsSupport.copySettings(seriesSettings);
		//
		if(seriesSettingsCopy != null) {
			MappingsType mappingsType = MappingsSupport.getMappingsType(seriesSettings);
			put(mappingsType, id, seriesSettingsCopy);
		}
	}

	public static void unmap(ISeries<?> series, BaseChart baseChart) {

		String id = series.getId();
		ISeriesSettings seriesSettings = baseChart.getSeriesSettings(id);
		MappingsType mappingsType = MappingsSupport.getMappingsType(seriesSettings);
		remove(mappingsType, id);
		baseChart.resetSeriesSettings(series);
	}

	/**
	 * Returns the mapped series settings.
	 * 
	 * @return List
	 */
	public static List<MappedSeriesSettings> getMappings() {

		List<MappedSeriesSettings> mappings = new ArrayList<>();
		for(Map.Entry<String, ISeriesSettings> entry : MAPPINGS.entrySet()) {
			String key = entry.getKey();
			int index = key.indexOf(KEY_DELIMITER);
			if(index > 0) {
				String value = key.substring(0, index);
				MappingsType mappingsType = MappingsSupport.getMappingsType(value);
				if(!MappingsType.NONE.equals(mappingsType)) {
					String id = key.substring(index + 1, key.length());
					ISeriesSettings seriesSettings = entry.getValue();
					mappings.add(new MappedSeriesSettings(mappingsType, id, seriesSettings));
				}
			}
		}
		//
		return mappings;
	}

	/**
	 * Updates the base chart by mapped series.
	 * 
	 * @param baseChart
	 */
	public static void update(BaseChart baseChart) {

		ISeriesSet seriesSet = baseChart.getSeriesSet();
		for(ISeries<?> series : seriesSet.getSeries()) {
			String id = series.getId();
			ISeriesSettings seriesSettings = baseChart.getSeriesSettings(id);
			MappingsType mappingsType = MappingsSupport.getMappingsType(seriesSettings);
			String key = getKey(mappingsType, id);
			if(MAPPINGS.containsKey(key)) {
				baseChart.applySeriesSettings(series, seriesSettings);
			}
		}
	}

	private static boolean isValidMappingsType(MappingsType mappingsType) {

		return !MappingsType.NONE.equals(mappingsType);
	}
}