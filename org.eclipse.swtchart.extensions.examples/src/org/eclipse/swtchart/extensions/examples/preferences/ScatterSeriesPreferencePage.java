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

public class ScatterSeriesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ScatterSeriesPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Scatter Series chart settings.");
	}

	@Override
	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Chart Settings", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		//
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_ENABLE_RANGE_SELECTOR, "Enable Range Selector", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_SHOW_RANGE_SELECTOR_INITIALLY, "Show Range Selector Initially", getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_COLOR_HINT_RANGE_SELECTOR, "Color Hint Range Selector:", getFieldEditorParent()));
		addField(new IntegerFieldEditor(ScatterSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_X, "Range Selector Default Axis X (Index):", getFieldEditorParent()));
		addField(new IntegerFieldEditor(ScatterSeriesPreferenceConstants.P_RANGE_SELECTOR_DEFAULT_AXIS_Y, "Range Selector Default Axis Y (Index):", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_VERTICAL_SLIDER_VISIBLE, "Vertical Slider Visible (see Bug #511257)", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_HORIZONTAL_SLIDER_VISIBLE, "Horizontal Slider Visible", getFieldEditorParent()));
		addField(new StringFieldEditor(ScatterSeriesPreferenceConstants.P_TITLE, "Title:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_TITLE_VISIBLE, "Title Visible", getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_TITLE_COLOR, "Title Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_LEGEND_POSITION, "Legend Position:", PreferenceSupport.LEGEND_POSITIONS, getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_LEGEND_VISIBLE, "Legend Visible", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_ORIENTATION, "Orientation:", PreferenceSupport.ORIENTATIONS, getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_BACKGROUND, "Background:", getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_BACKGROUND_CHART, "Background Chart:", getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_BACKGROUND_PLOT_AREA, "Background Plot Area:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_ENABLE_COMPRESS, "Enable Compress", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_ZERO_X, "Zero X", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_ZERO_Y, "Zero Y", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_RESTRICT_ZOOM, "Restrict Zoom", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_X_ZOOM_ONLY, "X Zoom Only", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_Y_ZOOM_ONLY, "Y Zoom Only", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_FORCE_ZERO_MIN_Y, "Force Zero Min Y", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_EXTEND_TYPE_X, "Extend Type X:", PreferenceSupport.EXTEND_TYPES, getFieldEditorParent()));
		addField(new DoubleFieldEditor(ScatterSeriesPreferenceConstants.P_EXTEND_MIN_X, "Extend Min X:", getFieldEditorParent()));
		addField(new DoubleFieldEditor(ScatterSeriesPreferenceConstants.P_EXTEND_MAX_X, "Extend Max X:", getFieldEditorParent()));
		addField(new ComboFieldEditor(ScatterSeriesPreferenceConstants.P_EXTEND_TYPE_Y, "Extend Type Y:", PreferenceSupport.EXTEND_TYPES, getFieldEditorParent()));
		addField(new DoubleFieldEditor(ScatterSeriesPreferenceConstants.P_EXTEND_MIN_Y, "Extend Min Y:", getFieldEditorParent()));
		addField(new DoubleFieldEditor(ScatterSeriesPreferenceConstants.P_EXTEND_MAX_Y, "Extend Max Y:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_SHOW_POSITION_MARKER, "Show Position Marker", getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_COLOR_POSITION_MARKER, "Color Position Marker:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_SHOW_PLOT_CENTER_MARKER, "Show Plot Center Marker", getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_COLOR_PLOT_CENTER_MARKER, "Color Plot Center Marker:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_SHOW_LEGEND_MARKER, "Show Legend Marker", getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_COLOR_LEGEND_MARKER, "Color Legend Marker:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_SHOW_AXIS_ZERO_MARKER, "Show Axis Zero Marker", getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_COLOR_AXIS_ZERO_MARKER, "Color Axis Zero Marker:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_SHOW_SERIES_LABEL_MARKER, "Show Series Label Marker", getFieldEditorParent()));
		addField(new ColorFieldEditor(ScatterSeriesPreferenceConstants.P_COLOR_SERIES_LABEL_MARKER, "Color Series Label Marker:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ScatterSeriesPreferenceConstants.P_CREATE_MENU, "Create Menu", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}