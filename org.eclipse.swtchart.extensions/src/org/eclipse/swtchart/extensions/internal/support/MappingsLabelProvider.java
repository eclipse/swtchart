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
package org.eclipse.swtchart.extensions.internal.support;

import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.ResourceSupport;

public class MappingsLabelProvider extends ColumnLabelProvider implements ITableLabelProvider {

	public static final String ID = "ID";
	public static final String DESCRIPTION = "Description";
	//
	public static final String[] TITLES = { //
			ID, //
			DESCRIPTION //
	};
	//
	public static final int[] BOUNDS = { //
			24, //
			200 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return ResourceSupport.getImage(ResourceSupport.ICON_SERIES_MARKER);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof Map.Entry) {
			Map.Entry<String, ISeriesSettings> entry = (Map.Entry<String, ISeriesSettings>)element;
			//
			switch(columnIndex) {
				case 0:
					text = entry.getKey();
					break;
				case 1:
					text = entry.getValue().getDescription();
					break;
				default:
					text = "";
					break;
			}
		}
		return text;
	}
}
