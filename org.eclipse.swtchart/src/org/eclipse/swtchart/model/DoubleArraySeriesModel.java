/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.model;

import java.util.Iterator;
import java.util.stream.IntStream;

/**
 * A series model that is based on plain arrays
 * 
 * @author Christoph Läubrich
 *
 */
public class DoubleArraySeriesModel implements IndexedSeriesModel<Integer>, CartesianSeriesModel<Integer> {

	private final double[] xdata;
	private final double[] ydata;
	private final double minX;
	private final double maxX;
	private boolean isXMonotoneIncreasing = true;
	private final double minY;
	private final double maxY;

	public DoubleArraySeriesModel(double[] xSeries, double[] ySeries) {
		if(xSeries.length != ySeries.length) {
			throw new IllegalArgumentException(Messages.getString(Messages.X_Y_LENGTH_DOESNT_MATCH));
		}
		this.xdata = xSeries;
		this.ydata = ySeries;
		if(xSeries.length > 0) {
			double minX = xSeries[0];
			double maxX = xSeries[0];
			for(int i = 1; i < xSeries.length; i++) {
				if(minX > xSeries[i]) {
					minX = xSeries[i];
				}
				if(maxX < xSeries[i]) {
					maxX = xSeries[i];
				}
				if(xSeries[i - 1] > xSeries[i]) {
					isXMonotoneIncreasing = false;
				}
			}
			this.minX = minX;
			this.maxX = maxX;
		} else {
			this.minX = 0;
			this.maxX = 0;
		}
		if(ySeries.length > 0) {
			// find the min and max value of y series
			double minY = ySeries[0];
			double maxY = ySeries[0];
			for(int i = 1; i < ySeries.length; i++) {
				if(minY > ySeries[i]) {
					minY = ySeries[i];
				}
				if(maxY < ySeries[i]) {
					maxY = ySeries[i];
				}
			}
			this.minY = minY;
			this.maxY = maxY;
		} else {
			this.minY = 0;
			this.maxY = 0;
		}
	}

	public boolean isXMonotoneIncreasing() {

		return isXMonotoneIncreasing;
	}

	@Override
	public Iterator<Integer> iterator() {

		return IntStream.range(0, xdata.length).iterator();
	}

	@Override
	public int size() {

		return xdata.length;
	}

	@Override
	public Integer itemAt(int index) throws IndexOutOfBoundsException {

		return index;
	}

	@Override
	public Number getX(Integer data) {

		int value = data.intValue();
		if(value >= 0 && value < xdata.length) {
			return xdata[value];
		} else {
			return null;
		}
	}

	@Override
	public Number getY(Integer data) {

		int value = data.intValue();
		if(value >= 0 && value < ydata.length) {
			return ydata[data.intValue()];
		} else {
			return null;
		}
	}

	@Override
	public Number getMaxX() {

		return maxX;
	}

	@Override
	public Number getMinX() {

		return minX;
	}

	@Override
	public Number getMaxY() {

		return maxY;
	}

	@Override
	public Number getMinY() {

		return minY;
	}
}
