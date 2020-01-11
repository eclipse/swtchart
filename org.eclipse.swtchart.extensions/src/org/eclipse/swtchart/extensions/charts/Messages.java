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

package org.eclipse.swtchart.extensions.charts;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.charts.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String ADJUST_AXIS_RANGE = "ADJUST_AXIS_RANGE";
	public static final String ADJUST_X_AXIS_RANGE = "ADJUST_X_AXIS_RANGE";
	public static final String ADJUST_Y_AXIS_RANGE = "ADJUST_Y_AXIS_RANGE";
	public static final String BOLD = "BOLD";
	public static final String CHART = "CHART";
	public static final String CIRCLE = "CIRCLE";
	public static final String CROSS = "CROSS";
	public static final String DIAMON = "DIAMON";
	public static final String DIAMOND = "DIAMOND";
	public static final String EMOJI = "EMOJI";
	public static final String GRID = "GRID";
	public static final String INVERTED_TRIANGLE = "INVERTED_TRIANGLE"; 
	public static final String ITALIC = "ITALIC";
	public static final String LABEL = "LABEL";
	public static final String LEGEND = "LEGEND";
	public static final String NONE = "NONE";
	public static final String NORMAL = "NORMAL";
	public static final String PLUS = "PLUS";
	public static final String PRIMARY = "PRIMARY";
	public static final String PROPERTIES_MENU = "PROPERTIES_MENU";
	public static final String PROPERTIES = "PROPERTIES";
	public static final String SAVE_AS = "SAVE_AS";
	public static final String SAVE_AS_DIALOG = "SAVE_AS_DIALOG";
	public static final String SECONDARY = "SECONDARY";
	public static final String SERIES = "SERIES";
	public static final String SOLID = "SOLID";
	public static final String SQUARE = "SQUARE";
	public static final String TICK = "TICK";
	public static final String TRIANGLE = "TRIANGLE";
	public static final String X_AXIS = "X_AXIS";
	public static final String Y_AXIS = "Y_AXIS";
	public static final String ZOOM_IN = "ZOOM_IN";
	public static final String ZOOM_IN_CTRL = "ZOOM_IN_CTRL";
	public static final String ZOOM_IN_X_AXIS = "ZOOM_IN_X_AXIS";
	public static final String ZOOM_IN_Y_AXIS = "ZOOM_IN_Y_AXIS";
	public static final String ZOOM_OUT = "ZOOM_OUT";
	public static final String ZOOM_OUT_CTRL = "ZOOM_OUT_CTRL";
	public static final String ZOOM_OUT_X_AXIS = "ZOOM_OUT_X_AXIS";
	public static final String ZOOM_OUT_Y_AXIS = "ZOOM_OUT_Y_AXIS";

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
