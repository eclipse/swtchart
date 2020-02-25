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

public class LineSeriesDataPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public LineSeriesDataPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Set the data series settings.");
	}

	@Override
	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Line Series 1", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_ANTIALIAS_SERIES_1, "Antialias:", PreferenceSupport.ANTIALIAS_OPTIONS, getFieldEditorParent()));
		addField(new StringFieldEditor(LineSeriesPreferenceConstants.P_DESCRIPTION_SERIES_1, "Description:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_AREA_SERIES_1, "Enable Area", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_STACK_SERIES_1, "Enable Stack", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_STEP_SERIES_1, "Enable Step", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_LINE_COLOR_SERIES_1, "Line Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_LINE_STYLE_SERIES_1, "Line Style:", PreferenceSupport.LINE_STYLES, getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_LINE_WIDTH_SERIES_1, "Line Width:", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_1, "Symbol Color:", getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_SIZE_SERIES_1, "Symbol Size:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_1, "Symbol Type:", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_VISIBLE_SERIES_1, "Visible", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_VISIBLE_IN_LEGEND_SERIES_1, "Visible in Legend", getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Line Series 1 (Highlight)", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_ANTIALIAS_SERIES_1_HIGHLIGHT, "Antialias:", PreferenceSupport.ANTIALIAS_OPTIONS, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_AREA_SERIES_1_HIGHLIGHT, "Enable Area", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_STACK_SERIES_1_HIGHLIGHT, "Enable Stack", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_STEP_SERIES_1_HIGHLIGHT, "Enable Step", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_LINE_COLOR_SERIES_1_HIGHLIGHT, "Line Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_LINE_STYLE_SERIES_1_HIGHLIGHT, "Line Style:", PreferenceSupport.LINE_STYLES, getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_LINE_WIDTH_SERIES_1_HIGHLIGHT, "Line Width:", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_1_HIGHLIGHT, "Symbol Color:", getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_SIZE_SERIES_1_HIGHLIGHT, "Symbol Size:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_1_HIGHLIGHT, "Symbol Type:", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_VISIBLE_SERIES_1_HIGHLIGHT, "Visible", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_VISIBLE_IN_LEGEND_SERIES_1_HIGHLIGHT, "Visible in Legend", getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Line Series 2", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_ANTIALIAS_SERIES_2, "Antialias:", PreferenceSupport.ANTIALIAS_OPTIONS, getFieldEditorParent()));
		addField(new StringFieldEditor(LineSeriesPreferenceConstants.P_DESCRIPTION_SERIES_2, "Description:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_AREA_SERIES_2, "Enable Area", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_STACK_SERIES_2, "Enable Stack", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_STEP_SERIES_2, "Enable Step", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_LINE_COLOR_SERIES_2, "Line Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_LINE_STYLE_SERIES_2, "Line Style:", PreferenceSupport.LINE_STYLES, getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_LINE_WIDTH_SERIES_2, "Line Width:", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_2, "Symbol Color:", getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_SIZE_SERIES_2, "Symbol Size:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_2, "Symbol Type:", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_VISIBLE_SERIES_2, "Visible", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_VISIBLE_IN_LEGEND_SERIES_2, "Visible in Legend", getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Line Series 2 (Highlight)", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_ANTIALIAS_SERIES_2_HIGHLIGHT, "Antialias:", PreferenceSupport.ANTIALIAS_OPTIONS, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_AREA_SERIES_2_HIGHLIGHT, "Enable Area", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_STACK_SERIES_2_HIGHLIGHT, "Enable Stack", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_STEP_SERIES_2_HIGHLIGHT, "Enable Step", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_LINE_COLOR_SERIES_2_HIGHLIGHT, "Line Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_LINE_STYLE_SERIES_2_HIGHLIGHT, "Line Style:", PreferenceSupport.LINE_STYLES, getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_LINE_WIDTH_SERIES_2_HIGHLIGHT, "Line Width:", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_2_HIGHLIGHT, "Symbol Color:", getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_SIZE_SERIES_2_HIGHLIGHT, "Symbol Size:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_2_HIGHLIGHT, "Symbol Type:", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_VISIBLE_SERIES_2_HIGHLIGHT, "Visible", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_VISIBLE_IN_LEGEND_SERIES_2_HIGHLIGHT, "Visible in Legend", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}