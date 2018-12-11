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
package org.eclipse.swtchart.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.LineStyle;

/**
 * A utility class providing generic methods.
 */
public final class Util {

	/**
	 * Gets the text extent with given font in GC. If the given font is
	 * <code>null</code> or already disposed, point containing size zero will be
	 * returned.
	 * 
	 * @param font
	 *            the font
	 * @param text
	 *            the text
	 * @return a point containing text extent
	 */
	public static Point getExtentInGC(Font font, String text) {

		if(font == null || font.isDisposed()) {
			return new Point(0, 0);
		}
		// create GC
		int ARBITRARY_WIDTH = 10;
		int ARBITRARY_HEIGHT = 10;
		Image image = new Image(Display.getCurrent(), ARBITRARY_WIDTH, ARBITRARY_HEIGHT);
		GC gc = new GC(image);
		// get extent of text with given font
		gc.setFont(font);
		Point p;
		if(text == null || "".equals(text.trim())) {
			p = new Point(0, gc.getFontMetrics().getHeight());
		} else {
			p = gc.textExtent(text);
		}
		// dispose resources
		image.dispose();
		gc.dispose();
		return p;
	}

	/**
	 * Gets the index defined in SWT.
	 * 
	 * @param lineStyle
	 *            the line style
	 * @return the index defined in SWT.
	 */
	public static int getIndexDefinedInSWT(LineStyle lineStyle) {

		switch(lineStyle) {
			case NONE:
				return SWT.NONE;
			case SOLID:
				return SWT.LINE_SOLID;
			case DASH:
				return SWT.LINE_DASH;
			case DOT:
				return SWT.LINE_DOT;
			case DASHDOT:
				return SWT.LINE_DASHDOT;
			case DASHDOTDOT:
				return SWT.LINE_DASHDOTDOT;
			default:
				return SWT.LINE_SOLID;
		}
	}
}
