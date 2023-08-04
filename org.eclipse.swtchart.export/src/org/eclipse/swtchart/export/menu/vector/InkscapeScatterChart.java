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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.export.core.AxisSettings;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IAxisSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public class InkscapeScatterChart extends AbstractInkscapeLineChart {

	private static final String TEMPLATE_SCATTER_CHART = "Template_ScatterChart.svg";

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
		//
		BaseChart baseChart = scrollableChart.getBaseChart();
		boolean isShowAxisZeroMarker = baseChart.getChartSettings().isShowAxisZeroMarker();
		ISeries<?>[] series = baseChart.getSeriesSet().getSeries();
		String template = TEMPLATE_SCATTER_CHART;
		//
		try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(template)))) {
			double xTicks[] = baseChart.getAxisSet().getXAxis(axisSettings.getIndexAxisX()).getTick().getTickLabelValues();
			double yTicks[] = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getTick().getTickLabelValues();
			String line;
			StringBuilder tickX = new StringBuilder("<path\n" + "                       inkscape:connector-curvature=\"0\"\n" + "                       id=\"path888\"\n" + "                       d=\"m %x-coordinate%,279.77439 v 5.05309\"\n" + "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" + "                    <text\n" + "                       id=\"text892\"\n" + "                       y=\"289.01782\"\n" + "                       x=\"%x-coordinate%\"\n" + "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                       xml:space=\"preserve\"><tspan\n" + "                         id=\"tspan890\"\n" + "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:center;text-anchor:middle;stroke-width:0.26458332\"\n" + "                         y=\"289.01782\"\n" + "                         x=\"%x-coordinate%\"\n" + "                         sodipodi:role=\"line\">%X01%</tspan></text>");
			StringBuilder tickY = new StringBuilder("<path\n" + "                       inkscape:connector-curvature=\"0\"\n" + "                       id=\"path1299\"\n" + "                       d=\"m 15.387913,%y-coordinate% h 5.05309\"\n" + "                       style=\"fill:none;stroke:#000000;stroke-width:0.26458332px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" />\n" + "                    <text\n" + "                       xml:space=\"preserve\"\n" + "                       style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:arial;-inkscape-font-specification:arial;letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                       x=\"14.211229\"\n" + "                       y=\"%y-coordinate%\"\n" + "                       id=\"text1303\"><tspan\n" + "                         sodipodi:role=\"line\"\n" + "                         id=\"tspan1301\"\n" + "                         x=\"14.211229\"\n" + "                         y=\"%y-coordinate%\"\n" + "                         style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:3.52777767px;font-family:arial;-inkscape-font-specification:arial;text-align:end;text-anchor:end;stroke-width:0.26458332\">%Y01%</tspan></text>");
			StringBuilder legend = new StringBuilder("<path\n" + "                   style=\"fill:url(#linearGradient3662);fill-opacity:1;stroke:%COLOR%;stroke-width:0.5;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n" + "                   d=\"m 209.71446,%y1-coordinate% h 20.93268\"\n" + "                   id=\"path1744\"\n" + "                   inkscape:connector-curvature=\"0\" />\n" + "                <text\n" + "                   xml:space=\"preserve\"\n" + "                   style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:6.3499999px;line-height:1.25;font-family:'Bold Oblique';-inkscape-font-specification:'Bold Oblique, ';letter-spacing:0px;word-spacing:0px;fill:url(#linearGradient4353);fill-opacity:1;stroke:none;stroke-width:0.26458332\"\n" + "                   x=\"230.91121\"\n" + "                   y=\"%y2-coordinate%\"\n" + "                   id=\"text1748\"><tspan\n" + "                     sodipodi:role=\"line\"\n" + "                     id=\"tspan1746\"\n" + "                     x=\"230.91121\"\n" + "                     y=\"%y2-coordinate%\"\n" + "                     style=\"font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;font-size:4.23333311px;font-family:Arial;-inkscape-font-specification:Arial;fill:url(#linearGradient4353);fill-opacity:1;stroke-width:0.26458332\">%SERIES A%</tspan></text>");
			StringBuilder axisLabel = new StringBuilder("<path\n" + "         style=\"fill:none;stroke:#000000;stroke-width:0.26499999;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;stroke-miterlimit:4;stroke-dasharray:0.52999998,0.52999998;stroke-dashoffset:0;opacity:1\"\n" + "         d=\"M %x1-coordinate%,%y1-coordinate% L %x2-coordinate%,%y2-coordinate%\"\n" + "         id=\"path850\"\n" + "         inkscape:connector-curvature=\"0\" />");
			String regexX = ".*%X-AXIS_TICKS%.*";
			String regexY = ".*%Y-AXIS_TICKS%.*";
			String titleX = ".*%PLACEHOLDER X-AXIS%.*";
			String titleY = ".*%PLACEHOLDER Y-AXIS%.*";
			String data_series = ".*%DATA SERIES%.*";
			String regex_legend = ".*%LEGEND%.*";
			String axis_label = ".*%AXIS LABELS%.*";
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
						double x;
						if(!isReversedX) {
							x = start + ((xTicks[count] - lower) / (upper - lower) * height);
						} else {
							x = ((start + height) - (((xTicks[count] - lower) / (upper - lower) * height)));
						}
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
						double y;
						if(!isReversedY) {
							y = start - (height - ((upper - yTicks[count]) / (upper - lower) * height));
						} else {
							y = ((start - height) + (height - ((upper - yTicks[count]) / (upper - lower) * height)));
						}
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
					StringBuilder out = new StringBuilder("");
					int count = 0;
					for(ISeries<?> serie : series) {
						if(serie.isVisible()) {
							ILineSeries<?> lineSerie = (ILineSeries<?>)serie;
							Color color = lineSerie.getSymbolColor();
							String col = getColor(color);
							/*
							 * 6 taken just to keep appropriate distance between the Series names in the legend.
							 */
							double y1 = start1 + 6 * count;
							double y2 = start2 + 6 * count;
							String des = serie.getDescription();
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
							count++;
						}
					}
					line = line.replaceAll(regex_legend, out.toString());
				} else if(Pattern.matches(axis_label, line)) {
					if(isShowAxisZeroMarker) {
						StringBuilder out = new StringBuilder("");
						String split[] = axisLabel.toString().split("\\n");
						String match1 = ".*%x1-coordinate%.*";
						String match2 = ".*%x2-coordinate%.*";
						String match3 = ".*%y1-coordinate%.*";
						String match4 = ".*%y2-coordinate%.*";
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
								if(Pattern.matches(match1, string)) {
									string = string.replace("%x1-coordinate%", String.valueOf(x1));
								}
								if(Pattern.matches(match3, string)) {
									string = string.replace("%y1-coordinate%", String.valueOf(start2));
								}
								if(Pattern.matches(match2, string)) {
									string = string.replace("%x2-coordinate%", String.valueOf(x1));
								}
								if(Pattern.matches(match4, string)) {
									string = string.replace("%y2-coordinate%", String.valueOf(start2 - height2));
								}
								out.append(string);
								out.append("\n");
							}
						}
						out.append("\n");
						double upper2 = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().upper;
						double lower2 = baseChart.getAxisSet().getYAxis(axisSettings.getIndexAxisY()).getRange().lower;
						double y1;
						if(!isReversedY) {
							y1 = start2 - (height2 - ((upper2 - 0.0) / (upper2 - lower2) * height2));
						} else {
							y1 = ((start2 - height2) + (height2 - ((upper2 - 0.0) / (upper2 - lower2) * height2)));
						}
						if(y1 <= start2 && y1 >= start2 - height2) {
							for(String string : split) {
								if(Pattern.matches(match1, string)) {
									string = string.replace("%x1-coordinate%", String.valueOf(start1));
								}
								if(Pattern.matches(match3, string)) {
									string = string.replace("%y1-coordinate%", String.valueOf(y1));
								}
								if(Pattern.matches(match2, string)) {
									string = string.replace("%x2-coordinate%", String.valueOf(start1 + height1));
								}
								if(Pattern.matches(match4, string)) {
									string = string.replace("%y2-coordinate%", String.valueOf(y1));
								}
								out.append(string);
								out.append("\n");
							}
						}
						line = line.replaceAll(axis_label, out.toString());
					}
				} else if(Pattern.matches(data_series, line)) {
					StringBuilder out = new StringBuilder("");
					int widthPlotArea = baseChart.getPlotArea().getSize().x;
					int heightPlotArea = baseChart.getPlotArea().getSize().y;
					IAxisSet axisSet = baseChart.getAxisSet();
					int index = 0;
					for(ISeries<?> dataSeries : series) {
						if(dataSeries != null && dataSeries.isVisible()) {
							StringBuilder string = printScatterData(dataSeries, widthPlotArea, heightPlotArea, axisSettings, index++, axisSet, isReversedX, isReversedY);
							out.append(string);
						}
					}
					line = line.replaceAll(data_series, out.toString());
				}
				builder.append(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		//
		return builder.toString();
	}
}