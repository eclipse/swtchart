/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;

public class SelectHideSeriesEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	private int hideMask = SWT.MOD3;

	@Override
	public int getEvent() {

		return IMouseSupport.EVENT_MOUSE_DOUBLE_CLICK;
	}

	@Override
	public int getButton() {

		return IMouseSupport.MOUSE_BUTTON_LEFT;
	}

	@Override
	public int getStateMask() {

		return SWT.MOD1;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		if((event.stateMask & hideMask) == hideMask) {
			/*
			 * Hide
			 */
			String selectedSeriesId = baseChart.getSelectedseriesId(event);
			if(selectedSeriesId.equals("")) { //$NON-NLS-1$
				showClickbindingHelp(baseChart, "Unhide", "Display all series again.");
				baseChart.resetSeriesSettings();
			} else {
				showClickbindingHelp(baseChart, "Hide", "Hide the selected series.");
				baseChart.hideSeries(selectedSeriesId);
				baseChart.redraw();
			}
		} else {
			/*
			 * Select
			 */
			String selectedSeriesId = baseChart.getSelectedseriesId(event);
			if(selectedSeriesId.equals("")) { //$NON-NLS-1$
				showClickbindingHelp(baseChart, "Unselect", "Deselect a series.");
				baseChart.resetSeriesSettings();
			} else {
				showClickbindingHelp(baseChart, "Select", "Select series.");
				baseChart.selectSeries(selectedSeriesId);
				baseChart.redraw();
			}
		}
	}
}
