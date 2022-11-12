/*******************************************************************************
 * Copyright (c) 2022 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.marker;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.extensions.core.BaseChart;

public class BoxPlotMarker extends AbstractBaseChartPaintListener implements IBaseChartPaintListener {

	public BoxPlotMarker(BaseChart baseChart) {

		super(baseChart);
	}

	@SuppressWarnings("unused")
	@Override
	public void paintControl(PaintEvent e) {

		BaseChart baseChart = getBaseChart();
		ISeriesSet seriesSet = baseChart.getSeriesSet();
		for(ISeries<?> series : seriesSet.getSeries()) {
			/*
			 * TODO - Calculate statistics and draw the box
			 * for each series.
			 */
			// series.getXErrorBar();
			// series.getXSeries();
			// series.getYSeries();
			// e.gc.draw...
		}
	}
}