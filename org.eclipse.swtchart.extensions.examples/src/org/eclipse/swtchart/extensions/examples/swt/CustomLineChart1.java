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
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.swt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.axisconverter.MillisecondsToMinuteConverter;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class CustomLineChart1 extends LineChart {

	private static final int LENGTH_HINT_DATA_POINTS = 15000;
	//
	private boolean enableRangeSelector;
	private boolean showAxisTitle;
	private boolean enableHorizontalSlider;
	private String seriesXY;

	public CustomLineChart1(Composite parent, int style, boolean enableRangeSelector, boolean showAxisTitle, boolean enableHorizontalSlider, String seriesXY) {

		super(parent, style);
		this.enableRangeSelector = enableRangeSelector;
		this.showAxisTitle = showAxisTitle;
		this.enableHorizontalSlider = enableHorizontalSlider;
		this.seriesXY = seriesXY;
		createControl();
	}

	private void createControl() {

		configureChart();
		addDemoSeries();
	}

	private void configureChart() {

		try {
			IChartSettings chartSettings = getChartSettings();
			chartSettings.setOrientation(SWT.HORIZONTAL);
			chartSettings.setEnableRangeSelector(enableRangeSelector);
			chartSettings.setHorizontalSliderVisible(enableHorizontalSlider);
			chartSettings.setVerticalSliderVisible(enableHorizontalSlider);
			chartSettings.getRangeRestriction().setZeroX(true);
			chartSettings.getRangeRestriction().setZeroY(true);
			//
			IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
			primaryAxisSettingsX.setTitle("Time [ms]");
			primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.0##"), new DecimalFormatSymbols(Locale.ENGLISH)));
			primaryAxisSettingsX.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
			primaryAxisSettingsX.setPosition(Position.Secondary);
			primaryAxisSettingsX.setVisible(false);
			primaryAxisSettingsX.setGridLineStyle(LineStyle.NONE);
			//
			IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
			primaryAxisSettingsY.setTitle("Intensity [counts]");
			primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.0#E0"), new DecimalFormatSymbols(Locale.ENGLISH)));
			primaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
			primaryAxisSettingsY.setPosition(Position.Primary);
			primaryAxisSettingsY.setVisible(true);
			primaryAxisSettingsY.setGridLineStyle(LineStyle.NONE);
			/*
			 * Secondary X-Axes
			 */
			String axisTitle = (showAxisTitle) ? "Minutes" : "";
			ISecondaryAxisSettings secondaryAxisSettingsX1 = new SecondaryAxisSettings(axisTitle, "Time [min]", new MillisecondsToMinuteConverter());
			secondaryAxisSettingsX1.setPosition(Position.Primary);
			secondaryAxisSettingsX1.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
			secondaryAxisSettingsX1.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
			chartSettings.getSecondaryAxisSettingsListX().add(secondaryAxisSettingsX1);
			/*
			 * Secondary Y-Axes
			 */
			ISecondaryAxisSettings secondaryAxisSettingsY1 = new SecondaryAxisSettings("Intensity [%]", new PercentageConverter(SWT.VERTICAL, true));
			secondaryAxisSettingsY1.setPosition(Position.Secondary);
			secondaryAxisSettingsY1.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
			secondaryAxisSettingsY1.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
			chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY1);
			//
			applySettings(chartSettings);
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	private void addDemoSeries() {

		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		ISeriesData seriesData = SeriesConverter.getSeriesXY(seriesXY);
		//
		ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
		ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setEnableArea(true);
		ILineSeriesSettings lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
		lineSeriesSettingsHighlight.setLineWidth(2);
		lineSeriesDataList.add(lineSeriesData);
		/*
		 * Set series.
		 */
		addSeriesData(lineSeriesDataList, LENGTH_HINT_DATA_POINTS);
	}
}
