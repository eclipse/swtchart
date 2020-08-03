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
 * Philip Wenig - menu demo extension
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.axisconverter.ByteToKibibyteConverter;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class SecondaryAxisChart {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Kibibyte Example");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		//
		LineChart scrollableChart = new LineChart(shell, SWT.NONE);
		shell.open();
		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = scrollableChart.getChartSettings();
		chartSettings.setCreateMenu(true);
		adjustPrimaryAxes(chartSettings);
		addSecondaryAxis(chartSettings);
		chartSettings.setBufferSelection(true);
		scrollableChart.applySettings(chartSettings);
		/*
		 * Data and Settings
		 */
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		ISeriesData seriesData = getSeriesRandom();
		ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
		ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		lineSeriesSettings.setLineWidth(2);
		lineSeriesSettings.setEnableArea(false);
		lineSeriesDataList.add(lineSeriesData);
		scrollableChart.addSeriesData(lineSeriesDataList);
		//
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void adjustPrimaryAxes(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setVisible(false);
		//
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setGridLineStyle(LineStyle.NONE);
		primaryAxisSettingsY.setTitle("KB (kilobyte)");
	}

	private static void addSecondaryAxis(IChartSettings chartSettings) {

		ISecondaryAxisSettings secondaryAxisSettingsY = new SecondaryAxisSettings("KiB (kibibyte)", new ByteToKibibyteConverter());
		secondaryAxisSettingsY.setPosition(Position.Secondary);
		secondaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
		//
		chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY);
	}

	public static ISeriesData getSeriesRandom() {

		int size = 100;
		double[] ySeries = new double[size];
		//
		for(int i = 0; i < size; i++) {
			ySeries[i] = 500000;
		}
		//
		Random random = new Random();
		for(int i = 35; i < 50; i++) {
			ySeries[i] = random.nextDouble() * 1000000;
		}
		//
		return new SeriesData(ySeries, "Demo");
	}
}
