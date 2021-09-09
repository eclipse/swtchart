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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.MappingsSupport;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.core.SeriesLabelProvider;
import org.eclipse.swtchart.extensions.core.SeriesListUI;

public class SeriesEditingSupport extends EditingSupport {

	private SeriesListUI seriesListUI;
	private String title = "";

	public SeriesEditingSupport(SeriesListUI seriesListUI, String title) {

		super(seriesListUI);
		this.seriesListUI = seriesListUI;
		this.title = title;
	}

	@Override
	protected boolean canEdit(Object element) {

		boolean canEdit;
		switch(title) {
			case SeriesLabelProvider.VISIBLE:
				canEdit = true;
				break;
			case SeriesLabelProvider.VISIBLE_IN_LEGEND:
				canEdit = true;
				break;
			case SeriesLabelProvider.COLOR:
				canEdit = true;
				break;
			case SeriesLabelProvider.DESCRIPTION:
				canEdit = true;
				break;
			default:
				canEdit = false;
				break;
		}
		return canEdit;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {

		/*
		 * Probably, create a generic ISeriesSettings Cell Editor, which let's the
		 * user modify all fields except the id.
		 */
		CellEditor cellEditor;
		switch(title) {
			case SeriesLabelProvider.VISIBLE:
				cellEditor = new CheckboxCellEditor(seriesListUI.getTable());
				break;
			case SeriesLabelProvider.VISIBLE_IN_LEGEND:
				cellEditor = new CheckboxCellEditor(seriesListUI.getTable());
				break;
			case SeriesLabelProvider.COLOR:
				cellEditor = new ColorCellEditor(seriesListUI.getTable());
				break;
			case SeriesLabelProvider.DESCRIPTION:
				cellEditor = new TextCellEditor(seriesListUI.getTable());
				break;
			default:
				cellEditor = null;
				break;
		}
		return cellEditor;
	}

	@Override
	protected Object getValue(Object element) {

		Object object;
		switch(title) {
			case SeriesLabelProvider.VISIBLE:
				object = SeriesLabelProvider.isVisible(element);
				break;
			case SeriesLabelProvider.VISIBLE_IN_LEGEND:
				object = SeriesLabelProvider.isVisibleInLegend(element);
				break;
			case SeriesLabelProvider.COLOR:
				Color color = SeriesLabelProvider.getColor(element);
				object = color != null ? color.getRGB() : null;
				break;
			case SeriesLabelProvider.DESCRIPTION:
				object = SeriesLabelProvider.getDescription(element);
				break;
			default:
				object = null;
				break;
		}
		return object;
	}

	@Override
	protected void setValue(Object element, Object object) {

		if(element instanceof ISeries) {
			ISeries<?> series = (ISeries<?>)element;
			MappingsSupport.mapSettings(series, title, object, getScrollableChart());
			refresh();
		}
	}

	private ScrollableChart getScrollableChart() {

		return seriesListUI.getScrollableChart();
	}

	private void refresh() {

		MappingsSupport.adjustSettings(getScrollableChart());
		seriesListUI.refresh();
	}
}
