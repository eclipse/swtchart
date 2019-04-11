/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.examples.parts;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.customcharts.PCAChart;
import org.eclipse.swtchart.extensions.examples.Activator;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesData;

public class ScatterSeries_Edit_Part extends Composite {

	private Color COLOR_RED = getDisplay().getSystemColor(SWT.COLOR_RED);
	private Color COLOR_BLUE = getDisplay().getSystemColor(SWT.COLOR_BLUE);
	private Color COLOR_MAGENTA = getDisplay().getSystemColor(SWT.COLOR_MAGENTA);
	private Color COLOR_CYAN = getDisplay().getSystemColor(SWT.COLOR_CYAN);
	private Color COLOR_GRAY = getDisplay().getSystemColor(SWT.COLOR_GRAY);
	//
	private int SYMBOL_SIZE = 8;
	//
	private static final int KEY_CODE_R = 114;
	private static final int KEY_CODE_S = 115;
	private boolean isCustomSelection = false;
	//
	private TabFolder tabFolder;
	private Table table;
	private HandledChart handledChart;
	private Button buttonSetCustomSelection;

	private class HandledChart extends PCAChart {

		public HandledChart(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void handleKeyUpEvent(Event event) {

			super.handleKeyUpEvent(event);
			isCustomSelection = false;
			//
			if(event.keyCode == KEY_CODE_R) {
				/*
				 * Reset Selection
				 */
				BaseChart baseChart = getBaseChart();
				baseChart.resetSeriesSettings();
			} else if(event.keyCode == KEY_CODE_S) {
				/*
				 * Custom Selection
				 */
				isCustomSelection = true;
			}
			//
			enableButtons();
		}

		@Override
		public void handleMouseUpEvent(Event event) {

			super.handleMouseUpEvent(event);
			//
			if(isCustomSelection) {
				/*
				 * Set Selection
				 */
				BaseChart baseChart = getBaseChart();
				Rectangle plotAreaBounds = baseChart.getPlotArea().getBounds();
				ISeries[] series = baseChart.getSeriesSet().getSeries();
				//
				for(ISeries scatterSeries : series) {
					if(scatterSeries != null) {
						//
						// double[] xSeries = scatterSeries.getXSeries();
						// double[] ySeries = scatterSeries.getYSeries();
						int size = scatterSeries.getXSeries().length;
						String id = scatterSeries.getId();
						//
						for(int i = 0; i < size; i++) {
							Point point = scatterSeries.getPixelCoordinates(i);
							if(isPointVisible(point, plotAreaBounds)) {
								// System.out.println("\t" + xSeries[i] + "\t" + ySeries[i] + "\t" + point + "\t" + id);
								baseChart.selectSeries(id);
							}
						}
					}
				}
				//
				baseChart.redraw();
				isCustomSelection = false;
			}
			//
			enableButtons();
		}
	}

	@Inject
	public ScatterSeries_Edit_Part(Composite parent) {
		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		try {
			initialize();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	private void initialize() {

		this.setLayout(new GridLayout(1, true));
		tabFolder = new TabFolder(this, SWT.BOTTOM);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		tabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int index = tabFolder.getSelectionIndex();
				if(index == 1) {
					table.removeAll();
					BaseChart baseChart = handledChart.getBaseChart();
					for(String selectedSeriesId : baseChart.getSelectedSeriesIds()) {
						addTableRow(table, selectedSeriesId);
					}
				}
			}
		});
		createChartTabItem();
		createTableTabItems();
	}

	private void createChartTabItem() {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Chart");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(1, true));
		createChartSection(composite);
		tabItem.setControl(composite);
	}

	private void createTableTabItems() {

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Selected Series");
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		table = createTableSection(composite);
		tabItem.setControl(composite);
	}

