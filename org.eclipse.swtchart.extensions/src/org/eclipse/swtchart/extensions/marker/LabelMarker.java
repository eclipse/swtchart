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
 * Christoph Läubrich - adjust to new PlotArea API, save and restore transform
 *******************************************************************************/
package org.eclipse.swtchart.extensions.marker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.BaseChart;

public class LabelMarker extends AbstractBaseChartPaintListener implements IBaseChartPaintListener {

	private Map<Integer, String> labels = new HashMap<Integer, String>();
	private int indexSeries = -1;
	private ISeries<?> serie;
	private int orientation;

	public LabelMarker(BaseChart baseChart) {
		super(baseChart);
	}

	public void setLabels(List<String> labels, int indexSeries, int orientation) {

		Map<Integer, String> labelsMap = new HashMap<Integer, String>();
		int index = 0;
		for(String label : labels) {
			labelsMap.put(index++, label);
		}
		setLabels(labelsMap, indexSeries, orientation);
	}

	public void setLabels(Map<Integer, String> labels, int indexSeries, int orientation) {

		setSeriesIndex(indexSeries);
		setLabels(labels, orientation);
	}

	public void setLabels(Map<Integer, String> labels, int orientation) {

		this.orientation = orientation;
		this.labels = (labels != null) ? labels : new HashMap<Integer, String>();
	}

	public void setSeriesIndex(int indexSeries) {

		this.indexSeries = indexSeries;
	}

	public void setSeries(ISeries serie) {

		this.serie = serie;
	}

	@Override
	protected void finalize() throws Throwable {

		super.finalize();
	}

	public void clear() {

		labels.clear();
	}

	@Override
	public void paintControl(PaintEvent e) {

		ISeries<?> serie = getSeries();
		if(serie == null) {
			return;
		}
		//
		BaseChart baseChart = getBaseChart();
		IPlotArea plotArea = baseChart.getPlotArea();
		Rectangle rectangle;
		if(plotArea instanceof Scrollable) {
			rectangle = ((Scrollable)plotArea).getClientArea();
		} else {
			rectangle = plotArea.getBounds();
		}
		int size = serie.getXSeries().length;
		Transform oldTransform = new Transform(e.gc.getDevice());
		Transform textTransform;
		if(orientation == SWT.VERTICAL) {
			textTransform = new Transform(e.gc.getDevice());
			e.gc.getTransform(textTransform);
			textTransform.rotate(-90);
		} else {
			textTransform = null;
		}
		try {
			for(int index : labels.keySet()) {
				if(index < size) {
					/*
					 * Draw the label if the index is within the
					 * range of the double array.
					 */
					String label = labels.get(index);
					if(label == null || label.isEmpty()) {
						continue;
					}
					Point point = serie.getPixelCoordinates(index);
					//
					if(point.x > 0 && rectangle.contains(point)) {
						/*
						 * Calculate x and y
						 */
						int x;
						int y;
						Point labelSize = e.gc.textExtent(label);
						GC gc = e.gc;
						if(textTransform != null) {
							gc.setTransform(textTransform);
							x = -labelSize.x - (point.y - labelSize.x - 15);
							y = point.x - (labelSize.y / 2);
							gc.setTransform(oldTransform);
						} else {
							x = point.x - labelSize.x / 2;
							y = point.y - labelSize.y - 15;
						}
						gc.drawText(label, x, y, true);
					}
				}
			}
		} finally {
			oldTransform.dispose();
			if(textTransform != null) {
				textTransform.dispose();
			}
		}
	}

	protected ISeries getSeries() {

		if(serie != null) {
			return serie;
		}
		ISeries[] series = getBaseChart().getSeriesSet().getSeries();
		if(indexSeries >= 0 && indexSeries < series.length) {
			return series[indexSeries];
		}
		return null;
	}
}
