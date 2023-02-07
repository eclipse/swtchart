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
 * Philip Wenig - series settings edit support
 *******************************************************************************/
package org.eclipse.swtchart.extensions.properties;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.Resources;
import org.eclipse.swtchart.extensions.charts.InteractiveChart;
import org.eclipse.swtchart.extensions.core.ResourceSupport;

/**
 * The series label page on properties dialog.
 */
public class SeriesLabelPage extends AbstractSelectorPage {

	/** the series array */
	private ISeries<?>[] series;
	/** the show label button */
	protected Button showLabelButton;
	/** the color label */
	private Label colorLabel;
	/** the color button */
	protected ColorSelector colorButton;
	/** the font size label */
	private Label fontSizeLabel;
	/** the font size spinner */
	protected Spinner fontSizeSpinner;
	/** the states indicating the visibility of series */
	protected boolean[] visibleStates;
	/** the colors */
	protected RGB[] colors;
	/** the font size */
	protected int[] fontSizes;

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
	public SeriesLabelPage(InteractiveChart chart, String title) {

		super(chart, title, Messages.SERIES);
		series = chart.getSeriesSet().getSeries();
		visibleStates = new boolean[series.length];
		colors = new RGB[series.length];
		fontSizes = new int[series.length];
	}

	@Override
	protected String[] getListItems() {

		String[] items = new String[series.length];
		for(int i = 0; i < items.length; i++) {
			items[i] = String.valueOf(series[i].getId());
		}
		return items;
	}

	@Override
	protected void selectInitialValues() {

		for(int i = 0; i < series.length; i++) {
			visibleStates[i] = series[i].getLabel().isVisible();
			colors[i] = series[i].getLabel().getForeground().getRGB();
			fontSizes[i] = series[i].getLabel().getFont().getFontData()[0].getHeight();
		}
	}

	@Override
	protected void updateControlSelections() {

		showLabelButton.setSelection(visibleStates[selectedIndex]);
		setControlsEnable(visibleStates[selectedIndex]);
		colorButton.setColorValue(colors[selectedIndex]);
		fontSizeSpinner.setSelection(fontSizes[selectedIndex]);
	}

	@Override
	protected void addRightPanelContents(Composite parent) {

		addSeriesLabelPanel(parent);
	}

	/**
	 * Adds the series label panel.
	 * 
	 * @param parent
	 *            the parent to add the series label panel
	 */
	private void addSeriesLabelPanel(Composite parent) {

		Composite group = new Composite(parent, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		showLabelButton = createCheckBoxControl(group, Messages.SHOW_LABEL);
		showLabelButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = showLabelButton.getSelection();
				visibleStates[selectedIndex] = visible;
				setControlsEnable(visible);
			}
		});
		//
		colorLabel = createLabelControl(group, Messages.COLOR);
		colorButton = createColorButtonControl(group);
		colorButton.addListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {

				colors[selectedIndex] = colorButton.getColorValue();
			}
		});
		//
		fontSizeLabel = createLabelControl(group, Messages.FONT_SIZE);
		fontSizeSpinner = createSpinnerControl(group, 8, 30);
		fontSizeSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				fontSizes[selectedIndex] = fontSizeSpinner.getSelection();
			}
		});
	}

	/**
	 * Sets the enable state of controls.
	 * 
	 * @param enabled
	 *            true if controls are enabled
	 */
	protected void setControlsEnable(boolean enabled) {

		colorLabel.setEnabled(enabled);
		colorButton.setEnabled(enabled);
		fontSizeLabel.setEnabled(enabled);
		fontSizeSpinner.setEnabled(enabled);
	}

	@Override
	public void apply() {

		for(int i = 0; i < series.length; i++) {
			series[i].getLabel().setVisible(visibleStates[i]);
			Color color = ResourceSupport.getColor(colors[i]);
			series[i].getLabel().setForeground(color);
			FontData fontData = series[i].getLabel().getFont().getFontData()[0];
			fontData.setHeight(fontSizes[i]);
			Font font = ResourceSupport.getFont(fontData);
			series[i].getLabel().setFont(font);
		}
	}

	@Override
	protected void performDefaults() {

		visibleStates[selectedIndex] = false;
		colors[selectedIndex] = Display.getDefault().getSystemColor(SWT.COLOR_BLACK).getRGB();
		fontSizes[selectedIndex] = Resources.SMALL_FONT_SIZE;
		updateControlSelections();
		super.performDefaults();
	}
}