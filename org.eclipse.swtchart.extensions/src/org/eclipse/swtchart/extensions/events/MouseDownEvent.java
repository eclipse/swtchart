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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.internal.support.SeriesMapper;

public class MouseDownEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	private static Set<String> visibleSeriesId;

	@Override
	public int getEvent() {

		return IMouseSupport.EVENT_MOUSE_DOWN;
	}

	@Override
	public int getButton() {

		return IMouseSupport.MOUSE_BUTTON_LEFT;
	}

	@Override
	public int getStateMask() {

		return SWT.NONE;
	}

	public static Set<String> getVisibleSeriesId() {

		return Collections.unmodifiableSet(visibleSeriesId);
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		baseChart.getUserSelection().setStartCoordinate(event.x, event.y);
		baseChart.setClickStartTime(System.currentTimeMillis());
		/*
		 * Disable the buffered status.
		 */
		SeriesMapper mapper = new SeriesMapper(baseChart);
		visibleSeriesId = new HashSet<String>();
		mapper.mapSettings(baseChart.getSeriesSet().getSeries());
		Set<Entry<String, ISeriesSettings>> mapping = SeriesMapper.getMappings();
		for(Entry<String, ISeriesSettings> entry : mapping) {
			visibleSeriesId.add(entry.getKey());
		}
		if(baseChart.getChartSettings().isBufferSelection()) {
			baseChart.suspendUpdate(true);
			Image image = new Image(Display.getDefault(), baseChart.getPlotArea().getImageData());
			ISeriesSet set = baseChart.getSeriesSet();
			ISeries<?>[] series = set.getSeries();
			for(ISeries<?> serie : series) {
				baseChart.hideSeries(serie.getId());
			}
			baseChart.getPlotArea().setBackgroundImage(image);
			baseChart.suspendUpdate(false);
			baseChart.redraw();
		}
	}
}
