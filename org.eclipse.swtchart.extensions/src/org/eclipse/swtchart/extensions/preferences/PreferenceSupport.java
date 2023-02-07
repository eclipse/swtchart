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
package org.eclipse.swtchart.extensions.preferences;

import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.RangeRestriction;

public class PreferenceSupport {

	public static String[][] LEGEND_POSITIONS = new String[][]{//
			{Messages.LEFT, Integer.toString(SWT.LEFT)}, //
			{Messages.RIGHT, Integer.toString(SWT.RIGHT)}, //
			{Messages.TOP, Integer.toString(SWT.TOP)}, //
			{Messages.BOTTOM, Integer.toString(SWT.BOTTOM)}};
	//
	public static String[][] ORIENTATIONS = new String[][]{//
			{Messages.HORIZONTAL, Integer.toString(SWT.HORIZONTAL)}, //
			{Messages.VERTICAL, Integer.toString(SWT.VERTICAL)}};
	//
	public static String[][] AXIS_POSITIONS = new String[][]{//
			{Messages.PRIMARY, Position.Primary.toString()}, //
			{Messages.SECONDARY, Position.Secondary.toString()}};
	//
	public static String[][] LOCALES = new String[][]{//
			{Messages.ENGLISH, Locale.ENGLISH.getLanguage()}, //
			{Messages.US, Locale.US.getLanguage()}, //
			{Messages.GERMAN, Locale.GERMAN.getLanguage()}};
	//
	public static String[][] LINE_STYLES = new String[][]{//
			{Messages.NONE, LineStyle.NONE.toString()}, //
			{Messages.SOLID, LineStyle.SOLID.toString()}, //
			{Messages.DASH, LineStyle.DASH.toString()}, //
			{Messages.DOT, LineStyle.DOT.toString()}, //
			{Messages.DASH_DOT, LineStyle.DASHDOT.toString()}, //
			{Messages.DASH_DOT_DOT, LineStyle.DASHDOTDOT.toString()}};
	//
	public static String[][] ANTIALIAS_OPTIONS = new String[][]{//
			{Messages.DEFAULT, Integer.toString(SWT.DEFAULT)}, //
			{Messages.ON, Integer.toString(SWT.ON)}, //
			{Messages.OFF, Integer.toString(SWT.OFF)}};
	//
	public static String[][] SYMBOL_TYPES = new String[][]{//
			{Messages.NONE, PlotSymbolType.NONE.toString()}, //
			{Messages.CIRCLE, PlotSymbolType.CIRCLE.toString()}, //
			{Messages.CROSS, PlotSymbolType.CROSS.toString()}, //
			{Messages.DIAMOND, PlotSymbolType.DIAMOND.toString()}, //
			{Messages.INVERTED_TRIANGLE, PlotSymbolType.INVERTED_TRIANGLE.toString()}, //
			{Messages.PLUS, PlotSymbolType.PLUS.toString()}, //
			{Messages.SQUARE, PlotSymbolType.SQUARE.toString()}, //
			{Messages.TRIANGLE, PlotSymbolType.TRIANGLE.toString()}, //
			{Messages.EMOJI, PlotSymbolType.EMOJI.toString()}};
	//
	public static String[][] BAR_WIDTH_STYLES = new String[][]{//
			{Messages.FIXED, BarWidthStyle.FIXED.toString()}, //
			{Messages.STRETCHED, BarWidthStyle.STRETCHED.toString()}};
	//
	public static String[][] EXTEND_TYPES = new String[][]{//
			{Messages.RELATIVE, RangeRestriction.ExtendType.RELATIVE.toString()}, //
			{Messages.ABSOLUTE, RangeRestriction.ExtendType.ABSOLUTE.toString()}};

	private PreferenceSupport() {

	}
}
