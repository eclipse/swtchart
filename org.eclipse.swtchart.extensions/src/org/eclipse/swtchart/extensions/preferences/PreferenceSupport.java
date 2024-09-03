/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.preferences;

import java.util.Locale;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.ResourceSupport;

public class PreferenceSupport {

	public static final String[][] LEGEND_POSITIONS = new String[][]{ //
			{Messages.getString(Messages.LEFT), Integer.toString(SWT.LEFT)}, //
			{Messages.getString(Messages.RIGHT), Integer.toString(SWT.RIGHT)}, //
			{Messages.getString(Messages.TOP), Integer.toString(SWT.TOP)}, //
			{Messages.getString(Messages.BOTTOM), Integer.toString(SWT.BOTTOM)} //
	};
	//
	public static final String[][] ORIENTATIONS = new String[][]{ //
			{Messages.getString(Messages.HORIZONTAL), Integer.toString(SWT.HORIZONTAL)}, //
			{Messages.getString(Messages.VERTICAL), Integer.toString(SWT.VERTICAL)} //
	};
	//
	public static final String[][] AXIS_POSITIONS = new String[][]{ //
			{Messages.getString(Messages.PRIMARY), Position.Primary.toString()}, //
			{Messages.getString(Messages.SECONDARY), Position.Secondary.toString()} //
	};
	//
	public static final String[][] LOCALES = new String[][]{ //
			{Messages.getString(Messages.ENGLISH), Locale.ENGLISH.getLanguage()}, //
			{Messages.getString(Messages.US), Locale.US.getLanguage()}, //
			{Messages.getString(Messages.GERMAN), Locale.GERMAN.getLanguage()} //
	};
	//
	public static final String[][] LINE_STYLES = new String[][]{ //
			{Messages.getString(Messages.NONE), LineStyle.NONE.toString()}, //
			{Messages.getString(Messages.SOLID), LineStyle.SOLID.toString()}, //
			{Messages.getString(Messages.DASH), LineStyle.DASH.toString()}, //
			{Messages.getString(Messages.DOT), LineStyle.DOT.toString()}, //
			{Messages.getString(Messages.DASH_DOT), LineStyle.DASHDOT.toString()}, //
			{Messages.getString(Messages.DASH_DOT_DOT), LineStyle.DASHDOTDOT.toString()} //
	};
	//
	public static final String[][] ANTIALIAS_OPTIONS = new String[][]{ //
			{Messages.getString(Messages.DEFAULT), Integer.toString(SWT.DEFAULT)}, //
			{Messages.getString(Messages.ON), Integer.toString(SWT.ON)}, //
			{Messages.getString(Messages.OFF), Integer.toString(SWT.OFF)} //
	};
	//
	public static final String[][] SYMBOL_TYPES = new String[][]{//
			{Messages.getString(Messages.NONE), PlotSymbolType.NONE.toString()}, //
			{Messages.getString(Messages.CIRCLE), PlotSymbolType.CIRCLE.toString()}, //
			{Messages.getString(Messages.CROSS), PlotSymbolType.CROSS.toString()}, //
			{Messages.getString(Messages.DIAMOND), PlotSymbolType.DIAMOND.toString()}, //
			{Messages.getString(Messages.INVERTED_TRIANGLE), PlotSymbolType.INVERTED_TRIANGLE.toString()}, //
			{Messages.getString(Messages.PLUS), PlotSymbolType.PLUS.toString()}, //
			{Messages.getString(Messages.SQUARE), PlotSymbolType.SQUARE.toString()}, //
			{Messages.getString(Messages.TRIANGLE), PlotSymbolType.TRIANGLE.toString()}, //
			{Messages.getString(Messages.EMOJI), PlotSymbolType.EMOJI.toString()} //
	};
	//
	public static final String[][] BAR_WIDTH_STYLES = new String[][]{//
			{Messages.getString(Messages.FIXED), BarWidthStyle.FIXED.toString()}, //
			{Messages.getString(Messages.STRETCHED), BarWidthStyle.STRETCHED.toString()} //
	};
	//
	public static final String[][] EXTEND_TYPES = new String[][]{//
			{Messages.getString(Messages.RELATIVE), RangeRestriction.ExtendType.RELATIVE.toString()}, //
			{Messages.getString(Messages.ABSOLUTE), RangeRestriction.ExtendType.ABSOLUTE.toString()} //
	};

	private PreferenceSupport() {

	}

	/**
	 * The buffered selection creates a screenshot and sets it in the background
	 * while doing the data selection. This prevents to redraw the series while
	 * doing the selection event.
	 */
	public static boolean isBufferedSelection() {

		boolean bufferedSelection = false;
		IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
		if(preferenceStore != null) {
			bufferedSelection = preferenceStore.getBoolean(PreferenceConstants.P_BUFFER_SELECTION);
		}
		return bufferedSelection;
	}

	public static void setBufferedSelection(boolean active) {

		IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
		if(preferenceStore != null) {
			preferenceStore.setValue(PreferenceConstants.P_BUFFER_SELECTION, active);
		}
	}
}