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
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example for angled axis tick labels.
 */
public class AngledAxisTickLabelsExample {

	private static final double[] ySeries = {1.3, 2.4, 3.9, 2.6, 1.1};
	private static final String[] cagetorySeries = {"aaaaaaaaaa", "bb", "ccccccccccc", "dddddddddd", "eeeeeeeee"};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Angled Axis Tick Labels");
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
		chart.getTitle().setText("Angled Axis Tick Labels");
		// set category
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(cagetorySeries);
		chart.getAxisSet().getXAxis(0).getTick().setTickLabelAngle(45);
		// add bar series
		ISeries<?> barSeries = chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series");
		barSeries.setYSeries(ySeries);
		chart.getAxisSet().adjustRange();
		return chart;
	}
}