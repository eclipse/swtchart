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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.SeriesLabelProvider;
import org.eclipse.swtchart.extensions.core.SeriesListUI;
import org.eclipse.swtchart.extensions.core.SeriesMapper;

public class SeriesEditingSupport extends EditingSupport {

	private SeriesListUI seriesListUI;
	private int columnIndex = 0;

	public SeriesEditingSupport(SeriesListUI seriesListUI, int columnIndex) {

		super(seriesListUI);
		this.columnIndex = columnIndex;
		this.seriesListUI = seriesListUI;
	}

	@Override
	protected boolean canEdit(Object element) {

		boolean canEdit;
		switch(columnIndex) {
			case SeriesLabelProvider.INDEX_VISIBLE:
				canEdit = true;
				break;
			case SeriesLabelProvider.INDEX_VISIBLE_IN_LEGEND:
				canEdit = true;
				break;
			case SeriesLabelProvider.INDEX_COLOR:
				canEdit = true;
				break;
			case SeriesLabelProvider.INDEX_DESCRIPTION:
				canEdit = true;
				break;
			case SeriesLabelProvider.INDEX_MAPPING_STATUS:
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
			case SeriesLabelProvider.INDEX_VISIBLE:
				cellEditor = new CheckboxCellEditor(seriesListUI.getTable());
				break;
			case SeriesLabelProvider.INDEX_VISIBLE_IN_LEGEND:
				cellEditor = new CheckboxCellEditor(seriesListUI.getTable());
				break;
			case SeriesLabelProvider.INDEX_COLOR:
				cellEditor = new ColorCellEditor(seriesListUI.getTable());
				break;
			case SeriesLabelProvider.INDEX_DESCRIPTION:
				cellEditor = new TextCellEditor(seriesListUI.getTable());
				break;
			case SeriesLabelProvider.INDEX_MAPPING_STATUS:
				cellEditor = new CheckboxCellEditor(seriesListUI.getTable());
				break;
			default:
				cellEditor = null;
				break;
		}
		return cellEditor;
	}

	@Override
	protected Object getValue(Object element) {

		Object object = null;
		if(element instanceof ISeries) {
			/*
			 * Series Settings
			 */
			ISeries<?> series = (ISeries<?>)element;
			BaseChart baseChart = seriesListUI.getBaseChart();
			ISeriesSettings seriesSettings = baseChart.getSeriesSettings(series.getId());
			//
			switch(columnIndex) {
				case SeriesLabelProvider.INDEX_VISIBLE:
					object = seriesSettings.isVisible();
					break;
				case SeriesLabelProvider.INDEX_VISIBLE_IN_LEGEND:
					object = seriesSettings.isVisibleInLegend();
					break;
				case SeriesLabelProvider.INDEX_COLOR:
					Color color = SeriesLabelProvider.getColor(seriesSettings);
					object = color != null ? color.getRGB() : null;
					break;
				case SeriesLabelProvider.INDEX_DESCRIPTION:
					object = seriesSettings.getDescription();
					break;
				case SeriesLabelProvider.INDEX_MAPPING_STATUS:
					object = (SeriesMapper.get(series, baseChart) != null);
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

		if(element instanceof ISeries) {
			/*
			 * Series Settings
			 */
			ISeries<?> series = (ISeries<?>)element;
			BaseChart baseChart = getBaseChart();
			ISeriesSettings seriesSettings = baseChart.getSeriesSettings(series.getId());
			//
			switch(columnIndex) {
				case SeriesLabelProvider.INDEX_VISIBLE:
					seriesSettings.setVisible(Boolean.valueOf(object.toString()));
					break;
				case SeriesLabelProvider.INDEX_VISIBLE_IN_LEGEND:
					seriesSettings.setVisibleInLegend(Boolean.valueOf(object.toString()));
					break;
				case SeriesLabelProvider.INDEX_COLOR:
					if(object instanceof RGB rgbNew) {
						Color color = ResourceSupport.getColor(rgbNew);
						SeriesLabelProvider.setColor(seriesSettings, color);
					}
					break;
				case SeriesLabelProvider.INDEX_DESCRIPTION:
					seriesSettings.setDescription(object.toString());
					break;
				case SeriesLabelProvider.INDEX_MAPPING_STATUS:
					boolean map = Boolean.valueOf(object.toString());
					if(map) {
						SeriesMapper.map(series, baseChart);
					} else {
						SeriesMapper.unmap(series, baseChart);
					}
					break;
			}
			//
			baseChart.applySeriesSettings(series, seriesSettings, true);
			refresh();
		}
	}

	private BaseChart getBaseChart() {

		return seriesListUI.getBaseChart();
	}

	private void refresh() {

		getBaseChart().redraw();
		seriesListUI.refresh();
	}
}