/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 * Frank Buloup - Internationalization
 * Philip Wenig - line style handling
 *******************************************************************************/
package org.eclipse.swtchart.internal;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

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
		/*
		 * Create a GC
		 */
		int ARBITRARY_WIDTH = 10;
		int ARBITRARY_HEIGHT = 10;
		Image image = new Image(Display.getCurrent(), ARBITRARY_WIDTH, ARBITRARY_HEIGHT);
		GC gc = new GC(image);
		// get extent of text with given font
		gc.setFont(font);
		Point p;
		if(text == null || "".equals(text.trim())) { //$NON-NLS-1$
			p = new Point(0, gc.getFontMetrics().getHeight());
		} else {
			p = gc.textExtent(text);
		}
		/*
		 * Dispose resources
		 */
		image.dispose();
		gc.dispose();
		//
		return p;
	}
}