/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
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
import org.eclipse.swtchart.extensions.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		//
		preferenceStore.setDefault(PreferenceConstants.P_MOVE_LEGEND_X, PreferenceConstants.DEF_MOVE_LEGEND_X);
		preferenceStore.setDefault(PreferenceConstants.P_MOVE_LEGEND_Y, PreferenceConstants.DEF_MOVE_LEGEND_Y);
		preferenceStore.setDefault(PreferenceConstants.P_LEGEND_POSITION_X, PreferenceConstants.DEF_LEGEND_POSITION_X);
		preferenceStore.setDefault(PreferenceConstants.P_LEGEND_POSITION_Y, PreferenceConstants.DEF_LEGEND_POSITION_Y);
		preferenceStore.setDefault(PreferenceConstants.P_LEGEND_COLUMN_ORDER, PreferenceConstants.DEF_LEGEND_COLUMN_ORDER);
	}
}
