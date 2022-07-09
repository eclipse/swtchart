/*******************************************************************************
 * Copyright (c) 2017, 2022 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.examples.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swtchart.extensions.examples.Activator;
import org.eclipse.swtchart.extensions.preferences.PreferenceSupport;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class BarSeriesSecondaryAxesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public BarSeriesSecondaryAxesPreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Secondary axis settings");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Y-Axis", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new StringFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_TITLE, "Secondary Y-Axis Title:", getFieldEditorParent()));
		addField(new StringFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DESCRIPTION, "Secondary Y-Axis Description:", getFieldEditorParent()));
		addField(new StringFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_PATTERN, "Secondary Y-Axis Format Pattern:", getFieldEditorParent()));
		addField(new ComboFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_LOCALE, "Secondary Y-Axis Format Locale:", PreferenceSupport.LOCALES, getFieldEditorParent()));
		addField(new ColorFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_COLOR, "Secondary Y-Axis Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_POSITION, "Secondary Y-Axis Position:", PreferenceSupport.AXIS_POSITIONS, getFieldEditorParent()));
		addField(new BooleanFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_VISIBLE, "Secondary Y-Axis Visible", getFieldEditorParent()));
		addField(new ComboFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_GRID_LINE_STYLE, "Secondary Y-Axis Grid Line Style:", PreferenceSupport.LINE_STYLES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_ENABLE_LOG_SCALE, "Secondary Y-Axis Enable Log Scale", getFieldEditorParent()));
		addField(new DoubleFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_LOG_SCALE_BASE, "Secondary Y-Axis Log Scale Base", getFieldEditorParent()));
		addField(new IntegerFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_EXTRA_SPACE_TITLE, "Secondary Y-Axis Extra Space Title:", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}