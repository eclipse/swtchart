/*******************************************************************************
 * Copyright (c) 2019, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Sanatt Abrol - SVG export code
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.export.menu.vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.export.core.AbstractSeriesExportHandler;
import org.eclipse.swtchart.export.core.AxisSettings;
import org.eclipse.swtchart.export.core.ISeriesExportConverter;
import org.eclipse.swtchart.export.core.VectorExportSettingsDialog;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ChartType;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.scattercharts.ScatterChart;

public class SVGExportHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = "*.svg"; //$NON-NLS-1$
	private static final String NAME = MessageFormat.format(Messages.getString(Messages.SVG), FILE_EXTENSION);
	private static final String TITLE = Messages.getString(Messages.SAVE_AS_SVG);

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public Image getIcon() {

		return ResourceSupport.getImage(ResourceSupport.ICON_FIGURE);
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText(NAME);
		fileDialog.setFilterExtensions(new String[]{FILE_EXTENSION});
		//
		String fileName = fileDialog.open();
		if(fileName != null) {
			try {
				BaseChart baseChart = scrollableChart.getBaseChart();
				VectorExportSettingsDialog exportSettingsDialog = new VectorExportSettingsDialog(fileDialog.getParent(), baseChart);
				exportSettingsDialog.create();
				if(exportSettingsDialog.open() == Window.OK) {
					int indexAxisX = exportSettingsDialog.getIndexAxisSelectionX();
					int indexAxisY = exportSettingsDialog.getIndexAxisSelectionY();
					if(indexAxisX >= 0 && indexAxisY >= 0) {
						try {
							ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(fileDialog.getParent());
							monitorDialog.run(false, false, new IRunnableWithProgress() {

								@Override
								public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

									try {
										monitor.beginTask(Messages.getString(Messages.EXPORT_TO_SVG), IProgressMonitor.UNKNOWN);
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
											 * First check via instance of. If that fails, perform the enhanced
											 * check via the chart type.
											 */
											if(scrollableChart instanceof LineChart) {
												printLinePlot(fileName, printWriter, scrollableChart, axisSettings);
											} else if(scrollableChart instanceof BarChart) {
												printBarPlot(fileName, printWriter, scrollableChart, axisSettings);
											} else if(scrollableChart instanceof ScatterChart) {
												printScatterPlot(fileName, printWriter, scrollableChart, axisSettings);
											} else {
												/*
												 * The chart extends ScrollableChart directly.
												 */
												ChartType chartType = scrollableChart.getChartType();
												switch(chartType) {
													case STEP:
													case LINE:
														printLinePlot(fileName, printWriter, scrollableChart, axisSettings);
														break;
													case BAR:
														printBarPlot(fileName, printWriter, scrollableChart, axisSettings);
														break;
													case SCATTER:
														printScatterPlot(fileName, printWriter, scrollableChart, axisSettings);
														break;
													case PIE: // TODO
													default:
														System.out.println("The chart type export is not supported yet: " + chartType);
														break;
												}
											}
											//
											MessageDialog.openInformation(shell, TITLE, MESSAGE_OK);
										} catch(FileNotFoundException e) {
											MessageDialog.openError(shell, TITLE, MESSAGE_ERROR);
											e.printStackTrace();
										}
									} catch(Exception e) {
										e.printStackTrace();
									} finally {
										monitor.done();
									}
								}
							});
						} catch(InterruptedException e) {
							e.printStackTrace();
							Thread.currentThread().interrupt();
						}
					}
				}
				//
				exportSettingsDialog.reset();
				scrollableChart.updateLegend();
			} catch(InvocationTargetException e) {
				MessageDialog.openInformation(shell, TITLE, org.eclipse.swtchart.export.core.Messages.DATA_EXPORT_ERROR);
				e.getCause().printStackTrace();
			}
		}
	}

	private void printLinePlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) throws Exception {

		InkscapeLineChart inkscapeLineChart = new InkscapeLineChart();
		inkscapeLineChart.printLinePlot(fileName, printWriter, scrollableChart, axisSettings);
	}

	private void printBarPlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		InkscapeBarChart inkscapeBarChart = new InkscapeBarChart();
		inkscapeBarChart.printBarPlot(fileName, printWriter, scrollableChart, axisSettings);
	}

	private void printScatterPlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		InkscapeScatterChart inkscapeScatterChart = new InkscapeScatterChart();
		inkscapeScatterChart.printScatterPlot(fileName, printWriter, scrollableChart, axisSettings);
	}
}