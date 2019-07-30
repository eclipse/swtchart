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
import java.util.function.Supplier;

import org.eclipse.swtchart.model.CartesianSeriesModel;

public class BusinessObjectModel implements CartesianSeriesModel<BusinessObject> {

	private Supplier<Iterator<BusinessObject>> supplier;

	public BusinessObjectModel(Supplier<Iterator<BusinessObject>> supplier) {
		this.supplier = supplier;
	}

	@Override
	public Iterator<BusinessObject> iterator() {

		return supplier.get();
	}

	@Override
	public Number getX(BusinessObject data) {

		return data.getTimeOfBuying().getTime();
	}

	@Override
	public Number getY(BusinessObject data) {

		return data.getPrice();
	}
}
