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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.IErrorBar.ErrorBarType;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.util.ChartTestCase;
import org.junit.Test;

/**
 * Test case for error bar.
 */
public class ErrorBarTest extends ChartTestCase {

	private IErrorBar xErrorBar;
	private IErrorBar yErrorBar;
	private static final double[] ySeries1 = {-4, -2, 0, 2, 4};
	private static final double[] ySeries2 = {0.2, 0.3, 0.4, 0.5, 0.6};
	private static final double[] errors1 = {0.1, 0.3, 0.5, 0.7, 0.9};
	private static final double[] errors2 = {0.2, 0.6, 1.0, 1.4, 1.8};

	@Override
	public void setUp() throws Exception {

		super.setUp();
		ISeries<?> series = chart.getSeriesSet().createSeries(SeriesType.LINE, "series");
		series.setYSeries(ySeries1);
		xErrorBar = series.getXErrorBar();
		xErrorBar.setVisible(true);
		yErrorBar = series.getYErrorBar();
		yErrorBar.setVisible(true);
		chart.getAxisSet().adjustRange();
	}

	/**
	 * Test for type.
	 */
	@Test
	public void testType() throws Exception {

		// check default
		assertEquals(ErrorBarType.BOTH, xErrorBar.getType());
		assertEquals(ErrorBarType.BOTH, yErrorBar.getType());
		showChart();
		// plus for x error
		xErrorBar.setType(ErrorBarType.PLUS);
		assertEquals(ErrorBarType.PLUS, xErrorBar.getType());
		showChart();
		// minus for x error
		xErrorBar.setType(ErrorBarType.MINUS);
		assertEquals(ErrorBarType.MINUS, xErrorBar.getType());
		showChart();
		// both for x error
		xErrorBar.setType(ErrorBarType.BOTH);
		assertEquals(ErrorBarType.BOTH, xErrorBar.getType());
		showChart();
		// plus for y error
		yErrorBar.setType(ErrorBarType.PLUS);
		assertEquals(ErrorBarType.PLUS, yErrorBar.getType());
		showChart();
		// minus for y error
		yErrorBar.setType(ErrorBarType.MINUS);
		assertEquals(ErrorBarType.MINUS, yErrorBar.getType());
		showChart();
		// both for y error
		yErrorBar.setType(ErrorBarType.BOTH);
		assertEquals(ErrorBarType.BOTH, yErrorBar.getType());
		showChart();
	}

	/**
	 * Test for color.
	 */
	@Test
	public void testColor() throws Exception {

		// check default
		final Color darkGray = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
		assertEquals(darkGray.getRGB(), xErrorBar.getColor().getRGB());
		assertEquals(darkGray.getRGB(), yErrorBar.getColor().getRGB());
		// set null
		xErrorBar.setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		xErrorBar.setColor(null);
		assertEquals(darkGray.getRGB(), xErrorBar.getColor().getRGB());
		yErrorBar.setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		yErrorBar.setColor(null);
		assertEquals(darkGray.getRGB(), yErrorBar.getColor().getRGB());
		// set the disposed color
		Color color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			xErrorBar.setColor(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		try {
			yErrorBar.setColor(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		// set color
		showChart();
		xErrorBar.setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		final Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		assertEquals(red.getRGB(), xErrorBar.getColor().getRGB());
		showChart();
		yErrorBar.setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		assertEquals(red.getRGB(), yErrorBar.getColor().getRGB());
		showChart();
	}

	/**
	 * Test for line width.
	 */
	@Test
	public void testLineWidth() throws Exception {

		// check default
		assertEquals(1, xErrorBar.getLineWidth());
		assertEquals(1, yErrorBar.getLineWidth());
		showChart();
		// x error line width
		xErrorBar.setLineWidth(2);
		assertEquals(2, xErrorBar.getLineWidth());
		showChart();
		// y error line width
		yErrorBar.setLineWidth(2);
		assertEquals(2, yErrorBar.getLineWidth());
		showChart();
		// set illegal value
		xErrorBar.setLineWidth(0);
		yErrorBar.setLineWidth(0);
		assertEquals(1, xErrorBar.getLineWidth());
		assertEquals(1, yErrorBar.getLineWidth());
	}

	/**
	 * Test for error.
	 */
	@Test
	public void testError() throws Exception {

		// check default
		assertEquals(1d, xErrorBar.getError(), 0.01);
		assertEquals(1d, yErrorBar.getError(), 0.01);
		showChart();
		// set illegal negative value
		try {
			xErrorBar.setError(-1);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		try {
			yErrorBar.setError(-1);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		// set x error
		xErrorBar.setError(0.5);
		assertEquals(0.5, xErrorBar.getError(), 0.01);
		showChart();
		// set y error
		yErrorBar.setError(0.5);
		assertEquals(0.5, yErrorBar.getError(), 0.01);
		showChart();
		// log scale
		ISeries<?> series = chart.getSeriesSet().getSeries("series");
		series.setYSeries(ySeries2);
		yErrorBar.setError(0.1);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// log scale + vertical orientation
		chart.setOrientation(SWT.VERTICAL);
		showChart();
	}

	/**
	 * Test for error series.
	 */
	@Test
	public void testErrorSeries() throws Exception {

		// default
		assertEquals(0, xErrorBar.getPlusErrors().length);
		assertEquals(0, xErrorBar.getMinusErrors().length);
		assertEquals(0, yErrorBar.getPlusErrors().length);
		assertEquals(0, yErrorBar.getMinusErrors().length);
		showChart();
		// x error series
		xErrorBar.setMinusErrors(errors1);
		showChart();
		xErrorBar.setPlusErrors(errors1);
		showChart();
		// y error series
		yErrorBar.setMinusErrors(errors2);
		showChart();
		yErrorBar.setPlusErrors(errors2);
		showChart();
		// zoom out
		chart.getAxisSet().getXAxis(0).setRange(new Range(1, 3));
		chart.getAxisSet().getYAxis(0).setRange(new Range(-1.5, 1.5));
		showChart();
	}

	/**
	 * Test for label visibility.
	 */
	@Test
	public void testVisibility() throws Exception {

		chart.getSeriesSet().deleteSeries("series");
		ISeries<?> series = chart.getSeriesSet().createSeries(SeriesType.BAR, "series");
		series.setYSeries(ySeries1);
		xErrorBar = series.getXErrorBar();
		yErrorBar = series.getYErrorBar();
		chart.getAxisSet().adjustRange();
		// check default
		assertFalse(xErrorBar.isVisible());
		assertFalse(yErrorBar.isVisible());
		showChart();
		// x error
		xErrorBar.setVisible(true);
		assertTrue(xErrorBar.isVisible());
		showChart();
		xErrorBar.setVisible(false);
		assertFalse(xErrorBar.isVisible());
		showChart();
		// y error
		yErrorBar.setVisible(true);
		assertTrue(yErrorBar.isVisible());
		showChart();
		yErrorBar.setVisible(false);
		assertFalse(yErrorBar.isVisible());
		showChart();
	}
}
