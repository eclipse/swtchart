/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
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
package org.eclipse.swtchart.export.core;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;

public class BitmapExportSettingsDialog extends TitleAreaDialog {

	private Rectangle boundsMax;
	//
	private boolean customSize = false;
	private int customWidth = 100;
	private int customHeight = 100;
	//
	private Text textWidth;
	private Text textHeight;
	//
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();

	public BitmapExportSettingsDialog(Shell parent) {

		super(parent);
	}

	@Override
	public void create() {

		super.create();
		setTitle("Export Bitmap");
		setMessage("Define the bitmap export settings.");
	}

	public boolean isCustomSize() {

		return customSize;
	}

	public int getCustomWidth() {

		return customWidth;
	}

	public int getCustomHeight() {

		return customHeight;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		boundsMax = Display.getCurrent().getBounds();
		customSize = preferenceStore != null ? preferenceStore.getBoolean(PreferenceConstants.P_BITMAP_EXPORT_CUSTOM_SIZE) : PreferenceConstants.DEF_BITMAP_EXPORT_CUSTOM_SIZE;
		customWidth = preferenceStore != null ? preferenceStore.getInt(PreferenceConstants.P_BITMAP_EXPORT_WIDTH) : PreferenceConstants.DEF_BITMAP_EXPORT_WIDTH;
		customHeight = preferenceStore != null ? preferenceStore.getInt(PreferenceConstants.P_BITMAP_EXPORT_HEIGHT) : PreferenceConstants.DEF_BITMAP_EXPORT_HEIGHT;
		//
		Composite container = (Composite)super.createDialogArea(parent);
		//
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		//
		createLabelInfo(composite);
		createSectionCustomSize(composite);
		textWidth = createSectionWidth(composite);
		textHeight = createSectionHeight(composite);
		//
		updateWidgets();
		//
		return container;
	}

	@Override
	protected void okPressed() {

		if(preferenceStore != null) {
			preferenceStore.setValue(PreferenceConstants.P_BITMAP_EXPORT_CUSTOM_SIZE, customSize);
			preferenceStore.setValue(PreferenceConstants.P_BITMAP_EXPORT_WIDTH, customWidth);
			preferenceStore.setValue(PreferenceConstants.P_BITMAP_EXPORT_HEIGHT, customHeight);
		}
		super.okPressed();
	}

	private void createLabelInfo(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("This display allows max '" + boundsMax.width + " x " + boundsMax.height + "' px");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		label.setLayoutData(gridData);
	}

	private void createSectionCustomSize(Composite parent) {

		Button button = new Button(parent, SWT.CHECK);
		button.setText("Custom Size");
		button.setToolTipText("Activate this to enable the custom size");
		button.setSelection(customSize);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		button.setLayoutData(gridData);
		//
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				customSize = button.getSelection();
				updateWidgets();
			}
		});
	}

	private Text createSectionWidth(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("Custom Width");
		//
		Text text = new Text(parent, SWT.BORDER);
		text.setText(Integer.toString(customWidth));
		text.setToolTipText("Max width: " + boundsMax.width);
		text.setEnabled(customSize);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				try {
					int userWidth = Integer.parseInt(text.getText().trim());
					userWidth = userWidth < 1 ? 1 : userWidth;
					customWidth = userWidth > boundsMax.width ? boundsMax.width : userWidth;
				} catch(NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
		//
		return text;
	}

	private Text createSectionHeight(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("Custom Height");
		//
		Text text = new Text(parent, SWT.BORDER);
		text.setText(Integer.toString(customHeight));
		text.setToolTipText("Max height: " + boundsMax.height);
		text.setEnabled(customSize);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				try {
					int userHeight = Integer.parseInt(text.getText().trim());
					userHeight = userHeight < 1 ? 1 : userHeight;
					customHeight = userHeight > boundsMax.height ? boundsMax.height : userHeight;
				} catch(NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
		//
		return text;
	}

	private void updateWidgets() {

		boolean enabled = customSize;
		textWidth.setEnabled(enabled);
		textHeight.setEnabled(enabled);
	}
}