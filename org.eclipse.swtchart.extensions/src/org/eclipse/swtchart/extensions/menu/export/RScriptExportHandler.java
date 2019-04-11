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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.scattercharts.ScatterChart;
import org.eclipse.swtchart.ISeries;

public class RScriptExportHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = "*.R";
	public static final String NAME = "Image R-Script (" + FILE_EXTENSION + ")";
	//
	private static final String TITLE = "Save As R-Script Image";
	//
	private static final String AXIS_X = "x";
	private static final String AXIS_Y = "y";

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
						 * Axis settings.
						 */
						AxisSettings axisSettings = new AxisSettings();
						axisSettings.setIndexAxisX(indexAxisX);
						axisSettings.setIndexAxisY(indexAxisY);
						axisSettings.setAxisSettingsX(axisSettingsX);
						axisSettings.setAxisScaleConverterX(axisScaleConverterX);
						axisSettings.setAxisSettingsY(axisSettingsY);
						axisSettings.setAxisScaleConverterY(axisScaleConverterY);
						axisSettings.setExportVisibleOnly(exportSettingsDialog.isExportVisibleOnly());
						//
						if(scrollableChart instanceof LineChart) {
							printLinePlot(fileName, printWriter, scrollableChart, axisSettings);
						} else if(scrollableChart instanceof BarChart) {
							printBarPlot(fileName, printWriter, scrollableChart, axisSettings);
						} else if(scrollableChart instanceof ScatterChart) {
							printScatterPlot(fileName, printWriter, scrollableChart, axisSettings);
						}
						//
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

	private void printLinePlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		boolean exportVisibleOnly = axisSettings.isExportVisibleOnly();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries[] series = baseChart.getSeriesSet().getSeries();
		/*
		 * Read from script.
		 */
		printExecuteInfo(fileName, printWriter);
		/*
		 * Header
		 */
		int seriesSize = getSeriesSize(series, exportVisibleOnly);
		printWriter.println("# Header");
		printWriter.println("xValueList<-vector(\"list\", " + seriesSize + ")");
		printWriter.println("yValueList<-vector(\"list\", " + seriesSize + ")");
		printWriter.println("");
		/*
		 * Data
		 */
		printWriter.println("# Data");
		int widthPlotArea = baseChart.getPlotArea().getBounds().width;
		int index = 1;
		for(ISeries dataSeries : series) {
			if(dataSeries != null) {
				if(exportVisibleOnly) {
					if(dataSeries.isVisible()) {
						printLineData(dataSeries, widthPlotArea, axisSettings, index++, printWriter);
					}
				} else {
					printLineData(dataSeries, widthPlotArea, axisSettings, index++, printWriter);
				}
			}
		}
		printWriter.println("");
		/*
		 * Footer
		 */
		printWriter.println("#  Footer");
		printWriter.println("colorList<-c(\"black\", \"red\", \"blue\", \"green\", \"grey\", \"purple\", \"brown\", \"pink\", \"yellow\", \"orange\")");
		//
		printWriter.println("");
		printWriter.println("plot(");
		printWriter.println("	xValueList[[1]], yValueList[[1]],");
		printWriter.println("	xlim=c(range(xValueList)[1], range(xValueList)[2]),");
		printWriter.println("	ylim=c(range(yValueList)[1], range(yValueList)[2]),");
		printWriter.println("	type='l',");
		printWriter.println("	col=colorList[1],");
		printWriter.println("	ylab='" + axisSettingsY.getLabel() + "',");
		printWriter.println("	xlab='" + axisSettingsX.getLabel() + "'");
		printWriter.println(")");
		printWriter.println("");
		//
		if(seriesSize > 1) {
			printWriter.println("for(i in 2:" + seriesSize + "){");
			printWriter.println("	points(xValueList[[i]], yValueList[[i]], type='l', col=colorList[(i+8)%%9+1])");
			printWriter.println("}");
			printWriter.println("");
		}
		//
		int size;
		//
		int k;
		printWriter.println("legend('topleft',");
		printWriter.println("		c(");
		k = 0;
		size = series.length;
		for(ISeries dataSeries : series) {
			if(dataSeries != null) {
				printWriter.print("			'Series " + dataSeries.getDescription() + "'");
				if(k < size - 1) {
					printWriter.print(",");
				}
				printWriter.println();
				k++;
			}
		}
		printWriter.println("		),");
		printWriter.println("		col=c(");
		k = 0;
		size = series.length;
		for(ISeries dataSeries : series) {
			if(dataSeries != null) {
				printWriter.print("			colorList[(" + (k + 1) + "+8)%%9+1]");
				if(k < size - 1) {
					printWriter.print(",");
				}
				printWriter.println();
				k++;
			}
		}
		printWriter.println("		),");
		printWriter.println("		lwd=2");
		printWriter.println("	)");
		printWriter.println("");
	}

	private void printLineData(ISeries dataSeries, int widthPlotArea, AxisSettings axisSettings, int index, PrintWriter printWriter) {

		int indexAxisX = axisSettings.getIndexAxisX();
		int indexAxisY = axisSettings.getIndexAxisY();
		IAxisScaleConverter axisScaleConverterX = axisSettings.getAxisScaleConverterX();
		IAxisScaleConverter axisScaleConverterY = axisSettings.getAxisScaleConverterY();
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
				printValueLinePlot(AXIS_X, index, printWriter, xSeries[i], indexAxisX, BaseChart.ID_PRIMARY_X_AXIS, axisScaleConverterX);
				printValueLinePlot(AXIS_Y, index, printWriter, ySeries[i], indexAxisY, BaseChart.ID_PRIMARY_Y_AXIS, axisScaleConverterY);
			}
		}
	}

	private void printValueLinePlot(String axis, int index, PrintWriter printWriter, double value, int indexAxis, int indexPrimaryAxis, IAxisScaleConverter axisScaleConverter) {

		if(indexAxis == indexPrimaryAxis || axisScaleConverter == null) {
			if(axis.equals(AXIS_X)) {
				printWriter.println("xValueList[[" + index + "]]<-c(xValueList[[" + index + "]]," + value + ")");
			} else if(axis.equals(AXIS_Y)) {
				printWriter.println("yValueList[[" + index + "]]<-c(yValueList[[" + index + "]]," + value + ")");
			}
		} else {
			if(axisScaleConverter != null) {
				if(axis.equals(AXIS_X)) {
					printWriter.println("xValueList[[" + index + "]]<-c(xValueList[[" + index + "]]," + axisScaleConverter.convertToSecondaryUnit(value) + ")");
				} else if(axis.equals(AXIS_Y)) {
					printWriter.println("yValueList[[" + index + "]]<-c(yValueList[[" + index + "]]," + axisScaleConverter.convertToSecondaryUnit(value) + ")");
				}
			}
		}
	}

	private void printBarPlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		boolean exportVisibleOnly = axisSettings.isExportVisibleOnly();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries[] series = baseChart.getSeriesSet().getSeries();
		/*
		 * Read from script.
		 */
		printExecuteInfo(fileName, printWriter);
		/*
		 * Header
		 */
		printWriter.println("# Header");
		printWriter.println("count_values<-NULL");
		printWriter.println("");
		/*
		 * Data
		 */
		printWriter.println("# Data");
		int widthPlotArea = baseChart.getPlotArea().getBounds().width;
		for(ISeries dataSeries : series) {
			if(dataSeries != null) {
				if(exportVisibleOnly) {
					if(dataSeries.isVisible()) {
						printBarData(dataSeries, widthPlotArea, axisSettings, printWriter);
					}
				} else {
					printBarData(dataSeries, widthPlotArea, axisSettings, printWriter);
				}
			}
		}
		printWriter.println("");
		/*
		 * Footer
		 */
		printWriter.println("#  Footer");
		printWriter.println("hist(count_values, breaks = range(count_values)[2]-range(count_values)[1]+1, axes=FALSE, xlab='" + axisSettingsX.getLabel() + "', ylab='" + axisSettingsY.getLabel() + "', main='" + scrollableChart.getChartSettings().getTitle() + "')");
		printWriter.println("");
		printWriter.println("axis(2, at = NULL)");
		printWriter.println("lower_x <- NULL");
		printWriter.println("if(min(count_values) %% 10 != 0){");
		printWriter.println("  lower_x <- round(min(count_values) %/% 10,0)*10");
		printWriter.println("} else {");
		printWriter.println("  lower_x <- min(count_values)+0.5");
		printWriter.println("}");
		printWriter.println("");
		printWriter.println("upper_x <- round(max(count_values)/10,0)*10+0.5");
		printWriter.println("axis(1, at = seq(lower_x+0.5, upper_x+0.5, 10), labels=seq(lower_x, upper_x, 10), tick = TRUE )");
	}

	private void printBarData(ISeries dataSeries, int widthPlotArea, AxisSettings axisSettings, PrintWriter printWriter) {

		int indexAxisX = axisSettings.getIndexAxisX();
		IAxisScaleConverter axisScaleConverterX = axisSettings.getAxisScaleConverterX();
		IAxisScaleConverter axisScaleConverterY = axisSettings.getAxisScaleConverterY();
		/*
		 * Series
		 */
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
				boolean isPrimaryAxis = (indexAxisX == BaseChart.ID_PRIMARY_X_AXIS);
				printValueBarPlot(printWriter, xSeries[i], ySeries[i], isPrimaryAxis, axisScaleConverterX, axisScaleConverterY);
			}
		}
	}

	private void printValueBarPlot(PrintWriter printWriter, double valueX, double valueY, boolean isPrimaryAxis, IAxisScaleConverter axisScaleConverterX, IAxisScaleConverter axisScaleConverterY) {

		if(isPrimaryAxis || axisScaleConverterX == null || axisScaleConverterY == null) {
			printWriter.println("count_values<-c(count_values, rep(" + valueX + ", " + valueY + "))");
		} else {
			if(axisScaleConverterX != null && axisScaleConverterY != null) {
				printWriter.println("count_values<-c(count_values, rep(" + axisScaleConverterX.convertToSecondaryUnit(valueX) + ", " + axisScaleConverterY.convertToSecondaryUnit(valueY) + "))");
			}
		}
	}

	private void printScatterPlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		boolean exportVisibleOnly = axisSettings.isExportVisibleOnly();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries[] series = baseChart.getSeriesSet().getSeries();
		/*
		 * Read from script.
		 */
		printExecuteInfo(fileName, printWriter);
		/*
		 * Header
		 */
		printWriter.println("# Header");
		printWriter.println("scatter_labels <- NULL");
		printWriter.println("x_values <- NULL");
		printWriter.println("y_values <- NULL");
		printWriter.println("");
		/*
		 * Data
		 */
		printWriter.println("# Data");
		int widthPlotArea = baseChart.getPlotArea().getBounds().width;
		for(ISeries dataSeries : series) {
			if(dataSeries != null) {
				if(exportVisibleOnly) {
					if(dataSeries.isVisible()) {
						printScatterData(dataSeries, widthPlotArea, axisSettings, printWriter);
					}
				} else {
					printScatterData(dataSeries, widthPlotArea, axisSettings, printWriter);
				}
			}
		}
		printWriter.println("");
		/*
		 * Footer
		 */
		printWriter.println("#  Footer");
		printWriter.println("plot_data<-cbind(x_values, y_values)");
		printWriter.println("plot(plot_data, xlab=\"" + axisSettingsX.getLabel() + "\", ylab=\"" + axisSettingsY.getLabel() + "\")");
		printWriter.println("text(plot_data[,1], plot_data[,2], scatter_labels, pos=3)");
		printWriter.println("abline(h=0)");
		printWriter.println("abline(v=0)");
	}

	private void printScatterData(ISeries dataSeries, int widthPlotArea, AxisSettings axisSettings, PrintWriter printWriter) {

		int indexAxisX = axisSettings.getIndexAxisX();
		int indexAxisY = axisSettings.getIndexAxisY();
		IAxisScaleConverter axisScaleConverterX = axisSettings.getAxisScaleConverterX();
		IAxisScaleConverter axisScaleConverterY = axisSettings.getAxisScaleConverterY();
		/*
		 * Series
		 */
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
				printWriter.println("scatter_labels<-c(scatter_labels,'" + dataSeries.getId() + "')");
				printValueScatterPlot(AXIS_X, printWriter, xSeries[i], indexAxisX, BaseChart.ID_PRIMARY_X_AXIS, axisScaleConverterX);
				printValueScatterPlot(AXIS_Y, printWriter, ySeries[i], indexAxisY, BaseChart.ID_PRIMARY_Y_AXIS, axisScaleConverterY);
			}
		}
	}

	private void printValueScatterPlot(String axis, PrintWriter printWriter, double value, int indexAxis, int indexPrimaryAxis, IAxisScaleConverter axisScaleConverter) {

		if(indexAxis == indexPrimaryAxis || axisScaleConverter == null) {
			if(axis.equals(AXIS_X)) {
				printWriter.println("x_values<-c(x_values," + value + ")");
			} else if(axis.equals(AXIS_Y)) {
				printWriter.println("y_values<-c(y_values," + value + ")");
			}
		} else {
			if(axisScaleConverter != null) {
				if(axis.equals(AXIS_X)) {
					printWriter.println("x_values<-c(x_values," + axisScaleConverter.convertToSecondaryUnit(value) + ")");
				} else if(axis.equals(AXIS_Y)) {
					printWriter.println("y_values<-c(y_values," + axisScaleConverter.convertToSecondaryUnit(value) + ")");
				}
			}
		}
	}

	private void printExecuteInfo(String fileName, PrintWriter printWriter) {

		printWriter.println("#-----------------------------------");
		printWriter.println("# source('" + fileName + "')");
		printWriter.println("#-----------------------------------");
		printWriter.println("");
	}

	private int getSeriesSize(ISeries[] series, boolean isExportVisibleOnly) {

		int counter = 0;
		for(ISeries dataSeries : series) {
			if(dataSeries != null) {
				if(isExportVisibleOnly) {
					if(dataSeries.isVisible()) {
						counter++;
					}
				} else {
					counter++;
				}
			}
		}
		return counter;
	}
}
