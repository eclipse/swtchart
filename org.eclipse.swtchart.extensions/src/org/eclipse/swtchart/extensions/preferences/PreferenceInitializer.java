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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swtchart.extensions.core.ResourceSupport;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
		if(preferenceStore != null) {
			preferenceStore.setDefault(PreferenceConstants.P_BUFFER_SELECTION, PreferenceConstants.DEF_BUFFER_SELECTION);
			preferenceStore.setDefault(PreferenceConstants.P_KEEP_SERIES_DESCRIPTION, PreferenceConstants.DEF_KEEP_SERIES_DESCRIPTION);
			preferenceStore.setDefault(PreferenceConstants.P_MOVE_LEGEND_X, PreferenceConstants.DEF_MOVE_LEGEND_X);
			preferenceStore.setDefault(PreferenceConstants.P_MOVE_LEGEND_Y, PreferenceConstants.DEF_MOVE_LEGEND_Y);
			preferenceStore.setDefault(PreferenceConstants.P_LEGEND_POSITION_X, PreferenceConstants.DEF_LEGEND_POSITION_X);
			preferenceStore.setDefault(PreferenceConstants.P_LEGEND_POSITION_Y, PreferenceConstants.DEF_LEGEND_POSITION_Y);
			preferenceStore.setDefault(PreferenceConstants.P_SORT_LEGEND_TABLE, PreferenceConstants.DEF_SORT_LEGEND_TABLE);
			preferenceStore.setDefault(PreferenceConstants.P_LEGEND_COLUMN_ORDER, PreferenceConstants.DEF_LEGEND_COLUMN_ORDER);
			preferenceStore.setDefault(PreferenceConstants.P_CUSTOM_SERIES_COLUMN_ORDER, PreferenceConstants.DEF_CUSTOM_SERIES_COLUMN_ORDER);
			preferenceStore.setDefault(PreferenceConstants.P_SERIES_MAPPINGS, PreferenceConstants.DEF_SERIES_MAPPINGS);
			preferenceStore.setDefault(PreferenceConstants.P_PATH_MAPPINGS_IMPORT, PreferenceConstants.DEF_PATH_MAPPINGS_IMPORT);
			preferenceStore.setDefault(PreferenceConstants.P_PATH_MAPPINGS_EXPORT, PreferenceConstants.DEF_PATH_MAPPINGS_EXPORT);
			preferenceStore.setDefault(PreferenceConstants.P_BITMAP_EXPORT_CUSTOM_SIZE, PreferenceConstants.DEF_BITMAP_EXPORT_CUSTOM_SIZE);
			preferenceStore.setDefault(PreferenceConstants.P_BITMAP_EXPORT_WIDTH, PreferenceConstants.DEF_BITMAP_EXPORT_WIDTH);
			preferenceStore.setDefault(PreferenceConstants.P_BITMAP_EXPORT_HEIGHT, PreferenceConstants.DEF_BITMAP_EXPORT_HEIGHT);
			preferenceStore.setDefault(PreferenceConstants.P_SHOW_HELP_FOR_EVENTS, PreferenceConstants.DEF_SHOW_HELP_FOR_EVENTS);
			preferenceStore.setDefault(PreferenceConstants.P_HELP_POPUP_TIME_TO_CLOSE, PreferenceConstants.DEF_HELP_POPUP_TIME_TO_CLOSE);
		}
	}
}