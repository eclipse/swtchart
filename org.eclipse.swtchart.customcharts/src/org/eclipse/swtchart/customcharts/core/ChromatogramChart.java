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
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.menu.ResetChartHandler;

public class ChromatogramChart extends LineChart {

	public ChromatogramChart() {

		super();
		initialize();
	}

	public ChromatogramChart(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	private void initialize() {

		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = getChartSettings();
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(true);
		chartSettings.getRangeRestriction().setZeroX(true);
		chartSettings.getRangeRestriction().setZeroY(true);
		chartSettings.removeMenuEntry(chartSettings.getChartMenuEntryByClass(ResetChartHandler.class));
		chartSettings.addMenuEntry(new ResetChromatogramHandler());
		//
		setPrimaryAxisSet(chartSettings);
		addSecondaryAxisSet(chartSettings);
		applySettings(chartSettings);
	}

	private void setPrimaryAxisSet(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(Messages.RETENTION_TIME);
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.0##"), new DecimalFormatSymbols(Locale.ENGLISH))); //$NON-NLS-1$
		primaryAxisSettingsX.setColor(getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		primaryAxisSettingsX.setPosition(Position.Secondary);
		primaryAxisSettingsX.setVisible(false);
		primaryAxisSettingsX.setGridLineStyle(LineStyle.NONE);
		//
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(Messages.INTENSITY);
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.0#E0"), new DecimalFormatSymbols(Locale.ENGLISH))); //$NON-NLS-1$
		primaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		primaryAxisSettingsY.setGridLineStyle(LineStyle.NONE);
	}

	private void addSecondaryAxisSet(IChartSettings chartSettings) {

		ISecondaryAxisSettings secondaryAxisSettingsX = new SecondaryAxisSettings(Messages.MINUTES, new MillisecondsToMinuteConverter());
		secondaryAxisSettingsX.setPosition(Position.Primary);
		secondaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH))); //$NON-NLS-1$
		secondaryAxisSettingsX.setColor(getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		chartSettings.getSecondaryAxisSettingsListX().add(secondaryAxisSettingsX);
		//
		ISecondaryAxisSettings secondaryAxisSettingsY = new SecondaryAxisSettings(Messages.RELATIVE_INTENSITY, new PercentageConverter(SWT.VERTICAL, true));
		secondaryAxisSettingsY.setPosition(Position.Secondary);
		secondaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH))); //$NON-NLS-1$
		secondaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY);
	}
}
