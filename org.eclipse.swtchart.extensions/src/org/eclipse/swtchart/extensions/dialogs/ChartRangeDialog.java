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
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.dialogs;

import java.text.DecimalFormat;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.widgets.ExtendedCombo;

public class ChartRangeDialog extends TitleAreaDialog {

	private ScrollableChart scrollableChart;
	//
	private Text textStartX;
	private Text textStopX;
	private Combo comboScaleX;
	private Text textStartY;
	private Text textStopY;
	private Combo comboScaleY;
	//
	private ChartRangeValues chartRangeValues = new ChartRangeValues();

	public ChartRangeDialog(Shell parent, ScrollableChart scrollableChart) {

		super(parent);
		this.scrollableChart = scrollableChart;
	}

	public ChartRangeValues getChartRangeValues() {

		return chartRangeValues;
	}

	@Override
	public void create() {

		super.create();
		setTitle("Chart Range");
		setMessage("Set the chart to a defined range.");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite)super.createDialogArea(parent);
		//
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);
		//
		createSectionAxisX(composite);
		createSectionAxisY(composite);
		//
		initialize();
		//
		return container;
	}

	@Override
	protected void okPressed() {

		chartRangeValues.setAxisX(comboScaleX.getSelectionIndex());
		chartRangeValues.setStartX(textStartX.getText().trim());
		chartRangeValues.setStopX(textStopX.getText().trim());
		chartRangeValues.setAxisY(comboScaleY.getSelectionIndex());
		chartRangeValues.setStartY(textStartY.getText().trim());
		chartRangeValues.setStopY(textStopY.getText().trim());
		super.okPressed();
	}

	private void createSectionAxisX(Composite parent) {

		textStartX = new Text(parent, SWT.BORDER);
		textStartX.setText(""); //$NON-NLS-1$
		textStartX.setLayoutData(getTextGridData());
		//
		textStopX = new Text(parent, SWT.BORDER);
		textStopX.setText(""); //$NON-NLS-1$
		textStopX.setLayoutData(getTextGridData());
		//
		comboScaleX = ExtendedCombo.create(parent, SWT.READ_ONLY);
		comboScaleX.setLayoutData(getComboGridData());
		comboScaleX.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int selectionIndex = comboScaleX.getSelectionIndex();
				BaseChart baseChart = scrollableChart.getBaseChart();
				IAxis xAxis = baseChart.getAxisSet().getXAxis(selectionIndex);
				Range rangeX = xAxis.getRange();
				if(rangeX != null) {
					DecimalFormat decimalFormatX = baseChart.getDecimalFormat(IExtendedChart.X_AXIS, selectionIndex);
					textStartX.setText(decimalFormatX.format(rangeX.lower));
					textStopX.setText(decimalFormatX.format(rangeX.upper));
				}
			}
		});
	}

	private void createSectionAxisY(Composite parent) {

		textStartY = new Text(parent, SWT.BORDER);
		textStartY.setText(""); //$NON-NLS-1$
		textStartY.setLayoutData(getTextGridData());
		//
		textStopY = new Text(parent, SWT.BORDER);
		textStopY.setText(""); //$NON-NLS-1$
		textStopY.setLayoutData(getTextGridData());
		//
		comboScaleY = ExtendedCombo.create(parent, SWT.READ_ONLY);
		comboScaleY.setLayoutData(getComboGridData());
		comboScaleY.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int selectionIndex = comboScaleY.getSelectionIndex();
				BaseChart baseChart = scrollableChart.getBaseChart();
				IAxis yAxis = baseChart.getAxisSet().getYAxis(selectionIndex);
				Range rangeY = yAxis.getRange();
				if(rangeY != null) {
					DecimalFormat decimalFormatY = baseChart.getDecimalFormat(IExtendedChart.Y_AXIS, selectionIndex);
					textStartY.setText(decimalFormatY.format(rangeY.lower));
					textStopY.setText(decimalFormatY.format(rangeY.upper));
				}
			}
		});
	}

	private GridData getTextGridData() {

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 100;
		return gridData;
	}

	private GridData getComboGridData() {

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 100;
		return gridData;
	}

	private void initialize() {

		/*
		 * X Axes
		 */
		IChartSettings chartSettings = scrollableChart.getChartSettings();
		BaseChart baseChart = scrollableChart.getBaseChart();
		String[] axisLabelsX = baseChart.getAxisLabels(IExtendedChart.X_AXIS);
		comboScaleX.setItems(axisLabelsX);
		if(axisLabelsX.length > 0) {
			int selectedIndex = chartSettings.getRangeSelectorDefaultAxisX();
			if(selectedIndex >= 0 && selectedIndex < axisLabelsX.length) {
				comboScaleX.select(selectedIndex);
			} else {
				comboScaleX.select(0);
			}
		}
		/*
		 * Y Axes
		 */
		String[] axisLabelsY = baseChart.getAxisLabels(IExtendedChart.Y_AXIS);
		comboScaleY.setItems(axisLabelsY);
		if(axisLabelsY.length > 0) {
			int selectedIndex = chartSettings.getRangeSelectorDefaultAxisY();
			if(selectedIndex >= 0 && selectedIndex < axisLabelsY.length) {
				comboScaleY.select(selectedIndex);
			} else {
				comboScaleY.select(0);
			}
		}
		/*
		 * X/Y values
		 */
		setCurrentValues();
	}

	private void setCurrentValues() {

		BaseChart baseChart = scrollableChart.getBaseChart();
		//
		int indexX = (comboScaleX.getSelectionIndex() >= 0) ? comboScaleX.getSelectionIndex() : BaseChart.ID_PRIMARY_X_AXIS;
		int indexY = (comboScaleY.getSelectionIndex() >= 0) ? comboScaleY.getSelectionIndex() : BaseChart.ID_PRIMARY_Y_AXIS;
		IAxis xAxis = baseChart.getAxisSet().getXAxis(indexX);
		IAxis yAxis = baseChart.getAxisSet().getYAxis(indexY);
		Range rangeX = xAxis.getRange();
		Range rangeY = yAxis.getRange();
		//
		DecimalFormat decimalFormatX = baseChart.getDecimalFormat(IExtendedChart.X_AXIS, indexX);
		DecimalFormat decimalFormatY = baseChart.getDecimalFormat(IExtendedChart.Y_AXIS, indexY);
		/*
		 * Update the text boxes.
		 */
		if(rangeX != null && rangeY != null) {
			textStartX.setText(decimalFormatX.format(rangeX.lower));
			textStopX.setText(decimalFormatX.format(rangeX.upper));
			textStartY.setText(decimalFormatY.format(rangeY.lower));
			textStopY.setText(decimalFormatY.format(rangeY.upper));
		} else {
			textStartX.setText("");
			textStopX.setText("");
			textStartY.setText("");
			textStopY.setText("");
		}
	}
}