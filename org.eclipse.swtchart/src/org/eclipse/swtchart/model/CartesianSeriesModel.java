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

import java.util.Comparator;
import java.util.stream.StreamSupport;

/**
 * Defines a datamodel that maps to the Cartesian data space
 * 
 * @author Christoph Läubrich
 *
 * @param <T>
 */
public interface CartesianSeriesModel<T> extends SeriesModel<T> {

	public static final Integer ZERO = 0;
	public static final Comparator<Number> DEFAULT_NUMBER_COMPARATOR = new Comparator<Number>() {

		@Override
		public int compare(Number o1, Number o2) {

			return Double.compare(o1.doubleValue(), o2.doubleValue());
		}
	};

	/**
	 * Extracts the X-Coordinate from the given data item
	 * 
	 * @param data
	 * @return the x coordinate or <code>null</code> if this item does not denote a valid x-datapoint (e.g. disabled item)
	 */
	Number getX(T data);

	/**
	 * Extracts the Y-Coordinate from the given data item
	 * 
	 * @param data
	 * @return the y-coordinate or <code>null</code> if this item does not denote a valid y-datapoint (e.g. disabled item)
	 */
	Number getY(T data);

	/**
	 * 
	 * @return the minimum x value, subclasses might provide more efficient implementation
	 */
	default Number getMinX() {

		return StreamSupport.stream(this.spliterator(), false).filter(v -> getX(v) != null).map(v -> getX(v)).min(DEFAULT_NUMBER_COMPARATOR).get();
	}

	/**
	 * 
	 * @return the minimum y value, subclasses might provide more efficient implementation
	 */
	default Number getMinY() {

		return StreamSupport.stream(this.spliterator(), false).filter(v -> getY(v) != null).map(v -> getY(v)).min(DEFAULT_NUMBER_COMPARATOR).get();
	}

	/**
	 * 
	 * @return the minimum z value, subclasses might provide more efficient implementation
	 */
	default Number getMinZ() {

		return StreamSupport.stream(this.spliterator(), false).filter(v -> getZ(v) != null).map(v -> getZ(v)).min(DEFAULT_NUMBER_COMPARATOR).get();
	}

	/**
	 * 
	 * @return the minimum x value, subclasses might provide more efficient implementation
	 */
	default Number getMaxX() {

		return StreamSupport.stream(this.spliterator(), false).filter(v -> getX(v) != null).map(v -> getX(v)).max(DEFAULT_NUMBER_COMPARATOR).get();
	}

	/**
	 * 
	 * @return the minimum y value, subclasses might provide more efficient implementation
	 */
	default Number getMaxY() {

		return StreamSupport.stream(this.spliterator(), false).filter(v -> getY(v) != null).map(v -> getY(v)).max(DEFAULT_NUMBER_COMPARATOR).get();
	}

	/**
	 * 
	 * @return the minimum z value, subclasses might provide more efficient implementation
	 */
	default Number getMaxZ() {

		return StreamSupport.stream(this.spliterator(), false).filter(v -> getZ(v) != null).map(v -> getZ(v)).max(DEFAULT_NUMBER_COMPARATOR).get();
	}

	/**
	 * Extracts the Z-Coordinate from the given data item
	 * 
	 * @param data
	 * @return the z-coordinate or <code>null</code> if this item does not denote a valid z-datapoint (e.g. disabled item), the default implementation returns the constant {@link #ZERO} if {@link #getX(Object)} return non null
	 */
	default Number getZ(T data) {

		if(getX(data) != null) {
			return ZERO;
		}
		return null;
	}
}
