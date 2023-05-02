/*******************************************************************************
 * Copyright (c) 2020, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta Orignal API and implementation
 * Philip Wenig - series settings mappings
 *******************************************************************************/
package org.eclipse.swtchart.extensions.piecharts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ChartType;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.events.CircularMouseDownEvent;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;
import org.eclipse.swtchart.extensions.events.MouseDownEvent;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;
import org.eclipse.swtchart.extensions.menu.toggle.ToggleLabelTooltipsHandler;

public class PieChart extends ScrollableChart {

	public PieChart() {

		super();
		setChartType(ChartType.PIE);
		setData("org.eclipse.e4.ui.css.CssClassName", "BarChart");
	}

	public PieChart(Composite parent, int style) {

		super(parent, style);
		setChartType(ChartType.PIE);
		setData("org.eclipse.e4.ui.css.CssClassName", "BarChart");
	}

	public void addSeriesData(ICircularSeriesData seriesData) {

		/*
		 * Suspend the update when adding new data to improve the performance.
		 */
		if(seriesData != null && seriesData.getRootNode() != null) {
			/*
			 * Prepare
			 */
			BaseChart baseChart = getBaseChart();
			baseChart.suspendUpdate(true);
			/*
			 * Get the series data and apply the settings.
			 */
			try {
				/*
				 * Chart Settings
				 */
				IChartSettings chartSettings = getChartSettings();
				chartSettings.setHorizontalSliderVisible(false);
				chartSettings.setVerticalSliderVisible(false);
				chartSettings.getRangeRestriction().setZeroX(false);
				chartSettings.getRangeRestriction().setZeroY(false);
				chartSettings.setLegendVisible(true);
				chartSettings.setShowLegendMarker(true);
				adjustPrimaryAxisX(chartSettings, seriesData);
				adjustPrimaryAxisY(chartSettings, seriesData);
				adjustEventProcessors(chartSettings);
				adjustMenuEntries(chartSettings);
				applySettings(chartSettings);
				/*
				 * Series Settings
				 */
				ICircularSeriesSettings seriesSettings = seriesData.getSettings();
				ICircularSeries<?> circularSeries = createCircularSeries(seriesData, seriesSettings);
				baseChart.applySeriesSettings(circularSeries, seriesSettings);
			} catch(SeriesException e) {
				//
			}
			/*
			 * Finish
			 */
			baseChart.suspendUpdate(false);
			adjustRange(true);
			baseChart.redraw();
		}
	}

	private void adjustPrimaryAxisX(IChartSettings chartSettings, ICircularSeriesData seriesData) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle(seriesData.getNodeClass());
		primaryAxisSettingsX.setVisible(false);
	}

	private void adjustPrimaryAxisY(IChartSettings chartSettings, ICircularSeriesData seriesData) {

		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle(seriesData.getValueClass());
		primaryAxisSettingsY.setVisible(false);
	}

	private void adjustEventProcessors(IChartSettings chartSettings) {

		/*
		 * Handle the slice selection.
		 */
		IHandledEventProcessor handledEventProcessor = null;
		for(IHandledEventProcessor processor : chartSettings.getHandledEventProcessors()) {
			if(processor instanceof MouseDownEvent) {
				handledEventProcessor = processor;
				break;
			}
		}
		//
		if(handledEventProcessor != null) {
			chartSettings.removeHandledEventProcessor(handledEventProcessor);
		}
		//
		IHandledEventProcessor circularHandledEventProcessor = new CircularMouseDownEvent(this);
		chartSettings.addHandledEventProcessor(circularHandledEventProcessor);
	}

	private void adjustMenuEntries(IChartSettings chartSettings) {

		/*
		 * The series tooltips can't be used yet, as the data is handled in a different way.
		 */
		chartSettings.setEnableTooltips(false);
		chartSettings.removeMenuEntry(chartSettings.getChartMenuEntryByClass(ToggleLabelTooltipsHandler.class));
	}
}
