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
package org.eclipse.swtchart.extensions.core;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.dialogs.AbstractSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.BarSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.CircularSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.CreateSeriesMappingDialog;
import org.eclipse.swtchart.extensions.dialogs.LineSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.ScatterSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.internal.mappings.MappingsIO;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;

public class MappingsDialog extends Dialog {

	private AtomicReference<MappingsListUI> listControl = new AtomicReference<>();
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();

	public MappingsDialog(Shell shell) {

		super(shell);
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText(MappedSeriesSettings.DESCRIPTION);
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
		createMappingsList(composite);
		//
		updateInput();
		//
		return composite;
	}

	private void createMappingsList(Composite parent) {

		MappingsListUI mappingsListUI = new MappingsListUI(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		Table table = mappingsListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				Object object = mappingsListUI.getStructuredSelection().getFirstElement();
				if(object instanceof MappedSeriesSettings) {
					/*
					 * Series
					 */
					MappedSeriesSettings mappedSeriesSettings = (MappedSeriesSettings)object;
					ISeriesSettings seriesSettings = mappedSeriesSettings.getSeriesSettings();
					Shell shell = e.display.getActiveShell();
					AbstractSeriesSettingsDialog<?> settingsDialog = null;
					/*
					 * Dialog
					 */
					if(seriesSettings instanceof IBarSeriesSettings) {
						IBarSeriesSettings settings = (IBarSeriesSettings)seriesSettings;
						settingsDialog = new BarSeriesSettingsDialog(shell, settings);
					} else if(seriesSettings instanceof ICircularSeriesSettings) {
						ICircularSeriesSettings settings = (ICircularSeriesSettings)seriesSettings;
						settingsDialog = new CircularSeriesSettingsDialog(shell, settings);
					} else if(seriesSettings instanceof ILineSeriesSettings) {
						ILineSeriesSettings settings = (ILineSeriesSettings)seriesSettings;
						settingsDialog = new LineSeriesSettingsDialog(shell, settings);
					} else if(seriesSettings instanceof IScatterSeriesSettings) {
						IScatterSeriesSettings settings = (IScatterSeriesSettings)seriesSettings;
						settingsDialog = new ScatterSeriesSettingsDialog(shell, settings);
					}
					/*
					 * Apply
					 */
					if(settingsDialog != null) {
						if(settingsDialog.open() == Window.OK) {
							/*
							 * No further action is required here.
							 */
						}
					}
				}
			}
		});
		//
		listControl.set(mappingsListUI);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));
		//
		createButtonAdd(composite);
		createButtonDelete(composite);
		createButtonDeleteAll(composite);
		createButtonImport(composite);
		createButtonExport(composite);
		createButtonSave(composite);
	}

	private Button createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Add a mapping.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_ADD));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				CreateSeriesMappingDialog dialog = new CreateSeriesMappingDialog(e.display.getActiveShell());
				if(dialog.open() == Window.OK) {
					MappingsType mappingsType = dialog.getMappingsType();
					String id = dialog.getRegularExpression();
					ISeriesSettings seriesSettings = MappingsSupport.createSeriesSettings(mappingsType);
					if(seriesSettings != null) {
						seriesSettings.setDescription(dialog.getDescription());
						SeriesMapper.put(mappingsType, id, seriesSettings);
						updateInput();
					}
				}
			}
		});
		//
		return button;
	}

	private Button createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete the selected mappings.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_DELETE));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				MessageBox messageBox = new MessageBox(e.display.getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messageBox.setText(MappedSeriesSettings.DESCRIPTION);
				messageBox.setMessage("Would you like to delete the selected mappings?");
				int decision = messageBox.open();
				if(SWT.YES == decision) {
					Iterator<?> iterator = listControl.get().getStructuredSelection().iterator();
					while(iterator.hasNext()) {
						Object object = iterator.next();
						if(object instanceof MappedSeriesSettings) {
							MappedSeriesSettings mappedSeriesSettings = (MappedSeriesSettings)object;
							SeriesMapper.remove(mappedSeriesSettings.getMappingsType(), mappedSeriesSettings.getIdentifier());
						}
					}
					updateInput();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonDeleteAll(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete all mappings.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_DELETE_ALL));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				MessageBox messageBox = new MessageBox(e.display.getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messageBox.setText(MappedSeriesSettings.DESCRIPTION);
				messageBox.setMessage("Would you like to delete all mappings?");
				int decision = messageBox.open();
				if(SWT.YES == decision) {
					SeriesMapper.clear();
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
		button.setToolTipText("Import");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_IMPORT));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(e.display.getActiveShell(), SWT.READ_ONLY);
				fileDialog.setText(MappedSeriesSettings.DESCRIPTION);
				fileDialog.setFilterExtensions(new String[]{MappedSeriesSettings.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{MappedSeriesSettings.FILTER_NAME});
				fileDialog.setFilterPath(preferenceStore.getString(PreferenceConstants.P_PATH_MAPPINGS_IMPORT));
				String path = fileDialog.open();
				if(path != null) {
					preferenceStore.setValue(PreferenceConstants.P_PATH_MAPPINGS_IMPORT, fileDialog.getFilterPath());
					ResourceSupport.savePreferenceStore();
					File file = new File(path);
					Map<String, ISeriesSettings> mappings = MappingsIO.importSettings(file);
					for(Map.Entry<String, ISeriesSettings> mapping : mappings.entrySet()) {
						ISeriesSettings seriesSettings = mapping.getValue();
						SeriesMapper.put(mapping.getKey(), seriesSettings);
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
		button.setToolTipText("Export");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_EXPORT));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog fileDialog = new FileDialog(e.display.getActiveShell(), SWT.SAVE);
				fileDialog.setOverwrite(true);
				fileDialog.setText(MappedSeriesSettings.DESCRIPTION);
				fileDialog.setFilterExtensions(new String[]{MappedSeriesSettings.FILTER_EXTENSION});
				fileDialog.setFilterNames(new String[]{MappedSeriesSettings.FILTER_NAME});
				fileDialog.setFileName(MappedSeriesSettings.FILE_NAME);
				fileDialog.setFilterPath(preferenceStore.getString(PreferenceConstants.P_PATH_MAPPINGS_EXPORT));
				String path = fileDialog.open();
				if(path != null) {
					preferenceStore.setValue(PreferenceConstants.P_PATH_MAPPINGS_EXPORT, fileDialog.getFilterPath());
					ResourceSupport.savePreferenceStore();
					File file = new File(path);
					MappingsIO.exportSettings(file, SeriesMapper.getMappings());
				}
			}
		});
		//
		return button;
	}

	private Button createButtonSave(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Save the mappings so that they are available after restart.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_SAVE));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				MappingsIO.persistsSettings(SeriesMapper.getMappings());
			}
		});
		//
		return button;
	}

	private void updateInput() {

		listControl.get().setInput(SeriesMapper.getMappings());
	}
}