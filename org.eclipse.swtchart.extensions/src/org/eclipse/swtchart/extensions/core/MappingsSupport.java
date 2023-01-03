/*******************************************************************************
 * Copyright (c) 2021, 2023 Lablicate GmbH.
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

import org.eclipse.swtchart.extensions.barcharts.BarSeriesSettings;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.CircularSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesSettings;

public class MappingsSupport {

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
		} else if(seriesSettings instanceof ICircularSeriesSettings) {
			return MappingsType.CIRCULAR;
		} else {
			return MappingsType.NONE;
		}
	}

	public static MappingsType getMappingsType(String value) {

		try {
			return MappingsType.valueOf(value);
		} catch(Exception e) {
			return MappingsType.NONE;
		}
	}

	/**
	 * Creates a series settings instance, given by the mappings type.
	 * 
	 * @param mappingsType
	 * @return {@link ISeriesSettings}
	 */
	public static ISeriesSettings createSeriesSettings(MappingsType mappingsType) {

		ISeriesSettings seriesSettings = null;
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
			case CIRCULAR:
				seriesSettings = new CircularSeriesSettings();
				break;
			default:
				seriesSettings = null;
				break;
		}
		//
		return seriesSettings;
	}

	/**
	 * Copies the series settings. Null is returned if copying failed.
	 * 
	 * @param seriesSettings
	 * @return {@link ISeriesSettings}
	 */
	public static ISeriesSettings copySettings(ISeriesSettings seriesSettings) {

		if(seriesSettings != null) {
			ISeriesSettings seriesSettingsCopy = seriesSettings.makeDeepCopy();
			transferSettings(seriesSettings.getSeriesSettingsHighlight(), seriesSettingsCopy.getSeriesSettingsHighlight());
			return seriesSettingsCopy;
		} else {
			return null;
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
			if(!seriesSettingsSource.isHighlight() && !seriesSettingsSink.isHighlight()) {
				success = transfer(seriesSettingsSource.getSeriesSettingsHighlight(), seriesSettingsSink.getSeriesSettingsHighlight());
			}
		}
		//
		return success;
	}

	private static boolean transfer(ISeriesSettings seriesSettingsSource, ISeriesSettings seriesSettingsSink) {

		boolean success = false;
		if(seriesSettingsSource != null && seriesSettingsSink != null) {
			success = seriesSettingsSource.transfer(seriesSettingsSink);
		}
		//
		return success;
	}
}