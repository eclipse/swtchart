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
package org.eclipse.swtchart.extensions.examples.parts;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.customcharts.core.ChromatogramChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class PeakSeries_1_Part extends ChromatogramChart {

	@Inject
	public PeakSeries_1_Part(Composite parent) {

		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//
		try {
			initialize();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {

		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = getChartSettings();
		chartSettings.setCreateMenu(true);
		applySettings(chartSettings);
		/*
		 * Create series.
		 */
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		/*
		 * Peak
		 */
		ISeriesData seriesData = SeriesConverter.getSeriesXY(SeriesConverter.LINE_SERIES_1_SELECTED_PEAK_1);
		ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
		ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setEnableArea(true);
		lineSeriesSettings.setSymbolType(PlotSymbolType.NONE);
		lineSeriesSettings.setLineColor(getDisplay().getSystemColor(SWT.COLOR_RED));
		lineSeriesDataList.add(lineSeriesData);
		/*
		 * Set series.
		 */
		addSeriesData(lineSeriesDataList);
	}
}