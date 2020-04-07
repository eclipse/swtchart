/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.Activator;
import org.eclipse.swtchart.extensions.internal.marker.EmbeddedLegend;
import org.eclipse.swtchart.extensions.internal.support.SeriesMapper;

public class ExtendedLegendUI extends Composite {

	private ScrollableChart scrollableChart;
	//
	private static final int STEPS_MOVE_X = 10;
	private static final int STEPS_MOVE_Y = 5;
	//
	private Text textX;
	private Text textY;
	private SeriesListUI seriesListUI;
	//
	private EmbeddedLegend embeddedLegend;
	private int x = 0;
	private int y = 0;
	//
	private List<Control> controls = new ArrayList<>();

	public ExtendedLegendUI(Composite parent, int style) {
		super(parent, style);
		createControl();
	}

	public void setScrollableChart(ScrollableChart scrollableChart) {

		this.scrollableChart = scrollableChart;
		seriesListUI.setScrollableChart(scrollableChart);
		createEmbeddedLegend();
	}

	public void setInput(Object input) {

		/*
		 * First adjust the settings.
		 */
		if(input instanceof ISeries<?>[] && scrollableChart != null) {
			ISeries<?>[] seriesArray = (ISeries<?>[])input;
			BaseChart baseChart = scrollableChart.getBaseChart();
			SeriesMapper seriesMapper = new SeriesMapper(baseChart);
			seriesMapper.adjustSettings(seriesArray);
		}
		/*
		 * Then fill the series list.
		 */
		seriesListUI.setInput(input);
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		seriesListUI = createListSection(this);
		//
		updateControls();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(8, false));
		//
		add(createButtonMove(composite, Activator.ARROW_LEFT, "Move Legend Left"));
		add(createButtonMove(composite, Activator.ARROW_UP, "Move Legend Up"));
		add(createButtonMove(composite, Activator.ARROW_DOWN, "Move Legend Down"));
		add(createButtonMove(composite, Activator.ARROW_RIGHT, "Move Legend Right"));
		add(textX = createTextPositionX(composite));
		add(textY = createTextPositionY(composite));
		add(createButtonPositionLegend(composite));
		createButtonToggleLegend(composite);
	}

	private void add(Control control) {

		controls.add(control);
	}

	private Button createButtonMove(Composite parent, String icon, String tooltip) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText(tooltip);
		button.setImage(Activator.getDefault().getImage(icon));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(embeddedLegend != null) {
					/*
					 * Current position
					 */
					int x = embeddedLegend.getX();
					int y = embeddedLegend.getY();
					/*
					 * Modify the position
					 */
					switch(icon) {
						case Activator.ARROW_LEFT:
							x -= STEPS_MOVE_X;
							break;
						case Activator.ARROW_UP:
							y -= STEPS_MOVE_Y;
							break;
						case Activator.ARROW_DOWN:
							y += STEPS_MOVE_Y;
							break;
						case Activator.ARROW_RIGHT:
							x += STEPS_MOVE_X;
							break;
					}
					/*
					 * Update the position
					 */
					updateLegendPosition(x, y);
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
		gridData.minimumWidth = 80;
		text.setLayoutData(gridData);
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {

				try {
					int x = Integer.parseInt(text.getText().trim());
					ExtendedLegendUI.this.x = x;
				} catch(NumberFormatException e) {
					// logger.warn(e);
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
		gridData.minimumWidth = 80;
		text.setLayoutData(gridData);
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {

				try {
					int y = Integer.parseInt(text.getText().trim());
					ExtendedLegendUI.this.y = y;
				} catch(NumberFormatException e) {
					// logger.warn(e);
				}
			}
		});
		//
		return text;
	}

	private Button createButtonPositionLegend(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Set the position of the legend.");
		button.setImage(Activator.getDefault().getImage(Activator.ICON_POSITION));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				updateLegendPosition(x, y);
			}
		});
		//
		return button;
	}

	private void updateLegendPosition(int x, int y) {

		if(embeddedLegend != null) {
			embeddedLegend.setX(x);
			embeddedLegend.setY(y);
			if(scrollableChart != null) {
				scrollableChart.redraw();
			}
		}
	}

	private Button createButtonToggleLegend(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Toggle the visibility of the embedded legend.");
		button.setImage(Activator.getDefault().getImage(Activator.ICON_LEGEND));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(embeddedLegend != null) {
					/*
					 * Show/Hide
					 */
					boolean draw = embeddedLegend.isDraw();
					embeddedLegend.setDraw(!draw);
					//
					if(scrollableChart != null) {
						scrollableChart.redraw();
					}
					//
					updateControls();
				}
			}
		});
		//
		return button;
	}

	private SeriesListUI createListSection(Composite parent) {

		SeriesListUI seriesListUI = new SeriesListUI(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		Table table = seriesListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		return seriesListUI;
	}

	private void createEmbeddedLegend() {

		if(scrollableChart != null) {
			BaseChart baseChart = scrollableChart.getBaseChart();
			embeddedLegend = new EmbeddedLegend(baseChart);
			embeddedLegend.setDraw(false);
			baseChart.getPlotArea().addCustomPaintListener(embeddedLegend);
			/*
			 * Left mouse double-click to get the position to place the legend.
			 */
			baseChart.addCustomPointSelectionHandler(new ICustomSelectionHandler() {

				@Override
				public void handleUserSelection(Event event) {

					if(embeddedLegend.isDraw()) {
						x = event.x;
						textX.setText(Integer.toString(x));
						y = event.y;
						textY.setText(Integer.toString(y));
					}
				}
			});
		}
	}

	private void updateControls() {

		if(embeddedLegend != null) {
			boolean enabled = embeddedLegend.isDraw();
			for(Control control : controls) {
				control.setEnabled(enabled);
			}
		}
	}
}
