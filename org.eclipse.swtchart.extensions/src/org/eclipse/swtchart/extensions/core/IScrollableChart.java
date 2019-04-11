/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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

	IChartSettings getChartSettings();

	void applySettings(IChartSettings chartSettings);

	BaseChart getBaseChart();

	default ISeries addSeries(IChartSeriesData chartSeriesData) throws SeriesException {

		ISeriesData seriesData = chartSeriesData.getSeriesData();
		ISeriesSettings seriesSettings = chartSeriesData.getSettings();
		BaseChart baseChart = getBaseChart();
		ISeries series = baseChart.createSeries(seriesData, seriesSettings);
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
