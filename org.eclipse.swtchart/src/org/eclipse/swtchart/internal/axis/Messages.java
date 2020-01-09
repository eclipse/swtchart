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

package org.eclipse.swtchart.internal.axis;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.internal.axis.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String AXIS_ID_DONT_EXIST = "AXIS_ID_DONT_EXIST";
	public static final String GIVEN_RANGE_INVALID = "GIVEN_RANGE_INVALID";
	public static final String ILLEGAL_RANGE = "ILLEGAL_RANGE";
	public static final String LENGTH_MUST_BE_POSITIVE = "LENGTH_MUST_BE_POSITIVE";
	public static final String MUST_BE_LESS_MAX = "MUST_BE_LESS_MAX";
	public static final String SERIES_CONTAIN_INVALID_VALUES = "SERIES_CONTAIN_INVALID_VALUES";
	public static final String UNKNOWN_AXIS_POSITION = "UNKNOWN_AXIS_POSITION";
	public static final String X_AXIS = "X_AXIS";
	public static final String Y_AXIS = "Y_AXIS";
	public static final String Y_AXIS_CANNOT_BE_CATEGORY = "Y_AXIS_CANNOT_BE_CATEGORY";

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
