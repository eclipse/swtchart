/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.RangeRestriction.ExtendType;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.CircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.model.NodeDataModel;

public abstract class AbstractExtendedChart extends AbstractHandledChart implements IChartDataCoordinates, IRangeSupport, IExtendedChart {

	private int seriesMaxDataPoints;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private RangeRestriction rangeRestriction = new RangeRestriction();
	/*
	 * The extended values are only used internally.
	 */
	private double extendedMinX;
	private double extendedMaxX;
	private double extendedMinY;
	private double extendedMaxY;
	//
	private boolean isCircularSeries;
	//
	private Map<Integer, IAxisSettings> xAxisSettingsMap = new HashMap<>();
	private Map<Integer, IAxisSettings> yAxisSettingsMap = new HashMap<>();
	/*
	 * The series settings map contains the settings that are currently used.
	 * The reset map contains a copy of the initial series settings.
	 */
	private Map<String, ISeriesSettings> seriesSettingsMap = new HashMap<>();
	private Map<String, ISeriesSettings> seriesSettingsMapReset = new HashMap<>();

	public AbstractExtendedChart(Composite parent, int style) {

		super(parent, style);
		resetCoordinates();
	}

	@Override
	public int getSeriesMaxDataPoints() {

		return seriesMaxDataPoints;
	}

	@Override
	public double getMinX() {

		return minX;
	}

	@Override
	public double getMaxX() {

		return maxX;
	}

	@Override
	public double getMinY() {

		return minY;
	}

	@Override
	public double getMaxY() {

		return maxY;
	}

	@Override
	public RangeRestriction getRangeRestriction() {

		return rangeRestriction;
	}

	@Override
	public void setRangeRestriction(RangeRestriction rangeRestriction) {

		this.rangeRestriction = rangeRestriction;
	}

	public IAxisSettings getXAxisSettings(int index) {

		return xAxisSettingsMap.get(index);
	}

	public IAxisSettings getYAxisSettings(int index) {

		return yAxisSettingsMap.get(index);
	}

	public void putXAxisSettings(int key, IAxisSettings axisSettings) {

		xAxisSettingsMap.put(key, axisSettings);
	}

	public boolean isCircularChart() {

		return isCircularSeries;
	}

	public void removeXAxisSettings() {

		Set<Integer> keySet = xAxisSettingsMap.keySet();
		Set<Integer> removeKeys = new HashSet<>();
		for(int key : keySet) {
			if(key != BaseChart.ID_PRIMARY_X_AXIS) {
				removeKeys.add(key);
			}
		}
		/*
		 * Remove the keys.
		 */
		for(int key : removeKeys) {
			xAxisSettingsMap.remove(key);
		}
	}

	public void putYAxisSettings(int key, IAxisSettings axisSettings) {

		yAxisSettingsMap.put(key, axisSettings);
	}

	public void removeYAxisSettings() {

		Set<Integer> keySet = yAxisSettingsMap.keySet();
		Set<Integer> removeKeys = new HashSet<>();
		for(int key : keySet) {
			if(key != BaseChart.ID_PRIMARY_Y_AXIS) {
				removeKeys.add(key);
			}
		}
		/*
		 * Remove the keys.
		 */
		for(int key : removeKeys) {
			yAxisSettingsMap.remove(key);
		}
	}

	public ISeriesSettings getSeriesSettings(String id) {

		return seriesSettingsMap.get(id);
	}

	@Override
	public void setRange(IAxis axis, int xStart, int xStop, boolean adjustMinMax) {

		if(axis != null && Math.abs(xStop - xStart) > 0 && !isUpdateSuspended()) {
			double start = axis.getDataCoordinate(Math.min(xStart, xStop));
			double stop = axis.getDataCoordinate(Math.max(xStart, xStop));
			setRange(axis, start, stop, adjustMinMax);
		}
	}

	@Override
	public void setRange(IAxis axis, double start, double stop, boolean adjustMinMax) {

		if(axis != null && Math.abs(stop - start) > 0 && !isUpdateSuspended()) {
			double min = Math.min(start, stop);
			double max = Math.max(start, stop);
			axis.setRange(new Range(min, max));
			if(adjustMinMax) {
				adjustMinMaxRange(axis);
			}
			//
			if(axis.getDirection() == Direction.X) {
				adjustSecondaryXAxes();
			} else if(axis.getDirection() == Direction.Y) {
				adjustSecondaryYAxes();
			}
		}
	}

