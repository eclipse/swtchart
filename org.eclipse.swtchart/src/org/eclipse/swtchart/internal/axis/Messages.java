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
 * Sebastien Darche - Implement arbitrary base log scale
 *******************************************************************************/
package org.eclipse.swtchart.internal.axis;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.internal.axis.messages"; //$NON-NLS-1$
	//
	public static String AXIS_ID_DONT_EXIST;
	public static String GIVEN_RANGE_INVALID;
	public static String ILLEGAL_RANGE;
	public static String LENGTH_MUST_BE_POSITIVE;
	public static String MUST_BE_LESS_MAX;
	public static String SERIES_CONTAIN_INVALID_VALUES;
	public static String UNKNOWN_AXIS_POSITION;
	public static String X_AXIS;
	public static String Y_AXIS;
	public static String Y_AXIS_CANNOT_BE_CATEGORY;
	public static String LOGARITHM_BASE_IS_INVALID;
	//
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {

	}
}
