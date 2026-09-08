/*******************************************************************************
 * Copyright (c) 2017, 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.linecharts;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.ISeriesDataSupplier;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;

public class LineChart extends ScrollableChart implements ICompressionSupport {

	/*
	 * Experimentally try to use a slice supplier.
	 */
	private boolean useSliceDataSupplier = false;

	public LineChart() {

		super();
	}

	public LineChart(Composite parent, int style) {

		super(parent, style);
	}

	public void addSeriesData(List<ILineSeriesData> lineSeriesDataList) {

		addSeriesData(lineSeriesDataList, NO_COMPRESSION);
	}

	public void addSeriesData(List<ILineSeriesData> lineSeriesDataList, int compressToLength) {

		if(useSliceDataSupplier) {
			addSeriesDataSlice(lineSeriesDataList);
		} else {
			addSeriesDataClassic(lineSeriesDataList, compressToLength);
		}
	}

	/**
	 * The data is compressed to the given length.
	 * If you're unsure which length to set, then use one of the following variables:
	 *
	 * EXTREME_COMPRESSION
	 * HIGH_COMPRESSION
	 * MEDIUM_COMPRESSION
	 * LOW_COMPRESSION
	 * NO_COMPRESSION
	 *
	 * @param lineSeriesDataList
	 * @param compressToLength
	 */
	private void addSeriesDataClassic(List<ILineSeriesData> lineSeriesDataList, int compressToLength) {

		/*
		 * Suspend the update when adding new data to improve the performance.
		 */
		if(lineSeriesDataList != null && !lineSeriesDataList.isEmpty()) {
			BaseChart baseChart = getBaseChart();
			baseChart.suspendUpdate(true);
			for(ILineSeriesData lineSeriesData : lineSeriesDataList) {
				/*
				 * Get the series data and apply the settings.
				 */
				try {
					ISeriesData seriesData = lineSeriesData.getSeriesData();
					ISeriesData optimizedSeriesData = calculateSeries(seriesData, compressToLength);
					ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
					lineSeriesSettings.getSeriesSettingsHighlight(); // Initialize
					ILineSeries<?> lineSeries = (ILineSeries<?>)createSeries(optimizedSeriesData, lineSeriesSettings);
					baseChart.applySeriesSettings(lineSeries, lineSeriesSettings);
				} catch(SeriesException e) {

				}
			}
			baseChart.suspendUpdate(false);
			adjustRange(true);
			baseChart.redraw();
		}
	}

	/*
	 * Experimental
	 */
	private void addSeriesDataSlice(List<ILineSeriesData> lineSeriesDataList) {

		if(lineSeriesDataList != null && !lineSeriesDataList.isEmpty()) {
			BaseChart baseChart = getBaseChart();
			baseChart.suspendUpdate(true);
			for(ILineSeriesData lineSeriesData : lineSeriesDataList) {
				try {
					ISeriesData seriesData = lineSeriesData.getSeriesData();
					ISeriesDataSupplier seriesDataSupplier = createSupplier(seriesData.getXSeries(), seriesData.getYSeries());
					ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
					lineSeriesSettings.getSeriesSettingsHighlight(); // Initialize
					ILineSeries<?> lineSeries = (ILineSeries<?>)createSeries(seriesData, lineSeriesSettings);
					baseChart.applySeriesSettings(lineSeries, lineSeriesSettings);
					putSeriesDataSupplier(seriesData.getId(), seriesDataSupplier);
				} catch(SeriesException e) {

				}
			}
			baseChart.suspendUpdate(false);
			adjustRange(true);
			baseChart.redraw();
		}
	}

	/*
	 * Experimental
	 */
	private static ISeriesDataSupplier createSupplier(double[] xSeries, double[] ySeries) {

		return (seriesId, xMin, xMax, maxPoints) -> {
			/*
			 * Validation
			 */
			int start = ceilIndex(xSeries, xMin);
			int end = floorIndex(xSeries, xMax);
			if(start < 0 || end < 0 || start > end) {
				/*
				 * No data available.
				 */
				return null;
			}
			/*
			 * Slice
			 */
			int sliceLength = end - start + 1;
			if(sliceLength <= maxPoints) {
				/*
				 * The visible range is small enough to show every raw point.
				 */
				double[] x = new double[sliceLength];
				double[] y = new double[sliceLength];
				System.arraycopy(xSeries, start, x, 0, sliceLength);
				System.arraycopy(ySeries, start, y, 0, sliceLength);
				return new SeriesData(x, y, seriesId);
			}
			/*
			 * Sub-sample so we return at most maxPoints entries.
			 */
			int step = sliceLength / maxPoints;
			int count = sliceLength / step;
			double[] x = new double[count];
			double[] y = new double[count];
			for(int i = 0; i < count; i++) {
				int idx = start + i * step;
				x[i] = xSeries[idx];
				y[i] = ySeries[idx];
			}

			return new SeriesData(x, y, seriesId);
		};
	}

	private static int ceilIndex(double[] sortedArray, double value) {

		int result = Arrays.binarySearch(sortedArray, value);
		return result >= 0 ? result : -(result + 1);
	}

	private static int floorIndex(double[] sortedArray, double value) {

		int result = Arrays.binarySearch(sortedArray, value);
		return result >= 0 ? result : -(result + 2);
	}
}