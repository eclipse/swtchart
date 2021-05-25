/*******************************************************************************
 * Copyright (c) 2020, 2021 Lablicate GmbH.
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public class SeriesMapper {

	/*
	 * The mappings contains the changes series settings.
	 * The defaults contains the initial series settings.
	 */
	private static final Map<String, ISeriesSettings> DEFAULTS = new HashMap<>();
	private static final Map<String, ISeriesSettings> MAPPINGS = new HashMap<>();

	// Only static methods are used.
	private SeriesMapper() {

	}

	/**
	 * Clears all defaults and mappings.
	 */
	public static void clearAll() {

		DEFAULTS.clear();
		MAPPINGS.clear();
	}

	/**
	 * Clears all mappings.
	 */
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
	 * Put the instance to the default map.
	 * 
	 * @param key
	 * @param seriesSettings
	 */
	public static void putDefault(String key, ISeriesSettings seriesSettings) {

		if(key != null && seriesSettings != null) {
			DEFAULTS.put(key, seriesSettings);
		}
	}

	/**
	 * Put the instance to the settings map.
	 * 
	 * @param key
	 * @param seriesSettings
	 */
	public static void put(String key, ISeriesSettings seriesSettings) {

		if(key != null && seriesSettings != null) {
			MAPPINGS.put(key, seriesSettings);
		}
	}

	/**
	 * Resets the given id.
	 * 
	 * @param id
	 */
	public static void reset(String id) {

		remove(id);
		ISeriesSettings seriesSettingsDefault = getSeriesSettingsDefault(id);
		if(seriesSettingsDefault != null) {
			put(id, MappingsSupport.copySeriesSettings(seriesSettingsDefault));
		}
	}

	/**
	 * Clears the settings and replaces existing settings by their defaults.
	 */
	public static void reset() {

		clear();
		/*
		 * Transfer the default settings.
		 */
		for(Map.Entry<String, ISeriesSettings> mapping : DEFAULTS.entrySet()) {
			ISeriesSettings seriesSettingsSource = mapping.getValue();
			put(mapping.getKey(), MappingsSupport.copySeriesSettings(seriesSettingsSource));
		}
	}

	/**
	 * Returns an unmodifiable set of the mappings.
	 * 
	 * @return {@link Set}
	 */
	public static Set<Map.Entry<String, ISeriesSettings>> getMappings() {

		return Collections.unmodifiableSet(MAPPINGS.entrySet());
	}

	/**
	 * Contains the default setting.
	 * 
	 * @param id
	 * @return boolean
	 */
	public static boolean containsDefaultSeriesSetting(String id) {

		return DEFAULTS.containsKey(id);
	}

	/**
	 * Returns the default series setting.
	 * 
	 * @param id
	 * @return {@link ISeriesSettings}
	 */
	public static ISeriesSettings getSeriesSettingsDefault(String id) {

		return DEFAULTS.get(id);
	}

	/**
	 * Contains the mapped setting.
	 * 
	 * @param id
	 * @return boolean
	 */
	public static boolean containsMappedSeriesSetting(String id) {

		return MAPPINGS.containsKey(id);
	}

	/**
	 * Returns the mapped series setting.
	 * 
	 * @param id
	 * @return {@link ISeriesSettings}
	 */
	public static ISeriesSettings getSeriesSettingsMapped(String id) {

		return MAPPINGS.get(id);
	}

	/**
	 * Maps the settings.
	 * If the default setting is null, it will be not mapped.
	 * 
	 * @param id
	 * @param seriesSettings
	 * @param seriesSettingsDefault
	 */
	public static void mapSetting(String id, ISeriesSettings seriesSettings, ISeriesSettings seriesSettingsDefault) {

		if(seriesSettings != null) {
			put(id, seriesSettings);
			if(!containsDefaultSeriesSetting(id) && seriesSettingsDefault != null) {
				putDefault(id, seriesSettingsDefault);
			}
		}
	}

	/**
	 * If the default is not available yet, a copy will be created.
	 * Null might be returned.
	 * 
	 * @param id
	 * @param scrollableChart
	 * @return {@link ISeriesSettings}
	 */
	public static ISeriesSettings getSeriesSettingsDefault(String id, ScrollableChart scrollableChart) {

		ISeriesSettings seriesSettingsDefault = null;
		//
		if(SeriesMapper.containsDefaultSeriesSetting(id)) {
			seriesSettingsDefault = getSeriesSettingsDefault(id);
		} else {
			if(scrollableChart != null) {
				BaseChart baseChart = scrollableChart.getBaseChart();
				ISeriesSettings seriesSettings = baseChart.getSeriesSettings(id);
				if(seriesSettings != null) {
					seriesSettingsDefault = MappingsSupport.copySeriesSettings(seriesSettings);
				}
			}
		}
		//
		return seriesSettingsDefault;
	}
}
