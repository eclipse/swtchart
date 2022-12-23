/*******************************************************************************
 * Copyright (c) 2022 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * yoshitaka - support for axis categories
 *******************************************************************************/
package org.eclipse.swtchart.extensions.marker;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.scattercharts.BoxPlotChart;

public class BoxPlotMarker extends AbstractBaseChartPaintListener implements IBaseChartPaintListener {

	private static final int MAX_BOX_WIDTH = 80;
	private BoxPlotChart boxPlotChart;

	public BoxPlotMarker(BaseChart baseChart, BoxPlotChart boxPlotChart) {

		super(baseChart);
		this.boxPlotChart = boxPlotChart;
	}

	@SuppressWarnings("unused")
	@Override
	public void paintControl(PaintEvent e) {

		BaseChart baseChart = getBaseChart();
		ISeriesSet seriesSet = baseChart.getSeriesSet();
		GC gc = e.gc;
		IAxis xAxis = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		IAxis yAxis = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		double xLower = xAxis.getRange().lower;
		double xUpper = xAxis.getRange().upper;
		double yLower = yAxis.getRange().lower;
		double yUpper = yAxis.getRange().upper;
		ISeries<?>[] series = seriesSet.getSeries();
		double boxWidthRatio = 0.7;
		int boxWidth = (int)(baseChart.getPlotArea().getBounds().width / (xUpper + 1 - xLower) * boxWidthRatio);
		boxWidth = Math.min(MAX_BOX_WIDTH, boxWidth);
		for(int categoryIndex = 0; categoryIndex < series.length; categoryIndex++) {
			double[] xData = series[categoryIndex].getXSeries();
			double[] yData = series[categoryIndex].getYSeries();
			double yMinData = Double.MAX_VALUE;
			double yMaxData = -Double.MAX_VALUE;
			for(int index = 0; index < xData.length; index++) {
				yMinData = Math.min(yMinData, yData[index]);
				yMaxData = Math.max(yMaxData, yData[index]);
			}
			int yMin = yAxis.getPixelCoordinate(yMinData, yLower, yUpper);
			int yMax = yAxis.getPixelCoordinate(yMaxData, yLower, yUpper);
			int xCenter = xAxis.getPixelCoordinate(categoryIndex, xLower, xUpper);
			int xMin = xCenter - boxWidth / 2;
			int xMax = xCenter + boxWidth / 2;
			Arrays.sort(yData);
			double q1 = getValue(0.25, yData);
			double q2 = getValue(0.50, yData);
			double q3 = getValue(0.75, yData);
			int q1Y = yAxis.getPixelCoordinate(q1, yLower, yUpper);
			int q2Y = yAxis.getPixelCoordinate(q2, yLower, yUpper);
			int q3Y = yAxis.getPixelCoordinate(q3, yLower, yUpper);
			gc.setLineStyle(SWT.LINE_SOLID);
			gc.setLineWidth(2);
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
			gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			int originalAlpha = gc.getAlpha();
			gc.setAlpha(100);
			gc.fillRectangle(xMin, q3Y, xMax - xMin, q1Y - q3Y);
			gc.drawRectangle(xMin, q3Y, xMax - xMin, q1Y - q3Y);
			gc.drawLine(xMin, yMin, xMax, yMin);
			gc.drawLine(xMin, yMax, xMax, yMax);
			gc.drawLine(xCenter, yMin, xCenter, q1Y);
			gc.drawLine(xCenter, yMax, xCenter, q3Y);
			gc.drawLine(xMin, q2Y, xMax, q2Y);
			gc.setAlpha(originalAlpha);
			grawPointSymbols(gc, series[categoryIndex], xAxis, yAxis, xCenter, boxWidth);
		}
	}

	private void grawPointSymbols(GC gc, ISeries<?> series, IAxis xAxis, IAxis yAxis, int xCenter, int boxWidth) {

		if(series instanceof ILineSeries) {
			ILineSeries<?> lineSeries = (ILineSeries<?>)series;
			Color[] symbolColors = lineSeries.getSymbolColors();
			PlotSymbolType symbolType = boxPlotChart.getPlotSymbolType(series.getId());
			int symbolSize = lineSeries.getSymbolSize();
			double[] xData = series.getXSeries();
			double[] yData = series.getYSeries();
			double xMinData = Double.MAX_VALUE;
			double xMaxData = -Double.MAX_VALUE;
			for(int index = 0; index < xData.length; index++) {
				xMinData = Math.min(xMinData, xData[index]);
				xMaxData = Math.max(xMaxData, xData[index]);
			}
			for(int i = 0; i < xData.length; i++) {
				Color color;
				if(symbolColors.length > i) {
					color = symbolColors[i];
				} else {
					color = lineSeries.getSymbolColor();
				}
				// scale the x position within box
				int x = (int)(xCenter - boxWidth / 2d + (xData[i] - xMinData) / (xMaxData - xMinData) * boxWidth);
				int y = yAxis.getPixelCoordinate(yData[i]);
				Color oldForeground = gc.getForeground();
				gc.setForeground(color);
				Color oldBackground = gc.getBackground();
				gc.setBackground(color);
				switch(symbolType) {
					case CIRCLE:
						gc.fillOval(x - symbolSize, y - symbolSize, symbolSize * 2, symbolSize * 2);
						break;
					case SQUARE:
						gc.fillRectangle(x - symbolSize, y - symbolSize, symbolSize * 2, symbolSize * 2);
						break;
					case DIAMOND:
						int[] diamondArray = {x, y - symbolSize, x + symbolSize, y, x, y + symbolSize, x - symbolSize, y};
						gc.fillPolygon(diamondArray);
						break;
					case TRIANGLE:
						int[] triangleArray = {x, y - symbolSize, x + symbolSize, y + symbolSize, x - symbolSize, y + symbolSize};
						gc.fillPolygon(triangleArray);
						break;
					case INVERTED_TRIANGLE:
						int[] invertedTriangleArray = {x, y + symbolSize, x + symbolSize, y - symbolSize, x - symbolSize, y - symbolSize};
						gc.fillPolygon(invertedTriangleArray);
						break;
					case CROSS:
						gc.setLineStyle(SWT.LINE_SOLID);
						gc.drawLine(x - symbolSize, y - symbolSize, x + symbolSize, y + symbolSize);
						gc.drawLine(x - symbolSize, y + symbolSize, x + symbolSize, y - symbolSize);
						break;
					case PLUS:
						gc.setLineStyle(SWT.LINE_SOLID);
						gc.drawLine(x, y - symbolSize, x, y + symbolSize);
						gc.drawLine(x - symbolSize, y, x + symbolSize, y);
						break;
					case EMOJI:
						String extendedSymbol = lineSeries.getExtendedPlotSymbolType();
						Point extendedSymbolSize = gc.textExtent(extendedSymbol);
						gc.drawText(extendedSymbol, x - extendedSymbolSize.x / 2, y - extendedSymbolSize.y / 2, true);
						break;
					case NONE:
					default:
						break;
				}
				gc.setBackground(oldBackground);
				gc.setForeground(oldForeground);
			}
		}
	}

	public int getIndex(double rate, int size) {

		/**
		 * This index is just an approximation and I will modify it.
		 */
		int index = (int)(rate * (size + 1));
		return index;
	}

	public double getValue(double rate, double[] data) {

		int size = data.length;
		int index = getIndex(rate, size);
		return data[index - 1];
	}
}