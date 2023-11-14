/*******************************************************************************
 * Copyright (c) 2020, 2023 Lablicate GmbH.
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
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swtchart.extensions.core.ResourceSupport;

public class PreferencePage extends FieldEditorPreferencePage {

	public PreferencePage() {

		this(GRID);
	}

	public PreferencePage(int style) {

		super(style);
		setPreferenceStore(ResourceSupport.getPreferenceStore());
		setTitle("SWTChart"); //$NON-NLS-1$
		setDescription(""); //$NON-NLS-1$
	}

	@Override
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceConstants.P_BUFFER_SELECTION, Messages.getString(Messages.BUFFERED_SELECTION), getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_KEEP_SERIES_DESCRIPTION, Messages.getString(Messages.KEEP_SERIES_DESCRIPTION), getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_MOVE_LEGEND_X, Messages.getString(Messages.MOVE_LEGEND_X), getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_MOVE_LEGEND_Y, Messages.getString(Messages.MOVE_LEGEND_Y), getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_LEGEND_POSITION_X, Messages.getString(Messages.LEGEND_POSITION_X), getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_LEGEND_POSITION_Y, Messages.getString(Messages.LEGEND_POSITION_Y), getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_SORT_LEGEND_TABLE, Messages.getString(Messages.SORT_LEGEND_TABLE), getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_LEGEND_COLUMN_ORDER, Messages.getString(Messages.SORT_ORDER_COLUMNS_LEGEND), getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_CUSTOM_SERIES_COLUMN_ORDER, Messages.getString(Messages.SORT_ORDER_COLUMNS_CUSTOM_SERIES), getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_BITMAP_EXPORT_CUSTOM_SIZE, Messages.getString(Messages.BITMAP_EXPORT_CUSTOM_SIZE), getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_BITMAP_EXPORT_WIDTH, Messages.getString(Messages.BITMAP_EXPORT_WIDTH), getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_BITMAP_EXPORT_HEIGHT, Messages.getString(Messages.BITMAP_EXPORT_HEIGHT), getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_SHOW_HELP_FOR_EVENTS, Messages.getString(Messages.SHOW_POPUP_ON_CLICKBINDING), getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.P_HELP_POPUP_TIME_TO_CLOSE, Messages.getString(Messages.POPUP_CLOSE_TIME), getFieldEditorParent()));
	}
}