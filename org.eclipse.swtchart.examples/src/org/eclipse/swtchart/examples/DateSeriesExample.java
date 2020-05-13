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
 * Himanshu Balasamanta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.examples;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries.SeriesType;

public class DateSeriesExample {

	private static final double[] ySeries = {0.26, 0.59, 0.92, 0.70, 1.03, 1.34, 1.09, 1.42, 1.75};
	private static Date[] xDateSeries;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Line Chart");
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
		chart.getTitle().setText("Line Chart");
		chart.getAxisSet().getXAxis(0).getTitle().setText("Data Points");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Amplitude");
		xDateSeries = new Date[ySeries.length];
		for(int i = 0; i < ySeries.length; i++) {
			xDateSeries[i] = new Date(2020, 5, i * 2);
		}
		// create line series
		ILineSeries<?> lineSeries = (ILineSeries<?>)chart.getSeriesSet().createSeries(SeriesType.LINE, "line series");
		lineSeries.setYSeries(ySeries);
		lineSeries.setXDateSeries(xDateSeries);
		// adjust the axis range
		chart.getAxisSet().adjustRange();
		return chart;
	}
}
