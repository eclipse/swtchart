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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.NavigableSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ICustomSelectionHandler;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.examples.Activator;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class LineSeries_Selection_Part extends Composite {

	private Text textRangeXStart;
	private Text textRangeXStop;
	private Text textRangeYStart;
	private Text textRangeYStop;
	private Text textX;
	private Text textY;
	//
	private static final String DATA_POINT_SERIES = "DATA_POINT_SERIES";
	private NavigableSet<Double> xValues;
	private HashMap<Double, Double> yValues;
	private ISeriesData seriesDataPoint;
	private LineChart lineChart;

	@Inject
	public LineSeries_Selection_Part(Composite parent) {
		super(parent, SWT.NONE);
		try {
			initialize();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void dispose() {

		super.dispose();
	}

	private void initialize() throws Exception {

		this.setLayout(new GridLayout(1, true));
		this.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//
		Composite compositeInfo = new Composite(this, SWT.NONE);
		GridData gridDataComposite = new GridData(GridData.FILL_HORIZONTAL);
		gridDataComposite.horizontalAlignment = SWT.BEGINNING;
		compositeInfo.setLayoutData(gridDataComposite);
		compositeInfo.setLayout(new GridLayout(13, false));
		//
		createLabel(compositeInfo, "X-Start:");
		textRangeXStart = createText(compositeInfo);
		createLabel(compositeInfo, "X-Stop:");
		textRangeXStop = createText(compositeInfo);
		createLabel(compositeInfo, "Y-Start:");
		textRangeYStart = createText(compositeInfo);
		createLabel(compositeInfo, "Y-Stop:");
		textRangeYStop = createText(compositeInfo);
		createLabel(compositeInfo, "X:");
		textX = createText(compositeInfo);
		createLabel(compositeInfo, "Y:");
		textY = createText(compositeInfo);
		createButtonReset(compositeInfo);
		//
		lineChart = new LineChart(this, SWT.NONE);
		lineChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		lineChart.getBaseChart().addCustomRangeSelectionHandler(new ICustomSelectionHandler() {

			@Override
			public void handleUserSelection(Event event) {

				BaseChart baseChart = lineChart.getBaseChart();
				Range rangeX = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
				Range rangeY = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();
				DecimalFormat decimalFormatX = baseChart.getDecimalFormat(IExtendedChart.X_AXIS, BaseChart.ID_PRIMARY_X_AXIS);
				DecimalFormat decimalFormatY = baseChart.getDecimalFormat(IExtendedChart.Y_AXIS, BaseChart.ID_PRIMARY_Y_AXIS);
				textRangeXStart.setText(decimalFormatX.format(rangeX.lower));
				textRangeXStop.setText(decimalFormatX.format(rangeX.upper));
				textRangeYStart.setText(decimalFormatY.format(rangeY.lower));
				textRangeYStop.setText(decimalFormatY.format(rangeY.upper));
			}
		});
		lineChart.getBaseChart().addCustomPointSelectionHandler(new ICustomSelectionHandler() {

			@Override
			public void handleUserSelection(Event event) {

				BaseChart baseChart = lineChart.getBaseChart();
				double x = baseChart.getSelectedPrimaryAxisValue(event.x, IExtendedChart.X_AXIS);
				double y = baseChart.getSelectedPrimaryAxisValue(event.y, IExtendedChart.Y_AXIS);
				//
				DecimalFormat decimalFormatX = baseChart.getDecimalFormat(IExtendedChart.X_AXIS, BaseChart.ID_PRIMARY_X_AXIS);
				DecimalFormat decimalFormatY = baseChart.getDecimalFormat(IExtendedChart.Y_AXIS, BaseChart.ID_PRIMARY_Y_AXIS);
				textX.setText(decimalFormatX.format(x));
				textY.setText(decimalFormatY.format(y));
				//
				try {
					ISeries series = baseChart.getSeriesSet().getSeries(DATA_POINT_SERIES);
					double xSelected = xValues.floor(x);
					double ySelected = yValues.get(xSelected);
					double[] xSeries = new double[]{xSelected};
					double[] ySeries = new double[]{ySelected};
					series.setXSeries(xSeries);
					series.setYSeries(ySeries);
					baseChart.redraw();
				} catch(Exception e) {
					//
				}
			}
		});
		applyChartSettings();
	}

	private void createLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}

	private Text createText(Composite parent) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText("");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 150;
		text.setLayoutData(gridData);
		return text;
	}

	private void createButtonReset(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Reset the data");
		button.setText(Activator.getDefault() != null ? "" : "Reset");
		button.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_RESET) : null);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				textRangeXStart.setText("");
				textRangeXStop.setText("");
				textRangeYStart.setText("");
				textRangeYStop.setText("");
				textX.setText("");
				textY.setText("");
				lineChart.adjustRange(true);
				lineChart.redraw();
			}
		});
	}

	private void applyChartSettings() throws Exception {

		/*
		 * Chart Settings
		 */
		IChartSettings chartSettings = lineChart.getChartSettings();
		chartSettings.setCreateMenu(true);
		/*
		 * Primary X-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("Retention Time (milliseconds)");
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.0##"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsX.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		/*
		 * Primary Y-Axis
		 */
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("Intensity");
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.0#E0"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		primaryAxisSettingsY.setGridLineStyle(LineStyle.NONE);
		/*
		 * Secondary Y-Axes
		 */
		ISecondaryAxisSettings secondaryAxisSettingsY1 = new SecondaryAxisSettings("Relative Intensity [%]", new PercentageConverter(SWT.VERTICAL, true));
		secondaryAxisSettingsY1.setPosition(Position.Secondary);
		secondaryAxisSettingsY1.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
		secondaryAxisSettingsY1.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY1);
		//
		lineChart.applySettings(chartSettings);
		/*
		 * Create series.
		 */
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		ISeriesData seriesDataLine = SeriesConverter.getSeriesXY(SeriesConverter.LINE_SERIES_3);
		double[] xSeries = seriesDataLine.getXSeries();
		double[] ySeries = seriesDataLine.getYSeries();
		xValues = new TreeSet<Double>();
		yValues = new HashMap<Double, Double>();
		for(int i = 0; i < xSeries.length; i++) {
			double x = xSeries[i];
			xValues.add(x);
			yValues.put(x, ySeries[i]);
		}
		/*
		 * Line Series
		 */
		ILineSeriesData lineSeriesData;
		ILineSeriesSettings lineSeriesSettings;
		//
		lineSeriesData = new LineSeriesData(seriesDataLine);
		lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setEnableArea(true);
		ILineSeriesSettings lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
		lineSeriesSettingsHighlight.setLineWidth(2);
		lineSeriesDataList.add(lineSeriesData);
		/*
		 * Selected Point
		 */
		seriesDataPoint = new SeriesData(new double[]{0.0}, new double[]{0.0}, DATA_POINT_SERIES);
		lineSeriesData = new LineSeriesData(seriesDataPoint);
		lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setSymbolSize(5);
		lineSeriesSettings.setSymbolType(PlotSymbolType.CROSS);
		lineSeriesSettings.setEnableArea(true);
		lineSeriesDataList.add(lineSeriesData);
		/*
		 * Set series.
		 */
		lineChart.addSeriesData(lineSeriesDataList);
	}
}
