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

package org.eclipse.swtchart.export.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.export.core.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String ALL_SERIES = "ALL_SERIES";
	public static final String DATA_EXPORT_ERROR = "DATA_EXPORT_ERROR";
	public static final String DATA_EXPORT_SUCCESS = "DATA_EXPORT_SUCCESS";
	public static final String EXPORT = "EXPORT";
	public static final String EXPORT_AXIS_SELECTION = "EXPORT_AXIS_SELECTION";
	public static final String SELECT_X_Y_TO_EXPORT = "SELECT_X_Y_TO_EXPORT";
	public static final String VISIBLE_SERIES = "VISIBLE_SERIES";
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
