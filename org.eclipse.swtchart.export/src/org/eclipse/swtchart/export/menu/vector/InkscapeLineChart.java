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
 * Philip Wenig - initial API and implementation
 * Sanatt Abrol - SVG export code
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.export.menu.vector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.export.core.AxisSettings;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public class InkscapeLineChart extends AbstractInkscapeLineChart {

	private static final String TEMPLATE_LINE_CHART = "Template_LineChart.svg";
	//
	private static final String X_AXIS_TICKS = "%X-AXIS_TICKS%";
	private static final String Y_AXIS_TICKS = "%Y-AXIS_TICKS%";
	private static final String PLACEHOLDER_X_AXIS = "%PLACEHOLDER X-AXIS%";
	private static final String PLACEHOLDER_Y_AXIS = "%PLACEHOLDER Y-AXIS%";
	private static final String DATA_SERIES = "%DATA SERIES%";
	private static final String LEGEND = "%LEGEND%";
	private static final String AXIS_LABELS = "%AXIS LABELS%";
	private static final String COLOR = "%COLOR%";
	private static final String DATA_POINTS = "%DATA POINTS%";
	private static final String SERIES_A = "%SERIES A%";
	private static final String X_COORDINATE = "%X-COORDINATE%";
	private static final String X_01 = "%X01%";
	private static final String Y_COORDINATE = "%Y-COORDINATE%";
	private static final String Y_01 = "%Y01%";
	private static final String X1_COORDINATE = "%X1-COORDINATE%";
	private static final String X2_COORDINATE = "%X2-COORDINATE%";
	private static final String Y1_COORDINATE = "%Y1-COORDINATE%";
	private static final String Y2_COORDINATE = "%Y2-COORDINATE%";
	//
	private static final String HEADER_TICK_X = "<path\n" //
			+ "                       inkscape:connector-curvature=\"0\"\n" //
			+ "                       id=\"path888\"\n" //
			+ "                       d=\"m " + X_COORDINATE + ",279.77439 v 5.05309\"\n" //
			+ "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" //
			+ "                       <text\n" //
			+ "                       id=\"text892\"\n" //
			+ "                       y=\"289.01782\"\n" //
			+ "                       x=\"" + X_COORDINATE + "\"\n" //
			+ "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" //
			+ "                       xml:space=\"preserve\"><tspan\n" //
			+ "                         id=\"tspan890\"\n" //
			+ "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:center;text-anchor:middle;stroke-width:0.26458332\"\n" //
			+ "                         y=\"289.01782\"\n" //
			+ "                         x=\"" + X_COORDINATE + "\"\n" //
			+ "                         sodipodi:role=\"line\">" + X_01 + "</tspan></text>";
	//
	private static final String HEADER_TICK_Y = "<path\n" //
			+ "                       inkscape:connector-curvature=\"0\"\n" //
			+ "                       id=\"path1299\"\n" //
			+ "                       d=\"m 15.387913," + Y_COORDINATE + " h 5.05309\"\n" //
			+ "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" //
			+ "                       <text\n" //
			+ "                       xml:space=\"preserve\"\n" //
			+ "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" //
			+ "                       x=\"14.211229\"\n" //
			+ "                       y=\"" + Y_COORDINATE + "\"\n" //
			+ "                       id=\"text1303\"><tspan\n" //
			+ "                         sodipodi:role=\"line\"\n" //
			+ "                         id=\"tspan1301\"\n" //
			+ "                         x=\"14.211229\"\n" //
			+ "                         y=\"" + Y_COORDINATE + "\"\n" //
			+ "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:end;text-anchor:end;stroke-width:0.26458332\">" + Y_01 + "</tspan></text>";
	//
	private static final String HEADER_LEGEND = "<path\n" //
			+ "                   style=\"fill:url(#linearGradient3662);fill-opacity:1;stroke:" + COLOR + ";stroke-width:0.5;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n" //
			+ "                   d=\"m 209.71446," + Y1_COORDINATE + " h 20.93268\"\n" //
			+ "                   id=\"path1744\"\n" //
			+ "                   inkscape:connector-curvature=\"0\" />\n" //
			+ "                   <text\n" //
			+ "                   xml:space=\"preserve\"\n" //
			+ "                   style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:'Bold Oblique';-inkscape-font-specification:'Bold Oblique, ';letter-spacing:0px;word-spacing:0px;fill:url(#linearGradient4353);fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" //
			+ "                   x=\"230.91121\"\n" //
			+ "                   y=\"" + Y2_COORDINATE + "\"\n" //
			+ "                   id=\"text1748\"><tspan\n" //
			+ "                     sodipodi:role=\"line\"\n" //
			+ "                     id=\"tspan1746\"\n" //
			+ "                     x=\"230.91121\"\n" //
			+ "                     y=\"" + Y2_COORDINATE + "\"\n" //
			+ "                     style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:4.23333311px;font-family:Arial;-inkscape-font-specification:Arial;fill:url(#linearGradient4353);fill-opacity:1;stroke-width:0.26458332\">" + SERIES_A + "</tspan></text>";
	//
	private static final String HEADER_LABEL = "                     <path\n" //
			+ "         style=\"fill:none;stroke:#000000;stroke-width:0.26499999;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;stroke-miterlimit:4;stroke-dasharray:0.52999998,0.52999998;stroke-dashoffset:0;opacity:1\"\n" //
			+ "         d=\"M " + X1_COORDINATE + "," + Y1_COORDINATE + " L " + X2_COORDINATE + "," + Y2_COORDINATE + "\"\n" //
			+ "         id=\"path850\"\n" //
			+ "         inkscape:connector-curvature=\"0\" />";
	//
	private static final String HEADER_DATA = "                    <path\n" //
			+ "               style=\"fill:none;stroke:" + COLOR + ";stroke-width:0.45888707;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n" //
			+ "               d=\"M " + DATA_POINTS + "\"\n" //
			+ "               id=\"path1740\"\n" //
			+ "               inkscape:connector-curvature=\"0\" />";

	@Override
	public String generate(ScrollableChart scrollableChart, AxisSettings axisSettings) throws Exception {

		StringBuilder builder = new StringBuilder();
		//
		IAxisSettings axisSettingsX = axisSettings.getAxisSettingsX();
		IAxisSettings axisSettingsY = axisSettings.getAxisSettingsY();
		boolean isReversedX = axisSettingsX.isReversed();
		boolean isReversedY = axisSettingsY.isReversed();
		DecimalFormat formatX = axisSettingsX.getDecimalFormat();
		DecimalFormat formatY = axisSettingsY.getDecimalFormat();
		BaseChart baseChart = scrollableChart.getBaseChart();
		//
		boolean isShowAxisZeroMarker = baseChart.getChartSettings().isShowAxisZeroMarker();
		ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
		//
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(TEMPLATE_LINE_CHART)))) {
			/*
			 * Ticks
			 */
			double xTicks[] = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getTick().getTickLabelValues();
			double yTicks[] = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getTick().getTickLabelValues();
			//
			StringBuilder tickX = new StringBuilder(HEADER_TICK_X);
			StringBuilder tickY = new StringBuilder(HEADER_TICK_Y);
			StringBuilder legend = new StringBuilder(HEADER_LEGEND);
			StringBuilder axisLabel = new StringBuilder(HEADER_LABEL);
			String regexX = getRegularExpression(X_AXIS_TICKS);
			String regexY = getRegularExpression(Y_AXIS_TICKS);
			String titleX = getRegularExpression(PLACEHOLDER_X_AXIS);
			String titleY = getRegularExpression(PLACEHOLDER_Y_AXIS);
			String data_series = getRegularExpression(DATA_SERIES);
			String regex_legend = getRegularExpression(LEGEND);
			String axis_label = getRegularExpression(AXIS_LABELS);
			//
			String line;
			while((line = bufferedReader.readLine()) != null) {
				if(Pattern.matches(regexX, line)) {
					line = getTickX(baseChart, axisSettings, xTicks, tickX, formatX, isReversedX, regexX, line);
				} else if(Pattern.matches(regexY, line)) {
					line = getTickY(baseChart, axisSettings, yTicks, tickY, formatY, isReversedY, regexY, line);
				} else if(Pattern.matches(titleX, line)) {
					line = line.replace(PLACEHOLDER_X_AXIS, axisSettingsX.getTitle());
				} else if(Pattern.matches(titleY, line)) {
					line = line.replace(PLACEHOLDER_Y_AXIS, axisSettingsY.getTitle());
				} else if(Pattern.matches(regex_legend, line)) {
					line = getLegend(series, legend, regex_legend, line);
				} else if(Pattern.matches(axis_label, line)) {
					line = getAxisLabel(baseChart, axisSettings, axisLabel, isReversedX, isReversedY, isShowAxisZeroMarker, axis_label, line);
				} else if(Pattern.matches(data_series, line)) {
					line = getDataSeries(baseChart, series, axisSettings, isReversedX, isReversedY, data_series, line);
				}
				builder.append(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		//
		return builder.toString();
	}

	private String getTickX(BaseChart baseChart, AxisSettings axisSettings, double xTicks[], StringBuilder tickX, DecimalFormat formatX, boolean isReversedX, String regexX, String line) {

		int ticklabel_size = xTicks.length;
		double start = 20.306;
		double height = 307.216 - 20.306;
		StringBuilder builder = new StringBuilder("");
		double upper = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().upper;
		double lower = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().lower;
		//
		for(int count = 0; count < ticklabel_size; count++) {
			String split[] = tickX.toString().split(SPLIT_LINE_DELIMITER);
			String matchXCoordinate = getRegularExpression(X_COORDINATE);
			String matchX01 = getRegularExpression(X_01);
			double x;
			//
			if(!isReversedX) {
				x = start + ((xTicks[count] - lower) / (upper - lower) * height);
			} else {
				x = ((start + height) - (((xTicks[count] - lower) / (upper - lower) * height)));
			}
			//
			for(String string : split) {
				if(Pattern.matches(matchXCoordinate, string)) {
					string = string.replace(X_COORDINATE, String.valueOf(x));
				} else if(Pattern.matches(matchX01, string)) {
					string = string.replace(X_01, String.valueOf(formatX.format(xTicks[count])));
				}
				builder.append(string);
				builder.append(LINE_DELIMITER);
			}
			builder.append(LINE_DELIMITER);
		}
		//
		return line.replaceAll(regexX, builder.toString());
	}

	private String getTickY(BaseChart baseChart, AxisSettings axisSettings, double yTicks[], StringBuilder tickY, DecimalFormat formatY, boolean isReversedY, String regexY, String line) {

		int ticklabel_size = yTicks.length;
		double start = 279.90709;
		double height = 200.0;
		StringBuilder builder = new StringBuilder("");
		double upper = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().upper;
		double lower = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().lower;
		//
		for(int count = 0; count < ticklabel_size; count++) {
			String split[] = tickY.toString().split(SPLIT_LINE_DELIMITER);
			String matchYCoordinate = getRegularExpression(Y_COORDINATE);
			String matchY01 = getRegularExpression(Y_01);
			double y;
			//
			if(!isReversedY) {
				y = start - (height - ((upper - yTicks[count]) / (upper - lower) * height));
			} else {
				y = ((start - height) + (height - ((upper - yTicks[count]) / (upper - lower) * height)));
			}
			//
			for(String string : split) {
				if(Pattern.matches(matchYCoordinate, string)) {
					string = string.replace(Y_COORDINATE, String.valueOf(y));
				} else if(Pattern.matches(matchY01, string)) {
					string = string.replace(Y_01, String.valueOf(formatY.format(yTicks[count])));
				}
				builder.append(string);
				builder.append(LINE_DELIMITER);
			}
			builder.append(LINE_DELIMITER);
		}
		//
		return line.replaceAll(regexY, builder.toString());
	}

	private String getLegend(ISeries<?>[] series, StringBuilder legend, String regex_legend, String line) {

		double start1 = 100.5815;
		double start2 = 102.06668;
		StringBuilder builder = new StringBuilder("");
		int count = 0;
		//
		for(ISeries<?> serie : series) {
			if(serie.isVisible()) {
				ILineSeries<?> lineSerie = (ILineSeries<?>)serie;
				Color color = lineSerie.getLineColor();
				String col = getColor(color);
				/*
				 * 6 taken just to keep appropriate distance between the Series names in the legend.
				 */
				double y1 = start1 + 6 * count;
				double y2 = start2 + 6 * count;
				String des = serie.getDescription();
				String split[] = legend.toString().split(SPLIT_LINE_DELIMITER);
				String matchY1Coordinate = getRegularExpression(Y1_COORDINATE);
				String matchY2Coordinate = getRegularExpression(Y2_COORDINATE);
				String matchColor = getRegularExpression(COLOR);
				String matchSeriesA = getRegularExpression(SERIES_A);
				//
				for(String string : split) {
					if(Pattern.matches(matchY1Coordinate, string)) {
						string = string.replace(Y1_COORDINATE, String.valueOf(y1));
					} else if(Pattern.matches(matchY2Coordinate, string)) {
						string = string.replace(Y2_COORDINATE, String.valueOf(y2));
					} else if(Pattern.matches(matchColor, string)) {
						string = string.replace(COLOR, col);
					} else if(Pattern.matches(matchSeriesA, string)) {
						string = string.replace(SERIES_A, des);
					}
					builder.append(string);
					builder.append(LINE_DELIMITER);
				}
				builder.append(LINE_DELIMITER);
				count++;
			}
		}
		//
		return line.replaceAll(regex_legend, builder.toString());
	}

	private String getAxisLabel(BaseChart baseChart, AxisSettings axisSettings, StringBuilder axisLabel, boolean isReversedX, boolean isReversedY, boolean isShowAxisZeroMarker, String axis_label, String line) {

		String label = "";
		//
		if(isShowAxisZeroMarker) {
			StringBuilder builder = new StringBuilder("");
			String split[] = axisLabel.toString().split(SPLIT_LINE_DELIMITER);
			String matchX1Coordinate = getRegularExpression(X1_COORDINATE);
			String matchX2Coordinate = getRegularExpression(X2_COORDINATE);
			String matchY1Coordinate = getRegularExpression(Y1_COORDINATE);
			String matchY2Coordinate = getRegularExpression(Y2_COORDINATE);
			double start1 = 20.306;
			double height1 = 307.216 - 20.306;
			double start2 = 279.90709;
			double height2 = 200.0;
			double upper = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().upper;
			double lower = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getRange().lower;
			double x1;
			if(!isReversedX) {
				x1 = start1 + ((0.0 - lower) / (upper - lower) * height1);
			} else {
				x1 = ((start1 + height1) - (((0.0 - lower) / (upper - lower) * height1)));
			}
			if(x1 >= start1 && x1 <= start1 + height1) {
				for(String string : split) {
					if(Pattern.matches(matchX1Coordinate, string)) {
						string = string.replace(X1_COORDINATE, String.valueOf(x1));
					}
					if(Pattern.matches(matchY1Coordinate, string)) {
						string = string.replace(Y1_COORDINATE, String.valueOf(start2));
					}
					if(Pattern.matches(matchX2Coordinate, string)) {
						string = string.replace(X2_COORDINATE, String.valueOf(x1));
					}
					if(Pattern.matches(matchY2Coordinate, string)) {
						string = string.replace(Y2_COORDINATE, String.valueOf(start2 - height2));
					}
					builder.append(string);
					builder.append(LINE_DELIMITER);
				}
			}
			builder.append(LINE_DELIMITER);
			double upper2 = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().upper;
			double lower2 = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().lower;
			double y1;
			//
			if(!isReversedY) {
				y1 = start2 - (height2 - ((upper2 - 0.0) / (upper2 - lower2) * height2));
			} else {
				y1 = ((start2 - height2) + (height2 - ((upper2 - 0.0) / (upper2 - lower2) * height2)));
			}
			//
			if(y1 <= start2 && y1 >= start2 - height2) {
				for(String string : split) {
					if(Pattern.matches(matchX1Coordinate, string)) {
						string = string.replace(X1_COORDINATE, String.valueOf(start1));
					}
					if(Pattern.matches(matchY1Coordinate, string)) {
						string = string.replace(Y1_COORDINATE, String.valueOf(y1));
					}
					if(Pattern.matches(matchX2Coordinate, string)) {
						string = string.replace(X2_COORDINATE, String.valueOf(start1 + height1));
					}
					if(Pattern.matches(matchY2Coordinate, string)) {
						string = string.replace(Y2_COORDINATE, String.valueOf(y1));
					}
					builder.append(string);
					builder.append(LINE_DELIMITER);
				}
			}
			label = line.replaceAll(axis_label, builder.toString());
		}
		//
		return label;
	}

	private String getDataSeries(BaseChart baseChart, ISeries<?>[] series, AxisSettings axisSettings, boolean isReversedX, boolean isReversedY, String data_series, String line) {

		StringBuilder builder = new StringBuilder("");
		int widthPlotArea = baseChart.getPlotArea().getSize().x;
		int heightPlotArea = baseChart.getPlotArea().getSize().y;
		IAxisSet axisSet = baseChart.getAxisSet();
		int index = 0;
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				ILineSeries<?> lineSeries = (ILineSeries<?>)dataSeries;
				LineStyle lineStyle = lineSeries.getLineStyle();
				StringBuilder string = null;
				if(lineStyle != LineStyle.NONE) {
					string = printLineData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, axisSet, isReversedX, isReversedY);
				} else {
					string = printScatterData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, axisSet, isReversedX, isReversedY);
				}
				builder.append(string);
			}
		}
		//
		return line.replaceAll(data_series, builder.toString());
	}

	/*
	 * returns the data series to be replaced in the data series in template
	 */
	private StringBuilder printLineData(ISeries<?> dataSeries, int widthPlotArea, int heightPlotArea, AxisSettings axisSettings, int index, IAxisSet axisSet, boolean isReversedX, boolean isReversedY) {

		StringBuilder builder = new StringBuilder("");
		StringBuilder data = new StringBuilder(HEADER_DATA);
		ILineSeries<?> lineSeries = (ILineSeries<?>)dataSeries;
		Color lineColor = lineSeries.getLineColor();
		String color = getColor(lineColor);
		int indexAxisX = axisSettings.getIndexAxisX();
		int indexAxisY = axisSettings.getIndexAxisY();
		IAxisScaleConverter axisScaleConverterX = axisSettings.getAxisScaleConverterX();
		IAxisScaleConverter axisScaleConverterY = axisSettings.getAxisScaleConverterY();
		//
		double[] xSeries = dataSeries.getXSeries();
		double[] ySeries = dataSeries.getYSeries();
		String split[] = data.toString().split(SPLIT_LINE_DELIMITER);
		int size = dataSeries.getXSeries().length;
		//
		String match1 = getRegularExpression(COLOR);
		String match2 = getRegularExpression(DATA_POINTS);
		for(String string : split) {
			if(Pattern.matches(match1, string)) {
				string = string.replace(COLOR, color);
			} else if(Pattern.matches(match2, string)) {
				StringBuilder rep = new StringBuilder("");
				for(int i = 0; i < size; i++) {
					/*
					 * Only export if the data point is visible.
					 */
					Point point = dataSeries.getPixelCoordinates(i);
					if((point.x >= 0 && point.x <= widthPlotArea) && (point.y >= 0 && point.y <= heightPlotArea)) {
						rep.append(printValueLinePlot(AXIS_X, index, xSeries[i], indexAxisX, axisSet, BaseChart.ID_PRIMARY_X_AXIS, axisScaleConverterX, isReversedX, isReversedY));
						rep.append(",");
						rep.append(printValueLinePlot(AXIS_Y, index, ySeries[i], indexAxisY, axisSet, BaseChart.ID_PRIMARY_Y_AXIS, axisScaleConverterY, isReversedX, isReversedY));
						rep.append(" ");
					}
				}
				string = string.replace(DATA_POINTS, rep);
			}
			builder.append(string);
			builder.append(LINE_DELIMITER);
		}
		return builder;
	}

	/*
	 * returns value scaled to the appropriate coordinates in SVG
	 */
	private String printValueLinePlot(String axis, int index, double value, int indexAxis, IAxisSet axisSet, int indexPrimaryAxis, IAxisScaleConverter axisScaleConverter, boolean isReversedX, boolean isReversedY) {

		String line = "";
		double x = 255.5 - 23.5;
		double y = 263.5 - 80.5;
		if(indexAxis == indexPrimaryAxis || axisScaleConverter == null) {
			if(axis.equals(AXIS_X)) {
				IAxis xAxis = axisSet.getXAxis(indexAxis);
				double xUpper = xAxis.getRange().upper;
				double xLower = xAxis.getRange().lower;
				double x1;
				if(!isReversedX) {
					x1 = 23.5 + ((value - xLower) / (xUpper - xLower) * x);
				} else {
					x1 = ((23.5 + x) - ((value - xLower) / (xUpper - xLower) * x));
				}
				line = String.valueOf(x1);
			} else if(axis.equals(AXIS_Y)) {
				IAxis yAxis = axisSet.getYAxis(indexAxis);
				double yUpper = yAxis.getRange().upper;
				double yLower = yAxis.getRange().lower;
				double y1;
				if(!isReversedY) {
					y1 = 263.5 - (y - ((yUpper - value) / (yUpper - yLower) * y));
				} else {
					y1 = ((263.5 - y) + (y - ((yUpper - value) / (yUpper - yLower) * y)));
				}
				line = String.valueOf(y1);
			}
		} else {
			if(axisScaleConverter != null) {
				if(axis.equals(AXIS_X)) {
					IAxis xAxis = axisSet.getXAxis(indexAxis);
					double xUpper = xAxis.getRange().upper;
					double xLower = xAxis.getRange().lower;
					double x1;
					if(!isReversedX) {
						x1 = 23.5 + ((axisScaleConverter.convertToSecondaryUnit(value) - xLower) / (xUpper - xLower) * x);
					} else {
						x1 = ((23.5 + x) - ((axisScaleConverter.convertToSecondaryUnit(value) - xLower) / (xUpper - xLower) * x));
					}
					line = String.valueOf(x1);
				} else if(axis.equals(AXIS_Y)) {
					IAxis yAxis = axisSet.getYAxis(indexAxis);
					double yUpper = yAxis.getRange().upper;
					double yLower = yAxis.getRange().lower;
					double y1;
					if(!isReversedY) {
						y1 = 80.5 + ((yUpper - axisScaleConverter.convertToSecondaryUnit(value)) / (yUpper - yLower) * y);
					} else {
						y1 = ((263.5 - y) + ((yUpper - axisScaleConverter.convertToSecondaryUnit(value)) / (yUpper - yLower) * y));
					}
					line = String.valueOf(y1);
				}
			}
		}
		return line;
	}
}