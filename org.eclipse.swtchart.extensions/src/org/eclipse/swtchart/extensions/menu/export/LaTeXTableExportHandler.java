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

public class LaTeXTableExportHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = "*.tex";
	public static final String NAME = "LaTeX Table (" + FILE_EXTENSION + ")";
	//
	private static final String TITLE = "Save As LaTeX Table";
	private static final String TAB = "\t";
	private static final String DELIMITER = " & ";
	private static final String HORIZONTAL_LINE = "\\hline";
	private static final String LINE_END = " \\\\";

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		BaseChart baseChart = scrollableChart.getBaseChart();
		/*
		 * Select the export file.
		 */
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText(TITLE);
		fileDialog.setFilterExtensions(new String[]{FILE_EXTENSION});
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
						printWriter.println("\\begin{center}");
						printWriter.println("\\begin{tabular}{ c c }");
						//
						printWriter.print(TAB);
						printWriter.print(axisSettingsX.getLabel());
						printWriter.print(DELIMITER);
						printWriter.print(axisSettingsY.getLabel());
						printWriter.println(LINE_END);
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
						printWriter.println("\\end{tabular}");
						printWriter.println("\\end{center}");
						printWriter.flush();
						MessageDialog.openInformation(shell, TITLE, MESSAGE_OK);
					} catch(FileNotFoundException e) {
						MessageDialog.openError(shell, TITLE, MESSAGE_ERROR);
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
		printWriter.println(TAB + HORIZONTAL_LINE);
		printWriter.println(TAB + dataSeries.getId() + DELIMITER + LINE_END);
		printWriter.println(TAB + HORIZONTAL_LINE);
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
				printWriter.print(TAB);
				printValue(printWriter, xSeries[i], indexAxisX, BaseChart.ID_PRIMARY_X_AXIS, decimalFormatX, axisScaleConverterX);
				printWriter.print(DELIMITER);
				printValue(printWriter, ySeries[i], indexAxisY, BaseChart.ID_PRIMARY_Y_AXIS, decimalFormatY, axisScaleConverterY);
				printWriter.println(LINE_END);
			}
		}
		//
		printWriter.print(TAB);
		printWriter.print(DELIMITER);
		printWriter.println(LINE_END);
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
