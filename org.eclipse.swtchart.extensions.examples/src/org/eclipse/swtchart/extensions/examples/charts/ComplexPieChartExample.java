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
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.examples.parts.ComplexPieChart;

public class ComplexPieChartExample {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Complex PieChart");
		shell.setSize(700, 600);
		shell.setLayout(new FillLayout());
		//
		ComplexPieChart complexPieChart = new ComplexPieChart(shell);
		IChartSettings chartSettings = complexPieChart.getChartSettings();
		chartSettings.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		chartSettings.setBackgroundChart(display.getSystemColor(SWT.COLOR_WHITE));
		chartSettings.setBackgroundPlotArea(display.getSystemColor(SWT.COLOR_WHITE));
		complexPieChart.applySettings(chartSettings);
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
