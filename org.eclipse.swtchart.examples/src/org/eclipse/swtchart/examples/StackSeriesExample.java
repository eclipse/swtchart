/*******************************************************************************
 * Copyright (c) 2008, 2020 SWTChart project.
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
package org.eclipse.swtchart.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example for chart with stack series.
 */
public class StackSeriesExample {

	private static final double[] ySeries1 = {1.3, 2.4, 3.9, 2.6, 1.1};
	private static final double[] ySeries2 = {3.0, 2.1, 1.9, 2.3, 3.2};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Stack Series");
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
	static public Chart createChart(Composite parent) {

		// create a chart
		Chart chart = new Chart(parent, SWT.NONE);
		// set titles
		chart.getTitle().setText("Stack Series");
		chart.getAxisSet().getXAxis(0).getTitle().setText("Month");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Amplitude");
		// set category
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(new String[]{"Jan", "Feb", "Mar", "Apr", "May"});
		// create bar series
		IBarSeries<?> barSeries1 = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series 1");
		barSeries1.setYSeries(ySeries1);
		barSeries1.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		IBarSeries<?> barSeries2 = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series 2");
		barSeries2.setYSeries(ySeries2);
		// enable stack series
		barSeries1.enableStack(true);
		barSeries2.enableStack(true);
		// adjust the axis range
		chart.getAxisSet().adjustRange();
		return chart;
	}
}