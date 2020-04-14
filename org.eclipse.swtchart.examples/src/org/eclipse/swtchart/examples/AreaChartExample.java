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
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example for area chart.
 */
public class AreaChartExample {

	private static final double[] ySeries1 = {0.1, 0.38, 0.71, 0.92, 1.0};
	private static final double[] ySeries2 = {1.2, 3.53, 3.1, 0.1, 0.5};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Area Chart");
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
		chart.getTitle().setText("Area Chart");
		// create line series
		ILineSeries<?> lineSeries1 = (ILineSeries<?>)chart.getSeriesSet().createSeries(SeriesType.LINE, "line series 1");
		lineSeries1.setYSeries(ySeries1);
		lineSeries1.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		lineSeries1.enableArea(true);
		ILineSeries<?> lineSeries2 = (ILineSeries<?>)chart.getSeriesSet().createSeries(SeriesType.LINE, "line series 2");
		lineSeries2.setYSeries(ySeries2);
		lineSeries2.enableArea(true);
		// adjust the axis range
		chart.getAxisSet().adjustRange();
		return chart;
	}
}