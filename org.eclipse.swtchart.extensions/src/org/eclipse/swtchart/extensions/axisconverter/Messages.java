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

package org.eclipse.swtchart.extensions.axisconverter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.axisconverter.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static final String SCAN_MUST_BE_GE_0_KEY = "SCAN_MUST_BE_GE_0";
	public static final String SCAN_MUST_BE_G_0_KEY = "SCAN_MUST_BE_G_0";
	
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
