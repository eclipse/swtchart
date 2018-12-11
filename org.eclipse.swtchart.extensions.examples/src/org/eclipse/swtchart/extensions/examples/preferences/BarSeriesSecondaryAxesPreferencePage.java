/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		setDescription("Set the secondary axis settings.");
	}

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
		addField(new IntegerFieldEditor(BarSeriesPreferenceConstants.P_SECONDARY_Y_AXIS_EXTRA_SPACE_TITLE, "Secondary Y-Axis Extra Space Title:", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {

	}
}