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
package org.eclipse.swtchart.extensions.core;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;

public class SeriesLabelProvider extends ColumnLabelProvider implements ITableLabelProvider {

	public static final String ID = "ID";
	public static final String VISIBLE = "Visible";
	public static final String VISIBLE_IN_LEGEND = "Visible In Legend";
	public static final String COLOR = "Color";
	public static final String DESCRIPTION = "Description";
	public static final String MAPPING_STATUS = "Mapping Status";
	//
	public static final int INDEX_ID = 0;
	public static final int INDEX_MAPPING_STATUS = 1;
	public static final int INDEX_VISIBLE = 2;
	public static final int INDEX_VISIBLE_IN_LEGEND = 3;
	//
	private BaseChart baseChart = null;
	//
	public static final String[] TITLES = { //
			ID, //
			MAPPING_STATUS, //
			VISIBLE, //
			VISIBLE_IN_LEGEND, //
			COLOR, //
			DESCRIPTION, //
	};
	//
	public static final int[] BOUNDS = { //
			24, //
			30, //
			30, //
			30, //
			30, //
			200 //
	};

	/**
	 * Get the color of the selected series element.
	 * 
	 * @param element
	 * @return Color
	 */
	public static Color getColor(ISeriesSettings seriesSettings) {

		Color color = null;
		//
		if(seriesSettings instanceof IBarSeriesSettings) {
			color = ((IBarSeriesSettings)seriesSettings).getBarColor();
		} else if(seriesSettings instanceof ILineSeriesSettings) {
			color = ((ILineSeriesSettings)seriesSettings).getLineColor();
		} else if(seriesSettings instanceof IScatterSeriesSettings) {
			color = ((IScatterSeriesSettings)seriesSettings).getSymbolColor();
		} else if(seriesSettings instanceof ICircularSeriesSettings) {
			color = ((ICircularSeriesSettings)seriesSettings).getSliceColor();
		}
		//
		return color;
	}

	/**
	 * Set the color via settings. Don't forget to apply the settings
	 * via the base chart.
	 * 
	 * @param seriesSettings
	 * @param color
	 */
	public static void setColor(ISeriesSettings seriesSettings, Color color) {

		if(color != null) {
			if(seriesSettings instanceof IBarSeriesSettings) {
				((IBarSeriesSettings)seriesSettings).setBarColor(color);
			} else if(seriesSettings instanceof ILineSeriesSettings) {
				((ILineSeriesSettings)seriesSettings).setLineColor(color);
			} else if(seriesSettings instanceof IScatterSeriesSettings) {
				((IScatterSeriesSettings)seriesSettings).setSymbolColor(color);
			} else if(seriesSettings instanceof ICircularSeriesSettings) {
				((ICircularSeriesSettings)seriesSettings).setSliceColor(color);
			}
		}
	}

	public void setBaseChart(BaseChart baseChart) {

		this.baseChart = baseChart;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(element instanceof ISeries<?>) {
			/*
			 * CheckBoxes
			 */
			ISeries<?> series = (ISeries<?>)element;
			Image seriesMarker = ResourceSupport.getImage(ResourceSupport.ICON_SERIES_MARKER);
			Image checked = ResourceSupport.getImage(ResourceSupport.ICON_CHECKED);
			Image unchecked = ResourceSupport.getImage(ResourceSupport.ICON_UNCHECKED);
			Image mappings = ResourceSupport.getImage(ResourceSupport.ICON_MAPPINGS);
			//
			if(columnIndex == INDEX_ID) {
				return seriesMarker;
			} else if(columnIndex == INDEX_VISIBLE) {
				return series.isVisible() ? checked : unchecked;
			} else if(columnIndex == INDEX_VISIBLE_IN_LEGEND) {
				return series.isVisibleInLegend() ? checked : unchecked;
			} else if(columnIndex == INDEX_MAPPING_STATUS) {
				if(baseChart != null) {
					ISeriesSettings seriesSettings = SeriesMapper.get(series, baseChart);
					if(seriesSettings != null) {
						return mappings;
					}
				}
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
					text = ""; // Mapping Status
					break;
				case 3:
					text = ""; // VisibleInLegend
					break;
				case 4:
					text = ""; // Color
					break;
				case 5:
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