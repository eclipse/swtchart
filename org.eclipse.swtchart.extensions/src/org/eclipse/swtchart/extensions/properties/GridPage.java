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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.Resources;
import org.eclipse.swtchart.extensions.charts.InteractiveChart;

/**
 * The grid page on properties dialog.
 */
public class GridPage extends AbstractSelectorPage {

	/** the axes */
	private IAxis[] axes;
	/** the style combo */
	protected Combo styleCombo;
	/** the foreground button */
	protected ColorSelector foregroundButton;
	/** the line styles */
	protected LineStyle[] styles;
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
	public GridPage(InteractiveChart chart, Direction direction, String title) {

		super(chart, title, Messages.AXES);
		if(direction == Direction.X) {
			this.axes = chart.getAxisSet().getXAxes();
		} else if(direction == Direction.Y) {
			this.axes = chart.getAxisSet().getYAxes();
		}
		styles = new LineStyle[axes.length];
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
			styles[i] = axes[i].getGrid().getStyle();
			foregroundColors[i] = axes[i].getGrid().getForeground().getRGB();
		}
	}

	@Override
	protected void updateControlSelections() {

		styleCombo.setText(String.valueOf(styles[selectedIndex]));
		foregroundButton.setColorValue(foregroundColors[selectedIndex]);
	}

	@Override
	protected void addRightPanelContents(Composite parent) {

		addGridPanel(parent);
	}

	/**
	 * Adds the grid panel.
	 * 
	 * @param parent
	 *            the parent to add the grid panel
	 */
	private void addGridPanel(Composite parent) {

		Composite group = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);
		group.setLayout(new GridLayout(2, false));
		createLabelControl(group, Messages.LINE_STYLE);
		LineStyle[] values = LineStyle.values();
		String[] labels = new String[values.length];
		for(int i = 0; i < values.length; i++) {
			labels[i] = values[i].label();
		}
		styleCombo = createComboControl(group, labels);
		styleCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String value = styleCombo.getText();
				LineStyle selectedStyle = LineStyle.NONE;
				for(LineStyle style : LineStyle.values()) {
					if(style.label().equals(value)) {
						selectedStyle = style;
					}
				}
				styles[selectedIndex] = selectedStyle;
			}
		});
		createLabelControl(group, Messages.COLOR);
		foregroundButton = createColorButtonControl(group);
		foregroundButton.addListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {

				foregroundColors[selectedIndex] = foregroundButton.getColorValue();
			}
		});
	}

	@Override
	public void apply() {

		for(int i = 0; i < axes.length; i++) {
			axes[i].getGrid().setStyle(styles[i]);
			Color color = Resources.getColor(foregroundColors[i]);
			axes[i].getGrid().setForeground(color);
		}
	}

	@Override
	protected void performDefaults() {

		styles[selectedIndex] = LineStyle.DOT;
		foregroundColors[selectedIndex] = Display.getDefault().getSystemColor(SWT.COLOR_GRAY).getRGB();
		updateControlSelections();
		super.performDefaults();
	}
}