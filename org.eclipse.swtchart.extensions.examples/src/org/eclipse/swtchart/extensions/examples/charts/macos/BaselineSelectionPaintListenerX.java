/*******************************************************************************
 * Copyright (c) 2011, 2021 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts.macos;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ICustomPaintListener;

public class BaselineSelectionPaintListenerX implements ICustomPaintListener {

	private final static int VERTICAL_MARKER_SIZE = 10;
	//
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	//

	@Override
	public void paintControl(PaintEvent e) {

		if(x1 != x2) {
			System.out.println("Baseline (Listener): " + e);
			Color foreground = e.gc.getForeground();
			Color background = e.gc.getBackground();
			e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			e.gc.drawLine(x1, y1 - VERTICAL_MARKER_SIZE, x1, y1 + VERTICAL_MARKER_SIZE); // vertical start
			e.gc.drawLine(x1, y1, x2, y2);
			e.gc.drawLine(x2, y2 - VERTICAL_MARKER_SIZE, x2, y2 + VERTICAL_MARKER_SIZE); // vertical stop
			e.gc.setForeground(foreground);
			e.gc.setBackground(background);
		}
	}

	@Override
	public boolean drawBehindSeries() {

		return false;
	}

	/**
	 * Resets the start and end point.
	 */
	public void reset() {

		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;
	}

	/**
	 * @param x1
	 */
	public void setX1(int x1) {

		this.x1 = x1;
	}

	/**
	 * @param y1
	 */
	public void setY1(int y1) {

		this.y1 = y1;
	}

	/**
	 * @param x2
	 */
	public void setX2(int x2) {

		this.x2 = x2;
	}

	/**
	 * @param y2
	 */
	public void setY2(int y2) {

		this.y2 = y2;
	}
}
