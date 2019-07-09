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
 * Basic interface for {@link SeriesModel}s that allows access to an set of DataTypes items
 * 
 * @author Christoph Läubrich
 *
 * @param <SeriesTypes>
 */
public interface SeriesModel<T> extends Iterable<T> {
}
