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
package org.eclipse.swtchart.extensions.axisconverter;

import org.eclipse.swt.SWT;
import org.eclipse.swtchart.extensions.core.AbstractAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IChartDataCoordinates;

public class PercentageConverter extends AbstractAxisScaleConverter implements IAxisScaleConverter {

	private static final double REFERENCE = 100.0d;
	//
	private int orientation;
	private boolean isZeroBased;

	/**
	 * Select the orientation:
	 * X-Axis: SWT.HORIZONTAL
	 * Y-AXis: SWT.VERTICAL
	 */
	public PercentageConverter(int orientation, boolean isZeroBased) {
		this.orientation = orientation;
		this.isZeroBased = isZeroBased;
	}

	@Override
	public double convertToSecondaryUnit(double primaryValue) {

		IChartDataCoordinates chartDataCoordinates = getChartDataCoordinates();
		double convertedValue = 0;
		if(chartDataCoordinates != null) {
			/*
			 * Calculation
			 */
			double deltaRange = calculateDeltaRange(chartDataCoordinates);
			if(deltaRange != 0) {
				convertedValue = (REFERENCE / deltaRange) * primaryValue;
			}
		}
		return convertedValue;
	}

	@Override
	public double convertToPrimaryUnit(double secondaryValue) {

		IChartDataCoordinates chartDataCoordinates = getChartDataCoordinates();
		double convertedValue = 0;
		if(chartDataCoordinates != null) {
			/*
			 * Calculation
			 */
			double deltaRange = calculateDeltaRange(chartDataCoordinates);
			convertedValue = deltaRange * (secondaryValue / REFERENCE);
		}
		return convertedValue;
	}

	private double calculateDeltaRange(IChartDataCoordinates chartDataCoordinates) {

		double min;
		double max;
		//
		if(orientation == SWT.VERTICAL) {
			min = (isZeroBased) ? 0.0d : chartDataCoordinates.getMinY();
			max = chartDataCoordinates.getMaxY();
		} else {
			min = (isZeroBased) ? 0.0d : chartDataCoordinates.getMinX();
			max = chartDataCoordinates.getMaxX();
		}
		///
		return max - min;
	}
}
