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
package org.eclipse.swtchart.model;

import java.util.Iterator;
import java.util.stream.IntStream;

@SuppressWarnings("rawtypes")
public class StringArraySeriesModel implements CartesianSeriesModel {

	private final String[] labels;
	private final double[] values;

	public StringArraySeriesModel(String[] labels, double[] values) {
		if(labels.length != values.length) {
			throw new IllegalArgumentException(Messages.getString(Messages.X_Y_LENGTH_DOESNT_MATCH));
		}
		this.labels = labels;
		this.values = values;
	}

	public String[] getLabels() {

		return labels;
	}

	public double[] getValues() {

		return values;
	}

	public String getLabel(Integer data) {

		int value = data.intValue();
		if(value >= 0 && value < labels.length) {
			return labels[value];
		} else {
			return null;
		}
	}

	@SuppressWarnings("null")
	public double getValue(Integer data) {

		int value = data.intValue();
		if(value >= 0 && value < labels.length) {
			return values[value];
		} else {
			return (Double)null;
		}
	}

	@Override
	public Iterator iterator() {

		return IntStream.range(0, labels.length).iterator();
	}

	@Override
	public Number getX(Object data) {

		return null;
	}

	@Override
	public Number getY(Object data) {

		return null;
	}
}
