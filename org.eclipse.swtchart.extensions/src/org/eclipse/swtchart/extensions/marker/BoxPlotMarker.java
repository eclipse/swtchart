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
 *******************************************************************************/
package org.eclipse.swtchart.extensions.marker;

import java.math.BigDecimal;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.IAxis;

public class BoxPlotMarker extends AbstractBaseChartPaintListener implements IBaseChartPaintListener {

	public BoxPlotMarker(BaseChart baseChart) {

		super(baseChart);
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
		for(ISeries<?> series : seriesSet.getSeries()) {
			double[] xData = series.getXSeries();
			double[] yData = series.getYSeries();
			int index = 0;
			int xMin = 0;
			int xMax = 0;
			int yMin = 0;
			int yMax = 0;
			for (; index < xData.length; index++) {
				int x = xAxis.getPixelCoordinate(xData[index], xLower, xUpper);
				int y = yAxis.getPixelCoordinate(yData[index], yLower, yUpper);
				if (0 == index) {
					 xMin = x;
					 xMax = x;
					 yMin = y;
					 yMax = y;
				} else {
					if (x > xMax) {
						xMax = x;
					}
					if (x < xMin) {
						xMin = x;
					}
					if (y > yMax) {
						yMax = y;
					}
					if (y < yMin) {
						yMin = y;
					}
				}		
			}
			Arrays.sort(yData);
			double q1= getValue(0.25, yData);
			double q2= getValue(0.50, yData);
			double q3= getValue(0.75, yData);
			int q1Y = yAxis.getPixelCoordinate(q1, yLower, yUpper);
			int q2Y = yAxis.getPixelCoordinate(q2, yLower, yUpper);
			int q3Y = yAxis.getPixelCoordinate(q3, yLower, yUpper);
			
			Rectangle rectangle = new Rectangle(xMin, q1Y, xMax-xMin, q3Y-q1Y);
			gc.setLineStyle(SWT.LINE_SOLID);
			gc.setLineWidth(2);
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
			gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			gc.setAlpha(100);
			gc.fillRectangle(xMin, q1Y, xMax-xMin, q3Y-q1Y);
			gc.drawRectangle(xMin, q1Y, xMax-xMin, q3Y-q1Y);
			gc.drawLine(xMin, yMin, xMax, yMin);
			gc.drawLine(xMin, yMax, xMax, yMax);
			gc.drawLine(xMin + (xMax - xMin) / 2, yMin, xMin + (xMax - xMin) / 2, q3Y);
			gc.drawLine(xMin + (xMax - xMin) / 2, yMax, xMin + (xMax - xMin) / 2, q1Y);
			gc.drawLine(xMin , q2Y, xMax, q2Y);
		}

	}
	
	
	public int getIndex(double rate, int size) {
		/**
		 * This index is just an approximation and I will modify it.
		 */
		int index = (int)(rate * (size +1));
		return index;
	}
	
	public double getValue(double rate, double[] data) {
		int size = data.length;
		int index = getIndex(rate, size);
		return data[index - 1];
	}
}