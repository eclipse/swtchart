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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.ColorsSupport;

public class SeriesEditingSupport extends EditingSupport {

	private TableViewer tableViewer;
	private String title = "";

	public SeriesEditingSupport(TableViewer tableViewer, String title) {
		super(tableViewer);
		this.tableViewer = tableViewer;
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

		CellEditor cellEditor;
		switch(title) {
			case SeriesLabelProvider.VISIBLE:
				cellEditor = new CheckboxCellEditor(tableViewer.getTable());
				break;
			case SeriesLabelProvider.VISIBLE_IN_LEGEND:
				cellEditor = new CheckboxCellEditor(tableViewer.getTable());
				break;
			case SeriesLabelProvider.COLOR:
				cellEditor = new ColorCellEditor(tableViewer.getTable());
				break;
			case SeriesLabelProvider.DESCRIPTION:
				cellEditor = new TextCellEditor(tableViewer.getTable());
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
			switch(title) {
				case SeriesLabelProvider.VISIBLE:
					series.setVisible(Boolean.parseBoolean(object.toString()));
					break;
				case SeriesLabelProvider.VISIBLE_IN_LEGEND:
					series.setVisibleInLegend(Boolean.parseBoolean(object.toString()));
					break;
				case SeriesLabelProvider.COLOR:
					if(object instanceof RGB) {
						/*
						 * Create the color
						 */
						RGB rgb = (RGB)object;
						Color color = ColorsSupport.getColor(rgb);
						//
						if(series instanceof IBarSeries) {
							IBarSeries<?> barSeries = (IBarSeries<?>)element;
							barSeries.setBarColor(color);
						} else if(series instanceof ILineSeries) {
							ILineSeries<?> lineSeries = (ILineSeries<?>)element;
							lineSeries.setLineColor(color);
						}
					}
					break;
				case SeriesLabelProvider.DESCRIPTION:
					series.setDescription(object.toString().trim());
					break;
				default:
					// No action
					break;
			}
			//
			getViewer().refresh();
		}
	}
}
