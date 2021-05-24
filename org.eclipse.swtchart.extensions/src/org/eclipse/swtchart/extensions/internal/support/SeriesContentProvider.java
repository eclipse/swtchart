/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.internal.support;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swtchart.ISeriesSet;

public class SeriesContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object input) {

		if(input instanceof ISeriesSet) {
			return ((ISeriesSet)input).getSeries();
		} else {
			return null;
		}
	}
}
