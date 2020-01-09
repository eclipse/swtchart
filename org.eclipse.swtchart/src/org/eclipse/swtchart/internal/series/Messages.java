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

package org.eclipse.swtchart.internal.series;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.internal.series.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String POSITIVE_VALUE_FOR_ERROR = "POSITIVE_VALUE_FOR_ERROR";
	public static final String REQUIRES_INDEXED_SERIES_MODEL = "REQUIRES_INDEXED_SERIES_MODEL";
	public static final String SERIES_ID_NOT_EXISTS = "SERIES_ID_NOT_EXISTS";
	public static final String STACKED_SERIES_CANT_CONTAIN_NEGATIVE_VALUES = "STACKED_SERIES_CANT_CONTAIN_NEGATIVE_VALUES";
	public static final String UNKNOWN_BAR_WIDTH_STYLE = "UNKNOWN_BAR_WIDTH_STYLE";

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
