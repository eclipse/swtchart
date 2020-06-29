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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.customcharts.core.MassSpectrumChart;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;

public class BarSeries_1_3_Part extends MassSpectrumChart {

	@Inject
	public BarSeries_1_3_Part(Composite parent) {

		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		initialize();
	}

	private void initialize() {

		setNumberOfHighestIntensitiesToLabel(5);
		setLabelOption(LabelOption.CUSTOM);
		setCustomLabels(createCustomLabels());
		/*
		 * Create series.
		 */
		List<IBarSeriesData> barSeriesDataList = new ArrayList<IBarSeriesData>();
		ISeriesData seriesData = SeriesConverter.getSeriesXY(SeriesConverter.BAR_SERIES_1);
		//
		IBarSeriesData barSeriesData = new BarSeriesData(seriesData);
		barSeriesDataList.add(barSeriesData);
		/*
		 * Set series.
		 */
		addSeriesData(barSeriesDataList);
	}

	private Map<Double, String> createCustomLabels() {

		Map<Double, String> customLabels = new HashMap<Double, String>();
		//
		customLabels.put(16.0, "245 > 16.0 @12");
		customLabels.put(17.0, "245 > 17.0 @12");
		customLabels.put(18.0, "245 > 18.0 @12");
		customLabels.put(20.0, "245 > 20.0 @12");
		customLabels.put(25.0, "245 > 25.0 @12");
		customLabels.put(26.0, "245 > 26.0 @12");
		customLabels.put(29.3, "245 > 29.3 @12");
		customLabels.put(34.0, "245 > 34.0 @12");
		customLabels.put(36.0, "245 > 36.0 @12");
		customLabels.put(37.0, "245 > 37.0 @12");
		customLabels.put(38.0, "245 > 38.0 @12");
		customLabels.put(39.0, "245 > 39.0 @12");
		customLabels.put(40.0, "245 > 40.0 @12");
		customLabels.put(41.0, "245 > 41.0 @12");
		customLabels.put(42.0, "245 > 42.0 @12");
		customLabels.put(44.0, "245 > 44.0 @12");
		customLabels.put(45.0, "245 > 45.0 @12");
		customLabels.put(46.0, "245 > 46.0 @12");
		customLabels.put(47.0, "245 > 47.0 @12");
		customLabels.put(49.1, "245 > 49.1 @12");
		customLabels.put(50.0, "245 > 50.0 @12");
		customLabels.put(51.0, "245 > 51.0 @12");
		customLabels.put(52.1, "245 > 52.1 @12");
		customLabels.put(54.0, "245 > 54.0 @12");
		customLabels.put(55.0, "245 > 55.0 @12");
		customLabels.put(56.0, "245 > 56.0 @12");
		customLabels.put(57.0, "245 > 57.0 @12");
		customLabels.put(60.0, "245 > 60.0 @12");
		customLabels.put(61.0, "245 > 61.0 @12");
		customLabels.put(62.0, "245 > 62.0 @12");
		customLabels.put(63.0, "245 > 63.0 @12");
		customLabels.put(64.0, "245 > 64.0 @12");
		customLabels.put(65.0, "245 > 65.0 @12");
		customLabels.put(66.2, "245 > 66.2 @12");
		customLabels.put(67.0, "245 > 67.0 @12");
		customLabels.put(68.0, "245 > 68.0 @12");
		customLabels.put(69.0, "245 > 69.0 @12");
		customLabels.put(70.0, "245 > 70.0 @12");
		customLabels.put(71.0, "245 > 71.0 @12");
		customLabels.put(73.0, "245 > 73.0 @12");
		customLabels.put(74.0, "245 > 74.0 @12");
		customLabels.put(75.0, "245 > 75.0 @12");
		customLabels.put(77.1, "245 > 77.1 @12");
		customLabels.put(78.0, "245 > 78.0 @12");
		customLabels.put(79.0, "245 > 79.0 @12");
		customLabels.put(80.0, "245 > 80.0 @12");
		customLabels.put(81.0, "245 > 81.0 @12");
		customLabels.put(82.0, "245 > 82.0 @12");
		customLabels.put(83.0, "245 > 83.0 @12");
		customLabels.put(84.0, "245 > 84.0 @12");
		customLabels.put(85.0, "245 > 85.0 @12");
		customLabels.put(86.0, "245 > 86.0 @12");
		customLabels.put(87.0, "245 > 87.0 @12");
		customLabels.put(89.0, "245 > 89.0 @12");
		customLabels.put(90.0, "245 > 90.0 @12");
		customLabels.put(91.0, "245 > 91.0 @12");
		customLabels.put(93.0, "245 > 93.0 @12");
		customLabels.put(94.0, "245 > 94.0 @12");
		customLabels.put(95.0, "245 > 95.0 @12");
		customLabels.put(96.0, "245 > 96.0 @12");
		customLabels.put(97.0, "245 > 97.0 @12");
		customLabels.put(102.0, "245 > 102.0 @12");
		customLabels.put(103.0, "245 > 103.0 @12");
		customLabels.put(105.1, "245 > 105.1 @12");
		customLabels.put(106.2, "245 > 106.2 @12");
		customLabels.put(107.0, "245 > 107.0 @12");
		customLabels.put(118.0, "245 > 118.0 @12");
		customLabels.put(119.0, "245 > 119.0 @12");
		customLabels.put(122.1, "245 > 122.1 @12");
		customLabels.put(123.0, "245 > 123.0 @12");
		customLabels.put(124.0, "245 > 124.0 @12");
		customLabels.put(133.0, "245 > 133.0 @12");
		customLabels.put(134.0, "245 > 134.0 @12");
		customLabels.put(135.0, "245 > 135.0 @12");
		customLabels.put(137.0, "245 > 137.0 @12");
		customLabels.put(149.0, "245 > 149.0 @12");
		customLabels.put(150.0, "245 > 150.0 @12");
		customLabels.put(191.0, "245 > 191.0 @12");
		//
		return customLabels;
	}
}
