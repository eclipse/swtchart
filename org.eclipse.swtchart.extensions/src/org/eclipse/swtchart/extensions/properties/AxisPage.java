/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
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
 *******************************************************************************/
package org.eclipse.swtchart.extensions.properties;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.Constants;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.IDisposeListener;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.charts.InteractiveChart;

/**
 * The axis page on properties dialog.
 */
public class AxisPage extends AbstractSelectorPage {

	/** the key for axis title font */
	private static final String AXIS_TITLE_FONT = "org.eclipse.swtchart.axis.title.font"; //$NON-NLS-1$
	/** the key for axis title foreground */
	private static final String AXIS_TITLE_FOREGROUND = "org.eclipse.swtchart.axis.title.foreground"; //$NON-NLS-1$
	/** the axes */
	private IAxis[] axes;
	/** the axis direction */
	private Direction direction;
	/** the show title button */
	protected Button showTitleButton;
	/** the label for title */
	private Label titleLabel;
	/** the title text */
	protected Text titleText;
	/** the label for font size */
	private Label fontSizeLabel;
	/** the spinner for font size */
	protected Spinner fontSizeSpinner;
	/** the label for title color */
	private Label titleColorLabel;
	/** the color selector button */
	protected ColorSelector titleColorButton;
	/** the minimum range text */
	protected Text minRangeText;
	/** the maximum range text */
	protected Text maxRangeText;
	/** the position combo box */
	protected Combo positionCombo;
	/** the category button */
	protected Button categoryButton;
	/** the log scale button */
	protected Button logScaleButton;
	/** the states indicating id title is visible */
	protected boolean[] titleVisibleStates;
	/** the title texts */
	protected String[] titleTexts;
	/** the title font sizes */
	protected int[] titleFontSizes;
	/** the title colors */
	protected RGB[] titleColors;
	/** the minimum ranges */
	protected double[] minRanges;
	/** the maximum ranges */
	protected double[] maxRanges;
	/** the positions */
	protected Position[] positions;
	/** the states indicating if category is enabled */
	protected boolean[] categoryStates;
	/** the states indicating if log scale is enabled */
	protected boolean[] logScaleStates;

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
	public AxisPage(InteractiveChart chart, PropertiesResources resources, Direction direction, String title) {
		super(chart, resources, title, Messages.getString(Messages.AXES));
		this.direction = direction;
		if(direction == Direction.X) {
			this.axes = chart.getAxisSet().getXAxes();
		} else if(direction == Direction.Y) {
			this.axes = chart.getAxisSet().getYAxes();
		}
		titleVisibleStates = new boolean[axes.length];
		titleTexts = new String[axes.length];
		titleFontSizes = new int[axes.length];
		titleColors = new RGB[axes.length];
		minRanges = new double[axes.length];
		maxRanges = new double[axes.length];
		positions = new Position[axes.length];
		if(direction == Direction.X) {
			categoryStates = new boolean[axes.length];
		}
		logScaleStates = new boolean[axes.length];
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
			titleVisibleStates[i] = axes[i].getTitle().isVisible();
			titleTexts[i] = axes[i].getTitle().getText();
			titleFontSizes[i] = axes[i].getTitle().getFont().getFontData()[0].getHeight();
			titleColors[i] = axes[i].getTitle().getForeground().getRGB();
			minRanges[i] = axes[i].getRange().lower;
			maxRanges[i] = axes[i].getRange().upper;
			positions[i] = axes[i].getPosition();
			if(direction == Direction.X) {
				categoryStates[i] = axes[i].isCategoryEnabled();
			}
			logScaleStates[i] = axes[i].isLogScaleEnabled();
		}
	}

	@Override
	protected void updateControlSelections() {

		showTitleButton.setSelection(titleVisibleStates[selectedIndex]);
		setControlsEnable(titleVisibleStates[selectedIndex]);
		titleText.setText(titleTexts[selectedIndex]);
		fontSizeSpinner.setSelection(titleFontSizes[selectedIndex]);
		titleColorButton.setColorValue(titleColors[selectedIndex]);
		minRangeText.setText(String.valueOf(minRanges[selectedIndex]));
		maxRangeText.setText(String.valueOf(maxRanges[selectedIndex]));
		positionCombo.setText(String.valueOf(positions[selectedIndex]));
		logScaleButton.setSelection(logScaleStates[selectedIndex]);
		if(direction == Direction.X) {
			categoryButton.setSelection(categoryStates[selectedIndex]);
		}
	}

	@Override
	protected void addRightPanelContents(Composite parent) {

		addAxisPanel(parent);
		addTitleGroup(parent);
	}

	/**
	 * Adds axis panel.
	 * 
	 * @param parent
	 *            the parent to add the axis panel
	 */
	private void addAxisPanel(Composite parent) {

		Composite group = new Composite(parent, SWT.NONE);
		group.setLayout(new GridLayout(2, true));
		createLabelControl(group, Messages.getString(Messages.MIN_RANGE_VALUE));
		minRangeText = createTextControl(group);
		minRangeText.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {

				minRanges[selectedIndex] = Double.valueOf(minRangeText.getText());
			}
		});
		createLabelControl(group, Messages.getString(Messages.MAX_RANGE_VALUE));
		maxRangeText = createTextControl(group);
		maxRangeText.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {

				maxRanges[selectedIndex] = Double.valueOf(maxRangeText.getText());
			}
		});
		createLabelControl(group, Messages.getString(Messages.POSITION));
		String[] items = new String[]{Position.Primary.name(), Position.Secondary.name()};
		positionCombo = createComboControl(group, items);
		positionCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				positions[selectedIndex] = Position.valueOf(positionCombo.getText());
			}
		});
		logScaleButton = createCheckBoxControl(group, Messages.getString(Messages.ENABLE_LOG_SCALE));
		logScaleButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				logScaleStates[selectedIndex] = logScaleButton.getSelection();
			}
		});
		if(direction == Direction.X) {
			categoryButton = createCheckBoxControl(group, Messages.getString(Messages.ENABLE_CATEGORY));
			categoryButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {

					categoryStates[selectedIndex] = categoryButton.getSelection();
				}
			});
		}
	}

	/**
	 * Adds title group.
	 * 
	 * @param parent
	 *            the parent to add the title group
	 */
	private void addTitleGroup(Composite parent) {

		Group group = createGroupControl(parent, Messages.getString(Messages.TITLE), false); 
		showTitleButton = createCheckBoxControl(group, Messages.getString(Messages.SHOW_TITLE));
		showTitleButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = showTitleButton.getSelection();
				titleVisibleStates[selectedIndex] = visible;
				setControlsEnable(visible);
			}
		});
		titleLabel = createLabelControl(group, Messages.getString(Messages.TEXT));
		titleText = createTextControl(group);
		titleText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {

				titleTexts[selectedIndex] = titleText.getText();
			}
		});
		fontSizeLabel = createLabelControl(group, Messages.getString(Messages.FONT_SIZE));
		fontSizeSpinner = createSpinnerControl(group, 8, 30);
		fontSizeSpinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				titleFontSizes[selectedIndex] = fontSizeSpinner.getSelection();
			}
		});
		titleColorLabel = createLabelControl(group, Messages.getString(Messages.COLOR));
		titleColorButton = createColorButtonControl(group);
		titleColorButton.addListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {

				titleColors[selectedIndex] = titleColorButton.getColorValue();
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

		titleLabel.setEnabled(enabled);
		titleText.setEnabled(enabled);
		fontSizeLabel.setEnabled(enabled);
		fontSizeSpinner.setEnabled(enabled);
		titleColorLabel.setEnabled(enabled);
		titleColorButton.setEnabled(enabled);
	}

	@Override
	public void apply() {

		for(int i = 0; i < axes.length; i++) {
			axes[i].getTitle().setVisible(titleVisibleStates[i]);
			axes[i].getTitle().setText(titleTexts[i]);
			FontData fontData = axes[i].getTitle().getFont().getFontData()[0];
			fontData.setHeight(titleFontSizes[i]);
			Font font = new Font(Display.getDefault(), fontData);
			axes[i].getTitle().setFont(font);
			final String fontKey = AXIS_TITLE_FONT + axes[i].getDirection() + axes[i].getId();
			if(resources.getFont(fontKey) == null) {
				axes[i].addDisposeListener(new IDisposeListener() {

					public void disposed(Event e) {

						resources.removeFont(fontKey);
					}
				});
			}
			resources.put(fontKey, font);
			Color color = new Color(Display.getDefault(), titleColors[i]);
			axes[i].getTitle().setForeground(color);
			final String colorKey = AXIS_TITLE_FOREGROUND + axes[i].getDirection() + axes[i].getId();
			if(resources.getColor(colorKey) == null) {
				axes[i].addDisposeListener(new IDisposeListener() {

					public void disposed(Event e) {

						resources.removeColor(colorKey);
					}
				});
			}
			resources.put(colorKey, color);
			axes[i].setRange(new Range(minRanges[i], maxRanges[i]));
			axes[i].setPosition(positions[i]);
			try {
				axes[i].enableLogScale(logScaleStates[i]);
			} catch(IllegalStateException e) {
				axes[i].enableLogScale(false);
				logScaleButton.setSelection(false);
			}
			if(direction == Direction.X) {
				axes[i].enableCategory(categoryStates[i]);
			}
		}
	}

	@Override
	protected void performDefaults() {

		titleVisibleStates[selectedIndex] = true;
		if(direction == Direction.X) {
			titleTexts[selectedIndex] = Messages.getString(Messages.X_AXIS); 
			categoryStates[selectedIndex] = false;
		} else if(direction == Direction.Y) {
			titleTexts[selectedIndex] = Messages.getString(Messages.Y_AXIS);
		}
		positions[selectedIndex] = Position.Primary;
		titleFontSizes[selectedIndex] = Constants.MEDIUM_FONT_SIZE;
		titleColors[selectedIndex] = Display.getDefault().getSystemColor(SWT.COLOR_BLUE).getRGB();
		minRanges[selectedIndex] = 0.0;
		maxRanges[selectedIndex] = 1.0;
		logScaleStates[selectedIndex] = false;
		updateControlSelections();
		super.performDefaults();
	}
}
