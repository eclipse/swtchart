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
package org.eclipse.swtchart.extensions.linecharts;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;

public class LineChart extends ScrollableChart {

	private static final int DISPLAY_WIDTH = Display.getDefault().getClientArea().width;
	//
	public static final String COMPRESSION_EXTREME = "Extreme";
	public static final String COMPRESSION_HIGH = "High";
	public static final String COMPRESSION_MEDIUM = "Medium";
	public static final String COMPRESSION_LOW = "Low";
	public static final String COMPRESSION_NONE = "None";
	public static final String COMPRESSION_AUTO = "Auto";
	/*
	 * The compression number is dependent on the display width.
	 */
	public static final int EXTREME_COMPRESSION = DISPLAY_WIDTH;
	public static final int HIGH_COMPRESSION = DISPLAY_WIDTH * 2;
	public static final int MEDIUM_COMPRESSION = DISPLAY_WIDTH * 5;
	public static final int LOW_COMPRESSION = DISPLAY_WIDTH * 10;
	public static final int NO_COMPRESSION = Integer.MAX_VALUE;

	public LineChart() {
		super();
	}

	public LineChart(Composite parent, int style) {
		super(parent, style);
	}

	public void addSeriesData(List<ILineSeriesData> lineSeriesDataList) {

		addSeriesData(lineSeriesDataList, NO_COMPRESSION);
	}

	/**
	 * The data is compressed to the given length.
	 * If you're unsure which length to set, then use one of the following variables:
	 * 
	 * HIGH_COMPRESSION
	 * MEDIUM_COMPRESSION
	 * LOW_COMPRESSION
	 * NO_COMPRESSION
	 * 
	 * @param lineSeriesDataList
	 * @param compressToLength
	 */
	public void addSeriesData(List<ILineSeriesData> lineSeriesDataList, int compressToLength) {

		/*
		 * Suspend the update when adding new data to improve the performance.
		 */
		if(lineSeriesDataList != null && lineSeriesDataList.size() > 0) {
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
					ILineSeries lineSeries = (ILineSeries)createSeries(optimizedSeriesData, lineSeriesSettings);
					baseChart.applyLineSeriesSettings(lineSeries, lineSeriesSettings);
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
