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
package org.eclipse.swtchart.extensions.menu.toggle;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.menu.toggle.messages"; //$NON-NLS-1$
	//
	public static String AXIS_ZERO_MARKER;
	public static String LEGEND_MARKER;
	public static String PLOT_CENTER_MARKER;
	public static String POSITION_MARKER;
	public static String RANGE_SELECTOR;
	public static String SERIES_LABEL_MARKER;
	public static String SERIES_LEGEND;
	public static String SHOW_TOOLTIPS;
	//
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {

	}
}
