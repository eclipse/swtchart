/*******************************************************************************
 * Copyright (c) 2017, 2022 Lablicate GmbH.
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.examples.Activator;
import org.eclipse.swtchart.extensions.examples.preferences.BarSeriesDataPreferencePage;
import org.eclipse.swtchart.extensions.examples.preferences.BarSeriesPreferenceConstants;
import org.eclipse.swtchart.extensions.examples.preferences.BarSeriesPreferencePage;
import org.eclipse.swtchart.extensions.examples.preferences.BarSeriesPrimaryAxesPreferencePage;
import org.eclipse.swtchart.extensions.examples.preferences.BarSeriesSecondaryAxesPreferencePage;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;

public class BarSeries_Preferences_Part extends Composite {

	private BarChart barChart;
	private Map<RGB, Color> colors;

	@Inject
	public BarSeries_Preferences_Part(Composite parent) {

		super(parent, SWT.NONE);
		colors = new HashMap<>();
		try {
			initialize();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void dispose() {

		for(Color color : colors.values()) {
			color.dispose();
		}
		super.dispose();
	}

	private void initialize() throws Exception {

		this.setLayout(new GridLayout(1, true));
		/*
		 * Buttons
		 */
		Composite compositeButtons = new Composite(this, SWT.NONE);
		GridData gridDataComposite = new GridData(GridData.FILL_HORIZONTAL);
		gridDataComposite.horizontalAlignment = SWT.END;
		compositeButtons.setLayoutData(gridDataComposite);
		compositeButtons.setLayout(new GridLayout(1, false));
		//
		Button buttonOpenSettings = new Button(compositeButtons, SWT.PUSH);
		modifySettingsButton(buttonOpenSettings);
		buttonOpenSettings.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IPreferencePage preferencePage = new BarSeriesPreferencePage();
				preferencePage.setTitle("Chart Settings");
				IPreferencePage preferencePrimaryAxesPage = new BarSeriesPrimaryAxesPreferencePage();
				preferencePrimaryAxesPage.setTitle("Primary Axes");
				IPreferencePage preferenceSecondaryAxesPage = new BarSeriesSecondaryAxesPreferencePage();
				preferenceSecondaryAxesPage.setTitle("Secondary Axes");
				IPreferencePage preferenceDataPage = new BarSeriesDataPreferencePage();
				preferenceDataPage.setTitle("Series Data");
				//
				PreferenceManager preferenceManager = new PreferenceManager();
				preferenceManager.addToRoot(new PreferenceNode("1", preferencePage));
				preferenceManager.addToRoot(new PreferenceNode("2", preferencePrimaryAxesPage));
				preferenceManager.addToRoot(new PreferenceNode("3", preferenceSecondaryAxesPage));
				preferenceManager.addToRoot(new PreferenceNode("4", preferenceDataPage));
				//
				//
				PreferenceDialog preferenceDialog = new PreferenceDialog(e.display.getActiveShell(), preferenceManager);
				preferenceDialog.create();
				preferenceDialog.setMessage("Settings");
				if(preferenceDialog.open() == Window.OK) {
					try {
						applyChartSettings();
						applySeriesSettings();
					} catch(Exception e1) {
						MessageDialog.openError(e.display.getActiveShell(), "Settings", "Something has gone wrong to apply the chart settings.");
					}
				}
			}
		});
		//
		barChart = new BarChart(this, SWT.NONE);
		barChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		applyChartSettings();
		applySeriesSettings();
	}

	private void modifySettingsButton(Button button) {

		button.setToolTipText("Open the Settings");
		button.setText(Activator.getDefault() != null ? "" : "Settings");
		button.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_OPEN_SETTINGS) : null);
	}

	private void applyChartSettings() throws Exception {

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//
		Color colorHintRangeSelector = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_COLOR_HINT_RANGE_SELECTOR));
		Color colorTitle = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_TITLE_COLOR));
		Color colorBackground = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_BACKGROUND));
		Color colorBackgroundChart = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_BACKGROUND_CHART));
		Color colorBackgroundPlotArea = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_BACKGROUND_PLOT_AREA));
		Color colorPrimaryXAxis = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_COLOR));
		Color colorPrimaryYAxis = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_COLOR));
		Color colorSecondaryYAxis = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_COLOR));
		Locale localePrimaryXAxis = new Locale(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_DECIMAL_FORMAT_LOCALE));
		Locale localePrimaryYAxis = new Locale(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_LOCALE));
		Locale localeSecondaryYAxis = new Locale(preferenceStore.getString(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_LOCALE));
		Color colorPositionMarker = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_COLOR_POSITION_MARKER));
		Color colorPlotCenterMarker = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_COLOR_PLOT_CENTER_MARKER));
		Color colorLegendMarker = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_COLOR_LEGEND_MARKER));
		Color colorAxisZeroMarker = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_COLOR_AXIS_ZERO_MARKER));
		Color colorSeriesLabelMarker = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_COLOR_SERIES_LABEL_MARKER));
		//
		IChartSettings chartSettings = barChart.getChartSettings();
		chartSettings.setEnableRangeSelector(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_ENABLE_RANGE_SELECTOR));
		chartSettings.setShowRangeSelectorInitially(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_SHOW_RANGE_SELECTOR_INITIALLY));
		chartSettings.setColorHintRangeSelector(colorHintRangeSelector);
		chartSettings.setRangeSelectorDefaultAxisX(preferenceStore.getInt(BarSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_X));
		chartSettings.setRangeSelectorDefaultAxisY(preferenceStore.getInt(BarSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_Y));
		chartSettings.setVerticalSliderVisible(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_VERTICAL_SLIDER_VISIBLE));
		chartSettings.setHorizontalSliderVisible(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_HORIZONTAL_SLIDER_VISIBLE));
		chartSettings.setTitle(preferenceStore.getString(BarSeriesPreferenceConstants.P_TITLE));
		chartSettings.setTitleVisible(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_TITLE_VISIBLE));
		chartSettings.setTitleColor(colorTitle);
		chartSettings.setLegendPosition(preferenceStore.getInt(BarSeriesPreferenceConstants.P_LEGEND_POSITION));
		chartSettings.setLegendVisible(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_LEGEND_VISIBLE));
		chartSettings.setOrientation(preferenceStore.getInt(BarSeriesPreferenceConstants.P_ORIENTATION));
		chartSettings.setBackground(colorBackground);
		chartSettings.setBackgroundChart(colorBackgroundChart);
		chartSettings.setBackgroundPlotArea(colorBackgroundPlotArea);
		chartSettings.setEnableCompress(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_ENABLE_COMPRESS));
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setZeroX(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_ZERO_X));
		rangeRestriction.setZeroY(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_ZERO_Y));
		rangeRestriction.setRestrictFrame(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_RESTRICT_ZOOM));
		rangeRestriction.setRestrictSelectX(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_X_ZOOM_ONLY));
		rangeRestriction.setRestrictSelectY(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_Y_ZOOM_ONLY));
		rangeRestriction.setForceZeroMinY(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_FORCE_ZERO_MIN_Y));
		rangeRestriction.setExtendTypeX(RangeRestriction.ExtendType.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_EXTEND_TYPE_X)));
		rangeRestriction.setExtendMinX(preferenceStore.getDouble(BarSeriesPreferenceConstants.P_EXTEND_MIN_X));
		rangeRestriction.setExtendMaxX(preferenceStore.getDouble(BarSeriesPreferenceConstants.P_EXTEND_MAX_X));
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_EXTEND_TYPE_Y)));
		rangeRestriction.setExtendMinY(preferenceStore.getDouble(BarSeriesPreferenceConstants.P_EXTEND_MIN_Y));
		rangeRestriction.setExtendMaxY(preferenceStore.getDouble(BarSeriesPreferenceConstants.P_EXTEND_MAX_Y));
		//
		chartSettings.setShowPositionMarker(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_SHOW_POSITION_MARKER));
		chartSettings.setColorPositionMarker(colorPositionMarker);
		chartSettings.setShowPlotCenterMarker(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_SHOW_PLOT_CENTER_MARKER));
		chartSettings.setColorPlotCenterMarker(colorPlotCenterMarker);
		chartSettings.setShowLegendMarker(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_SHOW_LEGEND_MARKER));
		chartSettings.setColorLegendMarker(colorLegendMarker);
		chartSettings.setShowAxisZeroMarker(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_SHOW_AXIS_ZERO_MARKER));
		chartSettings.setColorLegendMarker(colorAxisZeroMarker);
		chartSettings.setShowSeriesLabelMarker(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_SHOW_SERIES_LABEL_MARKER));
		chartSettings.setColorSeriesLabelMarker(colorSeriesLabelMarker);
		//
		chartSettings.setCreateMenu(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_CREATE_MENU));
		/*
		 * Primary X-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_TITLE));
		primaryAxisSettingsX.setDescription(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_DESCRIPTION));
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat((preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_DECIMAL_FORMAT_PATTERN)), new DecimalFormatSymbols(localePrimaryXAxis)));
		primaryAxisSettingsX.setColor(colorPrimaryXAxis);
		primaryAxisSettingsX.setPosition(Position.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_POSITION)));
		primaryAxisSettingsX.setVisible(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_VISIBLE));
		primaryAxisSettingsX.setGridLineStyle(LineStyle.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_GRID_LINE_STYLE)));
		primaryAxisSettingsX.setEnableLogScale(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_ENABLE_LOG_SCALE));
		primaryAxisSettingsX.setLogScaleBase(preferenceStore.getDouble(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_LOG_SCALE_BASE));
		primaryAxisSettingsX.setExtraSpaceTitle(preferenceStore.getInt(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_EXTRA_SPACE_TITLE));
		/*
		 * Primary Y-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_TITLE));
		primaryAxisSettingsY.setDescription(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_DESCRIPTION));
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat((preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_PATTERN)), new DecimalFormatSymbols(localePrimaryYAxis)));
		primaryAxisSettingsY.setColor(colorPrimaryYAxis);
		primaryAxisSettingsY.setPosition(Position.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_POSITION)));
		primaryAxisSettingsY.setVisible(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_VISIBLE));
		primaryAxisSettingsY.setGridLineStyle(LineStyle.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_GRID_LINE_STYLE)));
		primaryAxisSettingsY.setEnableLogScale(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_ENABLE_LOG_SCALE));
		primaryAxisSettingsY.setLogScaleBase(preferenceStore.getDouble(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_LOG_SCALE_BASE));
		primaryAxisSettingsY.setExtraSpaceTitle(preferenceStore.getInt(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_EXTRA_SPACE_TITLE));
		/*
		 * Secondary Y-Axes
		 */
		chartSettings.getSecondaryAxisSettingsListY().clear();
		ISecondaryAxisSettings secondaryAxisSettingsY = new SecondaryAxisSettings(preferenceStore.getString(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_TITLE), new PercentageConverter(SWT.VERTICAL, true));
		secondaryAxisSettingsY.setDescription(preferenceStore.getString(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DESCRIPTION));
		secondaryAxisSettingsY.setDecimalFormat(new DecimalFormat((preferenceStore.getString(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_PATTERN)), new DecimalFormatSymbols(localeSecondaryYAxis)));
		secondaryAxisSettingsY.setColor(colorSecondaryYAxis);
		secondaryAxisSettingsY.setPosition(Position.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_POSITION)));
		secondaryAxisSettingsY.setVisible(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_VISIBLE));
		secondaryAxisSettingsY.setGridLineStyle(LineStyle.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_GRID_LINE_STYLE)));
		secondaryAxisSettingsY.setEnableLogScale(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_ENABLE_LOG_SCALE));
		secondaryAxisSettingsY.setLogScaleBase(preferenceStore.getDouble(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_LOG_SCALE_BASE));
		secondaryAxisSettingsY.setExtraSpaceTitle(preferenceStore.getInt(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_EXTRA_SPACE_TITLE));
		chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY);
		//
		barChart.applySettings(chartSettings);
	}

	private void applySeriesSettings() {

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		Color barColorSeries1 = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_BAR_COLOR_SERIES_1));
		Color barColorSeries1Highlight = getColor(PreferenceConverter.getColor(preferenceStore, BarSeriesPreferenceConstants.P_BAR_COLOR_SERIES_1_HIGHLIGHT));
		//
		barChart.deleteSeries();
		List<IBarSeriesData> barSeriesDataList = new ArrayList<IBarSeriesData>();
		ISeriesData seriesData;
		IBarSeriesData barSeriesData;
		IBarSeriesSettings barSeriesSettings;
		/*
		 * Series 1
		 */
		seriesData = SeriesConverter.getSeriesXY(SeriesConverter.BAR_SERIES_1);
		barSeriesData = new BarSeriesData(seriesData);
		barSeriesSettings = barSeriesData.getSettings();
		barSeriesSettings.setDescription(preferenceStore.getString(BarSeriesPreferenceConstants.P_DESCRIPTION_SERIES_1));
		//
		barSeriesSettings.setVisible(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_VISIBLE_SERIES_1));
		barSeriesSettings.setVisibleInLegend(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_VISIBLE_IN_LEGEND_SERIES_1));
		barSeriesSettings.setBarColor(barColorSeries1);
		barSeriesSettings.setBarPadding(preferenceStore.getInt(BarSeriesPreferenceConstants.P_BAR_PADDING_SERIES_1));
		barSeriesSettings.setBarWidth(preferenceStore.getInt(BarSeriesPreferenceConstants.P_BAR_WIDTH_SERIES_1));
		barSeriesSettings.setBarWidthStyle(BarWidthStyle.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_BAR_WIDTH_STYLE_SERIES_1)));
		//
		IBarSeriesSettings barSeriesSettingsHighlight = (IBarSeriesSettings)barSeriesSettings.getSeriesSettingsHighlight();
		barSeriesSettingsHighlight.setVisible(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_VISIBLE_SERIES_1_HIGHLIGHT));
		barSeriesSettingsHighlight.setVisibleInLegend(preferenceStore.getBoolean(BarSeriesPreferenceConstants.P_VISIBLE_IN_LEGEND_SERIES_1_HIGHLIGHT));
		barSeriesSettingsHighlight.setBarColor(barColorSeries1Highlight);
		barSeriesSettingsHighlight.setBarPadding(preferenceStore.getInt(BarSeriesPreferenceConstants.P_BAR_PADDING_SERIES_1_HIGHLIGHT));
		barSeriesSettingsHighlight.setBarWidth(preferenceStore.getInt(BarSeriesPreferenceConstants.P_BAR_WIDTH_SERIES_1_HIGHLIGHT));
		barSeriesSettingsHighlight.setBarWidthStyle(BarWidthStyle.valueOf(preferenceStore.getString(BarSeriesPreferenceConstants.P_BAR_WIDTH_STYLE_SERIES_1_HIGHLIGHT)));
		//
		barSeriesDataList.add(barSeriesData);
		//
		barChart.addSeriesData(barSeriesDataList);
	}

	private Color getColor(RGB rgb) {

		Color color = colors.get(rgb);
		if(color == null) {
			color = new Color(getDisplay(), rgb);
			colors.put(rgb, color);
		}
		return color;
	}
}