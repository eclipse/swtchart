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
package org.eclipse.swtchart.customcharts.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.customcharts.core.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	//
	public static final String INTENSITY = "INTENSITY";
	public static final String MINUTES = "MINUTES";
	public static final String RELATIVE_INTENSITY = "RELATIVE_INTENSITY";
	public static final String RETENTION_TIME = "RETENTION_TIME";
	public static final String RESET_CHROMATOGRAM = "RESET_CHROMATOGRAM";

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
