/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.util.ChartTestCase;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case for axis tick.
 */
public class AxisTickTest extends ChartTestCase {

	private IAxisTick xAxisTick;
	private IAxisTick yAxisTick;

	private static Date[] xSecondSeries = { getTime(18, 50, 14), getTime(18, 50, 15), getTime(18, 50, 16),
			getTime(18, 50, 17), getTime(18, 50, 18) };

	private static Date[] xMinuteSeries = { getTime(18, 50, 0), getTime(18, 51, 0), getTime(18, 52, 0),
			getTime(18, 53, 0), getTime(18, 54, 0) };

	private static Date[] xHourSeries = { getTime(18, 0, 0), getTime(19, 0, 0), getTime(20, 0, 0), getTime(21, 0, 0),
			getTime(22, 0, 0) };

	private static Date[] xDateSeries = { getDate(1998, 2, 10), getDate(1998, 2, 11), getDate(1998, 2, 12),
			getDate(1998, 2, 13), getDate(1998, 2, 14) };

	private static Date[] xMonthSeries = { getDate(1998, 8, 1), getDate(1998, 9, 1), getDate(1998, 10, 1),
			getDate(1998, 11, 1), getDate(1998, 12, 1) };

	private static Date[] xYearSeries = { getDate(1998, 0, 1), getDate(1999, 0, 1), getDate(2000, 0, 1),
			getDate(2001, 0, 1), getDate(2002, 0, 1) };

	private static final double[] ySeries = { 0.0, 0.38, 0.71, 0.92, 1.0 };

	@Override
	public void setUp() throws Exception {
		super.setUp();
		xAxisTick = chart.getAxisSet().getXAxis(0).getTick();
		yAxisTick = chart.getAxisSet().getYAxis(0).getTick();
	}

