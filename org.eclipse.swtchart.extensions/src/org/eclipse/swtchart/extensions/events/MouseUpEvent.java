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
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;

public class MouseUpEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	@Override
	public int getEvent() {

		return IMouseSupport.EVENT_MOUSE_UP;
	}

	@Override
	public int getButton() {

		return IMouseSupport.MOUSE_BUTTON_LEFT;
	}

	@Override
	public int getStateMask() {

		return SWT.BUTTON1;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		/*
		 * Disable the buffered status.
		 */
		if(baseChart.getChartSettings().isBufferSelection()) {
			baseChart.suspendUpdate(true);
			baseChart.getPlotArea().setBackgroundImage(null);
			ISeriesSet set = baseChart.getSeriesSet();
			ISeries<?>[] series = set.getSeries();
			for(ISeries<?> serie : series) {
				if(serie.isVisibleBuffered()) {
					baseChart.showSeries(serie.getId());
					serie.setVisibleBuffered(false);
				}
			}
			baseChart.suspendUpdate(false);
		}
		//
		long deltaTime = System.currentTimeMillis() - baseChart.getClickStartTime();
		if(deltaTime >= BaseChart.DELTA_CLICK_TIME) {
			baseChart.handleUserSelection(event);
		}
	}
}
