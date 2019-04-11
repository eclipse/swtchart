/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.preferences;

import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;

public class PreferenceSupport {

	public static String[][] LEGEND_POSITIONS = new String[][]{//
			{"Left", Integer.toString(SWT.LEFT)}, //
			{"Right", Integer.toString(SWT.RIGHT)}, //
			{"Top", Integer.toString(SWT.TOP)}, //
			{"Bottom", Integer.toString(SWT.BOTTOM)}//
	};
	//
	public static String[][] ORIENTATIONS = new String[][]{//
			{"Horizontal", Integer.toString(SWT.HORIZONTAL)}, //
			{"Vertical", Integer.toString(SWT.VERTICAL)}//
	};
	//
	public static String[][] AXIS_POSITIONS = new String[][]{//
			{"Primary", Position.Primary.toString()}, //
			{"Secondary", Position.Secondary.toString()}//
	};
	//
	public static String[][] LOCALES = new String[][]{//
			{"English", Locale.ENGLISH.getLanguage()}, //
			{"US", Locale.US.getLanguage()}, //
			{"German", Locale.GERMAN.getLanguage()}//
	};
	//
	public static String[][] LINE_STYLES = new String[][]{//
			{"None", LineStyle.NONE.toString()}, //
			{"Solid", LineStyle.SOLID.toString()}, //
			{"Dash", LineStyle.DASH.toString()}, //
			{"Dot", LineStyle.DOT.toString()}, //
			{"Dash Dot", LineStyle.DASHDOT.toString()}, //
			{"Dash Dot Dot", LineStyle.DASHDOTDOT.toString()}//
	};
	//
	public static String[][] ANTIALIAS_OPTIONS = new String[][]{//
			{"Default", Integer.toString(SWT.DEFAULT)}, //
			{"On", Integer.toString(SWT.ON)}, //
			{"Off", Integer.toString(SWT.OFF)}//
	};
	//
	public static String[][] SYMBOL_TYPES = new String[][]{//
			{"None", PlotSymbolType.NONE.toString()}, //
			{"Circle", PlotSymbolType.CIRCLE.toString()}, //
			{"Cross", PlotSymbolType.CROSS.toString()}, //
			{"Diamond", PlotSymbolType.DIAMOND.toString()}, //
			{"Inverted Triangle", PlotSymbolType.INVERTED_TRIANGLE.toString()}, //
			{"Plus", PlotSymbolType.PLUS.toString()}, //
			{"Square", PlotSymbolType.SQUARE.toString()}, //
			{"Triangle", PlotSymbolType.TRIANGLE.toString()}//
	};
	//
	public static String[][] BAR_WIDTH_STYLES = new String[][]{//
			{"Fixed", BarWidthStyle.FIXED.toString()}, //
			{"Stretched", BarWidthStyle.STRETCHED.toString()}//
	};
	//
	public static String[][] EXTEND_TYPES = new String[][]{//
			{"Relative", RangeRestriction.ExtendType.RELATIVE.toString()}, //
			{"Absolute", RangeRestriction.ExtendType.ABSOLUTE.toString()}//
	};
}
