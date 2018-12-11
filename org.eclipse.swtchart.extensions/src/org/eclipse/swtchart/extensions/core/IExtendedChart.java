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

import org.eclipse.swtchart.extensions.exceptions.SeriesException;
import org.eclipse.swtchart.ISeries;

public interface IExtendedChart {

	String X_AXIS = "X_AXIS";
	String Y_AXIS = "Y_AXIS";

	/**
	 * SeriesType.LINE or SeriesType.BAR are automatically set dependent which settings are used.
	 * 
	 * @param seriesData
	 * @param seriesSettings
	 * @return ISeries
	 * @throws SeriesException
	 */
	ISeries createSeries(ISeriesData seriesData, ISeriesSettings seriesSettings) throws SeriesException;

	/**
	 * Deletes the given series if it exists and
	 * recalculates the min/max values.
	 * 
	 * @param id
	 */
	void deleteSeries(String id);

	/**
	 * Append an existing series with the new arrays.
	 * 
	 * @param seriesData
	 */
	void appendSeries(ISeriesData seriesData);

	/**
	 * Sets the range, based on the start and stop coordinates.
	 * It's only possible to set the range for the primary axes as
	 * the range for secondary axes is calculated dynamically.
	 * 
	 * Use: IExtendedChart.X_AXIS or IExtendedChart.Y_AXIS.
	 * 
	 * @param axis
	 * @param start
	 * @param stop
	 * @param adjustMinMax
	 */
	void setRange(String axis, double start, double stop);

	/**
	 * Adjusts the range of all axes and validates the min/max ranges on demand.
	 * 
	 * @param adjustMinMax
	 */
	void adjustRange(boolean adjustMinMax);

	/**
	 * Adjusts all secondary x axes, based on range of the primary x axis.
	 */
	void adjustSecondaryXAxes();

	/**
	 * Adjusts all secondary y axes, based on range of the primary y axis.
	 */
	void adjustSecondaryYAxes();
}
