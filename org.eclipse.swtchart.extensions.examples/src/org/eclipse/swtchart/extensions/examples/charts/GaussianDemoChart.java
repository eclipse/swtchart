/*******************************************************************************
 * Copyright (c) 2026 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.linecharts.ICompressionSupport;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class GaussianDemoChart {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("DemoChart_Gaussian");
		shell.setSize(1200, 400);
		shell.setLayout(new FillLayout());
		shell.setBackground(new Color(255, 255, 255));
		shell.setBackgroundMode(SWT.INHERIT_FORCE);

		LineChart lineChart = new LineChart(shell, SWT.NONE);
		lineChart.setFileName("DemoChart_Gaussian");
		shell.open();

		IChartSettings chartSettings = lineChart.getChartSettings();
		chartSettings.getPrimaryAxisSettingsX().setTitleVisible(false);
		chartSettings.getPrimaryAxisSettingsY().setTitleVisible(false);
		chartSettings.getRangeRestriction().setRestrictSelectX(true);
		lineChart.applySettings(chartSettings);

		List<ILineSeriesData> lineSeriesDataList = new ArrayList<>();
		lineSeriesDataList.add(createLineSeriesData("A", new Color(255, 0, 0)));
		lineChart.addSeriesData(lineSeriesDataList, ICompressionSupport.NO_COMPRESSION);

		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static ILineSeriesData createLineSeriesData(String text, Color color) {

		int size = 10000;
		double[] xSeries = new double[size];
		double[] ySeries = new double[size];

		Random random = new Random(42);

		int peakCount = 120;

		double fwhm = 12.0; // full width at half maximum
		double sigma = fwhm / (2.0 * Math.sqrt(2.0 * Math.log(2.0)));

		double[] centers = new double[peakCount];
		double[] amplitudes = new double[peakCount];

		for(int p = 0; p < peakCount; p++) {
			centers[p] = random.nextDouble() * size;
			amplitudes[p] = 0.3 + random.nextDouble() * 1.7; // random peak height
		}

		for(int i = 0; i < size; i++) {
			double y = 0.02; // baseline
			for(int p = 0; p < peakCount; p++) {
				double dx = i - centers[p];
				y += amplitudes[p] * Math.exp(-(dx * dx) / (2.0 * sigma * sigma));
			}
			xSeries[i] = i;
			ySeries[i] = y;
		}
		SeriesData seriesData = new SeriesData(xSeries, ySeries, text);
		LineSeriesData lineSeriesData = new LineSeriesData(seriesData);
		ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setLineColor(color);
		lineSeriesSettings.setEnableArea(true);
		return lineSeriesData;
	}
}
