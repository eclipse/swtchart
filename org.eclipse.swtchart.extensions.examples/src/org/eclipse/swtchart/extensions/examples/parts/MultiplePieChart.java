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
 * Himanshu Balasamanta: Orignal API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.parts;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.customcharts.core.ParallelPieCharts;

public class MultiplePieChart {

	static private String[] labels = {"this", "legend", "needs", "improvement"};
	static private String[] titles = {"so", "many", "pie", "charts"};
	static private double[][] val = {{1.0, 2.0, 3.0, 5}, {4.0, 5.0, 6.0, 3}, {7, 8, 9, 7}, {1, 5, 3, 2}};
	static private String child = "child";
	static private double[] childVal = {1.2, 0.9, 0.4, 0.6};

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("PieChart");
		shell.setSize(700, 600);
		shell.setLayout(new FillLayout());
		//
		// just change SeriesType to SeriesType.DOUGHNUT
		ParallelPieCharts charts = new ParallelPieCharts(shell, SeriesType.PIE);
		charts.addPieChartSeries(labels, val);
		charts.addChild("legend", child, childVal);
		charts.setChartTitles(titles);
		shell.open();
		//
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
