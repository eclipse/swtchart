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

package org.eclipse.swtchart.extensions.menu.toggle;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.menu.toggle.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static String AXIS_ZERO_MARKER = "AXIS_ZERO_MARKER";
	public static String LEGEND_MARKER = "LEGEND_MARKER";
	public static String PLOT_CENTER_MARKER = "PLOT_CENTER_MARKER";
	public static String POSITION_MARKER = "POSITION_MARKER";
	public static String RANGE_SELECTOR = "RANGE_SELECTOR";
	public static String SERIES_LABEL_MARKER = "SERIES_LABEL_MARKER";
	public static String SERIES_LEGEND = "SERIES_LEGEND";
	public static String SHOW_TOOLTIPS = "SHOW_TOOLTIPS";

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
