/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.dialogs;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.IEnumLabel;
import org.eclipse.swtchart.extensions.core.MappingsType;
import org.eclipse.swtchart.extensions.widgets.ExtendedComboViewer;

public class CreateSeriesMappingDialog extends TitleAreaDialog {

	private String description = "";
	private MappingsType mappingsType = MappingsType.NONE;
	private String regularExpression = "";

	public CreateSeriesMappingDialog(Shell parentShell) {

		super(parentShell);
	}

	@Override
	public void create() {

		super.create();
		setTitle("Series Mapping");
		setMessage("Create a new series mapping.");
	}

	public String getDescription() {

		return description;
	}

	public MappingsType getMappingsType() {

		return mappingsType;
	}

	public String getRegularExpression() {

		return regularExpression;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite)super.createDialogArea(parent);
		//
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		//
		createSectionDescription(composite);
		createSectionType(composite);
		createSectionRegex(composite);
		//
		initialize();
		return container;
	}

	private void initialize() {

		validate();
	}

	private void createSectionDescription(Composite parent) {

		createLabel(parent, "Description");
		createTextDescription(parent);
	}

	private void createSectionType(Composite parent) {

		createLabel(parent, "Mappings Type");
		createComboViewerMappingsType(parent);
	}

	private void createSectionRegex(Composite parent) {

		createLabel(parent, "Regular Expression");
		createTextRegularExpression(parent);
	}

	private Text createTextDescription(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				description = text.getText().trim();
				validate();
			}
		});
		//
		return text;
	}

	private ComboViewer createComboViewerMappingsType(Composite parent) {

		ComboViewer comboViewer = new ExtendedComboViewer(parent, SWT.READ_ONLY);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setInput(MappingsType.values());
		comboViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof IEnumLabel) {
					return ((IEnumLabel)element).label();
				}
				//
				return null;
			}
		});
		//
		Combo combo = comboViewer.getCombo();
		combo.setToolTipText("Select the mappings type.");
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Object object = comboViewer.getStructuredSelection().getFirstElement();
				if(object instanceof MappingsType) {
					mappingsType = (MappingsType)object;
				}
				validate();
			}
		});
		//
		comboViewer.setSelection(new StructuredSelection(mappingsType));
		//
		return comboViewer;
	}

	private Text createTextRegularExpression(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				regularExpression = text.getText().trim();
				validate();
			}
		});
		//
		return text;
	}

	private void createLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}

	private void validate() {

		String message = null;
		if(MappingsType.NONE.equals(mappingsType)) {
			message = "Please select a valid mappings type.";
		}
		//
		if(regularExpression == null || regularExpression.isEmpty()) {
			message = "Please type in a regular expression.";
		} else {
			try {
				Pattern.compile(regularExpression);
			} catch(Exception e) {
				message = "The regular expression is not valid.";
			}
		}
		//
		setErrorMessage(message);
	}
}