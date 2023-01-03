/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.linecharts;

import org.eclipse.swt.widgets.Display;

public interface ICompressionSupport {

	public static final int DISPLAY_WIDTH = Display.getDefault().getClientArea().width;
	/*
	 * The compression type is partly used in a switch statement.
	 * Using the message approach is not valid as a constant expression is required.
	 * ---
	 * switch(compressionType) {
	 * case LineChart.COMPRESSION_AUTO:
	 * ...
	 * break;
	 * case LineChart.COMPRESSION_NONE:
	 * ...
	 * ---
	 * Probably, we should refactor this and introduce an enum with translated label.
	 */
	public static final String COMPRESSION_EXTREME = "Extreme"; // $NON-NLS-1$
	public static final String COMPRESSION_HIGH = "High"; //$NON-NLS-1$
	public static final String COMPRESSION_MEDIUM = "Medium"; //$NON-NLS-1$
	public static final String COMPRESSION_LOW = "Low"; //$NON-NLS-1$
	public static final String COMPRESSION_NONE = "None"; //$NON-NLS-1$
	public static final String COMPRESSION_AUTO = "Auto"; //$NON-NLS-1$
	/*
	 * The compression number is dependent on the display width.
	 */
	public static final int EXTREME_COMPRESSION = DISPLAY_WIDTH;
	public static final int HIGH_COMPRESSION = DISPLAY_WIDTH * 2;
	public static final int MEDIUM_COMPRESSION = DISPLAY_WIDTH * 5;
	public static final int LOW_COMPRESSION = DISPLAY_WIDTH * 10;
	public static final int NO_COMPRESSION = Integer.MAX_VALUE;
}