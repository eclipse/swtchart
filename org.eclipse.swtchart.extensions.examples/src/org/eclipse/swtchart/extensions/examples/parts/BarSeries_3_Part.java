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
import java.util.List;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.customcharts.core.MassSpectrumChart;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;

public class BarSeries_3_Part extends MassSpectrumChart {

	@Inject
	public BarSeries_3_Part(Composite parent) {

		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		initialize();
	}

	private void initialize() {

		IChartSettings chartSettings = getChartSettings();
		chartSettings.setCreateMenu(true);
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtendMinY(0.1d);
		applySettings(chartSettings);
		//
		setNumberOfHighestIntensitiesToLabel(5);
		setLabelOption(LabelOption.NOMIMAL);
		setCustomLabels(null);
		/*
		 * Create series.
		 */
		List<IBarSeriesData> barSeriesDataList = new ArrayList<IBarSeriesData>();
		ISeriesData seriesData;
		IBarSeriesData barSeriesData;
		IBarSeriesSettings barSeriesSettings;
		/*
		 * Positive
		 */
		seriesData = SeriesConverter.getSeriesXY(SeriesConverter.BAR_SERIES_3_POSITIVE);
		barSeriesData = new BarSeriesData(seriesData);
		barSeriesSettings = barSeriesData.getSettings();
		barSeriesSettings.setBarColor(getDisplay().getSystemColor(SWT.COLOR_RED));
		barSeriesDataList.add(barSeriesData);
		/*
		 * Negative
		 */
		seriesData = SeriesConverter.getSeriesXY(SeriesConverter.BAR_SERIES_3_NEGATIVE);
		barSeriesData = new BarSeriesData(seriesData);
		barSeriesSettings = barSeriesData.getSettings();
		barSeriesSettings.setBarColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		barSeriesDataList.add(barSeriesData);
		/*
		 * Set series.
		 */
		addSeriesData(barSeriesDataList);
	}
}
