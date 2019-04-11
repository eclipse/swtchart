/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.customcharts;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisSettings;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.internal.support.BarSeriesIon;
import org.eclipse.swtchart.extensions.internal.support.BarSeriesIonComparator;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ICustomPaintListener;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.ISeries;

public class MassSpectrumChart extends BarChart {

	public enum LabelOption {
		NOMIMAL, EXACT, CUSTOM;
	}

	private static final DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat();
	//
	private int numberOfHighestIntensitiesToLabel;
	private BarSeriesIonComparator barSeriesIonComparator;
	private LabelOption labelOption;
	private Map<Double, String> customLabels;

	public MassSpectrumChart() {
		super();
		initialize();
	}

	public MassSpectrumChart(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	public void setNumberOfHighestIntensitiesToLabel(int numberOfHighestIntensitiesToLabel) {

		if(numberOfHighestIntensitiesToLabel >= 0) {
			this.numberOfHighestIntensitiesToLabel = numberOfHighestIntensitiesToLabel;
		} else {
			this.numberOfHighestIntensitiesToLabel = 0;
		}
	}

	public void setLabelOption(LabelOption labelOption) {

		this.labelOption = labelOption;
	}

	public void setCustomLabels(Map<Double, String> customLabels) {

		if(customLabels != null) {
			this.customLabels = customLabels;
		} else {
			customLabels = new HashMap<Double, String>();
		}
	}

	private void initialize() {

		numberOfHighestIntensitiesToLabel = 5;
		barSeriesIonComparator = new BarSeriesIonComparator();
		labelOption = LabelOption.EXACT;
		customLabels = new HashMap<Double, String>();
		//
		IChartSettings chartSettings = getChartSettings();
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(true);
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setZeroX(false);
		rangeRestriction.setZeroY(false);
		rangeRestriction.setRestrictZoom(true);
		rangeRestriction.setExtendTypeX(RangeRestriction.ExtendType.ABSOLUTE);
		rangeRestriction.setExtendMinX(2.0d);
		rangeRestriction.setExtendMaxX(2.0d);
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtendMaxY(0.1d);
		//
		setPrimaryAxisSet(chartSettings);
		addSecondaryAxisSet(chartSettings);
		applySettings(chartSettings);
		//
		addSeriesLabelMarker();
	}

	private void setPrimaryAxisSet(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("m/z");
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.0##"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsX.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		//
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("Intensity");
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.0#E0"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
	}

	private void addSecondaryAxisSet(IChartSettings chartSettings) {

		ISecondaryAxisSettings secondaryAxisSettingsY = new SecondaryAxisSettings("Relative Intensity [%]", new PercentageConverter(SWT.VERTICAL, true));
		secondaryAxisSettingsY.setPosition(Position.Secondary);
		secondaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
		secondaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY);
	}

	private void addSeriesLabelMarker() {

		/*
		 * Plot the series name above the entry.
		 */
		IPlotArea plotArea = (IPlotArea)getBaseChart().getPlotArea();
		plotArea.addCustomPaintListener(new ICustomPaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				List<BarSeriesIon> barSeriesIons = getBarSeriesIonList();
				Collections.sort(barSeriesIons, barSeriesIonComparator);
				int barSeriesSize = barSeriesIons.size();
				int limit;
				/*
				 * Positive
				 */
				limit = numberOfHighestIntensitiesToLabel;
				for(int i = 0; i < limit; i++) {
					if(i < barSeriesSize) {
						BarSeriesIon barSeriesIon = barSeriesIons.get(i);
						printLabel(barSeriesIon, e);
					}
				}
				/*
				 * Negative
				 */
				limit = barSeriesIons.size() - numberOfHighestIntensitiesToLabel;
				limit = (limit < 0) ? 0 : limit;
				for(int i = barSeriesIons.size() - 1; i >= limit; i--) {
					BarSeriesIon barSeriesIon = barSeriesIons.get(i);
					if(barSeriesIon.getIntensity() < 0) {
						printLabel(barSeriesIon, e);
					}
				}
			}

			@Override
			public boolean drawBehindSeries() {

				return false;
			}
		});
	}

	private void printLabel(BarSeriesIon barSeriesIon, PaintEvent e) {

		Point point = barSeriesIon.getPoint();
		String label = getLabel(barSeriesIon.getMz());
		boolean negative = (barSeriesIon.getIntensity() < 0) ? true : false;
		Point labelSize = e.gc.textExtent(label);
		int x = (int)(point.x + 0.5d - labelSize.x / 2.0d);
		int y = point.y;
		if(!negative) {
			y = point.y - labelSize.y;
		}
		e.gc.drawText(label, x, y, true);
	}

	private String getLabel(double mz) {

		String label;
		switch(labelOption) {
			case NOMIMAL:
				label = Integer.toString((int)mz);
				break;
			case EXACT:
				DecimalFormat decimalFormat = getDecimalFormatMZ();
				label = decimalFormat.format(mz);
				break;
			case CUSTOM:
				label = customLabels.get(mz);
				if(label == null) {
					label = "";
				}
				break;
			default:
				label = "";
		}
		return label;
	}

	private List<BarSeriesIon> getBarSeriesIonList() {

		List<BarSeriesIon> barSeriesIons = new ArrayList<BarSeriesIon>();
		//
		int widthPlotArea = getBaseChart().getPlotArea().getBounds().width;
		ISeries[] series = getBaseChart().getSeriesSet().getSeries();
		for(ISeries barSeries : series) {
			if(barSeries != null) {
				//
				double[] xSeries = barSeries.getXSeries();
				double[] ySeries = barSeries.getYSeries();
				int size = barSeries.getXSeries().length;
				//
				for(int i = 0; i < size; i++) {
					Point point = barSeries.getPixelCoordinates(i);
					if(point.x >= 0 && point.x <= widthPlotArea) {
						barSeriesIons.add(new BarSeriesIon(xSeries[i], ySeries[i], point));
					}
				}
			}
		}
		return barSeriesIons;
	}

	private DecimalFormat getDecimalFormatMZ() {

		IAxisSettings axisSettings = getBaseChart().getXAxisSettings(BaseChart.ID_PRIMARY_X_AXIS);
		if(axisSettings != null) {
			return axisSettings.getDecimalFormat();
		} else {
			return DEFAULT_DECIMAL_FORMAT;
		}
	}
}
