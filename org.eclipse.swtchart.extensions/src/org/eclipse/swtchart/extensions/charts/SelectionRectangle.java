/*******************************************************************************
 * Copyright (c) 2008, 2018 SWTChart project.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.charts;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * Selection rectangle with mouse to zoom in/out.
 */
public class SelectionRectangle {

	/** the start point of selection */
	private Point startPoint;
	/** the end point of selection */
	private Point endPoint;

	/**
	 * Sets the start point.
	 * 
	 * @param x
	 *            the X coordinate of start point in pixels
	 * @param y
	 *            the Y coordinate of start point in pixels
	 */
	public void setStartPoint(int x, int y) {

		startPoint = new Point(x, y);
	}

	/**
	 * Sets the end point.
	 * 
	 * @param x
	 *            the X coordinate of end point in pixels
	 * @param y
	 *            the X coordinate of end point in pixels
	 */
	public void setEndPoint(int x, int y) {

		endPoint = new Point(x, y);
	}

	/**
	 * Gets the horizontal selected range.
	 * 
	 * @return the horizontal selected range in pixels
	 */
	public Point getHorizontalRange() {

		if(startPoint == null || endPoint == null) {
			return null;
		}
		return new Point(startPoint.x, endPoint.x);
	}

	/**
	 * Gets the vertical selected range.
	 * 
	 * @return the vertical selected range in pixels
	 */
	public Point getVerticalRange() {

		if(startPoint == null || endPoint == null) {
			return null;
		}
		return new Point(startPoint.y, endPoint.y);
	}

	/**
	 * Check if selection is disposed.
	 * 
	 * @return true if selection is disposed.
	 */
	public boolean isDisposed() {

		return startPoint == null;
	}

	/**
	 * Disposes the resource.
	 */
	public void dispose() {

		startPoint = null;
		endPoint = null;
	}

	public void draw(GC gc) {

		if(startPoint == null || endPoint == null) {
			return;
		}
		//
		int minX = Math.min(startPoint.x, endPoint.x);
		int maxX = Math.max(startPoint.x, endPoint.x);
		int minY = Math.min(startPoint.y, endPoint.y);
		int maxY = Math.max(startPoint.y, endPoint.y);
		//
		gc.drawRectangle(minX, minY, maxX - minX, maxY - minY);
	}
}
