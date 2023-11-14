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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swtchart.extensions.core.CustomSeriesLabelProvider;
import org.eclipse.swtchart.extensions.core.CustomSeriesListUI;
import org.eclipse.swtchart.extensions.model.ICustomSeries;

public class CustomSeriesEditingSupport extends EditingSupport {

	private CustomSeriesListUI customSeriesListUI;
	private int columnIndex = 0;

	public CustomSeriesEditingSupport(CustomSeriesListUI seriesListUI, int columnIndex) {

		super(seriesListUI);
		this.customSeriesListUI = seriesListUI;
		this.columnIndex = columnIndex;
	}

	@Override
	protected boolean canEdit(Object element) {

		boolean canEdit;
		switch(columnIndex) {
			case CustomSeriesLabelProvider.INDEX_DRAW:
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
		switch(columnIndex) {
			case CustomSeriesLabelProvider.INDEX_DRAW:
				cellEditor = new CheckboxCellEditor(customSeriesListUI.getTable());
				break;
			default:
				cellEditor = new TextCellEditor(customSeriesListUI.getTable());
				break;
		}
		return cellEditor;
	}

	@Override
	protected Object getValue(Object element) {

		Object object = null;
		if(element instanceof ICustomSeries customSeries) {
			/*
			 * Series Settings
			 */
			switch(columnIndex) {
				case CustomSeriesLabelProvider.INDEX_DRAW:
					object = customSeries.isDraw();
					break;
				default:
					object = null;
					break;
			}
		}
		return object;
	}

	@Override
	protected void setValue(Object element, Object object) {

		if(element instanceof ICustomSeries customSeries) {
			/*
			 * Series Settings
			 */
			switch(columnIndex) {
				case CustomSeriesLabelProvider.INDEX_DRAW:
					customSeries.setDraw(Boolean.valueOf(object.toString()));
					break;
			}
			//
			refresh();
		}
	}

	private void refresh() {

		customSeriesListUI.refresh();
	}
}