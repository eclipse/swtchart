/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterChart;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesData;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;

public class ScatterSeries_3_Part extends ScatterChart {

	@Inject
	public ScatterSeries_3_Part(Composite parent) {
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
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setExtendTypeX(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtendMinX(0.25d);
		rangeRestriction.setExtendMaxX(0.1d);
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtendMinY(0.25d);
		rangeRestriction.setExtendMaxY(0.1d);
		rangeRestriction.setRestrictZoom(false);
		chartSettings.setCreateMenu(true);
		//
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("1st Dimension");
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsX.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		//
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("2nd Dimension");
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.000"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		//
		applySettings(chartSettings);
		/*
		 * Create series.
		 */
		Map<Integer, Color> colors = new HashMap<Integer, Color>();
		colors.put(1, getDisplay().getSystemColor(SWT.COLOR_RED));
		colors.put(2, getDisplay().getSystemColor(SWT.COLOR_GRAY));
		colors.put(3, getDisplay().getSystemColor(SWT.COLOR_GREEN));
		colors.put(4, getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		colors.put(5, getDisplay().getSystemColor(SWT.COLOR_CYAN));
		colors.put(6, getDisplay().getSystemColor(SWT.COLOR_BLUE));
		colors.put(7, getDisplay().getSystemColor(SWT.COLOR_BLACK));
		colors.put(8, getDisplay().getSystemColor(SWT.COLOR_DARK_MAGENTA));
		colors.put(9, getDisplay().getSystemColor(SWT.COLOR_DARK_CYAN));
		colors.put(10, getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
		colors.put(11, getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
		//
		List<IScatterSeriesData> scatterSeriesDataList = new ArrayList<IScatterSeriesData>();
		for(int i = 1; i <= 5; i++) {
			for(int j = 1; j <= 20; j++) {
				String id = "Series_" + i + "-" + j;
				int y = getRandomValue(10.0d);
				double[] xSeries = new double[]{i};
				double[] ySeries = new double[]{y};
				ISeriesData seriesData = new SeriesData(xSeries, ySeries, id);
				IScatterSeriesData scatterSeriesData = new ScatterSeriesData(seriesData);
				IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getScatterSeriesSettings();
				scatterSeriesSettings.setDescription(id);
				scatterSeriesSettings.setSymbolSize(getRandomValue(30.0d));
				scatterSeriesSettings.setSymbolColor(colors.get(y));
				scatterSeriesSettings.setSymbolType(PlotSymbolType.CIRCLE);
				scatterSeriesDataList.add(scatterSeriesData);
			}
		}
		addSeriesData(scatterSeriesDataList);
	}

	private int getRandomValue(double multiplier) {

		return (int)(Math.random() * multiplier + 1);
	}
}
