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

	public static final int INDEX_ID = 0;
	public static final int INDEX_MAPPING_STATUS = 1;
	public static final int INDEX_VISIBLE = 2;
	public static final int INDEX_VISIBLE_IN_LEGEND = 3;
	public static final int INDEX_COLOR = 4;
	public static final int INDEX_DESCRIPTION = 5;
	//
	private BaseChart baseChart = null;
	//
	public static final String[] TITLES = { //
			Messages.getString(Messages.ID), //
			Messages.getString(Messages.MAPPING_STATUS), //
			Messages.getString(Messages.VISIBLE), //
			Messages.getString(Messages.VISIBLE_IN_LEGEND), //
			Messages.getString(Messages.COLOR), //
			Messages.getString(Messages.DESCRIPTION), //
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
		if(seriesSettings instanceof IBarSeriesSettings barSeriesSettings) {
			color = barSeriesSettings.getBarColor();
		} else if(seriesSettings instanceof ILineSeriesSettings lineSeriesSettings) {
			color = lineSeriesSettings.getLineColor();
		} else if(seriesSettings instanceof IScatterSeriesSettings scatterSeriesSettings) {
			color = scatterSeriesSettings.getSymbolColor();
		} else if(seriesSettings instanceof ICircularSeriesSettings circularSeriesSettings) {
			color = circularSeriesSettings.getSliceColor();
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
			if(seriesSettings instanceof IBarSeriesSettings barSeriesSettings) {
				barSeriesSettings.setBarColor(color);
			} else if(seriesSettings instanceof ILineSeriesSettings lineSeriesSettings) {
				lineSeriesSettings.setLineColor(color);
			} else if(seriesSettings instanceof IScatterSeriesSettings scatterSeriesSettings) {
				scatterSeriesSettings.setSymbolColor(color);
			} else if(seriesSettings instanceof ICircularSeriesSettings circularSeriesSettings) {
				circularSeriesSettings.setSliceColor(color);
			}
		}
	}

	public void setBaseChart(BaseChart baseChart) {

		this.baseChart = baseChart;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(element instanceof ISeries<?> series) {
			/*
			 * CheckBoxes
			 */
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

		String text = ""; //$NON-NLS-1$
		if(element instanceof ISeries) {
			ISeries<?> series = (ISeries<?>)element;
			switch(columnIndex) {
				case 0:
					text = series.getId();
					break;
				case 1:
					text = ""; // Visible //$NON-NLS-1$
					break;
				case 2:
					text = ""; // Mapping Status //$NON-NLS-1$
					break;
				case 3:
					text = ""; // VisibleInLegend //$NON-NLS-1$
					break;
				case 4:
					text = ""; // Color //$NON-NLS-1$
					break;
				case 5:
					text = series.getDescription();
					break;
				default:
					text = ""; //$NON-NLS-1$
					break;
			}
		}
		return text;
	}
}