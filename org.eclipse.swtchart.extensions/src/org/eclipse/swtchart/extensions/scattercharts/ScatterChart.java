/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.scattercharts;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;

public class ScatterChart extends ScrollableChart {

	public ScatterChart() {

		super();
		setData("org.eclipse.e4.ui.css.CssClassName", "ScatterChart");
	}

	public ScatterChart(Composite parent, int style) {

		super(parent, style);
		setData("org.eclipse.e4.ui.css.CssClassName", "ScatterChart");
	}

	public void addSeriesData(List<IScatterSeriesData> scatterSeriesDataList) {

		/*
		 * Suspend the update when adding new data to improve the performance.
		 */
		if(scatterSeriesDataList != null && !scatterSeriesDataList.isEmpty()) {
			/*
			 * Set the data.
			 */
			BaseChart baseChart = getBaseChart();
			baseChart.suspendUpdate(true);
			for(IScatterSeriesData scatterSeriesData : scatterSeriesDataList) {
				/*
				 * Get the series data and apply the settings.
				 */
				try {
					ISeriesData seriesData = scatterSeriesData.getSeriesData();
					ISeriesData optimizedSeriesData = calculateSeries(seriesData, ScrollableChart.NO_COMPRESS_TO_LENGTH);
					IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getSettings();
					scatterSeriesSettings.getSeriesSettingsHighlight(); // Initialize
					ILineSeries<?> scatterSeries = (ILineSeries<?>)createSeries(optimizedSeriesData, scatterSeriesSettings);
					baseChart.applySeriesSettings(scatterSeries, scatterSeriesSettings);
				} catch(SeriesException e) {
					//
				}
			}
			baseChart.suspendUpdate(false);
			adjustRange(true);
			baseChart.redraw();
		}
	}
}