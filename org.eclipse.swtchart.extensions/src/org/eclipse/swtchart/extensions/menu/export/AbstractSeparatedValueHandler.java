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
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.ISeries;

public abstract class AbstractSeparatedValueHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	private String title;
	private String fileExtension;
	private String delimiter;

	public AbstractSeparatedValueHandler(String title, String fileExtension, String delimiter) {
		this.title = title;
		this.fileExtension = fileExtension;
		this.delimiter = delimiter;
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		BaseChart baseChart = scrollableChart.getBaseChart();
		/*
		 * Select the export file.
		 */
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText(title);
		fileDialog.setFilterExtensions(new String[]{fileExtension});
		//
		String fileName = fileDialog.open();
		if(fileName != null) {
			/*
			 * Select the X and Y axis to export.
			 */
			ExportSettingsDialog exportSettingsDialog = new ExportSettingsDialog(shell, baseChart);
			exportSettingsDialog.create();
			if(exportSettingsDialog.open() == Window.OK) {
				//
				int indexAxisX = exportSettingsDialog.getIndexAxisSelectionX();
				int indexAxisY = exportSettingsDialog.getIndexAxisSelectionY();
				//
				if(indexAxisX >= 0 && indexAxisY >= 0) {
					/*
					 * X Axis Settings
					 */
					IAxisSettings axisSettingsX = baseChart.getXAxisSettings(indexAxisX);
					IAxisScaleConverter axisScaleConverterX = null;
					if(axisSettingsX instanceof ISecondaryAxisSettings) {
						ISecondaryAxisSettings secondaryAxisSettings = (ISecondaryAxisSettings)axisSettingsX;
						axisScaleConverterX = secondaryAxisSettings.getAxisScaleConverter();
					}
					/*
					 * Y Axis Settings
					 */
					IAxisSettings axisSettingsY = baseChart.getYAxisSettings(indexAxisY);
					IAxisScaleConverter axisScaleConverterY = null;
					if(axisSettingsY instanceof ISecondaryAxisSettings) {
						ISecondaryAxisSettings secondaryAxisSettings = (ISecondaryAxisSettings)axisSettingsY;
						axisScaleConverterY = secondaryAxisSettings.getAxisScaleConverter();
					}
					/*
					 * Print the XY data.
					 */
					PrintWriter printWriter = null;
					try {
						printWriter = new PrintWriter(new File(fileName));
						/*
						 * Header
						 */
						printWriter.print(axisSettingsX.getLabel());
						printWriter.print(delimiter);
						printWriter.println(axisSettingsY.getLabel());
						/*
						 * Axis settings.
						 */
						boolean exportVisibleOnly = exportSettingsDialog.isExportVisibleOnly();
						AxisSettings axisSettings = new AxisSettings();
						axisSettings.setIndexAxisX(indexAxisX);
						axisSettings.setIndexAxisY(indexAxisY);
						axisSettings.setAxisSettingsX(axisSettingsX);
						axisSettings.setAxisScaleConverterX(axisScaleConverterX);
						axisSettings.setAxisSettingsY(axisSettingsY);
						axisSettings.setAxisScaleConverterY(axisScaleConverterY);
						axisSettings.setExportVisibleOnly(exportVisibleOnly);
						/*
						 * Data
						 */
						int widthPlotArea = baseChart.getPlotArea().getBounds().width;
						ISeries[] series = baseChart.getSeriesSet().getSeries();
						for(ISeries dataSeries : series) {
							if(dataSeries != null) {
								if(exportVisibleOnly) {
									if(dataSeries.isVisible()) {
										exportSeries(dataSeries, widthPlotArea, axisSettings, printWriter);
									}
								} else {
									exportSeries(dataSeries, widthPlotArea, axisSettings, printWriter);
								}
							}
						}
						//
						printWriter.flush();
						MessageDialog.openInformation(shell, title, MESSAGE_OK);
					} catch(FileNotFoundException e) {
						MessageDialog.openError(shell, title, MESSAGE_ERROR);
						System.out.println(e);
					} finally {
						if(printWriter != null) {
							printWriter.close();
						}
					}
				}
			}
		}
	}

	private void exportSeries(ISeries dataSeries, int widthPlotArea, AxisSettings axisSettings, PrintWriter printWriter) {

		int indexAxisX = axisSettings.getIndexAxisX();
		int indexAxisY = axisSettings.getIndexAxisY();
		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		DecimalFormat decimalFormatX = axisSettingsX.getDecimalFormat();
		IAxisScaleConverter axisScaleConverterX = axisSettings.getAxisScaleConverterX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		DecimalFormat decimalFormatY = axisSettingsY.getDecimalFormat();
		IAxisScaleConverter axisScaleConverterY = axisSettings.getAxisScaleConverterY();
		/*
		 * Series
		 */
		printWriter.println(dataSeries.getId());
		//
		double[] xSeries = dataSeries.getXSeries();
		double[] ySeries = dataSeries.getYSeries();
		int size = dataSeries.getXSeries().length;
		//
		for(int i = 0; i < size; i++) {
			/*
			 * Only export if the data point is visible.
			 */
			Point point = dataSeries.getPixelCoordinates(i);
			if(point.x >= 0 && point.x <= widthPlotArea) {
				printValue(printWriter, xSeries[i], indexAxisX, BaseChart.ID_PRIMARY_X_AXIS, decimalFormatX, axisScaleConverterX);
				printWriter.print(delimiter);
				printValue(printWriter, ySeries[i], indexAxisY, BaseChart.ID_PRIMARY_Y_AXIS, decimalFormatY, axisScaleConverterY);
				printWriter.println("");
			}
		}
		//
		printWriter.println("");
	}

	private void printValue(PrintWriter printWriter, double value, int indexAxis, int indexPrimaryAxis, DecimalFormat decimalFormat, IAxisScaleConverter axisScaleConverter) {

		if(indexAxis == indexPrimaryAxis) {
			printWriter.print(value);
		} else {
			if(axisScaleConverter != null) {
				printWriter.print(decimalFormat.format(axisScaleConverter.convertToSecondaryUnit(value)));
			} else {
				printWriter.print(decimalFormat.format(value));
			}
		}
	}
}
