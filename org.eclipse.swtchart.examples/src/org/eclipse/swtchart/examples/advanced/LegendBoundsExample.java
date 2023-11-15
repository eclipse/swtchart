/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.examples.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example to get bounds of items on legend.
 */
public class LegendBoundsExample {

	private static final double[] ySeries1 = {0.1, 0.1, 0.2, 0.2, 0.3};
	private static final double[] ySeries2 = {0.5, 0.5, 0.4, 0.3, 0.2};
	private static final double[] ySeries3 = {0.3, 0.2, 0.3, 0.4, 0.4};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Legend Bounds");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		createChart(shell);
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * create the chart.
	 * 
	 * @param parent
	 *            The parent composite
	 * @return The created chart
	 */
	public static Chart createChart(Composite parent) {

		// create a chart
		final Chart chart = new Chart(parent, SWT.NONE);
		chart.getTitle().setText("Legend Bounds");
		chart.getPlotArea().setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		chart.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		// create bar series
		IBarSeries<?> series1 = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "series 1");
		series1.setYSeries(ySeries1);
		series1.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		IBarSeries<?> series2 = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "series 2");
		series2.setYSeries(ySeries2);
		series2.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_MAGENTA));
		IBarSeries<?> series3 = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "series 3");
		series3.setYSeries(ySeries3);
		// adjust the axis range
		chart.getAxisSet().adjustRange();
		// add mouse move listener to legend
		final Control legend = (Control)chart.getLegend();
		legend.addMouseMoveListener(e -> {
			for(ISeries<?> series : chart.getSeriesSet().getSeries()) {
				Rectangle r = chart.getLegend().getBounds(series.getId());
				if(r.x < e.x && e.x < r.x + r.width && r.y < e.y && e.y < r.y + r.height) {
					legend.setToolTipText(series.getId());
					return;
				}
			}
		});
		return chart;
	}
}
