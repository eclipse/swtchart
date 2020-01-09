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

package org.eclipse.swtchart.extensions.preferences;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.preferences.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static String ABSOLUTE = "ABSOLUTE";
	public static String BOTTOM = "BOTTOM";
	public static String CIRCLE = "CIRCLE";
	public static String CROSS = "CROSS";
	public static String DASH = "DASH";
	public static String DASH_DOT = "DASH_DOT";
	public static String DASH_DOT_DOT = "DASH_DOT_DOT";
	public static String DEFAULT = "DEFAULT";
	public static String DIAMOND = "DIAMOND";
	public static String DOT = "DOT";
	public static String EMOJI = "EMOJI";
	public static String ENGLISH = "ENGLISH";
	public static String FIXED = "FIXED";
	public static String GERMAN = "GERMAN";
	public static String HORIZONTAL = "HORIZONTAL";
	public static String INVERTED_TRIANGLE = "INVERTED_TRIANGLE";
	public static String LEFT = "LEFT";
	public static String NONE = "NONE";
	public static String OFF = "OFF";
	public static String ON = "ON";
	public static String PLUS = "PLUS";
	public static String PRIMARY = "PRIMARY";
	public static String RELATIVE = "RELATIVE";
	public static String RIGHT = "RIGHT";
	public static String SECONDARY = "SECONDARY";
	public static String SOLID = "SOLID";
	public static String SQUARE = "SQUARE";
	public static String STRETCHED = "STRETCHED";
	public static String TOP = "TOP";
	public static String TRIANGLE = "TRIANGLE";
	public static String US = "US";
	public static String VERTICAL = "VERTICAL";

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
