/*******************************************************************************
 * Copyright (c) 2020, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.extensions.internal.marker.EmbeddedLegend;
import org.eclipse.swtchart.extensions.internal.support.PositionValidator;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;

public class InChartLegendUI extends Composite {

	private ScrollableChart scrollableChart;
	private EmbeddedLegend embeddedLegend;
	private boolean capturePosition = false;
	//
	private Text textX;
	private Text textY;
	//
	private List<Control> controls = new ArrayList<>();
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();

	public InChartLegendUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	@Override
	public void update() {

		super.update();
		updateControls();
		updateLegendPosition(true);
	}

	public void setScrollableChart(ScrollableChart scrollableChart) {

		this.scrollableChart = scrollableChart;
		createEmbeddedLegend();
	}

	public boolean toggleLegend() {

		boolean draw = false;
		if(embeddedLegend != null) {
			/*
			 * Show/Hide
			 */
			draw = !embeddedLegend.isDraw();
			embeddedLegend.setDraw(draw);
			//
			if(scrollableChart != null) {
				scrollableChart.redraw();
			}
			//
			updateControls();
		}
		//
		return draw;
	}

	private void createControl() {

		GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		setLayout(gridLayout);
		//
		createToolbarInChartLegend(this);
		initialize();
	}

	private void initialize() {

	}

	private void createToolbarInChartLegend(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(7, false));
		//
		add(createButtonMove(composite, ResourceSupport.ARROW_LEFT, "Move Legend Left"));
		add(createButtonMove(composite, ResourceSupport.ARROW_UP, "Move Legend Up"));
		add(createButtonMove(composite, ResourceSupport.ARROW_DOWN, "Move Legend Down"));
		add(createButtonMove(composite, ResourceSupport.ARROW_RIGHT, "Move Legend Right"));
		add(textX = createTextPositionX(composite));
		add(textY = createTextPositionY(composite));
		add(createButtonSetPosition(composite));
	}

	private void add(Control control) {

		controls.add(control);
	}

	private Button createButtonMove(Composite parent, String icon, String tooltip) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(tooltip);
		button.setImage(ResourceSupport.getImage(icon));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(embeddedLegend != null) {
					/*
					 * Current position
					 */
					int moveX = preferenceStore != null ? preferenceStore.getInt(PreferenceConstants.P_MOVE_LEGEND_X) : PreferenceConstants.DEF_MOVE_LEGEND_X;
					int moveY = preferenceStore != null ? preferenceStore.getInt(PreferenceConstants.P_MOVE_LEGEND_Y) : PreferenceConstants.DEF_MOVE_LEGEND_Y;
					int x = embeddedLegend.getX();
					int y = embeddedLegend.getY();
					/*
					 * Modify the position
					 */
					switch(icon) {
						case ResourceSupport.ARROW_LEFT:
							x -= moveX;
							break;
						case ResourceSupport.ARROW_UP:
							y -= moveY;
							break;
						case ResourceSupport.ARROW_DOWN:
							y += moveY;
							break;
						case ResourceSupport.ARROW_RIGHT:
							x += moveX;
							break;
					}
					/*
					 * Update the position
					 */
					updateLegendPosition(x, y, true);
				}
			}
		});
		//
		return button;
	}

	private Text createTextPositionX(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setToolTipText("Legend Position X");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.minimumWidth = 60;
		text.setLayoutData(gridData);
		//
		PositionValidator validator = new PositionValidator();
		ControlDecoration controlDecoration = new ControlDecoration(text, SWT.LEFT | SWT.TOP);
		//
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if(validate(validator, controlDecoration, text)) {
					if(preferenceStore != null) {
						preferenceStore.setValue(PreferenceConstants.P_LEGEND_POSITION_X, validator.getPosition());
						ResourceSupport.savePreferenceStore();
						updateLegendPosition(true);
					}
				}
			}
		});
		//
		return text;
	}

	private Text createTextPositionY(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		text.setToolTipText("Legend Position Y");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.minimumWidth = 60;
		text.setLayoutData(gridData);
		//
		PositionValidator validator = new PositionValidator();
		ControlDecoration controlDecoration = new ControlDecoration(text, SWT.LEFT | SWT.TOP);
		//
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if(validate(validator, controlDecoration, text)) {
					if(preferenceStore != null) {
						preferenceStore.setValue(PreferenceConstants.P_LEGEND_POSITION_Y, validator.getPosition());
						ResourceSupport.savePreferenceStore();
						updateLegendPosition(true);
					}
				}
			}
		});
		//
		return text;
	}

	private Button createButtonSetPosition(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Set the position of the legend.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_POSITION));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				capturePosition = MessageDialog.openConfirm(e.display.getActiveShell(), "Legend Position", "Set the position manually by using left mouse button double-click in the chart.");
			}
		});
		//
		return button;
	}

	private void updateLegendPosition(int x, int y, boolean redraw) {

		if(embeddedLegend != null) {
			/*
			 * Legend
			 */
			embeddedLegend.setX(x);
			embeddedLegend.setY(y);
			if(preferenceStore != null) {
				preferenceStore.setValue(PreferenceConstants.P_LEGEND_POSITION_X, x);
				preferenceStore.setValue(PreferenceConstants.P_LEGEND_POSITION_Y, y);
				ResourceSupport.savePreferenceStore();
			}
			/*
			 * Text
			 */
			textX.setText(Integer.toString(embeddedLegend.getX()));
			textY.setText(Integer.toString(embeddedLegend.getY()));
			/*
			 * Update
			 */
			if(scrollableChart != null && redraw) {
				scrollableChart.redraw();
			}
		}
	}

	private void updateLegendPosition(boolean redraw) {

		if(preferenceStore != null) {
			updateLegendPosition(preferenceStore.getInt(PreferenceConstants.P_LEGEND_POSITION_X), preferenceStore.getInt(PreferenceConstants.P_LEGEND_POSITION_Y), redraw);
		}
	}

	private void createEmbeddedLegend() {

		if(scrollableChart != null) {
			BaseChart baseChart = scrollableChart.getBaseChart();
			embeddedLegend = new EmbeddedLegend(baseChart);
			embeddedLegend.setDraw(false);
			updateLegendPosition(false);
			baseChart.getPlotArea().addCustomPaintListener(embeddedLegend);
			/*
			 * Left mouse double-click to get the position to place the legend.
			 */
			baseChart.addCustomPointSelectionHandler(new ICustomSelectionHandler() {

				@Override
				public void handleUserSelection(Event event) {

					if(embeddedLegend.isDraw()) {
						if(capturePosition) {
							updateLegendPosition(event.x, event.y, true);
							updateControls();
							capturePosition = false;
						}
					}
				}
			});
		}
		updateControls();
	}

	private void updateControls() {

		if(embeddedLegend != null) {
			boolean enabled = embeddedLegend.isDraw();
			for(Control control : controls) {
				control.setEnabled(enabled);
			}
		}
	}

	private boolean validate(IValidator<String> validator, ControlDecoration controlDecoration, Text text) {

		IStatus status = validator.validate(text.getText().trim());
		if(status.isOK()) {
			controlDecoration.hide();
			return true;
		} else {
			controlDecoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage());
			controlDecoration.showHoverText(status.getMessage());
			controlDecoration.show();
			return false;
		}
	}
}