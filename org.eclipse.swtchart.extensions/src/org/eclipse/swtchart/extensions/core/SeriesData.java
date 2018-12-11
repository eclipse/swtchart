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

public class SeriesData implements ISeriesData {

	private double[] xSeries;
	private double[] ySeries;
	private String id;

	/**
	 * Sets the series.
	 * The xSeries is created automatically.
	 * It is equidistant, contains integer values and starts with 1.
	 * 
	 * @param ySeries
	 * @param id
	 */
	public SeriesData(double[] ySeries, String id) {
		this(ySeries, 1, id);
	}

	/**
	 * Sets the series.
	 * The xSeries is created automatically.
	 * It is equidistant, contains integer values and starts with the given value.
	 * 
	 * @param ySeries
	 * @param id
	 */
	public SeriesData(double[] ySeries, int xStart, String id) {
		assert (ySeries != null);
		assert (id != null);
		//
		xSeries = new double[ySeries.length];
		this.ySeries = ySeries;
		this.id = id;
		//
		for(int i = 0; i < ySeries.length; i++) {
			xSeries[i] = xStart++;
		}
	}

	/**
	 * Sets the series.
	 * 
	 * @param xSeries
	 * @param ySeries
	 * @param id
	 */
	public SeriesData(double[] xSeries, double[] ySeries, String id) {
		assert (xSeries != null);
		assert (ySeries != null);
		assert (id != null);
		//
		this.xSeries = xSeries;
		this.ySeries = ySeries;
		this.id = id;
	}

	@Override
	public double[] getXSeries() {

		return xSeries;
	}

	@Override
	public double[] getYSeries() {

		return ySeries;
	}

	@Override
	public String getId() {

		return id;
	}
}
