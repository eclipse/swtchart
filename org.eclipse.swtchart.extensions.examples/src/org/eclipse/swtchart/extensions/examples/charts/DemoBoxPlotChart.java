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
package org.eclipse.swtchart.extensions.examples.charts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.RangeRestriction.ExtendType;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.scattercharts.BoxPlotChart;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesData;

public class DemoBoxPlotChart {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Box Plot Chart");
		shell.setSize(1024, 768);
		shell.setLayout(new FillLayout());
		//
		BoxPlotChart boxPlotChart = new BoxPlotChart(shell, SWT.NONE);
		shell.open();
		/*
		 * Settings
		 */
		IChartSettings chartSettings = boxPlotChart.getChartSettings();
		chartSettings.setBufferSelection(true);
		chartSettings.setHorizontalSliderVisible(false);
		chartSettings.setVerticalSliderVisible(false);
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setExtendTypeX(ExtendType.ABSOLUTE);
		rangeRestriction.setExtendMinX(0.5d);
		rangeRestriction.setExtendMaxX(0.5d);
		rangeRestriction.setExtendTypeY(ExtendType.RELATIVE);
		rangeRestriction.setExtendMinY(0.05d);
		rangeRestriction.setExtendMaxY(0.05d);
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
//		primaryAxisSettingsX.setVisible(false);
		primaryAxisSettingsX.setGridLineStyle(LineStyle.NONE);
		primaryAxisSettingsX.setEnableCategory(true);
		primaryAxisSettingsX.setCategorySeries(new String[]{"Trace 1", "Trace 2"});;
		boxPlotChart.applySettings(chartSettings);
		/*
		 * Data
		 */
		Color colorBlue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		Color colorRed = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		//
		List<ISeriesData> boxPlotSeriesList = SeriesConverter.getSeriesBoxPlot(SeriesConverter.BOXPLOT_SERIES_1);
		List<IScatterSeriesData> scatterSeriesDataList = new ArrayList<IScatterSeriesData>();
		for(int i = 0; i < boxPlotSeriesList.size(); i++) {
			ISeriesData seriesData = boxPlotSeriesList.get(i);
			Color color = (i % 2 == 0) ? colorBlue : colorRed;
			IScatterSeriesData scatterSeriesData = new ScatterSeriesData(seriesData);
			IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getSettings();
			scatterSeriesSettings.setSymbolColor(color);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.CIRCLE);
			scatterSeriesSettings.setSymbolSize(4);
			scatterSeriesDataList.add(scatterSeriesData);
		}
		boxPlotChart.addSeriesData(scatterSeriesDataList);
		//
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		//
		display.dispose();
	}
}