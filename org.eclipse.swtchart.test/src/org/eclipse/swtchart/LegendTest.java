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
 * Philip Wenig - default font name
 *******************************************************************************/
package org.eclipse.swtchart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.util.ChartTestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test case for legend.
 */
public class LegendTest extends ChartTestCase {

	private ILegend legend;
	private static final double[] ySeries1 = {0.2, 0.2, 0.2, 0.2, 0.2};
	private static final double[] ySeries2 = {0.5, 0.4, 0.3, 0.2, 0.1};
	private static final double[] ySeries3 = {0.1, 0.2, 0.3, 0.4, 0.5};
	private static final double[] ySeries4 = {0.4, 0.4, 0.4, 0.4, 0.4};

	@Override
	public void setUp() {

		super.setUp();
		legend = chart.getLegend();
	}

	/**
	 * Test for legend visibility.
	 */
	@Test
	public void testVisibility() {

		// show legend for no series
		legend.setVisible(false);
		assertFalse(legend.isVisible());
		legend.setVisible(true);
		assertTrue(legend.isVisible());
		showChart();
		// show legend for one series
		ISeries<?> series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
		series1.setYSeries(ySeries1);
		showChart();
		legend.setVisible(false);
		assertFalse(legend.isVisible());
		showChart();
		legend.setVisible(true);
		assertTrue(legend.isVisible());
		showChart();
		// show legend for tree series
		ISeries<?> series2 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 2");
		series2.setYSeries(ySeries2);
		ISeries<?> series3 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 3");
		series3.setYSeries(ySeries3);
		showChart();
		legend.setVisible(false);
		assertFalse(legend.isVisible());
		showChart();
		legend.setVisible(true);
		assertTrue(legend.isVisible());
		showChart();
	}

	/**
	 * Test for foreground.
	 */
	@Test
	public void testForeground() {

		// default
		ISeries<?> series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
		series1.setYSeries(ySeries1);
		assertEquals(new RGB(0, 0, 0), legend.getForeground().getRGB());
		showChart();
		// set color
		Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		legend.setForeground(red);
		assertEquals(red.getRGB(), legend.getForeground().getRGB());
		showChart();
		// set null
		legend.setForeground(null);
		assertEquals(Display.getDefault().getSystemColor(SWT.COLOR_BLACK).toString(), legend.getForeground().toString());
		// set the disposed color
		Color color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			legend.setForeground(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
	}

	/**
	 * Test for background.
	 */
	@Test
	public void testBackground() {

		// set null
		legend.setBackground(null);
		Color color = legend.getBackground();
		assertEquals(new RGB(255, 255, 255), color.getRGB());
		// set color
		ISeries<?> series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
		series1.setYSeries(ySeries1);
		legend.setVisible(true);
		Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		legend.setBackground(red);
		color = legend.getBackground();
		assertEquals(red.getRGB(), color.getRGB());
		showChart();
		// set the disposed color
		color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			legend.setBackground(color);
			fail();
		} catch(SWTException | IllegalArgumentException e) {
			// expected to reach here
		}
	}

