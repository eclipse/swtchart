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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.core.SeriesLabelProvider;
import org.eclipse.swtchart.extensions.core.SeriesListUI;
import org.eclipse.swtchart.extensions.core.SeriesMapper;

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
		if(e1 instanceof ISeries && e2 instanceof ISeries && viewer instanceof SeriesListUI) {
			SeriesListUI seriesListUI = (SeriesListUI)viewer;
			BaseChart baseChart = seriesListUI.getScrollableChart().getBaseChart();
			ISeries<?> series1 = (ISeries<?>)e1;
			ISeriesSettings seriesSettings1 = baseChart.getSeriesSettings(series1.getId());
			Color color1 = SeriesLabelProvider.getColor(seriesSettings1);
			ISeries<?> series2 = (ISeries<?>)e2;
			ISeriesSettings seriesSettings2 = baseChart.getSeriesSettings(series2.getId());
			Color color2 = SeriesLabelProvider.getColor(seriesSettings2);
			//
			switch(propertyIndex) {
				case 0:
					sortOrder = series1.getId().compareTo(series2.getId());
					break;
				case 1:
					ScrollableChart scrollableChart = seriesListUI.getScrollableChart();
					if(scrollableChart != null) {
						ISeriesSettings mapping1 = SeriesMapper.get(series1, scrollableChart.getBaseChart());
						ISeriesSettings mapping2 = SeriesMapper.get(series1, scrollableChart.getBaseChart());
						if(mapping1 != null && mapping2 == null) {
							sortOrder = -1;
						} else if(mapping1 == null && mapping2 != null) {
							sortOrder = 1;
						} else {
							sortOrder = 0;
						}
					}
					break;
				case 2:
					sortOrder = Boolean.compare(series1.isVisible(), series2.isVisible());
					break;
				case 3:
					sortOrder = Boolean.compare(series1.isVisibleInLegend(), series2.isVisibleInLegend());
					break;
				case 4:
					sortOrder = (color1 != null && color2 != null) ? color1.toString().compareTo(color2.toString()) : 0;
					break;
				case 5:
					sortOrder = series1.getDescription().compareTo(series2.getDescription());
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