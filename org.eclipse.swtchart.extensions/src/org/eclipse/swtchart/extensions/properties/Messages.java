/*******************************************************************************
 * Copyright (c) 2020 SWT Chart Project
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

package org.eclipse.swtchart.extensions.properties;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.properties.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static String AXES = "AXES";
	public static String BACKGROUND = "BACKGROUND";
	public static String BACKGROUND_PLOT_AREA = "BACKGROUND_PLOT_AREA";
	public static String BAR_SERIES = "BAR_SERIES";
	public static String CHART_TITLE = "CHART_TITLE";
	public static String COLOR = "COLOR";
	public static String ENABLE_CATEGORY = "ENABLE_CATEGORY";
	public static String ENABLE_LOG_SCALE = "ENABLE_LOG_SCALE";
	public static String FONT_SIZE = "FONT_SIZE";
	public static String FOREGROUND = "FOREGROUND";
	public static String LINE_COLOR = "LINE_COLOR";
	public static String LINE_SERIES = "LINE_SERIES";
	public static String LINE_STYLE = "LINE_STYLE";
	public static String MAX_RANGE_VALUE = "MAX_RANGE_VALUE";
	public static String MIN_RANGE_VALUE = "MIN_RANGE_VALUE";
	public static String PADDING_SIZE = "PADDING_SIZE";
	public static String POSITION = "POSITION";
	public static String SERIES = "SERIES";
	public static String SHOW_LABEL = "SHOW_LABEL";
	public static String SHOW_LEGEND = "SHOW_LEGEND";
	public static String SHOW_PLOT = "SHOW_PLOT";
	public static String SHOW_TICK = "SHOW_TICK";
	public static String SHOW_TITLE = "SHOW_TITLE";
	public static String STACKED_SERIES = "STACKED_SERIES";
	public static String SYMBOL_COLOR = "SYMBOL_COLOR";
	public static String SYMBOL_SIZE = "SYMBOL_SIZE";
	public static String SYMBOL_TYPE = "SYMBOL_TYPE";
	public static String TEXT = "TEXT";
	public static String TITLE = "TITLE";
	public static String VERTICAL_ORIENTATION = "VERTICAL_ORIENTATION";
	public static String X_AXIS = "X_AXIS";
	public static String Y_AXIS = "Y_AXIS";

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
