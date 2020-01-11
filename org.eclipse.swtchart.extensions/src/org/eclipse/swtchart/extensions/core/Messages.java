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

package org.eclipse.swtchart.extensions.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.core.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static String CANT_SET_SECONDARY_X_AXIS_RANGE = "CANT_SET_SECONDARY_X_AXIS_RANGE";
	public static String X_Y_SERIES_LENGTH_DIFFERS = "X_Y_SERIES_LENGTH_DIFFERS";
	public static String CHART_TITLE = "CHART_TITLE";
	public static String DOUBLE_CLICK_TO_SHOW_RANGE_INFO = "DOUBLE_CLICK_TO_SHOW_RANGE_INFO";
	public static String HIDE = "HIDE";
	public static String HIDE_RANGE_SELECTOR_UI = "HIDE_RANGE_SELECTOR_UI";
	public static String LABEL_NOT_SET = "LABEL_NOT_SET";
	public static String NONE = "NONE";
	public static String NOT_SET = "NOT_SET";
	public static String RESET = "RESET";
	public static String RESET_RANGE = "RESET_RANGE";
	public static String SET = "SET";
	public static String SET_CURRENT_SELECTION = "SET_CURRENT_SELECTION";
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
