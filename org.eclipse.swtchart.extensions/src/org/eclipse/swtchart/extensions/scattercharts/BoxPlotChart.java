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
 * yoshitaka - support for axis categories
 *******************************************************************************/
package org.eclipse.swtchart.extensions.scattercharts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.extensions.marker.BoxPlotMarker;

public class BoxPlotChart extends ScatterChart {

	private Map<String, ILineSeries.PlotSymbolType> symbolTypes = new HashMap<>();

	public BoxPlotChart() {

		super();
		initialize();
	}

	public BoxPlotChart(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	private void initialize() {

		IPlotArea plotArea = getBaseChart().getPlotArea();
		BoxPlotMarker boxPlotMarker = new BoxPlotMarker(getBaseChart(), this);
		plotArea.addCustomPaintListener(boxPlotMarker);
	}

	@Override
	public void addSeriesData(List<IScatterSeriesData> scatterSeriesDataList) {

		setSymbolTypeToNone(scatterSeriesDataList);
		super.addSeriesData(scatterSeriesDataList);
	}

	private void setSymbolTypeToNone(List<IScatterSeriesData> scatterSeriesDataList) {

		for(IScatterSeriesData scatterSeriesData : scatterSeriesDataList) {
			IScatterSeriesSettings settings = scatterSeriesData.getSettings();
			symbolTypes.put(scatterSeriesData.getSeriesData().getId(), settings.getSymbolType());
			settings.setSymbolType(PlotSymbolType.NONE);
		}
	}

	public PlotSymbolType getPlotSymbolType(String seriesId) {

		return symbolTypes.get(seriesId);
	}
}