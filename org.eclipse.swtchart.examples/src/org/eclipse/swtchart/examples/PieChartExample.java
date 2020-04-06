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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.IPieSeries;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.internal.series.PieSeries;
import org.eclipse.swtchart.internal.series.Series;

/**
 * An example for bar chart.
 */
public class PieChartExample {

	private static final double[] values = {337309, 131646, 128948, 100123, 81708, 70478, 58226, 47806, 4067,265783};
	private static final String[] labels = {"USA","Spain","Italy","Germany","China","France","Iran","UK","India","Other"};
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Bar Chart");
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
		chart.getTitle().setText("COVID Cases around the Globe on 6/04/2020");
		// create pie series
		PieSeries pieSeries = (PieSeries)chart.getSeriesSet().createSeries(SeriesType.PIE, "pie series");
		//sets the series.
		((PieSeries)pieSeries).setSeries(labels, values);
		Color color = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
		pieSeries.setColor("India", color);
		return chart;
	}
}