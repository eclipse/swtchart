/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.charts;

import org.eclipse.swt.SWT;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.linecharts.LineChart;

public class ChartOptions {

	public static String[][] FONT_STYLES = new String[][]{//
			{"Normal", Integer.toString(SWT.NORMAL)}, //
			{"Bold", Integer.toString(SWT.BOLD)}, //
			{"Italic", Integer.toString(SWT.ITALIC)}//
	};
	//
	public static String[][] COMPRESSION_TYPES = new String[][]{//
			{LineChart.COMPRESSION_EXTREME, LineChart.COMPRESSION_EXTREME}, //
			{LineChart.COMPRESSION_HIGH, LineChart.COMPRESSION_HIGH}, //
			{LineChart.COMPRESSION_MEDIUM, LineChart.COMPRESSION_MEDIUM}, //
			{LineChart.COMPRESSION_LOW, LineChart.COMPRESSION_LOW}, {LineChart.COMPRESSION_AUTO, LineChart.COMPRESSION_AUTO}, //
			{LineChart.COMPRESSION_NONE, LineChart.COMPRESSION_NONE}//
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
	public static String[][] POSITIONS = new String[][]{//
			{"Primary", Position.Primary.toString()}, //
			{"Secondary", Position.Secondary.toString()}//
	};
	//
	public static String[][] LINE_STYLES = new String[][]{//
			{"None", LineStyle.NONE.toString()}, //
			{"-", LineStyle.DASH.toString()}, //
			{"-.", LineStyle.DASHDOT.toString()}, //
			{"-..", LineStyle.DASHDOTDOT.toString()}, //
			{".", LineStyle.DOT.toString()}, //
			{"Solid", LineStyle.SOLID.toString()}//
	};
}
