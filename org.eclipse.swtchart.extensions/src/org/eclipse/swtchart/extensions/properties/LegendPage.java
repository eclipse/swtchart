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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swtchart.Constants;
import org.eclipse.swtchart.ILegend;
import org.eclipse.swtchart.extensions.charts.InteractiveChart;

/**
 * The legend property page on properties dialog.
 */
public class LegendPage extends AbstractPage {

	/** the key for legend font */
	private static final String LEGEND_FONT = "org.eclipse.swtchart.legend.font";
	/** the key for legend foreground */
	private static final String LEGEND_FOREGROUND = "org.eclipse.swtchart.legend.foreground";
	/** the key for legend background */
	private static final String LEGEND_GACKGROUND = "org.eclipse.swtchart.legend.background";
	/** the show legend button */
	protected Button showLegendButton;
	/** the background label */
	private Label backgroundLabel;
	/** the background button */
	private ColorSelector backgroundButton;
	/** the foreground label */
	private Label foregroundLabel;
	/** the foreground button */
	private ColorSelector foregroundButton;
	/** the font size label */
	private Label fontSizeLabel;
	/** the font size spinner */
	private Spinner fontSizeSpinner;
	/** the legend */
	private ILegend legend;

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
	public LegendPage(InteractiveChart chart, PropertiesResources resources, String title) {
		super(chart, resources, title);
		legend = chart.getLegend();
	}

	/*
	 * @see PreferencePage#createContents(Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		addLegendPanel(composite);
		selectValues();
		return composite;
	}

	/**
	 * Adds the legend panel.
	 * 
	 * @param parent
	 *            the parent to add the legend panel
	 */
	private void addLegendPanel(Composite parent) {

		Composite group = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);
		group.setLayout(new GridLayout(2, false));
		showLegendButton = createCheckBoxControl(group, "Show legend");
		showLegendButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = showLegendButton.getSelection();
				setControlsEnable(visible);
			}
		});
		backgroundLabel = createLabelControl(group, "Background:");
		backgroundButton = createColorButtonControl(group);
		foregroundLabel = createLabelControl(group, "Foreground:");
		foregroundButton = createColorButtonControl(group);
		fontSizeLabel = createLabelControl(group, "Font size:");
		fontSizeSpinner = createSpinnerControl(group, 8, 30);
	}

	/**
	 * Selects the values for controls.
	 */
	private void selectValues() {

		showLegendButton.setSelection(legend.isVisible());
		setControlsEnable(legend.isVisible());
		backgroundButton.setColorValue(legend.getBackground().getRGB());
		foregroundButton.setColorValue(legend.getForeground().getRGB());
		fontSizeSpinner.setSelection(legend.getFont().getFontData()[0].getHeight());
	}

	/**
	 * Sets the enable state of controls.
	 * 
	 * @param enabled
	 *            true if controls are enabled
	 */
	protected void setControlsEnable(boolean enabled) {

		backgroundLabel.setEnabled(enabled);
		backgroundButton.setEnabled(enabled);
		foregroundLabel.setEnabled(enabled);
		foregroundButton.setEnabled(enabled);
		fontSizeLabel.setEnabled(enabled);
		fontSizeSpinner.setEnabled(enabled);
	}

	/*
	 * @see AbstractPreferencePage#apply()
	 */
	@Override
	public void apply() {

		legend.setVisible(showLegendButton.getSelection());
		Color color = new Color(Display.getDefault(), backgroundButton.getColorValue());
		legend.setBackground(color);
		resources.put(LEGEND_GACKGROUND, color);
		color = new Color(Display.getDefault(), foregroundButton.getColorValue());
		legend.setForeground(color);
		resources.put(LEGEND_FOREGROUND, color);
		FontData fontData = legend.getFont().getFontData()[0];
		Font font = new Font(legend.getFont().getDevice(), fontData.getName(), fontSizeSpinner.getSelection(), fontData.getStyle());
		legend.setFont(font);
		resources.put(LEGEND_FONT, font);
	}

	/*
	 * @see PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {

		showLegendButton.setSelection(true);
		setControlsEnable(true);
		backgroundButton.setColorValue(new RGB(255, 255, 255));
		foregroundButton.setColorValue(new RGB(0, 0, 0));
		fontSizeSpinner.setSelection(Constants.SMALL_FONT_SIZE);
		super.performDefaults();
	}
}
