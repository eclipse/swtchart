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
 *******************************************************************************/
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.IPlotArea;
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
		 * Disable to buffer the data.
		 */
		IPlotArea plotArea = baseChart.getPlotArea();
		if(plotArea.isBuffered()) {
			/*
			 * Dispose the background image.
			 */
			baseChart.suspendUpdate(true);
			plotArea.setBackgroundImage(null);
			Object object = plotArea.getControl().getData(IPlotArea.KEY_BUFFERED_BACKGROUND_IMAGE);
			if(object instanceof Image) {
				Image image = (Image)object;
				plotArea.getControl().setData(IPlotArea.KEY_BUFFERED_BACKGROUND_IMAGE, null);
				image.dispose();
			}
			plotArea.setBuffered(false);
			ISeriesSet set = baseChart.getSeriesSet();
			ISeries<?>[] series = set.getSeries();
			for(ISeries<?> serie : series) {
				if(serie.isVisibleBuffered()) {
					baseChart.showSeries(serie.getId());
					serie.setVisibleBuffered(false);
				}
			}
			baseChart.suspendUpdate(false);
			baseChart.redraw();
		}
		/*
		 * Apply the selection.
		 */
		if(isSingleClick(event)) {
			baseChart.setCursor(baseChart.getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
			long deltaTime = System.currentTimeMillis() - baseChart.getClickStartTime();
			if(deltaTime >= BaseChart.DELTA_CLICK_TIME) {
				baseChart.handleUserSelection(event);
			} else {
				baseChart.getUserSelection().reset();
			}
		}
	}
}