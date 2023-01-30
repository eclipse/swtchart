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
import java.util.regex.Pattern;

import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;

/*
 * Only static methods are used.
 */
public class SeriesMapper {

	private static final Map<MappingsKey, ISeriesSettings> MAPPINGS = new HashMap<>();

	private SeriesMapper() {

	}

	public static void clear() {

		MAPPINGS.clear();
	}

	/**
	 * Removes the mapping identified by the id.
	 * 
	 * @param mappingsType
	 * @param id
	 */
	public static void remove(MappingsKey mappingsKey) {

		MAPPINGS.remove(mappingsKey);
	}

	public static void put(MappingsKey mappingsKey, ISeriesSettings seriesSettings) {

		if(seriesSettings != null) {
			MAPPINGS.put(mappingsKey, seriesSettings);
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

		ISeriesSettings seriesSettingsMapped = null;
		//
		if(baseChart != null) {
			String id = series.getId();
			ISeriesSettings seriesSettings = baseChart.getSeriesSettings(id);
			MappingsType mappingsType = MappingsSupport.getMappingsType(seriesSettings);
			if(isValidMappingsType(mappingsType)) {
				/*
				 * Try to match the id.
				 */
				MappingsKey mappingsKey = new MappingsKey(mappingsType, id);
				seriesSettingsMapped = MAPPINGS.get(mappingsKey);
				if(seriesSettingsMapped == null) {
					MappingsKey mappingsKeyX = getKeyViaRegex(mappingsKey);
					if(mappingsKeyX != null) {
						seriesSettingsMapped = MAPPINGS.get(mappingsKeyX);
					}
				}
			}
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
			if(isValidMappingsType(mappingsType)) {
				put(new MappingsKey(mappingsType, id), seriesSettingsCopy);
			}
		}
	}

	/**
	 * Remove the mapping of the given series.
	 * 
	 * @param series
	 * @param baseChart
	 */
	public static void unmap(ISeries<?> series, BaseChart baseChart) {

		String seriesId = series.getId();
		ISeriesSettings seriesSettings = baseChart.getSeriesSettings(seriesId);
		MappingsType mappingsType = MappingsSupport.getMappingsType(seriesSettings);
		remove(new MappingsKey(mappingsType, seriesId));
		baseChart.resetSeriesSettings(series);
	}

	/**
	 * Returns the mapped series settings.
	 * 
	 * @return List
	 */
	public static List<MappedSeriesSettings> getMappings() {

		List<MappedSeriesSettings> mappings = new ArrayList<>();
		for(Map.Entry<MappingsKey, ISeriesSettings> entry : MAPPINGS.entrySet()) {
			MappingsKey mappingsKey = entry.getKey();
			MappingsType mappingsType = mappingsKey.getMappingsType();
			if(!MappingsType.NONE.equals(mappingsType)) {
				String id = mappingsKey.getId();
				ISeriesSettings seriesSettings = entry.getValue();
				mappings.add(new MappedSeriesSettings(mappingsType, id, seriesSettings));
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
			MappingsKey mappingsKey = new MappingsKey(mappingsType, id);
			if(MAPPINGS.containsKey(mappingsKey)) {
				baseChart.applySeriesSettings(series, seriesSettings);
			} else {
				/*
				 * Matched via Regex
				 */
				MappingsKey mappingsKeyX = getKeyViaRegex(mappingsKey);
				if(mappingsKeyX != null) {
					baseChart.applySeriesSettings(series, seriesSettings);
				}
			}
		}
	}

	private static boolean isValidMappingsType(MappingsType mappingsType) {

		return !MappingsType.NONE.equals(mappingsType);
	}

	private static MappingsKey getKeyViaRegex(MappingsKey mappingsKey) {

		/*
		 * Alternatively, match via a regular expression.
		 */
		for(MappingsKey mappingsKeyStored : MAPPINGS.keySet()) {
			try {
				if(!MappingsType.NONE.equals(mappingsKeyStored.getMappingsType())) {
					if(mappingsKeyStored.getMappingsType().equals(mappingsKey.getMappingsType())) {
						String regularExpression = mappingsKeyStored.getId();
						if(!regularExpression.isEmpty()) {
							String id = mappingsKey.getId();
							Pattern.compile(regularExpression);
							if(id.matches(regularExpression)) {
								return mappingsKeyStored;
							}
						}
					}
				}
			} catch(Exception e) {
				// Not a valid regular expression.
			}
		}
		//
		return null;
	}
}