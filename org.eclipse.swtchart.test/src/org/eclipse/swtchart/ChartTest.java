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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.util.ChartTestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test case for Chart.
 */
public class ChartTest extends ChartTestCase {

	private static final double[] xSeries = {1.0, 2.0, 3.0, 4.0, 5.0};
	private static final double[] ySeries1 = {0.1, 0.2, 0.3, 0.4, 0.5};
	private static final double[] ySeries2 = {1.0, 0.8, 0.6, 0.4, 0.2};
	private static final String[] categorySeries = {"Jan", "Feb", "Mar", "Apr", "May"};

	/**
	 * Test for background.
	 */
	@Test
	public void testBackground() {

		// check the default color
		showChart();
		Color color = chart.getBackground();
		assertEquals(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB(), color.getRGB());
		// set color
		Color syan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
		chart.setBackground(syan);
		color = chart.getBackground();
		assertEquals(syan.getRGB(), color.getRGB());
		showChart();
		// set the disposed color
		color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			chart.setBackground(color);
			fail();
		} catch(SWTException | IllegalArgumentException e) {
			// expected to reach here
		}
		color = chart.getBackground();
		assertEquals(syan.getRGB(), color.getRGB());
		// set null
		try {
			chart.setBackground(null);
		} catch(IllegalArgumentException e) {
			fail();
		}
		color = chart.getBackground();
		assertEquals(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB(), color.getRGB());
		showChart();
	}

	/**
	 * Test for background in plot area
	 */
	@Test
	public void testBackgroundInPlotArea() {

		// check the default color
		showChart();
		Color color = chart.getPlotArea().getBackground();
		assertEquals(new RGB(246, 245, 244), color.getRGB());
		// set color
		Color cyan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
		chart.getPlotArea().setBackground(cyan);
		color = chart.getPlotArea().getBackground();
		assertEquals(cyan.getRGB(), color.getRGB());
		showChart();
		// set the disposed color
		color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			chart.getPlotArea().setBackground(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		color = chart.getPlotArea().getBackground();
		assertEquals(cyan.getRGB(), color.getRGB());
		// set null
		try {
			chart.getPlotArea().setBackground(null);
		} catch(IllegalArgumentException e) {
			fail();
		}
		color = chart.getPlotArea().getBackground();
		assertEquals(new RGB(246, 245, 244), color.getRGB());
		showChart();
	}

	/**
	 * Test for chart orientation with line series
	 */
	@Test
	public void testOrientation1() {

		// create line series
		ILineSeries<?> lineSeries1 = (ILineSeries<?>)chart.getSeriesSet().createSeries(SeriesType.LINE, "line series 1");
		lineSeries1.setXSeries(xSeries);
		lineSeries1.setYSeries(ySeries1);
		lineSeries1.enableStack(true);
		lineSeries1.enableArea(true);
		ILineSeries<?> lineSeries2 = (ILineSeries<?>)chart.getSeriesSet().createSeries(SeriesType.LINE, "line series 2");
		lineSeries2.setXSeries(xSeries);
		lineSeries2.setYSeries(ySeries2);
		lineSeries2.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		lineSeries2.enableStack(true);
		lineSeries2.enableArea(true);
		// set category series
		chart.getAxisSet().getXAxis(0).setCategorySeries(categorySeries);
		// check default value
		int orientation = chart.getOrientation();
		assertEquals(SWT.HORIZONTAL, orientation);
		// set illegal value
		chart.setOrientation(-1);
		orientation = chart.getOrientation();
		assertEquals(SWT.HORIZONTAL, orientation);
		// horizontal + x log scale
		chart.setOrientation(SWT.HORIZONTAL);
		orientation = chart.getOrientation();
		assertEquals(SWT.HORIZONTAL, orientation);
		chart.getAxisSet().getXAxis(0).enableLogScale(true);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + y log scale
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + x log scale + y log scale
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(true);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + category + stack
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + y log scale + category
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + category
		lineSeries1.enableStack(false);
		lineSeries2.enableStack(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical
		chart.setOrientation(SWT.VERTICAL);
		orientation = chart.getOrientation();
		assertEquals(SWT.VERTICAL, orientation);
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + x log scale
		chart.getAxisSet().getXAxis(0).enableLogScale(true);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + y log scale
		lineSeries1.enableStack(true);
		lineSeries2.enableStack(true);
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + x log scale + y log scale
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(true);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + category + stack
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + y log scale + category
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + category
		lineSeries1.enableStack(false);
		lineSeries2.enableStack(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
	}

	/**
	 * Test for chart orientation with bar series
	 */
	@Test
	public void testOrientation2() {

		// create bar series
		IBarSeries<?> barSeries1 = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series 1");
		barSeries1.setXSeries(xSeries);
		barSeries1.setYSeries(ySeries1);
		barSeries1.enableStack(true);
		IBarSeries<?> barSeries2 = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series 2");
		barSeries2.setXSeries(xSeries);
		barSeries2.setYSeries(ySeries2);
		barSeries2.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		barSeries2.enableStack(true);
		// set category series
		chart.getAxisSet().getXAxis(0).setCategorySeries(categorySeries);
		// check default value
		showChart();
		int orientation = chart.getOrientation();
		assertEquals(SWT.HORIZONTAL, orientation);
		// set illegal value
		chart.setOrientation(-1);
		orientation = chart.getOrientation();
		assertEquals(SWT.HORIZONTAL, orientation);
		// horizontal + x log scale
		chart.setOrientation(SWT.HORIZONTAL);
		orientation = chart.getOrientation();
		assertEquals(SWT.HORIZONTAL, orientation);
		chart.getAxisSet().getXAxis(0).enableLogScale(true);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + y log scale
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + x log scale + y log scale
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(true);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + category + stack
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + y log scale + category
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// horizontal + category
		barSeries1.enableStack(false);
		barSeries2.enableStack(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical
		chart.setOrientation(SWT.VERTICAL);
		orientation = chart.getOrientation();
		assertEquals(SWT.VERTICAL, orientation);
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + x log scale
		chart.getAxisSet().getXAxis(0).enableLogScale(true);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + y log scale
		barSeries1.enableStack(true);
		barSeries2.enableStack(true);
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + x log scale + y log scale
		chart.getAxisSet().getXAxis(0).enableCategory(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(true);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + category + stack
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + y log scale + category
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(true);
		chart.getAxisSet().adjustRange();
		showChart();
		// vertical + category
		barSeries1.enableStack(false);
		barSeries2.enableStack(false);
		chart.getAxisSet().getXAxis(0).enableLogScale(false);
		chart.getAxisSet().getYAxis(0).enableLogScale(false);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().adjustRange();
		showChart();
	}

	/**
	 * Test for suspending update
	 */
	@Test
	public void testSuspendUpdate() {

		ISeries<?> series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series1");
		series1.setYSeries(ySeries1);
		ISeries<?> series2 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series2");
		series2.setYSeries(ySeries2);
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(categorySeries);
		chart.getAxisSet().adjustRange();
		showChart();
		int y = series2.getPixelCoordinates(0).y;
		try {
			chart.suspendUpdate(true);
			series1.enableStack(true);
			series2.enableStack(true);
			// update is suspended, so series should not be updated yet
			assertEquals(y, series2.getPixelCoordinates(0).y);
		} finally {
			chart.suspendUpdate(false);
			// update is now resumed, so series should be updated
			assertTrue(y > series2.getPixelCoordinates(0).y);
		}
		showChart();
	}

	/**
	 * Test for saving to file
	 */
	@Test
	@Ignore("environment dependent")
	public void testSaveToFile() {

		ISeries<?> series = chart.getSeriesSet().createSeries(SeriesType.LINE, "series1");
		series.setYSeries(ySeries1);
		chart.getAxisSet().adjustRange();
		showChart();
		String fileName = "/tmp/" + UUID.randomUUID().toString() + ".png";
		try {
			chart.save(fileName, SWT.IMAGE_PNG);
		} finally {
			File file = new File(fileName);
			assertTrue(file.exists());
			file.delete();
		}
	}

	/**
	 * Test for SWT resources that are internally created in following cases.
	 * <ul>
	 * <li>Y axis title is drawn.
	 * <li>risers are drawn.
	 * <li>rotated axis tick labels are drawn.
	 * </ul>
	 */
	@Test
	public void testSwtResources() throws Throwable {

		ISeries<?> barSeries = chart.getSeriesSet().createSeries(SeriesType.BAR, "bar series");
		barSeries.setYSeries(ySeries1);
		chart.getAxisSet().getXAxis(0).getTick().setTickLabelAngle(45);
		chart.getAxisSet().adjustRange();
		startTrackingSwtResources();
		for(int i = 0; i < 2; i++) {
			chart.redraw();
			// give UI thread a chance to redraw chart
			long time = System.currentTimeMillis();
			while(System.currentTimeMillis() - time < 100) {
				Display.getDefault().readAndDispatch();
			}
		}
		assertEquals(0, getSwtResourceCount());
	}
}
