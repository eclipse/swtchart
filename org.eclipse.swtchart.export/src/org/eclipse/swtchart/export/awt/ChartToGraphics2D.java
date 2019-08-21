/*******************************************************************************
 * Copyright (c) 2019 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Sanatt Abrol - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.export.awt;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.internal.axis.AxisTickMarks;
import org.eclipse.swtchart.internal.compress.CompressScatterSeries;
import org.eclipse.swtchart.internal.series.BarSeries;
import org.eclipse.swtchart.internal.series.LineSeries;

@SuppressWarnings("rawtypes")
public class ChartToGraphics2D {

	/** The chart object to be converted to SVG */
	private Chart chart;
	/** The X Axis */
	private Axis xAxis;
	/** The Y Axis */
	private Axis yAxis;
	/** The java.awt.Graphics2D object */
	private Graphics2D graphics2D;
	/** The height of the chart area */
	private int height;
	/** The width of the chart area */
	private int width;
	/** The border on X Axis */
	private int xBorder;
	/** the border on Y Axis */
	private int yBorder;

	/**
	 * Constructor
	 * 
	 * @param chart
	 *            the org.eclipse.swtchart.Chart object to be converted to java.awt.Graphics2D
	 * @param indexAxisX
	 *            the index of X Axis as selected by user
	 * @param indexAxisY
	 *            the index of Y Axis as selected by user
	 * @param g2d
	 *            the java.awt.Graphics2D object to be created
	 */
	public ChartToGraphics2D(Chart chart, int indexAxisX, int indexAxisY, Graphics2D g2d) {
		this.chart = chart;
		this.graphics2D = g2d;
		this.xAxis = (Axis)chart.getAxisSet().getXAxis(indexAxisX);
		this.yAxis = (Axis)chart.getAxisSet().getYAxis(indexAxisY);
		this.width = chart.getSize().x;
		this.height = chart.getSize().y;
		this.xBorder = 50;
		this.yBorder = 50;
		init();
	}

	/**
	 * Return the jawa.awt.Graphics2D object
	 */
	public Graphics2D getGraphics2D() {

		return this.graphics2D;
	}

	/**
	 * Initialize the conversion
	 */
	private void init() {

		/**
		 * Translate coordinate system to (50,0) to accommodate space for drawing Y axis
		 * title
		 */
		graphics2D.translate(50, 0);
		graphics2D.setClip(new java.awt.Rectangle(xBorder, yBorder, width - 2 * xBorder, height - 2 * yBorder));
		ISeries[] series = chart.getSeriesSet().getSeries();
		for(ISeries dataSeries : series) {
			if(dataSeries != null) {
				if(dataSeries.getType() == SeriesType.LINE) {
					drawLineSeries(dataSeries);
				} else if(dataSeries.getType() == SeriesType.BAR) {
					drawBarSeries(dataSeries);
				} else {
					return;
				}
			}
		}
		graphics2D.setClip(null);
		graphics2D.setColor(java.awt.Color.BLACK);
		/** Draw X Axis */
		int x11 = (int)(xBorder);
		int y11 = (int)(height - yBorder);
		int x12 = (int)(width - xBorder);
		int y12 = (int)(height - yBorder);
		graphics2D.drawLine(x11, y11, x12, y12);
		/** Draw Y Axis */
		int x21 = (int)(xBorder);
		int y21 = (int)(yBorder);
		int x22 = (int)(xBorder);
		int y22 = (int)(height - yBorder);
		graphics2D.drawLine(x21, y21, x22, y22);
		/** Draw Grids and Ticks on X axis and Y axis */
		drawGridAndTicks(xAxis);
		drawGridAndTicks(yAxis);
		/** Draw Tick Labels on X axis and Y axis */
		drawTickLabelsOnXAxis();
		drawTickLabelsOnYAxis();
		/** Translate everything back to (0,0) */
		graphics2D.translate(0, 0);
		/** Draw the X and Y axis labels */
		String labelX = xAxis.getTitle().getText();
		String labelY = yAxis.getTitle().getText();
		graphics2D.setFont(SwtToAwtUtils.toAwtFont(chart.getDisplay(), chart.getAxisSet().getAxes()[0].getTitle().getFont()));
		AffineTransform affineTransform = new AffineTransform();
		FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, true, true);
		java.awt.Font font = graphics2D.getFont();
		int textwidth = (int)(font.getStringBounds(labelX, fontRenderContext).getWidth());
		int textheight = (int)(font.getStringBounds(labelX, fontRenderContext).getHeight());
		int x1 = (int)(width / 2 - textwidth / 2);
		int y1 = (int)(height - textheight / 2 + AxisTickMarks.TICK_LENGTH);
		/** Draw X Axis label */
		graphics2D.drawString(labelX, x1, y1);
		affineTransform.rotate(Math.toRadians(-90), 0, 0);
		java.awt.Font rotatedFont = font.deriveFont(affineTransform);
		graphics2D.setFont(rotatedFont);
		int textwidthY = (int)(font.getStringBounds(labelY, fontRenderContext).getWidth());
		/** Draw Y Axis label */
		graphics2D.drawString(labelY, -5, height / 2 + textwidthY / 2);
	}

	/**
	 * Draw the bar series
	 */
	private void drawBarSeries(ISeries dataSeries) {

		BarSeries barSeries = (BarSeries)dataSeries;
		graphics2D.setColor(SwtToAwtUtils.toAwtColor(barSeries.getBarColor()));
		Rectangle[] rs = barSeries.getBoundsForCompressedSeries();
		for(int i = 0; i < rs.length; i++) {
			graphics2D.fillRect(rs[i].x + xBorder, rs[i].y + yBorder, rs[i].width, rs[i].height);
			graphics2D.drawRect(rs[i].x + xBorder, rs[i].y + yBorder, rs[i].width, rs[i].height);
		}
	}

	/**
	 * Draw the line and scatter series
	 */
	private void drawLineSeries(ISeries dataSeries) {

		LineSeries lineSeries = (LineSeries)dataSeries;
		// draw line series
		if(lineSeries.getLineStyle() != LineStyle.NONE) {
			graphics2D.setColor(SwtToAwtUtils.toAwtColor(lineSeries.getLineColor()));
			double[] xSeries = dataSeries.getXSeries();
			double[] ySeries = dataSeries.getYSeries();
			double xMin = Arrays.stream(xSeries).min().getAsDouble();
			double xMax = Arrays.stream(xSeries).max().getAsDouble();
			double yMin = Arrays.stream(ySeries).min().getAsDouble();
			double yMax = Arrays.stream(ySeries).max().getAsDouble();
			double xDenumerator = xMax - xMin;
			double yDenumerator = yMax - yMin;
			if(xMax > 0 && yMax > 0) {
				double factorX = (width - 2 * xBorder) / xDenumerator;
				double factorY = (height - 2 * yBorder) / yDenumerator;
				/*
				 * Draw the line series
				 */
				int nPoints = xSeries.length - 1;
				for(int i = 0; i < nPoints; i++) {
					int x1 = (int)((factorX * (xSeries[i] - xMin)) + xBorder);
					int y1 = (int)((height - factorY * (ySeries[i] - yMin)) - yBorder);
					int x2 = (int)((factorX * (xSeries[i + 1] - xMin)) + xBorder);
					int y2 = (int)((height - factorY * (ySeries[i + 1] - yMin)) - yBorder);
					graphics2D.drawLine(x1, y1, x2, y2);
				}
			}
			return;
		}
		// draw scatter plot
		if(lineSeries.getSymbolType() != PlotSymbolType.NONE) {
			PlotSymbolType symbolType = lineSeries.getSymbolType();
			int symbolSize = lineSeries.getSymbolSize();
			java.awt.Color symbolColor = SwtToAwtUtils.toAwtColor(lineSeries.getSymbolColor());
			CompressScatterSeries compressor = new CompressScatterSeries();
			compressor.setXSeries(dataSeries.getXSeries());
			compressor.setYSeries(dataSeries.getYSeries());
			// get x and y compressed series
			double[] xseries = compressor.getCompressedXSeries();
			double[] yseries = compressor.getCompressedYSeries();
			for(int i = 0; i < xseries.length; i++) {
				int h, v;
				if(xAxis.isHorizontalAxis()) {
					h = xAxis.getPixelCoordinate(xseries[i]) + xBorder;
					v = yAxis.getPixelCoordinate(yseries[i]) + yBorder;
				} else {
					v = xAxis.getPixelCoordinate(xseries[i]) + xBorder;
					h = yAxis.getPixelCoordinate(yseries[i]) + yBorder;
				}
				graphics2D.setColor(symbolColor);
				// draw the series symbol
				switch(symbolType) {
					case CIRCLE:
						graphics2D.fillOval(h - symbolSize, v - symbolSize, symbolSize * 2, symbolSize * 2);
						break;
					case SQUARE:
						graphics2D.fillRect(h - symbolSize, v - symbolSize, symbolSize * 2, symbolSize * 2);
						break;
					case TRIANGLE:
						int[] triangleXPoints = {h, h + symbolSize, h - symbolSize};
						int[] triangleYPoints = {v - symbolSize, v + symbolSize, v + symbolSize};
						graphics2D.fillPolygon(triangleXPoints, triangleYPoints, 3);
						break;
					case INVERTED_TRIANGLE:
						int[] invertedTriangleXPoints = {h, h + symbolSize, h - symbolSize};
						int[] invertedTriangleYPoints = {v + symbolSize, v - symbolSize, v - symbolSize};
						graphics2D.fillPolygon(invertedTriangleXPoints, invertedTriangleYPoints, 3);
						break;
					case CROSS:
						graphics2D.drawLine(h - symbolSize, v - symbolSize, h + symbolSize, v + symbolSize);
						graphics2D.drawLine(h - symbolSize, v + symbolSize, h + symbolSize, v - symbolSize);
						break;
					case PLUS:
						graphics2D.drawLine(h, v - symbolSize, h, v + symbolSize);
						graphics2D.drawLine(h - symbolSize, v, h + symbolSize, v);
						break;
					case DIAMOND:
						int[] diamondXPoints = {h, h + symbolSize, h, h - symbolSize};
						int[] diamondYPoints = {v - symbolSize, v, v + symbolSize, v};
						graphics2D.fillPolygon(diamondXPoints, diamondYPoints, 4);
						break;
					case NONE:
					default:
						break;
				}
			}
		}
	}

	/**
	 * Draw X axis tick-labels
	 */
	private void drawTickLabelsOnXAxis() {

		int offset = xAxis.getTick().getAxisTickMarks().getBounds().x;
		graphics2D.setFont(SwtToAwtUtils.toAwtFont(chart.getDisplay(), xAxis.getTick().getFont()));
		AffineTransform affineTransform = new AffineTransform();
		FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, true, true);
		java.awt.Font font = graphics2D.getFont();
		int angle = xAxis.getTick().getTickLabelAngle();
		ArrayList<Integer> tickLabelPositions = xAxis.getTick().getAxisTickLabels().getTickLabelPositions();
		ArrayList<Boolean> tickVisibilities = xAxis.getTick().getAxisTickLabels().getTickVisibilities();
		ArrayList<String> tickLabels = xAxis.getTick().getAxisTickLabels().getTickLabels();
		Rectangle bounds = xAxis.getTick().getAxisTickLabels().getBounds();
		Position position = xAxis.getPosition();
		/** Draw Tick Labels */
		for(int i = 0; i < tickLabelPositions.size(); i++) {
			if(xAxis.isValidCategoryAxis() || tickVisibilities.get(i) == true) {
				String text = tickLabels.get(i);
				int textWidth = (int)(font.getStringBounds(text, fontRenderContext).getWidth());
				int textHeight = (int)(font.getStringBounds(text, fontRenderContext).getHeight());
				if(angle == 0) {
					int x = (int)(tickLabelPositions.get(i) - textWidth / 2d + offset);
					graphics2D.drawString(text, bounds.x + x - xBorder, bounds.y + yBorder / 2 + AxisTickMarks.TICK_LENGTH);
					continue;
				}
				float x, y;
				if(position == Position.Primary) {
					x = (float)(offset + bounds.x + tickLabelPositions.get(i) - textWidth * Math.cos(Math.toRadians(angle)) - textHeight / 2d * Math.sin(Math.toRadians(angle)));
					y = (float)(bounds.y + textWidth * Math.sin(Math.toRadians(angle)));
				} else {
					x = (float)(offset + bounds.x + tickLabelPositions.get(i) - textHeight / 2d * Math.sin(Math.toRadians(angle)));
					y = (float)(bounds.y + bounds.height * Math.sin(Math.toRadians(angle)));
				}
				drawRotatedText(graphics2D, text, x - xBorder, y + yBorder / 2 + AxisTickMarks.TICK_LENGTH, angle);
			}
		}
	}

	/**
	 * Draw Y axis tick-labels
	 */
	private void drawTickLabelsOnYAxis() {

		graphics2D.setFont(SwtToAwtUtils.toAwtFont(chart.getDisplay(), yAxis.getTick().getFont()));
		AffineTransform affineTransform = new AffineTransform();
		FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, true, true);
		java.awt.Font font = graphics2D.getFont();
		int figureHeight = (int)(font.getStringBounds("dummy", fontRenderContext).getHeight());
		ArrayList<Integer> tickLabelPositions = yAxis.getTick().getAxisTickLabels().getTickLabelPositions();
		ArrayList<Boolean> tickVisibilities = yAxis.getTick().getAxisTickLabels().getTickVisibilities();
		ArrayList<String> tickLabels = yAxis.getTick().getAxisTickLabels().getTickLabels();
		Rectangle bounds = yAxis.getTick().getAxisTickLabels().getBounds();
		/** Draw Tick Labels */
		for(int i = 0; i < tickLabelPositions.size(); i++) {
			if(tickVisibilities.size() == 0 || tickLabels.size() == 0) {
				break;
			}
			if(tickVisibilities.get(i) == true) {
				String text = tickLabels.get(i);
				int x = Axis.MARGIN;
				if(tickLabels.get(0).startsWith("-") && !text.startsWith("-")) {
					x += (int)(font.getStringBounds("-", fontRenderContext).getWidth());
				}
				int y = (int)(bounds.height - 1 - tickLabelPositions.get(i) - figureHeight / 2.0);
				graphics2D.drawString(text, bounds.x + x - xBorder, bounds.y + y + yBorder / 2);
			}
		}
	}

	/**
	 * Draw a rotated text with a certain angle
	 */
	private void drawRotatedText(Graphics2D g2d, String text, float x, float y, int angle) {

		AffineTransform original = g2d.getTransform();
		g2d.rotate(Math.PI * (angle / 180));
		g2d.drawString(text, x, y);
		g2d.setTransform(original);
	}

	/**
	 * Draw a grid on the given axis
	 */
	private void drawGridAndTicks(Axis axis) {

		int xWidth;
		if(axis.isHorizontalAxis()) {
			xWidth = width - 2 * xBorder;
		} else {
			xWidth = height - 2 * yBorder;
		}
		ArrayList<Integer> tickLabelPosition = axis.getTick().getAxisTickLabels().getTickLabelPositions();
		if(axis.isValidCategoryAxis()) {
			int step = 0;
			if(tickLabelPosition.size() > 1) {
				step = tickLabelPosition.get(1).intValue() - tickLabelPosition.get(0).intValue();
			} else {
				step = xWidth;
			}
			int x = (int)(tickLabelPosition.get(0).intValue() - step / 2d);
			for(int i = 0; i < tickLabelPosition.size() + 1; i++) {
				x += step;
				if(x >= xWidth) {
					continue;
				}
				if(axis.isHorizontalAxis()) {
					int x1 = x + xBorder;
					int y1 = yBorder;
					int x2 = x1;
					int y2 = height - yBorder;
					drawDashedLine(x1, y1, x2, y2);
					// draw the tick
					graphics2D.drawLine(x1, y2, x2, y2 + AxisTickMarks.TICK_LENGTH);
				} else {
					int x1 = xBorder;
					int y1 = height - yBorder - x;
					int x2 = width - xBorder;
					int y2 = y1;
					drawDashedLine(x1, y1, x2, y2);
					// draw the tick
					graphics2D.drawLine(xBorder - AxisTickMarks.TICK_LENGTH, y1, xBorder, y2);
				}
			}
		} else {
			for(int i = 0; i < tickLabelPosition.size(); i++) {
				int x = tickLabelPosition.get(i).intValue();
				if(x >= xWidth) {
					continue;
				}
				if(axis.isHorizontalAxis()) {
					int x1 = x + xBorder;
					int y1 = yBorder;
					int x2 = x1;
					int y2 = height - yBorder;
					drawDashedLine(x1, y1, x2, y2);
					// draw the tick
					graphics2D.drawLine(x1, height - yBorder, x2, height - yBorder + AxisTickMarks.TICK_LENGTH);
				} else {
					int x1 = xBorder;
					int y1 = height - yBorder - x;
					int x2 = width - xBorder;
					int y2 = y1;
					drawDashedLine(x1, y1, x2, y2);
					// draw the tick
					graphics2D.drawLine(xBorder - AxisTickMarks.TICK_LENGTH, y1, xBorder, y2);
				}
			}
		}
	}

	/**
	 * Method to draw dashed line
	 */
	private void drawDashedLine(int x1, int y1, int x2, int y2) {

		Graphics2D g2d = (Graphics2D)graphics2D.create();
		g2d.setColor(java.awt.Color.GRAY);
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{2, 4}, 0);
		g2d.setStroke(dashed);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.dispose();
	}
}