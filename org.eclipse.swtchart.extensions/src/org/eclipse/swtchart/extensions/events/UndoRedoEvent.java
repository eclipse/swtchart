/*******************************************************************************
 * Copyright (c) 2017, 2021 Lablicate GmbH.
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
import org.eclipse.swtchart.extensions.core.IKeyboardSupport;

public class UndoRedoEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	private int redoMask = SWT.MOD2;

	@Override
	public int getEvent() {

		return IKeyboardSupport.EVENT_KEY_UP;
	}

	@Override
	public int getButton() {

		return IKeyboardSupport.KEY_CODE_LC_Z;
	}

	@Override
	public int getStateMask() {

		return SWT.MOD1;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		if((event.stateMask & redoMask) == redoMask) {
			/*
			 * Redo
			 */
			baseChart.redoSelection();
		} else {
			/*
			 * Undo
			 */
			baseChart.undoSelection();
		}
		baseChart.redraw();
	}
}
