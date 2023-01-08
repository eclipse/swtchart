/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
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
import org.eclipse.swtchart.customcharts.core.ChromatogramChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class LineSeries_7_Normal_Part extends ChromatogramChart {

	@Inject
	public LineSeries_7_Normal_Part(Composite parent) {

		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		try {
			initialize();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {

		IChartSettings chartSettings = getChartSettings();
		chartSettings.getRangeRestriction().setForceZeroMinY(false);
		applySettings(chartSettings);
		//
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		//
		ISeriesData seriesData;
		ILineSeriesData lineSeriesData;
		ILineSeriesSettings lineSeriesSettings;
		/*
		 * Normal
		 */
		seriesData = SeriesConverter.getSeriesXY(SeriesConverter.LINE_SERIES_7);
		lineSeriesData = new LineSeriesData(seriesData);
		lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setLineColor(getDisplay().getSystemColor(SWT.COLOR_RED));
		lineSeriesSettings.setEnableArea(true);
		ILineSeriesSettings lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
		lineSeriesSettingsHighlight.setLineWidth(2);
		lineSeriesDataList.add(lineSeriesData);
		/*
		 * Set series.
		 */
		addSeriesData(lineSeriesDataList, LineChart.HIGH_COMPRESSION);
	}
}
