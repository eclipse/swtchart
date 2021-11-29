/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
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
package org.eclipse.swtchart.internal.axis;

import java.text.Format;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis.Position;

public class AxisPositionMarker implements PaintListener {

	private static final int NOT_SET = -1;
	private static final int TRIANGLE_DELTA = 3;
	private static final int OFFSET_RECTANGLE_X = 8;
	private static final int OFFSET_RECTANGLE_Y = 2;
	private static final int OFFSET_TEXT_X = 4;
	private static final int OFFSET_TEXT_Y = 2;
	private static final int OFFSET_SPACE_Y = 5;
	private static final int OFFSET_LINE_Y = 2;
	private static final int ARC_WIDTH = 5;
	private static final int ARC_HEIGHT = 3;
	//
	private Chart chart;
	private Axis axis;
	private Rectangle bounds;
	private int x = NOT_SET;
	private int y = NOT_SET;
	private boolean draw = false;

	public AxisPositionMarker(Chart chart, Axis axis) {

		this.chart = chart;
		this.axis = axis;
		/*
		 * Draw the position marker.
		 */
		this.chart.addPaintListener(this);
	}

	public void setBounds(int x, int y, int width, int height) {

		bounds = new Rectangle(x, y, width, height);
	}

	public Rectangle getBounds() {

		return bounds;
	}

	public boolean isDraw() {

		return draw;
	}

	public void setDraw(boolean draw) {

		this.draw = draw;
	}

	public void update(int x, int y) {

		this.x = x;
		this.y = y;
	}

	public void reset() {

		x = NOT_SET;
		y = NOT_SET;
	}

	@Override
	public void paintControl(PaintEvent e) {

		if(draw) {
			if(axis.getTick().isVisible() && isActive()) {
				/*
				 * GC Settings
				 */
				GC gc = e.gc;
				Color foreground = gc.getForeground();
				Color background = gc.getBackground();
				gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
				gc.setBackground(e.display.getSystemColor(SWT.COLOR_DARK_GRAY));
				Font font = gc.getFont();
				//
				AxisTick axisTick = axis.getTick();
				Format axisFormat = axisTick.getFormat();
				gc.setFont(axisTick.getFont());
				//
				if(axis.isHorizontalAxis()) {
					drawPositionMarkerX(e.gc, axisFormat);
				} else {
					drawPositionMarkerY(e.gc, axisFormat);
				}
				//
				gc.setForeground(foreground);
				gc.setBackground(background);
				gc.setFont(font);
			}
		}
		reset();
	}

	private boolean isActive() {

		return x > NOT_SET && y > NOT_SET;
	}

	private void drawPositionMarkerX(GC gc, Format format) {

		String textValue = getTextValue(format, getValueX());
		Point textExtend = getTextExtend(gc, textValue);
		//
		if(axis.getPosition() == Position.Primary) {
			drawPrimaryMarkerX(gc, textValue, textExtend);
		} else {
			drawSecondardMarkerX(gc, textValue, textExtend);
		}
	}

	private void drawPositionMarkerY(GC gc, Format format) {

		String textValue = getTextValue(format, getValueY());
		Point textExtend = getTextExtend(gc, textValue);
		//
		if(axis.getPosition() == Position.Primary) {
			drawPrimaryMarkerY(gc, textValue, textExtend);
		} else {
			drawSecondaryMarkerY(gc, textValue, textExtend);
		}
	}

	private void drawPrimaryMarkerX(GC gc, String textValue, Point textExtend) {

		int offsetX = textExtend.x / 2;
		int positionX = bounds.x + x - offsetX;
		int positionY = bounds.y + OFFSET_RECTANGLE_X;
		//
		drawTrianglePrimaryX(gc);
		fillRoundRectangle(gc, positionX, positionY, textExtend.x, textExtend.y);
		drawText(gc, positionX + OFFSET_TEXT_X, positionY + OFFSET_TEXT_Y, textValue);
	}

	private void drawSecondardMarkerX(GC gc, String textValue, Point textExtend) {

		int offsetX = textExtend.x / 2;
		int offsetY = textExtend.y / 2;
		int positionX = bounds.x + x - offsetX;
		int positionY = bounds.y - offsetY - OFFSET_RECTANGLE_X;
		//
		drawTriangleSecondaryX(gc);
		fillRoundRectangle(gc, positionX, positionY - OFFSET_SPACE_Y, textExtend.x, textExtend.y);
		drawText(gc, positionX + OFFSET_TEXT_X, positionY - OFFSET_SPACE_Y, textValue);
	}

