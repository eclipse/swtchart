/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.examples.parts;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.marker.LabelMarker;

public class MyChart_Part extends BarChart {

	@Inject
	public MyChart_Part(Composite parent) {
		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		initialize();
	}

	private void initialize() {

		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = getChartSettings();
		chartSettings.setCreateMenu(true);
		chartSettings.setHorizontalSliderVisible(true);
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setRestrictZoom(false);
		//
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("X Axis (Primary)");
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.0"), new DecimalFormatSymbols(Locale.ENGLISH)));
		//
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("Y Axis (Primary)");
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.0"), new DecimalFormatSymbols(Locale.ENGLISH)));
		//
		applySettings(chartSettings);
		/*
		 * Bar Series
		 */
		double[] xSeries = new double[]{44.1, 50.4, 75.4, 102.3, 154.4};
		double[] ySeries = new double[]{102.0, 289.0, 389.0, 272.0, 160.0};
		ISeriesData seriesData = new SeriesData(xSeries, ySeries, "Distribution");
		List<IBarSeriesData> barSeriesDataList = new ArrayList<IBarSeriesData>();
		IBarSeriesData barSeriesData = new BarSeriesData(seriesData);
		barSeriesDataList.add(barSeriesData);
		addSeriesData(barSeriesDataList);
		/*
		 * Label Marker.
		 */
		BaseChart baseChart = getBaseChart();
		IPlotArea plotArea = (IPlotArea)baseChart.getPlotArea();
		LabelMarker labelMarker = new LabelMarker(baseChart);
		Map<Integer, String> labels = new HashMap<Integer, String>();
		labels.put(0, "44.1 (A)");
		labels.put(1, "50.4 (B)");
		labels.put(2, "75.4 (C)");
		labels.put(3, "102.3 (D)");
		labels.put(4, "154.4 (E)");
		labelMarker.setLabels(labels, 0, SWT.HORIZONTAL);
		plotArea.addCustomPaintListener(labelMarker);
	}
}
