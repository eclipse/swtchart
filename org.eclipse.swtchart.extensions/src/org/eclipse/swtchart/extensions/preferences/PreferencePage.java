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

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swtchart.extensions.Activator;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private ScopedPreferenceStore fallbackStore = null;

	public PreferencePage() {
		super(GRID);
		Activator activator = Activator.getDefault();
		if(activator != null) {
			setPreferenceStore(activator.getPreferenceStore());
		} else {
			if(fallbackStore == null) {
				fallbackStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.swtchart.extensions");
			}
			setPreferenceStore(fallbackStore);
		}
		setTitle("SWTChart Extensions");
		setDescription("");
	}

	public void createFieldEditors() {

		addField(new IntegerFieldEditor(PreferenceConstants.P_MOVE_LEGEND_X, "Move Legend [X]", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_MOVE_LEGEND_Y, "Move Legend [Y]", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_LEGEND_POSITION_X, "Legend Position [X]", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_LEGEND_POSITION_Y, "Legend Position [Y]", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_LEGEND_COLUMN_ORDER, "Sort Order Columns Legend", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {

	}
}
