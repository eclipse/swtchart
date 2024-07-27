/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.RangeRestriction;

public class MouseWheelZoomEvent extends AbstractMouseEvent {

	@Override
	public int getStateMask() {

		return SWT.NONE;
	}

	protected void runAction(BaseChart baseChart, Event event) {

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
			showClickbindingHelp(baseChart, "Zoom", "Zoom the X and Y axis.");
		} else {
			/*
			 * X or Y zoom.
			 */
			if(rangeRestriction.isRestrictZoomX()) {
				baseChart.zoomX(xAxis, event);
				showClickbindingHelp(baseChart, "Zoom", "Zoom the X axis.");
			} else if(rangeRestriction.isRestrictZoomY()) {
				baseChart.zoomY(yAxis, event);
				showClickbindingHelp(baseChart, "Zoom", "Zoom the Y axis.");
			}
		}
	}
}