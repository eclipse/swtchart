/*******************************************************************************
 * Copyright (c) 2020, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.internal.marker;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.SeriesLabelProvider;
import org.eclipse.swtchart.extensions.marker.AbstractBaseChartPaintListener;
import org.eclipse.swtchart.extensions.marker.IBaseChartPaintListener;

public class EmbeddedLegend extends AbstractBaseChartPaintListener implements IBaseChartPaintListener {

	private int x = 0;
	private int y = 0;

	public EmbeddedLegend(BaseChart baseChart) {

		super(baseChart);
	}

	public int getX() {

		return x;
	}

	public void setX(int x) {

		this.x = x;
	}

	public int getY() {

		return y;
	}

	public void setY(int y) {

		this.y = y;
	}

	@Override
	public void paintControl(PaintEvent e) {

		if(isDraw()) {
			/*
			 * Defaults
			 */
			GC gc = e.gc;
			int lineStyle = gc.getLineStyle();
			Color colorForeground = gc.getForeground();
			//
			BaseChart baseChart = getBaseChart();
			ISeriesSet seriesSet = baseChart.getSeriesSet();
			int x0 = x;
			int y0 = y;
			//
			for(ISeries<?> series : seriesSet.getSeries()) {
				if(series.isVisible()) {
					if(series.isVisibleInLegend()) {
						ISeriesSettings seriesSettings = baseChart.getSeriesSettings(series.getId());
						String description = seriesSettings.getDescription();
						Color color = SeriesLabelProvider.getColor(seriesSettings);
						if(color != null) {
							gc.setForeground(color);
							gc.drawText(description, x0, y0);
							y0 += 20;
						}
					}
				}
			}
			/*
			 * Reset
			 */
			gc.setLineStyle(lineStyle);
			gc.setForeground(colorForeground);
		}
	}
}