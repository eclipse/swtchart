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

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DoubleFieldEditor extends StringFieldEditor {

	private double minValue = 0.0d;
	private double maxValue = Double.MAX_VALUE;

	public DoubleFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	public DoubleFieldEditor(String name, String labelText, double minValue, double maxValue, Composite parent) {
		super(name, labelText, parent);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	protected boolean checkState() {

		Text textControl = getTextControl();
		if(textControl == null) {
			return false;
		}
		String stringValue = textControl.getText();
		Double value;
		try {
			value = Double.valueOf(stringValue);
			if(value >= minValue && value <= maxValue) {
				clearErrorMessage();
				return true;
			} else {
				setAndShowErrorMessage();
			}
		} catch(NumberFormatException e) {
			setAndShowErrorMessage();
		}
		return false;
	}

	@Override
	protected void doLoad() {

		Text textControl = getTextControl();
		if(textControl != null) {
			Double value = getPreferenceStore().getDouble(getPreferenceName());
			textControl.setText(value.toString());
		}
		super.doLoad();
	}

	@Override
	protected void doLoadDefault() {

		Text textControl = getTextControl();
		if(textControl != null) {
			Double value = getPreferenceStore().getDefaultDouble(getPreferenceName());
			textControl.setText(value.toString());
		}
		valueChanged();
	}

	@Override
	protected void doStore() {

		Text textControl = getTextControl();
		if(textControl != null) {
			double value = Double.valueOf(textControl.getText());
			getPreferenceStore().setValue(getPreferenceName(), value);
		}
	}

	private void setAndShowErrorMessage() {

		showErrorMessage("Allowed range (MIN = " + minValue + ")(MAX = " + maxValue + ")");
	}
}
