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
import org.eclipse.swtchart.extensions.axisconverter.MillisecondsToScanNumberConverter;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.examples.menu.ZoomInHandler;
import org.eclipse.swtchart.extensions.examples.menu.ZoomOutHandler;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

import jakarta.inject.Inject;

public class LineSeries_3_Part extends LineChart {

	@Inject
	public LineSeries_3_Part(Composite parent) {

		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
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
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(true);
		chartSettings.getRangeRestriction().setZeroX(true);
		chartSettings.getRangeRestriction().setZeroY(true);
		chartSettings.setEnableRangeSelector(true);
		chartSettings.setShowPositionMarker(true);
		chartSettings.setShowPlotCenterMarker(true);
		chartSettings.setShowLegendMarker(true);
		chartSettings.setCreateMenu(true);
		/*
		 * Primary X-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("Time [ms]");
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.0##"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsX.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		primaryAxisSettingsX.setPosition(Position.Secondary);
		primaryAxisSettingsX.setVisible(false);
		primaryAxisSettingsX.setGridLineStyle(LineStyle.NONE);
		/*
		 * Primary Y-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("Intensity [counts]");
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.0#E0"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		primaryAxisSettingsY.setGridLineStyle(LineStyle.NONE);
		/*
		 * Secondary X-Axes
		 */
		ISecondaryAxisSettings secondaryAxisSettingsX1 = new SecondaryAxisSettings("Scan Number", new MillisecondsToScanNumberConverter(50, 50));
		secondaryAxisSettingsX1.setPosition(Position.Primary);
		secondaryAxisSettingsX1.setDecimalFormat(new DecimalFormat(("0"), new DecimalFormatSymbols(Locale.ENGLISH)));
		secondaryAxisSettingsX1.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		secondaryAxisSettingsX1.setExtraSpaceTitle(0);
		chartSettings.getSecondaryAxisSettingsListX().add(secondaryAxisSettingsX1);
		//
		ISecondaryAxisSettings secondaryAxisSettingsX2 = new SecondaryAxisSettings("Time [min]", new MillisecondsToMinuteConverter());
		secondaryAxisSettingsX2.setPosition(Position.Primary);
		secondaryAxisSettingsX2.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
		secondaryAxisSettingsX2.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		secondaryAxisSettingsX2.setExtraSpaceTitle(0);
		chartSettings.getSecondaryAxisSettingsListX().add(secondaryAxisSettingsX2);
		/*
		 * Secondary Y-Axes
		 */
		ISecondaryAxisSettings secondaryAxisSettingsY1 = new SecondaryAxisSettings("Intensity [%]", new PercentageConverter(SWT.VERTICAL, true));
		secondaryAxisSettingsY1.setPosition(Position.Secondary);
		secondaryAxisSettingsY1.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
		secondaryAxisSettingsY1.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY1);
		/*
		 * Add menu items.
		 */
		chartSettings.addMenuEntry(new ZoomInHandler());
		chartSettings.addMenuEntry(new ZoomOutHandler());
		//
		applySettings(chartSettings);
		/*
		 * Create series.
		 */
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		ISeriesData seriesData = SeriesConverter.getSeriesXY(SeriesConverter.LINE_SERIES_3);
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
		addSeriesData(lineSeriesDataList);
	}
}