	@Override
	public void adjustMinMaxRange(IAxis axis) {

		if(axis != null && !isUpdateSuspended()) {
			Range range = axis.getRange();
			if(axis.getDirection().equals(Direction.X)) {
				/*
				 * X-AXIS
				 */
				if(rangeRestriction.isZeroX()) {
					range.lower = (range.lower < 0) ? 0 : range.lower;
				} else {
					range.lower = (range.lower < minX) ? minX : range.lower;
				}
				extendRange(IExtendedChart.X_AXIS, range, extendedMinX, extendedMaxX, rangeRestriction.getExtendMinX(), rangeRestriction.getExtendMaxX());
			} else {
				/*
				 * Y-AXIS
				 */
				if(rangeRestriction.isForceZeroMinY()) {
					range.lower = 0.0d;
				} else {
					if(rangeRestriction.isZeroY()) {
						range.lower = (range.lower < 0) ? 0 : range.lower;
					} else {
						range.lower = (range.lower < minY) ? minY : range.lower;
					}
				}
				extendRange(IExtendedChart.Y_AXIS, range, extendedMinY, extendedMaxY, rangeRestriction.getExtendMinY(), rangeRestriction.getExtendMaxY());
			}
			/*
			 * Adjust the range.
			 */
			if(range.lower < range.upper) {
				axis.setRange(range);
			}
		}
	}

	/**
	 * Reset the currently used series settings by the stored initial settings if available.
	 * 
	 * @param id
	 */
	protected void resetSeriesSettings(String id) {

		ISeriesSettings seriesSettingsSource = seriesSettingsMapReset.get(id);
		if(seriesSettingsSource != null) {
			ISeriesSettings seriesSettingsSink = seriesSettingsMap.get(id);
			if(seriesSettingsSink != null) {
				MappingsSupport.transferSettings(seriesSettingsSource, seriesSettingsSink);
			}
		}
	}

	private void extendRange(String axis, Range range, double min, double max, double extendMin, double extendMax) {

		/*
		 * Calculate the extension value.
		 */
		double lowerExtension = getExtensionValue(axis, range.lower, extendMin);
		double upperExtension = getExtensionValue(axis, range.upper, extendMax);
		/*
		 * Min
		 */
		if(lowerExtension != 0.0d) {
			if(range.lower != min) {
				double lowerCalculated;
				if(range.lower > 0.0d) {
					lowerCalculated = range.lower - lowerExtension;
				} else {
					lowerCalculated = range.lower + lowerExtension;
				}
				//
				if(lowerCalculated <= min) {
					range.lower = min;
				} else {
					range.lower = (range.lower < min) ? min : range.lower;
				}
			}
		} else {
			range.lower = (range.lower < min) ? min : range.lower;
		}
		/*
		 * Max
		 */
		if(upperExtension != 0.0d) {
			if(range.upper != max) {
				double upperCalculated;
				if(range.upper > 0.0d) {
					upperCalculated = range.upper + upperExtension;
				} else {
					upperCalculated = range.upper - upperExtension;
				}
				//
				if(upperCalculated >= max) {
					range.upper = max;
				} else {
					range.upper = (range.upper > max) ? max : range.upper;
				}
			}
		} else {
			range.upper = (range.upper > max) ? max : range.upper;
		}
	}

