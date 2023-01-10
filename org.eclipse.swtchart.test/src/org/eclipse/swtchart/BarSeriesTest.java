/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.util.ChartTestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test case for bar series.
 */
public class BarSeriesTest extends ChartTestCase {

	private ISeriesSet seriesSet;
	private static final double[] xSeries1 = {1.0, 2.0, 3.0, 4.0, 5.0};
	private static final double[] ySeries1 = {0.1, 0.2, 0.3, 0.4, 0.5};
	private static final double[] ySeries2 = {0.5, 0.4, 0.3, 0.2, 0.1};
	private static final double[] ySeries3 = {0.2, 0.2, 0.2, 0.2, 0.2};
	private static final String[] categorySeries = {"a", "b", "c", "d", "e"};

	@Override
	public void setUp()  {

		super.setUp();
		seriesSet = chart.getSeriesSet();
	}

	/**
	 * Test for series type.
	 */
	@Test
	public void testType()  {

		final ISeries<?> series = seriesSet.createSeries(SeriesType.BAR, "series");
		series.setYSeries(ySeries1);
		assertEquals(SeriesType.BAR, series.getType());
	}

	/**
	 * Test for visibility.
	 */
	@Test
	public void testVisibility()  {

		ISeries<?> series = seriesSet.createSeries(SeriesType.BAR, "series");
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
	public void testStack()  {

		// stacked bar series on category axis
		ISeries<?> series1 = seriesSet.createSeries(SeriesType.BAR, "series1");
		series1.setYSeries(ySeries1);
		ISeries<?> series2 = seriesSet.createSeries(SeriesType.BAR, "series2");
		series2.setYSeries(ySeries2);
		((IBarSeries<?>)series2).setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		xAxis.setCategorySeries(categorySeries);
		chart.getAxisSet().adjustRange();
		// non-category (cannot be stacked)
		series1.enableStack(true);
		series2.enableStack(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + category + stacked bar series
		xAxis.enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + category + stacked series + non-stacked series
		ISeries<?> series3 = seriesSet.createSeries(SeriesType.BAR, "series3");
		series3.setYSeries(ySeries3);
		((IBarSeries<?>)series3).setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		ISeries<?> series4 = seriesSet.createSeries(SeriesType.LINE, "series4");
		series4.setYSeries(ySeries2);
		chart.getAxisSet().adjustRange();
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + category + stacked bar series + stacked line series
		ISeries<?> series5 = seriesSet.createSeries(SeriesType.LINE, "series5");
		series5.setYSeries(ySeries1);
		series4.enableStack(true);
		series5.enableStack(true);
		chart.getAxisSet().adjustRange();
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
	public void testPixelCoordinates()  {

		ISeries<?> series1 = chart.getSeriesSet().createSeries(SeriesType.BAR, "series1");
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
		ISeries<?> series2 = seriesSet.createSeries(SeriesType.BAR, "series2");
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
	 * Test for padding.
	 */
	@Test
	public void testPadding()  {

		// set illegal padding
		IBarSeries<?> series = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series 1");
		try {
			series.setBarPadding(-10);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		try {
			series.setBarPadding(200);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		// set padding
		series.setYSeries(ySeries1);
		series.enableStack(true);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(categorySeries);
		chart.getAxisSet().adjustRange();
		showChart();
		series.setBarPadding(0);
		assertEquals(0, series.getBarPadding());
		showChart();
		series.setBarPadding(100);
		assertEquals(100, series.getBarPadding());
		showChart();
		series.setBarPadding(50);
		assertEquals(50, series.getBarPadding());
		showChart();
	}

	/**
	 * Test for setting bar color.
	 */
	@Test
	public void testBarColor()  {

		// specify null
		IBarSeries<?> series = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series 1");
		series.setBarColor(null);
		Color syan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
		assertEquals(syan.getRGB(), series.getBarColor().getRGB());
		// set color
		series.setYSeries(ySeries1);
		series.enableStack(true);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(categorySeries);
		chart.getAxisSet().adjustRange();
		showChart();
		Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		series.setBarColor(black);
		assertEquals(black.getRGB(), series.getBarColor().getRGB());
		showChart();
		// set the disposed color
		Color color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			series.setBarColor(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
	}

	/**
	 * Test for bar bounds.
	 */
	@Test
	@Ignore("environment dependent")
	public void testBounds()  {

		IBarSeries<?> series1 = (IBarSeries<?>)seriesSet.createSeries(SeriesType.BAR, "series1");
		series1.setYSeries(ySeries1);
		series1.enableStack(true);
		IBarSeries<?> series2 = (IBarSeries<?>)seriesSet.createSeries(SeriesType.BAR, "series2");
		series2.setYSeries(ySeries2);
		series2.enableStack(true);
		series2.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		IBarSeries<?> series3 = (IBarSeries<?>)seriesSet.createSeries(SeriesType.BAR, "series3");
		series3.setYSeries(ySeries3);
		series3.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_MAGENTA));
		chart.getAxisSet().getXAxis(0).setCategorySeries(categorySeries);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// get bounds
		Rectangle[] rs = series1.getBounds();
		assertEquals(new Rectangle(5, 181, 20, 36), rs[0]);
		assertEquals(new Rectangle(55, 146, 20, 71), rs[1]);
		assertEquals(new Rectangle(105, 111, 20, 106), rs[2]);
		assertEquals(new Rectangle(155, 76, 20, 141), rs[3]);
		assertEquals(new Rectangle(205, 41, 20, 176), rs[4]);
		// get bounds where some data points are invisible
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().getXAxis(0).setRange(new Range(0.5, 2.5));
		rs = series1.getBounds();
		assertNull(rs[0]);
		assertEquals(new Rectangle(11, 146, 34, 71), rs[1]);
		assertEquals(new Rectangle(137, 111, 34, 106), rs[2]);
		assertNull(rs[4]);
		showChart();
	}
}
