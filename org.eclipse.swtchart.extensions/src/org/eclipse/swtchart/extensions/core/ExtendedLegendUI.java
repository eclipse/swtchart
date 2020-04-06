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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.Activator;
import org.eclipse.swtchart.extensions.internal.marker.EmbeddedLegend;

public class ExtendedLegendUI extends Composite {

	private SeriesListUI seriesListUI;
	private ScrollableChart scrollableChart;
	private EmbeddedLegend embeddedLegend;
	//
	private int x = 0;
	private int y = 0;

	public ExtendedLegendUI(Composite parent, int style) {
		super(parent, style);
		createControl();
	}

	public void setScrollableChart(ScrollableChart scrollableChart) {

		this.scrollableChart = scrollableChart;
		embeddedLegend = createEmbeddedLegend();
	}

	public void setInput(Object input) {

		seriesListUI.setInput(input);
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		seriesListUI = createListSection(this);
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(2, false));
		//
		createButtonPositionLegend(composite);
		createButtonToggleLegend(composite);
	}

	private Button createButtonPositionLegend(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Toggle the visibility of the embedded legend.");
		button.setImage(Activator.getDefault().getImage(Activator.ICON_POSITION));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(embeddedLegend != null) {
					embeddedLegend.setX(x);
					embeddedLegend.setY(y);
					if(scrollableChart != null) {
						scrollableChart.redraw();
					}
				}
			}
		});
		//
		return button;
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
					boolean draw = embeddedLegend.isDraw();
					embeddedLegend.setDraw(!draw);
					//
					if(scrollableChart != null) {
						scrollableChart.redraw();
					}
				}
			}
		});
		//
		return button;
	}

	private SeriesListUI createListSection(Composite parent) {

		SeriesListUI listUI = new SeriesListUI(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		listUI.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		return listUI;
	}

	private EmbeddedLegend createEmbeddedLegend() {

		EmbeddedLegend embeddedLegend = null;
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

					x = event.x;
					y = event.y;
				}
			});
		}
		return embeddedLegend;
	}
}
