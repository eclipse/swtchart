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
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu.export;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IExtendedChart;

public class ExportSettingsDialog extends TitleAreaDialog {

	private BaseChart baseChart;
	//
	private Combo comboScaleX;
	private Combo comboScaleY;
	private Combo comboExportOption;
	//
	private int indexAxisX;
	private int indexAxisY;
	private boolean exportVisibleOnly;
	//
	private static final String SERIES_ALL = "All Series";
	private static final String SERIES_VISIBLE = "Visible Series";
	private String[] exportOptions;

	public ExportSettingsDialog(Shell parent, BaseChart baseChart) {
		super(parent);
		this.baseChart = baseChart;
		exportOptions = new String[]{SERIES_ALL, SERIES_VISIBLE};
	}

	@Override
	public void create() {

		super.create();
		setTitle("Export Axis Selection");
		setMessage("Select the X and Y axis you'd like to export.", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite)super.createDialogArea(parent);
		Composite container = new Composite(composite, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		//
		createSelectionAxisX(container);
		createSelectionAxisY(container);
		createExportOptionSelection(container);
		//
		return composite;
	}

	private void createSelectionAxisX(Composite container) {

		Label label = new Label(container, SWT.NONE);
		label.setText("X Axis:");
		//
		String[] axisLabelsX = baseChart.getAxisLabels(IExtendedChart.X_AXIS);
		comboScaleX = new Combo(container, SWT.READ_ONLY);
		comboScaleX.setItems(axisLabelsX);
		comboScaleX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if(axisLabelsX.length > 0) {
			comboScaleX.select(0);
		}
	}

	private void createSelectionAxisY(Composite container) {

		Label label = new Label(container, SWT.NONE);
		label.setText("Y Axis:");
		//
		String[] axisLabelsY = baseChart.getAxisLabels(IExtendedChart.Y_AXIS);
		comboScaleY = new Combo(container, SWT.READ_ONLY);
		comboScaleY.setItems(axisLabelsY);
		comboScaleY.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if(axisLabelsY.length > 0) {
			comboScaleY.select(0);
		}
	}

	private void createExportOptionSelection(Composite container) {

		Label label = new Label(container, SWT.NONE);
		label.setText("Export:");
		//
		comboExportOption = new Combo(container, SWT.READ_ONLY);
		comboExportOption.setItems(exportOptions);
		comboExportOption.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if(exportOptions.length > 0) {
			comboExportOption.select(0);
		}
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	private void saveInput() {

		indexAxisX = comboScaleX.getSelectionIndex();
		indexAxisY = comboScaleY.getSelectionIndex();
		exportVisibleOnly = SERIES_VISIBLE.equals(comboExportOption.getText().trim());
	}

	@Override
	protected void okPressed() {

		saveInput();
		super.okPressed();
	}

	public int getIndexAxisSelectionX() {

		return indexAxisX;
	}

	public int getIndexAxisSelectionY() {

		return indexAxisY;
	}

	public boolean isExportVisibleOnly() {

		return exportVisibleOnly;
	}
}
