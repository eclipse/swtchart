/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 * Frank Buloup - Internationalization
 * Philip Wenig - series settings mappings
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.Resources;
import org.eclipse.swtchart.extensions.charts.InteractiveChart;

/**
 * The chart property page on properties dialog.
 */
public class ChartPage extends AbstractPage {

	/** the color selector for background color in plot area */
	private ColorSelector backgroundInPlotAreaButton;
	/** the color selector for background */
	private ColorSelector backgroundButton;
	/** the orientation button */
	private Button orientationButton;
	/** the show title button */
	protected Button showTitleButton;
	/** the title label */
	private Label titleLabel;
	/** the title text */
	private Text titleText;
	/** the font size label */
	private Label fontSizeLabel;
	/** the font size spinner */
	private Spinner fontSizeSpinner;
	/** the title color label */
	private Label titleColorLabel;
	/** the title color button */
	private ColorSelector titleColorButton;

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
	public ChartPage(InteractiveChart chart, String title) {

		super(chart, title);
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		addChartPanel(composite);
		addTitleGroup(composite);
		selectValues();
		return composite;
	}

	/**
	 * Adds the chart panel.
	 * 
	 * @param parent
	 *            the parent to add the chart panel
	 */
	private void addChartPanel(Composite parent) {

		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayout(new GridLayout(2, false));
		createLabelControl(panel, Messages.BACKGROUND_PLOT_AREA);
		backgroundInPlotAreaButton = createColorButtonControl(panel);
		createLabelControl(panel, Messages.BACKGROUND);
		backgroundButton = createColorButtonControl(panel);
		orientationButton = createCheckBoxControl(panel, Messages.VERTICAL_ORIENTATION);
	}

	/**
	 * Adds the title group.
	 * 
	 * @param parent
	 *            the parent to add the title group
	 */
	private void addTitleGroup(Composite parent) {

		Group group = createGroupControl(parent, Messages.TITLE, false);
		showTitleButton = createCheckBoxControl(group, Messages.SHOW_TITLE);
		showTitleButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				setTitleControlsEnable(showTitleButton.getSelection());
			}
		});
		titleLabel = createLabelControl(group, Messages.TEXT);
		titleText = createTextControl(group);
		fontSizeLabel = createLabelControl(group, Messages.FONT_SIZE);
		fontSizeSpinner = createSpinnerControl(group, 8, 30);
		titleColorLabel = createLabelControl(group, Messages.COLOR);
		titleColorButton = createColorButtonControl(group);
	}

	/**
	 * Selects the values for controls.
	 */
	private void selectValues() {

		backgroundInPlotAreaButton.setColorValue(chart.getPlotArea().getBackground().getRGB());
		backgroundButton.setColorValue(chart.getBackground().getRGB());
		orientationButton.setSelection(chart.getOrientation() == SWT.VERTICAL);
		ITitle title = chart.getTitle();
		showTitleButton.setSelection(title.isVisible());
		setTitleControlsEnable(title.isVisible());
		titleText.setText(title.getText());
		fontSizeSpinner.setSelection(title.getFont().getFontData()[0].getHeight());
		titleColorButton.setColorValue(title.getForeground().getRGB());
	}

	/**
	 * Sets the enable state of title controls.
	 * 
	 * @param enabled
	 *            true if title controls are enabled
	 */
	protected void setTitleControlsEnable(boolean enabled) {

		titleLabel.setEnabled(enabled);
		titleText.setEnabled(enabled);
		fontSizeLabel.setEnabled(enabled);
		fontSizeSpinner.setEnabled(enabled);
		titleColorLabel.setEnabled(enabled);
		titleColorButton.setEnabled(enabled);
	}

	@Override
	public void apply() {

		Color color = Resources.getColor(backgroundInPlotAreaButton.getColorValue());
		chart.getPlotArea().setBackground(color);
		color = Resources.getColor(backgroundButton.getColorValue());
		chart.setBackground(color);
		chart.setOrientation(orientationButton.getSelection() ? SWT.VERTICAL : SWT.HORIZONTAL);
		ITitle title = chart.getTitle();
		title.setVisible(showTitleButton.getSelection());
		title.setText(titleText.getText());
		FontData fontData = title.getFont().getFontData()[0];
		fontData.setHeight(fontSizeSpinner.getSelection());
		Font font = Resources.getFont(fontData);
		title.setFont(font);
		color = Resources.getColor(titleColorButton.getColorValue());
		title.setForeground(color);
	}

	@Override
	protected void performDefaults() {

		backgroundInPlotAreaButton.setColorValue(new RGB(255, 255, 255));
		backgroundButton.setColorValue(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB());
		orientationButton.setSelection(false);
		showTitleButton.setSelection(true);
		setTitleControlsEnable(true);
		titleText.setText(Messages.CHART_TITLE);
		fontSizeSpinner.setSelection(Resources.LARGE_FONT_SIZE);
		titleColorButton.setColorValue(new RGB(0, 0, 255));
		super.performDefaults();
	}
}