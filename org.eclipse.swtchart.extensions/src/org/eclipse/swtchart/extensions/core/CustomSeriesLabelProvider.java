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
package org.eclipse.swtchart.extensions.core;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swtchart.extensions.model.ICustomSeries;

public class CustomSeriesLabelProvider extends ColumnLabelProvider implements ITableLabelProvider {

	public static final String ID = "ID";
	public static final String DRAW = "Draw";
	public static final String LABEL = "Label";
	public static final String DESCRIPTION = "Description";
	//
	public static final int INDEX_ID = 0;
	public static final int INDEX_DRAW = 1;
	//
	public static final String[] TITLES = { //
			ID, //
			DRAW, //
			LABEL, //
			DESCRIPTION, //
	};
	//
	public static final int[] BOUNDS = { //
			24, //
			30, //
			100, //
			200 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(element instanceof ICustomSeries customPaintSeries) {
			if(columnIndex == INDEX_ID) {
				return ResourceSupport.getImage(ResourceSupport.ICON_SERIES_MARKER);
			} else if(columnIndex == INDEX_DRAW) {
				Image checked = ResourceSupport.getImage(ResourceSupport.ICON_CHECKED);
				Image unchecked = ResourceSupport.getImage(ResourceSupport.ICON_UNCHECKED);
				return customPaintSeries.isDraw() ? checked : unchecked;
			}
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof ICustomSeries customPaintSeries) {
			switch(columnIndex) {
				case 0:
					text = customPaintSeries.getId();
					break;
				case 1:
					text = "";
					break;
				case 2:
					text = customPaintSeries.getLabel();
					break;
				case 3:
					text = customPaintSeries.getDescription();
					break;
				default:
					text = "";
					break;
			}
		}
		//
		return text;
	}
}