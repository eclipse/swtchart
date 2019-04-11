/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.internal.marker;

import java.text.DecimalFormat;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisSettings;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.marker.AbstractPositionPaintListener;
import org.eclipse.swtchart.extensions.marker.IPositionPaintListener;

public class LegendMarker extends AbstractPositionPaintListener implements IPositionPaintListener {

	private StringBuilder stringBuilder;
	private String[] axisLabelsX;
	private DecimalFormat decimalFormatX;
	private String[] axisLabelsY;
	private DecimalFormat decimalFormatY;

	public LegendMarker(BaseChart baseChart) {
		super(baseChart);
		//
		stringBuilder = new StringBuilder();
		axisLabelsX = baseChart.getAxisLabels(IExtendedChart.X_AXIS);
		decimalFormatX = baseChart.getDecimalFormat(IExtendedChart.X_AXIS, BaseChart.ID_PRIMARY_X_AXIS);
		axisLabelsY = baseChart.getAxisLabels(IExtendedChart.Y_AXIS);
		decimalFormatY = baseChart.getDecimalFormat(IExtendedChart.Y_AXIS, BaseChart.ID_PRIMARY_Y_AXIS);
	}

	@Override
	public void paintControl(PaintEvent e) {

		if(isDraw()) {
			stringBuilder.delete(0, stringBuilder.length());
			e.gc.setForeground(getForegroundColor());
			//
			BaseChart baseChart = getBaseChart();
			double primaryValueX = baseChart.getSelectedPrimaryAxisValue(getX(), IExtendedChart.X_AXIS);
			double primaryValueY = baseChart.getSelectedPrimaryAxisValue(getY(), IExtendedChart.Y_AXIS);
			//
			drawXAxes(primaryValueX);
			drawYAxes(primaryValueY);
			e.gc.drawText(stringBuilder.toString(), 10, 10);
		}
	}

	private void drawXAxes(double primaryValueX) {

		/*
		 * X Axes
		 */
		BaseChart baseChart = getBaseChart();
		IAxisSettings axisSettingsX = baseChart.getXAxisSettings(BaseChart.ID_PRIMARY_X_AXIS);
		if(axisSettingsX != null && axisSettingsX.isVisible()) {
			stringBuilder.append(axisLabelsX[BaseChart.ID_PRIMARY_X_AXIS]);
			stringBuilder.append(": ");
			stringBuilder.append(decimalFormatX.format(primaryValueX));
		}
		//
		for(int id : baseChart.getAxisSet().getXAxisIds()) {
			if(id != BaseChart.ID_PRIMARY_X_AXIS) {
				IAxisSettings axisSettings = baseChart.getXAxisSettings(id);
				if(axisSettings != null) {
					IAxisScaleConverter axisScaleConverter = baseChart.getAxisScaleConverter(IExtendedChart.X_AXIS, id);
					if(axisSettings.isVisible() && axisScaleConverter != null) {
						if(stringBuilder.length() > 0) {
							stringBuilder.append("\n");
						}
						stringBuilder.append(axisLabelsX[id]);
						stringBuilder.append(": ");
						stringBuilder.append(axisSettings.getDecimalFormat().format(axisScaleConverter.convertToSecondaryUnit(primaryValueX)));
					}
				}
			}
		}
	}

	private void drawYAxes(double primaryValueY) {

		/*
		 * Y Axes
		 */
		BaseChart baseChart = getBaseChart();
		IAxisSettings axisSettingsY = baseChart.getYAxisSettings(BaseChart.ID_PRIMARY_Y_AXIS);
		if(axisSettingsY != null && axisSettingsY.isVisible()) {
			if(stringBuilder.length() > 0) {
				stringBuilder.append("\n");
			}
			stringBuilder.append(axisLabelsY[BaseChart.ID_PRIMARY_Y_AXIS]);
			stringBuilder.append(": ");
			stringBuilder.append(decimalFormatY.format(primaryValueY));
		}
		//
		for(int id : baseChart.getAxisSet().getYAxisIds()) {
			if(id != BaseChart.ID_PRIMARY_Y_AXIS) {
				IAxisSettings axisSettings = baseChart.getYAxisSettings(id);
				if(axisSettings != null) {
					IAxisScaleConverter axisScaleConverter = baseChart.getAxisScaleConverter(IExtendedChart.Y_AXIS, id);
					if(axisSettings.isVisible() && axisScaleConverter != null) {
						if(stringBuilder.length() > 0) {
							stringBuilder.append("\n");
						}
						stringBuilder.append(axisLabelsY[id]);
						stringBuilder.append(": ");
						stringBuilder.append(axisSettings.getDecimalFormat().format(axisScaleConverter.convertToSecondaryUnit(primaryValueY)));
					}
				}
			}
		}
	}
}
