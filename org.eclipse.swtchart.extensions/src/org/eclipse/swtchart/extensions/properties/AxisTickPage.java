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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.Resources;
import org.eclipse.swtchart.extensions.charts.InteractiveChart;

/**
 * The tick page on properties dialog.
 */
public class AxisTickPage extends AbstractSelectorPage {

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
	public AxisTickPage(InteractiveChart chart, Direction direction, String title) {

		super(chart, title, Messages.getString(Messages.AXES));
		if(direction == Direction.X) {
			this.axes = chart.getAxisSet().getXAxes();
		} else if(direction == Direction.Y) {
			this.axes = chart.getAxisSet().getYAxes();
		}
		visibilityStates = new boolean[axes.length];
		fontSizes = new int[axes.length];
		foregroundColors = new RGB[axes.length];
	}

	@Override
	protected String[] getListItems() {

		String[] items = new String[axes.length];
		for(int i = 0; i < items.length; i++) {
			items[i] = String.valueOf(axes[i].getId());
		}
		return items;
	}

	@Override
	protected void selectInitialValues() {

		for(int i = 0; i < axes.length; i++) {
			visibilityStates[i] = axes[i].getTick().isVisible();
			fontSizes[i] = axes[i].getTick().getFont().getFontData()[0].getHeight();
			foregroundColors[i] = axes[i].getTick().getForeground().getRGB();
		}
	}

	@Override
	protected void updateControlSelections() {

		showTickButton.setSelection(visibilityStates[selectedIndex]);
		setControlsEnable(visibilityStates[selectedIndex]);
		fontSizeSpinner.setSelection(fontSizes[selectedIndex]);
		foregroundButton.setColorValue(foregroundColors[selectedIndex]);
	}

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
		showTickButton = createCheckBoxControl(group, Messages.getString(Messages.SHOW_TICK));
		showTickButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = showTickButton.getSelection();
				visibilityStates[selectedIndex] = visible;
				setControlsEnable(visible);
			}
		});
		fontSizeLabel = createLabelControl(group, Messages.getString(Messages.FONT_SIZE));
		fontSizeSpinner = createSpinnerControl(group, 8, 30);
		fontSizeSpinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				fontSizes[selectedIndex] = fontSizeSpinner.getSelection();
			}
		});
		foregroundLabel = createLabelControl(group, Messages.getString(Messages.COLOR));
		foregroundButton = createColorButtonControl(group);
		foregroundButton.addListener(new IPropertyChangeListener() {

			@Override
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

	@Override
	public void apply() {

		for(int i = 0; i < axes.length; i++) {
			axes[i].getTick().setVisible(visibilityStates[i]);
			FontData fontData = axes[i].getTick().getFont().getFontData()[0];
			fontData.setHeight(fontSizes[i]);
			Font font = Resources.getFont(fontData);
			axes[i].getTick().setFont(font);
			Color color = Resources.getColor(foregroundColors[i]);
			axes[i].getTick().setForeground(color);
		}
	}

	@Override
	protected void performDefaults() {

		visibilityStates[selectedIndex] = true;
		fontSizes[selectedIndex] = Resources.SMALL_FONT_SIZE;
		foregroundColors[selectedIndex] = Display.getDefault().getSystemColor(SWT.COLOR_BLUE).getRGB();
		updateControlSelections();
		super.performDefaults();
	}
}