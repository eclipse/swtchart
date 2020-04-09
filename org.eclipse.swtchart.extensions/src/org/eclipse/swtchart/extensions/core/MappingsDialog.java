/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
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

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.internal.support.SeriesMapper;

public class MappingsDialog extends Dialog {

	private MappingsListUI mappingsListUI;

	public MappingsDialog(Shell shell) {
		super(shell);
	}

	@Override
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setText("Setting Mappings");
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
		mappingsListUI = createMappingsList(composite);
		// createToolbarMain(composite);
		updateInput();
		//
		return composite;
	}

	private MappingsListUI createMappingsList(Composite parent) {

		MappingsListUI mappingsListUI = new MappingsListUI(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		mappingsListUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		return mappingsListUI;
	}

	@SuppressWarnings("unused")
	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));
		//
		createButtonDelete(composite);
		createButtonDeleteAll(composite);
	}

	private Button createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete selected mapping(s).");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_DELETE));
		button.addSelectionListener(new SelectionAdapter() {

			@SuppressWarnings({"rawtypes", "unchecked"})
			@Override
			public void widgetSelected(SelectionEvent e) {

				Iterator iterator = mappingsListUI.getStructuredSelection().iterator();
				while(iterator.hasNext()) {
					Object object = iterator.next();
					if(object instanceof Map.Entry) {
						Map.Entry<String, ISeriesSettings> entry = (Map.Entry<String, ISeriesSettings>)object;
						SeriesMapper.remove(entry.getKey());
					}
				}
				updateInput();
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

				SeriesMapper.clear();
				updateInput();
			}
		});
		//
		return button;
	}

	private void updateInput() {

		mappingsListUI.setInput(SeriesMapper.getMappings());
	}
}
