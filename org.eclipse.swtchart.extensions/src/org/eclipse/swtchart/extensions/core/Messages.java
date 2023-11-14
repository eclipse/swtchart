/*******************************************************************************
 * Copyright (c) 2020, 2023 SWT Chart Project
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Frank Buloup - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.core.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	//
	public static String ADD_MAPPING = "ADD_MAPPING";
	public static String ALL_MAPPINGS_DELETED = "ALL_MAPPINGS_DELETED";
	public static String BAR = "BAR";
	public static String CANT_SET_SECONDARY_X_AXIS_RANGE = "CANT_SET_SECONDARY_X_AXIS_RANGE";
	public static String X_Y_SERIES_LENGTH_DIFFERS = "X_Y_SERIES_LENGTH_DIFFERS";
	public static String CHART_TITLE = "CHART_TITLE";
	public static String CIRCULAR = "CIRCULAR";
	public static String COLOR = "COLOR";
	public static String DELETE_ALL_MAPPINGS = "DELETE_ALL_MAPPINGS";
	public static String DELETE_SELECTED_MAPPING = "DELETE_SELECTED_MAPPING";
	public static String DESCRIPTION = "DESCRIPTION";
	public static String DISPLAY_MAPPINGS = "DISPLAY_MAPPINGS";
	public static String DOUBLE_CLICK_TO_SHOW_RANGE_INFO = "DOUBLE_CLICK_TO_SHOW_RANGE_INFO";
	public static String DRAW = "DRAW";
	public static String EXPORT = "EXPORT";
	public static String FAILED_TO_APPLY_SETTINGS = "FAILED_TO_APPLY_SETTINGS";
	public static String HIDE = "HIDE";
	public static String HIDE_RANGE_SELECTOR_UI = "HIDE_RANGE_SELECTOR_UI";
	public static String ID = "ID";
	public static String IMPORT = "IMPORT";
	public static String LABEL = "LABEL";
	public static String LABEL_NOT_SET = "LABEL_NOT_SET";
	public static String LEGEND_POSITION = "LEGEND_POSITION";
	public static String LEGEND_POSITION_X = "LEGEND_POSITION_X";
	public static String LEGEND_POSITION_Y = "LEGEND_POSITION_Y";
	public static String LINE = "LINE";
	public static String MAP_ALL_LISTED_SERIES = "MAP_ALL_LISTED_SERIES";
	public static String MAPPED_SERIES_SETTINGS = "MAPPED_SERIES_SETTINGS";
	public static String MAPPING_STATUS = "MAPPING_STATUS";
	public static String MAPPINGS = "MAPPINGS";
	public static String MAPPINGS_EXPORTED = "MAPPINGS_EXPORTED";
	public static String MAPPINGS_IMPORTED = "MAPPINGS_IMPORTED";
	public static String MAPPINGS_SAVED = "MAPPINGS_SAVED";
	public static String MOVE_LEGEND_DOWN = "MOVE_LEGEND_DOWN";
	public static String MOVE_LEGEND_LEFT = "MOVE_LEGEND_LEFT";
	public static String MOVE_LEGEND_RIGHT = "MOVE_LEGEND_RIGHT";
	public static String MOVE_LEGEND_UP = "MOVE_LEGEND_UP";
	public static String NEW_MAPPING_ADDED = "NEW_MAPPING_ADDED";
	public static String NONE = "NONE";
	public static String NOT_SET = "NOT_SET";
	public static String OPEN_SETTINGS_PAGE = "OPEN_SETTINGS_PAGE";
	public static String POSITION_USING_MOUSE = "POSITION_USING_MOUSE";
	public static String REALLY_DELETE_ALL_MAPPINGS = "REALLY_DELETE_ALL_MAPPINGS";
	public static String REALLY_DELETE_SELECTED_MAPPING = "REALLY_DELETE_SELECTED_MAPPING";
	public static String RESET = "RESET";
	public static String RESET_RANGE = "RESET_RANGE";
	public static String SAVE_MAPPINGS = "SAVE_MAPPINGS";
	public static String SCATTER = "SCATTER";
	public static String SELECTED_MAPPING_DELETED = "SELECTED_MAPPING_DELETED";
	public static String SERIES_POPUP_MENU = "SERIES_POPUP_MENU";
	public static String SET = "SET";
	public static String SET_CURRENT_SELECTION = "SET_CURRENT_SELECTION";
	public static String SET_LEGEND_POSITION = "SET_LEGEND_POSITION";
	public static String SETTINGS = "SETTINGS";
	public static String SORT_TABLE = "SORT_TABLE";
	public static String TOGGLE_VISIBILITY = "TOGGLE_VISIBILITY";
	public static String TOGGLE_VISIBILITY_OF_EMBEDDED_LEGEND = "TOGGLE_VISIBILITY_OF_EMBEDDED_LEGEND";
	public static String TRANSFER_MAPPINGS_OF_SELECTED_SERIES = "TRANSFER_MAPPINGS_OF_SELECTED_SERIES";
	public static String UNDETERMINED_SERIES_TYPE = "UNDETERMINED_SERIES_TYPE";
	public static String VISIBLE = "VISIBLE";
	public static String VISIBLE_IN_LEGEND = "VISIBLE_IN_LEGEND";
	public static String X_AXIS = "X_AXIS";
	public static String Y_AXIS = "Y_AXIS";

	private Messages() {

	}

	public static String getString(String key) {

		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch(MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
