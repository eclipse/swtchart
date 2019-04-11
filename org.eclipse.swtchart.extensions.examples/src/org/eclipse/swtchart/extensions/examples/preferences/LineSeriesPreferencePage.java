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
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swtchart.extensions.examples.Activator;
import org.eclipse.swtchart.extensions.preferences.PreferenceSupport;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LineSeriesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public LineSeriesPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Line Series chart settings.");
	}

	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Chart Settings", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_RANGE_SELECTOR, "Enable Range Selector", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SHOW_RANGE_SELECTOR_INITIALLY, "Show Range Selector Initially", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_COLOR_HINT_RANGE_SELECTOR, "Color Hint Range Selector:", getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_X, "Range Selector Default Axis X (Index):", getFieldEditorParent()));
		addField(new IntegerFieldEditor(LineSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_Y, "Range Selector Default Axis Y (Index):", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_VERTICAL_SLIDER_VISIBLE, "Vertical Slider Visible (see Bug #511257)", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_HORIZONTAL_SLIDER_VISIBLE, "Horizontal Slider Visible", getFieldEditorParent()));
		addField(new StringFieldEditor(LineSeriesPreferenceConstants.P_TITLE, "Title:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_TITLE_VISIBLE, "Title Visible", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_TITLE_COLOR, "Title Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_LEGEND_POSITION, "Legend Position:", PreferenceSupport.LEGEND_POSITIONS, getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_LEGEND_VISIBLE, "Legend Visible", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_ORIENTATION, "Orientation:", PreferenceSupport.ORIENTATIONS, getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_BACKGROUND, "Background:", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_BACKGROUND_CHART, "Background Chart:", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_BACKGROUND_PLOT_AREA, "Background Plot Area:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ENABLE_COMPRESS, "Enable Compress", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ZERO_X, "Zero X", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_ZERO_Y, "Zero Y", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_RESTRICT_ZOOM, "Restrict Zoom", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_X_ZOOM_ONLY, "X Zoom Only", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_Y_ZOOM_ONLY, "Y Zoom Only", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_FORCE_ZERO_MIN_Y, "Force Zero Min Y", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_EXTEND_TYPE_X, "Extend Type X:", PreferenceSupport.EXTEND_TYPES, getFieldEditorParent()));
		addField(new DoubleFieldEditor(LineSeriesPreferenceConstants.P_EXTEND_MIN_X, "Extend Min X:", getFieldEditorParent()));
		addField(new DoubleFieldEditor(LineSeriesPreferenceConstants.P_EXTEND_MAX_X, "Extend Max X:", getFieldEditorParent()));
		addField(new ComboFieldEditor(LineSeriesPreferenceConstants.P_EXTEND_TYPE_Y, "Extend Type Y:", PreferenceSupport.EXTEND_TYPES, getFieldEditorParent()));
		addField(new DoubleFieldEditor(LineSeriesPreferenceConstants.P_EXTEND_MIN_Y, "Extend Min Y:", getFieldEditorParent()));
		addField(new DoubleFieldEditor(LineSeriesPreferenceConstants.P_EXTEND_MAX_Y, "Extend Max Y:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SHOW_POSITION_MARKER, "Show Position Marker", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_COLOR_POSITION_MARKER, "Color Position Marker:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SHOW_PLOT_CENTER_MARKER, "Show Plot Center Marker", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_COLOR_PLOT_CENTER_MARKER, "Color Plot Center Marker:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SHOW_LEGEND_MARKER, "Show Legend Marker", getFieldEditorParent()));
		addField(new ColorFieldEditor(LineSeriesPreferenceConstants.P_COLOR_LEGEND_MARKER, "Color Legend Marker:", getFieldEditorParent()));
		/*
		 * Not needed here.
		 */
		FieldEditor fieldEditor1;
		FieldEditor fieldEditor2;
		FieldEditor fieldEditor3;
		FieldEditor fieldEditor4;
		addField(fieldEditor1 = new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SHOW_AXIS_ZERO_MARKER, "Show Axis Zero Marker", getFieldEditorParent()));
		addField(fieldEditor2 = new ColorFieldEditor(LineSeriesPreferenceConstants.P_COLOR_AXIS_ZERO_MARKER, "Color Axis Zero Marker:", getFieldEditorParent()));
		addField(fieldEditor3 = new BooleanFieldEditor(LineSeriesPreferenceConstants.P_SHOW_SERIES_LABEL_MARKER, "Show Series Label Marker", getFieldEditorParent()));
		addField(fieldEditor4 = new ColorFieldEditor(LineSeriesPreferenceConstants.P_COLOR_SERIES_LABEL_MARKER, "Color Series Label Marker:", getFieldEditorParent()));
		fieldEditor1.setEnabled(false, getFieldEditorParent());
		fieldEditor2.setEnabled(false, getFieldEditorParent());
		fieldEditor3.setEnabled(false, getFieldEditorParent());
		fieldEditor4.setEnabled(false, getFieldEditorParent());
		//
		addField(new BooleanFieldEditor(LineSeriesPreferenceConstants.P_CREATE_MENU, "Create Menu", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {

	}
}