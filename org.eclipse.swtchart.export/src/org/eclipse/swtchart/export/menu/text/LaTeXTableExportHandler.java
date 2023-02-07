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
package org.eclipse.swtchart.export.menu.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.MessageFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.export.core.AbstractSeriesExportHandler;
import org.eclipse.swtchart.export.core.AxisSettings;
import org.eclipse.swtchart.export.core.VectorExportSettingsDialog;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class LaTeXTableExportHandler extends AbstractSeriesExportHandler implements IChartMenuEntry {

	private static final String FILE_EXTENSION = "*.tex"; //$NON-NLS-1$
	public static final String NAME = MessageFormat.format(Messages.LATEX_TABLE, FILE_EXTENSION);
	//
	private static final String TITLE = Messages.SAVE_AS_LATEX;
	private static final String TAB = "\t"; //$NON-NLS-1$
	private static final String DELIMITER = " & "; //$NON-NLS-1$
	private static final String HORIZONTAL_LINE = "\\hline"; //$NON-NLS-1$
	private static final String LINE_END = " \\\\"; //$NON-NLS-1$

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public Image getIcon() {

		return ResourceSupport.getImage(ResourceSupport.ICON_TEX);
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
			VectorExportSettingsDialog exportSettingsDialog = new VectorExportSettingsDialog(shell, baseChart);
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
					try (PrintWriter printWriter = new PrintWriter(new File(fileName))) {
						/*
						 * Header
						 */
						printWriter.println("\\begin{center}"); //$NON-NLS-1$
						printWriter.println("\\begin{tabular}{ c c }"); //$NON-NLS-1$
						//
						printWriter.print(TAB);
						printWriter.print(axisSettingsX.getLabel());
						printWriter.print(DELIMITER);
						printWriter.print(axisSettingsY.getLabel());
						printWriter.println(LINE_END);
						/*
						 * Axis settings.
						 */
						AxisSettings axisSettings = new AxisSettings();
						axisSettings.setIndexAxisX(indexAxisX);
						axisSettings.setIndexAxisY(indexAxisY);
						axisSettings.setAxisSettingsX(axisSettingsX);
						axisSettings.setAxisScaleConverterX(axisScaleConverterX);
						axisSettings.setAxisSettingsY(axisSettingsY);
						axisSettings.setAxisScaleConverterY(axisScaleConverterY);
						/*
						 * Data
						 */
						int widthPlotArea = baseChart.getPlotArea().getSize().x;
						ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
						for(ISeries<?> dataSeries : series) {
							if(dataSeries != null && dataSeries.isVisible()) {
								exportSeries(dataSeries, widthPlotArea, axisSettings, printWriter);
							}
						}
						//
						printWriter.println("\\end{tabular}"); //$NON-NLS-1$
						printWriter.println("\\end{center}"); //$NON-NLS-1$
						printWriter.flush();
						MessageDialog.openInformation(shell, TITLE, org.eclipse.swtchart.export.core.Messages.DATA_EXPORT_SUCCESS);
					} catch(FileNotFoundException e) {
						MessageDialog.openError(shell, TITLE, org.eclipse.swtchart.export.core.Messages.DATA_EXPORT_ERROR);
						e.printStackTrace();
					}
				}
			}
			//
			exportSettingsDialog.reset();
			scrollableChart.updateLegend();
		}
	}

	private void exportSeries(ISeries<?> dataSeries, int widthPlotArea, AxisSettings axisSettings, PrintWriter printWriter) {

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
		printWriter.println(TAB + getIdentifier(dataSeries) + DELIMITER + LINE_END);
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