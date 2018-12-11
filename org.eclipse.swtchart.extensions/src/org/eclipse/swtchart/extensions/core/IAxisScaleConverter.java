/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

public interface IAxisScaleConverter {

	/**
	 * May be null if not set correctly.
	 * 
	 * @return {@link IChartDataCoordinates}
	 */
	IChartDataCoordinates getChartDataCoordinates();

	/**
	 * The data coordinates are set by the base chart.
	 * There is no need to set them manually.
	 * 
	 * @param chartDataCoordinates
	 */
	void setChartDataCoordinates(IChartDataCoordinates chartDataCoordinates);

	/**
	 * Converts the primary value to the secondary axis value.
	 * E.g.: milliseconds to minutes
	 * 
	 * @param primaryValue
	 * @return double
	 */
	double convertToSecondaryUnit(double primaryValue);

	/**
	 * Converts the secondary axis value to the primary value.
	 * E.g.: minutes to milliseconds
	 * 
	 * @param secondaryValue
	 * @return double
	 */
	double convertToPrimaryUnit(double secondaryValue);
}
