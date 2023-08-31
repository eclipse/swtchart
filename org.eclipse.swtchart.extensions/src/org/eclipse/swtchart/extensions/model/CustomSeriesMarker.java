/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.model;

import java.util.List;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.marker.AbstractBaseChartPaintListener;
import org.eclipse.swtchart.extensions.marker.IBaseChartPaintListener;

public class CustomSeriesMarker extends AbstractBaseChartPaintListener implements IBaseChartPaintListener {

	public CustomSeriesMarker(BaseChart baseChart) {

		super(baseChart);
	}

	@Override
	public void paintControl(PaintEvent e) {

		List<ICustomSeries> customSeriesList = getBaseChart().getCustomSeries();
		for(ICustomSeries customSeries : customSeriesList) {
			if(customSeries.isDraw()) {
				// TODO
			}
		}
	}
}