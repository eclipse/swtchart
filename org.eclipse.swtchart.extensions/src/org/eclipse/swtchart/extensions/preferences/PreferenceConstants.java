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
package org.eclipse.swtchart.extensions.preferences;

public class PreferenceConstants {

	public static final int MIN_MOVE_LEGEND = 1;
	public static final int MAX_MOVE_LEGEND = 100;
	/*
	 * The buffered selection creates a screenshot and sets it in the background
	 * while doing the data selection. This prevents to redraw the series while
	 * doing the selection event. Known limitations:
	 * ---
	 * macOS - https://github.com/eclipse/swtchart/issues/150
	 * GTK3 - https://github.com/eclipse/swtchart/issues/166
	 */
	public static final String P_BUFFER_SELECTION = "bufferSelection";
	public static final boolean DEF_BUFFER_SELECTION = false;
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
}