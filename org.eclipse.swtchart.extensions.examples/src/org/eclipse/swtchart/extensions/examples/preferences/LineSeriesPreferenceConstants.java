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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.preferences;

import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;

public class LineSeriesPreferenceConstants {

	public static final String POSTFIX = "LineSeries";
	//
	public static final String P_ENABLE_RANGE_SELECTOR = "enableRangeSelector" + POSTFIX;
	public static final boolean DEF_ENABLE_RANGE_SELECTOR = true;
	public static final String P_SHOW_RANGE_SELECTOR_INITIALLY = "showRangeSelectorInitially" + POSTFIX;
	public static final boolean DEF_SHOW_RANGE_SELECTOR_INITIALLY = true;
	public static final String P_COLOR_HINT_RANGE_SELECTOR = "colorHintRangeSelector" + POSTFIX;
	public static final String DEF_COLOR_HINT_RANGE_SELECTOR = "255,0,0";
	public static final String P_RANGE_SELECTOR_DEFAULT_AXIS_X = "rangeSelectorDefaultAxisX" + POSTFIX;
	public static final int DEF_RANGE_SELECTOR_DEFAULT_AXIS_X = 0; // Index
	public static final String P_RANGE_SELECTOR_DEFAULT_AXIS_Y = "rangeSelectorDefaultAxisY" + POSTFIX;
	public static final int DEF_RANGE_SELECTOR_DEFAULT_AXIS_Y = 0; // Index
	//
	public static final String P_VERTICAL_SLIDER_VISIBLE = "verticalSliderVisible" + POSTFIX;
	public static final boolean DEF_VERTICAL_SLIDER_VISIBLE = false;
	public static final String P_HORIZONTAL_SLIDER_VISIBLE = "HorizontalSliderVisible" + POSTFIX;
	public static final boolean DEF_HORIZONTALSLIDER_VISIBLE = true;
	//
	public static final String P_TITLE = "title" + POSTFIX;
	public static final String DEF_TITLE = "Line Series";
	public static final String P_TITLE_VISIBLE = "titleVisible" + POSTFIX;
	public static final boolean DEF_TITLE_VISIBLE = false;
	public static final String P_TITLE_COLOR = "titleColor" + POSTFIX;
	public static final String DEF_TITLE_COLOR = "0,0,0";
	//
	public static final String P_LEGEND_POSITION = "legendPosition" + POSTFIX;
	public static final int DEF_LEGEND_POSITION = SWT.RIGHT;
	public static final String P_LEGEND_VISIBLE = "legendVisible" + POSTFIX;
	public static final boolean DEF_LEGEND_VISIBLE = false;
	//
	public static final String P_ORIENTATION = "orientation" + POSTFIX;
	public static final int DEF_ORIENTATION = SWT.HORIZONTAL;
	public static final String P_BACKGROUND = "background" + POSTFIX;
	public static final String DEF_BACKGROUND = "255,255,255";
	public static final String P_BACKGROUND_CHART = "backgroundChart" + POSTFIX;
	public static final String DEF_BACKGROUND_CHART = "255,255,255";
	public static final String P_BACKGROUND_PLOT_AREA = "backgroundPlotArea" + POSTFIX;
	public static final String DEF_BACKGROUND_PLOT_AREA = "255,255,255";
	//
	public static final String P_ENABLE_COMPRESS = "enableCompress" + POSTFIX;
	public static final boolean DEF_ENABLE_COMPRESS = true;
	public static final String P_ZERO_Y = "zeroY" + POSTFIX;
	public static final boolean DEF_ZERO_Y = true;
	public static final String P_ZERO_X = "zeroX" + POSTFIX;
	public static final boolean DEF_ZERO_X = true;
	public static final String P_RESTRICT_ZOOM = "restrictZoom" + POSTFIX;
	public static final boolean DEF_RESTRICT_ZOOM = true;
	public static final String P_X_ZOOM_ONLY = "xZoomOnly" + POSTFIX;
	public static final boolean DEF_X_ZOOM_ONLY = false;
	public static final String P_Y_ZOOM_ONLY = "yZoomOnly" + POSTFIX;
	public static final boolean DEF_Y_ZOOM_ONLY = false;
	public static final String P_FORCE_ZERO_MIN_Y = "forceZeroMinY" + POSTFIX;
	public static final boolean DEF_FORCE_ZERO_MIN_Y = false;
	public static final String P_EXTEND_TYPE_X = "extendTypeX" + POSTFIX;
	public static final String DEF_EXTEND_TYPE_X = RangeRestriction.ExtendType.RELATIVE.toString();
	public static final String P_EXTEND_MIN_X = "extendMinX" + POSTFIX;
	public static final double DEF_EXTEND_MIN_X = 0.0d;
	public static final String P_EXTEND_MAX_X = "extendMaxX" + POSTFIX;
	public static final double DEF_EXTEND_MAX_X = 0.0d;
	public static final String P_EXTEND_TYPE_Y = "extendTypeY" + POSTFIX;
	public static final String DEF_EXTEND_TYPE_Y = RangeRestriction.ExtendType.RELATIVE.toString();
	public static final String P_EXTEND_MIN_Y = "extendMinY" + POSTFIX;
	public static final double DEF_EXTEND_MIN_Y = 0.0d;
	public static final String P_EXTEND_MAX_Y = "extendMaxY" + POSTFIX;
	public static final double DEF_EXTEND_MAX_Y = 0.0d;
	//
	public static final String P_SHOW_POSITION_MARKER = "showPositionMarker" + POSTFIX;
	public static final boolean DEF_SHOW_POSITION_MARKER = true;
	public static final String P_COLOR_POSITION_MARKER = "colorPositionMarker" + POSTFIX;
	public static final String DEF_COLOR_POSITION_MARKER = "100,100,100";
	public static final String P_SHOW_PLOT_CENTER_MARKER = "showPlotCenterMarker" + POSTFIX;
	public static final boolean DEF_SHOW_PLOT_CENTER_MARKER = true;
	public static final String P_COLOR_PLOT_CENTER_MARKER = "colorPlotCenterMarker" + POSTFIX;
	public static final String DEF_COLOR_PLOT_CENTER_MARKER = "100,100,100";
	public static final String P_SHOW_LEGEND_MARKER = "showLegendMarker" + POSTFIX;
	public static final boolean DEF_SHOW_LEGEND_MARKER = true;
	public static final String P_COLOR_LEGEND_MARKER = "colorLegendMarker" + POSTFIX;
	public static final String DEF_COLOR_LEGEND_MARKER = "100,100,100";
	public static final String P_SHOW_AXIS_ZERO_MARKER = "showAxisZeroMarker" + POSTFIX;
	public static final boolean DEF_SHOW_AXIS_ZERO_MARKER = false;
	public static final String P_COLOR_AXIS_ZERO_MARKER = "colorAxisZeroMarker" + POSTFIX;
	public static final String DEF_COLOR_AXIS_ZERO_MARKER = "100,100,100";
	public static final String P_SHOW_SERIES_LABEL_MARKER = "showSeriesLabelMarker" + POSTFIX;
	public static final boolean DEF_SHOW_SERIES_LABEL_MARKER = false;
	public static final String P_COLOR_SERIES_LABEL_MARKER = "colorSeriesLabelMarker" + POSTFIX;
	public static final String DEF_COLOR_SERIES_LABEL_MARKER = "100,100,100";
	//
	public static final String P_CREATE_MENU = "createMenu" + POSTFIX;
	public static final boolean DEF_CREATE_MENU = true;
	//
	public static final String P_PRIMARY_X_AXIS_TITLE = "primaryXAxisTitle" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_TITLE = "Milliseconds";
	public static final String P_PRIMARY_X_AXIS_DESCRIPTION = "primaryXAxisDescription" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_DESCRIPTION = "Retention Time (milliseconds)";
	public static final String P_PRIMARY_X_AXIS_DECIMAL_FORMAT_PATTERN = "primaryXAxisDecimalFormatPattern" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_DECIMAL_FORMAT_PATTERN = "0.0##";
	public static final String P_PRIMARY_X_AXIS_DECIMAL_FORMAT_LOCALE = "primaryXAxisDecimalFormatLocale" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_DECIMAL_FORMAT_LOCALE = Locale.ENGLISH.getLanguage();
	public static final String P_PRIMARY_X_AXIS_COLOR = "primaryXAxisColor" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_COLOR = "0,0,0";
	public static final String P_PRIMARY_X_AXIS_POSITION = "primaryXAxisPosition" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_POSITION = Position.Secondary.toString();
	public static final String P_PRIMARY_X_AXIS_VISIBLE = "primaryXAxisVisible" + POSTFIX;
	public static final boolean DEF_PRIMARY_X_AXIS_VISIBLE = false;
	public static final String P_PRIMARY_X_AXIS_GRID_LINE_STYLE = "primaryXAxisGridLineStyle" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_GRID_LINE_STYLE = LineStyle.NONE.toString();
	public static final String P_PRIMARY_X_AXIS_ENABLE_LOG_SCALE = "primaryXAxisEnableLogScale" + POSTFIX;
	public static final boolean DEF_PRIMARY_X_AXIS_ENABLE_LOG_SCALE = false;
	public static final String P_PRIMARY_X_AXIS_EXTRA_SPACE_TITLE = "primaryXAxisExtraSpaceTitle" + POSTFIX;
	public static final int DEF_PRIMARY_X_AXIS_EXTRA_SPACE_TITLE = 25;
	//
	public static final String P_PRIMARY_Y_AXIS_TITLE = "primaryYAxisTitle" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_TITLE = "Intensity";
	public static final String P_PRIMARY_Y_AXIS_DESCRIPTION = "primaryYAxisDescription" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_DESCRIPTION = "Intensity";
	public static final String P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_PATTERN = "primaryYAxisDecimalFormatPattern" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_DECIMAL_FORMAT_PATTERN = "0.0#E0";
	public static final String P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_LOCALE = "primaryYAxisDecimalFormatLocale" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_DECIMAL_FORMAT_LOCALE = Locale.ENGLISH.getLanguage();
	public static final String P_PRIMARY_Y_AXIS_COLOR = "primaryYAxisColor" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_COLOR = "0,0,0";
	public static final String P_PRIMARY_Y_AXIS_POSITION = "primaryYAxisPosition" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_POSITION = Position.Primary.toString();
	public static final String P_PRIMARY_Y_AXIS_VISIBLE = "primaryYAxisVisible" + POSTFIX;
	public static final boolean DEF_PRIMARY_Y_AXIS_VISIBLE = true;
	public static final String P_PRIMARY_Y_AXIS_GRID_LINE_STYLE = "primaryYAxisGridLineStyle" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_GRID_LINE_STYLE = LineStyle.NONE.toString();
	public static final String P_PRIMARY_Y_AXIS_ENABLE_LOG_SCALE = "primaryYAxisEnableLogScale" + POSTFIX;
	public static final boolean DEF_PRIMARY_Y_AXIS_ENABLE_LOG_SCALE = false;
	public static final String P_PRIMARY_Y_AXIS_EXTRA_SPACE_TITLE = "primaryYAxisExtraSpaceTitle" + POSTFIX;
	public static final int DEF_PRIMARY_Y_AXIS_EXTRA_SPACE_TITLE = 25;
	//
	public static final String P_SECONDARY_X_AXIS_TITLE = "secondaryXAxisTitle" + POSTFIX;
	public static final String DEF_SECONDARY_X_AXIS_TITLE = "Min";
	public static final String P_SECONDARY_X_AXIS_DESCRIPTION = "secondaryXAxisDescription" + POSTFIX;
	public static final String DEF_SECONDARY_X_AXIS_DESCRIPTION = "Minutes";
	public static final String P_SECONDARY_X_AXIS_DECIMAL_FORMAT_PATTERN = "secondaryXAxisDecimalFormatPattern" + POSTFIX;
	public static final String DEF_SECONDARY_X_AXIS_DECIMAL_FORMAT_PATTERN = "0.0##";
	public static final String P_SECONDARY_X_AXIS_DECIMAL_FORMAT_LOCALE = "secondaryXAxisDecimalFormatLocale" + POSTFIX;
	public static final String DEF_SECONDARY_X_AXIS_DECIMAL_FORMAT_LOCALE = Locale.ENGLISH.getLanguage();
	public static final String P_SECONDARY_X_AXIS_COLOR = "secondaryXAxisColor" + POSTFIX;
	public static final String DEF_SECONDARY_X_AXIS_COLOR = "0,0,0";
	public static final String P_SECONDARY_X_AXIS_POSITION = "secondaryXAxisPosition" + POSTFIX;
	public static final String DEF_SECONDARY_X_AXIS_POSITION = Position.Primary.toString();
	public static final String P_SECONDARY_X_AXIS_VISIBLE = "secondaryXAxisVisible" + POSTFIX;
	public static final boolean DEF_SECONDARY_X_AXIS_VISIBLE = true;
	public static final String P_SECONDARY_X_AXIS_GRID_LINE_STYLE = "secondaryXAxisGridLineStyle" + POSTFIX;
	public static final String DEF_SECONDARY_X_AXIS_GRID_LINE_STYLE = LineStyle.DOT.toString();
	public static final String P_SECONDARY_X_AXIS_ENABLE_LOG_SCALE = "secondaryXAxisEnableLogScale" + POSTFIX;
	public static final boolean DEF_SECONDARY_X_AXIS_ENABLE_LOG_SCALE = false;
	public static final String P_SECONDARY_X_AXIS_EXTRA_SPACE_TITLE = "secondaryXAxisExtraSpaceTitle" + POSTFIX;
	public static final int DEF_SECONDARY_X_AXIS_EXTRA_SPACE_TITLE = 25;
	//
	public static final String P_SECONDARY_Y_AXIS_TITLE = "secondaryYAxisTitle" + POSTFIX;
	public static final String DEF_SECONDARY_Y_AXIS_TITLE = "Int [%]";
	public static final String P_SECONDARY_Y_AXIS_DESCRIPTION = "secondaryYAxisDescription" + POSTFIX;
	public static final String DEF_SECONDARY_Y_AXIS_DESCRIPTION = "Relative Intensity [%]";
	public static final String P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_PATTERN = "secondaryYAxisDecimalFormatPattern" + POSTFIX;
	public static final String DEF_SECONDARY_Y_AXIS_DECIMAL_FORMAT_PATTERN = "0.00";
	public static final String P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_LOCALE = "secondaryYAxisDecimalFormatLocale" + POSTFIX;
	public static final String DEF_SECONDARY_Y_AXIS_DECIMAL_FORMAT_LOCALE = Locale.ENGLISH.getLanguage();
	public static final String P_SECONDARY_Y_AXIS_COLOR = "secondaryYAxisColor" + POSTFIX;
	public static final String DEF_SECONDARY_Y_AXIS_COLOR = "0,0,0";
	public static final String P_SECONDARY_Y_AXIS_POSITION = "secondaryYAxisPosition" + POSTFIX;
	public static final String DEF_SECONDARY_Y_AXIS_POSITION = Position.Secondary.toString();
	public static final String P_SECONDARY_Y_AXIS_VISIBLE = "secondaryYAxisVisible" + POSTFIX;
	public static final boolean DEF_SECONDARY_Y_AXIS_VISIBLE = true;
	public static final String P_SECONDARY_Y_AXIS_GRID_LINE_STYLE = "secondaryYAxisGridLineStyle" + POSTFIX;
	public static final String DEF_SECONDARY_Y_AXIS_GRID_LINE_STYLE = LineStyle.DOT.toString();
	public static final String P_SECONDARY_Y_AXIS_ENABLE_LOG_SCALE = "secondaryYAxisEnableLogScale" + POSTFIX;
	public static final boolean DEF_SECONDARY_Y_AXIS_ENABLE_LOG_SCALE = false;
	public static final String P_SECONDARY_Y_AXIS_EXTRA_SPACE_TITLE = "secondaryYAxisExtraSpaceTitle" + POSTFIX;
	public static final int DEF_SECONDARY_Y_AXIS_EXTRA_SPACE_TITLE = 25;
	//
	public static final String P_DESCRIPTION_SERIES_1 = "descriptionSeries1" + POSTFIX;
	public static final String DEF_DESCRIPTION_SERIES_1 = "Measurement 1";
	//
	public static final String P_ANTIALIAS_SERIES_1 = "antialiasSeries1" + POSTFIX;
	public static final int DEF_ANTIALIAS_SERIES_1 = SWT.DEFAULT;
	public static final String P_ENABLE_AREA_SERIES_1 = "enableAreaSeries1" + POSTFIX;
	public static final boolean DEF_ENABLE_AREA_SERIES_1 = true;
	public static final String P_ENABLE_STACK_SERIES_1 = "enableStackSeries1" + POSTFIX;
	public static final boolean DEF_ENABLE_STACK_SERIES_1 = false;
	public static final String P_ENABLE_STEP_SERIES_1 = "enableStepSeries1" + POSTFIX;
	public static final boolean DEF_ENABLE_STEP_SERIES_1 = false;
	public static final String P_LINE_COLOR_SERIES_1 = "lineColorSeries1" + POSTFIX;
	public static final String DEF_LINE_COLOR_SERIES_1 = "255,0,0";
	public static final String P_LINE_STYLE_SERIES_1 = "lineStyleSeries1" + POSTFIX;
	public static final String DEF_LINE_STYLE_SERIES_1 = LineStyle.SOLID.toString();
	public static final String P_LINE_WIDTH_SERIES_1 = "lineWidthSeries1" + POSTFIX;
	public static final int DEF_LINE_WIDTH_SERIES_1 = 1;
	public static final String P_SYMBOL_COLOR_SERIES_1 = "symbolColorSeries1" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_1 = "0,0,0";
	public static final String P_SYMBOL_SIZE_SERIES_1 = "symbolSizeSeries1" + POSTFIX;
	public static final int DEF_SYMBOL_SIZE_SERIES_1 = 8;
	public static final String P_SYMBOL_TYPE_SERIES_1 = "symbolTypeSeries1" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_1 = PlotSymbolType.NONE.toString();
	public static final String P_VISIBLE_SERIES_1 = "visibleSeries1" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_1 = true;
	public static final String P_VISIBLE_IN_LEGEND_SERIES_1 = "visibleInLegendSeries1" + POSTFIX;
	public static final boolean DEF_VISIBLE_IN_LEGEND_SERIES_1 = true;
	//
	public static final String P_ANTIALIAS_SERIES_1_HIGHLIGHT = "antialiasSeries1Highlight" + POSTFIX;
	public static final int DEF_ANTIALIAS_SERIES_1_HIGHLIGHT = SWT.DEFAULT;
	public static final String P_ENABLE_AREA_SERIES_1_HIGHLIGHT = "enableAreaSeries1Highlight" + POSTFIX;
	public static final boolean DEF_ENABLE_AREA_SERIES_1_HIGHLIGHT = true;
	public static final String P_ENABLE_STACK_SERIES_1_HIGHLIGHT = "enableStackSeries1Highlight" + POSTFIX;
	public static final boolean DEF_ENABLE_STACK_SERIES_1_HIGHLIGHT = false;
	public static final String P_ENABLE_STEP_SERIES_1_HIGHLIGHT = "enableStepSeries1Highlight" + POSTFIX;
	public static final boolean DEF_ENABLE_STEP_SERIES_1_HIGHLIGHT = false;
	public static final String P_LINE_COLOR_SERIES_1_HIGHLIGHT = "lineColorSeries1Highlight" + POSTFIX;
	public static final String DEF_LINE_COLOR_SERIES_1_HIGHLIGHT = "255,0,0";
	public static final String P_LINE_STYLE_SERIES_1_HIGHLIGHT = "lineStyleSeries1Highlight" + POSTFIX;
	public static final String DEF_LINE_STYLE_SERIES_1_HIGHLIGHT = LineStyle.SOLID.toString();
	public static final String P_LINE_WIDTH_SERIES_1_HIGHLIGHT = "lineWidthSeries1Highlight" + POSTFIX;
	public static final int DEF_LINE_WIDTH_SERIES_1_HIGHLIGHT = 1;
	public static final String P_SYMBOL_COLOR_SERIES_1_HIGHLIGHT = "symbolColorSeries1Highlight" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_1_HIGHLIGHT = "0,0,0";
	public static final String P_SYMBOL_SIZE_SERIES_1_HIGHLIGHT = "symbolSizeSeries1Highlight" + POSTFIX;
	public static final int DEF_SYMBOL_SIZE_SERIES_1_HIGHLIGHT = 8;
	public static final String P_SYMBOL_TYPE_SERIES_1_HIGHLIGHT = "symbolTypeSeries1Highlight" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_1_HIGHLIGHT = PlotSymbolType.NONE.toString();
	public static final String P_VISIBLE_SERIES_1_HIGHLIGHT = "visibleSeries1Highlight" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_1_HIGHLIGHT = true;
	public static final String P_VISIBLE_IN_LEGEND_SERIES_1_HIGHLIGHT = "visibleInLegendSeries1Highlight" + POSTFIX;
	public static final boolean DEF_VISIBLE_IN_LEGEND_SERIES_1_HIGHLIGHT = true;
	//
	public static final String P_DESCRIPTION_SERIES_2 = "descriptionSeries2" + POSTFIX;
	public static final String DEF_DESCRIPTION_SERIES_2 = "Measurement 2";
	//
	public static final String P_ANTIALIAS_SERIES_2 = "antialiasSeries2" + POSTFIX;
	public static final int DEF_ANTIALIAS_SERIES_2 = SWT.DEFAULT;
	public static final String P_ENABLE_AREA_SERIES_2 = "enableAreaSeries2" + POSTFIX;
	public static final boolean DEF_ENABLE_AREA_SERIES_2 = true;
	public static final String P_ENABLE_STACK_SERIES_2 = "enableStackSeries2" + POSTFIX;
	public static final boolean DEF_ENABLE_STACK_SERIES_2 = false;
	public static final String P_ENABLE_STEP_SERIES_2 = "enableStepSeries2" + POSTFIX;
	public static final boolean DEF_ENABLE_STEP_SERIES_2 = false;
	public static final String P_LINE_COLOR_SERIES_2 = "lineColorSeries2" + POSTFIX;
	public static final String DEF_LINE_COLOR_SERIES_2 = "125,125,125";
	public static final String P_LINE_STYLE_SERIES_2 = "lineStyleSeries2" + POSTFIX;
	public static final String DEF_LINE_STYLE_SERIES_2 = LineStyle.SOLID.toString();
	public static final String P_LINE_WIDTH_SERIES_2 = "lineWidthSeries2" + POSTFIX;
	public static final int DEF_LINE_WIDTH_SERIES_2 = 1;
	public static final String P_SYMBOL_COLOR_SERIES_2 = "symbolColorSeries2" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_2 = "0,0,0";
	public static final String P_SYMBOL_SIZE_SERIES_2 = "symbolSizeSeries2" + POSTFIX;
	public static final int DEF_SYMBOL_SIZE_SERIES_2 = 8;
	public static final String P_SYMBOL_TYPE_SERIES_2 = "symbolTypeSeries2" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_2 = PlotSymbolType.NONE.toString();
	public static final String P_VISIBLE_SERIES_2 = "visibleSeries2" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_2 = true;
	public static final String P_VISIBLE_IN_LEGEND_SERIES_2 = "visibleInLegendSeries2" + POSTFIX;
	public static final boolean DEF_VISIBLE_IN_LEGEND_SERIES_2 = true;
	//
	public static final String P_ANTIALIAS_SERIES_2_HIGHLIGHT = "antialiasSeries2Highlight" + POSTFIX;
	public static final int DEF_ANTIALIAS_SERIES_2_HIGHLIGHT = SWT.DEFAULT;
	public static final String P_ENABLE_AREA_SERIES_2_HIGHLIGHT = "enableAreaSeries2Highlight" + POSTFIX;
	public static final boolean DEF_ENABLE_AREA_SERIES_2_HIGHLIGHT = true;
	public static final String P_ENABLE_STACK_SERIES_2_HIGHLIGHT = "enableStackSeries2Highlight" + POSTFIX;
	public static final boolean DEF_ENABLE_STACK_SERIES_2_HIGHLIGHT = false;
	public static final String P_ENABLE_STEP_SERIES_2_HIGHLIGHT = "enableStepSeries2Highlight" + POSTFIX;
	public static final boolean DEF_ENABLE_STEP_SERIES_2_HIGHLIGHT = false;
	public static final String P_LINE_COLOR_SERIES_2_HIGHLIGHT = "lineColorSeries2Highlight" + POSTFIX;
	public static final String DEF_LINE_COLOR_SERIES_2_HIGHLIGHT = "125,125,125";
	public static final String P_LINE_STYLE_SERIES_2_HIGHLIGHT = "lineStyleSeries2Highlight" + POSTFIX;
	public static final String DEF_LINE_STYLE_SERIES_2_HIGHLIGHT = LineStyle.SOLID.toString();
	public static final String P_LINE_WIDTH_SERIES_2_HIGHLIGHT = "lineWidthSeries2Highlight" + POSTFIX;
	public static final int DEF_LINE_WIDTH_SERIES_2_HIGHLIGHT = 2;
	public static final String P_SYMBOL_COLOR_SERIES_2_HIGHLIGHT = "symbolColorSeries2Highlight" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_2_HIGHLIGHT = "0,0,0";
	public static final String P_SYMBOL_SIZE_SERIES_2_HIGHLIGHT = "symbolSizeSeries2Highlight" + POSTFIX;
	public static final int DEF_SYMBOL_SIZE_SERIES_2_HIGHLIGHT = 8;
	public static final String P_SYMBOL_TYPE_SERIES_2_HIGHLIGHT = "symbolTypeSeries2Highlight" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_2_HIGHLIGHT = PlotSymbolType.NONE.toString();
	public static final String P_VISIBLE_SERIES_2_HIGHLIGHT = "visibleSeries2Highlight" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_2_HIGHLIGHT = true;
	public static final String P_VISIBLE_IN_LEGEND_SERIES_2_HIGHLIGHT = "visibleInLegendSeries2Highlight" + POSTFIX;
	public static final boolean DEF_VISIBLE_IN_LEGEND_SERIES_2_HIGHLIGHT = true;
}
