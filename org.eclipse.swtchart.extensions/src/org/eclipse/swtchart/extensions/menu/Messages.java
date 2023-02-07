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
package org.eclipse.swtchart.extensions.menu;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.menu.messages"; //$NON-NLS-1$
	//
	public static String EXPORT_CHART_SELECTION;
	public static String RANGE_SELECTION;
	public static String REDO_SELECTION;
	public static String RESET_CHART;
	public static String RESET_SELECTED_SERIES;
	public static String TOGGLE_VISIBILITY;
	public static String UNDO_SELECTION;
	//
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {

	}
}