	/**
	 * Test for legend font.
	 */
	@Test
	public void testFont() {

		// set null
		legend.setFont(null);
		FontData fontData = legend.getFont().getFontData()[0];
		Font font = new Font(Display.getCurrent(), Resources.DEFAULT_FONT_NAME, 9, SWT.NORMAL);
		FontData systemFontData = font.getFontData()[0];
		assertEquals(systemFontData.getName(), fontData.getName());
		assertEquals(systemFontData.getHeight(), fontData.getHeight());
		assertEquals(systemFontData.getStyle(), fontData.getStyle());
		// set font
		ISeries<?> series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
		series1.setYSeries(ySeries1);
		legend.setVisible(true);
		font.dispose();
		font = new Font(Display.getCurrent(), Resources.DEFAULT_FONT_NAME, 18, SWT.ITALIC);
		legend.setFont(font);
		fontData = legend.getFont().getFontData()[0];
		assertEquals(Resources.DEFAULT_FONT_NAME, fontData.getName());
		assertEquals(18, fontData.getHeight());
		assertEquals(SWT.ITALIC, fontData.getStyle());
		showChart();
		// set the disposed font
		font.dispose();
		try {
			legend.setFont(font);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		// set large font size
		font = new Font(Display.getCurrent(), Resources.DEFAULT_FONT_NAME, 36, SWT.ITALIC);
		legend.setFont(font);
		fontData = legend.getFont().getFontData()[0];
		assertEquals(36, fontData.getHeight());
		showChart();
		// set tiny font size
		font.dispose();
		font = new Font(Display.getCurrent(), Resources.DEFAULT_FONT_NAME, 4, SWT.ITALIC);
		legend.setFont(font);
		fontData = legend.getFont().getFontData()[0];
		assertEquals(4, fontData.getHeight());
		showChart();
		font.dispose();
	}

	/**
	 * Test for legend position.
	 */
	@Test
	public void testPosition() {

		ISeries<?> series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
		series1.setYSeries(ySeries1);
		ISeries<?> series2 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 2");
		series2.setYSeries(ySeries2);
		ISeries<?> series3 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 3");
		series3.setYSeries(ySeries3);
		chart.getAxisSet().adjustRange();
		showChart();
		// check default
		assertEquals(SWT.RIGHT, legend.getPosition());
		// set position
		legend.setPosition(SWT.TOP);
		assertEquals(SWT.TOP, legend.getPosition());
		showChart();
		legend.setPosition(SWT.BOTTOM);
		assertEquals(SWT.BOTTOM, legend.getPosition());
		showChart();
		legend.setPosition(SWT.LEFT);
		assertEquals(SWT.LEFT, legend.getPosition());
		showChart();
		legend.setPosition(SWT.RIGHT);
		assertEquals(SWT.RIGHT, legend.getPosition());
		showChart();
		// set illegal position
		legend.setPosition(SWT.TOP);
		legend.setPosition(SWT.NONE);
		assertEquals(SWT.RIGHT, legend.getPosition());
		showChart();
	}

	/**
	 * Test for legend bounds.
	 */
	@Test
	@Ignore("environment dependent")
	public void testBounds() {

		ISeries<?> series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series1");
		series1.setYSeries(ySeries1);
		ISeries<?> series2 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series2");
		series2.setYSeries(ySeries2);
		ISeries<?> series3 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series3");
		series3.setYSeries(ySeries3);
		ISeries<?> series4 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series4");
		series4.setYSeries(ySeries4);
		chart.getAxisSet().adjustRange();
		// right position + no truncated
		Rectangle r = legend.getBounds("series1");
		assertEquals(new Rectangle(0, 5, 81, 14), r);
		r = legend.getBounds("series2");
		assertEquals(new Rectangle(0, 24, 81, 14), r);
		r = legend.getBounds("series3");
		assertEquals(new Rectangle(0, 43, 81, 14), r);
		r = legend.getBounds("series4");
		assertEquals(new Rectangle(0, 62, 81, 14), r);
		showChart();
		// right position + one series is truncated
		Point size = chart.getSize();
		chart.setSize(size.x, 100);
		r = legend.getBounds("series1");
		assertEquals(new Rectangle(0, 5, 81, 14), r);
		r = legend.getBounds("series2");
		assertEquals(new Rectangle(0, 24, 81, 14), r);
		r = legend.getBounds("series3");
		assertEquals(new Rectangle(0, 43, 81, 14), r);
		r = legend.getBounds("series4");
		assertEquals(new Rectangle(81, 5, 81, 14), r);
		showChart();
		// right position + two series are truncated
		chart.setSize(size.x, 80);
		r = legend.getBounds("series1");
		assertEquals(new Rectangle(0, 5, 81, 14), r);
		r = legend.getBounds("series2");
		assertEquals(new Rectangle(0, 24, 81, 14), r);
		r = legend.getBounds("series3");
		assertEquals(new Rectangle(81, 5, 81, 14), r);
		r = legend.getBounds("series4");
		assertEquals(new Rectangle(81, 24, 81, 14), r);
		showChart();
		// right position + three series are truncated
		chart.setSize(size.x, 60);
		r = legend.getBounds("series1");
		assertEquals(new Rectangle(0, 5, 81, 14), r);
		r = legend.getBounds("series2");
		assertEquals(new Rectangle(81, 5, 81, 14), r);
		r = legend.getBounds("series3");
		assertEquals(new Rectangle(162, 5, 81, 14), r);
		r = legend.getBounds("series4");
		assertEquals(new Rectangle(243, 5, 81, 14), r);
		showChart();
		// right position + no truncated
		chart.setSize(size);
		legend.setPosition(SWT.TOP);
		r = legend.getBounds("series1");
		assertEquals(new Rectangle(0, 5, 81, 14), r);
		r = legend.getBounds("series2");
		assertEquals(new Rectangle(81, 5, 81, 14), r);
		r = legend.getBounds("series3");
		assertEquals(new Rectangle(162, 5, 81, 14), r);
		r = legend.getBounds("series4");
		assertEquals(new Rectangle(243, 5, 81, 14), r);
		showChart();
		// right position + one series is truncated
		chart.setSize(250, size.y);
		r = legend.getBounds("series1");
		assertEquals(new Rectangle(0, 5, 81, 14), r);
		r = legend.getBounds("series2");
		assertEquals(new Rectangle(81, 5, 81, 14), r);
		r = legend.getBounds("series3");
		assertEquals(new Rectangle(162, 5, 81, 14), r);
		r = legend.getBounds("series4");
		assertEquals(new Rectangle(0, 24, 81, 14), r);
		showChart();
		// right position + two series are truncated
		chart.setSize(200, size.y);
		r = legend.getBounds("series1");
		assertEquals(new Rectangle(0, 5, 81, 14), r);
		r = legend.getBounds("series2");
		assertEquals(new Rectangle(81, 5, 81, 14), r);
		r = legend.getBounds("series3");
		assertEquals(new Rectangle(0, 24, 81, 14), r);
		r = legend.getBounds("series4");
		assertEquals(new Rectangle(81, 24, 81, 14), r);
		showChart();
		// right position + three series are truncated
		chart.setSize(130, size.y);
		r = legend.getBounds("series1");
		assertEquals(new Rectangle(0, 5, 81, 14), r);
		r = legend.getBounds("series2");
		assertEquals(new Rectangle(0, 24, 81, 14), r);
		r = legend.getBounds("series3");
		assertEquals(new Rectangle(0, 43, 81, 14), r);
		r = legend.getBounds("series4");
		assertEquals(new Rectangle(0, 62, 81, 14), r);
		showChart();
	}
}