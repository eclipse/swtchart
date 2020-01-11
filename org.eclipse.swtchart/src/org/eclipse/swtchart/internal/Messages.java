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

package org.eclipse.swtchart.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.internal.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String CHART_TITLE = "CHART_TITLE";
	public static final String DISPOSED_COLOR_GIVEN = "DISPOSED_COLOR_GIVEN";
	public static final String DISPOSED_FONT_GIVEN = "DISPOSED_FONT_GIVEN";

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
