/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class MeasurementSeries_2_Part extends LineChart {

	@Inject
	public MeasurementSeries_2_Part(Composite parent) {
		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		try {
			initialize();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	private void initialize() throws Exception {

		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = getChartSettings();
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(false);
		chartSettings.getRangeRestriction().setZeroX(true);
		chartSettings.getRangeRestriction().setZeroY(true);
		chartSettings.setCreateMenu(true);
		/*
		 * Primary X-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("Concentration [mg/g]");
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsX.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		primaryAxisSettingsX.setGridLineStyle(LineStyle.DOT);
		/*
		 * Primary Y-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("Area");
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		primaryAxisSettingsY.setGridLineStyle(LineStyle.DOT);
		//
		applySettings(chartSettings);
		/*
		 * Create series.
		 */
		Map<Integer, Color> colors = new HashMap<Integer, Color>();
		colors.put(1, getDisplay().getSystemColor(SWT.COLOR_BLACK));
		colors.put(2, getDisplay().getSystemColor(SWT.COLOR_RED));
		colors.put(3, getDisplay().getSystemColor(SWT.COLOR_GRAY));
		//
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		ISeriesData seriesData;
		ILineSeriesData lineSeriesData;
		ILineSeriesSettings lineSeriesSettings;
		ILineSeriesSettings lineSeriesSettingsHighlight;
		//
		for(int i = 1; i <= 3; i++) {
			/*
			 * Readings
			 */
			seriesData = SeriesConverter.getSeriesXY("Measurement2_Readings_" + i);
			lineSeriesData = new LineSeriesData(seriesData);
			lineSeriesSettings = lineSeriesData.getSettings();
			lineSeriesSettings.setEnableArea(false);
			lineSeriesSettings.setLineStyle(LineStyle.NONE);
			lineSeriesSettings.setSymbolSize(5);
			lineSeriesSettings.setSymbolColor(colors.get(i));
			lineSeriesSettings.setSymbolType(PlotSymbolType.CIRCLE);
			lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
			lineSeriesSettingsHighlight.setLineWidth(2);
			lineSeriesDataList.add(lineSeriesData);
			/*
			 * Regression
			 */
			seriesData = SeriesConverter.getSeriesXY("Measurement2_Regression_" + i);
			lineSeriesData = new LineSeriesData(seriesData);
			lineSeriesSettings = lineSeriesData.getSettings();
			lineSeriesSettings.setEnableArea(false);
			lineSeriesSettings.setLineStyle(LineStyle.SOLID);
			lineSeriesSettings.setLineColor(colors.get(i));
			lineSeriesSettings.setSymbolType(PlotSymbolType.NONE);
			lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
			lineSeriesSettingsHighlight.setLineWidth(2);
			lineSeriesDataList.add(lineSeriesData);
		}
		/*
		 * Set series.
		 */
		addSeriesData(lineSeriesDataList);
	}
}
