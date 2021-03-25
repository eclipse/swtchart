/*******************************************************************************
 * Copyright (c) 2020, 2021 Lablicate GmbH.
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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {

		super(GRID);
		IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
		if(preferenceStore != null) {
			setPreferenceStore(preferenceStore);
		}
		//
		setTitle("SWTChart");
		setDescription("");
	}

	public void createFieldEditors() {

		addField(new IntegerFieldEditor(PreferenceConstants.P_MOVE_LEGEND_X, "Move Legend [X]", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_MOVE_LEGEND_Y, "Move Legend [Y]", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_LEGEND_POSITION_X, "Legend Position [X]", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_LEGEND_POSITION_Y, "Legend Position [Y]", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_SORT_LEGEND_TABLE, "Sort Legend Table", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_LEGEND_COLUMN_ORDER, "Sort Order Columns Legend", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {

		PreferenceInitializer preferenceInitializer = new PreferenceInitializer();
		preferenceInitializer.initializeDefaultPreferences();
	}
}
