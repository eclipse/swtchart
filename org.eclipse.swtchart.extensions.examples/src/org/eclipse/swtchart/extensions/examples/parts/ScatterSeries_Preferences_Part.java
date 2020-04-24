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
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.examples.Activator;
import org.eclipse.swtchart.extensions.examples.preferences.ScatterSeriesDataPreferencePage;
import org.eclipse.swtchart.extensions.examples.preferences.ScatterSeriesPreferenceConstants;
import org.eclipse.swtchart.extensions.examples.preferences.ScatterSeriesPreferencePage;
import org.eclipse.swtchart.extensions.examples.preferences.ScatterSeriesPrimaryAxesPreferencePage;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterChart;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesData;

public class ScatterSeries_Preferences_Part extends Composite {

	private ScatterChart scatterChart;
	private Map<RGB, Color> colors;

	@Inject
	public ScatterSeries_Preferences_Part(Composite parent) {
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

				IPreferencePage preferencePage = new ScatterSeriesPreferencePage();
				preferencePage.setTitle("Chart Settings");
				IPreferencePage preferencePrimaryAxesPage = new ScatterSeriesPrimaryAxesPreferencePage();
				preferencePrimaryAxesPage.setTitle("Primary Axes");
				IPreferencePage preferenceDataPage = new ScatterSeriesDataPreferencePage();
				preferenceDataPage.setTitle("Series Data");
				//
				PreferenceManager preferenceManager = new PreferenceManager();
				preferenceManager.addToRoot(new PreferenceNode("1", preferencePage));
				preferenceManager.addToRoot(new PreferenceNode("2", preferencePrimaryAxesPage));
				preferenceManager.addToRoot(new PreferenceNode("3", preferenceDataPage));
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
		scatterChart = new ScatterChart(this, SWT.NONE);
		scatterChart.setLayoutData(new GridData(GridData.FILL_BOTH));
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
		Color colorHintRangeSelector = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_COLOR_HINT_RANGE_SELECTOR));
		Color colorTitle = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_TITLE_COLOR));
		Color colorBackground = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_BACKGROUND));
		Color colorBackgroundChart = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_BACKGROUND_CHART));
		Color colorBackgroundPlotArea = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_BACKGROUND_PLOT_AREA));
		Color colorPrimaryXAxis = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_COLOR));
		Color colorPrimaryYAxis = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_COLOR));
		Locale localePrimaryXAxis = new Locale(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_DECIMAL_FORMAT_LOCALE));
		Locale localePrimaryYAxis = new Locale(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_LOCALE));
		Color colorPositionMarker = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_COLOR_POSITION_MARKER));
		Color colorPlotCenterMarker = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_COLOR_PLOT_CENTER_MARKER));
		Color colorLegendMarker = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_COLOR_LEGEND_MARKER));
		Color colorAxisZeroMarker = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_COLOR_AXIS_ZERO_MARKER));
		Color colorSeriesLabelMarker = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_COLOR_SERIES_LABEL_MARKER));
		//
		IChartSettings chartSettings = scatterChart.getChartSettings();
		chartSettings.setEnableRangeSelector(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_ENABLE_RANGE_SELECTOR));
		chartSettings.setShowRangeSelectorInitially(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_SHOW_RANGE_SELECTOR_INITIALLY));
		chartSettings.setColorHintRangeSelector(colorHintRangeSelector);
		chartSettings.setRangeSelectorDefaultAxisX(preferenceStore.getInt(ScatterSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_X));
		chartSettings.setRangeSelectorDefaultAxisY(preferenceStore.getInt(ScatterSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_Y));
		chartSettings.setVerticalSliderVisible(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_VERTICAL_SLIDER_VISIBLE));
		chartSettings.setHorizontalSliderVisible(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_HORIZONTAL_SLIDER_VISIBLE));
		chartSettings.setTitle(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_TITLE));
		chartSettings.setTitleVisible(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_TITLE_VISIBLE));
		chartSettings.setTitleColor(colorTitle);
		chartSettings.setLegendPosition(preferenceStore.getInt(ScatterSeriesPreferenceConstants.P_LEGEND_POSITION));
		chartSettings.setLegendVisible(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_LEGEND_VISIBLE));
		chartSettings.setOrientation(preferenceStore.getInt(ScatterSeriesPreferenceConstants.P_ORIENTATION));
		chartSettings.setBackground(colorBackground);
		chartSettings.setBackgroundChart(colorBackgroundChart);
		chartSettings.setBackgroundPlotArea(colorBackgroundPlotArea);
		chartSettings.setEnableCompress(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_ENABLE_COMPRESS));
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setZeroX(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_ZERO_X));
		rangeRestriction.setZeroY(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_ZERO_Y));
		rangeRestriction.setRestrictFrame(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_RESTRICT_ZOOM));
		rangeRestriction.setRestrictSelectX(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_X_ZOOM_ONLY));
		rangeRestriction.setRestrictSelectY(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_Y_ZOOM_ONLY));
		rangeRestriction.setForceZeroMinY(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_FORCE_ZERO_MIN_Y));
		rangeRestriction.setExtendTypeX(RangeRestriction.ExtendType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_EXTEND_TYPE_X)));
		rangeRestriction.setExtendMinX(preferenceStore.getDouble(ScatterSeriesPreferenceConstants.P_EXTEND_MIN_X));
		rangeRestriction.setExtendMaxX(preferenceStore.getDouble(ScatterSeriesPreferenceConstants.P_EXTEND_MAX_X));
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_EXTEND_TYPE_Y)));
		rangeRestriction.setExtendMinY(preferenceStore.getDouble(ScatterSeriesPreferenceConstants.P_EXTEND_MIN_Y));
		rangeRestriction.setExtendMaxY(preferenceStore.getDouble(ScatterSeriesPreferenceConstants.P_EXTEND_MAX_Y));
		//
		chartSettings.setShowPositionMarker(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_SHOW_POSITION_MARKER));
		chartSettings.setColorPositionMarker(colorPositionMarker);
		chartSettings.setShowPlotCenterMarker(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_SHOW_PLOT_CENTER_MARKER));
		chartSettings.setColorPlotCenterMarker(colorPlotCenterMarker);
		chartSettings.setShowLegendMarker(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_SHOW_LEGEND_MARKER));
		chartSettings.setColorLegendMarker(colorLegendMarker);
		chartSettings.setShowAxisZeroMarker(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_SHOW_AXIS_ZERO_MARKER));
		chartSettings.setColorAxisZeroMarker(colorAxisZeroMarker);
		chartSettings.setShowSeriesLabelMarker(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_SHOW_SERIES_LABEL_MARKER));
		chartSettings.setColorSeriesLabelMarker(colorSeriesLabelMarker);
		//
		chartSettings.setCreateMenu(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_CREATE_MENU));
		/*
		 * Primary X-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_TITLE));
		primaryAxisSettingsX.setDescription(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_DESCRIPTION));
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat((preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_DECIMAL_FORMAT_PATTERN)), new DecimalFormatSymbols(localePrimaryXAxis)));
		primaryAxisSettingsX.setColor(colorPrimaryXAxis);
		primaryAxisSettingsX.setPosition(Position.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_POSITION)));
		primaryAxisSettingsX.setVisible(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_VISIBLE));
		primaryAxisSettingsX.setGridLineStyle(LineStyle.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_GRID_LINE_STYLE)));
		primaryAxisSettingsX.setEnableLogScale(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_ENABLE_LOG_SCALE));
		primaryAxisSettingsX.setExtraSpaceTitle(preferenceStore.getInt(ScatterSeriesPreferenceConstants.P_PRIMARY_X_AXIS_EXTRA_SPACE_TITLE));
		/*
		 * Primary Y-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_TITLE));
		primaryAxisSettingsY.setDescription(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_DESCRIPTION));
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat((preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_PATTERN)), new DecimalFormatSymbols(localePrimaryYAxis)));
		primaryAxisSettingsY.setColor(colorPrimaryYAxis);
		primaryAxisSettingsY.setPosition(Position.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_POSITION)));
		primaryAxisSettingsY.setVisible(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_VISIBLE));
		primaryAxisSettingsY.setGridLineStyle(LineStyle.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_GRID_LINE_STYLE)));
		primaryAxisSettingsY.setEnableLogScale(preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_ENABLE_LOG_SCALE));
		primaryAxisSettingsY.setExtraSpaceTitle(preferenceStore.getInt(ScatterSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_EXTRA_SPACE_TITLE));
		//
		scatterChart.applySettings(chartSettings);
	}

	private void applySeriesSettings() {

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		int symbolSize = preferenceStore.getInt(ScatterSeriesPreferenceConstants.P_SYMBOL_SIZE_SERIES);
		//
		Color symbolColorSeriesLeftTop = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_LEFT_TOP));
		Color symbolColorSeriesRightTop = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_RIGHT_TOP));
		Color symbolColorSeriesLeftBottom = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_LEFT_BOTTOM));
		Color symbolColorSeriesRightBottom = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_RIGHT_BOTTOM));
		PlotSymbolType plotSymbolTypeLeftTop = PlotSymbolType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_LEFT_TOP));
		boolean isVisibleLeftTop = preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_LEFT_TOP);
		PlotSymbolType plotSymbolTypeRightTop = PlotSymbolType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_RIGHT_TOP));
		boolean isVisibleRightTop = preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_RIGHT_TOP);
		PlotSymbolType plotSymbolTypeLeftBottom = PlotSymbolType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_LEFT_BOTTOM));
		boolean isVisibleLeftBottom = preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_LEFT_BOTTOM);
		PlotSymbolType plotSymbolTypeRightBottom = PlotSymbolType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_RIGHT_BOTTOM));
		boolean isVisibleRightBottom = preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_RIGHT_BOTTOM);
		//
		Color symbolColorSeriesLeftTopHighlight = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_HIGHLIGHT_LEFT_TOP));
		Color symbolColorSeriesRightTopHighlight = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_HIGHLIGHT_RIGHT_TOP));
		Color symbolColorSeriesLeftBottomHighlight = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_HIGHLIGHT_LEFT_BOTTOM));
		Color symbolColorSeriesRightBottomHighlight = getColor(PreferenceConverter.getColor(preferenceStore, ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_HIGHLIGHT_RIGHT_BOTTOM));
		PlotSymbolType plotSymbolTypeLeftTopHighlight = PlotSymbolType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_HIGHLIGHT_LEFT_TOP));
		boolean isVisibleLeftTopHighlight = preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_HIGHLIGHT_LEFT_TOP);
		PlotSymbolType plotSymbolTypeRightTopHighlight = PlotSymbolType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_HIGHLIGHT_RIGHT_TOP));
		boolean isVisibleRightTopHighlight = preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_HIGHLIGHT_RIGHT_TOP);
		PlotSymbolType plotSymbolTypeLeftBottomHighlight = PlotSymbolType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_HIGHLIGHT_LEFT_BOTTOM));
		boolean isVisibleLeftBottomHighlight = preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_HIGHLIGHT_LEFT_BOTTOM);
		PlotSymbolType plotSymbolTypeRightBottomHighlight = PlotSymbolType.valueOf(preferenceStore.getString(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_HIGHLIGHT_RIGHT_BOTTOM));
		boolean isVisibleRightBottomHighlight = preferenceStore.getBoolean(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_HIGHLIGHT_RIGHT_BOTTOM);
		//
		scatterChart.deleteSeries();
		/*
		 * Data
		 */
		List<ISeriesData> scatterSeriesList = SeriesConverter.getSeriesScatter(SeriesConverter.SCATTER_SERIES_1);
		List<IScatterSeriesData> scatterSeriesDataList = new ArrayList<IScatterSeriesData>();
		//
		for(ISeriesData seriesData : scatterSeriesList) {
			IScatterSeriesData scatterSeriesData = new ScatterSeriesData(seriesData);
			IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getSettings();
			IScatterSeriesSettings scatterSeriesSettingsHighlight = (IScatterSeriesSettings)scatterSeriesSettings.getSeriesSettingsHighlight();
			/*
			 * Set the color and symbol type.
			 */
			double x = seriesData.getXSeries()[0];
			double y = seriesData.getYSeries()[0];
			scatterSeriesSettings.setSymbolSize(symbolSize);
			scatterSeriesSettingsHighlight.setSymbolSize(symbolSize);
			//
			if(x > 0 && y > 0) {
				scatterSeriesSettings.setSymbolColor(symbolColorSeriesRightTop);
				scatterSeriesSettings.setSymbolType(plotSymbolTypeRightTop);
				scatterSeriesSettings.setVisible(isVisibleRightTop);
				scatterSeriesSettingsHighlight.setSymbolColor(symbolColorSeriesRightTopHighlight);
				scatterSeriesSettingsHighlight.setSymbolType(plotSymbolTypeRightTopHighlight);
				scatterSeriesSettingsHighlight.setVisible(isVisibleRightTopHighlight);
			} else if(x > 0 && y < 0) {
				scatterSeriesSettings.setSymbolColor(symbolColorSeriesRightBottom);
				scatterSeriesSettings.setSymbolType(plotSymbolTypeRightBottom);
				scatterSeriesSettings.setVisible(isVisibleRightBottom);
				scatterSeriesSettingsHighlight.setSymbolColor(symbolColorSeriesRightBottomHighlight);
				scatterSeriesSettingsHighlight.setSymbolType(plotSymbolTypeRightBottomHighlight);
				scatterSeriesSettingsHighlight.setVisible(isVisibleRightBottomHighlight);
			} else if(x < 0 && y > 0) {
				scatterSeriesSettings.setSymbolColor(symbolColorSeriesLeftTop);
				scatterSeriesSettings.setSymbolType(plotSymbolTypeLeftTop);
				scatterSeriesSettings.setVisible(isVisibleLeftTop);
				scatterSeriesSettingsHighlight.setSymbolColor(symbolColorSeriesLeftTopHighlight);
				scatterSeriesSettingsHighlight.setSymbolType(plotSymbolTypeLeftTopHighlight);
				scatterSeriesSettingsHighlight.setVisible(isVisibleLeftTopHighlight);
			} else if(x < 0 && y < 0) {
				scatterSeriesSettings.setSymbolColor(symbolColorSeriesLeftBottom);
				scatterSeriesSettings.setSymbolType(plotSymbolTypeLeftBottom);
				scatterSeriesSettings.setVisible(isVisibleLeftBottom);
				scatterSeriesSettingsHighlight.setSymbolColor(symbolColorSeriesLeftBottomHighlight);
				scatterSeriesSettingsHighlight.setSymbolType(plotSymbolTypeLeftBottomHighlight);
				scatterSeriesSettingsHighlight.setVisible(isVisibleLeftBottomHighlight);
			}
			//
			scatterSeriesDataList.add(scatterSeriesData);
		}
		//
		scatterChart.addSeriesData(scatterSeriesDataList);
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
