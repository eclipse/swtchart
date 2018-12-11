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
import org.eclipse.swtchart.extensions.examples.Activator;
import org.eclipse.swtchart.extensions.preferences.PreferenceSupport;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ScatterSeriesDataPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ScatterSeriesDataPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Set the data series settings.");
	}

	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Scatter Series", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new IntegerFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_SIZE_SERIES, "Symbol Size:", getFieldEditorParent()));
		//
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_LEFT_TOP, "Symbol Color (Left Top):", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_LEFT_TOP, "Symbol Type (Left Top):", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_LEFT_TOP, "Visible (Left Top)", getFieldEditorParent()));
		//
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_RIGHT_TOP, "Symbol Color (Right Top):", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_RIGHT_TOP, "Symbol Type (Right Top):", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_RIGHT_TOP, "Visible (Right Top)", getFieldEditorParent()));
		//
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_LEFT_BOTTOM, "Symbol Color (Left Bottom):", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_LEFT_BOTTOM, "Symbol Type (Left Bottom):", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_LEFT_BOTTOM, "Visible (Left Bottom)", getFieldEditorParent()));
		//
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_RIGHT_BOTTOM, "Symbol Color (Right Bottom):", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_RIGHT_BOTTOM, "Symbol Type (Right Bottom):", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_RIGHT_BOTTOM, "Visible (Right Bottom)", getFieldEditorParent()));
		//
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_HIGHLIGHT_LEFT_TOP, "Symbol Color (Left Top):", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_HIGHLIGHT_LEFT_TOP, "Symbol Type (Left Top):", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_HIGHLIGHT_LEFT_TOP, "Visible (Left Top)", getFieldEditorParent()));
		//
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_HIGHLIGHT_RIGHT_TOP, "Symbol Color (Right Top):", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_HIGHLIGHT_RIGHT_TOP, "Symbol Type (Right Top):", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_HIGHLIGHT_RIGHT_TOP, "Visible (Right Top)", getFieldEditorParent()));
		//
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_HIGHLIGHT_LEFT_BOTTOM, "Symbol Color (Left Bottom):", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_HIGHLIGHT_LEFT_BOTTOM, "Symbol Type (Left Bottom):", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_HIGHLIGHT_LEFT_BOTTOM, "Visible (Left Bottom)", getFieldEditorParent()));
		//
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_COLOR_SERIES_HIGHLIGHT_RIGHT_BOTTOM, "Symbol Color (Right Bottom):", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_SYMBOL_TYPE_SERIES_HIGHLIGHT_RIGHT_BOTTOM, "Symbol Type (Right Bottom):", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_VISIBLE_SERIES_HIGHLIGHT_RIGHT_BOTTOM, "Visible (Right Bottom)", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {

	}
}