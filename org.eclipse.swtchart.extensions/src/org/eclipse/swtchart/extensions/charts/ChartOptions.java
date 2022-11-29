/*******************************************************************************
 * Copyright (c) 2019, 2022 Lablicate GmbH.
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
import org.eclipse.swtchart.extensions.linecharts.LineChart;

public class ChartOptions {

	public static final String[][] FONT_STYLES = new String[][]{ //
			{Messages.getString(Messages.NORMAL), Integer.toString(SWT.NORMAL)}, //
			{Messages.getString(Messages.BOLD), Integer.toString(SWT.BOLD)}, //
			{Messages.getString(Messages.ITALIC), Integer.toString(SWT.ITALIC)} //
	};
	//
	public static final String[][] COMPRESSION_TYPES = new String[][]{ //
			{LineChart.COMPRESSION_EXTREME, LineChart.COMPRESSION_EXTREME}, //
			{LineChart.COMPRESSION_HIGH, LineChart.COMPRESSION_HIGH}, //
			{LineChart.COMPRESSION_MEDIUM, LineChart.COMPRESSION_MEDIUM}, //
			{LineChart.COMPRESSION_LOW, LineChart.COMPRESSION_LOW}, {LineChart.COMPRESSION_AUTO, LineChart.COMPRESSION_AUTO}, //
			{LineChart.COMPRESSION_NONE, LineChart.COMPRESSION_NONE} //
	};
	//
	public static final String[][] SYMBOL_TYPES = new String[][]{ //
			{Messages.getString(Messages.NONE), PlotSymbolType.NONE.toString()}, //
			{Messages.getString(Messages.CIRCLE), PlotSymbolType.CIRCLE.toString()}, //
			{Messages.getString(Messages.CROSS), PlotSymbolType.CROSS.toString()}, //
			{Messages.getString(Messages.DIAMON), PlotSymbolType.DIAMOND.toString()}, //
			{Messages.getString(Messages.INVERTED_TRIANGLE), PlotSymbolType.INVERTED_TRIANGLE.toString()}, //
			{Messages.getString(Messages.PLUS), PlotSymbolType.PLUS.toString()}, //
			{Messages.getString(Messages.SQUARE), PlotSymbolType.SQUARE.toString()}, //
			{Messages.getString(Messages.TRIANGLE), PlotSymbolType.TRIANGLE.toString()}, //
			{Messages.getString(Messages.EMOJI), PlotSymbolType.EMOJI.toString()} //
	};
	//
	public static final String[][] POSITIONS = new String[][]{ //
			{Messages.getString(Messages.PRIMARY), Position.Primary.toString()}, //
			{Messages.getString(Messages.SECONDARY), Position.Secondary.toString()} //
	};
	//
	public static final String[][] LINE_STYLES = new String[][]{ //
			{Messages.getString(Messages.NONE), LineStyle.NONE.toString()}, //
			{"-", LineStyle.DASH.toString()}, //
			{"-.", LineStyle.DASHDOT.toString()}, //
			{"-..", LineStyle.DASHDOTDOT.toString()}, //
			{".", LineStyle.DOT.toString()}, //
			{Messages.getString(Messages.SOLID), LineStyle.SOLID.toString()} //
	};
}
