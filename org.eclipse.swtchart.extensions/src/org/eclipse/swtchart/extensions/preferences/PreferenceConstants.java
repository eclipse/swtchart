/*******************************************************************************
 * Copyright (c) 2020, 2024 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.preferences;

public class PreferenceConstants {

	public static final int MIN_MOVE_LEGEND = 1;
	public static final int MAX_MOVE_LEGEND = 100;
	//
	public static final String P_BUFFER_SELECTION = "bufferSelection";
	public static final boolean DEF_BUFFER_SELECTION = true;
	public static final String P_KEEP_SERIES_DESCRIPTION = "keepSeriesDescription";
	public static final boolean DEF_KEEP_SERIES_DESCRIPTION = false;
	//
	public static final String P_MOVE_LEGEND_X = "moveLegendX";
	public static final int DEF_MOVE_LEGEND_X = 10;
	public static final String P_MOVE_LEGEND_Y = "moveLegendY";
	public static final int DEF_MOVE_LEGEND_Y = 5;
	//
	public static final String P_LEGEND_POSITION_X = "legendPositionX";
	public static final int DEF_LEGEND_POSITION_X = 100;
	public static final String P_LEGEND_POSITION_Y = "legendPositionY";
	public static final int DEF_LEGEND_POSITION_Y = 100;
	//
	public static final String P_SORT_LEGEND_TABLE = "sortLegendTable";
	public static final boolean DEF_SORT_LEGEND_TABLE = false;
	//
	public static final String P_LEGEND_COLUMN_ORDER = "legendColumnOrder";
	public static final String DEF_LEGEND_COLUMN_ORDER = "";
	public static final String P_CUSTOM_SERIES_COLUMN_ORDER = "customSeriesColumnOrder";
	public static final String DEF_CUSTOM_SERIES_COLUMN_ORDER = "";
	//
	public static final String P_SERIES_MAPPINGS = "seriesMappings";
	public static final String DEF_SERIES_MAPPINGS = "";
	public static final String P_PATH_MAPPINGS_IMPORT = "pathMappingsImport";
	public static final String DEF_PATH_MAPPINGS_IMPORT = "";
	public static final String P_PATH_MAPPINGS_EXPORT = "pathMappingsExport";
	public static final String DEF_PATH_MAPPINGS_EXPORT = "";
	//
	public static final String P_BITMAP_EXPORT_CUSTOM_SIZE = "bitmapExportCustomSize";
	public static final boolean DEF_BITMAP_EXPORT_CUSTOM_SIZE = false;
	public static final String P_BITMAP_EXPORT_WIDTH = "bitmapExportWidth";
	public static final int DEF_BITMAP_EXPORT_WIDTH = 512;
	public static final String P_BITMAP_EXPORT_HEIGHT = "bitmapExportHeight";
	public static final int DEF_BITMAP_EXPORT_HEIGHT = 512;
	//
	public static final String P_SHOW_HELP_FOR_EVENTS = "showHelpForEvents";
	public static final boolean DEF_SHOW_HELP_FOR_EVENTS = false;
	public static final String P_HELP_POPUP_TIME_TO_CLOSE = "showHelpForEvents_timeToClose";
	public static final int DEF_HELP_POPUP_TIME_TO_CLOSE = 6000;
	//
	public static final String P_EXPORT_INDEX_AXIS_X = "exportIndexAxisX_";
	public static final int DEF_EXPORT_INDEX_AXIS_X = 0;
	public static final String P_EXPORT_INDEX_AXIS_Y = "exportIndexAxisY_";
	public static final int DEF_EXPORT_INDEX_AXIS_Y = 0;
}