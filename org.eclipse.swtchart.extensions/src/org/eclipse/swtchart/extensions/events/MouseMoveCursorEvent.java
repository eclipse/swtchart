/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swtchart.extensions.core.BaseChart;

public class MouseMoveCursorEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	private Cursor defaultCursor = null;
	private ToolTip tooltip = null;

	@Override
	public int getEvent() {

		return BaseChart.EVENT_MOUSE_MOVE;
	}

	@Override
	public int getStateMask() {

		return SWT.NONE;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		if(defaultCursor == null) {
			defaultCursor = baseChart.getDisplay().getSystemCursor(SWT.CURSOR_ARROW);
		}
		//
		if(tooltip == null) {
			tooltip = new ToolTip(baseChart.getShell(), SWT.NONE);
		}
		//
		String selectedSeriesId = baseChart.getSelectedseriesId(event);
		if(selectedSeriesId.equals("")) {
			baseChart.setCursor(defaultCursor);
			tooltip.setVisible(false);
		} else {
			baseChart.setCursor(baseChart.getDisplay().getSystemCursor(SWT.CURSOR_HAND));
			if(baseChart.getChartSettings().isEnableTooltips()) {
				tooltip.setMessage(selectedSeriesId);
				tooltip.setVisible(true);
				tooltip.setAutoHide(false);
			}
		}
	}
}
