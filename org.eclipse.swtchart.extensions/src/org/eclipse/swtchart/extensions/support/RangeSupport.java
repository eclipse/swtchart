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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;

public class RangeSupport {

	/*
	 * Integer.MAX_VALUE doesn't work under Windows.
	 */
	private static final int HORIZONTAL_SCROLL_LENGTH = 1000000;
	private static final int VERTICAL_SCROLL_LENGTH = 1000000;

	public static void applyHorizontalSlide(BaseChart baseChart, double factor, boolean slidePrevious) {

		IAxis xAxis = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		IAxis yAxis = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		//
		if(xAxis != null && yAxis != null) {
			/*
			 * Slide to the previous or next block.
			 */
			double minX = baseChart.getMinX();
			double maxX = baseChart.getMaxX();
			double delta = (xAxis.getRange().upper - xAxis.getRange().lower) * factor;
			double lower;
			double upper;
			//
			if(slidePrevious) {
				/*
				 * Previous
				 */
				lower = xAxis.getRange().lower - delta;
				upper = xAxis.getRange().upper - delta;
				lower = (lower < minX) ? minX : lower;
			} else {
				/*
				 * Next
				 */
				lower = xAxis.getRange().lower + delta;
				upper = xAxis.getRange().upper + delta;
				upper = (upper > maxX) ? maxX : upper;
			}
			/*
			 * Validate the range.
			 */
			if(lower >= minX && upper <= maxX) {
				Range range = new Range(lower, upper);
				if(lower != upper) {
					applyHorizontalSlide(baseChart, xAxis, yAxis, range, new Event());
				}
			}
		}
	}

	public static void applyVerticalSlide(BaseChart baseChart, double factor, boolean slidePrevious) {

		IAxis xAxis = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		IAxis yAxis = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		//
		if(xAxis != null && yAxis != null) {
			/*
			 * Slide to the previous or next block.
			 */
			double minY = baseChart.getMinX();
			double maxY = baseChart.getMaxX();
			double delta = (yAxis.getRange().upper - yAxis.getRange().lower) * factor;
			double lower;
			double upper;
			//
			if(slidePrevious) {
				/*
				 * Previous
				 */
				lower = yAxis.getRange().lower + delta;
				upper = yAxis.getRange().upper + delta;
				upper = (upper > maxY) ? maxY : upper;
			} else {
				/*
				 * Next
				 */
				lower = yAxis.getRange().lower - delta;
				upper = yAxis.getRange().upper - delta;
				lower = (lower < minY) ? minY : lower;
			}
			/*
			 * Validate the range.
			 */
			if(lower >= minY && upper <= maxY) {
				Range range = new Range(lower, upper);
				if(lower != upper) {
					applyVerticalSlide(baseChart, xAxis, yAxis, range, new Event());
				}
			}
		}
	}

	/**
	 * This method returns null if the range can't be calculated correctly.
	 * 
	 * @param range
	 * @param slider
	 * @param sliderOrientation
	 * @return Range
	 */
	public static Range calculateShiftedRange(BaseChart baseChart, Range range, double selection, int sliderOrientation) {

		int maxX = HORIZONTAL_SCROLL_LENGTH;
		int maxY = VERTICAL_SCROLL_LENGTH;
		/*
		 * Relative binding to Integer.MAX_VALUE.
		 */
		double deltaX = baseChart.getMaxX() - baseChart.getMinX();
		double deltaY = baseChart.getMaxY() - baseChart.getMinY();
		//
		if(deltaX > 0 && deltaY > 0) {
			/*
			 * Shift calculation
			 */
			double coeffX = maxX / deltaX;
			double coeffY = maxY / deltaY;
			double shiftX = -coeffX * baseChart.getMinX();
			double shiftY = coeffY * baseChart.getMaxY();
			/*
			 * Validate
			 */
			if(coeffX != 0 && !Double.isNaN(shiftX) && coeffY != 0 && !Double.isNaN(shiftY)) {
				/*
				 * Get the current selection
				 */
				boolean isChartHorizontal = isOrientationHorizontal(baseChart);
				double max;
				double min;
				if(sliderOrientation == SWT.HORIZONTAL) {
					min = (isChartHorizontal ? (selection - shiftX) / coeffX : (shiftY - selection) / coeffY);
					max = (isChartHorizontal ? min + (range.upper - range.lower) : min - (range.upper - range.lower));
				} else {
					max = (!isChartHorizontal ? (selection - shiftX) / coeffX : (shiftY - selection) / coeffY);
					min = (!isChartHorizontal ? max + (range.upper - range.lower) : max - (range.upper - range.lower));
				}
				//
				if(!Double.isNaN(min) && !Double.isNaN(max)) {
					return new Range(min, max);
				}
			}
		}
		//
		return null;
	}

	public static boolean isOrientationHorizontal(BaseChart baseChart) {

		return (baseChart.getOrientation() == SWT.HORIZONTAL);
	}

	public static boolean applyHorizontalSlide(BaseChart baseChart, IAxis xAxis, IAxis yAxis, Range range, Event event) {

		if(xAxis != null && yAxis != null) {
			if(range != null) {
				if(RangeSupport.isOrientationHorizontal(baseChart)) {
					if(baseChart.isRangeValid(range)) {
						xAxis.setRange(range);
						baseChart.adjustMinMaxRange(xAxis);
						adjustSecondaryXAxes(baseChart);
					}
				} else {
					if(baseChart.isRangeValid(range)) {
						yAxis.setRange(range);
						baseChart.adjustMinMaxRange(yAxis);
						adjustSecondaryYAxes(baseChart);
					}
				}
				//
				baseChart.redraw();
				return true;
			}
		}
		//
		return false;
	}

	public static boolean applyVerticalSlide(BaseChart baseChart, IAxis xAxis, IAxis yAxis, Range range, Event event) {

		if(range != null) {
			if(RangeSupport.isOrientationHorizontal(baseChart)) {
				if(baseChart.isRangeValid(range)) {
					yAxis.setRange(range);
					baseChart.adjustMinMaxRange(yAxis);
					adjustSecondaryYAxes(baseChart);
				}
			} else {
				if(baseChart.isRangeValid(range)) {
					xAxis.setRange(range);
					baseChart.adjustMinMaxRange(xAxis);
					adjustSecondaryXAxes(baseChart);
				}
			}
			//
			baseChart.redraw();
			return true;
		}
		//
		return false;
	}

	private static void adjustSecondaryXAxes(BaseChart baseChart) {

		baseChart.adjustSecondaryXAxes();
	}

	private static void adjustSecondaryYAxes(BaseChart baseChart) {

		baseChart.adjustSecondaryYAxes();
	}
}