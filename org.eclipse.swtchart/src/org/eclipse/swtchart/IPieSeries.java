/*******************************************************************************
 * Copyright (c) 2020 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart;

import org.eclipse.swt.graphics.Color;

/**
 * Contains methods to be implemented that are common to both PieSeries and MultiLevel Pie Series
 */
public interface IPieSeries {

	/**
	 * gets the label series
	 * 
	 * @return labels that will be visible
	 */
	public String[] getLabels();

	/**
	 * each element of this array is the color that the corresponding data
	 * are present with same index in labelSeries and valueSeries
	 * 
	 * @return color[]
	 */
	public Color[] getColors();

	/**
	 * sets the pie series.
	 * 
	 * @param labels
	 * @param values
	 */
	public void setSeries(String[] labels, double[] values);

	/**
	 * allows user to change color of given label to required color.
	 * This must be called after adding all the series, else it will be overridden.
	 * 
	 * @param label
	 * @param color
	 */
	public void setColor(String label, Color color);

	/**
	 * allows user to set multiple colors together. The colors are set corresponding to the
	 * the labels in the label series.
	 * This must be called after adding all the series, else it will be overridden.
	 * 
	 * @param colors
	 */
	public void setColor(Color[] colors);
}
