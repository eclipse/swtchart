/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.menu;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.dialogs.ChartRangeDialog;
import org.eclipse.swtchart.extensions.dialogs.ChartRangeValues;

public class SetRangeChartHandler extends AbstractChartMenuEntry implements IChartMenuEntry {

	@Override
	public String getCategory() {

		return IChartMenuCategories.STANDARD_OPERATION;
	}

	@Override
	public String getName() {

		return "Set Chart Range";
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		ChartRangeDialog chartRangeDialog = new ChartRangeDialog(shell, scrollableChart);
		chartRangeDialog.create();
		if(chartRangeDialog.open() == Window.OK) {
			try {
				/*
				 * Get the start and stop value.
				 */
				ChartRangeValues chartRangeValues = chartRangeDialog.getChartRangeValues();
				Range rangeX = getRange(IExtendedChart.X_AXIS, scrollableChart, chartRangeValues);
				Range rangeY = getRange(IExtendedChart.Y_AXIS, scrollableChart, chartRangeValues);
				/*
				 * Apply the range.
				 */
				if(rangeX != null && rangeY != null) {
					scrollableChart.setRange(IExtendedChart.X_AXIS, rangeX);
					scrollableChart.setRange(IExtendedChart.Y_AXIS, rangeY);
					fireUpdateRangeSelection(scrollableChart);
				}
			} catch(ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private void fireUpdateRangeSelection(ScrollableChart scrollableChart) {

		BaseChart baseChart = scrollableChart.getBaseChart();
		Event event = new Event();
		baseChart.fireUpdateCustomRangeSelectionHandlers(event);
	}

	private Range getRange(String axis, ScrollableChart scrollableChart, ChartRangeValues chartRangeValues) throws ParseException {

		/*
		 * Get the decimal format.
		 */
		BaseChart baseChart = scrollableChart.getBaseChart();
		DecimalFormat decimalFormat;
		int selectedAxis;
		//
		if(axis.equals(IExtendedChart.X_AXIS)) {
			selectedAxis = chartRangeValues.getAxisX();
			decimalFormat = baseChart.getDecimalFormat(IExtendedChart.X_AXIS, selectedAxis);
		} else {
			selectedAxis = chartRangeValues.getAxisY();
			decimalFormat = baseChart.getDecimalFormat(IExtendedChart.Y_AXIS, selectedAxis);
		}
		/*
		 * Get the start and stop value.
		 */
		double valueStart;
		double valueStop;
		//
		if(axis.equals(IExtendedChart.X_AXIS)) {
			valueStart = decimalFormat.parse(chartRangeValues.getStartX()).doubleValue();
			valueStop = decimalFormat.parse(chartRangeValues.getStopX()).doubleValue();
		} else {
			valueStart = decimalFormat.parse(chartRangeValues.getStartY()).doubleValue();
			valueStop = decimalFormat.parse(chartRangeValues.getStopY()).doubleValue();
		}
		/*
		 * Convert the range on demand.
		 */
		Range range = null;
		if(selectedAxis == 0) {
			range = new Range(valueStart, valueStop);
		} else {
			IAxisScaleConverter axisScaleConverter = baseChart.getAxisScaleConverter(axis, selectedAxis);
			if(axisScaleConverter != null) {
				valueStart = axisScaleConverter.convertToPrimaryUnit(valueStart);
				valueStop = axisScaleConverter.convertToPrimaryUnit(valueStop);
				range = new Range(valueStart, valueStop);
			}
		}
		//
		return range;
	}
}