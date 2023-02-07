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
package org.eclipse.swtchart.extensions.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.core.messages"; //$NON-NLS-1$
	//
	public static String CANT_SET_SECONDARY_X_AXIS_RANGE;
	public static String X_Y_SERIES_LENGTH_DIFFERS;
	public static String CHART_TITLE;
	public static String DOUBLE_CLICK_TO_SHOW_RANGE_INFO;
	public static String HIDE;
	public static String HIDE_RANGE_SELECTOR_UI;
	public static String LABEL_NOT_SET;
	public static String NONE;
	public static String NOT_SET;
	public static String RESET;
	public static String RESET_RANGE;
	public static String SET;
	public static String SET_CURRENT_SELECTION;
	public static String X_AXIS;
	public static String Y_AXIS;
	//
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {

	}
}
