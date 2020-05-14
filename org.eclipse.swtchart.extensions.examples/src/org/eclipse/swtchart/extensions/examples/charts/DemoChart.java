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
 * Yash Bharatiya - initial API and implementation
 * Philip Wenig - menu demo extension
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.examples.parts.LineSeries_1_Part;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class DemoChart {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("DemoChart");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		//
		ScrollableChart scrollableChart = new LineSeries_1_Part(shell);
		shell.open();
		/*
		 * Use this demo to test the buffer selection in different
		 * environments.
		 */
		IChartSettings chartSettings = scrollableChart.getChartSettings();
		chartSettings.setLegendExtendedVisible(true);
		chartSettings.setBufferSelection(true);
		chartSettings.addMenuEntry(new IChartMenuEntry() {

			@Override
			public String getName() {

				return "Copy to Clipboard (Axis Ticks)";
			}

			@Override
			public String getCategory() {

				return "Tools";
			}

			@Override
			public void execute(Shell shell, ScrollableChart scrollableChart) {

				Clipboard clipboard = new Clipboard(Display.getDefault());
				//
				StringBuilder builder = new StringBuilder();
				IAxisSet axisSet = scrollableChart.getBaseChart().getAxisSet();
				builder.append(getAxisTicks(axisSet.getXAxes()));
				builder.append(getAxisTicks(axisSet.getYAxes()));
				Object[] data = new Object[]{builder.toString()};
				//
				TextTransfer textTransfer = TextTransfer.getInstance();
				Transfer[] dataTypes = new Transfer[]{textTransfer};
				clipboard.setContents(data, dataTypes);
			}

			private String getAxisTicks(IAxis[] axes) {

				StringBuilder builder = new StringBuilder();
				for(IAxis axis : axes) {
					ITitle title = axis.getTitle();
					if(title.isVisible()) {
						builder.append(title.getText());
						builder.append("\n");
						IAxisTick ticks = axis.getTick();
						double[] values = ticks.getTickLabelValues();
						for(double value : values) {
							builder.append(value);
							builder.append("\t");
						}
						builder.append("\n");
					}
				}
				return builder.toString();
			}
		});
		scrollableChart.applySettings(chartSettings);
		//
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
