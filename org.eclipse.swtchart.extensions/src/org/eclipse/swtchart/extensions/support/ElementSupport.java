/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.support;

import org.eclipse.swtchart.Range;

/*
 * This element supports only positive x ranges currently.
 * Keep in mind (Coordinates):
 * ---
 * The GC has its root upper left, whereas the root of primary axis data is lower left.
 * 0,0 --------------------------------+
 * |===================================|
 * |===================================|
 * |===================================|
 * |===================================|
 * |===================================|
 * 0,0 --------------------------------+
 */
public class ElementSupport {

	private Range rangeX;
	private Range rangeY;
	private double widthChart = 0;
	private double heightChart = 0;
	private double factorX = 0;
	private double factorY = 0;
	private double partX = 0;
	private double partY = 0;
	//
	private boolean coversNegativeRangeY = false;
	private double fullRangeNegativeY = 0;
	private double factorUpperRangeY = 0;
	private double upperPartY = 0;

	public ElementSupport(Range rangeX, Range rangeY, double widthChart, double heightChart) {

		this.rangeX = rangeX;
		this.rangeY = rangeY;
		this.widthChart = widthChart;
		this.heightChart = heightChart;
		//
		initialize();
	}

	public Range getRangeX() {

		return rangeX;
	}

	public Range getRangeY() {

		return rangeY;
	}

	public PointPrimary convertPoint(int x, int y) {

		double primaryX = rangeX.lower + partX * (factorX * x);
		double primaryY = 0;
		//
		if(coversNegativeRangeY) {
			if(y <= upperPartY) {
				primaryY = rangeY.upper * (1.0d - (factorY * y));
			} else {
				double yp = y - upperPartY;
				double factorp = 1 / (heightChart - upperPartY) * yp;
				primaryY = rangeY.lower * factorp;
			}
		} else {
			/*
			 * 1 - ... see coordinates
			 */
			primaryY = rangeY.lower + partY * (1.0d - (factorY * y));
		}
		//
		return new PointPrimary(primaryX, primaryY);
	}

	public RectanglePrimary convertRectangle(int x, int y, int width, int height) {

		PointPrimary primaryStart = convertPoint(x, y);
		PointPrimary primaryStop = convertPoint(x + width, y + height);
		//
		double widthPrimary = primaryStop.getX() - primaryStart.getX();
		double heightPrimary = 0;
		//
		if(coversNegativeRangeY) {
			if(y <= upperPartY) {
				heightPrimary = (primaryStart.getY() - primaryStop.getY()) * (1 / factorUpperRangeY);
			} else {
				heightPrimary = Math.abs(primaryStop.getY() - primaryStart.getY());
			}
		} else {
			heightPrimary = primaryStart.getY() - primaryStop.getY();
		}
		//
		return new RectanglePrimary(primaryStart.getX(), primaryStart.getY(), widthPrimary, heightPrimary);
	}

	private void initialize() {

		if(widthChart > 0 && heightChart > 0) {
			factorX = 1.0d / widthChart;
			factorY = 1.0d / heightChart;
		}
		//
		partX = rangeX.upper - rangeX.lower;
		partY = rangeY.upper - rangeY.lower;
		//
		coversNegativeRangeY = rangeY.lower < 0;
		if(coversNegativeRangeY) {
			fullRangeNegativeY = Math.abs(rangeY.lower) + rangeY.upper;
			if(fullRangeNegativeY > 0) {
				factorUpperRangeY = 1 / fullRangeNegativeY * rangeY.upper;
				upperPartY = heightChart * factorUpperRangeY;
			}
		}
	}
}