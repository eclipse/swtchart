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

/**
 * A series model that is accessible by an index
 * 
 * @author Christoph Läubrich
 *
 * @param <T>
 */
public interface IndexedSeriesModel<T> extends SeriesModel<T> {

	/**
	 * 
	 * @return the size of this {@link IndexedSeriesModel}
	 */
	int size();

	/**
	 * Get the item at the specified index
	 * 
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 *             if index < 0 or >= {@link #size()}
	 */
	T itemAt(int index) throws IndexOutOfBoundsException;
}
