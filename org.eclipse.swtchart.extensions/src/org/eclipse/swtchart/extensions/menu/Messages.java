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
package org.eclipse.swtchart.extensions.menu;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.menu.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	//
	public static final String EXPORT_CHART_SELECTION = "EXPORT_CHART_SELECTION";
	public static final String RANGE_SELECTION = "RANGE_SELECTION";
	public static final String REDO_SELECTION = "REDO_SELECTION";
	public static final String RESET_CHART = "RESET_CHART";
	public static final String RESET_SELECTED_SERIES = "RESET_SELECTED_SERIES";
	public static final String TOGGLE_VISIBILITY = "TOGGLE_VISIBILITY";
	public static final String UNDO_SELECTION = "UNDO_SELECTION";
	public static final String RESET_X_AXIS = "RESET_X_AXIS";
	public static final String RESET_Y_AXIS = "RESET_Y_AXIS";
	public static final String SET_CHART_RANGE = "SET_CHART_RANGE";
	public static final String ZOOM_IN = "ZOOM_IN";
	public static final String ZOOM_OUT = "ZOOM_OUT";

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
