/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesSettings;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.MappingsType;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesSettings;

public class MappingsSupport {

	/**
	 * Adjusts the series settings.
	 * 
	 * @param seriesArray
	 */
	public static void adjustSettings(ScrollableChart scrollableChart) {

		if(scrollableChart != null) {
			/*
			 * Adjust
			 */
			BaseChart baseChart = scrollableChart.getBaseChart();
			ISeries<?>[] seriesArray = baseChart.getSeriesSet().getSeries();
			for(ISeries<?> series : seriesArray) {
				/*
				 * Transfer the mapped setting to the series setting
				 * of the base chart.
				 */
				String id = series.getId();
				ISeriesSettings seriesSettingsSource = SeriesMapper.containsMappedSeriesSetting(id) ? SeriesMapper.getSeriesSettingsMapped(id) : SeriesMapper.getSeriesSettingsDefault(id);
				ISeriesSettings seriesSettingsSink = baseChart.getSeriesSettings(id);
				boolean success = MappingsSupport.transferSettings(seriesSettingsSource, seriesSettingsSink);
				if(success) {
					baseChart.applySeriesSettings(series, seriesSettingsSink);
				}
			}
			/*
			 * Redraw
			 */
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {

					baseChart.redraw();
				}
			});
		}
	}

	/**
	 * Map the series settings.
	 * 
	 * @param series
	 * @param property
	 * @param object
	 * @param scrollableChart
	 */
	public static void mapSettings(ISeries<?> series, String property, Object object, ScrollableChart scrollableChart) {

		String id = series.getId();
		ISeriesSettings seriesSettingsDefault = SeriesMapper.getSeriesSettingsDefault(id, scrollableChart);
		ISeriesSettings seriesSettings = getMappedSettings(id, seriesSettingsDefault);
		//
		switch(property) {
			case SeriesLabelProvider.VISIBLE:
				boolean visible = Boolean.parseBoolean(object.toString());
				series.setVisible(visible);
				if(seriesSettings != null) {
					seriesSettings.setVisible(visible);
				}
				break;
			case SeriesLabelProvider.VISIBLE_IN_LEGEND:
				boolean visibleInLegend = Boolean.parseBoolean(object.toString());
				series.setVisibleInLegend(visibleInLegend);
				if(seriesSettings != null) {
					seriesSettings.setVisibleInLegend(visibleInLegend);
				}
				break;
			case SeriesLabelProvider.COLOR:
				if(object instanceof RGB) {
					RGB rgb = (RGB)object;
					Color color = ResourceSupport.getColor(rgb);
					SeriesLabelProvider.setColor(series, color);
					if(seriesSettings != null) {
						setColor(seriesSettings, color);
					}
				}
				break;
			case SeriesLabelProvider.DESCRIPTION:
				String description = object.toString().trim();
				series.setDescription(description);
				if(seriesSettings != null) {
					seriesSettings.setDescription(description);
				}
				break;
			default:
				break;
		}
		/*
		 * Map the changed settings
		 */
		if(seriesSettings != null) {
			SeriesMapper.mapSetting(id, seriesSettings, seriesSettingsDefault);
		}
	}

	/**
	 * Transfers the status of the source to the sink settings.
	 * 
	 * @param seriesSettingsSource
	 * @param seriesSettingsSink
	 */
	public static boolean transferSettings(ISeriesSettings seriesSettingsSource, ISeriesSettings seriesSettingsSink) {

		boolean success = transfer(seriesSettingsSource, seriesSettingsSink);
		if(success) {
			success = transfer(seriesSettingsSource.getSeriesSettingsHighlight(), seriesSettingsSink.getSeriesSettingsHighlight());
		}
		//
		return success;
	}

	/**
	 * Returns the settings type.
	 * 
	 * @param seriesSettings
	 * @return {@link MappingsType}
	 */
	public static MappingsType getMappingsType(ISeriesSettings seriesSettings) {

		if(seriesSettings instanceof IBarSeriesSettings) {
			return MappingsType.BAR;
		} else if(seriesSettings instanceof ILineSeriesSettings) {
			return MappingsType.LINE;
		} else if(seriesSettings instanceof IScatterSeriesSettings) {
			return MappingsType.SCATTER;
		} else {
			return MappingsType.NONE;
		}
	}

	/**
	 * Copies the series settings.
	 * 
	 * @param seriesSettings
	 * @return {@link ISeriesSettings}
	 */
	public static ISeriesSettings copySeriesSettings(ISeriesSettings seriesSettings) {

		MappingsType mappingType = getMappingsType(seriesSettings);
		ISeriesSettings seriesSettingsCopied = createSeriesSettings(mappingType);
		transfer(seriesSettings, seriesSettingsCopied);
		return seriesSettingsCopied;
	}

	/**
	 * Creates a series settings instance, given by the mappings type.
	 * 
	 * @param mappingsType
	 * @return {@link ISeriesSettings}
	 */
	public static ISeriesSettings createSeriesSettings(MappingsType mappingsType) {

		return createSeriesSettings(mappingsType.name());
	}

	/**
	 * Creates a series settings instance, given by the mappings type.
	 * 
	 * @param mappingsType
	 * @return {@link ISeriesSettings}
	 */
	public static ISeriesSettings createSeriesSettings(String mappingsTypeName) {

		ISeriesSettings seriesSettings = null;
		try {
			MappingsType mappingsType = MappingsType.valueOf(mappingsTypeName);
			switch(mappingsType) {
				case BAR:
					seriesSettings = new BarSeriesSettings();
					break;
				case LINE:
					seriesSettings = new LineSeriesSettings();
					break;
				case SCATTER:
					seriesSettings = new ScatterSeriesSettings();
					break;
				default:
					seriesSettings = null;
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		//
		return seriesSettings;
	}

	/**
	 * Get the bar or line color of the setting. If this fails, the default color is returned.
	 * 
	 * @param seriesSettings
	 * @return Color
	 */
	public static Color getColor(ISeriesSettings seriesSettings) {

		if(seriesSettings instanceof IBarSeriesSettings) {
			IBarSeriesSettings barSeriesSettings = (IBarSeriesSettings)seriesSettings;
			return barSeriesSettings.getBarColor();
		} else if(seriesSettings instanceof ILineSeriesSettings) {
			ILineSeriesSettings lineSeriesSettings = (ILineSeriesSettings)seriesSettings;
			return lineSeriesSettings.getLineColor();
		} else {
			// TODO - circular settings
			return ResourceSupport.getColorDefault();
		}
	}

	/**
	 * If appropriate, set the bar or line color.
	 * 
	 * @param seriesSettings
	 * @param color
	 */
	public static void setColor(ISeriesSettings seriesSettings, Color color) {

		if(color != null) {
			if(seriesSettings instanceof IBarSeriesSettings) {
				IBarSeriesSettings barSeriesSettings = (IBarSeriesSettings)seriesSettings;
				barSeriesSettings.setBarColor(color);
			} else if(seriesSettings instanceof ILineSeriesSettings) {
				ILineSeriesSettings lineSeriesSettings = (ILineSeriesSettings)seriesSettings;
				lineSeriesSettings.setLineColor(color);
			} else {
				// TODO - circular settings
			}
		}
	}

	private static boolean transfer(ISeriesSettings seriesSettingsSource, ISeriesSettings seriesSettingsSink) {

		/*
		 * TODO - transfer all settings from Bar, Line, Scatter and Circular settings.
		 */
		boolean success = false;
		if(seriesSettingsSource != null && seriesSettingsSink != null) {
			seriesSettingsSink.setDescription(seriesSettingsSource.getDescription());
			seriesSettingsSink.setVisible(seriesSettingsSource.isVisible());
			seriesSettingsSink.setVisibleInLegend(seriesSettingsSource.isVisible());
			setColor(seriesSettingsSink, getColor(seriesSettingsSource));
			success = true;
		}
		//
		return success;
	}

	private static ISeriesSettings getMappedSettings(String id, ISeriesSettings seriesSettingsDefault) {

		ISeriesSettings seriesSettings = SeriesMapper.getSeriesSettingsMapped(id);
		if(seriesSettings == null) {
			seriesSettings = MappingsSupport.copySeriesSettings(seriesSettingsDefault);
		}
		//
		return seriesSettings;
	}
}