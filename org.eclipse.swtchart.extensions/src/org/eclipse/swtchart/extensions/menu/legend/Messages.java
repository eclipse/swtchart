/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu.legend;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.menu.legend.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	//
	public static final String ADD_SERIES_MAPPING = "ADD_SERIES_MAPPING";
	public static final String REMOVE_SERIES_MAPPING = "REMOVE_SERIES_MAPPING";
	public static final String ADD_REMOVE_SERIES_MAPPING = "ADD_REMOVE_SERIES_MAPPING";
	public static final String ADJUST_COLOR_OF_SELECTED_SERIES = "ADJUST_COLOR_OF_SELECTED_SERIES";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String DESCRIPTION_MUST_NOT_BE_EMPTY = "DESCRIPTION_MUST_NOT_BE_EMPTY";
	public static final String FORGOT_TO_SET_DESCRIPTION = "FORGOT_TO_SET_DESCRIPTION";
	public static final String HIDE_SELECTED_SERIES = "HIDE_SELECTED_SERIES";
	public static final String HIDE_SELECTED_SERIES_IN_LEGEND = "HIDE_SELECTED_SERIES_IN_LEGEND";
	public static final String HIDE_SERIES = "HIDE_SERIES";
	public static final String HIDE_SERIES_IN_LEGEND = "HIDE_SERIES_IN_LEGEND";
	public static final String SET_COLOR = "SET_COLOR";
	public static final String SET_DESCRIPTION = "SET_DESCRIPTION";
	public static final String SET_DESCRIPTION_FOR_SELECTED_SERIES = "setDescriptionForSelectedSeries";
	public static final String SET_SERIES_COLOR = "setSeriesColor";
	public static final String SHOW_SELECTED_SERIES = "showSelectedSeries";
	public static final String SHOW_SELECTED_SERIES_IN_LEGEND = "showSelectedSeriesInLegend";
	public static final String SHOW_SERIES = "SHOW_SERIES";
	public static final String SHOW_SERIES_IN_LEGEND = "SHOW_SERIES_IN_LEGEND";

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
