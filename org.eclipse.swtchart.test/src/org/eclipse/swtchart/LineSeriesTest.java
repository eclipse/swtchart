/*******************************************************************************
 * Copyright (c) 2008, 2020 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.util.ChartTestCase;
import org.junit.Test;

/**
 * Test case for line series.
 */
public class LineSeriesTest extends ChartTestCase {

	private ISeriesSet seriesSet;
	private static final double[] xSeries1 = {1, 2, 3, 4, 5};
	private static final double[] xSeries2 = {3, 1, 4, 8, 2};
	private static final double[] ySeries1 = {0.1, 0.2, 0.3, 0.4, 0.5};
	private static final double[] ySeries2 = {0.5, 0.4, 0.3, 0.2, 0.1};
	private static final double[] ySeries3 = {-0.1, -0.2, -0.3, -0.4, -0.5};
	private static final String[] categorySeries = {"a", "b", "c", "d", "e"};

	@Override
	public void setUp() throws Exception {

		super.setUp();
		seriesSet = chart.getSeriesSet();
	}

	/**
	 * Test for series type.
	 */
	@Test
	public void testType() throws Exception {

		ISeries<?> series = seriesSet.createSeries(SeriesType.LINE, "series");
		series.setYSeries(ySeries1);
		assertEquals(SeriesType.LINE, series.getType());
	}

	/**
	 * Test for visibility.
	 */
	@Test
	public void testVisibility() throws Exception {

		ISeries<?> series = seriesSet.createSeries(SeriesType.LINE, "series1");
		series.setYSeries(ySeries1);
		chart.getAxisSet().adjustRange();
		showChart();
		series.setVisible(false);
		assertFalse(series.isVisible());
		showChart();
	}