	@Override
	public ISeries<?> createSeries(ISeriesData seriesData, ISeriesSettings seriesSettings) throws SeriesException {

		SeriesType seriesType = getSeriesType(seriesSettings);
		if(seriesType != null) {
			if(SeriesType.PIE.equals(seriesType)) {
				/*
				 * Pie or Doughnut
				 */
				ICircularSeriesData circularSeriesData = (ICircularSeriesData)seriesData;
				ICircularSeriesSettings circularSeriesSettings = (ICircularSeriesSettings)seriesSettings;
				NodeDataModel nodeDataModel = circularSeriesData.getDataModel();
				//
				isCircularSeries = true;
				seriesType = circularSeriesSettings.getSeriesType();
				/*
				 * Create the series.
				 */
				String id = CircularSeriesData.ID;
				mapSeriesSettings(id, seriesSettings);
				ISeries<?> series = seriesSet.createSeries(seriesType, id);
				ICircularSeries<?> circularSeries = (ICircularSeries<?>)series;
				circularSeries.setNodeDataModel(nodeDataModel);
				double depth = nodeDataModel.getRootPointer().getMaxSubTreeDepth() - 1;
				updateCoordinates(-depth, depth, -depth, depth);
				/*
				 * Create the series settings.
				 */
				String[] labels = circularSeries.getLabels();
				Color[] colors = circularSeries.getColors();
				if(labels != null) {
					for(int i = 0; i < labels.length; i++) {
						String label = labels[i];
						Node node = circularSeries.getNodeById(label);
						Color color = colors[i] != null ? colors[i] : Display.getDefault().getSystemColor(SWT.COLOR_RED);
						ISeriesSettings seriesSettingsCopy = MappingsSupport.copySettings(circularSeriesSettings);
						if(seriesSettingsCopy instanceof ICircularSeriesSettings) {
							ICircularSeriesSettings circularSeriesSettingsCopy = (ICircularSeriesSettings)seriesSettingsCopy;
							circularSeriesSettingsCopy.setVisible(node.isVisible());
							circularSeriesSettingsCopy.setVisibleInLegend(node.isVisibleInLegend());
							circularSeriesSettingsCopy.setDescription(node.getDescription());
							circularSeriesSettingsCopy.setSliceColor(color);
						}
						mapSeriesSettings(labels[i], seriesSettingsCopy);
					}
				}
				//
				return circularSeries;
			} else {
				/*
				 * Bar, Scatter or Line
				 */
				double[] xSeries = seriesData.getXSeries();
				double[] ySeries = seriesData.getYSeries();
				//
				if(xSeries.length == ySeries.length) {
					/*
					 * Put the settings to the map.
					 */
					String id = seriesData.getId();
					mapSeriesSettings(id, seriesSettings);
					ISeriesSet seriesSet = getSeriesSet();
					ISeries<?> series = seriesSet.createSeries(seriesType, id);
					series.setXSeries(xSeries);
					series.setYSeries(ySeries);
					calculateCoordinates(series);
					return series;
				} else {
					throw new SeriesException(Messages.getString(Messages.X_Y_SERIES_LENGTH_DIFFERS));
				}
			}
		} else {
			throw new SeriesException("The series type couldn't be determined.");
		}
	}

	private void mapSeriesSettings(String id, ISeriesSettings seriesSettings) {

		seriesSettingsMap.put(id, seriesSettings);
		ISeriesSettings seriesSettingsCopy = MappingsSupport.copySettings(seriesSettings);
		if(seriesSettingsCopy != null) {
			seriesSettingsMapReset.put(id, seriesSettingsCopy);
		}
	}

	@Override
	public void deleteSeries(String id) {

		ISeriesSet seriesSet = getSeriesSet();
		if(seriesSet.getSeries(id) != null) {
			resetCoordinates();
			seriesSet.deleteSeries(id);
			seriesSettingsMap.remove(id);
			seriesSettingsMapReset.remove(id);
			for(ISeries<?> series : seriesSet.getSeries()) {
				calculateCoordinates(series);
			}
		}
	}

	@Override
	public void appendSeries(ISeriesData seriesData) {

		if(seriesData != null) {
			ISeriesSet seriesSet = getSeriesSet();
			ISeries<?> series = seriesSet.getSeries(seriesData.getId());
			if(series != null) {
				/*
				 * Append the data.
				 */
				double[] xSeriesNew = concatenateSeries(series.getXSeries(), seriesData.getXSeries());
				series.setXSeries(xSeriesNew);
				double[] ySeriesNew = concatenateSeries(series.getYSeries(), seriesData.getYSeries());
				series.setYSeries(ySeriesNew);
				//
				calculateCoordinates(series);
			}
		}
	}

