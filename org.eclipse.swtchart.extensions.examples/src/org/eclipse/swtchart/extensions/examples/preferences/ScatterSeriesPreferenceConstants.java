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
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.RangeRestriction;

public class ScatterSeriesPreferenceConstants {

	public static final String POSTFIX = "ScatterSeries";
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
	public static final boolean DEF_VERTICAL_SLIDER_VISIBLE = true;
	public static final String P_HORIZONTAL_SLIDER_VISIBLE = "HorizontalSliderVisible" + POSTFIX;
	public static final boolean DEF_HORIZONTALSLIDER_VISIBLE = true;
	//
	public static final String P_TITLE = "title" + POSTFIX;
	public static final String DEF_TITLE = "Scatter Series";
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
	public static final boolean DEF_ZERO_Y = false;
	public static final String P_ZERO_X = "zeroX" + POSTFIX;
	public static final boolean DEF_ZERO_X = false;
	public static final String P_RESTRICT_ZOOM = "restrictZoom" + POSTFIX;
	public static final boolean DEF_RESTRICT_ZOOM = false;
	public static final String P_X_ZOOM_ONLY = "xZoomOnly" + POSTFIX;
	public static final boolean DEF_X_ZOOM_ONLY = false;
	public static final String P_Y_ZOOM_ONLY = "yZoomOnly" + POSTFIX;
	public static final boolean DEF_Y_ZOOM_ONLY = false;
	public static final String P_FORCE_ZERO_MIN_Y = "forceZeroMinY" + POSTFIX;
	public static final boolean DEF_FORCE_ZERO_MIN_Y = false;
	public static final String P_EXTEND_TYPE_X = "extendTypeX" + POSTFIX;
	public static final String DEF_EXTEND_TYPE_X = RangeRestriction.ExtendType.RELATIVE.toString();
	public static final String P_EXTEND_MIN_X = "extendMinX" + POSTFIX;
	public static final double DEF_EXTEND_MIN_X = 0.1d;
	public static final String P_EXTEND_MAX_X = "extendMaxX" + POSTFIX;
	public static final double DEF_EXTEND_MAX_X = 0.1d;
	public static final String P_EXTEND_TYPE_Y = "extendTypeY" + POSTFIX;
	public static final String DEF_EXTEND_TYPE_Y = RangeRestriction.ExtendType.RELATIVE.toString();
	public static final String P_EXTEND_MIN_Y = "extendMinY" + POSTFIX;
	public static final double DEF_EXTEND_MIN_Y = 0.1d;
	public static final String P_EXTEND_MAX_Y = "extendMaxY" + POSTFIX;
	public static final double DEF_EXTEND_MAX_Y = 0.1d;
	//
	public static final String P_SHOW_POSITION_MARKER = "showPositionMarker" + POSTFIX;
	public static final boolean DEF_SHOW_POSITION_MARKER = true;
	public static final String P_COLOR_POSITION_MARKER = "colorPositionMarker" + POSTFIX;
	public static final String DEF_COLOR_POSITION_MARKER = "100,100,100";
	public static final String P_SHOW_PLOT_CENTER_MARKER = "showPlotCenterMarker" + POSTFIX;
	public static final boolean DEF_SHOW_PLOT_CENTER_MARKER = false;
	public static final String P_COLOR_PLOT_CENTER_MARKER = "colorPlotCenterMarker" + POSTFIX;
	public static final String DEF_COLOR_PLOT_CENTER_MARKER = "100,100,100";
	public static final String P_SHOW_LEGEND_MARKER = "showLegendMarker" + POSTFIX;
	public static final boolean DEF_SHOW_LEGEND_MARKER = false;
	public static final String P_COLOR_LEGEND_MARKER = "colorLegendMarker" + POSTFIX;
	public static final String DEF_COLOR_LEGEND_MARKER = "100,100,100";
	public static final String P_SHOW_AXIS_ZERO_MARKER = "showAxisZeroMarker" + POSTFIX;
	public static final boolean DEF_SHOW_AXIS_ZERO_MARKER = true;
	public static final String P_COLOR_AXIS_ZERO_MARKER = "colorAxisZeroMarker" + POSTFIX;
	public static final String DEF_COLOR_AXIS_ZERO_MARKER = "100,100,100";
	public static final String P_SHOW_SERIES_LABEL_MARKER = "showSeriesLabelMarker" + POSTFIX;
	public static final boolean DEF_SHOW_SERIES_LABEL_MARKER = true;
	public static final String P_COLOR_SERIES_LABEL_MARKER = "colorSeriesLabelMarker" + POSTFIX;
	public static final String DEF_COLOR_SERIES_LABEL_MARKER = "0,0,0";
	//
	public static final String P_CREATE_MENU = "createMenu" + POSTFIX;
	public static final boolean DEF_CREATE_MENU = true;
	//
	public static final String P_PRIMARY_X_AXIS_TITLE = "primaryXAxisTitle" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_TITLE = "PC1";
	public static final String P_PRIMARY_X_AXIS_DESCRIPTION = "primaryXAxisDescription" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_DESCRIPTION = "Principal Component 1";
	public static final String P_PRIMARY_X_AXIS_DECIMAL_FORMAT_PATTERN = "primaryXAxisDecimalFormatPattern" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_DECIMAL_FORMAT_PATTERN = "0";
	public static final String P_PRIMARY_X_AXIS_DECIMAL_FORMAT_LOCALE = "primaryXAxisDecimalFormatLocale" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_DECIMAL_FORMAT_LOCALE = Locale.ENGLISH.getLanguage();
	public static final String P_PRIMARY_X_AXIS_COLOR = "primaryXAxisColor" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_COLOR = "0,0,0";
	public static final String P_PRIMARY_X_AXIS_POSITION = "primaryXAxisPosition" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_POSITION = Position.Primary.toString();
	public static final String P_PRIMARY_X_AXIS_VISIBLE = "primaryXAxisVisible" + POSTFIX;
	public static final boolean DEF_PRIMARY_X_AXIS_VISIBLE = true;
	public static final String P_PRIMARY_X_AXIS_GRID_LINE_STYLE = "primaryXAxisGridLineStyle" + POSTFIX;
	public static final String DEF_PRIMARY_X_AXIS_GRID_LINE_STYLE = LineStyle.DOT.toString();
	public static final String P_PRIMARY_X_AXIS_ENABLE_LOG_SCALE = "primaryXAxisEnableLogScale" + POSTFIX;
	public static final boolean DEF_PRIMARY_X_AXIS_ENABLE_LOG_SCALE = false;
	public static final String P_PRIMARY_X_AXIS_EXTRA_SPACE_TITLE = "primaryXAxisExtraSpaceTitle" + POSTFIX;
	public static final int DEF_PRIMARY_X_AXIS_EXTRA_SPACE_TITLE = 25;
	//
	public static final String P_PRIMARY_Y_AXIS_TITLE = "primaryYAxisTitle" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_TITLE = "PC2";
	public static final String P_PRIMARY_Y_AXIS_DESCRIPTION = "primaryYAxisDescription" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_DESCRIPTION = "Principal Component 2";
	public static final String P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_PATTERN = "primaryYAxisDecimalFormatPattern" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_DECIMAL_FORMAT_PATTERN = "0.000";
	public static final String P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_LOCALE = "primaryYAxisDecimalFormatLocale" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_DECIMAL_FORMAT_LOCALE = Locale.ENGLISH.getLanguage();
	public static final String P_PRIMARY_Y_AXIS_COLOR = "primaryYAxisColor" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_COLOR = "0,0,0";
	public static final String P_PRIMARY_Y_AXIS_POSITION = "primaryYAxisPosition" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_POSITION = Position.Primary.toString();
	public static final String P_PRIMARY_Y_AXIS_VISIBLE = "primaryYAxisVisible" + POSTFIX;
	public static final boolean DEF_PRIMARY_Y_AXIS_VISIBLE = true;
	public static final String P_PRIMARY_Y_AXIS_GRID_LINE_STYLE = "primaryYAxisGridLineStyle" + POSTFIX;
	public static final String DEF_PRIMARY_Y_AXIS_GRID_LINE_STYLE = LineStyle.DOT.toString();
	public static final String P_PRIMARY_Y_AXIS_ENABLE_LOG_SCALE = "primaryYAxisEnableLogScale" + POSTFIX;
	public static final boolean DEF_PRIMARY_Y_AXIS_ENABLE_LOG_SCALE = false;
	public static final String P_PRIMARY_Y_AXIS_EXTRA_SPACE_TITLE = "primaryYAxisExtraSpaceTitle" + POSTFIX;
	public static final int DEF_PRIMARY_Y_AXIS_EXTRA_SPACE_TITLE = 25;
	//
	public static final String P_SYMBOL_SIZE_SERIES = "symbolSizeSeries" + POSTFIX;
	public static final int DEF_SYMBOL_SIZE_SERIES = 8;
	//
	public static final String P_SYMBOL_COLOR_SERIES_LEFT_TOP = "symbolColorSeriesLeftTop" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_LEFT_TOP = "255,0,255";
	public static final String P_SYMBOL_TYPE_SERIES_LEFT_TOP = "symbolTypeSeriesLeftTop" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_LEFT_TOP = PlotSymbolType.DIAMOND.toString();
	public static final String P_VISIBLE_SERIES_LEFT_TOP = "visibleSeriesLeftTop" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_LEFT_TOP = true;
	//
	public static final String P_SYMBOL_COLOR_SERIES_RIGHT_TOP = "symbolColorSeriesRightTop" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_RIGHT_TOP = "255,0,0";
	public static final String P_SYMBOL_TYPE_SERIES_RIGHT_TOP = "symbolTypeSeriesRightTop" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_RIGHT_TOP = PlotSymbolType.SQUARE.toString();
	public static final String P_VISIBLE_SERIES_RIGHT_TOP = "visibleSeriesRightTop" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_RIGHT_TOP = true;
	//
	public static final String P_SYMBOL_COLOR_SERIES_LEFT_BOTTOM = "symbolColorSeriesLeftBottom" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_LEFT_BOTTOM = "0,255,255";
	public static final String P_SYMBOL_TYPE_SERIES_LEFT_BOTTOM = "symbolTypeSeriesLeftBottom" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_LEFT_BOTTOM = PlotSymbolType.INVERTED_TRIANGLE.toString();
	public static final String P_VISIBLE_SERIES_LEFT_BOTTOM = "visibleSeriesLeftBottom" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_LEFT_BOTTOM = true;
	//
	public static final String P_SYMBOL_COLOR_SERIES_RIGHT_BOTTOM = "symbolColorSeriesRightBottom" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_RIGHT_BOTTOM = "0,0,255";
	public static final String P_SYMBOL_TYPE_SERIES_RIGHT_BOTTOM = "symbolTypeSeriesRightBottom" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_RIGHT_BOTTOM = PlotSymbolType.TRIANGLE.toString();
	public static final String P_VISIBLE_SERIES_RIGHT_BOTTOM = "visibleSeriesRightBottom" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_RIGHT_BOTTOM = true;
	//
	public static final String P_SYMBOL_COLOR_SERIES_HIGHLIGHT_LEFT_TOP = "symbolColorSeriesHighlightLeftTop" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_HIGHLIGHT_LEFT_TOP = "255,0,255";
	public static final String P_SYMBOL_TYPE_SERIES_HIGHLIGHT_LEFT_TOP = "symbolTypeSeriesHighlightLeftTop" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_HIGHLIGHT_LEFT_TOP = PlotSymbolType.DIAMOND.toString();
	public static final String P_VISIBLE_SERIES_HIGHLIGHT_LEFT_TOP = "visibleSeriesHighlightLeftTop" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_HIGHLIGHT_LEFT_TOP = true;
	//
	public static final String P_SYMBOL_COLOR_SERIES_HIGHLIGHT_RIGHT_TOP = "symbolColorSeriesHighlightRightTop" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_HIGHLIGHT_RIGHT_TOP = "255,0,0";
	public static final String P_SYMBOL_TYPE_SERIES_HIGHLIGHT_RIGHT_TOP = "symbolTypeSeriesHighlightRightTop" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_HIGHLIGHT_RIGHT_TOP = PlotSymbolType.SQUARE.toString();
	public static final String P_VISIBLE_SERIES_HIGHLIGHT_RIGHT_TOP = "visibleSeriesHighlightRightTop" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_HIGHLIGHT_RIGHT_TOP = true;
	//
	public static final String P_SYMBOL_COLOR_SERIES_HIGHLIGHT_LEFT_BOTTOM = "symbolColorSeriesHighlightLeftBottom" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_HIGHLIGHT_LEFT_BOTTOM = "0,255,255";
	public static final String P_SYMBOL_TYPE_SERIES_HIGHLIGHT_LEFT_BOTTOM = "symbolTypeSeriesHighlightLeftBottom" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_HIGHLIGHT_LEFT_BOTTOM = PlotSymbolType.INVERTED_TRIANGLE.toString();
	public static final String P_VISIBLE_SERIES_HIGHLIGHT_LEFT_BOTTOM = "visibleSeriesHighlightLeftBottom" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_HIGHLIGHT_LEFT_BOTTOM = true;
	//
	public static final String P_SYMBOL_COLOR_SERIES_HIGHLIGHT_RIGHT_BOTTOM = "symbolColorSeriesHighlightRightBottom" + POSTFIX;
	public static final String DEF_SYMBOL_COLOR_SERIES_HIGHLIGHT_RIGHT_BOTTOM = "0,0,255";
	public static final String P_SYMBOL_TYPE_SERIES_HIGHLIGHT_RIGHT_BOTTOM = "symbolTypeSeriesHighlightRightBottom" + POSTFIX;
	public static final String DEF_SYMBOL_TYPE_SERIES_HIGHLIGHT_RIGHT_BOTTOM = PlotSymbolType.TRIANGLE.toString();
	public static final String P_VISIBLE_SERIES_HIGHLIGHT_RIGHT_BOTTOM = "visibleSeriesHighlightRightBottom" + POSTFIX;
	public static final boolean DEF_VISIBLE_SERIES_HIGHLIGHT_RIGHT_BOTTOM = true;
}
