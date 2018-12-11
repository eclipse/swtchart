/*******************************************************************************
 * Copyright (c) 2008, 2018 SWTChart project.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.properties;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.extensions.charts.InteractiveChart;

/**
 * Abstract class for properties page.
 */
public abstract class AbstractPage extends PreferencePage {

	/** the chart */
	protected InteractiveChart chart;
	/** the properties resources */
	protected PropertiesResources resources;

	/**
	 * Constructor.
	 * 
	 * @param chart
	 *            the chart
	 * @param resources
	 *            the properties resources
	 * @param title
	 *            the title
	 */
	public AbstractPage(InteractiveChart chart, PropertiesResources resources, String title) {
		this.chart = chart;
		this.resources = resources;
		setTitle(title);
	}

	/*
	 * @see PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {

		if(getControl() != null) {
			apply();
			chart.redraw();
		}
		return true;
	}

	/**
	 * Apply the values specified on controls.
	 */
	abstract public void apply();

	/**
	 * Creates the group control which contains two columns for controls.
	 * 
	 * @param parent
	 *            the parent to create the group control
	 * @param text
	 *            the group name
	 * @param equal
	 *            true if making columns equal width
	 * @return the group
	 */
	protected Group createGroupControl(Composite parent, String text, boolean equal) {

		Group group = new Group(parent, SWT.NULL);
		group.setText(text);
		group.setLayout(new GridLayout(2, equal));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return group;
	}

	/**
	 * Creates the label.
	 * 
	 * @param parent
	 *            the parent to create the label
	 * @param text
	 *            the label text
	 * @return the label
	 */
	protected Label createLabelControl(Composite parent, String text) {

		Label label = new Label(parent, SWT.NULL);
		label.setText(text);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 1;
		label.setLayoutData(gridData);
		return label;
	}

	/**
	 * Creates the color selector.
	 * 
	 * @param parent
	 *            the parent to create the color selector
	 * @return the color selector
	 */
	protected ColorSelector createColorButtonControl(Composite parent) {

		return new ColorSelector(parent);
	}

	/**
	 * Creates the check box.
	 * 
	 * @param parent
	 *            the parent to create the check box
	 * @param label
	 *            the label text
	 * @return {@link Button} control
	 */
	protected Button createCheckBoxControl(Composite parent, String label) {

		Composite composite = new Composite(parent, SWT.NULL);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));
		Button button = new Button(composite, SWT.CHECK);
		GridData gridData1 = new GridData();
		gridData1.horizontalSpan = 1;
		button.setLayoutData(gridData1);
		createLabelControl(composite, label);
		return button;
	}

	/**
	 * Creates the text field.
	 * 
	 * @param parent
	 *            the parent to create the text field
	 * @return the text
	 */
	protected Text createTextControl(Composite parent) {

		Text text = new Text(parent, SWT.BORDER | SWT.SINGLE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 1;
		text.setLayoutData(gridData);
		return text;
	}

	/**
	 * Creates the combo control.
	 * 
	 * @param parent
	 *            the parent to create the combo
	 * @param items
	 *            the combo items
	 * @return the combo
	 */
	protected Combo createComboControl(Composite parent, String[] items) {

		Combo combo = new Combo(parent, SWT.BORDER | SWT.SINGLE);
		combo.setItems(items);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 1;
		combo.setLayoutData(gridData);
		return combo;
	}

	/**
	 * Creates the spinner.
	 * 
	 * @param parent
	 *            the parent to create the spinner
	 * @param min
	 *            the minimum value of spinner
	 * @param max
	 *            the maximum value of spinner
	 * @return the spinner
	 */
	protected Spinner createSpinnerControl(Composite parent, int min, int max) {

		Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setMinimum(min);
		spinner.setMaximum(max);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 1;
		spinner.setLayoutData(gridData);
		return spinner;
	}
}
