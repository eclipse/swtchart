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
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swtchart.Constants;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.extensions.charts.InteractiveChart;
import org.eclipse.swtchart.IDisposeListener;

/**
 * The tick page on properties dialog.
 */
public class AxisTickPage extends AbstractSelectorPage {

	/** the key for axis tick font */
	private static final String AXIS_TICK_FONT = "org.eclipse.swtchart.axistick.font";
	/** the key for axis tick foreground */
	private static final String AXIS_TICK_FOREGROUND = "org.eclipse.swtchart.axistick.foreground";
	/** the axes */
	private IAxis[] axes;
	/** the show tick button */
	protected Button showTickButton;
	/** the label for font size */
	private Label fontSizeLabel;
	/** the spinner for font size */
	protected Spinner fontSizeSpinner;
	/** the foreground label */
	private Label foregroundLabel;
	/** the foreground button */
	protected ColorSelector foregroundButton;
	/** the states indicating the visibility of axis ticks */
	protected boolean[] visibilityStates;
	/** the font sizes */
	protected int[] fontSizes;
	/** the foreground colors */
	protected RGB[] foregroundColors;

	/**
	 * Constructor.
	 * 
	 * @param chart
	 *            the chart
	 * @param resources
	 *            the properties resources
	 * @param direction
	 *            the direction
	 * @param title
	 *            the title
	 */
	public AxisTickPage(InteractiveChart chart, PropertiesResources resources, Direction direction, String title) {
		super(chart, resources, title, "Axes:");
		if(direction == Direction.X) {
			this.axes = chart.getAxisSet().getXAxes();
		} else if(direction == Direction.Y) {
			this.axes = chart.getAxisSet().getYAxes();
		}
		visibilityStates = new boolean[axes.length];
		fontSizes = new int[axes.length];
		foregroundColors = new RGB[axes.length];
	}

	/*
	 * @see AbstractSelectorPage#getListItems()
	 */
	@Override
	protected String[] getListItems() {

		String[] items = new String[axes.length];
		for(int i = 0; i < items.length; i++) {
			items[i] = String.valueOf(axes[i].getId());
		}
		return items;
	}

	/*
	 * @see AbstractSelectorPage#selectInitialValues()
	 */
	@Override
	protected void selectInitialValues() {

		for(int i = 0; i < axes.length; i++) {
			visibilityStates[i] = axes[i].getTick().isVisible();
			fontSizes[i] = axes[i].getTick().getFont().getFontData()[0].getHeight();
			foregroundColors[i] = axes[i].getTick().getForeground().getRGB();
		}
	}

	/*
	 * @see AbstractSelectorPage#updateControlSelections()
	 */
	@Override
	protected void updateControlSelections() {

		showTickButton.setSelection(visibilityStates[selectedIndex]);
		setControlsEnable(visibilityStates[selectedIndex]);
		fontSizeSpinner.setSelection(fontSizes[selectedIndex]);
		foregroundButton.setColorValue(foregroundColors[selectedIndex]);
	}

	/*
	 * @see AbstractSelectorPage#addRightPanelContents(Composite)
	 */
	@Override
	protected void addRightPanelContents(Composite parent) {

		addTickPanel(parent);
	}

	/**
	 * Create the tick panel.
	 * 
	 * @param parent
	 *            the parent to add the tick panel
	 */
	private void addTickPanel(Composite parent) {

		Composite group = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);
		group.setLayout(new GridLayout(2, false));
		showTickButton = createCheckBoxControl(group, "Show tick");
		showTickButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = showTickButton.getSelection();
				visibilityStates[selectedIndex] = visible;
				setControlsEnable(visible);
			}
		});
		fontSizeLabel = createLabelControl(group, "Font size:");
		fontSizeSpinner = createSpinnerControl(group, 8, 30);
		fontSizeSpinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				fontSizes[selectedIndex] = fontSizeSpinner.getSelection();
			}
		});
		foregroundLabel = createLabelControl(group, "Color:");
		foregroundButton = createColorButtonControl(group);
		foregroundButton.addListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {

				foregroundColors[selectedIndex] = foregroundButton.getColorValue();
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

		fontSizeLabel.setEnabled(enabled);
		fontSizeSpinner.setEnabled(enabled);
		foregroundLabel.setEnabled(enabled);
		foregroundButton.setEnabled(enabled);
	}

	/*
	 * @see AbstractPreferencePage#apply()
	 */
	@Override
	public void apply() {

		for(int i = 0; i < axes.length; i++) {
			axes[i].getTick().setVisible(visibilityStates[i]);
			FontData fontData = axes[i].getTick().getFont().getFontData()[0];
			fontData.setHeight(fontSizes[i]);
			Font font = new Font(Display.getDefault(), fontData);
			axes[i].getTick().setFont(font);
			final String fontKey = AXIS_TICK_FONT + axes[i].getDirection() + axes[i].getId();
			if(resources.getFont(fontKey) == null) {
				axes[i].addDisposeListener(new IDisposeListener() {

					public void disposed(Event e) {

						resources.removeFont(fontKey);
					}
				});
			}
			resources.put(fontKey, font);
			Color color = new Color(Display.getDefault(), foregroundColors[i]);
			axes[i].getTick().setForeground(color);
			final String colorKey = AXIS_TICK_FOREGROUND + axes[i].getDirection() + axes[i].getId();
			if(resources.getColor(colorKey) == null) {
				axes[i].addDisposeListener(new IDisposeListener() {

					public void disposed(Event e) {

						resources.removeColor(colorKey);
					}
				});
			}
			resources.put(colorKey, color);
		}
	}

	/*
	 * @see PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {

		visibilityStates[selectedIndex] = true;
		fontSizes[selectedIndex] = Constants.SMALL_FONT_SIZE;
		foregroundColors[selectedIndex] = Display.getDefault().getSystemColor(SWT.COLOR_BLUE).getRGB();
		updateControlSelections();
		super.performDefaults();
	}
}
