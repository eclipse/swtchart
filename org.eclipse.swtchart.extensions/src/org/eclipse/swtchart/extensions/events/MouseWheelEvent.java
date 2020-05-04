/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.core.RangeRestriction;

public class MouseWheelEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	@Override
	public int getEvent() {

		return IMouseSupport.EVENT_MOUSE_WHEEL;
	}

	@Override
	public int getButton() {

		return IMouseSupport.MOUSE_BUTTON_WHEEL;
	}

	@Override
	public int getStateMask() {

		return SWT.NONE;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		System.out.println(baseChart);
		System.out.println(event);
		runZoomAction(baseChart, event);
		postValidateZoom(baseChart);
		//
		baseChart.fireUpdateCustomRangeSelectionHandlers(event);
		baseChart.redraw();
	}

	private void runZoomAction(BaseChart baseChart, Event event) {

		RangeRestriction rangeRestriction = baseChart.getRangeRestriction();
		IAxisSet axisSet = baseChart.getAxisSet();
		IAxis xAxis = axisSet.getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		IAxis yAxis = axisSet.getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		//
		if(baseChart.isZoomXY(rangeRestriction)) {
			/*
			 * X and Y zoom.
			 */
			baseChart.zoomX(xAxis, event);
			baseChart.zoomY(yAxis, event);
		} else {
			/*
			 * X or Y zoom.
			 */
			if(rangeRestriction.isRestrictZoomX()) {
				baseChart.zoomX(xAxis, event);
			} else if(rangeRestriction.isRestrictZoomY()) {
				baseChart.zoomY(yAxis, event);
			}
		}
	}

	private void postValidateZoom(BaseChart baseChart) {

		RangeRestriction rangeRestriction = baseChart.getRangeRestriction();
		IAxisSet axisSet = baseChart.getAxisSet();
		IAxis xAxis = axisSet.getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		IAxis yAxis = axisSet.getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		/*
		 * Adjust the range if it shall not exceed the initial
		 * min and max values.
		 */
		if(rangeRestriction.isRestrictFrame()) {
			/*
			 * Adjust the primary axes.
			 * The secondary axes are adjusted by setting the range.
			 */
			Range rangeX = xAxis.getRange();
			Range rangeY = yAxis.getRange();
			baseChart.setRange(xAxis, rangeX.lower, rangeX.upper, true);
			baseChart.setRange(yAxis, rangeY.lower, rangeY.upper, true);
		} else {
			/*
			 * Update the secondary axes.
			 */
			baseChart.adjustSecondaryXAxes();
			baseChart.adjustSecondaryYAxes();
		}
	}
}
