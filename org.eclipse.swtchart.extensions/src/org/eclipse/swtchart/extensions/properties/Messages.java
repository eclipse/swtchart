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
package org.eclipse.swtchart.extensions.properties;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.properties.messages"; //$NON-NLS-1$
	//
	public static String AXES;
	public static String BACKGROUND;
	public static String BACKGROUND_PLOT_AREA;
	public static String BAR_SERIES;
	public static String CHART_TITLE;
	public static String COLOR;
	public static String ENABLE_CATEGORY;
	public static String ENABLE_LOG_SCALE;
	public static String FONT_SIZE;
	public static String FOREGROUND;
	public static String LINE_COLOR;
	public static String LINE_SERIES;
	public static String LINE_STYLE;
	public static String MAX_RANGE_VALUE;
	public static String MIN_RANGE_VALUE;
	public static String PADDING_SIZE;
	public static String POSITION;
	public static String SERIES;
	public static String SHOW_LABEL;
	public static String SHOW_LEGEND;
	public static String SHOW_PLOT;
	public static String SHOW_TICK;
	public static String SHOW_TITLE;
	public static String STACKED_SERIES;
	public static String SYMBOL_COLOR;
	public static String SYMBOL_SIZE;
	public static String SYMBOL_TYPE;
	public static String TEXT;
	public static String TITLE;
	public static String VERTICAL_ORIENTATION;
	public static String X_AXIS;
	public static String Y_AXIS;
	//
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {

	}
}
