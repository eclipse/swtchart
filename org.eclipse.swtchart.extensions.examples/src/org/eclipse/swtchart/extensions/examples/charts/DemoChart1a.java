/*******************************************************************************
 * Copyright (c) 2020, 2024 SWTChart project.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.examples.parts.LineSeries_1a_Part;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class DemoChart1a {

	private static final String CATEGORY = "Settings";

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Area Strict Example (Experimental)");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		//
		ScrollableChart scrollableChart = new LineSeries_1a_Part(shell);
		scrollableChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		shell.open();
		/*
		 * Demo Chart Background (experimental)
		 */
		IChartSettings chartSettings = scrollableChart.getChartSettings();
		chartSettings.setTitle("");
		chartSettings.setTitleVisible(false);
		chartSettings.setTitleColor(display.getSystemColor(SWT.COLOR_BLACK));
		chartSettings.setLegendExtendedVisible(false);
		chartSettings.setBufferSelection(true);
		chartSettings.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		chartSettings.setBackgroundChart(display.getSystemColor(SWT.COLOR_WHITE));
		chartSettings.setBackgroundPlotArea(display.getSystemColor(SWT.COLOR_WHITE));
		addOrientationOption(chartSettings);
		addAreaStrictOption(chartSettings);
		scrollableChart.applySettings(chartSettings);
		//
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void addOrientationOption(IChartSettings chartSettings) {

		chartSettings.addMenuEntry(new IChartMenuEntry() {

			@Override
			public String getName() {

				return "Orientation (Toggle)";
			}

			@Override
			public String getCategory() {

				return CATEGORY;
			}

			@Override
			public Image getIcon() {

				return ResourceSupport.getImage(ResourceSupport.ICON_FIGURE);
			}

			@Override
			public void execute(Shell shell, ScrollableChart scrollableChart) {

				IChartSettings chartSettings = scrollableChart.getChartSettings();
				int selection = chartSettings.getOrientation() == SWT.HORIZONTAL ? SWT.VERTICAL : SWT.HORIZONTAL;
				chartSettings.setOrientation(selection);
				scrollableChart.applySettings(chartSettings);
			}
		});
	}

	private static void addAreaStrictOption(IChartSettings chartSettings) {

		chartSettings.addMenuEntry(new IChartMenuEntry() {

			@Override
			public String getName() {

				return "Area Strict (Toggle)";
			}

			@Override
			public String getCategory() {

				return CATEGORY;
			}

			@Override
			public Image getIcon() {

				return ResourceSupport.getImage(ResourceSupport.ICON_FIGURE);
			}

			@Override
			public void execute(Shell shell, ScrollableChart scrollableChart) {

				ISeriesSettings seriesSettings = scrollableChart.getBaseChart().getSeriesSettings(SeriesConverter.LINE_SERIES_1_SELECTED_PEAK_1);
				if(seriesSettings instanceof ILineSeriesSettings lineSeriesSettings) {
					lineSeriesSettings.setAreaStrict(!lineSeriesSettings.isAreaStrict());
				}
				scrollableChart.getBaseChart().applySeriesSettings();
			}
		});
	}
}