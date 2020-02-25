/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 * Christoph Läubrich - adjust to ne PlotArea API
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.extensions.charts.InteractiveChart;
import org.eclipse.ui.part.ViewPart;

/**
 * An example view to show InteractiveChart.
 */
public class InteractiveChartExample extends ViewPart {

	private static final String[] categorySeries = {"Mon", "Tue", "Wed", "Thu", "Fri"};
	private static final double[] yLineSeries1 = {4.6, 5.4, 6.9, 5.6, 7.1};
	private static final double[] yLineSeries2 = {6.0, 5.1, 4.9, 5.3, 4.2};
	private static final double[] yBarSeries1 = {1.1, 2.9, 3.3, 4.4, 3.5};
	private static final double[] yBarSeries2 = {4.3, 3.4, 2.8, 2.1, 1.9};
	/** the chart */
	private Chart chart;

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout());
		// create an interactive chart
		chart = new InteractiveChart(parent, SWT.NONE);
		// set title
		chart.getTitle().setText("Sample Interactive Chart");
		// set category series
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(categorySeries);
		// create line series 1
		ILineSeries lineSeries1 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "line series 1");
		lineSeries1.setYSeries(yLineSeries1);
		// create line series 2
		ILineSeries lineSeries2 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "line series 2");
		lineSeries2.setYSeries(yLineSeries2);
		lineSeries2.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		// create bar series 1
		IBarSeries barSeries1 = (IBarSeries)chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series 1");
		barSeries1.setYSeries(yBarSeries1);
		// create bar series 2
		IBarSeries barSeries2 = (IBarSeries)chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series 2");
		barSeries2.setYSeries(yBarSeries2);
		barSeries2.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		// adjust the axis range
		chart.getAxisSet().adjustRange();
	}

	@Override
	public void setFocus() {

		chart.setFocus();
	}

	@Override
	public void dispose() {

		super.dispose();
		chart.dispose();
	}
}
