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
package org.eclipse.swtchart.testmodel;

import java.util.Iterator;
import java.util.stream.IntStream;

import org.eclipse.swtchart.model.CartesianSeriesModel;
import org.eclipse.swtchart.model.IndexedSeriesModel;

public final class SineWave implements CartesianSeriesModel<Integer>, IndexedSeriesModel<Integer> {

	private static final int MIN_X = -1800;
	private static final int MAX_X = 1800;

	@Override
	public Iterator<Integer> iterator() {

		return IntStream.range(MIN_X, MAX_X).iterator();
	}

	@Override
	public Number getX(Integer data) {

		return data;
	}

	@Override
	public Number getY(Integer data) {

		return (Math.sin((data.doubleValue() / 10) * (Math.PI / 180d)) + 1.0) / 2;
	}

	@Override
	public Number getMaxX() {

		return MAX_X;
	}

	@Override
	public Number getMinX() {

		return MIN_X;
	}

	@Override
	public int size() {

		return MAX_X;
	}

	@Override
	public Integer itemAt(int index) throws IndexOutOfBoundsException {

		return index;
	}
}