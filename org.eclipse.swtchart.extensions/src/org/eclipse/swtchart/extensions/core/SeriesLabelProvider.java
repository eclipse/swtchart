/*******************************************************************************
 * Copyright (c) 2020, 2021 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.core;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries;

public class SeriesLabelProvider extends ColumnLabelProvider implements ITableLabelProvider {

	public static final String ID = "ID";
	public static final String VISIBLE = "Visible";
	public static final String VISIBLE_IN_LEGEND = "Visible In Legend";
	public static final String COLOR = "Color";
	public static final String DESCRIPTION = "Description";
	//
	public static final int INDEX_ID = 0;
	public static final int INDEX_VISIBLE = 1;
	public static final int INDEX_VISIBLE_IN_LEGEND = 2;
	//
	public static final String[] TITLES = { //
			ID, //
			VISIBLE, //
			VISIBLE_IN_LEGEND, //
			COLOR, //
			DESCRIPTION //
	};
	//
	public static final int[] BOUNDS = { //
			24, //
			30, //
			30, //
			30, //
			200 //
	};

	public static boolean isVisible(Object element) {

		boolean isVisible = false;
		//
		if(element instanceof ISeries<?>) {
			ISeries<?> series = (ISeries<?>)element;
			isVisible = series.isVisible();
		}
		//
		return isVisible;
	}

	public static boolean isVisibleInLegend(Object element) {

		boolean isVisible = false;
		//
		if(element instanceof ISeries<?>) {
			ISeries<?> series = (ISeries<?>)element;
			isVisible = series.isVisibleInLegend();
		}
		//
		return isVisible;
	}

	public static Color getColor(Object element) {

		Color color = null;
		//
		if(element instanceof IBarSeries) {
			IBarSeries<?> series = (IBarSeries<?>)element;
			color = series.getBarColor();
		} else if(element instanceof ILineSeries) {
			ILineSeries<?> series = (ILineSeries<?>)element;
			color = series.getLineColor();
		}
		//
		return color;
	}

	public static void setColor(Object element, Color color) {

		if(color != null) {
			if(element instanceof IBarSeries) {
				IBarSeries<?> barSeries = (IBarSeries<?>)element;
				barSeries.setBarColor(color);
			} else if(element instanceof ILineSeries) {
				ILineSeries<?> lineSeries = (ILineSeries<?>)element;
				lineSeries.setLineColor(color);
			}
		}
	}

	public static String getDescription(Object element) {

		String description = "";
		if(element instanceof ISeries) {
			ISeries<?> series = (ISeries<?>)element;
			description = series.getDescription();
		}
		return description;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(element instanceof ISeries) {
			/*
			 * CheckBoxes
			 */
			Image seriesMarker = ResourceSupport.getImage(ResourceSupport.ICON_SERIES_MARKER);
			Image checked = ResourceSupport.getImage(ResourceSupport.ICON_CHECKED);
			Image unchecked = ResourceSupport.getImage(ResourceSupport.ICON_UNCHECKED);
			//
			if(columnIndex == INDEX_ID) {
				return seriesMarker;
			} else if(columnIndex == INDEX_VISIBLE) {
				return isVisible(element) ? checked : unchecked;
			} else if(columnIndex == INDEX_VISIBLE_IN_LEGEND) {
				return isVisibleInLegend(element) ? checked : unchecked;
			}
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof ISeries) {
			ISeries<?> series = (ISeries<?>)element;
			switch(columnIndex) {
				case 0:
					text = series.getId();
					break;
				case 1:
					text = ""; // Visible
					break;
				case 2:
					text = ""; // VisibleInLegend
					break;
				case 3:
					text = ""; // Color
					break;
				case 4:
					text = series.getDescription();
					break;
				default:
					text = "";
					break;
			}
		}
		return text;
	}
}
