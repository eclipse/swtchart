/*******************************************************************************
 * Copyright (c) 2019, 2020 Lablicate GmbH.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.LineStyle;
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
import org.eclipse.swtchart.extensions.scattercharts.ScatterChart;
import org.eclipse.swtchart.internal.series.LineSeries;

public class SVGExportHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = "*.svg"; //$NON-NLS-1$
	private static final String NAME = Messages.getString(Messages.SVG) + FILE_EXTENSION + ")"; //$NON-NLS-1$
	private static final String TITLE = Messages.getString(Messages.SAVE_AS_SVG);
	//
	private static final String AXIS_X = "x"; //$NON-NLS-1$
	private static final String AXIS_Y = "y"; //$NON-NLS-1$

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText(NAME);
		fileDialog.setFilterExtensions(new String[]{"*.svg"}); //$NON-NLS-1$
		//
		String fileName = fileDialog.open();
		if(fileName != null) {
			try {
				BaseChart baseChart = scrollableChart.getBaseChart();
				ExportSettingsDialog exportSettingsDialog = new ExportSettingsDialog(fileDialog.getParent(), baseChart);
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
											MessageDialog.openInformation(shell, TITLE, MESSAGE_OK);
										} catch(FileNotFoundException e) {
											MessageDialog.openError(shell, TITLE, MESSAGE_ERROR);
											System.out.println(e);
										} finally {
											if(printWriter != null) {
												printWriter.flush();
												printWriter.close();
											}
										}
									} catch(Exception e) {
										e.printStackTrace();
									} finally {
										monitor.done();
									}
								}
							});
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch(Exception e) {
				MessageDialog.openInformation(shell, TITLE, MESSAGE_ERROR);
				e.printStackTrace();
			}
		}
	}

	/*
	 * Exports The LineSeries Chart to SVG via the Inkscape Template
	 */
	private void printLinePlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) throws Exception {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		boolean exportVisibleOnly = axisSettings.isExportVisibleOnly();
		DecimalFormat formatX = axisSettingsX.getDecimalFormat();
		DecimalFormat formatY = axisSettingsY.getDecimalFormat();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
		String Template = "Template_LineChart.svg";
		try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(Template)))) {
			double xTicks[] = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getTick().getTickLabelValues();
			double yTicks[] = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getTick().getTickLabelValues();
			String line;
			StringBuilder tickX = new StringBuilder("<path\n" + "                       inkscape:connector-curvature=\"0\"\n" + "                       id=\"path888\"\n" + "                       d=\"m %x-coordinate%,279.77439 v 5.05309\"\n" + "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" + "                    <text\n" + "		       id=\"text892\"\n" + "                       y=\"289.01782\"\n" + "                       x=\"%x-coordinate%\"\n" + "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                       xml:space=\"preserve\"><tspan\n" + "                         id=\"tspan890\"\n" + "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:center;text-anchor:middle;stroke-width:0.26458332\"\n" + "                         y=\"289.01782\"\n" + "                         x=\"%x-coordinate%\"\n" + "                         sodipodi:role=\"line\">%X01%</tspan></text>");
			StringBuilder tickY = new StringBuilder("<path\n" + "                       inkscape:connector-curvature=\"0\"\n" + "                       id=\"path1299\"\n" + "                       d=\"m 15.387913,%y-coordinate% h 5.05309\"\n" + "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" + "                    <text\n" + "                       xml:space=\"preserve\"\n" + "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                       x=\"14.211229\"\n" + "                       y=\"%y-coordinate%\"\n" + "                       id=\"text1303\"><tspan\n" + "                         sodipodi:role=\"line\"\n" + "                         id=\"tspan1301\"\n" + "                         x=\"14.211229\"\n" + "                         y=\"%y-coordinate%\"\n" + "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:end;text-anchor:end;stroke-width:0.26458332\">%Y01%</tspan></text>");
			StringBuilder legend = new StringBuilder("<path\n" + "                   style=\"fill:url(#linearGradient3662);fill-opacity:1;stroke:%COLOR%;stroke-width:0.5;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n" + "                   d=\"m 209.71446,%y1-coordinate% h 20.93268\"\n" + "                   id=\"path1744\"\n" + "                   inkscape:connector-curvature=\"0\" />\n" + "                <text\n" + "                   xml:space=\"preserve\"\n" + "                   style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:'Bold Oblique';-inkscape-font-specification:'Bold Oblique, ';letter-spacing:0px;word-spacing:0px;fill:url(#linearGradient4353);fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                   x=\"230.91121\"\n" + "                   y=\"%y2-coordinate%\"\n" + "                   id=\"text1748\"><tspan\n" + "                     sodipodi:role=\"line\"\n" + "                     id=\"tspan1746\"\n" + "                     x=\"230.91121\"\n" + "                     y=\"%y2-coordinate%\"\n" + "                     style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:4.23333311px;font-family:Arial;-inkscape-font-specification:Arial;fill:url(#linearGradient4353);fill-opacity:1;stroke-width:0.26458332\">%SERIES A%</tspan></text>");
			String regexX = ".*%X-AXIS_TICKS%.*";
			String regexY = ".*%Y-AXIS_TICKS%.*";
			String titleX = ".*%PLACEHOLDER X-AXIS%.*";
			String titleY = ".*%PLACEHOLDER Y-AXIS%.*";
			String data_series = ".*%DATA SERIES%.*";
			String regex_legend = ".*%LEGEND%.*";
			while((line = in.readLine()) != null) {
				if(Pattern.matches(regexX, line)) {
					int ticklabel_size = xTicks.length;
					double start = 20.306;
					double height = 307.216 - 20.306;
					StringBuilder out = new StringBuilder("");
					double upper = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().upper;
					double lower = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().lower;
					for(int count = 0; count < ticklabel_size; count++) {
						String split[] = tickX.toString().split("\\n");
						String match1 = ".*%x-coordinate%.*";
						String match2 = ".*%X01%.*";
						double x = start + ((xTicks[count] - lower) / (upper - lower) * height);
						for(String string : split) {
							if(Pattern.matches(match1, string)) {
								string = string.replace("%x-coordinate%", String.valueOf(x));
							} else if(Pattern.matches(match2, string)) {
								string = string.replace("%X01%", String.valueOf(formatX.format(xTicks[count])));
							}
							out.append(string);
							out.append("\n");
						}
						out.append("\n");
					}
					line = line.replaceAll(regexX, out.toString());
				} else if(Pattern.matches(regexY, line)) {
					int ticklabel_size = yTicks.length;
					double start = 279.90709;
					double height = 200.0;
					StringBuilder out = new StringBuilder("");
					double upper = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().upper;
					double lower = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().lower;
					for(int count = 0; count < ticklabel_size; count++) {
						String split[] = tickY.toString().split("\\n");
						String match1 = ".*%y-coordinate%.*";
						String match2 = ".*%Y01%.*";
						double y = start - (height - ((upper - yTicks[count]) / (upper - lower) * height));
						for(String string : split) {
							if(Pattern.matches(match1, string)) {
								string = string.replace("%y-coordinate%", String.valueOf(y));
							} else if(Pattern.matches(match2, string)) {
								string = string.replace("%Y01%", String.valueOf(formatY.format(yTicks[count])));
							}
							out.append(string);
							out.append("\n");
						}
						out.append("\n");
					}
					line = line.replaceAll(regexY, out.toString());
				} else if(Pattern.matches(titleX, line)) {
					line = line.replace("%PLACEHOLDER X-AXIS%", axisSettingsX.getTitle());
				} else if(Pattern.matches(titleY, line)) {
					line = line.replace("%PLACEHOLDER Y-AXIS%", axisSettingsY.getTitle());
				} else if(Pattern.matches(regex_legend, line)) {
					double start1 = 100.5815;
					double start2 = 102.06668;
					String color[] = {"#000000", "#FF0000", "#0000FF", "#008000", "#808080", "#800080", "#FFFF00", "#A52A2A", "#FFC0CB", "#FFA500"};
					StringBuilder out = new StringBuilder("");
					for(int count = 0; count < series.length; count++) {
						String col = color[count % 10];
						double y1 = start1 + 6 * count;
						double y2 = start2 + 6 * count;
						String des = series[count].getDescription();
						String split[] = legend.toString().split("\\n");
						String match1 = ".*%y1-coordinate%.*";
						String match2 = ".*%y2-coordinate%.*";
						String match3 = ".*%COLOR%.*";
						String match4 = ".*%SERIES A%.*";
						for(String string : split) {
							if(Pattern.matches(match1, string)) {
								string = string.replace("%y1-coordinate%", String.valueOf(y1));
							} else if(Pattern.matches(match2, string)) {
								string = string.replace("%y2-coordinate%", String.valueOf(y2));
							} else if(Pattern.matches(match3, string)) {
								string = string.replace("%COLOR%", col);
							} else if(Pattern.matches(match4, string)) {
								string = string.replace("%SERIES A%", des);
							}
							out.append(string);
							out.append("\n");
						}
						out.append("\n");
					}
					line = line.replaceAll(regex_legend, out.toString());
				} else if(Pattern.matches(data_series, line)) {
					StringBuilder out = new StringBuilder("");
					int widthPlotArea = baseChart.getPlotArea().getSize().x;
					int heightPlotArea = baseChart.getPlotArea().getSize().y;
					IAxisSet axisSet = baseChart.getAxisSet();
					int index = 0;
					for(ISeries<?> dataSeries : series) {
						LineSeries lineSeries = (LineSeries)dataSeries;
						LineStyle lineStyle = lineSeries.getLineStyle();
						if(dataSeries != null) {
							StringBuilder string = null;
							if(lineStyle != LineStyle.NONE) {
								if(exportVisibleOnly) {
									if(dataSeries.isVisible()) {
										string = printLineData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, printWriter, axisSet);
									}
								} else {
									string = printLineData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, printWriter, axisSet);
								}
							} else {
								if(exportVisibleOnly) {
									if(dataSeries.isVisible()) {
										string = printScatterData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, printWriter, axisSet);
									}
								} else {
									string = printScatterData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, printWriter, axisSet);
								}
							}
							out.append(string);
						}
					}
					line = line.replaceAll(data_series, out.toString());
				}
				printWriter.println(line);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	/*
	 * returns the data series to be replaced in the data series in template
	 */
	private StringBuilder printLineData(ISeries<?> dataSeries, int widthPlotArea, int heightPlotArea, AxisSettings axisSettings, int index, PrintWriter printWriter, IAxisSet axisSet) {

		StringBuilder out = new StringBuilder("");
		StringBuilder data = new StringBuilder("<path\n" + "               style=\"fill:none;stroke:%COLOR%;stroke-width:0.45888707;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n" + "               d=\"M %DATA POINTS%\"\n" + "               id=\"path1740\"\n" + "               inkscape:connector-curvature=\"0\" />");
		String color[] = {"#000000", "#FF0000", "#0000FF", "#008000", "#808080", "#800080", "#FFFF00", "#A52A2A", "#FFC0CB", "#FFA500"};
		int indexAxisX = axisSettings.getIndexAxisX();
		int indexAxisY = axisSettings.getIndexAxisY();
		IAxisScaleConverter axisScaleConverterX = axisSettings.getAxisScaleConverterX();
		IAxisScaleConverter axisScaleConverterY = axisSettings.getAxisScaleConverterY();
		//
		double[] xSeries = dataSeries.getXSeries();
		double[] ySeries = dataSeries.getYSeries();
		String split[] = data.toString().split("\\n");
		int size = dataSeries.getXSeries().length;
		//
		String match1 = ".*%COLOR%.*";
		String match2 = ".*%DATA POINTS%.*";
		for(String string : split) {
			if(Pattern.matches(match1, string)) {
				string = string.replace("%COLOR%", color[index % 10]);
			} else if(Pattern.matches(match2, string)) {
				StringBuilder rep = new StringBuilder("");
				for(int i = 0; i < size; i++) {
					/*
					 * Only export if the data point is visible.
					 */
					Point point = dataSeries.getPixelCoordinates(i);
					if((point.x >= 0 && point.x <= widthPlotArea) && (point.y >= 0 && point.y <= heightPlotArea)) {
						rep.append(printValueLinePlot(AXIS_X, index, printWriter, xSeries[i], indexAxisX, axisSet, BaseChart.ID_PRIMARY_X_AXIS, axisScaleConverterX));
						rep.append(",");
						rep.append(printValueLinePlot(AXIS_Y, index, printWriter, ySeries[i], indexAxisY, axisSet, BaseChart.ID_PRIMARY_Y_AXIS, axisScaleConverterY));
						rep.append(" ");
					}
				}
				string = string.replace("%DATA POINTS%", rep);
			}
			out.append(string);
			out.append("\n");
		}
		return out;
	}

	/*
	 * returns value scaled to the appropriate coordinates in SVG
	 */
	private String printValueLinePlot(String axis, int index, PrintWriter printWriter, double value, int indexAxis, IAxisSet axisSet, int indexPrimaryAxis, IAxisScaleConverter axisScaleConverter) {

		String ret = "";
		double x = 255.5 - 23.5;
		double y = 263.5 - 80.5;
		if(indexAxis == indexPrimaryAxis || axisScaleConverter == null) {
			if(axis.equals(AXIS_X)) {
				IAxis xAxis = axisSet.getXAxis(indexAxis);
				double xUpper = xAxis.getRange().upper;
				double xLower = xAxis.getRange().lower;
				double x1 = 23.5 + ((value - xLower) / (xUpper - xLower) * x);
				ret = String.valueOf(x1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			} else if(axis.equals(AXIS_Y)) {
				IAxis yAxis = axisSet.getYAxis(indexAxis);
				double yUpper = yAxis.getRange().upper;
				double yLower = yAxis.getRange().lower;
				double y1 = 263.5 - (y - ((yUpper - value) / (yUpper - yLower) * y));
				ret = String.valueOf(y1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
		} else {
			if(axisScaleConverter != null) {
				if(axis.equals(AXIS_X)) {
					IAxis xAxis = axisSet.getXAxis(indexAxis);
					double xUpper = xAxis.getRange().upper;
					double xLower = xAxis.getRange().lower;
					double x1 = 23.5 + ((axisScaleConverter.convertToSecondaryUnit(value) - xLower) / (xUpper - xLower) * x);
					ret = String.valueOf(x1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				} else if(axis.equals(AXIS_Y)) {
					IAxis yAxis = axisSet.getYAxis(indexAxis);
					double yUpper = yAxis.getRange().upper;
					double yLower = yAxis.getRange().lower;
					double y1 = 80.0 + ((yUpper - axisScaleConverter.convertToSecondaryUnit(value)) / (yUpper - yLower) * y);
					ret = String.valueOf(y1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
			}
		}
		return ret;
	}

	/*
	 * Exports The BarSeries Chart to SVG via the Inkscape Template
	 */
	private void printBarPlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		boolean exportVisibleOnly = axisSettings.isExportVisibleOnly();
		DecimalFormat formatX = axisSettingsX.getDecimalFormat();
		DecimalFormat formatY = axisSettingsY.getDecimalFormat();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
		String Template = "Template_BarChart.svg";
		try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(Template)))) {
			double xTicks[] = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getTick().getTickLabelValues();
			double yTicks[] = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getTick().getTickLabelValues();
			String line;
			StringBuilder tickX = new StringBuilder("<path\n" + "                       inkscape:connector-curvature=\"0\"\n" + "                       id=\"path888\"\n" + "                       d=\"m %x-coordinate%,279.77439 v 5.05309\"\n" + "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" + "                    <text\n" + "		       id=\"text892\"\n" + "                       y=\"289.01782\"\n" + "                       x=\"%x-coordinate%\"\n" + "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                       xml:space=\"preserve\"><tspan\n" + "                         id=\"tspan890\"\n" + "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:center;text-anchor:middle;stroke-width:0.26458332\"\n" + "                         y=\"289.01782\"\n" + "                         x=\"%x-coordinate%\"\n" + "                         sodipodi:role=\"line\">%X01%</tspan></text>");
			StringBuilder tickY = new StringBuilder("<path\n" + "                       inkscape:connector-curvature=\"0\"\n" + "                       id=\"path1299\"\n" + "                       d=\"m 15.387913,%y-coordinate% h 5.05309\"\n" + "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" + "                    <text\n" + "                       xml:space=\"preserve\"\n" + "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                       x=\"14.211229\"\n" + "                       y=\"%y-coordinate%\"\n" + "                       id=\"text1303\"><tspan\n" + "                         sodipodi:role=\"line\"\n" + "                         id=\"tspan1301\"\n" + "                         x=\"14.211229\"\n" + "                         y=\"%y-coordinate%\"\n" + "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:end;text-anchor:end;stroke-width:0.26458332\">%Y01%</tspan></text>");
			StringBuilder legend = new StringBuilder("<path\n" + "                   style=\"fill:url(#linearGradient3662);fill-opacity:1;stroke:%COLOR%;stroke-width:0.5;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n" + "                   d=\"m 209.71446,%y1-coordinate% h 20.93268\"\n" + "                   id=\"path1744\"\n" + "                   inkscape:connector-curvature=\"0\" />\n" + "                <text\n" + "                   xml:space=\"preserve\"\n" + "                   style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:'Bold Oblique';-inkscape-font-specification:'Bold Oblique, ';letter-spacing:0px;word-spacing:0px;fill:url(#linearGradient4353);fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                   x=\"230.91121\"\n" + "                   y=\"%y2-coordinate%\"\n" + "                   id=\"text1748\"><tspan\n" + "                     sodipodi:role=\"line\"\n" + "                     id=\"tspan1746\"\n" + "                     x=\"230.91121\"\n" + "                     y=\"%y2-coordinate%\"\n" + "                     style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:4.23333311px;font-family:Arial;-inkscape-font-specification:Arial;fill:url(#linearGradient4353);fill-opacity:1;stroke-width:0.26458332\">%SERIES A%</tspan></text>");
			String regexX = ".*%X-AXIS_TICKS%.*";
			String regexY = ".*%Y-AXIS_TICKS%.*";
			String titleX = ".*%PLACEHOLDER X-AXIS%.*";
			String titleY = ".*%PLACEHOLDER Y-AXIS%.*";
			String data_series = ".*%DATA SERIES%.*";
			String regex_legend = ".*%LEGEND%.*";
			while((line = in.readLine()) != null) {
				if(Pattern.matches(regexX, line)) {
					int ticklabel_size = xTicks.length;
					double start = 20.306;
					double height = 307.216 - 20.306;
					StringBuilder out = new StringBuilder("");
					double upper = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().upper;
					double lower = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().lower;
					for(int count = 0; count < ticklabel_size; count++) {
						String split[] = tickX.toString().split("\\n");
						String match1 = ".*%x-coordinate%.*";
						String match2 = ".*%X01%.*";
						double x = start + ((xTicks[count] - lower) / (upper - lower) * height);
						for(String string : split) {
							if(Pattern.matches(match1, string)) {
								string = string.replace("%x-coordinate%", String.valueOf(x));
							} else if(Pattern.matches(match2, string)) {
								string = string.replace("%X01%", String.valueOf(formatX.format(xTicks[count])));
							}
							out.append(string);
							out.append("\n");
						}
						out.append("\n");
					}
					line = line.replaceAll(regexX, out.toString());
				} else if(Pattern.matches(regexY, line)) {
					int ticklabel_size = yTicks.length;
					double start = 279.90709;
					double height = 200.0;
					StringBuilder out = new StringBuilder("");
					double upper = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().upper;
					double lower = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().lower;
					for(int count = 0; count < ticklabel_size; count++) {
						String split[] = tickY.toString().split("\\n");
						String match1 = ".*%y-coordinate%.*";
						String match2 = ".*%Y01%.*";
						double y = start - (height - ((upper - yTicks[count]) / (upper - lower) * height));
						for(String string : split) {
							if(Pattern.matches(match1, string)) {
								string = string.replace("%y-coordinate%", String.valueOf(y));
							} else if(Pattern.matches(match2, string)) {
								string = string.replace("%Y01%", String.valueOf(formatY.format(yTicks[count])));
							}
							out.append(string);
							out.append("\n");
						}
						out.append("\n");
					}
					line = line.replaceAll(regexY, out.toString());
				} else if(Pattern.matches(titleX, line)) {
					line = line.replace("%PLACEHOLDER X-AXIS%", axisSettingsX.getTitle());
				} else if(Pattern.matches(titleY, line)) {
					line = line.replace("%PLACEHOLDER Y-AXIS%", axisSettingsY.getTitle());
				} else if(Pattern.matches(regex_legend, line)) {
					double start1 = 100.5815;
					double start2 = 102.06668;
					String color[] = {"#FF0000", "#000000", "#0000FF", "#008000", "#808080", "#800080", "#FFFF00", "#A52A2A", "#FFC0CB", "#FFA500"};
					StringBuilder out = new StringBuilder("");
					for(int count = 0; count < series.length; count++) {
						String col = color[count % 10];
						double y1 = start1 + 6 * count;
						double y2 = start2 + 6 * count;
						String des = series[count].getDescription();
						String split[] = legend.toString().split("\\n");
						String match1 = ".*%y1-coordinate%.*";
						String match2 = ".*%y2-coordinate%.*";
						String match3 = ".*%COLOR%.*";
						String match4 = ".*%SERIES A%.*";
						for(String string : split) {
							if(Pattern.matches(match1, string)) {
								string = string.replace("%y1-coordinate%", String.valueOf(y1));
							} else if(Pattern.matches(match2, string)) {
								string = string.replace("%y2-coordinate%", String.valueOf(y2));
							} else if(Pattern.matches(match3, string)) {
								string = string.replace("%COLOR%", col);
							} else if(Pattern.matches(match4, string)) {
								string = string.replace("%SERIES A%", des);
							}
							out.append(string);
							out.append("\n");
						}
						out.append("\n");
					}
					line = line.replaceAll(regex_legend, out.toString());
				} else if(Pattern.matches(data_series, line)) {
					StringBuilder out = new StringBuilder("");
					int widthPlotArea = baseChart.getPlotArea().getSize().x;
					int heightPlotArea = baseChart.getPlotArea().getSize().y;
					IAxisSet axisSet = baseChart.getAxisSet();
					int index = 0;
					for(ISeries<?> dataSeries : series) {
						if(dataSeries != null) {
							StringBuilder string = null;
							if(exportVisibleOnly) {
								if(dataSeries.isVisible()) {
									string = printBarData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, printWriter, axisSet);
								}
							} else {
								string = printBarData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, printWriter, axisSet);
							}
							out.append(string);
						}
					}
					line = line.replaceAll(data_series, out.toString());
				}
				printWriter.println(line);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	/*
	 * returns the data series to be replaced in the data series in template
	 */
	private StringBuilder printBarData(ISeries<?> dataSeries, int widthPlotArea, int heightPlotArea, AxisSettings axisSettings, int index, PrintWriter printWriter, IAxisSet axisSet) {

		StringBuilder out = new StringBuilder("");
		/* BarSeries to be added to the template during export */
		StringBuilder data = new StringBuilder("<rect\n" + "         style=\"opacity:1;fill:%COLOR%;fill-opacity:1;stroke:none;stroke-width:0.96499991;stroke-linecap:square;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-dashoffset:0;stroke-opacity:1\"\n" + "         id=\"rect901\"\n" + "         width=\"1\"\n" + "         height=\"%height%\"\n" + "         x=\"%x-coordinate%\"\n" + "         y=\"%y-coordinate%\"\n" + "         ry=\"0\" />");
		String color[] = {"#FF0000", "#000000", "#0000FF", "#008000", "#808080", "#800080", "#FFFF00", "#A52A2A", "#FFC0CB", "#FFA500"};
		int indexAxisX = axisSettings.getIndexAxisX();
		int indexAxisY = axisSettings.getIndexAxisY();
		IAxisScaleConverter axisScaleConverterX = axisSettings.getAxisScaleConverterX();
		IAxisScaleConverter axisScaleConverterY = axisSettings.getAxisScaleConverterY();
		//
		double[] xSeries = dataSeries.getXSeries();
		double[] ySeries = dataSeries.getYSeries();
		String split[] = data.toString().split("\\n");
		int size = dataSeries.getXSeries().length;
		//
		String match1 = ".*%COLOR%.*";
		String match2 = ".*%x-coordinate%.*";
		String match3 = ".*%y-coordinate%.*";
		String match4 = ".*%height%.*";
		for(int i = 0; i < size; i++) {
			/*
			 * Only export if the data point is visible.
			 */
			Point point = dataSeries.getPixelCoordinates(i);
			if((point.x >= 0 && point.x <= widthPlotArea)) {
				double offset = 0.25;
				double x = Double.parseDouble(printValueBarPlot(AXIS_X, index, printWriter, xSeries[i], indexAxisX, axisSet, BaseChart.ID_PRIMARY_X_AXIS, axisScaleConverterX));
				double y = Double.parseDouble(printValueBarPlot(AXIS_Y, index, printWriter, ySeries[i], indexAxisY, axisSet, BaseChart.ID_PRIMARY_Y_AXIS, axisScaleConverterY));
				double base = Double.parseDouble(printValueBarPlot(AXIS_Y, index, printWriter, 0.0, indexAxisY, axisSet, BaseChart.ID_PRIMARY_Y_AXIS, axisScaleConverterY));
				double height = Math.abs(y - base);
				/* Width for BarSeries in the Export */
				double width = 1.0;
				/* Values for X,Y, height in the Export */
				double newX = x;
				double newY = y;
				double newWidth = width;
				double newHeight = height;
				/*
				 * Adjusting Bar Width For Export
				 * Have a look at the BarSeries implementation
				 */
				if(x < 23.5) {
					newX = -offset;
					newWidth += (x + offset);
				}
				if(y < 80.5) {
					newY = -offset;
					newHeight += y + offset;
				}
				double plotWidthArea = 255.5 - 23.5;
				if(x + width > plotWidthArea) {
					newWidth -= x + width - plotWidthArea + offset;
					if(newWidth < 0) {
						newWidth = 0;
					}
				}
				if(y > base) {
					if(base + height > y) {
						newHeight -= (base + height) - y + offset;
						if(newHeight < 0) {
							newHeight = 0;
						}
					}
				} else {
					if(y + height > base) {
						newHeight -= (y + height) - base + offset;
						if(newHeight < 0) {
							newHeight = 0;
						}
					}
				}
				for(String string : split) {
					if(Pattern.matches(match1, string)) {
						string = string.replace("%COLOR%", color[index % 10]);
					} else if(Pattern.matches(match2, string)) {
						string = string.replace("%x-coordinate%", String.valueOf(newX));
					} else if(Pattern.matches(match3, string)) {
						if(ySeries[i] >= 0) {
							string = string.replace("%y-coordinate%", String.valueOf(newY));
						} else {
							string = string.replace("%y-coordinate%", String.valueOf(base));
						}
					} else if(Pattern.matches(match4, string)) {
						string = string.replace("%height%", String.valueOf(newHeight));
					}
					out.append(string);
					out.append("\n");
				}
			}
		}
		return out;
	}

	/*
	 * returns value scaled to the appropriate coordinates in SVG
	 */
	private String printValueBarPlot(String axis, int index, PrintWriter printWriter, double value, int indexAxis, IAxisSet axisSet, int indexPrimaryAxis, IAxisScaleConverter axisScaleConverter) {

		String ret = null;
		double x = 255.5 - 23.5;
		double y = 263.5 - 80.5;
		if(indexAxis == indexPrimaryAxis || axisScaleConverter == null) {
			if(axis.equals(AXIS_X)) {
				IAxis xAxis = axisSet.getXAxis(indexAxis);
				double xUpper = xAxis.getRange().upper;
				double xLower = xAxis.getRange().lower;
				value = (value > xUpper) ? xUpper : value;
				value = (value < xLower) ? xLower : value;
				double x1 = 23.5 + ((value - xLower) / (xUpper - xLower) * x);
				ret = String.valueOf(x1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			} else if(axis.equals(AXIS_Y)) {
				IAxis yAxis = axisSet.getYAxis(indexAxis);
				double yUpper = yAxis.getRange().upper;
				double yLower = yAxis.getRange().lower;
				value = (value > yUpper) ? yUpper : value;
				value = (value < yLower) ? yLower : value;
				double y1 = 263.5 - (y - ((yUpper - value) / (yUpper - yLower) * y));
				ret = String.valueOf(y1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
		} else {
			if(axisScaleConverter != null) {
				if(axis.equals(AXIS_X)) {
					IAxis xAxis = axisSet.getXAxis(indexAxis);
					double xUpper = xAxis.getRange().upper;
					double xLower = xAxis.getRange().lower;
					value = axisScaleConverter.convertToSecondaryUnit(value);
					value = (value > xUpper) ? xUpper : value;
					value = (value < xLower) ? xLower : value;
					double x1 = 23.5 + ((value - xLower) / (xUpper - xLower) * x);
					ret = String.valueOf(x1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				} else if(axis.equals(AXIS_Y)) {
					IAxis yAxis = axisSet.getYAxis(indexAxis);
					double yUpper = yAxis.getRange().upper;
					double yLower = yAxis.getRange().lower;
					value = axisScaleConverter.convertToSecondaryUnit(value);
					value = (value > yUpper) ? yUpper : value;
					value = (value < yLower) ? yLower : value;
					double y1 = 80.5 + ((yUpper - value) / (yUpper - yLower) * y);
					ret = String.valueOf(y1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
			}
		}
		return ret;
	}

	private void printScatterPlot(String fileName, PrintWriter printWriter, ScrollableChart scrollableChart, AxisSettings axisSettings) {

		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		boolean exportVisibleOnly = axisSettings.isExportVisibleOnly();
		DecimalFormat formatX = axisSettingsX.getDecimalFormat();
		DecimalFormat formatY = axisSettingsY.getDecimalFormat();
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
		String Template = "Template_ScatterChart.svg";
		try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(Template)))) {
			double xTicks[] = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getTick().getTickLabelValues();
			double yTicks[] = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getTick().getTickLabelValues();
			String line;
			StringBuilder tickX = new StringBuilder("<path\n" + "                       inkscape:connector-curvature=\"0\"\n" + "                       id=\"path888\"\n" + "                       d=\"m %x-coordinate%,279.77439 v 5.05309\"\n" + "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" + "                    <text\n" + "		       id=\"text892\"\n" + "                       y=\"289.01782\"\n" + "                       x=\"%x-coordinate%\"\n" + "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                       xml:space=\"preserve\"><tspan\n" + "                         id=\"tspan890\"\n" + "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:center;text-anchor:middle;stroke-width:0.26458332\"\n" + "                         y=\"289.01782\"\n" + "                         x=\"%x-coordinate%\"\n" + "                         sodipodi:role=\"line\">%X01%</tspan></text>");
			StringBuilder tickY = new StringBuilder("<path\n" + "                       inkscape:connector-curvature=\"0\"\n" + "                       id=\"path1299\"\n" + "                       d=\"m 15.387913,%y-coordinate% h 5.05309\"\n" + "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" + "                    <text\n" + "                       xml:space=\"preserve\"\n" + "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                       x=\"14.211229\"\n" + "                       y=\"%y-coordinate%\"\n" + "                       id=\"text1303\"><tspan\n" + "                         sodipodi:role=\"line\"\n" + "                         id=\"tspan1301\"\n" + "                         x=\"14.211229\"\n" + "                         y=\"%y-coordinate%\"\n" + "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:end;text-anchor:end;stroke-width:0.26458332\">%Y01%</tspan></text>");
			StringBuilder legend = new StringBuilder("<path\n" + "                   style=\"fill:url(#linearGradient3662);fill-opacity:1;stroke:%COLOR%;stroke-width:0.5;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n" + "                   d=\"m 209.71446,%y1-coordinate% h 20.93268\"\n" + "                   id=\"path1744\"\n" + "                   inkscape:connector-curvature=\"0\" />\n" + "                <text\n" + "                   xml:space=\"preserve\"\n" + "                   style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:'Bold Oblique';-inkscape-font-specification:'Bold Oblique, ';letter-spacing:0px;word-spacing:0px;fill:url(#linearGradient4353);fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                   x=\"230.91121\"\n" + "                   y=\"%y2-coordinate%\"\n" + "                   id=\"text1748\"><tspan\n" + "                     sodipodi:role=\"line\"\n" + "                     id=\"tspan1746\"\n" + "                     x=\"230.91121\"\n" + "                     y=\"%y2-coordinate%\"\n" + "                     style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:4.23333311px;font-family:Arial;-inkscape-font-specification:Arial;fill:url(#linearGradient4353);fill-opacity:1;stroke-width:0.26458332\">%SERIES A%</tspan></text>");
			String regexX = ".*%X-AXIS_TICKS%.*";
			String regexY = ".*%Y-AXIS_TICKS%.*";
			String titleX = ".*%PLACEHOLDER X-AXIS%.*";
			String titleY = ".*%PLACEHOLDER Y-AXIS%.*";
			String data_series = ".*%DATA SERIES%.*";
			String regex_legend = ".*%LEGEND%.*";
			while((line = in.readLine()) != null) {
				if(Pattern.matches(regexX, line)) {
					int ticklabel_size = xTicks.length;
					double start = 20.306;
					double height = 307.216 - 20.306;
					StringBuilder out = new StringBuilder("");
					double upper = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().upper;
					double lower = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().lower;
					for(int count = 0; count < ticklabel_size; count++) {
						String split[] = tickX.toString().split("\\n");
						String match1 = ".*%x-coordinate%.*";
						String match2 = ".*%X01%.*";
						double x = start + ((xTicks[count] - lower) / (upper - lower) * height);
						for(String string : split) {
							if(Pattern.matches(match1, string)) {
								string = string.replace("%x-coordinate%", String.valueOf(x));
							} else if(Pattern.matches(match2, string)) {
								string = string.replace("%X01%", String.valueOf(formatX.format(xTicks[count])));
							}
							out.append(string);
							out.append("\n");
						}
						out.append("\n");
					}
					line = line.replaceAll(regexX, out.toString());
				} else if(Pattern.matches(regexY, line)) {
					int ticklabel_size = yTicks.length;
					double start = 279.90709;
					double height = 200.0;
					StringBuilder out = new StringBuilder("");
					double upper = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().upper;
					double lower = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().lower;
					for(int count = 0; count < ticklabel_size; count++) {
						String split[] = tickY.toString().split("\\n");
						String match1 = ".*%y-coordinate%.*";
						String match2 = ".*%Y01%.*";
						double y = start - (height - ((upper - yTicks[count]) / (upper - lower) * height));
						for(String string : split) {
							if(Pattern.matches(match1, string)) {
								string = string.replace("%y-coordinate%", String.valueOf(y));
							} else if(Pattern.matches(match2, string)) {
								string = string.replace("%Y01%", String.valueOf(formatY.format(yTicks[count])));
							}
							out.append(string);
							out.append("\n");
						}
						out.append("\n");
					}
					line = line.replaceAll(regexY, out.toString());
				} else if(Pattern.matches(titleX, line)) {
					line = line.replace("%PLACEHOLDER X-AXIS%", axisSettingsX.getTitle());
				} else if(Pattern.matches(titleY, line)) {
					line = line.replace("%PLACEHOLDER Y-AXIS%", axisSettingsY.getTitle());
				} else if(Pattern.matches(regex_legend, line)) {
					double start1 = 100.5815;
					double start2 = 102.06668;
					String color[] = {"#FF0000", "#000000", "#0000FF", "#008000", "#808080", "#800080", "#FFFF00", "#A52A2A", "#FFC0CB", "#FFA500"};
					StringBuilder out = new StringBuilder("");
					for(int count = 0; count < series.length; count++) {
						String col = color[count % 10];
						double y1 = start1 + 6 * count;
						double y2 = start2 + 6 * count;
						String des = series[count].getDescription();
						String split[] = legend.toString().split("\\n");
						String match1 = ".*%y1-coordinate%.*";
						String match2 = ".*%y2-coordinate%.*";
						String match3 = ".*%COLOR%.*";
						String match4 = ".*%SERIES A%.*";
						for(String string : split) {
							if(Pattern.matches(match1, string)) {
								string = string.replace("%y1-coordinate%", String.valueOf(y1));
							} else if(Pattern.matches(match2, string)) {
								string = string.replace("%y2-coordinate%", String.valueOf(y2));
							} else if(Pattern.matches(match3, string)) {
								string = string.replace("%COLOR%", col);
							} else if(Pattern.matches(match4, string)) {
								string = string.replace("%SERIES A%", des);
							}
							out.append(string);
							out.append("\n");
						}
						out.append("\n");
					}
					line = line.replaceAll(regex_legend, out.toString());
				} else if(Pattern.matches(data_series, line)) {
					StringBuilder out = new StringBuilder("");
					int widthPlotArea = baseChart.getPlotArea().getSize().x;
					int heightPlotArea = baseChart.getPlotArea().getSize().y;
					IAxisSet axisSet = baseChart.getAxisSet();
					int index = 0;
					for(ISeries<?> dataSeries : series) {
						if(dataSeries != null) {
							StringBuilder string = null;
							if(exportVisibleOnly) {
								if(dataSeries.isVisible()) {
									string = printScatterData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, printWriter, axisSet);
								}
							} else {
								string = printScatterData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, printWriter, axisSet);
							}
							out.append(string);
						}
					}
					line = line.replaceAll(data_series, out.toString());
				}
				printWriter.println(line);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	private StringBuilder printScatterData(ISeries<?> dataSeries, int widthPlotArea, int heightPlotArea, AxisSettings axisSettings, int index, PrintWriter printWriter, IAxisSet axisSet) {

		StringBuilder out = new StringBuilder("");
		StringBuilder data = new StringBuilder("<circle\n" + "         style=\"opacity:1;fill:%COLOR%;fill-opacity:1;stroke:none;stroke-width:0.96499991;stroke-linecap:square;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-dashoffset:0;stroke-opacity:1\"\n" + "         id=\"rect901\"\n" + "	 cx=\"%x-coordinate%\"\n" + "	 cy=\"%y-coordinate%\"\n" + "	 r=\"1\" />");
		String color[] = {"#FF0000", "#000000", "#0000FF", "#008000", "#808080", "#800080", "#FFFF00", "#A52A2A", "#FFC0CB", "#FFA500"};
		int indexAxisX = axisSettings.getIndexAxisX();
		int indexAxisY = axisSettings.getIndexAxisY();
		IAxisScaleConverter axisScaleConverterX = axisSettings.getAxisScaleConverterX();
		IAxisScaleConverter axisScaleConverterY = axisSettings.getAxisScaleConverterY();
		//
		double[] xSeries = dataSeries.getXSeries();
		double[] ySeries = dataSeries.getYSeries();
		String split[] = data.toString().split("\\n");
		int size = dataSeries.getXSeries().length;
		//
		String match1 = ".*%COLOR%.*";
		String match2 = ".*%x-coordinate%.*";
		String match3 = ".*%y-coordinate%.*";
		for(int i = 0; i < size; i++) {
			/*
			 * Only export if the data point is visible.
			 */
			Point point = dataSeries.getPixelCoordinates(i);
			if((point.x >= 0 && point.x <= widthPlotArea) && (point.y >= 0 && point.y <= heightPlotArea)) {
				double x = Double.parseDouble(printValueScatterPlot(AXIS_X, index, printWriter, xSeries[i], indexAxisX, axisSet, BaseChart.ID_PRIMARY_X_AXIS, axisScaleConverterX));
				double y = Double.parseDouble(printValueScatterPlot(AXIS_Y, index, printWriter, ySeries[i], indexAxisY, axisSet, BaseChart.ID_PRIMARY_Y_AXIS, axisScaleConverterY));
				for(String string : split) {
					if(Pattern.matches(match1, string)) {
						string = string.replace("%COLOR%", color[index % 10]);
					} else if(Pattern.matches(match2, string)) {
						string = string.replace("%x-coordinate%", String.valueOf(x));
					} else if(Pattern.matches(match3, string)) {
						string = string.replace("%y-coordinate%", String.valueOf(y));
					}
					out.append(string);
					out.append("\n");
				}
			}
		}
		return out;
	}

	private String printValueScatterPlot(String axis, int index, PrintWriter printWriter, double value, int indexAxis, IAxisSet axisSet, int indexPrimaryAxis, IAxisScaleConverter axisScaleConverter) {

		String ret = null;
		double x = 255.5 - 23.5;
		double y = 263.5 - 80.5;
		if(indexAxis == indexPrimaryAxis || axisScaleConverter == null) {
			if(axis.equals(AXIS_X)) {
				IAxis xAxis = axisSet.getXAxis(indexAxis);
				double xUpper = xAxis.getRange().upper;
				double xLower = xAxis.getRange().lower;
				double x1 = 23.5 + ((value - xLower) / (xUpper - xLower) * x);
				ret = String.valueOf(x1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			} else if(axis.equals(AXIS_Y)) {
				IAxis yAxis = axisSet.getYAxis(indexAxis);
				double yUpper = yAxis.getRange().upper;
				double yLower = yAxis.getRange().lower;
				double y1 = 263.5 - (y - ((yUpper - value) / (yUpper - yLower) * y));
				ret = String.valueOf(y1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
		} else {
			if(axisScaleConverter != null) {
				if(axis.equals(AXIS_X)) {
					IAxis xAxis = axisSet.getXAxis(indexAxis);
					double xUpper = xAxis.getRange().upper;
					double xLower = xAxis.getRange().lower;
					value = axisScaleConverter.convertToSecondaryUnit(value);
					double x1 = 23.5 + ((value - xLower) / (xUpper - xLower) * x);
					ret = String.valueOf(x1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				} else if(axis.equals(AXIS_Y)) {
					IAxis yAxis = axisSet.getYAxis(indexAxis);
					double yUpper = yAxis.getRange().upper;
					double yLower = yAxis.getRange().lower;
					value = axisScaleConverter.convertToSecondaryUnit(value);
					double y1 = 80.5 + ((yUpper - value) / (yUpper - yLower) * y);
					ret = String.valueOf(y1);// $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}
			}
		}
		return ret;
	}
}
