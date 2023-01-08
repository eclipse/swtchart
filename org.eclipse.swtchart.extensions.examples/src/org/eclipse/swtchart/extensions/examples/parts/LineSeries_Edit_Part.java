/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.customcharts.core.ChromatogramChart;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.examples.Activator;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class LineSeries_Edit_Part extends Composite {

	private static final int NUM_SERIES = 5;
	private TabFolder tabFolder;
	/*
	 * Chart
	 */
	private Combo comboSelectSeries;
	private Text textShiftX;
	private Combo comboScaleX;
	private Text textShiftY;
	private Combo comboScaleY;
	//
	// private int shiftConstraintRangeSelection;
	private int shiftConstraintDeleteX;
	private int shiftConstraintDeleteY;
	// private int shiftConstraintClinchX;
	// private int shiftConstraintStretchX;
	// private int shiftConstraintBroadenX;
	// private int shiftConstraintNarrowX;
	//
	private ChromatogramChart chromatogramChart;
	//
	private Map<Integer, Table> shiftTableMap;

	@Inject
	public LineSeries_Edit_Part(Composite parent) {

		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		try {
			initialize();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {

		shiftTableMap = new HashMap<Integer, Table>();
		//
		this.setLayout(new GridLayout(1, true));
		tabFolder = new TabFolder(this, SWT.BOTTOM);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int index = tabFolder.getSelectionIndex();
				Table table = shiftTableMap.get(index);
				List<double[]> dataShifts = chromatogramChart.getBaseChart().getDataShiftHistory(SeriesConverter.LINE_SERIES + "4_" + index);
				table.removeAll();
				for(double[] dataShift : dataShifts) {
					addTableRow(table, dataShift);
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
		composite.setLayout(new GridLayout(1, true));
		createChartSection(composite);
		tabItem.setControl(composite);
	}

	private void createTableTabItems() {

		for(int i = 1; i <= NUM_SERIES; i++) {
			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText(SeriesConverter.LINE_SERIES + "4_" + i);
			Composite composite = new Composite(tabFolder, SWT.NONE);
			composite.setLayout(new GridLayout(1, true));
			Table table = createTableSection(composite);
			shiftTableMap.put(i, table);
			tabItem.setControl(composite);
		}
	}

	private Table createTableSection(Composite parent) {

		Table table = new Table(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		String labelAxisX = chromatogramChart.getBaseChart().getXAxisSettings(BaseChart.ID_PRIMARY_X_AXIS).getLabel();
		String labelAxisY = chromatogramChart.getBaseChart().getYAxisSettings(BaseChart.ID_PRIMARY_Y_AXIS).getLabel();
		//
		addTableColumn(table, "Start [" + labelAxisX + "]", 150);
		addTableColumn(table, "Stop [" + labelAxisX + "]", 150);
		addTableColumn(table, "Shift [" + labelAxisX + "]", 150);
		addTableColumn(table, "Start [" + labelAxisY + "]", 150);
		addTableColumn(table, "Stop [" + labelAxisY + "]", 150);
		addTableColumn(table, "Shift [" + labelAxisY + "]", 150);
		addTableColumn(table, "Shift Constraints", 150);
		//
		return table;
	}

	private void addTableColumn(Table table, String column, int width) {

		TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText(column);
		tableColumn.setWidth(width);
	}

	public void addTableRow(Table table, double[] dataShift) {

		int size = dataShift.length;
		String[] row = new String[size];
		for(int i = 0; i < size; i++) {
			row[i] = Double.toString(dataShift[i]);
		}
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(row);
	}

	private void createChartSection(Composite parent) {

		/*
		 * Buttons
		 */
		Composite compositeButtons = new Composite(parent, SWT.NONE);
		GridData gridDataComposite = new GridData(GridData.FILL_HORIZONTAL);
		gridDataComposite.horizontalAlignment = SWT.BEGINNING;
		compositeButtons.setLayoutData(gridDataComposite);
		compositeButtons.setLayout(new GridLayout(18, false));
		//
		createLabel(compositeButtons);
		createCombo(compositeButtons);
		createTextShiftX(compositeButtons);
		createComboScaleX(compositeButtons);
		createButtonLeft(compositeButtons);
		createButtonRight(compositeButtons);
		createTextShiftY(compositeButtons);
		createComboScaleY(compositeButtons);
		createButtonUp(compositeButtons);
		createButtonDown(compositeButtons);
		// createButtonConstraintRangeSelection(compositeButtons);
		createButtonConstraintDeleteX(compositeButtons);
		createButtonConstraintDeleteY(compositeButtons);
		// createButtonConstraintClinchX(compositeButtons);
		// createButtonConstraintStrechX(compositeButtons);
		// createButtonConstraintBroadenX(compositeButtons);
		// createButtonConstraintNarrowX(compositeButtons);
		createButtonReset(compositeButtons);
		//
		createChart(parent);
	}

	private void createLabel(Composite parent) {

		Label label = new Label(parent, SWT.NONE);
		label.setText("Selected Data Series:");
	}

	private void createCombo(Composite parent) {

		comboSelectSeries = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		comboSelectSeries.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				selectSeries();
			}
		});
	}

	private void createTextShiftX(Composite parent) {

		textShiftX = new Text(parent, SWT.BORDER);
		textShiftX.setText("");
		textShiftX.setLayoutData(getGridData());
	}

	private void createComboScaleX(Composite parent) {

		comboScaleX = new Combo(parent, SWT.READ_ONLY);
		comboScaleX.setLayoutData(getGridData());
	}

	private void createButtonLeft(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Move Left");
		button.setText(Activator.getDefault() != null ? "" : "Move Left");
		button.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_LEFT) : null);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				BaseChart baseChart = chromatogramChart.getBaseChart();
				double shiftX = getShift(IExtendedChart.X_AXIS) * -1.0d;
				String selectedSeriesId = comboSelectSeries.getText().trim();
				baseChart.shiftSeries(selectedSeriesId, shiftX, 0.0d);
				baseChart.redraw();
			}
		});
	}

	private void createButtonRight(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Move Right");
		button.setText(Activator.getDefault() != null ? "" : "Move Right");
		button.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_RIGHT) : null);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				BaseChart baseChart = chromatogramChart.getBaseChart();
				double shiftX = getShift(IExtendedChart.X_AXIS);
				String selectedSeriesId = comboSelectSeries.getText().trim();
				baseChart.shiftSeries(selectedSeriesId, shiftX, 0.0d);
				baseChart.redraw();
			}
		});
	}

	private void createTextShiftY(Composite parent) {

		textShiftY = new Text(parent, SWT.BORDER);
		textShiftY.setText("");
		textShiftY.setLayoutData(getGridData());
	}

	private void createComboScaleY(Composite parent) {

		comboScaleY = new Combo(parent, SWT.READ_ONLY);
		comboScaleY.setLayoutData(getGridData());
	}

	private void createButtonUp(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Move Up");
		button.setText(Activator.getDefault() != null ? "" : "Move Up");
		button.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_UP) : null);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				BaseChart baseChart = chromatogramChart.getBaseChart();
				double shiftY = getShift(IExtendedChart.Y_AXIS);
				String selectedSeriesId = comboSelectSeries.getText().trim();
				baseChart.shiftSeries(selectedSeriesId, 0.0d, shiftY);
				baseChart.redraw();
			}
		});
	}

	private void createButtonDown(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Move Down");
		button.setText(Activator.getDefault() != null ? "" : "Move Down");
		button.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_DOWN) : null);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				BaseChart baseChart = chromatogramChart.getBaseChart();
				double shiftY = getShift(IExtendedChart.Y_AXIS) * -1.0d;
				String selectedSeriesId = comboSelectSeries.getText().trim();
				baseChart.shiftSeries(selectedSeriesId, 0.0d, shiftY);
				baseChart.redraw();
			}
		});
	}

	// private void createButtonConstraintRangeSelection(Composite parent) {
	//
	// Button button = new Button(parent, SWT.CHECK);
	// button.setText("Range Selection");
	// button.setSelection(false);
	// button.addSelectionListener(new SelectionAdapter() {
	//
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	//
	// if(button.getSelection()) {
	// shiftConstraintRangeSelection = BaseChart.SHIFT_CONSTRAINT_RANGE_SELECTION;
	// } else {
	// shiftConstraintRangeSelection = 0;
	// }
	// setShiftConstraints();
	// }
	// });
	// }
	private void createButtonConstraintDeleteX(Composite parent) {

		Button button = new Button(parent, SWT.CHECK);
		button.setText("Delete X");
		button.setSelection(false);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(button.getSelection()) {
					shiftConstraintDeleteX = BaseChart.SHIFT_CONSTRAINT_DELETE_X;
				} else {
					shiftConstraintDeleteX = 0;
				}
				setShiftConstraints();
			}
		});
	}

	private void createButtonConstraintDeleteY(Composite parent) {

		Button button = new Button(parent, SWT.CHECK);
		button.setText("Delete Y");
		button.setSelection(false);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(button.getSelection()) {
					shiftConstraintDeleteY = BaseChart.SHIFT_CONSTRAINT_DELETE_Y;
				} else {
					shiftConstraintDeleteY = 0;
				}
				setShiftConstraints();
			}
		});
	}

	// private void createButtonConstraintClinchX(Composite parent) {
	//
	// Button button = new Button(parent, SWT.CHECK);
	// button.setText("Clinch X");
	// button.setSelection(false);
	// button.addSelectionListener(new SelectionAdapter() {
	//
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	//
	// if(button.getSelection()) {
	// shiftConstraintClinchX = BaseChart.SHIFT_CONSTRAINT_CLINCH_X;
	// } else {
	// shiftConstraintClinchX = 0;
	// }
	// setShiftConstraints();
	// }
	// });
	// }
	// private void createButtonConstraintStrechX(Composite parent) {
	//
	// Button button = new Button(parent, SWT.CHECK);
	// button.setText("Stretch X");
	// button.setSelection(false);
	// button.addSelectionListener(new SelectionAdapter() {
	//
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	//
	// if(button.getSelection()) {
	// shiftConstraintStretchX = BaseChart.SHIFT_CONSTRAINT_STRETCH_X;
	// } else {
	// shiftConstraintStretchX = 0;
	// }
	// setShiftConstraints();
	// }
	// });
	// }
	//
	// private void createButtonConstraintBroadenX(Composite parent) {
	//
	// Button button = new Button(parent, SWT.CHECK);
	// button.setText("Broaden X");
	// button.setSelection(false);
	// button.addSelectionListener(new SelectionAdapter() {
	//
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	//
	// if(button.getSelection()) {
	// shiftConstraintBroadenX = BaseChart.SHIFT_CONSTRAINT_BROADEN_X;
	// } else {
	// shiftConstraintBroadenX = 0;
	// }
	// setShiftConstraints();
	// }
	// });
	// }
	//
	// private void createButtonConstraintNarrowX(Composite parent) {
	//
	// Button button = new Button(parent, SWT.CHECK);
	// button.setText("Narrow X");
	// button.setSelection(false);
	// button.addSelectionListener(new SelectionAdapter() {
	//
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	//
	// if(button.getSelection()) {
	// shiftConstraintNarrowX = BaseChart.SHIFT_CONSTRAINT_NARROW_X;
	// } else {
	// shiftConstraintNarrowX = 0;
	// }
	// setShiftConstraints();
	// }
	// });
	// }
	private void createButtonReset(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the data");
		button.setText(Activator.getDefault() != null ? "" : "Reset");
		button.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_RESET) : null);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				loadChromatogramData();
			}
		});
	}

	private void createChart(Composite parent) {

		chromatogramChart = new ChromatogramChart(parent, SWT.BORDER);
		chromatogramChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		IChartSettings chartSettings = chromatogramChart.getChartSettings();
		chartSettings.setCreateMenu(true);
		chartSettings.setSupportDataShift(true);
		chromatogramChart.applySettings(chartSettings);
		//
		loadChromatogramData();
	}

	private void loadChromatogramData() {

		chromatogramChart.deleteSeries();
		//
		Map<Integer, Color> colors = new HashMap<Integer, Color>();
		colors.put(1, getDisplay().getSystemColor(SWT.COLOR_RED));
		colors.put(2, getDisplay().getSystemColor(SWT.COLOR_BLACK));
		colors.put(3, getDisplay().getSystemColor(SWT.COLOR_GRAY));
		colors.put(4, getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
		colors.put(5, getDisplay().getSystemColor(SWT.COLOR_GRAY));
		//
		String[] items = new String[6];
		items[0] = "No Selection";
		//
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		for(int i = 1; i <= NUM_SERIES; i++) {
			ISeriesData seriesData = SeriesConverter.getSeriesXY(SeriesConverter.LINE_SERIES + "4_" + i);
			items[i] = seriesData.getId();
			ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
			ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
			lineSeriesSettings.setLineColor(colors.get(i));
			lineSeriesSettings.setEnableArea(false);
			ILineSeriesSettings lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
			lineSeriesSettingsHighlight.setLineWidth(2);
			lineSeriesDataList.add(lineSeriesData);
		}
		//
		chromatogramChart.addSeriesData(lineSeriesDataList, LineChart.MEDIUM_COMPRESSION);
		comboSelectSeries.setItems(items);
		comboSelectSeries.select(1); // LineSeries4_1
		selectSeries();
		//
		setComboAxisItems();
	}

	public void setComboAxisItems() {

		/*
		 * X Axes
		 */
		BaseChart baseChart = chromatogramChart.getBaseChart();
		String[] axisLabelsX = baseChart.getAxisLabels(IExtendedChart.X_AXIS);
		comboScaleX.setItems(axisLabelsX);
		if(axisLabelsX.length > 0) {
			int selectedIndex = 1; // "Minutes"
			if(selectedIndex >= 0 && selectedIndex < axisLabelsX.length) {
				comboScaleX.select(selectedIndex);
				textShiftX.setText("0.5");
			} else {
				comboScaleX.select(0); // Milliseconds
				textShiftX.setText("10000");
			}
		}
		/*
		 * Y Axes
		 */
		String[] axisLabelsY = baseChart.getAxisLabels(IExtendedChart.Y_AXIS);
		comboScaleY.setItems(axisLabelsY);
		if(axisLabelsY.length > 0) {
			int selectedIndex = 1; // "Relative Intensity [%]"
			if(selectedIndex >= 0 && selectedIndex < axisLabelsY.length) {
				comboScaleY.select(selectedIndex);
				textShiftY.setText("1.2");
			} else {
				comboScaleY.select(0); // Intensity
				textShiftY.setText("100000");
			}
		}
	}

	private void setShiftConstraints() {

		// int shiftConstraints = shiftConstraintSelection | shiftConstraintDeleteX | shiftConstraintDeleteY | shiftConstraintClinchX | shiftConstraintStretchX | shiftConstraintBroadenX | shiftConstraintNarrowX;
		int shiftConstraints = shiftConstraintDeleteX | shiftConstraintDeleteY;
		BaseChart baseChart = chromatogramChart.getBaseChart();
		baseChart.setShiftConstraints(shiftConstraints);
	}

	private double getShift(String axis) {

		double shiftValue = 0.0d;
		try {
			/*
			 * Try to calculate the primary unit.
			 */
			BaseChart baseChart = chromatogramChart.getBaseChart();
			DecimalFormat decimalFormat;
			int selectedAxis;
			//
			if(axis.equals(IExtendedChart.X_AXIS)) {
				selectedAxis = comboScaleX.getSelectionIndex();
				decimalFormat = baseChart.getDecimalFormat(IExtendedChart.X_AXIS, selectedAxis);
			} else {
				selectedAxis = comboScaleY.getSelectionIndex();
				decimalFormat = baseChart.getDecimalFormat(IExtendedChart.Y_AXIS, selectedAxis);
			}
			//
			double secondaryValue;
			if(axis.equals(IExtendedChart.X_AXIS)) {
				secondaryValue = decimalFormat.parse(textShiftX.getText().trim()).doubleValue();
			} else {
				secondaryValue = decimalFormat.parse(textShiftY.getText().trim()).doubleValue();
			}
			/*
			 * Convert the range on demand.
			 */
			if(selectedAxis == 0) {
				shiftValue = secondaryValue;
			} else {
				IAxisScaleConverter axisScaleConverter = baseChart.getAxisScaleConverter(axis, selectedAxis);
				shiftValue = axisScaleConverter.convertToPrimaryUnit(secondaryValue);
			}
		} catch(ParseException e) {
			e.printStackTrace();
		}
		//
		return shiftValue;
	}

	private void selectSeries() {

		BaseChart baseChart = chromatogramChart.getBaseChart();
		String selectedSeriesId = comboSelectSeries.getText().trim();
		baseChart.resetSeriesSettings();
		baseChart.selectSeries(selectedSeriesId);
		baseChart.redraw();
	}

	private GridData getGridData() {

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		return gridData;
	}
}
