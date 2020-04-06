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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.ISeries;

public class SeriesComparator extends ViewerComparator {

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
		if(e1 instanceof ISeries && e2 instanceof ISeries) {
			ISeries<?> series1 = (ISeries<?>)e1;
			Color color1 = SeriesLabelProvider.getColor(series1);
			ISeries<?> series2 = (ISeries<?>)e2;
			Color color2 = SeriesLabelProvider.getColor(series2);
			//
			switch(propertyIndex) {
				case 0:
					sortOrder = Boolean.compare(series1.isVisible(), series2.isVisible());
					break;
				case 1:
					sortOrder = Boolean.compare(series1.isVisibleInLegend(), series2.isVisibleInLegend());
					break;
				case 2:
					sortOrder = (color1 != null && color2 != null) ? color1.toString().compareTo(color2.toString()) : 0;
					break;
				case 3:
					sortOrder = series1.getDescription().compareTo(series2.getDescription());
					break;
				case 4:
					sortOrder = series1.getId().compareTo(series2.getId());
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