	private void drawPrimaryMarkerY(GC gc, String textValue, Point textExtend) {

		int offsetY = textExtend.y / 2;
		int positionX = bounds.x - textExtend.x + OFFSET_RECTANGLE_X;
		int positionY = bounds.y + y - offsetY - OFFSET_LINE_Y;
		//
		drawTrianglePrimaryY(gc);
		fillRoundRectangle(gc, positionX - OFFSET_TEXT_Y, positionY, textExtend.x - OFFSET_TEXT_X, textExtend.y);
		drawText(gc, positionX, positionY + OFFSET_TEXT_Y, textValue);
	}

	private void drawSecondaryMarkerY(GC gc, String textValue, Point textExtend) {

		int offsetY = textExtend.y / 2;
		int positionX = bounds.x + OFFSET_RECTANGLE_X;
		int positionY = bounds.y + y - offsetY - OFFSET_LINE_Y;
		//
		drawTriangleSecondaryY(gc);
		fillRoundRectangle(gc, positionX, positionY, textExtend.x + OFFSET_TEXT_X, textExtend.y);
		drawText(gc, positionX + OFFSET_TEXT_X + TRIANGLE_DELTA, positionY + OFFSET_TEXT_Y, textValue);
	}

	private void drawTrianglePrimaryX(GC gc) {

		int positionX = bounds.x + x;
		int positionY = bounds.y;
		int x1 = positionX;
		int y1 = positionY;
		int x2 = positionX + OFFSET_RECTANGLE_X - TRIANGLE_DELTA;
		int y2 = positionY + OFFSET_RECTANGLE_X;
		int x3 = positionX - OFFSET_RECTANGLE_X + TRIANGLE_DELTA;
		int y3 = positionY + OFFSET_RECTANGLE_X;
		//
		drawTriangle(gc, x1, y1, x2, y2, x3, y3);
	}

	private void drawTriangleSecondaryX(GC gc) {

		int positionX = bounds.x + x;
		int positionY = bounds.y - OFFSET_SPACE_Y;
		int x1 = positionX + OFFSET_RECTANGLE_X - TRIANGLE_DELTA;
		int y1 = positionY;
		int x2 = positionX - OFFSET_RECTANGLE_X + TRIANGLE_DELTA;
		int y2 = positionY;
		int x3 = positionX;
		int y3 = positionY + OFFSET_RECTANGLE_X;
		//
		drawTriangle(gc, x1, y1, x2, y2, x3, y3);
	}

	private void drawTrianglePrimaryY(GC gc) {

		int positionX = bounds.x;
		int positionY = bounds.y + y - OFFSET_LINE_Y;
		int x1 = positionX;
		int y1 = positionY - OFFSET_RECTANGLE_X + TRIANGLE_DELTA;
		int x2 = positionX;
		int y2 = positionY + OFFSET_RECTANGLE_X - TRIANGLE_DELTA;
		int x3 = positionX + OFFSET_RECTANGLE_X;
		int y3 = positionY;
		//
		drawTriangle(gc, x1, y1, x2, y2, x3, y3);
	}

	private void drawTriangleSecondaryY(GC gc) {

		int positionX = bounds.x;
		int positionY = bounds.y + y - OFFSET_LINE_Y;
		int x1 = positionX + OFFSET_RECTANGLE_X;
		int y1 = positionY - OFFSET_RECTANGLE_X + TRIANGLE_DELTA;
		int x2 = positionX + OFFSET_RECTANGLE_X;
		int y2 = positionY + OFFSET_RECTANGLE_X - TRIANGLE_DELTA;
		int x3 = positionX;
		int y3 = positionY;
		//
		drawTriangle(gc, x1, y1, x2, y2, x3, y3);
	}

	private void drawTriangle(GC gc, int x1, int y1, int x2, int y2, int x3, int y3) {

		gc.fillPolygon(new int[]{x1, y1, x2, y2, x3, y3});
	}

	private void fillRoundRectangle(GC gc, int x, int y, int width, int height) {

		gc.fillRoundRectangle(x, y, width, height, ARC_WIDTH, ARC_HEIGHT);
	}

	private void drawText(GC gc, int x, int y, String text) {

		gc.drawText(text, x, y);
	}

	private String getTextValue(Format format, double value) {

		return format.format(value);
	}

	private Point getTextExtend(GC gc, String text) {

		Point point = gc.textExtent(text);
		point.x += OFFSET_RECTANGLE_X;
		point.y += OFFSET_RECTANGLE_Y;
		return point;
	}

	private double getValueX() {

		return axis.getDataCoordinate(x);
	}

	private double getValueY() {

		return axis.getDataCoordinate(y);
	}
}