	/**
	 * Test for foreground.
	 */
	@Test
	public void testForeground() throws Exception {

		// set null
		xAxisTick.setForeground(null);
		Color color = xAxisTick.getForeground();
		assertEquals(new RGB(0, 0, 255), color.getRGB());
		yAxisTick.setForeground(null);
		color = yAxisTick.getForeground();
		assertEquals(new RGB(0, 0, 255), color.getRGB());

		// set normal color
		showChart();
		Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		xAxisTick.setForeground(black);
		color = xAxisTick.getForeground();
		assertEquals(black.getRGB(), color.getRGB());
		showChart();
		yAxisTick.setForeground(black);
		color = yAxisTick.getForeground();
		assertEquals(black.getRGB(), color.getRGB());
		showChart();

		// set the disposed color
		color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			xAxisTick.setForeground(color);
			fail();
		} catch (IllegalArgumentException e) {
			// expected to reach here
		}
		color = xAxisTick.getForeground();
		assertEquals(new RGB(0, 0, 0), color.getRGB());
		color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			yAxisTick.setForeground(color);
			fail();
		} catch (IllegalArgumentException e) {
			// expected to reach here
		}
		color = yAxisTick.getForeground();
		assertEquals(new RGB(0, 0, 0), color.getRGB());
	}

	/**
	 * Test for axis tick font.
	 */
	@Test
	public void testFont() throws Exception {

		// set null
		xAxisTick.setFont(null);
		FontData fontData = xAxisTick.getFont().getFontData()[0];
		FontData smallFontData = Display.getDefault().getSystemFont().getFontData()[0];
		assertEquals(smallFontData.getName(), fontData.getName());
		assertEquals(smallFontData.getHeight(), fontData.getHeight());
		assertEquals(smallFontData.getStyle(), fontData.getStyle());
		yAxisTick.setFont(null);
		fontData = yAxisTick.getFont().getFontData()[0];
		assertEquals(smallFontData.getName(), fontData.getName());
		assertEquals(smallFontData.getHeight(), fontData.getHeight());
		assertEquals(smallFontData.getStyle(), fontData.getStyle());

		// set the disposed font
		Font font = new Font(Display.getCurrent(), "Tahoma", 11, SWT.BOLD);
		font.dispose();
		try {
			xAxisTick.setFont(font);
			fail();
		} catch (IllegalArgumentException e) {
			// expected to reach here
		}
		font = new Font(Display.getCurrent(), "Tahoma", 12, SWT.BOLD);
		font.dispose();
		try {
			yAxisTick.setFont(font);
			fail();
		} catch (IllegalArgumentException e) {
			// expected to reach here
		}

		// set normal font
		font = new Font(Display.getCurrent(), "Tahoma", 18, SWT.ITALIC);
		xAxisTick.setFont(font);
		fontData = xAxisTick.getFont().getFontData()[0];
		assertEquals("Tahoma", fontData.getName());
		assertEquals(18, fontData.getHeight());
		assertEquals(SWT.ITALIC, fontData.getStyle());
		showChart();
		yAxisTick.setFont(font);
		fontData = yAxisTick.getFont().getFontData()[0];
		assertEquals("Tahoma", fontData.getName());
		assertEquals(18, fontData.getHeight());
		assertEquals(SWT.ITALIC, fontData.getStyle());
		showChart();
		font.dispose();

		// set large font size
		font = new Font(Display.getCurrent(), "Tahoma", 64, SWT.BOLD);
		xAxisTick.setFont(font);
		fontData = xAxisTick.getFont().getFontData()[0];
		assertEquals(64, fontData.getHeight());
		showChart();
		yAxisTick.setFont(font);
		fontData = yAxisTick.getFont().getFontData()[0];
		assertEquals(64, fontData.getHeight());
		showChart();
		font.dispose();

		// set tiny font size
		font = new Font(Display.getCurrent(), "Tahoma", 4, SWT.ITALIC);
		xAxisTick.setFont(font);
		fontData = xAxisTick.getFont().getFontData()[0];
		assertEquals(4, fontData.getHeight());
		showChart();
		yAxisTick.setFont(font);
		fontData = yAxisTick.getFont().getFontData()[0];
		assertEquals(4, fontData.getHeight());
		showChart();
		font.dispose();
	}

	/**
	 * Test for axis tick visibility.
	 */
	@Test
	public void testVisibility() throws Exception {

		// show X axis tick
		xAxisTick.setVisible(false);
		assertFalse(xAxisTick.isVisible());
		showChart();
		xAxisTick.setVisible(true);
		assertTrue(xAxisTick.isVisible());
		showChart();

		// show Y axis tick
		yAxisTick.setVisible(false);
		assertFalse(yAxisTick.isVisible());
		showChart();
		yAxisTick.setVisible(true);
		assertTrue(yAxisTick.isVisible());
		showChart();
	}

	/**
	 * Test for tick mark step hint.
	 */
	@Test
	public void testTickMarkStepHint() throws Exception {

		// set small value the tick mark step hint
		xAxisTick.setTickMarkStepHint(10);
		assertEquals(64, xAxisTick.getTickMarkStepHint());
		showChart();
		yAxisTick.setTickMarkStepHint(10);
		assertEquals(64, yAxisTick.getTickMarkStepHint());
		showChart();

		// set the normal tick mark step hint
		xAxisTick.setTickMarkStepHint(200);
		assertEquals(200, xAxisTick.getTickMarkStepHint());
		showChart();
		xAxisTick.setTickMarkStepHint(16);
		assertEquals(16, xAxisTick.getTickMarkStepHint());
		showChart();
		yAxisTick.setTickMarkStepHint(200);
		assertEquals(200, yAxisTick.getTickMarkStepHint());
		showChart();
		yAxisTick.setTickMarkStepHint(16);
		assertEquals(16, yAxisTick.getTickMarkStepHint());
		showChart();

		// set large value the tick mark step hint
		xAxisTick.setTickMarkStepHint(1000);
		assertEquals(1000, xAxisTick.getTickMarkStepHint());
		showChart();
		yAxisTick.setTickMarkStepHint(1000);
		assertEquals(1000, yAxisTick.getTickMarkStepHint());
		showChart();
	}

	/**
	 * Test for format.
	 */
	@Test
	public void testFormat() throws Exception {
		// create line series
		ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "line series");
		lineSeries.setXDateSeries(xYearSeries);
		lineSeries.setYSeries(ySeries);
		chart.getAxisSet().adjustRange();
		showChart();

		Format yFormat = new DecimalFormat("###.000 M");
		yAxisTick.setFormat(yFormat);
		showChart();

		lineSeries.setXDateSeries(xMonthSeries);
		chart.getAxisSet().adjustRange();
		showChart();

		lineSeries.setXDateSeries(xDateSeries);
		chart.getAxisSet().adjustRange();
		showChart();

		lineSeries.setXDateSeries(xHourSeries);
		chart.getAxisSet().adjustRange();
		showChart();

		lineSeries.setXDateSeries(xMinuteSeries);
		chart.getAxisSet().adjustRange();
		showChart();

		lineSeries.setXDateSeries(xSecondSeries);
		chart.getAxisSet().adjustRange();
		showChart();

		Format xFormat = DateFormat.getTimeInstance(DateFormat.FULL);
		xAxisTick.setFormat(xFormat);
		showChart();
	}

	/**
	 * Test for tick label values.
	 */
	@Test
	public void testTickLabelValues() throws Throwable {
		ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "line series");
		lineSeries.setXDateSeries(xYearSeries);
		lineSeries.setYSeries(ySeries);
		chart.getAxisSet().adjustRange();
		showChart();

		double[] values = yAxisTick.getTickLabelValues();
		double[] expected = { 0.0, 0.2, 0.4, 0.6, 0.8, 1.0 };
		assertEquals(expected.length, values.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], values[i], 0);
		}
	}

	/**
	 * Test for tick label angle.
	 */
	@Test
	public void testTickLabelAngle() throws Throwable {
		ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "line series");
		lineSeries.setYSeries(ySeries);
		chart.getAxisSet().adjustRange();
		xAxisTick.setTickLabelAngle(45);
		showChart();
	}

	/**
	 * Test for bounds.
	 */
	@Test
    @Ignore("environment dependent")
	public void testBounds() throws Throwable {
		ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "line series");
		lineSeries.setYSeries(ySeries);
		chart.getAxisSet().adjustRange();
		showChart();

		Rectangle xAxisBounds = xAxisTick.getBounds();
		assertEquals(58, xAxisBounds.x);
		assertEquals(247, xAxisBounds.y);
		assertEquals(233, xAxisBounds.width);
		assertEquals(30, xAxisBounds.height);

		Rectangle yAxisBounds = yAxisTick.getBounds();
		assertEquals(23, yAxisBounds.x);
		assertEquals(30, yAxisBounds.y);
		assertEquals(35, yAxisBounds.width);
		assertEquals(217, yAxisBounds.height);

		chart.getAxisSet().getXAxis(0).setPosition(Position.Secondary);
		chart.getAxisSet().getYAxis(0).setPosition(Position.Secondary);
		showChart();

		xAxisBounds = xAxisTick.getBounds();
		assertEquals(5, xAxisBounds.x);
		assertEquals(48, xAxisBounds.y);
		assertEquals(233, xAxisBounds.width);
		assertEquals(30, xAxisBounds.height);

		yAxisBounds = yAxisTick.getBounds();
		assertEquals(238, yAxisBounds.x);
		assertEquals(78, yAxisBounds.y);
		assertEquals(35, yAxisBounds.width);
		assertEquals(217, yAxisBounds.height);
	}

	private static Date getDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, date);
		return cal.getTime();
	}

	private static Date getTime(int hrs, int min, int sec) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2008, 11, 1, hrs, min, sec);
		return cal.getTime();
	}
}
