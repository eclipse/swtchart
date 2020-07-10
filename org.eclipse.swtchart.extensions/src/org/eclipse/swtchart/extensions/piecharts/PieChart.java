/*******************************************************************************
 * Copyright (c) 2020 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta Orignal API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.piecharts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;

public class PieChart extends ScrollableChart {

	public PieChart() {

		super();
	}

	public PieChart(Composite parent, int style) {

		super(parent, style);
	}

	public void addSeriesData(ICircularSeriesData model) {

		/*
		 * Suspend the update when adding new data to improve the performance.
		 */
		if(model != null && model.getRootNode() != null) {
			BaseChart baseChart = getBaseChart();
			baseChart.suspendUpdate(true);
			initialise(baseChart);
			// for(IPieSeriesData pieSeriesData : pieSeriesDataList) {
			/*
			 * Get the series data and apply the settings.
			 */
			try {
				ICircularSeriesSettings pieSeriesSettings = (ICircularSeriesSettings)model.getSettings();
				ICircularSeries pieSeries = (ICircularSeries)createCircularSeries(model, pieSeriesSettings);
				baseChart.applyCircularSeriesSettings(pieSeries, pieSeriesSettings);
			} catch(SeriesException e) {
				//
			}
			// }
			baseChart.suspendUpdate(false);
			// adjustRange(true);
			baseChart.redraw();
		} else {
			// throw error
		}
	}

	private void initialise(BaseChart chart) {

		IAxis[] axes = chart.getAxisSet().getAxes();
		for(IAxis axis : axes) {
			axis.getTick().setVisible(false);
			axis.getGrid().setVisible(false);
			axis.getTitle().setVisible(false);
		}
	}
}
