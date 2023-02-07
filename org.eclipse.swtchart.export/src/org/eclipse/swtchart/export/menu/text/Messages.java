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
package org.eclipse.swtchart.export.menu.text;

import org.eclipse.osgi.util.NLS;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.export.menu.text.messages"; //$NON-NLS-1$
	//
	public static String COMMA_SEPARATED_VALUES;
	public static String IMAGE_R_SCRIPT;
	public static String LATEX_TABLE;
	public static String SAVE_AS_COMMA_SEPARATED;
	public static String SAVE_AS_IMAGE_R_SCRIPT;
	public static String SAVE_AS_LATEX;
	public static String SAVE_AS_TAB_SEPARATED;
	public static String TAB_SEPARATED_VALUES;
	//
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {

	}
}
