/*******************************************************************************
 * Copyright (c) 2022, 2023 Lablicate GmbH.
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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.examples.parts.PeakSeries_1_Part;

public class NoBorderSimpleChart {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("NoBorderSimpleChart");
		shell.setSize(300, 300);
		shell.setLayout(new FillLayout());
		//
		ScrollableChart scrollableChart = new PeakSeries_1_Part(shell);
		shell.open();
		/*
		 * Use this demo to test the buffer selection in different
		 * environments.
		 */
		Font font = new Font(Display.getCurrent(), "Arial", 12, SWT.NONE);
		IChartSettings chartSettings = scrollableChart.getChartSettings();
		chartSettings.setBufferSelection(true);
		chartSettings.setHorizontalSliderVisible(false);
		chartSettings.setVerticalSliderVisible(false);
		chartSettings.getPrimaryAxisSettingsX().setVisible(false);
		chartSettings.getPrimaryAxisSettingsY().setVisible(false);
		chartSettings.setTitle("Toluene");
		chartSettings.setTitleColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		chartSettings.setTitleFont(font);
		chartSettings.setTitleVisible(true);
		chartSettings.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		chartSettings.setBackgroundChart(display.getSystemColor(SWT.COLOR_WHITE));
		chartSettings.setBackgroundPlotArea(display.getSystemColor(SWT.COLOR_WHITE));
		disableSecondaryAxes(chartSettings.getSecondaryAxisSettingsListX());
		disableSecondaryAxes(chartSettings.getSecondaryAxisSettingsListY());
		scrollableChart.applySettings(chartSettings);
		//
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		//
		font.dispose();
		display.dispose();
	}

	private static void disableSecondaryAxes(List<ISecondaryAxisSettings> secondaryAxisSettings) {

		for(ISecondaryAxisSettings secondaryAxisSetting : secondaryAxisSettings) {
			secondaryAxisSetting.setVisible(false);
			secondaryAxisSetting.setGridLineStyle(LineStyle.NONE);
		}
	}
}
