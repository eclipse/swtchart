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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.customcharts.core.messages"; //$NON-NLS-1$
	//
	public static String INTENSITY;
	public static String MINUTES;
	public static String RELATIVE_INTENSITY;
	public static String RETENTION_TIME;
	public static String RESET_CHROMATOGRAM;
	//
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {

	}
}
