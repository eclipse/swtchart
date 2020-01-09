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

package org.eclipse.swtchart.export.menu.text;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.export.menu.text.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String COMMA_SEPARATED_VALUES = "COMMA_SEPARATED_VALUES";
	public static final String IMAGE_R_SCRIPT = "IMAGE_R_SCRIPT";
	public static final String LATEX_TABLE = "LATEX_TABLE";
	public static final String R_EXTENSION = "R_EXTENSION";
	public static final String SAVE_AS_COMMA_SEPARATED = "SAVE_AS_COMMA_SEPARATED";
	public static final String SAVE_AS_IMAGE_R_SCRIPT = "SAVE_AS_IMAGE_R_SCRIPT";
	public static final String SAVE_AS_LATEX = "SAVE_AS_LATEX";
	public static final String SAVE_AS_TAB_SEPARATED = "SAVE_AS_TAB_SEPARATED";
	public static final String TAB_SEPARATED_VALUES = "TAB_SEPARATED_VALUES";

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
