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
package org.eclipse.swtchart.extensions.examples.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swtchart.extensions.examples.Activator;

public class BarSeriesPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		/*
		 * Bar Series
		 */
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_ENABLE_RANGE_SELECTOR, BarSeriesPreferenceConstants.DEF_ENABLE_RANGE_SELECTOR);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SHOW_RANGE_SELECTOR_INITIALLY, BarSeriesPreferenceConstants.DEF_SHOW_RANGE_SELECTOR_INITIALLY);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_COLOR_HINT_RANGE_SELECTOR, BarSeriesPreferenceConstants.DEF_COLOR_HINT_RANGE_SELECTOR);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_X, BarSeriesPreferenceConstants.DEF_RANGE_SELECTOR_DEFAULT_AXIS_X);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_Y, BarSeriesPreferenceConstants.DEF_RANGE_SELECTOR_DEFAULT_AXIS_Y);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_VERTICAL_SLIDER_VISIBLE, BarSeriesPreferenceConstants.DEF_VERTICAL_SLIDER_VISIBLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_HORIZONTAL_SLIDER_VISIBLE, BarSeriesPreferenceConstants.DEF_HORIZONTALSLIDER_VISIBLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_TITLE, BarSeriesPreferenceConstants.DEF_TITLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_TITLE_VISIBLE, BarSeriesPreferenceConstants.DEF_TITLE_VISIBLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_TITLE_COLOR, BarSeriesPreferenceConstants.DEF_TITLE_COLOR);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_LEGEND_POSITION, BarSeriesPreferenceConstants.DEF_LEGEND_POSITION);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_LEGEND_VISIBLE, BarSeriesPreferenceConstants.DEF_LEGEND_VISIBLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_ORIENTATION, BarSeriesPreferenceConstants.DEF_ORIENTATION);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BACKGROUND, BarSeriesPreferenceConstants.DEF_BACKGROUND);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BACKGROUND_CHART, BarSeriesPreferenceConstants.DEF_BACKGROUND_CHART);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BACKGROUND_PLOT_AREA, BarSeriesPreferenceConstants.DEF_BACKGROUND_PLOT_AREA);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_ENABLE_COMPRESS, BarSeriesPreferenceConstants.DEF_ENABLE_COMPRESS);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_ZERO_X, BarSeriesPreferenceConstants.DEF_ZERO_X);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_ZERO_Y, BarSeriesPreferenceConstants.DEF_ZERO_Y);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_RESTRICT_ZOOM, BarSeriesPreferenceConstants.DEF_RESTRICT_ZOOM);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_X_ZOOM_ONLY, BarSeriesPreferenceConstants.DEF_X_ZOOM_ONLY);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_Y_ZOOM_ONLY, BarSeriesPreferenceConstants.DEF_Y_ZOOM_ONLY);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_FORCE_ZERO_MIN_Y, BarSeriesPreferenceConstants.DEF_FORCE_ZERO_MIN_Y);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_EXTEND_TYPE_X, BarSeriesPreferenceConstants.DEF_EXTEND_TYPE_X);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_EXTEND_MIN_X, BarSeriesPreferenceConstants.DEF_EXTEND_MIN_X);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_EXTEND_MAX_X, BarSeriesPreferenceConstants.DEF_EXTEND_MAX_X);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_EXTEND_TYPE_Y, BarSeriesPreferenceConstants.DEF_EXTEND_TYPE_Y);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_EXTEND_MIN_Y, BarSeriesPreferenceConstants.DEF_EXTEND_MIN_Y);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_EXTEND_MAX_Y, BarSeriesPreferenceConstants.DEF_EXTEND_MAX_Y);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SHOW_POSITION_MARKER, BarSeriesPreferenceConstants.DEF_SHOW_POSITION_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_COLOR_POSITION_MARKER, BarSeriesPreferenceConstants.DEF_COLOR_POSITION_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SHOW_PLOT_CENTER_MARKER, BarSeriesPreferenceConstants.DEF_SHOW_PLOT_CENTER_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_COLOR_PLOT_CENTER_MARKER, BarSeriesPreferenceConstants.DEF_COLOR_PLOT_CENTER_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SHOW_LEGEND_MARKER, BarSeriesPreferenceConstants.DEF_SHOW_LEGEND_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_COLOR_LEGEND_MARKER, BarSeriesPreferenceConstants.DEF_COLOR_LEGEND_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SHOW_AXIS_ZERO_MARKER, BarSeriesPreferenceConstants.DEF_SHOW_AXIS_ZERO_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_COLOR_AXIS_ZERO_MARKER, BarSeriesPreferenceConstants.DEF_COLOR_AXIS_ZERO_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SHOW_SERIES_LABEL_MARKER, BarSeriesPreferenceConstants.DEF_SHOW_SERIES_LABEL_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_COLOR_SERIES_LABEL_MARKER, BarSeriesPreferenceConstants.DEF_COLOR_SERIES_LABEL_MARKER);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_CREATE_MENU, BarSeriesPreferenceConstants.DEF_CREATE_MENU);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_TITLE, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_TITLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_DESCRIPTION, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_DESCRIPTION);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_DECIMAL_FORMAT_PATTERN, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_DECIMAL_FORMAT_PATTERN);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_DECIMAL_FORMAT_LOCALE, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_DECIMAL_FORMAT_LOCALE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_COLOR, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_COLOR);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_POSITION, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_POSITION);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_VISIBLE, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_VISIBLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_GRID_LINE_STYLE, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_GRID_LINE_STYLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_ENABLE_LOG_SCALE, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_ENABLE_LOG_SCALE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_LOG_SCALE_BASE, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_LOG_SCALE_BASE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_X_AXIS_EXTRA_SPACE_TITLE, BarSeriesPreferenceConstants.DEF_PRIMARY_X_AXIS_EXTRA_SPACE_TITLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_TITLE, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_TITLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_DESCRIPTION, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_DESCRIPTION);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_PATTERN, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_DECIMAL_FORMAT_PATTERN);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_DECIMAL_FORMAT_LOCALE, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_DECIMAL_FORMAT_LOCALE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_COLOR, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_COLOR);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_POSITION, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_POSITION);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_VISIBLE, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_VISIBLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_GRID_LINE_STYLE, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_GRID_LINE_STYLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_ENABLE_LOG_SCALE, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_ENABLE_LOG_SCALE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_LOG_SCALE_BASE, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_LOG_SCALE_BASE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_PRIMARY_Y_AXIS_EXTRA_SPACE_TITLE, BarSeriesPreferenceConstants.DEF_PRIMARY_Y_AXIS_EXTRA_SPACE_TITLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_TITLE, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_TITLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DESCRIPTION, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_DESCRIPTION);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_PATTERN, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_DECIMAL_FORMAT_PATTERN);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_LOCALE, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_DECIMAL_FORMAT_LOCALE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_COLOR, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_COLOR);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_POSITION, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_POSITION);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_VISIBLE, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_VISIBLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_GRID_LINE_STYLE, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_GRID_LINE_STYLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_ENABLE_LOG_SCALE, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_ENABLE_LOG_SCALE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_LOG_SCALE_BASE, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_LOG_SCALE_BASE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_EXTRA_SPACE_TITLE, BarSeriesPreferenceConstants.DEF_SECONDARY_Y_AXIS_EXTRA_SPACE_TITLE);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_DESCRIPTION_SERIES_1, BarSeriesPreferenceConstants.DEF_DESCRIPTION_SERIES_1);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_VISIBLE_SERIES_1, BarSeriesPreferenceConstants.DEF_VISIBLE_SERIES_1);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_VISIBLE_IN_LEGEND_SERIES_1, BarSeriesPreferenceConstants.DEF_VISIBLE_IN_LEGEND_SERIES_1);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BAR_COLOR_SERIES_1, BarSeriesPreferenceConstants.DEF_BAR_COLOR_SERIES_1);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BAR_PADDING_SERIES_1, BarSeriesPreferenceConstants.DEF_BAR_PADDING_SERIES_1);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BAR_WIDTH_SERIES_1, BarSeriesPreferenceConstants.DEF_BAR_WIDTH_SERIES_1);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BAR_WIDTH_STYLE_SERIES_1, BarSeriesPreferenceConstants.DEF_BAR_WIDTH_STYLE_SERIES_1);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_VISIBLE_SERIES_1_HIGHLIGHT, BarSeriesPreferenceConstants.DEF_VISIBLE_SERIES_1_HIGHLIGHT);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_VISIBLE_IN_LEGEND_SERIES_1_HIGHLIGHT, BarSeriesPreferenceConstants.DEF_VISIBLE_IN_LEGEND_SERIES_1_HIGHLIGHT);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BAR_COLOR_SERIES_1_HIGHLIGHT, BarSeriesPreferenceConstants.DEF_BAR_COLOR_SERIES_1_HIGHLIGHT);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BAR_PADDING_SERIES_1_HIGHLIGHT, BarSeriesPreferenceConstants.DEF_BAR_PADDING_SERIES_1_HIGHLIGHT);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BAR_WIDTH_SERIES_1_HIGHLIGHT, BarSeriesPreferenceConstants.DEF_BAR_WIDTH_SERIES_1_HIGHLIGHT);
		preferenceStore.setDefault(BarSeriesPreferenceConstants.P_BAR_WIDTH_STYLE_SERIES_1_HIGHLIGHT, BarSeriesPreferenceConstants.DEF_BAR_WIDTH_STYLE_SERIES_1_HIGHLIGHT);
	}
}