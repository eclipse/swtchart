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
package org.eclipse.swtchart;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Bar series.
 */
public interface IBarSeries extends ISeries {

	/**
	 * Bar width style.
	 */
	public enum BarWidthStyle {
		/** the style stretching the bar width depending on interval of bars. */
		STRETCHED,
		/** the style fixing the bar width regardless of interval of bars. */
		FIXED;
	}

	/**
	 * Gets the bar width style.
	 * 
	 * @param style
	 *            the bar width style
	 * @return the bar width style
	 */
	BarWidthStyle getBarWidthStyle(BarWidthStyle style);

	/**
	 * Sets the bar width style. The default is <tt>BarWidthStyle.STRETCHED</tt>
	 * .
	 * 
	 * @param style
	 *            the bar width style
	 */
	void setBarWidthStyle(BarWidthStyle style);

	/**
	 * Gets the bar width in pixels.
	 * 
	 * @return the bar width in pixels
	 */
	int getBarWidth();

	/**
	 * Sets the bar width in pixels. The specified bar width is active only when
	 * the bar width style is set to <tt>BarWidthStyle.FIXED</tt>.
	 * 
	 * @param width
	 *            the bar width in pixels
	 */
	void setBarWidth(int width);

	/**
	 * Gets the bar padding in percentage.
	 * 
	 * @return the bar padding in percentage
	 */
	int getBarPadding();

	/**
	 * Sets the bar padding in percentage. The specified padding is active only
	 * when the bar width style is set to <tt>BarWidthStyle.STRETCHED</tt>.
	 * 
	 * @param padding
	 *            the bar padding in percentage
	 */
	void setBarPadding(int padding);

	/**
	 * Gets the bar color.
	 * 
	 * @return the bar color
	 */
	Color getBarColor();

	/**
	 * Sets the bar color. If null is given, default color will be set.
	 * 
	 * @param color
	 *            the bar color
	 */
	void setBarColor(Color color);

	/**
	 * Gets the array of bar rectangles. This method is typically used for mouse
	 * listener to check whether mouse cursor is on bar.
	 * <p>
	 * The returned array has the same size as data points. Depending on X axis
	 * range, some bars can be out of screen. In this case, the rectangles for
	 * invisible bars will be <tt>null<tt> in the returned array.
	 * 
	 * @return the array of bar rectangles in pixels.
	 */
	Rectangle[] getBounds();
}