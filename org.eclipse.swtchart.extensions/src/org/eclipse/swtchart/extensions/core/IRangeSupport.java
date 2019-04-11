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
package org.eclipse.swtchart.extensions.core;

import org.eclipse.swtchart.IAxis;

public interface IRangeSupport {

	RangeRestriction getRangeRestriction();

	void setRangeRestriction(RangeRestriction rangeRestriction);

	/**
	 * Sets the range, based on the start and stop coordinates of the composite.
	 * In this case, axis.getDataCoordinate is used to get the data coordinate.
	 * 
	 * @param axis
	 * @param xStart
	 * @param xStop
	 * @param adjustMinMax
	 */
	void setRange(IAxis axis, int xStart, int xStop, boolean adjustMinMax);

	/**
	 * Sets the range, based on the start and stop coordinates.
	 * It's only allowed to use the primary axes.
	 * 
	 * @param axis
	 * @param start
	 * @param stop
	 * @param adjustMinMax
	 */
	void setRange(IAxis axis, double start, double stop, boolean adjustMinMax);

	/**
	 * Adjust the axis to its allowed/constrained values.
	 * 
	 * @param axis
	 */
	void adjustMinMaxRange(IAxis axis);
}
