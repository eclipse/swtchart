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
 * Yash Bharatiya - initial API and implementation
 * Philip Wenig - menu demo extension
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
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
		addClipboardMenuEntry(chartSettings);
		addToggleAxisLinesMenuEntry(chartSettings);
		scrollableChart.applySettings(chartSettings);
		//
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void addClipboardMenuEntry(IChartSettings chartSettings) {

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
			public Image getIcon() {

				return ResourceSupport.getImage(ResourceSupport.ICON_COPY_CLIPBOARD);
			}

			@Override
			public void execute(Shell shell, ScrollableChart scrollableChart) {

				StringBuilder builder = new StringBuilder();
				IAxisSet axisSet = scrollableChart.getBaseChart().getAxisSet();
				builder.append(getAxisTicks(axisSet.getXAxes()));
				builder.append(getAxisTicks(axisSet.getYAxes()));
				Object[] data = new Object[]{builder.toString()};
				//
				TextTransfer textTransfer = TextTransfer.getInstance();
				Transfer[] dataTypes = new Transfer[]{textTransfer};
				Clipboard clipboard = new Clipboard(Display.getDefault());
				clipboard.setContents(data, dataTypes);
				clipboard.dispose();
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
	}

	private static void addToggleAxisLinesMenuEntry(IChartSettings chartSettings) {

		chartSettings.addMenuEntry(new IChartMenuEntry() {

			@Override
			public String getName() {

				return "Toggle Axis Lines";
			}

			@Override
			public String getCategory() {

				return "Tools";
			}

			@Override
			public Image getIcon() {

				if(chartSettings.getPrimaryAxisSettingsX().isDrawAxisLine()) {
					return ResourceSupport.getImage(ResourceSupport.ICON_CHECKED);
				} else {
					return ResourceSupport.getImage(ResourceSupport.ICON_UNCHECKED);
				}
			}

			@Override
			public void execute(Shell shell, ScrollableChart scrollableChart) {

				IChartSettings chartSettings = scrollableChart.getChartSettings();
				/*
				 * Primary Axes
				 */
				chartSettings.getPrimaryAxisSettingsX().setDrawAxisLine(!chartSettings.getPrimaryAxisSettingsX().isDrawAxisLine());
				chartSettings.getPrimaryAxisSettingsY().setDrawAxisLine(!chartSettings.getPrimaryAxisSettingsY().isDrawAxisLine());
				/*
				 * Secondary Axes
				 */
				for(ISecondaryAxisSettings secondaryAxisSettings : chartSettings.getSecondaryAxisSettingsListX()) {
					secondaryAxisSettings.setDrawAxisLine(!secondaryAxisSettings.isDrawAxisLine());
				}
				//
				for(ISecondaryAxisSettings secondaryAxisSettings : chartSettings.getSecondaryAxisSettingsListY()) {
					secondaryAxisSettings.setDrawAxisLine(!secondaryAxisSettings.isDrawAxisLine());
				}
				//
				scrollableChart.applySettings(chartSettings);
			}
		});
	}
}
