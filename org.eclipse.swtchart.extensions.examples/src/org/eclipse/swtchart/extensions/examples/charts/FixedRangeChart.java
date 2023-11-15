/*******************************************************************************
 * Copyright (c) 2022, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.examples.parts.BarSeries_1_1_Part;

public class FixedRangeChart {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("FixedRangeChart");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		//
		ScrollableChart scrollableChart = new BarSeries_1_1_Part(shell);
		IChartSettings chartSettings = scrollableChart.getChartSettings();
		chartSettings.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		chartSettings.setBackgroundChart(display.getSystemColor(SWT.COLOR_WHITE));
		chartSettings.setBackgroundPlotArea(display.getSystemColor(SWT.COLOR_WHITE));
		scrollableChart.applySettings(chartSettings);
		shell.open();
		/*
		 * Set a fixed range.
		 */
		IAxis axisX = scrollableChart.getBaseChart().getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		axisX.setRange(new Range(10, 250));
		IAxis axisY = scrollableChart.getBaseChart().getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		axisY.setRange(new Range(0, 1500));
		//
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}