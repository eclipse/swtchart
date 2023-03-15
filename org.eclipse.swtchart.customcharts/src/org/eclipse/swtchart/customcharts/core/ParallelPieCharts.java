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
 * Philip Wenig - circular series extended legend
 *******************************************************************************/
package org.eclipse.swtchart.customcharts.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.extensions.core.ChartSettings;
import org.eclipse.swtchart.extensions.piecharts.CircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.PieChart;
import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.support.CircularLegend;

public class ParallelPieCharts {

	private Composite composite;
	private List<PieChart> linkedPieCharts;
	private CircularLegend legend;
	private boolean redrawOnClick;
	private SeriesType seriesType;
	private int noOfCharts;
	// private String[] pieTitles; // It's not used yet.
	private CircularSeriesData[] dataArray;

	public ParallelPieCharts(Composite parent, SeriesType type, boolean redraw) {

		composite = parent;
		redrawOnClick = redraw;
		seriesType = type;
		linkedPieCharts = new ArrayList<>();
		parent.setLayout(new FillLayout());
	}

	/**
	 * Add data to this Pie Chart. We'll build one pie chart for each value in the
	 * array provided. The val matrix must have an array of an array of values. Ex.
	 * labels = {'a', 'b'} val = {{1,2,3}, {4,5,6}} This will create 3 pie charts.
	 * For the first one, 'a' will be 1 and 'b' will be 4. For the second chart 'a'
	 * will be 2 and 'b' will be 5. For the third 'a' will be 3 and 'b' will be 6.
	 * 
	 * @param labels
	 *            The titles of each series. (These are not the same as titles
	 *            given to pies.)
	 * @param val
	 *            New values.
	 */
	public void addPieChartSeries(String[] labels, double[][] val) {

		noOfCharts = val[0].length;
		String[] legendLabels = labels;
		int noOfSlices = labels.length;
		dataArray = new CircularSeriesData[noOfCharts];
		// create the charts independently
		for(int i = 0; i != noOfCharts; i++) {
			dataArray[i] = new CircularSeriesData();
			dataArray[i].getSettings().setSeriesType(seriesType);
			double[] values = new double[noOfSlices];
			for(int j = 0; j != noOfSlices; j++) {
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
		for(int i = 0; i != noOfCharts; i++) {
			for(int j = 0; j != noOfCharts; j++) {
				if(i == j) {
					continue;
				}
				linkedPieCharts.get(i).addLinkedScrollableChart(linkedPieCharts.get(j));
			}
		}
	}

	/**
	 * 
	 * @param parentId
	 *            the id to which we want to introduce the child to in each
	 *            chart.
	 * @param childId
	 *            the child we wish to introduce
	 * @param vals
	 *            values of the child in each chart.
	 * 
	 *            Throws error if vals.length is not equal to number of charts.
	 */
	public void addChild(String parentId, String childId, double[] vals) {

		if(vals.length != noOfCharts) {
			// throw error
			return;
		}
		for(int i = 0; i != noOfCharts; i++) {
			Node parent = dataArray[i].getNodeById(parentId);
			parent.addChild(childId, vals[i]);
			setSettings(i);
		}
	}

	/**
	 * 
	 * @param parentId
	 *            the id of node to which we want to add the children in each
	 *            chart.
	 * @param childrenId
	 *            the id of the children we wish to add.
	 * @param vals
	 *            each array in it should represent the values of the
	 *            corresponding child node (present at the same index) in
	 *            each of the charts.
	 */
	public void addChildren(String parentId, String[] childrenId, double[][] vals) {

		if(vals.length != noOfCharts || vals[0].length != childrenId.length) {
			// throw error
			return;
		}
		for(int i = 0; i != childrenId.length; i++) {
			addChild(parentId, childrenId[i], vals[i]);
			setSettings(i);
		}
	}

	public void setChartTitles(String[] titles) {

		// this.pieTitles = titles; // It's not used yet.
		int length = Math.min(noOfCharts, titles.length);
		for(int i = 0; i != length; i++) {
			linkedPieCharts.get(i).getChartSettings().setTitle(titles[i]);
			PieChart pieChart = linkedPieCharts.get(i);
			ChartSettings chartSettings = (ChartSettings)pieChart.getChartSettings();
			chartSettings.setTitleColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			pieChart.applySettings(chartSettings);
		}
	}

	public void setRedrawOnClick(boolean redrawOnClick) {

		this.redrawOnClick = redrawOnClick;
		for(int i = 0; i != noOfCharts; i++) {
			setSettings(i);
		}
	}

	/**
	 * settings for this
	 */
	private void setSettings(int index) {

		// is legend common
		PieChart pieChart = linkedPieCharts.get(index);
		pieChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		if(index == noOfCharts - 1) {
			if(legend == null) {
				legend = new CircularLegend(composite, SWT.NONE);
			}
			legend.setChart(pieChart.getBaseChart());
			legend.updateLayoutData();
			legend.setVisible(true);
		}
		ChartSettings chartSettings = (ChartSettings)pieChart.getChartSettings();
		chartSettings.setLegendVisible(false);
		chartSettings.setTitleColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		chartSettings.setLegendExtendedVisible(false);
		chartSettings.setShowLegendMarker(false);
		pieChart.applySettings(chartSettings);
	}

	private void setCircularSettings(CircularSeriesData circularSeriesData) {

		ICircularSeriesSettings settings = circularSeriesData.getSettings();
		if(redrawOnClick) {
			settings.setRedrawOnClick(true);
			settings.setSliceColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		} else {
			settings.setSliceColor(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			settings.setRedrawOnClick(false);
		}
	}
}
