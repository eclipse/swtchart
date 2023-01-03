/*******************************************************************************
 * Copyright (c) 2020, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 * Philip Wenig - series settings mapping
 *******************************************************************************/
package org.eclipse.swtchart.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example for pie chart.
 */
public class SingleLevelPieChartExample {

	private static final double[] values = {337309, 131646, 128948, 100123, 81708, 70478, 58226, 47806, 4067, 265783};
	private static final String[] labels = {"USA", "Spain", "Italy", "Germany", "China", "France", "Iran", "UK", "India", "Other"};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Pie Chart");
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

		Chart chart = new Chart(parent, SWT.NONE);
		chart.getTitle().setText("Single Level Pie Chart");
		ICircularSeries<?> circularSeries = (ICircularSeries<?>)chart.getSeriesSet().createSeries(SeriesType.PIE, "pie series");
		circularSeries.setSeries(labels, values);
		circularSeries.setColor("India", Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));
		//
		return chart;
	}
}