/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
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
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.export.core.AbstractSeriesExportHandler;
import org.eclipse.swtchart.export.core.AxisSettings;
import org.eclipse.swtchart.export.core.ExportSettingsDialog;
import org.eclipse.swtchart.export.core.ISeriesExportConverter;
import org.eclipse.swtchart.extensions.barcharts.BarChart;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.StepChart;
import org.eclipse.swtchart.extensions.scattercharts.ScatterChart;

public class RScriptExportHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = Messages.getString(Messages.R_EXTENSION);
	public static final String NAME = Messages.getString(Messages.IMAGE_R_SCRIPT) + FILE_EXTENSION + ")"; //$NON-NLS-1$
	//
	private static final String TITLE = Messages.getString(Messages.SAVE_AS_IMAGE_R_SCRIPT);
	//
	private static final String AXIS_X = "x"; //$NON-NLS-1$
	private static final String AXIS_Y = "y"; //$NON-NLS-1$

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
						//
						if(scrollableChart instanceof LineChart) {
							printLinePlot(fileName, printWriter, scrollableChart, axisSettings);
						} else if(scrollableChart instanceof BarChart) {
							printBarPlot(fileName, printWriter, scrollableChart, axisSettings);
						} else if(scrollableChart instanceof ScatterChart) {
							printScatterPlot(fileName, printWriter, scrollableChart, axisSettings);
						} else if(scrollableChart instanceof StepChart) {
							printStepPlot(fileName, printWriter, scrollableChart, axisSettings);
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
							exportSettingsDialog.reset(baseChart);
						}
					}
				}
			}
		}
	}

	private String getColor(Color color) {

		StringBuilder hex_color = new StringBuilder("#");
		double r = (double)color.getRed();
		double g = (double)color.getGreen();
		double b = (double)color.getBlue();
		double[] rgb = new double[]{r, g, b};
		for(double x : rgb) {
			double hex = 16.0d;
			double div = x / hex;
			int count = (int)div;
			double rem = div - count;
			rem = (int)(rem * hex);
			char first, second;
			if(count >= 10) {
				first = (char)('a' + (count - 10));
			} else {
				first = (char)('0' + count);
			}
			if(rem >= 10) {
				second = (char)('a' + (rem - 10));
			} else {
				second = (char)('0' + rem);
			}
			hex_color.append(first);
			hex_color.append(second);
		}
		return hex_color.toString();
	}

	private void printLinePlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
		/*
		 * Read from script.
		 */
		printExecuteInfo(fileName, printWriter);
		/*
		 * Header
		 */
		int seriesSize = getSeriesSize(series);
		printWriter.println("# Header"); //$NON-NLS-1$
		printWriter.println("xValueList<-vector(\"list\", " + seriesSize + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		printWriter.println("yValueList<-vector(\"list\", " + seriesSize + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		printWriter.println(""); //$NON-NLS-1$
		/*
		 * Data
		 */
		printWriter.println("# Data"); //$NON-NLS-1$
		int widthPlotArea = baseChart.getPlotArea().getSize().x;
		int index = 1;
		ArrayList<String> color = new ArrayList<String>();
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				ILineSeries<?> lineSeries = (ILineSeries<?>)dataSeries;
				Color col = lineSeries.getLineColor();
				color.add(getColor(col));
				printLineData(dataSeries, widthPlotArea, axisSettings, index++, printWriter);
			}
		}
		printWriter.println(""); //$NON-NLS-1$
		/*
		 * Footer
		 */
		printWriter.println("#  Footer"); //$NON-NLS-1$
		StringBuilder list = new StringBuilder("colorList<-c(");
		int length = color.size();
		int color_count = 0;
		for(String col : color) {
			list.append("\"");
			list.append(col);
			list.append("\"");
			if(color_count != length - 1) {
				list.append(",");
				color_count++;
			}
		}
		list.append(")");
		printWriter.println(list); // $NON-NLS-1$
		//
		printWriter.println(""); //$NON-NLS-1$
		printWriter.println("plot("); //$NON-NLS-1$
		printWriter.println("	xValueList[[1]], yValueList[[1]],"); //$NON-NLS-1$
		printWriter.println("	xlim=c(range(xValueList)[1], range(xValueList)[2]),"); //$NON-NLS-1$
		printWriter.println("	ylim=c(range(yValueList)[1], range(yValueList)[2]),"); //$NON-NLS-1$
		printWriter.println("	type='l',"); //$NON-NLS-1$
		printWriter.println("	col=colorList[1],"); //$NON-NLS-1$
		printWriter.println("	ylab='" + axisSettingsY.getLabel() + "',"); //$NON-NLS-1$ //$NON-NLS-2$
		printWriter.println("	xlab='" + axisSettingsX.getLabel() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		printWriter.println(")"); //$NON-NLS-1$
		printWriter.println(""); //$NON-NLS-1$
		//
		if(seriesSize > 1) {
			printWriter.println("for(i in 2:" + seriesSize + "){"); //$NON-NLS-1$ //$NON-NLS-2$
			printWriter.println("	lines(xValueList[[i]], yValueList[[i]], type='l', col=colorList[i])"); //$NON-NLS-1$
			printWriter.println("}"); //$NON-NLS-1$
			printWriter.println(""); //$NON-NLS-1$
		}
		//
		//
		int size = seriesSize;
		int k;
		printWriter.println("legend('topleft',"); //$NON-NLS-1$
		printWriter.println("		c("); //$NON-NLS-1$
		k = 0;
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				printWriter.print("			'Series " + dataSeries.getDescription() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				if(k < size - 1) {
					printWriter.print(","); //$NON-NLS-1$
				}
				printWriter.println();
				k++;
			}
		}
		printWriter.println("		),"); //$NON-NLS-1$
		printWriter.println("		col=c("); //$NON-NLS-1$
		k = 0;
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				printWriter.print("			colorList[" + (k + 1) + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				if(k < size - 1) {
					printWriter.print(","); //$NON-NLS-1$
				}
				printWriter.println();
				k++;
			}
		}
		printWriter.println("		),"); //$NON-NLS-1$
		printWriter.println("		lwd=2"); //$NON-NLS-1$
		printWriter.println("	)"); //$NON-NLS-1$
		printWriter.println(""); //$NON-NLS-1$
	}

	private void printLineData(ISeries<?> dataSeries, int widthPlotArea, AxisSettings axisSettings, int index, PrintWriter printWriter) {

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
				printWriter.println("xValueList[[" + index + "]]<-c(xValueList[[" + index + "]]," + value + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			} else if(axis.equals(AXIS_Y)) {
				printWriter.println("yValueList[[" + index + "]]<-c(yValueList[[" + index + "]]," + value + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
		} else {
			if(axisScaleConverter != null) {
				if(axis.equals(AXIS_X)) {
					printWriter.println("xValueList[[" + index + "]]<-c(xValueList[[" + index + "]]," + axisScaleConverter.convertToSecondaryUnit(value) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				} else if(axis.equals(AXIS_Y)) {
					printWriter.println("yValueList[[" + index + "]]<-c(yValueList[[" + index + "]]," + axisScaleConverter.convertToSecondaryUnit(value) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
			}
		}
	}

	private void printBarPlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
		/*
		 * Read from script.
		 */
		printExecuteInfo(fileName, printWriter);
		/*
		 * Header
		 */
		printWriter.println("# Header"); //$NON-NLS-1$
		printWriter.println("count_values<-NULL"); //$NON-NLS-1$
		printWriter.println(""); //$NON-NLS-1$
		/*
		 * Data
		 */
		printWriter.println("# Data"); //$NON-NLS-1$
		int widthPlotArea = baseChart.getPlotArea().getSize().x;
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				printBarData(dataSeries, widthPlotArea, axisSettings, printWriter);
			}
		}
		printWriter.println(""); //$NON-NLS-1$
		/*
		 * Footer
		 */
		printWriter.println("#  Footer"); //$NON-NLS-1$
		printWriter.println("hist(count_values, breaks = range(count_values)[2]-range(count_values)[1]+1, axes=FALSE, xlab='" + axisSettingsX.getLabel() + "', ylab='" + axisSettingsY.getLabel() + "', main='" + scrollableChart.getChartSettings().getTitle() + "')"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		printWriter.println(""); //$NON-NLS-1$
		printWriter.println("axis(2, at = NULL)"); //$NON-NLS-1$
		printWriter.println("lower_x <- NULL"); //$NON-NLS-1$
		printWriter.println("if(min(count_values) %% 10 != 0){"); //$NON-NLS-1$
		printWriter.println("  lower_x <- round(min(count_values) %/% 10,0)*10"); //$NON-NLS-1$
		printWriter.println("} else {"); //$NON-NLS-1$
		printWriter.println("  lower_x <- min(count_values)+0.5"); //$NON-NLS-1$
		printWriter.println("}"); //$NON-NLS-1$
		printWriter.println(""); //$NON-NLS-1$
		printWriter.println("upper_x <- round(max(count_values)/10,0)*10+0.5"); //$NON-NLS-1$
		printWriter.println("axis(1, at = seq(lower_x+0.5, upper_x+0.5, 10), labels=seq(lower_x, upper_x, 10), tick = TRUE )"); //$NON-NLS-1$
	}

	private void printBarData(ISeries<?> dataSeries, int widthPlotArea, AxisSettings axisSettings, PrintWriter printWriter) {

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
			printWriter.println("count_values<-c(count_values, rep(" + valueX + ", " + valueY + "))"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			if(axisScaleConverterX != null && axisScaleConverterY != null) {
				printWriter.println("count_values<-c(count_values, rep(" + axisScaleConverterX.convertToSecondaryUnit(valueX) + ", " + axisScaleConverterY.convertToSecondaryUnit(valueY) + "))"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
	}

	private void printScatterPlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
		/*
		 * Read from script.
		 */
		printExecuteInfo(fileName, printWriter);
		/*
		 * Header
		 */
		printWriter.println("# Header"); //$NON-NLS-1$
		printWriter.println("scatter_labels <- NULL"); //$NON-NLS-1$
		printWriter.println("x_values <- NULL"); //$NON-NLS-1$
		printWriter.println("y_values <- NULL"); //$NON-NLS-1$
		printWriter.println(""); //$NON-NLS-1$
		/*
		 * Data
		 */
		printWriter.println("# Data"); //$NON-NLS-1$
		int widthPlotArea = baseChart.getPlotArea().getSize().x;
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				printScatterData(dataSeries, widthPlotArea, axisSettings, printWriter);
			}
		}
		printWriter.println(""); //$NON-NLS-1$
		/*
		 * Footer
		 */
		printWriter.println("#  Footer"); //$NON-NLS-1$
		printWriter.println("plot_data<-cbind(x_values, y_values)"); //$NON-NLS-1$
		printWriter.println("plot(plot_data, xlab=\"" + axisSettingsX.getLabel() + "\", ylab=\"" + axisSettingsY.getLabel() + "\")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		printWriter.println("text(plot_data[,1], plot_data[,2], scatter_labels, pos=3)"); //$NON-NLS-1$
		printWriter.println("abline(h=0)"); //$NON-NLS-1$
		printWriter.println("abline(v=0)"); //$NON-NLS-1$
	}

	private void printScatterData(ISeries<?> dataSeries, int widthPlotArea, AxisSettings axisSettings, PrintWriter printWriter) {

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
				printWriter.println("scatter_labels<-c(scatter_labels,'" + getIdentifier(dataSeries) + "')"); //$NON-NLS-1$ //$NON-NLS-2$
				printValueScatterPlot(AXIS_X, printWriter, xSeries[i], indexAxisX, BaseChart.ID_PRIMARY_X_AXIS, axisScaleConverterX);
				printValueScatterPlot(AXIS_Y, printWriter, ySeries[i], indexAxisY, BaseChart.ID_PRIMARY_Y_AXIS, axisScaleConverterY);
			}
		}
	}

	private void printValueScatterPlot(String axis, PrintWriter printWriter, double value, int indexAxis, int indexPrimaryAxis, IAxisScaleConverter axisScaleConverter) {

		if(indexAxis == indexPrimaryAxis || axisScaleConverter == null) {
			if(axis.equals(AXIS_X)) {
				printWriter.println("x_values<-c(x_values," + value + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(axis.equals(AXIS_Y)) {
				printWriter.println("y_values<-c(y_values," + value + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			if(axisScaleConverter != null) {
				if(axis.equals(AXIS_X)) {
					printWriter.println("x_values<-c(x_values," + axisScaleConverter.convertToSecondaryUnit(value) + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				} else if(axis.equals(AXIS_Y)) {
					printWriter.println("y_values<-c(y_values," + axisScaleConverter.convertToSecondaryUnit(value) + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}

	private void printStepPlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
		/*
		 * Read from script.
		 */
		printExecuteInfo(fileName, printWriter);
		/*
		 * Header
		 */
		int seriesSize = getSeriesSize(series);
		printWriter.println("# Header"); //$NON-NLS-1$
		printWriter.println("xValueList<-vector(\"list\", " + seriesSize + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		printWriter.println("yValueList<-vector(\"list\", " + seriesSize + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		printWriter.println(""); //$NON-NLS-1$
		/*
		 * Data
		 */
		printWriter.println("# Data"); //$NON-NLS-1$
		int widthPlotArea = baseChart.getPlotArea().getSize().x;
		int index = 1;
		ArrayList<String> color = new ArrayList<String>();
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				ILineSeries<?> lineSeries = (ILineSeries<?>)dataSeries;
				Color col = lineSeries.getLineColor();
				color.add(getColor(col));
				printLineData(dataSeries, widthPlotArea, axisSettings, index++, printWriter);
			}
		}
		printWriter.println(""); //$NON-NLS-1$
		/*
		 * Footer
		 */
		printWriter.println("#  Footer"); //$NON-NLS-1$
		StringBuilder list = new StringBuilder("colorList<-c(");
		int length = color.size();
		int color_count = 0;
		for(String col : color) {
			list.append("\"");
			list.append(col);
			list.append("\"");
			if(color_count != length - 1) {
				list.append(",");
				color_count++;
			}
		}
		list.append(")");
		printWriter.println(list); // $NON-NLS-1$
		//
		printWriter.println(""); //$NON-NLS-1$
		printWriter.println("plot("); //$NON-NLS-1$
		printWriter.println("	xValueList[[1]], yValueList[[1]],"); //$NON-NLS-1$
		printWriter.println("	xlim=c(range(xValueList)[1], range(xValueList)[2]),"); //$NON-NLS-1$
		printWriter.println("	ylim=c(range(yValueList)[1], range(yValueList)[2]),"); //$NON-NLS-1$
		printWriter.println("	type='s',"); //$NON-NLS-1$
		printWriter.println("	col=colorList[1],"); //$NON-NLS-1$
		printWriter.println("	ylab='" + axisSettingsY.getLabel() + "',"); //$NON-NLS-1$ //$NON-NLS-2$
		printWriter.println("	xlab='" + axisSettingsX.getLabel() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		printWriter.println(")"); //$NON-NLS-1$
		printWriter.println(""); //$NON-NLS-1$
		//
		if(seriesSize > 1) {
			printWriter.println("for(i in 2:" + seriesSize + "){"); //$NON-NLS-1$ //$NON-NLS-2$
			printWriter.println("	lines(xValueList[[i]], yValueList[[i]], type='s', col=colorList[i])"); //$NON-NLS-1$
			printWriter.println("}"); //$NON-NLS-1$
			printWriter.println(""); //$NON-NLS-1$
		}
		//
		//
		int size = seriesSize;
		int k;
		printWriter.println("legend('topleft',"); //$NON-NLS-1$
		printWriter.println("		c("); //$NON-NLS-1$
		k = 0;
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				printWriter.print("			'Series " + dataSeries.getDescription() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				if(k < size - 1) {
					printWriter.print(","); //$NON-NLS-1$
				}
				printWriter.println();
				k++;
			}
		}
		printWriter.println("		),"); //$NON-NLS-1$
		printWriter.println("		col=c("); //$NON-NLS-1$
		k = 0;
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				printWriter.print("			colorList[" + (k + 1) + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				if(k < size - 1) {
					printWriter.print(","); //$NON-NLS-1$
				}
				printWriter.println();
				k++;
			}
		}
		printWriter.println("		),"); //$NON-NLS-1$
		printWriter.println("		lwd=2"); //$NON-NLS-1$
		printWriter.println("	)"); //$NON-NLS-1$
		printWriter.println(""); //$NON-NLS-1$
	}

	private void printExecuteInfo(String fileName, PrintWriter printWriter) {

		printWriter.println("#-----------------------------------"); //$NON-NLS-1$
		printWriter.println("# source('" + fileName + "')"); //$NON-NLS-1$ //$NON-NLS-2$
		printWriter.println("#-----------------------------------"); //$NON-NLS-1$
		printWriter.println(""); //$NON-NLS-1$
	}

	private int getSeriesSize(ISeries<?>[] series) {

		int counter = 0;
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null) {
				if(dataSeries.isVisible()) {
					counter++;
				}
			}
		}
		return counter;
	}
}
