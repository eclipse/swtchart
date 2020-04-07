/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
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

import java.util.Map;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;

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

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof Map.Entry && e2 instanceof Map.Entry) {
			Map.Entry<String, ISeriesSettings> entry1 = (Map.Entry<String, ISeriesSettings>)e1;
			Map.Entry<String, ISeriesSettings> entry2 = (Map.Entry<String, ISeriesSettings>)e2;
			//
			switch(propertyIndex) {
				case 0:
					sortOrder = entry1.getKey().compareTo(entry2.getKey());
					break;
				case 1:
					sortOrder = entry1.getValue().getDescription().compareTo(entry2.getValue().getDescription());
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
