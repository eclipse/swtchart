/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.customcharts.ChromatogramChart;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class LineSeries_4_Part extends ChromatogramChart {

	@Inject
	public LineSeries_4_Part(Composite parent) {

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
		chartSettings.setCreateMenu(true);
		chartSettings.setBufferSelection(true);
		applySettings(chartSettings);
		/*
		 * Create series.
		 */
		Map<Integer, Color> colors = new HashMap<Integer, Color>();
		colors.put(1, getDisplay().getSystemColor(SWT.COLOR_RED));
		colors.put(2, getDisplay().getSystemColor(SWT.COLOR_BLACK));
		colors.put(3, getDisplay().getSystemColor(SWT.COLOR_GRAY));
		colors.put(4, getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
		colors.put(5, getDisplay().getSystemColor(SWT.COLOR_GRAY));
		//
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		for(int i = 1; i <= 5; i++) {
			ISeriesData seriesData = SeriesConverter.getSeriesXY(SeriesConverter.LINE_SERIES + "4_" + i);
			ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
			ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
			lineSeriesSettings.setLineColor(colors.get(i));
			lineSeriesSettings.setEnableArea(false);
			ILineSeriesSettings lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
			lineSeriesSettingsHighlight.setLineWidth(2);
			lineSeriesDataList.add(lineSeriesData);
		}
		//
		addSeriesData(lineSeriesDataList, LineChart.MEDIUM_COMPRESSION);
	}
}
