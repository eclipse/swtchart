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
package org.eclipse.swtchart.extensions.properties;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.properties.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	//
	public static final String AXES = "AXES";
	public static final String BACKGROUND = "BACKGROUND";
	public static final String BACKGROUND_PLOT_AREA = "BACKGROUND_PLOT_AREA";
	public static final String BAR_SERIES = "BAR_SERIES";
	public static final String CHART_TITLE = "CHART_TITLE";
	public static final String COLOR = "COLOR";
	public static final String ENABLE_CATEGORY = "ENABLE_CATEGORY";
	public static final String ENABLE_LOG_SCALE = "ENABLE_LOG_SCALE";
	public static final String FONT_SIZE = "FONT_SIZE";
	public static final String FOREGROUND = "FOREGROUND";
	public static final String LINE_COLOR = "LINE_COLOR";
	public static final String LINE_SERIES = "LINE_SERIES";
	public static final String LINE_STYLE = "LINE_STYLE";
	public static final String MAX_RANGE_VALUE = "MAX_RANGE_VALUE";
	public static final String MIN_RANGE_VALUE = "MIN_RANGE_VALUE";
	public static final String PADDING_SIZE = "PADDING_SIZE";
	public static final String POSITION = "POSITION";
	public static final String SERIES = "SERIES";
	public static final String SHOW_LABEL = "SHOW_LABEL";
	public static final String SHOW_LEGEND = "SHOW_LEGEND";
	public static final String SHOW_PLOT = "SHOW_PLOT";
	public static final String SHOW_TICK = "SHOW_TICK";
	public static final String SHOW_TITLE = "SHOW_TITLE";
	public static final String STACKED_SERIES = "STACKED_SERIES";
	public static final String SYMBOL_COLOR = "SYMBOL_COLOR";
	public static final String SYMBOL_SIZE = "SYMBOL_SIZE";
	public static final String SYMBOL_TYPE = "SYMBOL_TYPE";
	public static final String TEXT = "TEXT";
	public static final String TITLE = "TITLE";
	public static final String VERTICAL_ORIENTATION = "VERTICAL_ORIENTATION";
	public static final String X_AXIS = "X_AXIS";
	public static final String Y_AXIS = "Y_AXIS";

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
