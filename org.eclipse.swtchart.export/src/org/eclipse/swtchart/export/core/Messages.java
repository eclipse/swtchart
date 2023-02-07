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
package org.eclipse.swtchart.export.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.export.core.messages"; //$NON-NLS-1$
	//
	public static String ALL_SERIES;
	public static String DATA_EXPORT_ERROR;
	public static String DATA_EXPORT_SUCCESS;
	public static String EXPORT;
	public static String EXPORT_AXIS_SELECTION;
	public static String SELECT_X_Y_TO_EXPORT;
	public static String VISIBLE_SERIES;
	public static String X_AXIS;
	public static String Y_AXIS;
	//
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {

	}
}
