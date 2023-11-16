/*******************************************************************************
 * Copyright (c) 2020, 2023 SWT Chart Project
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Frank Buloup - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.preferences;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.preferences.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	//
	public static final String ABSOLUTE = "ABSOLUTE";
	public static final String BITMAP_EXPORT_CUSTOM_SIZE = "BITMAP_EXPORT_CUSTOM_SIZE";
	public static final String BITMAP_EXPORT_WIDTH = "BITMAP_EXPORT_WIDTH";
	public static final String BITMAP_EXPORT_HEIGHT = "BITMAP_EXPORT_HEIGHT";
	public static final String BOTTOM = "BOTTOM";
	public static final String BUFFERED_SELECTION = "BUFFERED_SELECTION";
	public static final String CIRCLE = "CIRCLE";
	public static final String CROSS = "CROSS";
	public static final String DASH = "DASH";
	public static final String DASH_DOT = "DASH_DOT";
	public static final String DASH_DOT_DOT = "DASH_DOT_DOT";
	public static final String DEFAULT = "DEFAULT";
	public static final String DIAMOND = "DIAMOND";
	public static final String DOT = "DOT";
	public static final String EMOJI = "EMOJI";
	public static final String ENGLISH = "ENGLISH";
	public static final String FIXED = "FIXED";
	public static final String GERMAN = "GERMAN";
	public static final String HORIZONTAL = "HORIZONTAL";
	public static final String INVERTED_TRIANGLE = "INVERTED_TRIANGLE";
	public static final String KEEP_SERIES_DESCRIPTION = "KEEP_SERIES_DESCRIPTION";
	public static final String LEFT = "LEFT";
	public static final String LEGEND_POSITION_X = "LEGEND_POSITION_X";
	public static final String LEGEND_POSITION_Y = "LEGEND_POSITION_Y";
	public static final String MOVE_LEGEND_X = "MOVE_LEGEND_X";
	public static final String MOVE_LEGEND_Y = "MOVE_LEGEND_Y";
	public static final String NONE = "NONE";
	public static final String OFF = "OFF";
	public static final String ON = "ON";
	public static final String PLUS = "PLUS";
	public static final String POPUP_CLOSE_TIME = "POPUP_CLOSE_TIME";
	public static final String PRIMARY = "PRIMARY";
	public static final String RELATIVE = "RELATIVE";
	public static final String RIGHT = "RIGHT";
	public static final String SECONDARY = "SECONDARY";
	public static final String SHOW_POPUP_ON_CLICKBINDING = "SHOW_POPUP_ON_CLICKBINDING";
	public static final String SOLID = "SOLID";
	public static final String SORT_LEGEND_TABLE = "SORT_LEGEND_TABLE";
	public static final String SORT_ORDER_COLUMNS_LEGEND = "SORT_ORDER_COLUMNS_LEGEND";
	public static final String SORT_ORDER_COLUMNS_CUSTOM_SERIES = "SORT_ORDER_COLUMNS_CUSTOM_SERIES";
	public static final String SQUARE = "SQUARE";
	public static final String STRETCHED = "STRETCHED";
	public static final String TOP = "TOP";
	public static final String TRIANGLE = "TRIANGLE";
	public static final String US = "US";
	public static final String VERTICAL = "VERTICAL";

	private Messages() {

	}

	public static String getString(String key) {

		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch(MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
