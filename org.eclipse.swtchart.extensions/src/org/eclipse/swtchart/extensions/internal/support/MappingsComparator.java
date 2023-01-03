/*******************************************************************************
 * Copyright (c) 2020, 2023 Lablicate GmbH.
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
import org.eclipse.swtchart.extensions.core.MappedSeriesSettings;

public class MappingsComparator extends ViewerComparator {

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
		if(e1 instanceof MappedSeriesSettings && e2 instanceof MappedSeriesSettings) {
			MappedSeriesSettings mappedSeriesSettings1 = (MappedSeriesSettings)e1;
			MappedSeriesSettings mappedSeriesSettings2 = (MappedSeriesSettings)e2;
			//
			switch(propertyIndex) {
				case 0:
					sortOrder = mappedSeriesSettings1.getMappingsType().compareTo(mappedSeriesSettings2.getMappingsType());
					break;
				case 1:
					sortOrder = mappedSeriesSettings1.getIdentifier().compareTo(mappedSeriesSettings2.getIdentifier());
					break;
				case 2:
					sortOrder = mappedSeriesSettings1.getDescription().compareTo(mappedSeriesSettings2.getDescription());
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
		return sortOrder;
	}
}