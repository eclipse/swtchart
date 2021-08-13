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
package org.eclipse.swtchart.extensions.examples.charts.macos;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.extensions.examples.parts.LineSeries_1_Part;

public class LineChartX extends LineSeries_1_Part {

	private Cursor defaultCursor;
	private BaselineSelectionPaintListenerX baselineSelectionPaintListenerX;
	//
	private int xStart;
	private int yStart;
	private int xStop;
	private int yStop;

	public LineChartX(Composite parent) {

		super(parent);
		createControl();
	}

	private void createControl() {

		defaultCursor = getBaseChart().getCursor();
		/*
		 * Add the paint listeners to draw the selected peak range.
		 */
		IPlotArea plotArea = getBaseChart().getPlotArea();
		baselineSelectionPaintListenerX = new BaselineSelectionPaintListenerX();
		plotArea.addCustomPaintListener(baselineSelectionPaintListenerX);
	}

	@Override
	public void handleMouseDownEvent(Event event) {

		super.handleMouseDownEvent(event);
		if(isControlKeyPressed(event) && event.count == 1) {
			System.out.println("MouseDown (Chart): " + event);
			startBaselineSelection(event.x, event.y);
			setCursor(SWT.CURSOR_CROSS);
		}
	}

	@Override
	public void handleMouseMoveEvent(Event event) {

		super.handleMouseMoveEvent(event);
		if(isControlKeyPressed(event)) {
			System.out.println("MouseMove (Chart): " + event);
			if(xStart > 0 && yStart > 0) {
				trackBaselineSelection(event.x, event.y);
			}
		}
	}

	@Override
	public void handleMouseUpEvent(Event event) {

		super.handleMouseUpEvent(event);
		if(isControlKeyPressed(event)) {
			System.out.println("MouseUp (Chart): " + event);
			stopBaselineSelection(event.x, event.y);
			setCursorDefault();
			resetSelectedRange();
		}
	}

	private void startBaselineSelection(int x, int y) {

		xStart = x;
		yStart = y;
		/*
		 * Set the start point.
		 */
		baselineSelectionPaintListenerX.setX1(xStart);
		baselineSelectionPaintListenerX.setY1(yStart);
	}

	private void trackBaselineSelection(int x, int y) {

		xStop = x;
		yStop = y;
		//
		baselineSelectionPaintListenerX.setX1(xStart);
		baselineSelectionPaintListenerX.setY1(yStart);
		baselineSelectionPaintListenerX.setX2(xStop);
		baselineSelectionPaintListenerX.setY2(yStop);
		//
		redrawChart();
	}

	private void stopBaselineSelection(int x, int y) {

		xStop = x;
		yStop = y;
	}

	private void resetSelectedRange() {

		baselineSelectionPaintListenerX.reset();
		//
		xStart = 0;
		yStart = 0;
		xStop = 0;
		yStop = 0;
		//
		redrawChart();
	}

	private boolean isControlKeyPressed(Event event) {

		System.out.println("Event (Chart): " + event);
		return (event.stateMask & SWT.MOD1) == SWT.MOD1;
	}

	private void setCursor(int cursorId) {

		getBaseChart().setCursor(getBaseChart().getDisplay().getSystemCursor(cursorId));
	}

	private void setCursorDefault() {

		getBaseChart().setCursor(defaultCursor);
	}

	private void redrawChart() {

		getBaseChart().redraw();
	}
}