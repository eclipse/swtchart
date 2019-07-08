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
 * Christoph Läubrich - optimize disposal
 *******************************************************************************/
package org.eclipse.swtchart.internal.series;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.internal.compress.CompressConfig;
import org.eclipse.swtchart.internal.compress.ICompress;

/**
 * A series container.
 */
public class SeriesSet implements ISeriesSet {

	/** the chart */
	private final Chart chart;
	/** the series */
	private LinkedHashMap<String, Series> seriesMap;

	/**
	 * Constructor.
	 * 
	 * @param chart
	 *            the chart
	 */
	public SeriesSet(Chart chart) {
		this.chart = chart;
		seriesMap = new LinkedHashMap<String, Series>();
		chart.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {

				dispose();
			}
		});
	}

	/*
	 * @see ISeriesSet#createSeries(ISeries.SeriesType, String)
	 */
	@Override
	public ISeries createSeries(SeriesType type, String id) {

		if(id == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
			return null; // to suppress warning...
		}
		String trimmedId = id.trim();
		if("".equals(trimmedId)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		Series series = null;
		if(type == SeriesType.BAR) {
			series = new BarSeries(chart, trimmedId);
		} else if(type == SeriesType.LINE) {
			series = new LineSeries(chart, trimmedId);
		} else {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			return null; // to suppress warning...
		}
		Series oldSeries = seriesMap.get(trimmedId);
		if(oldSeries != null) {
			oldSeries.dispose();
		}
		int[] xAxisIds = chart.getAxisSet().getXAxisIds();
		int[] yAxisIds = chart.getAxisSet().getYAxisIds();
		series.setXAxisId(xAxisIds[0]);
		series.setYAxisId(yAxisIds[0]);
		seriesMap.put(trimmedId, series);
		Axis axis = (Axis)chart.getAxisSet().getXAxis(xAxisIds[0]);
		if(axis != null) {
			updateStackAndRiserData();
		}
		// legend will be shown if there is previously no series.
		chart.updateLayout();
		return series;
	}

	/*
	 * @see ISeriesSet#getSeries(String)
	 */
	@Override
	public ISeries getSeries(String id) {

		if(id == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		String trimmedId = id.trim();
		return seriesMap.get(trimmedId);
	}

	/*
	 * @see ISeriesSet#getSeries()
	 */
	@Override
	public ISeries[] getSeries() {

		Set<String> keys = seriesMap.keySet();
		ISeries[] series = new ISeries[keys.size()];
		int i = 0;
		for(String key : keys) {
			series[i++] = seriesMap.get(key);
		}
		return series;
	}

	/*
	 * @see ISeriesSet#deleteSeries(String)
	 */
	@Override
	public void deleteSeries(String id) {

		String trimmedId = validateSeriesId(id);
		seriesMap.get(trimmedId).dispose();
		seriesMap.remove(trimmedId);
		updateStackAndRiserData();
		// legend will be hidden if this is the last series
		chart.updateLayout();
	}

	/*
	 * @see ISeriesSet#bringForward(String)
	 */
	@Override
	public void bringForward(String id) {

		String trimmedId = validateSeriesId(id);
		String seriesId = null;
		LinkedHashMap<String, Series> newSeriesMap = new LinkedHashMap<String, Series>();
		for(Entry<String, Series> entry : seriesMap.entrySet()) {
			if(entry.getKey().equals(trimmedId)) {
				seriesId = trimmedId;
				continue;
			}
			newSeriesMap.put(entry.getKey(), entry.getValue());
			if(seriesId != null) {
				newSeriesMap.put(seriesId, seriesMap.get(seriesId));
				seriesId = null;
			}
		}
		if(seriesId != null) {
			newSeriesMap.put(seriesId, seriesMap.get(seriesId));
		}
		seriesMap = newSeriesMap;
		updateStackAndRiserData();
		chart.updateLayout();
	}

	/*
	 * @see ISeriesSet#bringToFront(String)
	 */
	@Override
	public void bringToFront(String id) {

		String trimmedId = validateSeriesId(id);
		Series series = seriesMap.get(trimmedId);
		seriesMap.remove(trimmedId);
		seriesMap.put(series.getId(), series);
		updateStackAndRiserData();
		chart.updateLayout();
	}

	/*
	 * @see ISeriesSet#sendBackward(String)
	 */
	@Override
	public void sendBackward(String id) {

		String trimmedId = validateSeriesId(id);
		String seriesId = null;
		LinkedHashMap<String, Series> newSeriesMap = new LinkedHashMap<String, Series>();
		for(Entry<String, Series> entry : seriesMap.entrySet()) {
			if(!entry.getKey().equals(trimmedId) || seriesId == null) {
				newSeriesMap.put(entry.getKey(), entry.getValue());
				seriesId = entry.getKey();
				continue;
			}
			newSeriesMap.remove(seriesId);
			newSeriesMap.put(entry.getKey(), entry.getValue());
			newSeriesMap.put(seriesId, seriesMap.get(seriesId));
		}
		seriesMap = newSeriesMap;
		updateStackAndRiserData();
		chart.updateLayout();
	}

	/*
	 * @see ISeriesSet#sendToBack(String)
	 */
	@Override
	public void sendToBack(String id) {

		String trimmedId = validateSeriesId(id);
		LinkedHashMap<String, Series> newSeriesMap = new LinkedHashMap<String, Series>();
		newSeriesMap.put(trimmedId, seriesMap.get(trimmedId));
		for(Entry<String, Series> entry : seriesMap.entrySet()) {
			if(!entry.getKey().equals(trimmedId)) {
				newSeriesMap.put(entry.getKey(), entry.getValue());
			}
		}
		seriesMap = newSeriesMap;
		updateStackAndRiserData();
		chart.updateLayout();
	}

	/**
	 * Disposes the series.
	 */
	public void dispose() {

		for(Entry<String, Series> entry : seriesMap.entrySet()) {
			entry.getValue().dispose();
		}
	}

	/**
	 * Validates the given series id.
	 * 
	 * @param id
	 *            the series id.
	 * @return the valid series id
	 */
	private String validateSeriesId(String id) {

		if(id == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		String trimmedId = id.trim();
		if(seriesMap.get(trimmedId) == null) {
			throw new IllegalArgumentException("Given series id doesn't exist");
		}
		return trimmedId;
	}

	/**
	 * Compresses all series data.
	 */
	public void compressAllSeries() {

		if(!chart.isCompressEnabled()) {
			return;
		}
		CompressConfig config = new CompressConfig();
		final int PRECISION = 2;
		Point p = chart.getPlotArea().getSize();
		int width = p.x * PRECISION;
		int height = p.y * PRECISION;
		config.setSizeInPixel(width, height);
		for(ISeries series : getSeries()) {
			int xAxisId = series.getXAxisId();
			int yAxisId = series.getYAxisId();
			IAxis xAxis = chart.getAxisSet().getXAxis(xAxisId);
			IAxis yAxis = chart.getAxisSet().getYAxis(yAxisId);
			if(xAxis == null || yAxis == null) {
				continue;
			}
			Range xRange = xAxis.getRange();
			Range yRange = yAxis.getRange();
			if(xRange == null || yRange == null) {
				continue;
			}
			double xMin = xRange.lower;
			double xMax = xRange.upper;
			double yMin = yRange.lower;
			double yMax = yRange.upper;
			config.setXLogScale(xAxis.isLogScaleEnabled());
			config.setYLogScale(yAxis.isLogScaleEnabled());
			double lower = xMin - (xMax - xMin) * 0.015;
			double upper = xMax + (xMax - xMin) * 0.015;
			if(xAxis.isLogScaleEnabled()) {
				lower = ((Series)series).getXRange().lower;
			}
			config.setXRange(lower, upper);
			lower = yMin - (yMax - yMin) * 0.015;
			upper = yMax + (yMax - yMin) * 0.015;
			if(yAxis.isLogScaleEnabled()) {
				lower = ((Series)series).getYRange().lower;
			}
			config.setYRange(lower, upper);
			ICompress compressor = ((Series)series).getCompressor();
			compressor.compress(config);
		}
	}

	/**
	 * Updates the compressor associated with the given axis.
	 * <p>
	 * In most cases, compressor is updated when series is changed. However,
	 * there is a case that compressor has to be updated with the changes in
	 * axis.
	 * 
	 * @param axis
	 *            the axis
	 */
	public void updateCompressor(Axis axis) {

		for(ISeries series : getSeries()) {
			int axisId = (axis.getDirection() == Direction.X) ? series.getXAxisId() : series.getYAxisId();
			if(axisId != axis.getId()) {
				continue;
			}
			ICompress compressor = ((Series)series).getCompressor();
			if(axis.isValidCategoryAxis()) {
				String[] categorySeries = axis.getCategorySeries();
				if(categorySeries == null) {
					continue;
				}
				double[] xSeries = new double[categorySeries.length];
				for(int i = 0; i < xSeries.length; i++) {
					xSeries[i] = i;
				}
				compressor.setXSeries(xSeries);
			} else if(((Series)series).getXSeries() != null) {
				compressor.setXSeries(((Series)series).getXSeries());
			}
		}
		compressAllSeries();
	}

	/**
	 * Updates the stack and riser data.
	 */
	public void updateStackAndRiserData() {

		if(chart.isUpdateSuspended()) {
			return;
		}
		for(IAxis xAxis : chart.getAxisSet().getXAxes()) {
			((Axis)xAxis).setNumRisers(0);
			for(IAxis yAxis : chart.getAxisSet().getYAxes()) {
				updateStackAndRiserData(xAxis, yAxis);
			}
		}
	}

	/**
	 * Updates the stack and riser data for given axes.
	 * 
	 * @param xAxis
	 *            the X axis
	 * @param yAxis
	 *            the Y axis
	 */
	private void updateStackAndRiserData(IAxis xAxis, IAxis yAxis) {

		int riserCnt = 0;
		int stackRiserPosition = -1;
		double[] stackBarSeries = null;
		double[] stackLineSeries = null;
		if(((Axis)xAxis).isValidCategoryAxis()) {
			String[] categorySeries = xAxis.getCategorySeries();
			if(categorySeries != null) {
				int size = categorySeries.length;
				stackBarSeries = new double[size];
				stackLineSeries = new double[size];
			}
		}
		for(ISeries series : getSeries()) {
			if(series.getXAxisId() != xAxis.getId() || series.getYAxisId() != yAxis.getId() || !series.isVisible()) {
				continue;
			}
			if(series.isStackEnabled() && !chart.getAxisSet().getYAxis(series.getYAxisId()).isLogScaleEnabled() && ((Axis)xAxis).isValidCategoryAxis()) {
				if(series.getType() == SeriesType.BAR) {
					if(stackRiserPosition == -1) {
						stackRiserPosition = riserCnt;
						riserCnt++;
					}
					((BarSeries)series).setRiserIndex(((Axis)xAxis).getNumRisers() + stackRiserPosition);
					setStackSeries(stackBarSeries, series);
				} else if(series.getType() == SeriesType.LINE) {
					setStackSeries(stackLineSeries, series);
				}
			} else {
				if(series.getType() == SeriesType.BAR) {
					((BarSeries)series).setRiserIndex(((Axis)xAxis).getNumRisers() + riserCnt++);
				}
			}
		}
		((Axis)xAxis).setNumRisers(((Axis)xAxis).getNumRisers() + riserCnt);
	}

	/**
	 * Sets the stack series.
	 * 
	 * @param stackSeries
	 *            the stack series
	 * @param series
	 *            the series
	 */
	private static void setStackSeries(double[] stackSeries, ISeries series) {

		double[] ySeries = series.getYSeries();
		if(ySeries == null || stackSeries == null) {
			return;
		}
		for(int i = 0; i < stackSeries.length; i++) {
			if(i >= ySeries.length) {
				break;
			}
			stackSeries[i] = BigDecimal.valueOf(stackSeries[i]).add(BigDecimal.valueOf(ySeries[i])).doubleValue();
		}
		double[] copiedStackSeries = new double[stackSeries.length];
		System.arraycopy(stackSeries, 0, copiedStackSeries, 0, stackSeries.length);
		((Series)series).setStackSeries(copiedStackSeries);
	}
}
