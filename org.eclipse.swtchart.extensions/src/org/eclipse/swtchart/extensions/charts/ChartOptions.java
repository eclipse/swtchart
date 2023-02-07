/*******************************************************************************
 * Copyright (c) 2019, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.charts;

import org.eclipse.swt.SWT;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.linecharts.ICompressionSupport;

public class ChartOptions {

	public static final String[][] FONT_STYLES = new String[][]{ //
			{Messages.NORMAL, Integer.toString(SWT.NORMAL)}, //
			{Messages.BOLD, Integer.toString(SWT.BOLD)}, //
			{Messages.ITALIC, Integer.toString(SWT.ITALIC)} //
	};
	//
	public static final String[][] COMPRESSION_TYPES = new String[][]{ //
			{ICompressionSupport.COMPRESSION_EXTREME, ICompressionSupport.COMPRESSION_EXTREME}, //
			{ICompressionSupport.COMPRESSION_HIGH, ICompressionSupport.COMPRESSION_HIGH}, //
			{ICompressionSupport.COMPRESSION_MEDIUM, ICompressionSupport.COMPRESSION_MEDIUM}, //
			{ICompressionSupport.COMPRESSION_LOW, ICompressionSupport.COMPRESSION_LOW}, //
			{ICompressionSupport.COMPRESSION_AUTO, ICompressionSupport.COMPRESSION_AUTO}, //
			{ICompressionSupport.COMPRESSION_NONE, ICompressionSupport.COMPRESSION_NONE} //
	};
	//
	public static final String[][] SYMBOL_TYPES = new String[][]{ //
			{Messages.NONE, PlotSymbolType.NONE.toString()}, //
			{Messages.CIRCLE, PlotSymbolType.CIRCLE.toString()}, //
			{Messages.CROSS, PlotSymbolType.CROSS.toString()}, //
			{Messages.DIAMON, PlotSymbolType.DIAMOND.toString()}, //
			{Messages.INVERTED_TRIANGLE, PlotSymbolType.INVERTED_TRIANGLE.toString()}, //
			{Messages.PLUS, PlotSymbolType.PLUS.toString()}, //
			{Messages.SQUARE, PlotSymbolType.SQUARE.toString()}, //
			{Messages.TRIANGLE, PlotSymbolType.TRIANGLE.toString()}, //
			{Messages.EMOJI, PlotSymbolType.EMOJI.toString()} //
	};
	//
	public static final String[][] POSITIONS = new String[][]{ //
			{Messages.PRIMARY, Position.Primary.toString()}, //
			{Messages.SECONDARY, Position.Secondary.toString()} //
	};
	//
	public static final String[][] LINE_STYLES = new String[][]{ //
			{Messages.NONE, LineStyle.NONE.toString()}, //
			{"-", LineStyle.DASH.toString()}, //
			{"-.", LineStyle.DASHDOT.toString()}, //
			{"-..", LineStyle.DASHDOTDOT.toString()}, //
			{".", LineStyle.DOT.toString()}, //
			{Messages.SOLID, LineStyle.SOLID.toString()} //
	};

	private ChartOptions() {

	}
}
