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
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.customcharts.core;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.extensions.axisconverter.PassThroughConverter;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;
import org.eclipse.swtchart.extensions.scattercharts.ScatterChart;

public class PCAChart extends ScatterChart {

	private Color colorBlack = getDisplay().getSystemColor(SWT.COLOR_BLACK);
	//
	private String chartTitle = ""; //$NON-NLS-1$
	private String xAxisTitle = "PC1"; //$NON-NLS-1$
	private String yAxisTitle = "PC2"; //$NON-NLS-1$
	private DecimalFormat decimalFormat = new DecimalFormat(("0.0##"), new DecimalFormatSymbols(Locale.ENGLISH)); //$NON-NLS-1$

	public PCAChart() {

		super();
		initialize();
	}

	public PCAChart(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	@Override
	public void addSeriesData(List<IScatterSeriesData> scatterSeriesDataList) {

		super.addSeriesData(scatterSeriesDataList);
		int symbolSize = 0;
		for(IScatterSeriesData scatterSeriesData : scatterSeriesDataList) {
			symbolSize = Math.max(symbolSize, scatterSeriesData.getSettings().getSymbolSize());
		}
	}

	public void setTitles(String chartTitle, String xAxisTitle, String yAxisTitle) {

		this.chartTitle = chartTitle;
		this.xAxisTitle = xAxisTitle;
		this.yAxisTitle = yAxisTitle;
		//
		IChartSettings chartSettings = getChartSettings();
		chartSettings.setTitle(chartTitle);
		chartSettings.getPrimaryAxisSettingsX().setTitle(xAxisTitle);
		chartSettings.getPrimaryAxisSettingsY().setTitle(yAxisTitle);
		applySettings(chartSettings);
	}

	public void setDecimalFormat(DecimalFormat decimalFormat) {

		this.decimalFormat = decimalFormat;
		//
		IChartSettings chartSettings = getChartSettings();
		chartSettings.getPrimaryAxisSettingsX().setDecimalFormat(decimalFormat);
		chartSettings.getPrimaryAxisSettingsY().setDecimalFormat(decimalFormat);
		for(ISecondaryAxisSettings secondaryAxisSettings : chartSettings.getSecondaryAxisSettingsListX()) {
			secondaryAxisSettings.setDecimalFormat(decimalFormat);
		}
		for(ISecondaryAxisSettings secondaryAxisSettings : chartSettings.getSecondaryAxisSettingsListY()) {
			secondaryAxisSettings.setDecimalFormat(decimalFormat);
		}
		applySettings(chartSettings);
	}

	private void initialize() {

		IChartSettings chartSettings = getChartSettings();
		chartSettings.setTitle(chartTitle);
		chartSettings.setTitleVisible(true);
		chartSettings.setTitleColor(colorBlack);
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(false);
		chartSettings.setVerticalSliderVisible(true);
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setZeroX(false);
		rangeRestriction.setZeroY(false);
		rangeRestriction.setRestrictFrame(false);
		rangeRestriction.setExtendTypeX(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtend(0.25d);
		chartSettings.setShowAxisZeroMarker(true);
		chartSettings.setColorAxisZeroMarker(colorBlack);
		chartSettings.setShowSeriesLabelMarker(true);
		chartSettings.setColorSeriesLabelMarker(colorBlack);
		//
		setPrimaryAxisSet(chartSettings);
		addSecondaryAxisSet(chartSettings);
		//
		applySettings(chartSettings);
	}

	private void setPrimaryAxisSet(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(xAxisTitle);
		primaryAxisSettingsX.setDecimalFormat(decimalFormat);
		primaryAxisSettingsX.setColor(colorBlack);
		//
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(yAxisTitle);
		primaryAxisSettingsY.setDecimalFormat(decimalFormat);
		primaryAxisSettingsY.setColor(colorBlack);
	}

	private void addSecondaryAxisSet(IChartSettings chartSettings) {

		ISecondaryAxisSettings secondaryAxisSettingsX = new SecondaryAxisSettings(xAxisTitle, new PassThroughConverter());
		secondaryAxisSettingsX.setTitle(""); //$NON-NLS-1$
		secondaryAxisSettingsX.setPosition(Position.Secondary);
		secondaryAxisSettingsX.setDecimalFormat(decimalFormat);
		secondaryAxisSettingsX.setColor(colorBlack);
		chartSettings.getSecondaryAxisSettingsListX().add(secondaryAxisSettingsX);
		//
		ISecondaryAxisSettings secondaryAxisSettingsY = new SecondaryAxisSettings(yAxisTitle, new PassThroughConverter());
		secondaryAxisSettingsY.setPosition(Position.Secondary);
		secondaryAxisSettingsY.setDecimalFormat(decimalFormat);
		secondaryAxisSettingsY.setColor(colorBlack);
		chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY);
	}
}
