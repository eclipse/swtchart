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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.util.ChartTestCase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case for series label.
 */
public class SeriesLabelTest extends ChartTestCase {

	private ISeriesLabel label;

	private static final double[] ySeries1 = { 0.1, 0.2, 0.3, 0.4, 0.5 };
	private static final double[] ySeries2 = { 0.5, 0.4, 0.3, 0.2, 0.1 };
	private static final double[] ySeries3 = { 0.2, 0.2, 0.2, 0.2, 0.2 };
	private static final double[] ySeries4 = { -0.2, 0.1, 0, 0.1, 0.2 };
	private static final String[] categorySeries = { "a", "b", "c", "d", "e" };
	private static final String[] formats1 = { "aa", "bb", "cc", "dd", "ee" };
	private static final String[] formats2 = { "a1", "a2", "a3", "a4", "a5" };
	private static final String[] formats3 = { "b1", "b2", "b3", "b4", "b5" };

	@Override
	public void setUp() throws Exception {
		super.setUp();
		ISeries series = chart.getSeriesSet().createSeries(SeriesType.LINE, "series");
		series.setYSeries(ySeries1);
		label = series.getLabel();
		label.setVisible(true);
		chart.getAxisSet().adjustRange();
	}

	/**
	 * Test for label format.
	 */
    @Test
	public void testFormat() throws Exception {

		// set null
		label.setFormat(null);
		assertEquals("#.###########", label.getFormat());
		showChart();

		// set decimal format
		label.setFormat("###.000");
		assertEquals("###.000", label.getFormat());
		showChart();

		// set string format
		label.setFormat("foo");
		assertEquals("foo", label.getFormat());
		showChart();

		// vertical + line
		chart.setOrientation(SWT.VERTICAL);
		showChart();

		// horizontal + bar
		chart.setOrientation(SWT.HORIZONTAL);
		chart.getSeriesSet().deleteSeries("series");
		ISeries series2 = chart.getSeriesSet().createSeries(SeriesType.BAR, "series2");
		series2.setYSeries(ySeries2);
		series2.getLabel().setVisible(true);
		chart.getAxisSet().adjustRange();
		showChart();

		// horizontal + bar + negative data points
		series2.setYSeries(ySeries4);
		series2.getLabel().setVisible(true);
		chart.getAxisSet().adjustRange();
		showChart();

		// horizontal + bar + category
		chart.getAxisSet().getXAxis(0).setCategorySeries(categorySeries);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		showChart();

		// horizontal + bar + category + stack
		ISeries series3 = chart.getSeriesSet().createSeries(SeriesType.BAR, "series3");
		series3.setYSeries(ySeries3);
		series3.getLabel().setVisible(true);
		series3.enableStack(true);
		series2.setYSeries(ySeries2);
		series2.enableStack(true);
		chart.getAxisSet().adjustRange();
		showChart();

		// horizontal + bars + category + stack
		ISeries series1 = chart.getSeriesSet().createSeries(SeriesType.BAR, "series1");
		series1.setYSeries(ySeries1);
		series1.getLabel().setVisible(true);
		chart.getAxisSet().adjustRange();
		showChart();

		// horizontal + bars + category + stack + partially visible series
		chart.getAxisSet().getXAxis(0).setRange(new Range(2, 3));
		showChart();

		// vertical + bars + category + stack
		chart.setOrientation(SWT.VERTICAL);
		chart.getAxisSet().adjustRange();
		showChart();
	}

	/**
	 * Test for label formats.
	 */
    @Test
	public void testFormats() throws Exception {

		// decimal formats
		String[] formats = { "#.##", "#.##", "#.##", ".000", ".000" };
		label.setFormats(formats);
		showChart();

		// string formats
		label.setFormats(formats1);
		showChart();

		// horizontal + bar + category + stack
		chart.getSeriesSet().deleteSeries("series");
		ISeries series1 = chart.getSeriesSet().createSeries(SeriesType.BAR, "series1");
		series1.setYSeries(ySeries1);
		series1.getLabel().setVisible(true);
		series1.getLabel().setFormats(formats1);
		chart.getAxisSet().getXAxis(0).setCategorySeries(categorySeries);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		ISeries series2 = chart.getSeriesSet().createSeries(SeriesType.BAR, "series2");
		series2.getLabel().setVisible(true);
		series2.setYSeries(ySeries2);
		series2.enableStack(true);
		series2.getLabel().setFormats(formats2);
		ISeries series3 = chart.getSeriesSet().createSeries(SeriesType.BAR, "series3");
		series3.setYSeries(ySeries3);
		series3.getLabel().setVisible(true);
		series3.enableStack(true);
		series3.getLabel().setFormats(formats3);
		chart.getAxisSet().adjustRange();
		showChart();

		// horizontal + bar + category + stack + partially visible series
		chart.getAxisSet().getXAxis(0).setRange(new Range(2, 3));
		showChart();

		// vertical + bar + category + stack
		chart.setOrientation(SWT.VERTICAL);
		chart.getAxisSet().adjustRange();
		showChart();
	}

	/**
	 * Test for setting color.
	 */
    @Test
	public void testForeground() throws Exception {

		// set null
		label.setForeground(null);
		assertEquals(new RGB(0, 0, 0), label.getForeground().getRGB());

		// set the disposed color
		Color color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			label.setForeground(color);
			fail();
		} catch (IllegalArgumentException e) {
			// expected to reach here
		}

		// set color
		showChart();
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		assertEquals(new RGB(0, 0, 255), label.getForeground().getRGB());
		showChart();
	}

	/**
	 * Test for title font.
	 */
    @Test
	public void testFont() throws Exception {

		// set null
		label.setFont(null);
		FontData fontData = label.getFont().getFontData()[0];
		FontData smallFontData = Display.getDefault().getSystemFont().getFontData()[0];
		assertEquals(smallFontData.getName(), fontData.getName());
		assertEquals(smallFontData.getHeight(), fontData.getHeight());
		assertEquals(smallFontData.getStyle(), fontData.getStyle());

		// set font
		Font font = new Font(Display.getCurrent(), "Tahoma", 13, SWT.ITALIC);
		label.setFont(font);
		fontData = label.getFont().getFontData()[0];
		assertEquals("Tahoma", fontData.getName());
		assertEquals(13, fontData.getHeight());
		assertEquals(SWT.ITALIC, fontData.getStyle());
		showChart();

		// set the disposed font
		font.dispose();
		try {
			label.setFont(font);
			fail();
		} catch (IllegalArgumentException e) {
			// expected to reach here
		}

		// set large font size
		font = new Font(Display.getCurrent(), "Tahoma", 16, SWT.ITALIC);
		label.setFont(font);
		fontData = label.getFont().getFontData()[0];
		assertEquals(16, fontData.getHeight());
		showChart();
		font.dispose();

		// set tiny font size
		font = new Font(Display.getCurrent(), "Tahoma", 4, SWT.ITALIC);
		label.setFont(font);
		fontData = label.getFont().getFontData()[0];
		assertEquals(4, fontData.getHeight());
		showChart();
		font.dispose();
	}

	/**
	 * Test for label visibility.
	 */
    @Test
	public void testVisibility() throws Exception {

		// set visibility
		label.setVisible(false);
		assertFalse(label.isVisible());
		showChart();
		label.setVisible(true);
		assertTrue(label.isVisible());
		showChart();
	}
}
