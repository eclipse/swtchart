/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.customcharts.core.PCAChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesData;

import jakarta.inject.Inject;

public class ScatterSeries_1_Part extends PCAChart {

	private Color COLOR_RED = getDisplay().getSystemColor(SWT.COLOR_RED);
	private Color COLOR_BLUE = getDisplay().getSystemColor(SWT.COLOR_BLUE);
	private Color COLOR_MAGENTA = getDisplay().getSystemColor(SWT.COLOR_MAGENTA);
	private Color COLOR_CYAN = getDisplay().getSystemColor(SWT.COLOR_CYAN);
	private Color COLOR_GRAY = getDisplay().getSystemColor(SWT.COLOR_GRAY);
	//
	private int SYMBOL_SIZE = 8;

	@Inject
	public ScatterSeries_1_Part(Composite parent) {

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
		applySettings(chartSettings);
		/*
		 * Data
		 */
		List<ISeriesData> scatterSeriesList = SeriesConverter.getSeriesScatter(SeriesConverter.SCATTER_SERIES_1);
		List<IScatterSeriesData> scatterSeriesDataList = new ArrayList<IScatterSeriesData>();
		//
		for(ISeriesData seriesData : scatterSeriesList) {
			IScatterSeriesData scatterSeriesData = new ScatterSeriesData(seriesData);
			IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getSettings();
			/*
			 * Set the color and symbol type.
			 */
			double x = seriesData.getXSeries()[0];
			double y = seriesData.getYSeries()[0];
			applySettings(scatterSeriesSettings, x, y, SYMBOL_SIZE);
			scatterSeriesDataList.add(scatterSeriesData);
		}
		/*
		 * Set series.
		 */
		addSeriesData(scatterSeriesDataList);
	}

	private void applySettings(IScatterSeriesSettings scatterSeriesSettings, double x, double y, int symbolSize) {

		scatterSeriesSettings.setSymbolSize(SYMBOL_SIZE);
		//
		if(x > 0 && y > 0) {
			scatterSeriesSettings.setSymbolColor(COLOR_RED);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.SQUARE);
		} else if(x > 0 && y < 0) {
			scatterSeriesSettings.setSymbolColor(COLOR_BLUE);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.TRIANGLE);
		} else if(x < 0 && y > 0) {
			scatterSeriesSettings.setSymbolColor(COLOR_MAGENTA);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.DIAMOND);
		} else if(x < 0 && y < 0) {
			scatterSeriesSettings.setSymbolColor(COLOR_CYAN);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.INVERTED_TRIANGLE);
		}
		//
		IScatterSeriesSettings scatterSeriesSettingsHighlight = (IScatterSeriesSettings)scatterSeriesSettings.getSeriesSettingsHighlight();
		scatterSeriesSettingsHighlight.setSymbolColor(COLOR_GRAY);
		scatterSeriesSettingsHighlight.setSymbolType(PlotSymbolType.CIRCLE);
		scatterSeriesSettingsHighlight.setSymbolSize(symbolSize);
	}
}