	private Table createTableSection(Composite parent) {

		Table table = new Table(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		addTableColumn(table, "Selected Series", 500);
		//
		return table;
	}

	private void addTableColumn(Table table, String column, int width) {

		TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText(column);
		tableColumn.setWidth(width);
	}

	public void addTableRow(Table table, String selectedSeries) {

		String[] row = new String[1];
		row[0] = selectedSeries;
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(row);
	}

	private void createChartSection(Composite parent) {

		/*
		 * Buttons
		 */
		Composite compositeButtons = new Composite(parent, SWT.NONE);
		compositeButtons.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridData gridDataComposite = new GridData(GridData.FILL_HORIZONTAL);
		gridDataComposite.horizontalAlignment = SWT.END;
		compositeButtons.setLayoutData(gridDataComposite);
		compositeButtons.setLayout(new GridLayout(2, false));
		//
		createButtonEnableSelection(compositeButtons);
		createButtonReset(compositeButtons);
		//
		createChart(parent);
	}

	private void createButtonEnableSelection(Composite parent) {

		buttonSetCustomSelection = new Button(parent, SWT.PUSH);
		buttonSetCustomSelection.setToolTipText("Enable Custom Selection");
		buttonSetCustomSelection.setText(Activator.getDefault() != null ? "" : "Custom Selection (s)");
		buttonSetCustomSelection.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_START) : null);
		buttonSetCustomSelection.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				isCustomSelection = true;
				enableButtons();
			}
		});
	}

	private void createButtonReset(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the data");
		button.setText(Activator.getDefault() != null ? "" : "Reset");
		button.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_RESET) : null);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				isCustomSelection = false;
				BaseChart baseChart = handledChart.getBaseChart();
				baseChart.resetSeriesSettings();
				enableButtons();
			}
		});
	}

	private void createChart(Composite parent) {

		handledChart = new HandledChart(parent, SWT.BORDER);
		handledChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		IChartSettings chartSettings = handledChart.getChartSettings();
		chartSettings.setCreateMenu(true);
		chartSettings.setSupportDataShift(true);
		handledChart.applySettings(chartSettings);
		//
		loadScatterData();
	}

	private void loadScatterData() {

		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = handledChart.getChartSettings();
		chartSettings.setCreateMenu(true);
		handledChart.applySettings(chartSettings);
		/*
		 * Data
		 */
		List<ISeriesData> scatterSeriesList = SeriesConverter.getSeriesScatter(SeriesConverter.SCATTER_SERIES_1);
		List<IScatterSeriesData> scatterSeriesDataList = new ArrayList<IScatterSeriesData>();
		//
		for(ISeriesData seriesData : scatterSeriesList) {
			IScatterSeriesData scatterSeriesData = new ScatterSeriesData(seriesData);
			IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getSettings();
			/*
			 * Set the color and symbol type.
			 */
			double x = seriesData.getXSeries()[0];
			double y = seriesData.getYSeries()[0];
			applySettings(scatterSeriesSettings, x, y, SYMBOL_SIZE);
			scatterSeriesDataList.add(scatterSeriesData);
		}
		/*
		 * Set series.
		 */
		handledChart.addSeriesData(scatterSeriesDataList);
	}

	private boolean isPointVisible(Point point, Rectangle plotAreaBounds) {

		if(point.x >= 0 && point.x <= plotAreaBounds.width && point.y >= 0 && point.y <= plotAreaBounds.height) {
			return true;
		} else {
			return false;
		}
	}

	private void applySettings(IScatterSeriesSettings scatterSeriesSettings, double x, double y, int symbolSize) {

		scatterSeriesSettings.setSymbolSize(SYMBOL_SIZE);
		//
		if(x > 0 && y > 0) {
			scatterSeriesSettings.setSymbolColor(COLOR_RED);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.SQUARE);
		} else if(x > 0 && y < 0) {
			scatterSeriesSettings.setSymbolColor(COLOR_BLUE);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.TRIANGLE);
		} else if(x < 0 && y > 0) {
			scatterSeriesSettings.setSymbolColor(COLOR_MAGENTA);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.DIAMOND);
		} else if(x < 0 && y < 0) {
			scatterSeriesSettings.setSymbolColor(COLOR_CYAN);
			scatterSeriesSettings.setSymbolType(PlotSymbolType.INVERTED_TRIANGLE);
		}
		//
		IScatterSeriesSettings scatterSeriesSettingsHighlight = (IScatterSeriesSettings)scatterSeriesSettings.getSeriesSettingsHighlight();
		scatterSeriesSettingsHighlight.setSymbolColor(COLOR_GRAY);
		scatterSeriesSettingsHighlight.setSymbolType(PlotSymbolType.CIRCLE);
		scatterSeriesSettingsHighlight.setSymbolSize(symbolSize);
	}

	private void enableButtons() {

		buttonSetCustomSelection.setEnabled((isCustomSelection) ? false : true);
	}
}
