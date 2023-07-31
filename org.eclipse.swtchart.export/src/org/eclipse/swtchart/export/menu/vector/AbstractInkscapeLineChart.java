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
 * Philip Wenig - initial API and implementation
 * Sanatt Abrol - SVG export code
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.export.menu.vector;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.export.core.AxisSettings;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;

public abstract class AbstractInkscapeLineChart extends AbstractInkscapeTemplate {

	protected StringBuilder printScatterData(ISeries<?> dataSeries, int widthPlotArea, int heightPlotArea, AxisSettings axisSettings, int index, PrintWriter printWriter, IAxisSet axisSet, boolean isReversedX, boolean isReversedY) {

		return printScatterData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index, axisSet, isReversedX, isReversedY);
	}

	protected StringBuilder printScatterData(ISeries<?> dataSeries, int widthPlotArea, int heightPlotArea, AxisSettings axisSettings, int index, IAxisSet axisSet, boolean isReversedX, boolean isReversedY) {

		StringBuilder out = new StringBuilder("");
		StringBuilder data = new StringBuilder("<circle\n" + "         style=\"opacity:1;fill:%COLOR%;fill-opacity:1;stroke:none;stroke-width:0.96499991;stroke-linecap:square;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-dashoffset:0;stroke-opacity:1\"\n" + "         id=\"rect901\"\n" + "	 cx=\"%x-coordinate%\"\n" + "	 cy=\"%y-coordinate%\"\n" + "	 r=\"1\" />");
		ILineSeries<?> lineSerie = (ILineSeries<?>)dataSeries;
		Color lineColor = lineSerie.getSymbolColor();
		String color = getColor(lineColor);
		int indexAxisX = axisSettings.getIndexAxisX();
		int indexAxisY = axisSettings.getIndexAxisY();
		IAxisScaleConverter axisScaleConverterX = axisSettings.getAxisScaleConverterX();
		IAxisScaleConverter axisScaleConverterY = axisSettings.getAxisScaleConverterY();
		//
		double[] xSeries = dataSeries.getXSeries();
		double[] ySeries = dataSeries.getYSeries();
		String split[] = data.toString().split(SPLIT_LINE_DELIMITER);
		int size = dataSeries.getXSeries().length;
		//
		String match1 = ".*%COLOR%.*";
		String match2 = ".*%x-coordinate%.*";
		String match3 = ".*%y-coordinate%.*";
		for(int i = 0; i < size; i++) {
			/*
			 * Only export if the data point is visible.
			 */
			Point point = dataSeries.getPixelCoordinates(i);
			if((point.x >= 0 && point.x <= widthPlotArea) && (point.y >= 0 && point.y <= heightPlotArea)) {
				double x = Double.parseDouble(printValueScatterPlot(AXIS_X, index, xSeries[i], indexAxisX, axisSet, BaseChart.ID_PRIMARY_X_AXIS, axisScaleConverterX, isReversedX, isReversedY));
				double y = Double.parseDouble(printValueScatterPlot(AXIS_Y, index, ySeries[i], indexAxisY, axisSet, BaseChart.ID_PRIMARY_Y_AXIS, axisScaleConverterY, isReversedX, isReversedY));
				for(String string : split) {
					if(Pattern.matches(match1, string)) {
						string = string.replace("%COLOR%", color);
					} else if(Pattern.matches(match2, string)) {
						string = string.replace("%x-coordinate%", String.valueOf(x));
					} else if(Pattern.matches(match3, string)) {
						string = string.replace("%y-coordinate%", String.valueOf(y));
					}
					out.append(string);
					out.append("\n");
				}
			}
		}
		return out;
	}

	private String printValueScatterPlot(String axis, int index, double value, int indexAxis, IAxisSet axisSet, int indexPrimaryAxis, IAxisScaleConverter axisScaleConverter, boolean isReversedX, boolean isReversedY) {

		String ret = null;
		double x = 255.5 - 23.5;
		double y = 263.5 - 80.5;
		if(indexAxis == indexPrimaryAxis || axisScaleConverter == null) {
			if(axis.equals(AXIS_X)) {
				IAxis xAxis = axisSet.getXAxis(indexAxis);
				double xUpper = xAxis.getRange().upper;
				double xLower = xAxis.getRange().lower;
				double x1;
				if(!isReversedX) {
					x1 = 23.5 + ((value - xLower) / (xUpper - xLower) * x);
				} else {
					x1 = ((23.5 + x) - ((value - xLower) / (xUpper - xLower) * x));
				}
				ret = String.valueOf(x1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			} else if(axis.equals(AXIS_Y)) {
				IAxis yAxis = axisSet.getYAxis(indexAxis);
				double yUpper = yAxis.getRange().upper;
				double yLower = yAxis.getRange().lower;
				double y1 = 263.5 - (y - ((yUpper - value) / (yUpper - yLower) * y));
				ret = String.valueOf(y1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
		} else {
			if(axisScaleConverter != null) {
				if(axis.equals(AXIS_X)) {
					IAxis xAxis = axisSet.getXAxis(indexAxis);
					double xUpper = xAxis.getRange().upper;
					double xLower = xAxis.getRange().lower;
					value = axisScaleConverter.convertToSecondaryUnit(value);
					double x1;
					if(!isReversedX) {
						x1 = 23.5 + ((axisScaleConverter.convertToSecondaryUnit(value) - xLower) / (xUpper - xLower) * x);
					} else {
						x1 = ((23.5 + x) - ((axisScaleConverter.convertToSecondaryUnit(value) - xLower) / (xUpper - xLower) * x));
					}
					ret = String.valueOf(x1);
				} else if(axis.equals(AXIS_Y)) {
					IAxis yAxis = axisSet.getYAxis(indexAxis);
					double yUpper = yAxis.getRange().upper;
					double yLower = yAxis.getRange().lower;
					value = axisScaleConverter.convertToSecondaryUnit(value);
					double y1;
					if(!isReversedY) {
						y1 = 80.5 + ((yUpper - axisScaleConverter.convertToSecondaryUnit(value)) / (yUpper - yLower) * y);
					} else {
						y1 = ((263.5 - y) + ((yUpper - axisScaleConverter.convertToSecondaryUnit(value)) / (yUpper - yLower) * y));
					}
					ret = String.valueOf(y1);
				}
			}
		}
		return ret;
	}
}