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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.events.MouseMoveShiftEvent;

public class MouseMoveShiftEventX extends MouseMoveShiftEvent {

	@Override
	public int getEvent() {

		return IMouseSupport.EVENT_MOUSE_MOVE;
	}

	@Override
	public int getButton() {

		return IMouseSupport.MOUSE_BUTTON_NONE;
	}

	@Override
	public int getStateMask() {

		return SWT.CTRL;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		System.out.println("MouseMoveShift (Handler): " + event);
		super.handleEvent(baseChart, event);
	}

	protected boolean isSingleClick(Event event) {

		System.out.println("Event (MouseMoveShift): " + event);
		return event.count == 1;
	}
}