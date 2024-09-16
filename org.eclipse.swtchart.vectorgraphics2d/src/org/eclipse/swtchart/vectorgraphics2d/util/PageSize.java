/*******************************************************************************
 * Copyright (c) 2010, 2019 VectorGraphics2D project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Erich Seifert - initial API and implementation
 * Michael Seifert - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.util;

import java.awt.geom.Rectangle2D;

/**
 * <p>
 * Class that represents a page with a specified origin and size.
 * The class is immutable and can be initialized with coordinates and
 * dimensions or only dimensions:
 * </p>
 * <pre>PageSize a3 = new PageSize(0.0, 0.0, 297.0, 420.0);
 *PageSize a4 = new PageSize(210.0, 297.0);</pre>
 *
 * <p>
 * For convenience the class contains static constants for common page
 * sizes:
 * </p>
 * <pre>PageSize a4 = PageSize.A4;
 *PageSize letter = PageSize.LETTER;</pre>
 */
public class PageSize {

	private static final double MM_PER_INCH = 2.54;
	public static final PageSize A3 = new PageSize(297.0, 420.0);
	public static final PageSize A4 = new PageSize(210.0, 297.0);
	public static final PageSize A5 = new PageSize(148.0, 210.0);
	public static final PageSize LETTER = new PageSize(8.5 * MM_PER_INCH, 11.0 * MM_PER_INCH);
	public static final PageSize LEGAL = new PageSize(8.5 * MM_PER_INCH, 14.0 * MM_PER_INCH);
	public static final PageSize TABLOID = new PageSize(11.0 * MM_PER_INCH, 17.0 * MM_PER_INCH);
	public static final PageSize LEDGER = TABLOID.getLandscape();
	private final double x;
	private final double y;
	private final double width;
	private final double height;

	public PageSize(double x, double y, double width, double height) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public PageSize(double width, double height) {

		this(0.0, 0.0, width, height);
	}

	public PageSize(Rectangle2D size) {

		this(size.getX(), size.getY(), size.getWidth(), size.getHeight());
	}

	public PageSize getPortrait() {

		if(width <= height) {
			return this;
		}
		return new PageSize(x, y, height, width);
	}

	public PageSize getLandscape() {

		if(width >= height) {
			return this;
		}
		return new PageSize(x, y, height, width);
	}

	public double getX() {

		return x;
	}

	public double getY() {

		return y;
	}

	public double getWidth() {

		return width;
	}

	public double getHeight() {

		return height;
	}
}
