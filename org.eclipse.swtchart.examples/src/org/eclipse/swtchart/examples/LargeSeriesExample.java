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
 *******************************************************************************/
package org.eclipse.swtchart.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example for large series chart.
 */
public class LargeSeriesExample {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Large Series");
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
		chart.getTitle().setText("Large Series");
		chart.getAxisSet().getXAxis(0).getTitle().setText("Data Points");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Amplitude");
		// create line series
		ILineSeries lineSeries = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "line series");
		lineSeries.setYSeries(getSeries());
		lineSeries.setSymbolType(PlotSymbolType.NONE);
		// adjust the axis range
		chart.getAxisSet().adjustRange();
		return chart;
	}

	private static double[] getSeries() {

		double[] series = new double[1048576];
		for(int i = 0; i < series.length; i++) {
			series[i] = Math.sin(i * 33 * Math.PI / series.length) + Math.sin(i * 15 * Math.PI / series.length);
		}
		return series;
	}
}