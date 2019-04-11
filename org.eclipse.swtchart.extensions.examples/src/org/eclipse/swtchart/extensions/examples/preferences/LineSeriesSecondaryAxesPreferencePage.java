/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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

public class LineSeriesSecondaryAxesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public LineSeriesSecondaryAxesPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Set the secondary axis settings.");
	}

	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("X-Axis", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new StringFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_TITLE, "Secondary X-Axis Title:", getFieldEditorParent()));
		addField(new StringFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_DESCRIPTION, "Secondary X-Axis Description:", getFieldEditorParent()));
		addField(new StringFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_DECIMAL_FORMAT_PATTERN, "Secondary X-Axis Format Pattern:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_DECIMAL_FORMAT_LOCALE, "Secondary X-Axis Format Locale:", PreferenceSupport.LOCALES, getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_COLOR, "Secondary X-Axis Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_POSITION, "Secondary X-Axis Position:", PreferenceSupport.AXIS_POSITIONS, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_VISIBLE, "Secondary X-Axis Visible", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_GRID_LINE_STYLE, "Secondary X-Axis Grid Line Style:", PreferenceSupport.LINE_STYLES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_ENABLE_LOG_SCALE, "Secondary X-Axis Enable Log Scale", getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_X_AXIS_EXTRA_SPACE_TITLE, "Secondary X-Axis Extra Space Title:", getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Y-Axis", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new StringFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_TITLE, "Secondary Y-Axis Title:", getFieldEditorParent()));
		addField(new StringFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DESCRIPTION, "Secondary Y-Axis Description:", getFieldEditorParent()));
		addField(new StringFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_PATTERN, "Secondary Y-Axis Format Pattern:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_DECIMAL_FORMAT_LOCALE, "Secondary Y-Axis Format Locale:", PreferenceSupport.LOCALES, getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_COLOR, "Secondary Y-Axis Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_POSITION, "Secondary Y-Axis Position:", PreferenceSupport.AXIS_POSITIONS, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_VISIBLE, "Secondary Y-Axis Visible", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_GRID_LINE_STYLE, "Secondary Y-Axis Grid Line Style:", PreferenceSupport.LINE_STYLES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_ENABLE_LOG_SCALE, "Secondary Y-Axis Enable Log Scale", getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_EXTRA_SPACE_TITLE, "Secondary Y-Axis Extra Space Title:", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {

	}
}