	/**
	 * Test for stack.
	 */
	@Test
	public void testStack() throws Exception {

		ISeries<?> series1 = seriesSet.createSeries(SeriesType.LINE, "series1");
		series1.setYSeries(ySeries1);
		ISeries<?> series2 = seriesSet.createSeries(SeriesType.LINE, "series2");
		series2.setYSeries(ySeries2);
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		xAxis.setCategorySeries(categorySeries);
		chart.getAxisSet().adjustRange();
		// non-category (cannot be stacked)
		series1.enableStack(true);
		series2.enableStack(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + category
		xAxis.enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + category
		chart.setOrientation(SWT.VERTICAL);
		showChart();
	}

	/**
	 * Test for series.
	 */
	@Test
	public void testSeries() {

		// set null
		ISeries<?> series = seriesSet.createSeries(SeriesType.LINE, "series");
		try {
			series.setXSeries(null);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		try {
			series.setYSeries(null);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		// get series before setting series
		double[] xSeries = series.getXSeries();
		assertEquals(0, xSeries.length);
		double[] ySeries = series.getYSeries();
		assertEquals(0, ySeries.length);
		// set empty series
		series.setXSeries(new double[0]);
		xSeries = series.getXSeries();
		assertEquals(0, xSeries.length);
		series.setYSeries(new double[0]);
		ySeries = series.getYSeries();
		assertEquals(0, ySeries.length);
		// set series
		series.setXSeries(xSeries1);
		xSeries = series.getXSeries();
		assertEquals(xSeries1.length, xSeries.length);
		for(int i = 0; i < xSeries.length; i++) {
			assertEquals(xSeries1[i], xSeries[i], 0.01);
		}
		series.setYSeries(ySeries1);
		ySeries = series.getYSeries();
		assertEquals(ySeries1.length, ySeries.length);
		for(int i = 0; i < ySeries.length; i++) {
			assertEquals(ySeries1[i], ySeries[i], 0.01);
		}
	}

	/**
	 * Test for axis id.
	 */
	@Test
	public void testAxisId() {

		ISeries<?> series = seriesSet.createSeries(SeriesType.LINE, "series");
		series.setXSeries(xSeries1);
		chart.getAxisSet().createXAxis();
		chart.getAxisSet().createYAxis();
		series.setXAxisId(1);
		series.setYAxisId(1);
		assertEquals(1, series.getXAxisId());
		assertEquals(1, series.getYAxisId());
	}

	/**
	 * Test for pixel coordinates.
	 */
	@Test
	public void testPixelCoordinates() throws Exception {

		ILineSeries<?> series1 = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series 1");
		series1.setXSeries(xSeries1);
		series1.setYSeries(ySeries1);
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		IAxis yAxis = chart.getAxisSet().getYAxis(0);
		xAxis.setRange(new Range(1, 5));
		yAxis.setRange(new Range(0.1, 0.5));
		Point r = chart.getPlotArea().getSize();
		// horizontal
		Point p = series1.getPixelCoordinates(1);
		assertEquals(r.x / 4d, p.x, 1);
		assertEquals(r.y / 4d * 3, p.y, 1);
		// horizontal + category
		xAxis.setCategorySeries(categorySeries);
		xAxis.enableCategory(true);
		xAxis.adjustRange();
		p = series1.getPixelCoordinates(1);
		assertEquals(r.x / 10d * 3, p.x, 1);
		assertEquals(r.y / 4d * 3, p.y, 1);
		// horizontal + stack
		ILineSeries<?> series2 = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series 2");
		series2.setYSeries(ySeries2);
		series1.enableStack(true);
		series2.enableStack(true);
		xAxis.setRange(new Range(0, 4));
		yAxis.setRange(new Range(0, 0.7));
		p = series2.getPixelCoordinates(1);
		assertEquals(r.x / 10d * 3, p.x, 1);
		assertEquals(r.y / 7d, p.y, 1);
		// vertical
		chart.setOrientation(SWT.VERTICAL);
		xAxis.enableCategory(false);
		series1.enableStack(false);
		series2.enableStack(false);
		xAxis.setRange(new Range(1, 5));
		yAxis.setRange(new Range(0.1, 0.5));
		r = chart.getPlotArea().getSize();
		p = series1.getPixelCoordinates(1);
		assertEquals(r.x / 4d, p.x, 1);
		assertEquals(r.y / 4d * 3, p.y, 1);
		// vertical + category
		xAxis.enableCategory(true);
		xAxis.setRange(new Range(0, 4));
		yAxis.setRange(new Range(0, 0.7));
		r = chart.getPlotArea().getSize();
		p = series1.getPixelCoordinates(1);
		assertEquals(r.x / 7d * 2, p.x, 1);
		assertEquals(r.y / 10d * 7, p.y, 1);
		// vertical + stack
		series1.enableStack(true);
		series2.enableStack(true);
		p = series2.getPixelCoordinates(1);
		assertEquals(r.x / 7d * 6, p.x, 1);
		assertEquals(r.y / 10d * 7, p.y, 1);
	}

	/**
	 * Test for line style.
	 */
	@Test
	public void testLineStyle() throws Exception {

		ILineSeries<?> series = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series");
		series.setYSeries(ySeries1);
		series.setSymbolType(PlotSymbolType.NONE);
		chart.getAxisSet().adjustRange();
		showChart();
		// set type
		series.setLineStyle(LineStyle.DASH);
		assertEquals(LineStyle.DASH, series.getLineStyle());
		showChart();
		series.setLineStyle(LineStyle.DASHDOT);
		assertEquals(LineStyle.DASHDOT, series.getLineStyle());
		showChart();
		series.setLineStyle(LineStyle.DASHDOTDOT);
		assertEquals(LineStyle.DASHDOTDOT, series.getLineStyle());
		showChart();
		series.setLineStyle(LineStyle.DOT);
		assertEquals(LineStyle.DOT, series.getLineStyle());
		showChart();
		series.setLineStyle(LineStyle.NONE);
		assertEquals(LineStyle.NONE, series.getLineStyle());
		showChart();
		// set null
		series.setLineStyle(null);
		assertEquals(LineStyle.SOLID, series.getLineStyle());
	}

	/**
	 * Test for line color.
	 */
	@Test
	public void testLineColor() throws Exception {

		ILineSeries<?> series = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series");
		series.setYSeries(ySeries1);
		series.setAntialias(SWT.ON);
		series.setSymbolType(PlotSymbolType.NONE);
		chart.getAxisSet().adjustRange();
		showChart();
		// set line color
		series.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		assertEquals(new RGB(0, 0, 0), series.getLineColor().getRGB());
		showChart();
		// set null
		series.setLineColor(null);
		assertEquals(new RGB(0, 0, 255), series.getLineColor().getRGB());
		// set the disposed color
		Color color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			series.setLineColor(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
	}

	/**
	 * Test for line width.
	 */
	@Test
	public void testLineWidth() throws Exception {

		ILineSeries<?> series = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series");
		series.setYSeries(ySeries1);
		series.setAntialias(SWT.ON);
		series.setSymbolType(PlotSymbolType.NONE);
		chart.getAxisSet().adjustRange();
		assertEquals(1, series.getLineWidth());
		showChart();
		// set line width
		series.setLineWidth(3);
		assertEquals(3, series.getLineWidth());
		showChart();
		// set line width
		series.setLineWidth(5);
		assertEquals(5, series.getLineWidth());
		showChart();
		// set illegal value to reset to default
		series.setLineWidth(0);
		assertEquals(1, series.getLineWidth());
	}

	/**
	 * Test for symbol type.
	 */
	@Test
	public void testSymbolType() throws Exception {

		ILineSeries<?> series = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "plot series 1");
		series.setYSeries(ySeries1);
		series.setLineStyle(LineStyle.NONE);
		chart.getAxisSet().adjustRange();
		showChart();
		// set symbol type
		series.setSymbolType(PlotSymbolType.CIRCLE);
		assertEquals(PlotSymbolType.CIRCLE, series.getSymbolType());
		showChart();
		series.setSymbolType(PlotSymbolType.CROSS);
		assertEquals(PlotSymbolType.CROSS, series.getSymbolType());
		showChart();
		series.setSymbolType(PlotSymbolType.DIAMOND);
		assertEquals(PlotSymbolType.DIAMOND, series.getSymbolType());
		showChart();
		series.setSymbolType(PlotSymbolType.INVERTED_TRIANGLE);
		assertEquals(PlotSymbolType.INVERTED_TRIANGLE, series.getSymbolType());
		showChart();
		series.setSymbolType(PlotSymbolType.PLUS);
		assertEquals(PlotSymbolType.PLUS, series.getSymbolType());
		showChart();
		series.setSymbolType(PlotSymbolType.SQUARE);
		assertEquals(PlotSymbolType.SQUARE, series.getSymbolType());
		showChart();
		series.setSymbolType(PlotSymbolType.TRIANGLE);
		assertEquals(PlotSymbolType.TRIANGLE, series.getSymbolType());
		showChart();
		series.setSymbolType(PlotSymbolType.NONE);
		assertEquals(PlotSymbolType.NONE, series.getSymbolType());
		showChart();
		series.setSymbolType(PlotSymbolType.EMOJI);
		assertEquals(PlotSymbolType.EMOJI, series.getSymbolType());
		assertEquals("😂", series.getExtendedPlotSymbolType());
		showChart();
		// set null
		series.setSymbolType(null);
		assertEquals(PlotSymbolType.CIRCLE, series.getSymbolType());
	}

	/**
	 * Test for symbol size.
	 */
	@Test
	public void testSymbolSize() throws Exception {

		ILineSeries<?> series = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series");
		series.setYSeries(ySeries1);
		series.setLineStyle(LineStyle.NONE);
		chart.getAxisSet().adjustRange();
		showChart();
		// set tiny value
		series.setSymbolSize(1);
		assertEquals(1, series.getSymbolSize());
		showChart();
		// set symbol size
		series.setSymbolSize(10);
		assertEquals(10, series.getSymbolSize());
		showChart();
		// set huge value
		series.setSymbolSize(20);
		assertEquals(20, series.getSymbolSize());
		showChart();
		// set illegal value
		series.setSymbolSize(-10);
		final int DEFAULT_SIZE = 4;
		assertEquals(DEFAULT_SIZE, series.getSymbolSize());
	}

	/**
	 * Test for setting color.
	 */
	@Test
	public void testSymbolColor() throws Exception {

		ILineSeries<?> series = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series");
		series.setYSeries(ySeries1);
		series.setLineStyle(LineStyle.NONE);
		chart.getAxisSet().adjustRange();
		showChart();
		// set color
		Color gray = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		series.setSymbolColor(gray);
		assertEquals(gray.getRGB(), series.getSymbolColor().getRGB());
		showChart();
		// set null
		series.setSymbolColor(null);
		Color darkGray = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
		assertEquals(darkGray.getRGB(), series.getSymbolColor().getRGB());
		// set the disposed color
		Color color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			series.setSymbolColor(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
	}

	/**
	 * Test for setting colors.
	 */
	@Test
	public void testSymbolColors() throws Exception {

		ILineSeries<?> series = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series");
		series.setYSeries(ySeries1);
		series.setLineStyle(LineStyle.NONE);
		chart.getAxisSet().adjustRange();
		showChart();
		// default
		assertEquals(0, series.getSymbolColors().length);
		// set colors
		final Color red = new Color(Display.getDefault(), new RGB(255, 0, 0));
		final Color green = new Color(Display.getDefault(), new RGB(0, 255, 0));
		Color[] colors = new Color[]{red, red, red, green, green};
		series.setSymbolColors(colors);
		Color[] results = series.getSymbolColors();
		for(int i = 0; i < colors.length; i++) {
			assertEquals(colors[i].getRGB(), results[i].getRGB());
		}
		showChart();
		// set the disposed color
		Color color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		colors = new Color[]{red, red, red, color, green};
		try {
			series.setSymbolColors(colors);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
	}

	/**
	 * Test for area chart.
	 */
	@Test
	public void testArea() throws Exception {

		ILineSeries<?> series1 = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series1");
		series1.setYSeries(ySeries1);
		ILineSeries<?> series2 = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series2");
		series2.setYSeries(ySeries2);
		series2.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		xAxis.setCategorySeries(categorySeries);
		chart.getAxisSet().adjustRange();
		// non-category + non-stack
		series1.enableArea(true);
		series2.enableArea(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// category + non-stack
		xAxis.enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// category + stack
		series1.enableStack(true);
		series2.enableStack(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// category + stack + range
		chart.getAxisSet().getXAxis(0).setRange(new Range(2, 3));
		showChart();
		// non-category + non-stack + vertical
		xAxis.enableCategory(false);
		series1.enableStack(false);
		series2.enableStack(false);
		chart.setOrientation(SWT.VERTICAL);
		chart.getAxisSet().adjustRange();
		showChart();
		// category + non-stack + vertical
		xAxis.enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// category + stack + vertical
		series1.enableStack(true);
		series2.enableStack(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// category + stack + range + vertical
		chart.getAxisSet().getXAxis(0).setRange(new Range(2, 3));
		showChart();
	}

	/**
	 * Test for step chart.
	 */
	@Test
	public void testStep() throws Exception {

		// create line series
		ILineSeries<?> series1 = (ILineSeries<?>)chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
		series1.setYSeries(ySeries1);
		series1.setSymbolType(PlotSymbolType.NONE);
		series1.enableStep(true);
		// horizontal + step
		chart.getAxisSet().adjustRange();
		series1.enableArea(true);
		showChart();
		// horizontal + negative series
		series1.setYSeries(ySeries3);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + step + category
		series1.setYSeries(ySeries1);
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		xAxis.enableCategory(true);
		xAxis.setCategorySeries(categorySeries);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + step + category + stack
		ILineSeries<?> series2 = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series 2");
		series2.setYSeries(ySeries2);
		series2.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		series2.setSymbolType(PlotSymbolType.NONE);
		series2.enableStep(true);
		series2.enableArea(true);
		series2.enableStack(true);
		series1.enableStack(true);
		chart.getAxisSet().adjustRange();
		showChart();
		seriesSet.deleteSeries("series 2");
		// vertical + step
		chart.setOrientation(SWT.VERTICAL);
		xAxis.enableCategory(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + step + negative series
		series1.setYSeries(ySeries3);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + step + category
		series1.setYSeries(ySeries1);
		xAxis.enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + step + category + stack
		series2 = (ILineSeries<?>)seriesSet.createSeries(SeriesType.LINE, "series 2");
		series2.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		series2.setYSeries(ySeries2);
		series2.setSymbolType(PlotSymbolType.NONE);
		series2.enableStep(true);
		series2.enableArea(true);
		series1.enableStack(true);
		series2.enableStack(true);
		chart.getAxisSet().adjustRange();
		showChart();
	}

	/**
	 * Test for scatter chart.
	 */
	@Test
	public void testScatter() throws Throwable {

		ILineSeries<?> series = (ILineSeries<?>)chart.getSeriesSet().createSeries(SeriesType.LINE, "series");
		series.setXSeries(xSeries2);
		series.setYSeries(ySeries1);
		series.setLineStyle(LineStyle.NONE);
		chart.getAxisSet().adjustRange();
		showChart();
	}
}
