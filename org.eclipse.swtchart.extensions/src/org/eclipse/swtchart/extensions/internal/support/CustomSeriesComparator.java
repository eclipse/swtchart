/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swtchart.extensions.model.ICustomSeries;

public class CustomSeriesComparator extends ViewerComparator {

	private static final int DESCENDING = 1;
	//
	private int propertyIndex = 0;
	private int direction = DESCENDING;

	public int getDirection() {

		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(int column) {

		if(column == this.propertyIndex) {
			direction = 1 - direction;
		} else {
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof ICustomSeries customSeries1 && e2 instanceof ICustomSeries customSeries2) {
			//
			switch(propertyIndex) {
				case 0:
					sortOrder = customSeries1.getId().compareTo(customSeries2.getId());
					break;
				case 1:
					sortOrder = Boolean.compare(customSeries1.isDraw(), customSeries2.isDraw());
					break;
				case 2:
					sortOrder = customSeries1.getLabel().compareTo(customSeries2.getLabel());
					break;
				case 3:
					sortOrder = customSeries1.getDescription().compareTo(customSeries2.getDescription());
					break;
				default:
					sortOrder = 0;
					break;
			}
		}
		//
		if(direction == DESCENDING) {
			sortOrder = -sortOrder;
		}
		//
		return sortOrder;
	}
}