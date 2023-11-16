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
	public static final String ADD_MAPPING = "ADD_MAPPING";
	public static final String ALL_MAPPINGS_DELETED = "ALL_MAPPINGS_DELETED";
	public static final String BAR = "BAR";
	public static final String CANT_SET_SECONDARY_X_AXIS_RANGE = "CANT_SET_SECONDARY_X_AXIS_RANGE";
	public static final String X_Y_SERIES_LENGTH_DIFFERS = "X_Y_SERIES_LENGTH_DIFFERS";
	public static final String CHART_TITLE = "CHART_TITLE";
	public static final String CIRCULAR = "CIRCULAR";
	public static final String COLOR = "COLOR";
	public static final String DELETE_ALL_MAPPINGS = "DELETE_ALL_MAPPINGS";
	public static final String DELETE_SELECTED_MAPPING = "DELETE_SELECTED_MAPPING";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String DISPLAY_MAPPINGS = "DISPLAY_MAPPINGS";
	public static final String DOUBLE_CLICK_TO_SHOW_RANGE_INFO = "DOUBLE_CLICK_TO_SHOW_RANGE_INFO";
	public static final String DRAW = "DRAW";
	public static final String EXPORT = "EXPORT";
	public static final String FAILED_TO_APPLY_SETTINGS = "FAILED_TO_APPLY_SETTINGS";
	public static final String HIDE = "HIDE";
	public static final String HIDE_RANGE_SELECTOR_UI = "HIDE_RANGE_SELECTOR_UI";
	public static final String ID = "ID";
	public static final String IMPORT = "IMPORT";
	public static final String LABEL = "LABEL";
	public static final String LABEL_NOT_SET = "LABEL_NOT_SET";
	public static final String LEGEND_POSITION = "LEGEND_POSITION";
	public static final String LEGEND_POSITION_X = "LEGEND_POSITION_X";
	public static final String LEGEND_POSITION_Y = "LEGEND_POSITION_Y";
	public static final String LINE = "LINE";
	public static final String MAP_ALL_LISTED_SERIES = "MAP_ALL_LISTED_SERIES";
	public static final String MAPPED_SERIES_SETTINGS = "MAPPED_SERIES_SETTINGS";
	public static final String MAPPING_STATUS = "MAPPING_STATUS";
	public static final String MAPPINGS = "MAPPINGS";
	public static final String MAPPINGS_EXPORTED = "MAPPINGS_EXPORTED";
	public static final String MAPPINGS_IMPORTED = "MAPPINGS_IMPORTED";
	public static final String MAPPINGS_SAVED = "MAPPINGS_SAVED";
	public static final String MOVE_LEGEND_DOWN = "MOVE_LEGEND_DOWN";
	public static final String MOVE_LEGEND_LEFT = "MOVE_LEGEND_LEFT";
	public static final String MOVE_LEGEND_RIGHT = "MOVE_LEGEND_RIGHT";
	public static final String MOVE_LEGEND_UP = "MOVE_LEGEND_UP";
	public static final String NEW_MAPPING_ADDED = "NEW_MAPPING_ADDED";
	public static final String NONE = "NONE";
	public static final String NOT_SET = "NOT_SET";
	public static final String OPEN_SETTINGS_PAGE = "OPEN_SETTINGS_PAGE";
	public static final String POSITION_USING_MOUSE = "POSITION_USING_MOUSE";
	public static final String REALLY_DELETE_ALL_MAPPINGS = "REALLY_DELETE_ALL_MAPPINGS";
	public static final String REALLY_DELETE_SELECTED_MAPPING = "REALLY_DELETE_SELECTED_MAPPING";
	public static final String RESET = "RESET";
	public static final String RESET_RANGE = "RESET_RANGE";
	public static final String SAVE_MAPPINGS = "SAVE_MAPPINGS";
	public static final String SCATTER = "SCATTER";
	public static final String SELECTED_MAPPING_DELETED = "SELECTED_MAPPING_DELETED";
	public static final String SERIES_POPUP_MENU = "SERIES_POPUP_MENU";
	public static final String SET = "SET";
	public static final String SET_CURRENT_SELECTION = "SET_CURRENT_SELECTION";
	public static final String SET_LEGEND_POSITION = "SET_LEGEND_POSITION";
	public static final String SETTINGS = "SETTINGS";
	public static final String SORT_TABLE = "SORT_TABLE";
	public static final String TOGGLE_VISIBILITY = "TOGGLE_VISIBILITY";
	public static final String TOGGLE_VISIBILITY_OF_EMBEDDED_LEGEND = "TOGGLE_VISIBILITY_OF_EMBEDDED_LEGEND";
	public static final String TRANSFER_MAPPINGS_OF_SELECTED_SERIES = "TRANSFER_MAPPINGS_OF_SELECTED_SERIES";
	public static final String UNDETERMINED_SERIES_TYPE = "UNDETERMINED_SERIES_TYPE";
	public static final String VISIBLE = "VISIBLE";
	public static final String VISIBLE_IN_LEGEND = "VISIBLE_IN_LEGEND";
	public static final String X_AXIS = "X_AXIS";
	public static final String Y_AXIS = "Y_AXIS";

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
