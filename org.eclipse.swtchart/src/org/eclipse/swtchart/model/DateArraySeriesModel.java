/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.model;

import java.util.Date;
import java.util.Iterator;
import java.util.stream.IntStream;

public class DateArraySeriesModel implements IndexedSeriesModel<Integer>, CartesianSeriesModel<Integer> {

	private Date[] dates;

	public DateArraySeriesModel(Date[] dates) {
		this.dates = dates;
	}

	@Override
	public Iterator<Integer> iterator() {

		return IntStream.range(0, dates.length).iterator();
	}

	@Override
	public Number getX(Integer data) {

		int value = data.intValue();
		if(value >= 0 && value < dates.length) {
			return value;
		}
		return null;
	}

	@Override
	public Number getY(Integer data) {

		int value = data.intValue();
		if(value >= 0 && value < dates.length) {
			return dates[value].getTime();
		}
		return null;
	}

	@Override
	public int size() {

		return 0;
	}

	@Override
	public Integer itemAt(int index) throws IndexOutOfBoundsException {

		return index;
	}
}
