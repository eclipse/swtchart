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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.parts;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.customcharts.core.ChromatogramChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;
import org.eclipse.swtchart.extensions.marker.LabelMarker;

public class LineSeries_9_Part extends ChromatogramChart {

	private int indexSeries;

	@Inject
	public LineSeries_9_Part(Composite parent) {

		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//
		try {
			initialize();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {

		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = getChartSettings();
		chartSettings.setCreateMenu(true);
		applySettings(chartSettings);
		/*
		 * Create series.
		 */
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		//
		ISeriesData seriesData;
		ILineSeriesData lineSeriesData;
		ILineSeriesSettings lineSeriesSettings;
		/*
		 * Chromatogram [0]
		 */
		seriesData = SeriesConverter.getSeriesXY(SeriesConverter.LINE_SERIES_1);
		lineSeriesData = new LineSeriesData(seriesData);
		lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setEnableArea(true);
		ILineSeriesSettings lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
		lineSeriesSettingsHighlight.setLineWidth(2);
		lineSeriesDataList.add(lineSeriesData);
		/*
		 * Active Peaks [1]
		 */
		indexSeries = 1;
		seriesData = SeriesConverter.getSeriesXY(SeriesConverter.LINE_SERIES_1_ACTIVE_PEAKS);
		lineSeriesData = new LineSeriesData(seriesData);
		lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setEnableArea(false);
		lineSeriesSettings.setLineStyle(LineStyle.NONE);
		lineSeriesSettings.setSymbolType(PlotSymbolType.INVERTED_TRIANGLE);
		lineSeriesSettings.setSymbolSize(5);
		lineSeriesSettings.setLineColor(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		lineSeriesSettings.setSymbolColor(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		lineSeriesDataList.add(lineSeriesData);
		/*
		 * Set series.
		 */
		addSeriesData(lineSeriesDataList);
		/*
		 * Add the label marker.
		 */
		IPlotArea plotArea = getBaseChart().getPlotArea();
		LabelMarker labelMarker = new LabelMarker(getBaseChart());
		List<String> labels = new ArrayList<String>();
		labels.add("2-Methoxy-4-vinylphenol");
		labels.add("Ethanone, 1-(2-hydroxy-5-methylphenyl)-");
		labels.add("4-Hydroxy-2-methylacetophenone");
		labels.add("Ethanone, 1-(2-hydroxy-5-methylphenyl)-");
		labels.add("4-Hydroxy-3-methylacetophenone");
		labels.add("3-Methoxyacetophenone");
		labels.add("3-Methyl-4-isopropylphenol");
		labels.add("Phenol, 3,4-dimethoxy-");
		labels.add("2,4-Dimethoxyphenol");
		labels.add("3-Amino-2,6-dimethoxypyridine");
		labelMarker.setLabels(labels, indexSeries, SWT.HORIZONTAL);
		plotArea.addCustomPaintListener(labelMarker);
	}
}
