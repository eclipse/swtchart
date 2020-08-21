/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.core;

import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;

public interface IScrollableChart {

	/**
	 * Usually, instanceof should be used to check if the chart is of type LineChart, BarChart, ... .
	 * A scrollable chart could be also used directly to display line series , bar series. Hence, the
	 * chart type gives a hint when it is needed e.g. for export purposes.
	 * 
	 * @return {ChartType}
	 */
	ChartType getChartType();

	IChartSettings getChartSettings();

	void applySettings(IChartSettings chartSettings);

	BaseChart getBaseChart();

	default ISeries<?> addSeries(IChartSeriesData chartSeriesData) throws SeriesException {

		ISeriesData seriesData = chartSeriesData.getSeriesData();
		ISeriesSettings seriesSettings = chartSeriesData.getSettings();
		BaseChart baseChart = getBaseChart();
		ISeries<?> series = baseChart.createSeries(seriesData, seriesSettings);
		baseChart.applySeriesSettings(series, seriesSettings);
		return series;
	}

	/**
	 * Delete all series.
	 */
	void deleteSeries();

	/**
	 * Sets the range, based on the range coordinates.
	 * It's only possible to set the range for the primary axes as
	 * the range for secondary axes is calculated dynamically.
	 * 
	 * Use: IExtendedChart.X_AXIS or IExtendedChart.Y_AXIS.
	 * 
	 * @param axis
	 * @param start
	 * @param stop
	 * @param adjustMinMax
	 */
	void setRange(String axis, Range range);
}
