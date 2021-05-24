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
package org.eclipse.swtchart.extensions.core;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.internal.support.MappingsIO;
import org.eclipse.swtchart.extensions.internal.support.MappingsSupport;
import org.eclipse.swtchart.extensions.internal.support.SeriesMapper;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;

public class MappingsDialog extends Dialog {

	private static final String DESCRIPTION = "Mappings";
	private static final String IMPORT = "Import " + DESCRIPTION;
	private static final String EXPORT = "Export " + DESCRIPTION;
	private static final String FILTER_EXTENSION = "*.txt";
	private static final String FILTER_NAME = "SWTChart Mappings (*.txt)";
	private static final String FILE_NAME = "SWTChartMappings.txt";
	//
	private MappingsListUI mappingsListUI;
	private ScrollableChart scrollableChart;
	//
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();

	public MappingsDialog(Shell shell, ScrollableChart scrollableChart) {

		super(shell);
		this.scrollableChart = scrollableChart;
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText(DESCRIPTION);
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	@Override
	protected Point getInitialSize() {

		return new Point(600, 500);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite)super.createDialogArea(parent);
		//
		createToolbarMain(composite);
		mappingsListUI = createMappingsList(composite);
		//
		updateInput();
		//
		return composite;
	}

	private MappingsListUI createMappingsList(Composite parent) {

		MappingsListUI mappingsListUI = new MappingsListUI(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		mappingsListUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		return mappingsListUI;
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(4, false));
		//
		createButtonReset(composite);
		createButtonResetAll(composite);
		createButtonImport(composite);
		createButtonExport(composite);
	}

	private Button createButtonReset(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Reset the selected mappings.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_DELETE));
		button.addSelectionListener(new SelectionAdapter() {

			@SuppressWarnings({"rawtypes", "unchecked"})
			@Override
			public void widgetSelected(SelectionEvent e) {

				MessageBox messageBox = new MessageBox(e.display.getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messageBox.setText(DESCRIPTION);
				messageBox.setMessage("Would you like to reset the selected mappings?");
				int decision = messageBox.open();
				if(SWT.YES == decision) {
					Iterator iterator = mappingsListUI.getStructuredSelection().iterator();
					while(iterator.hasNext()) {
						Object object = iterator.next();
						if(object instanceof Map.Entry) {
							Map.Entry<String, ISeriesSettings> entry = (Map.Entry<String, ISeriesSettings>)object;
							SeriesMapper.reset(entry.getKey());
						}
					}
					updateInput();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonResetAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Reset all mappings.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_DELETE_ALL));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				MessageBox messageBox = new MessageBox(e.display.getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messageBox.setText(DESCRIPTION);
				messageBox.setMessage("Would you like to reset all mappings?");
				int decision = messageBox.open();
				if(SWT.YES == decision) {
					SeriesMapper.reset();
					updateInput();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonImport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(IMPORT);
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_IMPORT));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(e.display.getActiveShell(), SWT.READ_ONLY);
				fileDialog.setText(IMPORT);
				fileDialog.setFilterExtensions(new String[]{FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{FILTER_NAME});
				fileDialog.setFilterPath(preferenceStore.getString(PreferenceConstants.P_PATH_MAPPINGS_IMPORT));
				String path = fileDialog.open();
				if(path != null) {
					preferenceStore.putValue(PreferenceConstants.P_PATH_MAPPINGS_IMPORT, fileDialog.getFilterPath());
					File file = new File(path);
					Map<String, ISeriesSettings> mappings = MappingsIO.importSettings(file);
					for(Map.Entry<String, ISeriesSettings> mapping : mappings.entrySet()) {
						/*
						 * Map the settings
						 */
						String id = mapping.getKey();
						ISeriesSettings seriesSettings = mapping.getValue();
						ISeriesSettings seriesSettingsDefault = SeriesMapper.getSeriesSettingsDefault(id, scrollableChart);
						SeriesMapper.mapSetting(id, seriesSettings, seriesSettingsDefault);
					}
					updateInput();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonExport(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(EXPORT);
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_EXPORT));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(e.display.getActiveShell(), SWT.SAVE);
				fileDialog.setOverwrite(true);
				fileDialog.setText(EXPORT);
				fileDialog.setFilterExtensions(new String[]{FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{FILTER_NAME});
				fileDialog.setFileName(FILE_NAME);
				fileDialog.setFilterPath(preferenceStore.getString(PreferenceConstants.P_PATH_MAPPINGS_EXPORT));
				String path = fileDialog.open();
				if(path != null) {
					preferenceStore.putValue(PreferenceConstants.P_PATH_MAPPINGS_EXPORT, fileDialog.getFilterPath());
					File file = new File(path);
					MappingsIO.exportSettings(file, SeriesMapper.getMappings());
				}
			}
		});
		//
		return button;
	}

	private void updateInput() {

		MappingsSupport.adjustSettings(scrollableChart);
		mappingsListUI.setInput(SeriesMapper.getMappings());
	}
}
