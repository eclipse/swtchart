/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.util.ChartTestCase;
import org.junit.Test;

/**
 * Test case for axis set.
 */
public class AxisSetTest extends ChartTestCase {

	private IAxisSet axisSet;
	private static final double[] ySeries1 = {-2, -1, 0, 1, 2};
	private static final double[] ySeries2 = {4, 2, 0, -2, -4};

	@Override
	public void setUp() throws Exception {

		super.setUp();
		axisSet = chart.getAxisSet();
	}

	/**
	 * Test for default axes.
	 */
	@Test
	public void testDefaultAxes() throws Exception {

		// get the default axes
		IAxis[] axes = axisSet.getAxes();
		assertEquals(2, axes.length);
		for(IAxis axis : axes) {
			int id = axis.getId();
			assertEquals(0, id);
		}
		// get the default X axis
		IAxis[] xAxes = axisSet.getXAxes();
		assertEquals(1, xAxes.length);
		int id = xAxes[0].getId();
		assertEquals(0, id);
		// get the default Y axis
		IAxis[] yAxes = axisSet.getYAxes();
		assertEquals(1, yAxes.length);
		id = yAxes[0].getId();
		assertEquals(0, id);
		// get the id of default axes
		int[] ids = axisSet.getXAxisIds();
		assertArrayEquals(new int[]{0}, ids);
		ids = axisSet.getYAxisIds();
		assertArrayEquals(new int[]{0}, ids);
		// get axes with id
		IAxis xAxis = axisSet.getXAxis(0);
		id = xAxis.getId();
		assertEquals(0, id);
		IAxis yAxis = axisSet.getYAxis(0);
		id = yAxis.getId();
		assertEquals(0, id);
		// get axes with invalid id
		xAxis = axisSet.getXAxis(1);
		assertNull(xAxis);
		yAxis = axisSet.getYAxis(-1);
		assertNull(yAxis);
	}

	/**
	 * Test for creating and deleting axes.
	 */
	@Test
	public void testCreateAndDeleteAxes() throws Exception {

		ISeries series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
		series1.setYSeries(ySeries1);
		axisSet.adjustRange();
		showChart();
		// create x axes
		int xAxisId = axisSet.createXAxis();
		assertEquals(1, xAxisId);
		xAxisId = axisSet.createXAxis();
		assertEquals(2, xAxisId);
		showChart();
		// create y axes
		int yAxisId = axisSet.createYAxis();
		assertEquals(1, yAxisId);
		yAxisId = axisSet.createYAxis();
		assertEquals(2, yAxisId);
		showChart();
		int[] ids = axisSet.getXAxisIds();
		assertArrayEquals(new int[]{0, 1, 2}, ids);
		ids = axisSet.getYAxisIds();
		assertArrayEquals(new int[]{0, 1, 2}, ids);
		// delete x axis
		axisSet.deleteXAxis(2);
		ids = axisSet.getXAxisIds();
		assertEquals(2, ids.length);
		showChart();
		// delete y axis
		axisSet.deleteYAxis(1);
		ids = axisSet.getYAxisIds();
		assertEquals(2, ids.length);
		showChart();
		// delete axis whose id is 0 or doesn't exist
		try {
			axisSet.deleteXAxis(0);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		try {
			axisSet.deleteXAxis(2);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		try {
			axisSet.deleteYAxis(0);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		try {
			axisSet.deleteYAxis(1);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		ISeries series2 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 2");
		series2.setYSeries(ySeries2);
		series2.setXAxisId(1);
		series2.setYAxisId(2);
		axisSet.adjustRange();
		showChart();
		// delete axis to which series are assigned
		axisSet.deleteXAxis(1);
		axisSet.deleteYAxis(2);
		assertEquals(1, axisSet.getXAxisIds().length);
		assertEquals(1, axisSet.getYAxisIds().length);
		assertEquals(0, series2.getXAxisId());
		assertEquals(0, series2.getYAxisId());
		showChart();
	}

	/**
	 * Test for adjusting axis range.
	 */
	@Test
	public void testAdjustRange() throws Exception {

		ISeries series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
		ISeries series2 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 2");
		series1.setYSeries(ySeries1);
		series2.setYSeries(ySeries2);
		showChart();
		// adjust axis range of all axes
		axisSet.adjustRange();
		Range xRange = axisSet.getXAxis(0).getRange();
		assertEquals(-0.2, xRange.lower, 0.1);
		assertEquals(4.2, xRange.upper, 0.1);
		Range yRange = axisSet.getYAxis(0).getRange();
		assertEquals(-4.4, yRange.lower, 0.1);
		assertEquals(4.4, yRange.upper, 0.1);
		showChart();
	}

	/**
	 * Test for zooming in and out.
	 */
	@Test
	public void testZoomInOut() throws Exception {

		ISeries series1 = chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
		series1.setYSeries(ySeries1);
		axisSet.adjustRange();
		showChart();
		// zoom in
		axisSet.zoomIn();
		showChart();
		// zoom out
		axisSet.zoomOut();
		showChart();
	}
}