	protected boolean isRangeValid(Range range) {

		if(Double.isNaN(range.lower) || Double.isNaN(range.upper)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * If no type could be matched, null is returned.
	 * 
	 * @param seriesSettings
	 * @return {@link SeriesType}
	 */
	private SeriesType getSeriesType(ISeriesSettings seriesSettings) {

		SeriesType seriesType;
		//
		if(seriesSettings instanceof ILineSeriesSettings) {
			seriesType = SeriesType.LINE;
		} else if(seriesSettings instanceof IScatterSeriesSettings) {
			seriesType = SeriesType.LINE;
		} else if(seriesSettings instanceof IBarSeriesSettings) {
			seriesType = SeriesType.BAR;
		} else if(seriesSettings instanceof ICircularSeriesSettings) {
			seriesType = SeriesType.PIE;
		} else {
			seriesType = null;
		}
		//
		return seriesType;
	}

	private double[] concatenateSeries(double[] a, double[] b) {

		int length = a.length + b.length;
		double[] c = new double[length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	@Override
	public void setRange(String axis, double start, double stop) {

		IAxisSet axisSet = getAxisSet();
		IAxis selectedAxis = (axis.equals(IExtendedChart.X_AXIS)) ? axisSet.getXAxis(BaseChart.ID_PRIMARY_X_AXIS) : axisSet.getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		setRange(selectedAxis, start, stop, true);
	}

	@Override
	public void adjustRange(boolean adjustMinMax) {

		if(!isUpdateSuspended()) {
			getAxisSet().adjustRange();
			if(adjustMinMax) {
				adjustMinMaxRange(getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS));
				adjustMinMaxRange(getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS));
			}
			/*
			 * Adjust the secondary axes.
			 */
			adjustSecondaryXAxes();
			adjustSecondaryYAxes();
		}
	}

	public void adjustSecondaryAxes() {

		adjustSecondaryXAxes();
		adjustSecondaryYAxes();
	}

	@Override
	public void adjustSecondaryXAxes() {

		IAxisSet axisSet = getAxisSet();
		IAxis xAxis = axisSet.getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		Range range = xAxis.getRange();
		for(int id : axisSet.getXAxisIds()) {
			if(id != BaseChart.ID_PRIMARY_X_AXIS) {
				IAxis axis = axisSet.getXAxis(id);
				IAxisSettings axisSettings = xAxisSettingsMap.get(id);
				if(axis != null && axisSettings instanceof ISecondaryAxisSettings) {
					IAxisScaleConverter axisScaleConverter = ((ISecondaryAxisSettings)axisSettings).getAxisScaleConverter();
					axisScaleConverter.setChartDataCoordinates(this);
					double start = axisScaleConverter.convertToSecondaryUnit(range.lower);
					double end = axisScaleConverter.convertToSecondaryUnit(range.upper);
					if(end > start) {
						Range adjustedRange = new Range(start, end);
						axis.setRange(adjustedRange);
					} else {
						System.out.println(Messages.getString(Messages.CANT_SET_SECONDARY_X_AXIS_RANGE) + start + "\t" + end); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		}
	}

	@Override
	public void adjustSecondaryYAxes() {

		IAxisSet axisSet = getAxisSet();
		IAxis yAxis = axisSet.getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		Range range = yAxis.getRange();
		for(int id : axisSet.getYAxisIds()) {
			if(id != BaseChart.ID_PRIMARY_Y_AXIS) {
				IAxis axis = axisSet.getYAxis(id);
				IAxisSettings axisSettings = yAxisSettingsMap.get(id);
				if(axis != null && axisSettings instanceof ISecondaryAxisSettings) {
					IAxisScaleConverter axisScaleConverter = ((ISecondaryAxisSettings)axisSettings).getAxisScaleConverter();
					axisScaleConverter.setChartDataCoordinates(this);
					double start = axisScaleConverter.convertToSecondaryUnit(range.lower);
					double end = axisScaleConverter.convertToSecondaryUnit(range.upper);
					if(end > start) {
						Range adjustedRange = new Range(start, end);
						if(isRangeValid(adjustedRange)) {
							axis.setRange(adjustedRange);
						}
					}
				}
			}
		}
	}

	/*
	 * Min/max values will be set dynamically via Math.min and Math.max.
	 * Using the default double value 0 could lead to errors when using
	 * Math.min(...), hence initialize the values with the lowest/highest value.
	 */
	private void resetCoordinates() {

		seriesMaxDataPoints = 0;
		minX = Double.POSITIVE_INFINITY;
		maxX = Double.NEGATIVE_INFINITY;
		minY = Double.POSITIVE_INFINITY;
		maxY = Double.NEGATIVE_INFINITY;
	}

	private void calculateCoordinates(ISeries<?> series) {

		double[] xSeries = series.getXSeries();
		double[] ySeries = series.getYSeries();
		//
		if(xSeries.length != 0) {
			double seriesMinX = Arrays.stream(xSeries).min().getAsDouble();
			double seriesMaxX = Arrays.stream(xSeries).max().getAsDouble();
			double seriesMinY = Arrays.stream(ySeries).min().getAsDouble();
			double seriesMaxY = Arrays.stream(ySeries).max().getAsDouble();
			seriesMaxDataPoints = Math.max(seriesMaxDataPoints, xSeries.length);
			updateCoordinates(seriesMinX, seriesMaxX, seriesMinY, seriesMaxY);
		}
		//
	}

	protected void updateCoordinates(double seriesMinX, double seriesMaxX, double seriesMinY, double seriesMaxY) {

		minX = Math.min(minX, seriesMinX);
		minX = (rangeRestriction.isZeroX() && minX < 0.0d) ? 0.0d : minX;
		maxX = Math.max(maxX, seriesMaxX);
		minY = Math.min(minY, seriesMinY);
		minY = (rangeRestriction.isZeroY() && minY < 0.0d) ? 0.0d : minY;
		minY = (rangeRestriction.isForceZeroMinY()) ? 0.0d : minY;
		maxY = Math.max(maxY, seriesMaxY);
		//
		calculateExtendedCoordinates();
	}

	private void calculateExtendedCoordinates() {

		double extensionValue;
		/*
		 * Min X
		 */
		extendedMinX = minX;
		extensionValue = getExtensionValue(IExtendedChart.X_AXIS, extendedMinX, rangeRestriction.getExtendMinX());
		if(extendedMinX > 0.0d) {
			extendedMinX -= extensionValue;
		} else {
			extendedMinX += extensionValue;
		}
		/*
		 * Max X
		 */
		extendedMaxX = maxX;
		extensionValue = getExtensionValue(IExtendedChart.X_AXIS, extendedMaxX, rangeRestriction.getExtendMaxX());
		if(extendedMaxX > 0.0d) {
			extendedMaxX += extensionValue;
		} else {
			extendedMaxX -= extensionValue;
		}
		/*
		 * Min Y
		 */
		extendedMinY = minY;
		extensionValue = getExtensionValue(IExtendedChart.Y_AXIS, extendedMinY, rangeRestriction.getExtendMinY());
		if(extendedMinY > 0.0d) {
			extendedMinY -= extensionValue;
		} else {
			extendedMinY += extensionValue;
		}
		/*
		 * Max Y
		 */
		extendedMaxY = maxY;
		extensionValue = getExtensionValue(IExtendedChart.Y_AXIS, extendedMaxY, rangeRestriction.getExtendMaxY());
		if(extendedMaxY > 0.0d) {
			extendedMaxY += extensionValue;
		} else {
			extendedMaxY -= extensionValue;
		}
	}

	/**
	 * Use IExtendedChart.X_AXIS or IExtendedChart.Y_AXIS.
	 * 
	 * @param axis
	 * @param base
	 * @param extend
	 * @return double
	 */
	private double getExtensionValue(String axis, double base, double extend) {

		ExtendType extendType;
		if(IExtendedChart.X_AXIS.equals(axis)) {
			extendType = rangeRestriction.getExtendTypeX();
		} else {
			extendType = rangeRestriction.getExtendTypeY();
		}
		//
		double extensionValue = 0.0d;
		switch(extendType) {
			case RELATIVE:
				extensionValue = base * extend;
				break;
			case ABSOLUTE:
				extensionValue = extend;
				break;
		}
		//
		return extensionValue;
	}
}