/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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
import org.eclipse.swtchart.extensions.core.BaseChart;

public class MouseMoveSelectionEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	@Override
	public int getEvent() {

		return BaseChart.EVENT_MOUSE_MOVE;
	}

	@Override
	public int getStateMask() {

		return SWT.BUTTON1;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		/*
		 * Set Selection Range
		 */
		baseChart.getUserSelection().setStopCoordinate(event.x, event.y);
		baseChart.increaseRedrawCounter();
		if(baseChart.isRedraw()) {
			/*
			 * Rectangle is drawn here:
			 * void paintControl(PaintEvent e)
			 */
			baseChart.redraw();
			baseChart.resetRedrawCounter();
		}
	}
}
