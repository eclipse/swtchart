/*******************************************************************************
 * Copyright (c) 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - menu demo extension
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.ui.part.ViewPart;

public class DemoChartDialog {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("DemoChartDialog");
		shell.setSize(250, 100);
		shell.setLayout(new FillLayout());
		//
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Open Chart Dynamically");
		button.setToolTipText("https://github.com/eclipse/swtchart/issues/372");
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				class BarChartView extends ViewPart {

					private Chart chart;
					private static final double[] ySeries = {0.2, 1.1, 1.9, 2.3, 1.8, 1.5, 1.8, 2.6, 2.9, 3.2};

					@Override
					public void createPartControl(Composite parent) {

						chart = new Chart(parent, SWT.NONE);
						chart.getTitle().setText("Bar Chart");
						chart.getAxisSet().getXAxis(0).getTitle().setText("Data Points");
						chart.getAxisSet().getYAxis(0).getTitle().setText("Amplitude");
						//
						IBarSeries<?> barSeries = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series");
						barSeries.setYSeries(ySeries);
						chart.getAxisSet().adjustRange();
						//
						parent.addDisposeListener(new DisposeListener() {

							@Override
							public void widgetDisposed(DisposeEvent e) {

								if(chart != null && !chart.isDisposed()) {
									System.out.println("Dispose Listener");
									chart.dispose();
								}
							}
						});
					}

					@Override
					public void dispose() {

						System.out.println("Dispose");
						super.dispose();
					}

					@Override
					public void setFocus() {

						chart.setFocus();
					}
				}
				/*
				 * Open the chart
				 */
				Shell shell = new Shell(display);
				shell.setText("Bar Chart");
				shell.setSize(650, 500);
				shell.setLayout(new FillLayout());
				//
				BarChartView barChartView = new BarChartView();
				barChartView.createPartControl(shell);
				//
				shell.open();
			}
		});
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