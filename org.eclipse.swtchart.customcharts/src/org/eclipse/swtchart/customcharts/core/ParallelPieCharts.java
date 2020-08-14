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
package org.eclipse.swtchart.customcharts.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.core.ChartSettings;
import org.eclipse.swtchart.extensions.core.CircularLegend;
import org.eclipse.swtchart.extensions.piecharts.CircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.PieChart;
import org.eclipse.swtchart.internal.series.SeriesSet;

public class ParallelPieCharts {

	Composite composite;
	List<PieChart> linkedPieCharts;
	int noOfCharts, noOfSlices;
	String[] legendLabels;
	String[] pieTitles;
	CircularSeriesData[] dataArray;

	public ParallelPieCharts(Composite parent) {
		composite = parent;
		linkedPieCharts = new ArrayList<PieChart>();
		parent.setLayout(new FillLayout());
	}

	/**
	 * Add data to this Pie Chart. We'll build one pie chart for each value in the
	 * array provided. The val matrix must have an array of an array of values. Ex.
	 * labels = {'a', 'b'} val = {{1,2,3}, {4,5,6}} This will create 3 pie charts.
	 * For the first one, 'a' will be 1 and 'b' will be 4. For the second chart 'a'
	 * will be 2 and 'b' will be 5. For the third 'a' will be 3 and 'b' will be 6.
	 * 
	 * @param labels The titles of each series. (These are not the same as titles
	 *               given to pies.)
	 * @param val    New values.
	 */
	public void addPieChartSeries(String labels[], double val[][]) {

		noOfCharts = val[0].length;
		legendLabels = labels;
		noOfSlices = labels.length;
		double[] values = new double[noOfSlices];
		dataArray = new CircularSeriesData[noOfCharts];
		// create the charts independently
		for (int i = 0; i != noOfCharts; i++) {
			dataArray[i] = new CircularSeriesData();
			values = new double[noOfSlices];
			for (int j = 0; j != noOfSlices; j++) {
				values[j] = val[j][i];
			}
			dataArray[i].setSeries(legendLabels, values);
			setCircularSettings(dataArray[i]);
			PieChart pieChart = new PieChart(composite, SWT.NONE);
			pieChart.addSeriesData(dataArray[i]);
			linkedPieCharts.add(pieChart);
			setSettings(i);
		}
		// add them to linked scrollable charts
		for (int i = 0; i != noOfCharts; i++) {
			for (int j = 0; j != noOfCharts; j++) {
				if (i == j)
					continue;
				linkedPieCharts.get(i).addLinkedScrollableChart(linkedPieCharts.get(j));
			}
			// linkedPieCharts.get(i).setLayoutData(new RowLayout(SWT.HORIZONTAL));
		}
	}

	public void setChartTitles(String[] titles) {

		this.pieTitles = titles;
		int length = Math.min(noOfCharts, titles.length);
		for (int i = 0; i != length; i++) {
			linkedPieCharts.get(i).getChartSettings().setTitle(titles[i]);
			PieChart pieChart = linkedPieCharts.get(i);
			ChartSettings chartSettings = (ChartSettings) pieChart.getChartSettings();
			chartSettings.setTitleColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			pieChart.applySettings(chartSettings);
		}
	}

	private void setCircularSettings(CircularSeriesData circularSeriesData) {

		ICircularSeriesSettings settings = circularSeriesData.getSettings();
		settings.setBorderColor(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		settings.setRedrawOnClick(false);
	}

	/**
	 * settings for this
	 */
	public void setSettings(int index) {
		// is legend common
		PieChart pieChart = linkedPieCharts.get(index);
		pieChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		ChartSettings chartSettings = (ChartSettings) pieChart.getChartSettings();
		chartSettings.setLegendVisible(false);
		if (index == noOfCharts - 1) {
			SeriesSet seriesSet = (SeriesSet) pieChart.getBaseChart().getSeriesSet();
			CircularLegend legend = new CircularLegend(composite, SWT.NONE);
			legend.setChart(pieChart.getBaseChart());
			legend.updateLayoutData();
			legend.setVisible(true);
		}
		chartSettings.setTitleColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		chartSettings.setLegendExtendedVisible(false);
		chartSettings.setShowLegendMarker(false);
		pieChart.applySettings(chartSettings);
	}
}
