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
package org.eclipse.swtchart.extensions.internal.marker;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.marker.AbstractBaseChartPaintListener;
import org.eclipse.swtchart.extensions.marker.IBaseChartPaintListener;
import org.eclipse.swtchart.Range;

public class AxisZeroMarker extends AbstractBaseChartPaintListener implements IBaseChartPaintListener {

	public AxisZeroMarker(BaseChart baseChart) {
		super(baseChart);
	}

	@Override
	public void paintControl(PaintEvent e) {

		if(isDraw()) {
			BaseChart baseChart = getBaseChart();
			Range xRange = baseChart.getAxisSet().getXAxes()[BaseChart.ID_PRIMARY_X_AXIS].getRange();
			Range yRange = baseChart.getAxisSet().getYAxes()[BaseChart.ID_PRIMARY_Y_AXIS].getRange();
			/*
			 * Mark the zero lines if possible.
			 * Otherwise draw the marker in half width.
			 */
			if(xRange.lower < 0 && xRange.upper > 0 && yRange.lower < 0 && yRange.upper > 0) {
				Rectangle rectangle = baseChart.getPlotArea().getClientArea();
				int width = rectangle.width;
				int height = rectangle.height;
				int xWidth;
				int yHeight;
				/*
				 * Dependent where the zero values are.
				 * xDelta and yDelta can't be zero -> protect from division by zero.
				 */
				double xDelta = xRange.upper - xRange.lower;
				double yDelta = yRange.upper - yRange.lower;
				double xDiff = xRange.lower * -1; // lower is negative
				double yDiff = yRange.upper;
				double xPart = ((100 / xDelta) * xDiff) / 100; // percent -> 0.0 - 1.0
				double yPart = ((100 / yDelta) * yDiff) / 100; // percent -> 0.0 - 1.0
				xWidth = (int)(width * xPart);
				yHeight = (int)(height * yPart);
				/*
				 * Draw the line.
				 */
				e.gc.setForeground(getForegroundColor());
				e.gc.drawLine(xWidth, 0, xWidth, height); // Vertical line through zero
				e.gc.drawLine(0, yHeight, width, yHeight); // Horizontal line through zero
			}
		}
	}
}
