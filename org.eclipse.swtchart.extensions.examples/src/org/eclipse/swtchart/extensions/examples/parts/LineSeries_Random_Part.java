/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.customcharts.core.ChromatogramChart;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.examples.Activator;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

import jakarta.inject.Inject;

public class LineSeries_Random_Part extends Composite {

	private static final String ID = "LINE_SERIES_RANDOM";
	private static int x = 0;
	private static int xDelta = 10;
	//
	private Button buttonStart;
	private Button buttonStop;
	private Button buttonReset;
	private ChromatogramChart chromatogramChart;
	//
	private Display display = getDisplay();
	private Acquisition acquisition;
	private Recording recording;

	@Inject
	public LineSeries_Random_Part(Composite parent) {

		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		try {
			initialize();
			acquisition = new Acquisition();
			recording = new Recording();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private class Acquisition {

		private boolean recordData = false;

		public boolean isRecordData() {

			return recordData;
		}

		public void setRecordData(boolean recordData) {

			this.recordData = recordData;
		}
	}

	private class Recording implements Runnable {

		@Override
		public void run() {

			if(acquisition.isRecordData()) {
				chromatogramChart.appendSeries(getRandomSeriesData());
			}
			display.timerExec(500, this);
		}
	}

	private void initialize() throws Exception {

		this.setLayout(new GridLayout(1, true));
		/*
		 * Buttons
		 */
		Composite compositeButtons = new Composite(this, SWT.NONE);
		GridData gridDataComposite = new GridData(GridData.FILL_HORIZONTAL);
		gridDataComposite.horizontalAlignment = SWT.END;
		compositeButtons.setLayoutData(gridDataComposite);
		compositeButtons.setLayout(new GridLayout(3, false));
		//
		buttonStart = new Button(compositeButtons, SWT.PUSH);
		buttonStart.setToolTipText("Start Recording");
		buttonStart.setText(Activator.getDefault() != null ? "" : "Start");
		buttonStart.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_START) : null);
		buttonStart.setEnabled(true);
		buttonStart.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				acquisition.setRecordData(true);
				display.asyncExec(recording);
				//
				setButtonsEnabled(true);
			}
		});
		//
		buttonStop = new Button(compositeButtons, SWT.PUSH);
		buttonStop.setToolTipText("Stop Recording");
		buttonStop.setText(Activator.getDefault() != null ? "" : "Stop");
		buttonStop.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_STOP) : null);
		buttonStop.setEnabled(false);
		buttonStop.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				acquisition.setRecordData(false);
				display.timerExec(-1, recording);
				//
				setButtonsEnabled(false);
			}
		});
		//
		buttonReset = new Button(compositeButtons, SWT.PUSH);
		buttonReset.setToolTipText("Reset");
		buttonReset.setText(Activator.getDefault() != null ? "" : "Reset");
		buttonReset.setImage(Activator.getDefault() != null ? Activator.getDefault().getImage(Activator.ICON_RESET) : null);
		buttonReset.setEnabled(true);
		buttonReset.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				acquisition.setRecordData(false);
				display.timerExec(-1, recording);
				//
				chromatogramChart.deleteSeries();
				x = 0;
				//
				List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
				ISeriesData seriesData = getRandomSeriesData();
				ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
				ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
				lineSeriesSettings.setLineColor(getDisplay().getSystemColor(SWT.COLOR_RED));
				lineSeriesSettings.setEnableArea(true);
				lineSeriesDataList.add(lineSeriesData);
				ILineSeriesSettings lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
				lineSeriesSettingsHighlight.setLineWidth(2);
				chromatogramChart.addSeriesData(lineSeriesDataList);
			}
		});
		/*
		 * Chart
		 */
		chromatogramChart = new ChromatogramChart(this, SWT.BORDER);
		chromatogramChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
		ISeriesData seriesData = getRandomSeriesData();
		ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
		ILineSeriesSettings lineSeriesSettings = lineSeriesData.getSettings();
		lineSeriesSettings.setLineColor(getDisplay().getSystemColor(SWT.COLOR_RED));
		lineSeriesSettings.setEnableArea(true);
		ILineSeriesSettings lineSeriesSettingsHighlight = (ILineSeriesSettings)lineSeriesSettings.getSeriesSettingsHighlight();
		lineSeriesSettingsHighlight.setLineWidth(2);
		lineSeriesDataList.add(lineSeriesData);
		chromatogramChart.addSeriesData(lineSeriesDataList);
	}

	private void setButtonsEnabled(boolean recordData) {

		buttonStart.setEnabled(!recordData);
		buttonStop.setEnabled(recordData);
		buttonReset.setEnabled(!recordData);
	}

	private static ISeriesData getRandomSeriesData() {

		int length = 101;
		double[] xSeries = new double[length];
		double[] ySeries = new double[length];
		//
		double a = Math.random(); // height
		double i = -5.0d;
		double iDelta = 0.1d;
		//
		for(int j = 0; j < length; j++) {
			xSeries[j] = x;
			ySeries[j] = a * Math.exp(-i * i / 2) / Math.sqrt(2 * Math.PI);
			x += xDelta;
			i += iDelta;
		}
		return new SeriesData(xSeries, ySeries, ID);
	}
}
