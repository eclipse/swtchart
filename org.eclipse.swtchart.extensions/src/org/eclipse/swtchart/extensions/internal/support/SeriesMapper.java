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

import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;

public class SeriesMapper {

	private static final Map<String, ISeriesSettings> MAPPINGS = new HashMap<>();
	private BaseChart baseChart;

	public SeriesMapper(BaseChart baseChart) {

		this.baseChart = baseChart;
	}

	public static void clear() {

		MAPPINGS.clear();
	}

	public static void remove(String key) {

		MAPPINGS.remove(key);
	}

	public static void put(String key, ISeriesSettings seriesSettings) {

		MAPPINGS.put(key, seriesSettings);
	}

	public static Set<Map.Entry<String, ISeriesSettings>> getMappings() {

		return Collections.unmodifiableSet(MAPPINGS.entrySet());
	}

	public void mapSettings(ISeries<?>[] seriesArray) {

		if(baseChart != null) {
			for(ISeries<?> series : seriesArray) {
				String id = series.getId();
				ISeriesSettings seriesSettings = baseChart.getSeriesSettings(id);
				if(seriesSettings != null) {
					/*
					 * Fetch the modified series settings
					 */
					seriesSettings.setVisible(series.isVisible());
					seriesSettings.setVisibleInLegend(series.isVisibleInLegend());
					adjustColor(series, seriesSettings);
					seriesSettings.setDescription(series.getDescription());
					/*
					 * Store the settings
					 */
					MAPPINGS.put(id, seriesSettings);
				}
			}
		}
	}

	public void adjustSettings(ISeries<?>[] seriesArray) {

		if(baseChart != null) {
			for(ISeries<?> series : seriesArray) {
				String id = series.getId();
				ISeriesSettings settingsBase = baseChart.getSeriesSettings(id);
				ISeriesSettings settingsAdjusted = MAPPINGS.get(id);
				if(settingsBase != null && settingsAdjusted != null) {
					/*
					 * Adjust the settings displayed in the legend dialog.
					 */
					settingsBase.setVisible(settingsAdjusted.isVisible());
					settingsBase.setVisibleInLegend(settingsAdjusted.isVisibleInLegend());
					adjustColor(settingsBase, settingsAdjusted);
					settingsBase.setDescription(settingsAdjusted.getDescription());
					/*
					 * Apply the settings
					 */
					baseChart.applySeriesSettings(series, settingsBase);
				}
			}
			//
			baseChart.redraw();
		}
	}

	private void adjustColor(ISeries<?> series, ISeriesSettings seriesSettings) {

		if(series instanceof IBarSeries<?> && seriesSettings instanceof IBarSeriesSettings) {
			IBarSeries<?> barSeries = (IBarSeries<?>)series;
			IBarSeriesSettings barSeriesSettings = (IBarSeriesSettings)seriesSettings;
			barSeriesSettings.setBarColor(barSeries.getBarColor());
		} else if(series instanceof ILineSeries<?> && seriesSettings instanceof ILineSeriesSettings) {
			ILineSeries<?> lineSeries = (ILineSeries<?>)series;
			ILineSeriesSettings lineSeriesSettings = (ILineSeriesSettings)seriesSettings;
			lineSeriesSettings.setLineColor(lineSeries.getLineColor());
		}
	}

	private void adjustColor(ISeriesSettings settingsBase, ISeriesSettings settingsAdjusted) {

		if(settingsBase instanceof IBarSeriesSettings && settingsAdjusted instanceof IBarSeriesSettings) {
			IBarSeriesSettings barSettingsBase = (IBarSeriesSettings)settingsBase;
			IBarSeriesSettings barSettingsAdjusted = (IBarSeriesSettings)settingsAdjusted;
			barSettingsBase.setBarColor(barSettingsAdjusted.getBarColor());
		} else if(settingsBase instanceof ILineSeriesSettings && settingsAdjusted instanceof ILineSeriesSettings) {
			ILineSeriesSettings lineSettingsBase = (ILineSeriesSettings)settingsBase;
			ILineSeriesSettings lineSettingAdjusted = (ILineSeriesSettings)settingsAdjusted;
			lineSettingsBase.setLineColor(lineSettingAdjusted.getLineColor());
		}
	}
}
