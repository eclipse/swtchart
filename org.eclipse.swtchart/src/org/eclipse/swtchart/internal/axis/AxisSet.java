/*******************************************************************************
 * Copyright (c) 2008, 2018 SWTChart project.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.internal.axis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.internal.series.SeriesSet;

/**
 * An axis container. By default, axis set has X Axis and Y axis with axis id 0.
 */
public class AxisSet implements IAxisSet {

	/** the set of X axes */
	private HashMap<Integer, Axis> xAxisMap;
	/** the set of Y axes */
	private HashMap<Integer, Axis> yAxisMap;
	/** the chart */
	private Chart chart;

	/**
	 * Constructor.
	 *
	 * @param chart
	 *            the chart
	 */
	public AxisSet(Chart chart) {
		this.chart = chart;
		xAxisMap = new HashMap<Integer, Axis>();
		yAxisMap = new HashMap<Integer, Axis>();
		// add default axes
		Axis xAxis = new Axis(0, Direction.X, chart);
		Axis yAxis = new Axis(0, Direction.Y, chart);
		xAxisMap.put(0, xAxis);
		yAxisMap.put(0, yAxis);
	}

	/**
	 * Gets the axis map for given direction.
	 *
	 * @param direction
	 *            the direction
	 * @return the axis map
	 */
	private HashMap<Integer, Axis> getAxisMap(Direction direction) {

		if(direction == Direction.X) {
			return xAxisMap;
		}
		return yAxisMap;
	}

	/*
	 * @see IAxisSet#createXAxis()
	 */
	public int createXAxis() {

		return createAxis(Direction.X);
	}

	/*
	 * @see IAxisSet#createYAxis()
	 */
	public int createYAxis() {

		return createAxis(Direction.Y);
	}

	/**
	 * Creates the axis for given direction.
	 *
	 * @param direction
	 *            the direction of axis
	 * @return the created axis id
	 */
	private int createAxis(Direction direction) {

		int id = getUniqueId(direction);
		Axis axis = new Axis(id, direction, chart);
		getAxisMap(direction).put(id, axis);
		chart.updateLayout();
		SeriesSet series = (SeriesSet)chart.getSeriesSet();
		if(series != null) {
			series.compressAllSeries();
		}
		return id;
	}

	/**
	 * Gets a unique axis id.
	 *
	 * @param direction
	 *            the axis direction
	 * @return a unique axis id
	 */
	private int getUniqueId(Direction direction) {

		Set<Integer> keySet = getAxisMap(direction).keySet();
		int i = 0;
		while(keySet.contains(i)) {
			i++;
		}
		return i;
	}

	/*
	 * @see IAxisSet#getXAxis(int)
	 */
	public IAxis getXAxis(int id) {

		return getAxis(id, Direction.X);
	}

	/*
	 * @see IAxisSet#getYAxis(int)
	 */
	public IAxis getYAxis(int id) {

		return getAxis(id, Direction.Y);
	}

	/**
	 * Gets the axis with axis id for given direction.
	 *
	 * @param id
	 *            the axis id
	 * @param direction
	 *            the direction
	 * @return the axis
	 */
	private IAxis getAxis(int id, Direction direction) {

		return getAxisMap(direction).get(id);
	}

	/*
	 * @see IAxisSet#getXAxes()
	 */
	public IAxis[] getXAxes() {

		Collection<Axis> values = xAxisMap.values();
		return values.toArray(new Axis[values.size()]);
	}

	/*
	 * @see IAxisSet#getYAxes()
	 */
	public IAxis[] getYAxes() {

		Collection<Axis> values = yAxisMap.values();
		return values.toArray(new Axis[values.size()]);
	}

	/*
	 * @see IAxisSet#getAxes()
	 */
	public IAxis[] getAxes() {

		Collection<Axis> axes = new ArrayList<Axis>();
		axes.addAll(xAxisMap.values());
		axes.addAll(yAxisMap.values());
		return axes.toArray(new Axis[axes.size()]);
	}

	/*
	 * @see IAxisSet#getXAxisIds()
	 */
	public int[] getXAxisIds() {

		return getAxisIds(Direction.X);
	}

	/*
	 * @see IAxisSet#getYAxisIds()
	 */
	public int[] getYAxisIds() {

		return getAxisIds(Direction.Y);
	}

	/**
	 * Gets the axis ids for given direction.
	 *
	 * @param direction
	 *            the direction
	 * @return the axis ids
	 */
	private int[] getAxisIds(Direction direction) {

		Set<Integer> keySet = getAxisMap(direction).keySet();
		Integer[] array = keySet.toArray(new Integer[keySet.size()]);
		int[] ids = new int[array.length];
		for(int i = 0; i < ids.length; i++) {
			ids[i] = array[i];
		}
		Arrays.sort(ids);
		return ids;
	}

	/*
	 * @see IAxisSet#deleteXAxis(int)
	 */
	public void deleteXAxis(int id) {

		deleteAxis(id, Direction.X);
	}

	/*
	 * @see IAxisSet#deleteYAxis(int)
	 */
	public void deleteYAxis(int id) {

		deleteAxis(id, Direction.Y);
	}

	/**
	 * Deletes the axis with the axis id for given direction.
	 *
	 * @param id
	 *            the axis id
	 * @param direction
	 *            the direction
	 */
	private void deleteAxis(int id, Direction direction) {

		if(id == 0) {
			SWT.error(SWT.ERROR_CANNOT_BE_ZERO);
		}
		if(getAxisMap(direction).get(id) == null) {
			throw new IllegalArgumentException("Given axis id doesn't exist");
		}
		((Axis)getAxis(id, direction)).dispose();
		getAxisMap(direction).remove(id);
		for(ISeries series : chart.getSeriesSet().getSeries()) {
			if(direction == Direction.X) {
				if(series.getXAxisId() == id) {
					series.setXAxisId(0);
				}
			} else {
				if(series.getYAxisId() == id) {
					series.setYAxisId(0);
				}
			}
		}
		chart.updateLayout();
	}

	/*
	 * @see IAxisSet#adjustRange()
	 */
	public void adjustRange() {

		for(IAxis axis : getAxes()) {
			((Axis)axis).adjustRange(false);
		}
		chart.updateLayout();
	}

	/*
	 * @see IAxisSet#zoomIn()
	 */
	public void zoomIn() {

		for(IAxis axis : getAxes()) {
			axis.zoomIn();
		}
	}

	/*
	 * @see IAxisSet#zoomOut()
	 */
	public void zoomOut() {

		for(IAxis axis : getAxes()) {
			axis.zoomOut();
		}
	}

	/**
	 * Updates the layout data.
	 */
	public void updateLayoutData() {

		for(IAxis axis : getAxes()) {
			((Axis)axis).updateLayoutData();
		}
	}

	/**
	 * Refreshes the cache.
	 */
	public void refresh() {

		for(IAxis axis : getAxes()) {
			((Axis)axis).refresh();
		}
	}

	/**
	 * Disposes the resources.
	 */
	public void dispose() {

		for(IAxis axis : getAxes()) {
			((Axis)axis).dispose();
		}
	}
}
