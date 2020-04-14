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
 * Christoph Läubrich - use getSize instead of bounds since we are not interested in the location anyways, add support for datamodel
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.internal.axis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IDisposeListener;
import org.eclipse.swtchart.IGrid;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.internal.Grid;
import org.eclipse.swtchart.internal.series.Series;
import org.eclipse.swtchart.internal.series.SeriesSet;

/**
 * An axis.
 */
public class Axis implements IAxis {

	/** the margin in pixels */
	public final static int MARGIN = 5;
	/** the default minimum value of range */
	public final static double DEFAULT_MIN = 0d;
	/** the default maximum value of range */
	public final static double DEFAULT_MAX = 1d;
	/** the default minimum value of log scale range */
	public final static double DEFAULT_LOG_SCALE_MIN = 0.1d;
	/** the default maximum value of log scale range */
	public final static double DEFAULT_LOG_SCALE_MAX = 1d;
	/** the ratio to be zoomed */
	private static final double ZOOM_RATIO = 0.2;
	/** the ratio to be scrolled */
	private static final double SCROLL_RATIO = 0.1;
	/** the maximum resolution with digits */
	private static final double MAX_RESOLUTION = 13;
	/** the axis id */
	private int id;
	/** the axis direction */
	private Direction direction;
	/** the axis position */
	private Position position;
	/** the minimum value of axis range */
	private double min;
	/** the maximum value of axis range */
	private double max;
	/** the axis title */
	private AxisTitle title;
	/** the axis tick */
	private AxisTick tick;
	/** the grid */
	private Grid grid;
	/** the plot chart */
	private Chart chart;
	/** the state if the axis scale is log scale */
	private boolean logScaleEnabled;
	/** the state indicating if axis type is category */
	private boolean categoryAxisEnabled;
	/** the state indicating if axis is reversed */
	private boolean reversed;
	/** the state indicates if the axis data points are only integers */
	private boolean integerDataPointAxis;
	/** the category series */
	private String[] categorySeries;
	/** the number of riser per category */
	private int numRisers;
	/** the state indicating if the axis is horizontal */
	private boolean isHorizontalAxis;
	/** the plot area width */
	private int width;
	/** the plot area height */
	private int height;
	/** the list of dispose listeners */
	private List<IDisposeListener> listeners;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            the axis index
	 * @param direction
	 *            the axis direction (X or Y)
	 * @param chart
	 *            the chart
	 */
	public Axis(int id, Direction direction, Chart chart) {
		this.id = id;
		this.direction = direction;
		this.chart = chart;
		grid = new Grid(this);
		title = new AxisTitle(chart, SWT.NONE, this, direction);
		tick = new AxisTick(chart, this);
		listeners = new ArrayList<IDisposeListener>();
		// sets initial default values
		position = Position.Primary;
		min = DEFAULT_MIN;
		max = DEFAULT_MAX;
		logScaleEnabled = false;
		categoryAxisEnabled = false;
		reversed = false;
		integerDataPointAxis = false;
	}

	@Override
	public int getId() {

		return id;
	}

	@Override
	public Direction getDirection() {

		return direction;
	}

	@Override
	public Position getPosition() {

		return position;
	}

	@Override
	public void setPosition(Position position) {

		if(position == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if(this.position == position) {
			return;
		}
		this.position = position;
		chart.updateLayout();
	}

	@Override
	public void setRange(Range range) {

		setRange(range, true);
	}

	/**
	 * Sets the axis range.
	 * 
	 * @param range
	 *            the axis range
	 * @param update
	 *            true if updating the chart layout
	 */
	public void setRange(Range range, boolean update) {

		if(range == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
			return; // to suppress warnings...
		}
		if(Double.isNaN(range.lower) || Double.isNaN(range.upper) || Double.isInfinite(range.lower) || Double.isInfinite(range.upper) || range.lower > range.upper) {
			throw new IllegalArgumentException(Messages.getString(Messages.ILLEGAL_RANGE) + range);
		}
		if(min == range.lower && max == range.upper) {
			return;
		}
		if(isValidCategoryAxis()) {
			min = (int)range.lower;
			max = (int)range.upper;
			if(min < 0) {
				min = 0;
			}
			if(max > categorySeries.length - 1) {
				max = categorySeries.length - 1;
			}
		} else {
			if(range.lower == range.upper) {
				throw new IllegalArgumentException(Messages.getString(Messages.GIVEN_RANGE_INVALID));
			}
			if(logScaleEnabled && range.lower <= 0) {
				range.lower = min;
			}
			if(Math.abs(range.lower / (range.upper - range.lower)) > Math.pow(10, MAX_RESOLUTION)) {
				return;
			}
			min = range.lower;
			max = range.upper;
		}
		if(update) {
			chart.updateLayout();
		}
	}

	@Override
	public Range getRange() {

		return new Range(min, max);
	}

	@Override
	public ITitle getTitle() {

		return title;
	}

	@Override
	public AxisTick getTick() {

		return tick;
	}

	@Override
	public boolean isIntegerDataPointAxis() {

		return integerDataPointAxis;
	}

	@Override
	public void setIntegerDataPointAxis(boolean integerDataPointAxis) {

		if(this.integerDataPointAxis == integerDataPointAxis) {
			return;
		} else {
			this.integerDataPointAxis = integerDataPointAxis;
			if(integerDataPointAxis) {
				enableCategory(false);
				enableLogScale(false);
			}
			updateLayoutData();
		}
	}

	@Override
	public void enableLogScale(boolean enabled) throws IllegalStateException {

		if(logScaleEnabled == enabled) {
			return;
		}
		if(enabled) {
			// check if series contain zero or negative value
			double minSeriesValue = getMinSeriesValue();
			if(minSeriesValue <= 0) {
				throw new IllegalStateException(Messages.getString(Messages.SERIES_CONTAIN_INVALID_VALUES));
			}
			// adjust the range in order not to have zero or negative value
			if(min <= 0) {
				min = Double.isNaN(minSeriesValue) ? DEFAULT_LOG_SCALE_MIN : minSeriesValue;
			}
			if(max < min) {
				max = DEFAULT_LOG_SCALE_MAX;
			}
			// disable category axis
			if(categoryAxisEnabled) {
				categoryAxisEnabled = false;
				((SeriesSet)chart.getSeriesSet()).updateCompressor(this);
			}
		}
		logScaleEnabled = enabled;
		chart.updateLayout();
		((SeriesSet)chart.getSeriesSet()).compressAllSeries();
	}

	/**
	 * Gets the minimum value of series belonging to this axis.
	 * 
	 * @return the minimum value of series belonging to this axis
	 */
	private double getMinSeriesValue() {

		double minimum = Double.NaN;
		for(ISeries<?> series : chart.getSeriesSet().getSeries()) {
			if(series.getYSeries().length == 0) {
				continue;
			}
			double lower;
			if(direction == Direction.X && series.getXAxisId() == getId()) {
				lower = ((Series<?>)series).getXRange().lower;
			} else if(direction == Direction.Y && series.getYAxisId() == getId()) {
				lower = ((Series<?>)series).getYRange().lower;
			} else {
				continue;
			}
			if(Double.isNaN(minimum) || lower < minimum) {
				minimum = lower;
			}
		}
		return minimum;
	}

	@Override
	public boolean isLogScaleEnabled() {

		return logScaleEnabled;
	}

	@Override
	public IGrid getGrid() {

		return grid;
	}

	@Override
	public void adjustRange() {

		adjustRange(true);
	}

	/**
	 * Adjusts the axis range to the series belonging to the axis.
	 * 
	 * @param update
	 *            true if updating chart layout
	 */
	public void adjustRange(boolean update) {

		if(isValidCategoryAxis()) {
			setRange(new Range(0, categorySeries.length - 1));
			return;
		}
		double minimum = Double.NaN;
		double maximum = Double.NaN;
		for(ISeries<?> series : chart.getSeriesSet().getSeries()) {
			int axisId = direction == Direction.X ? series.getXAxisId() : series.getYAxisId();
			if(!series.isVisible() || getId() != axisId) {
				continue;
			}
			// get axis length
			int length;
			if(isHorizontalAxis) {
				length = chart.getPlotArea().getSize().x;
			} else {
				length = chart.getPlotArea().getSize().y;
			}
			// get min and max value of series
			Range range = ((Series<?>)series).getAdjustedRange(this, length);
			if(Double.isNaN(minimum) || range.lower < minimum) {
				minimum = range.lower;
			}
			if(Double.isNaN(maximum) || range.upper > maximum) {
				maximum = range.upper;
			}
		}
		// set adjusted range
		if(!Double.isNaN(minimum) && !Double.isNaN(maximum)) {
			if(minimum == maximum) {
				double margin = (minimum == 0) ? 1d : Math.abs(minimum / 2d);
				minimum -= margin;
				maximum += margin;
			}
			setRange(new Range(minimum, maximum), update);
		}
	}

	@Override
	public void zoomIn() {

		zoomIn((max + min) / 2d);
	}

	@Override
	public void zoomIn(double coordinate) {

		double lower = min;
		double upper = max;
		if(isValidCategoryAxis()) {
			if(lower != upper) {
				if((min + max) / 2d < coordinate) {
					lower = min + 1;
				} else if(coordinate < (min + max) / 2d) {
					upper = max - 1;
				} else {
					lower = min + 1;
					upper = max - 1;
				}
			}
		} else if(isLogScaleEnabled()) {
			double digitMin = Math.log10(min);
			double digitMax = Math.log10(max);
			double digitCoordinate = Math.log10(coordinate);
			lower = Math.pow(10, digitMin + 2 * SCROLL_RATIO * (digitCoordinate - digitMin));
			upper = Math.pow(10, digitMax + 2 * SCROLL_RATIO * (digitCoordinate - digitMax));
		} else {
			lower = min + 2 * ZOOM_RATIO * (coordinate - min);
			upper = max + 2 * ZOOM_RATIO * (coordinate - max);
		}
		setRange(new Range(lower, upper));
	}

	@Override
	public void zoomOut() {

		zoomOut((min + max) / 2d);
	}

	@Override
	public void zoomOut(double coordinate) {

		double lower = min;
		double upper = max;
		if(isValidCategoryAxis()) {
			if((min + max) / 2d < coordinate && min != 0) {
				lower = min - 1;
			} else if(coordinate < (min + max) / 2d && max != categorySeries.length - 1) {
				upper = max + 1;
			} else {
				lower = min - 1;
				upper = max + 1;
			}
		} else if(isLogScaleEnabled()) {
			double digitMin = Math.log10(min);
			double digitMax = Math.log10(max);
			double digitCoordinate = Math.log10(coordinate);
			lower = Math.pow(10, (digitMin - ZOOM_RATIO * digitCoordinate) / (1 - ZOOM_RATIO));
			upper = Math.pow(10, (digitMax - ZOOM_RATIO * digitCoordinate) / (1 - ZOOM_RATIO));
		} else {
			lower = (min - 2 * ZOOM_RATIO * coordinate) / (1 - 2 * ZOOM_RATIO);
			upper = (max - 2 * ZOOM_RATIO * coordinate) / (1 - 2 * ZOOM_RATIO);
		}
		setRange(new Range(lower, upper));
	}

	@Override
	public void scrollUp() {

		double lower = min;
		double upper = max;
		if(isValidCategoryAxis()) {
			if(upper < categorySeries.length - 1) {
				lower = min + 1;
				upper = max + 1;
			}
		} else if(isLogScaleEnabled()) {
			double digitMax = Math.log10(upper);
			double digitMin = Math.log10(lower);
			upper = Math.pow(10, digitMax + (digitMax - digitMin) * SCROLL_RATIO);
			lower = Math.pow(10, digitMin + (digitMax - digitMin) * SCROLL_RATIO);
		} else {
			lower = min + (max - min) * SCROLL_RATIO;
			upper = max + (max - min) * SCROLL_RATIO;
		}
		setRange(new Range(lower, upper));
	}

	@Override
	public void scrollDown() {

		double lower = min;
		double upper = max;
		if(isValidCategoryAxis()) {
			if(lower >= 1) {
				lower = min - 1;
				upper = max - 1;
			}
		} else if(isLogScaleEnabled()) {
			double digitMax = Math.log10(upper);
			double digitMin = Math.log10(lower);
			upper = Math.pow(10, digitMax - (digitMax - digitMin) * SCROLL_RATIO);
			lower = Math.pow(10, digitMin - (digitMax - digitMin) * SCROLL_RATIO);
		} else {
			lower = min - (max - min) * SCROLL_RATIO;
			upper = max - (max - min) * SCROLL_RATIO;
		}
		setRange(new Range(lower, upper));
	}

	@Override
	public boolean isCategoryEnabled() {

		return categoryAxisEnabled;
	}

	/**
	 * Gets the state indicating if the axis is valid category axis.
	 * 
	 * @return true if the axis is valid category axis
	 */
	public boolean isValidCategoryAxis() {

		return categoryAxisEnabled && categorySeries != null && categorySeries.length != 0;
	}

	@Override
	public void enableCategory(boolean enabled) {

		if(categoryAxisEnabled == enabled) {
			return;
		}
		if(enabled) {
			if(direction == Direction.Y) {
				throw new IllegalStateException(Messages.getString(Messages.Y_AXIS_CANNOT_BE_CATEGORY));
			}
			if(categorySeries != null && categorySeries.length != 0) {
				min = (min < 0 || min >= categorySeries.length) ? 0 : (int)min;
				max = (max < 0 || max >= categorySeries.length) ? max = categorySeries.length - 1 : (int)max;
			}
			logScaleEnabled = false;
		}
		categoryAxisEnabled = enabled;
		chart.updateLayout();
		((SeriesSet)chart.getSeriesSet()).updateCompressor(this);
		((SeriesSet)chart.getSeriesSet()).updateStackAndRiserData();
	}

	@Override
	public void setCategorySeries(String[] series) {

		if(series == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
			return; // to suppress warnings...
		}
		if(direction == Direction.Y) {
			throw new IllegalStateException(Messages.getString(Messages.Y_AXIS_CANNOT_BE_CATEGORY));
		}
		String[] copiedSeries = new String[series.length];
		System.arraycopy(series, 0, copiedSeries, 0, series.length);
		categorySeries = copiedSeries;
		if(isValidCategoryAxis()) {
			min = (min < 0) ? 0 : (int)min;
			max = (max >= categorySeries.length) ? max = categorySeries.length - 1 : (int)max;
		}
		chart.updateLayout();
		((SeriesSet)chart.getSeriesSet()).updateCompressor(this);
		((SeriesSet)chart.getSeriesSet()).updateStackAndRiserData();
	}

	@Override
	public String[] getCategorySeries() {

		String[] copiedCategorySeries = null;
		if(categorySeries != null) {
			copiedCategorySeries = new String[categorySeries.length];
			System.arraycopy(categorySeries, 0, copiedCategorySeries, 0, categorySeries.length);
		}
		return copiedCategorySeries;
	}

	@Override
	public void setReversed(boolean reversed) {

		if(this.reversed == reversed) {
			return;
		}
		this.reversed = reversed;
		chart.updateLayout();
	}

	@Override
	public boolean isReversed() {

		return reversed;
	}

	@Override
	public int getPixelCoordinate(double dataCoordinate) {

		return getPixelCoordinate(dataCoordinate, min, max);
	}

	/**
	 * Gets the pixel coordinate corresponding to the given data coordinate.
	 * 
	 * @param dataCoordinate
	 *            the data coordinate
	 * @param lower
	 *            the min value of range
	 * @param upper
	 *            the max value of range
	 * @return the pixel coordinate on plot area
	 */
	public int getPixelCoordinate(double dataCoordinate, double lower, double upper) {

		int pixelCoordinate;
		if(isReversed()) {
			if(isHorizontalAxis) {
				if(logScaleEnabled) {
					pixelCoordinate = (int)((Math.log10(upper) - Math.log10(dataCoordinate)) / (Math.log10(upper) - Math.log10(lower)) * width);
				} else if(categoryAxisEnabled) {
					pixelCoordinate = (int)((upper - dataCoordinate + 0.5) / (upper + 1 - lower) * width);
				} else {
					pixelCoordinate = (int)((upper - dataCoordinate) / (upper - lower) * width);
				}
			} else {
				if(logScaleEnabled) {
					pixelCoordinate = (int)((Math.log10(dataCoordinate) - Math.log10(lower)) / (Math.log10(upper) - Math.log10(lower)) * height);
				} else if(categoryAxisEnabled) {
					pixelCoordinate = (int)((dataCoordinate + 0.5 - lower) / (upper + 1 - lower) * height);
				} else {
					pixelCoordinate = (int)((dataCoordinate - lower) / (upper - lower) * height);
				}
			}
		} else {
			if(isHorizontalAxis) {
				if(logScaleEnabled) {
					pixelCoordinate = (int)((Math.log10(dataCoordinate) - Math.log10(lower)) / (Math.log10(upper) - Math.log10(lower)) * width);
				} else if(categoryAxisEnabled) {
					pixelCoordinate = (int)((dataCoordinate + 0.5 - lower) / (upper + 1 - lower) * width);
				} else {
					pixelCoordinate = (int)((dataCoordinate - lower) / (upper - lower) * width);
				}
			} else {
				if(logScaleEnabled) {
					pixelCoordinate = (int)((Math.log10(upper) - Math.log10(dataCoordinate)) / (Math.log10(upper) - Math.log10(lower)) * height);
				} else if(categoryAxisEnabled) {
					pixelCoordinate = (int)((upper - dataCoordinate + 0.5) / (upper + 1 - lower) * height);
				} else {
					pixelCoordinate = (int)((upper - dataCoordinate) / (upper - lower) * height);
				}
			}
		}
		return pixelCoordinate;
	}

	@Override
	public double getDataCoordinate(int pixelCoordinate) {

		return getDataCoordinate(pixelCoordinate, min, max);
	}

	/**
	 * Gets the data coordinate corresponding to the given pixel coordinate on
	 * plot area.
	 * 
	 * @param pixelCoordinate
	 *            the pixel coordinate on plot area
	 * @param lower
	 *            the min value of range
	 * @param upper
	 *            the max value of range
	 * @return the data coordinate
	 */
	public double getDataCoordinate(int pixelCoordinate, double lower, double upper) {

		double dataCoordinate;
		if(isReversed()) {
			if(isHorizontalAxis) {
				if(logScaleEnabled) {
					dataCoordinate = Math.pow(10, Math.log10(upper) - pixelCoordinate / (double)width * (Math.log10(upper) - Math.log10(lower)) + Math.log10(lower));
				} else if(categoryAxisEnabled) {
					dataCoordinate = Math.floor(upper + 1 - pixelCoordinate / (double)width * (upper + 1 - lower) + lower);
				} else {
					dataCoordinate = (width - pixelCoordinate) / (double)width * (upper - lower) + lower;
				}
			} else {
				if(logScaleEnabled) {
					dataCoordinate = Math.pow(10, pixelCoordinate / (double)height * (Math.log10(upper) - Math.log10(lower)));
				} else if(categoryAxisEnabled) {
					dataCoordinate = Math.floor(pixelCoordinate / (double)height * (upper + 1 - lower));
				} else {
					dataCoordinate = pixelCoordinate / (double)height * (upper - lower) + lower;
				}
			}
		} else {
			if(isHorizontalAxis) {
				if(logScaleEnabled) {
					dataCoordinate = Math.pow(10, pixelCoordinate / (double)width * (Math.log10(upper) - Math.log10(lower)) + Math.log10(lower));
				} else if(categoryAxisEnabled) {
					dataCoordinate = Math.floor(pixelCoordinate / (double)width * (upper + 1 - lower) + lower);
				} else {
					dataCoordinate = pixelCoordinate / (double)width * (upper - lower) + lower;
				}
			} else {
				if(logScaleEnabled) {
					dataCoordinate = Math.pow(10, Math.log10(upper) - pixelCoordinate / (double)height * (Math.log10(upper) - Math.log10(lower)));
				} else if(categoryAxisEnabled) {
					dataCoordinate = Math.floor(upper + 1 - pixelCoordinate / (double)height * (upper + 1 - lower));
				} else {
					dataCoordinate = (height - pixelCoordinate) / (double)height * (upper - lower) + lower;
				}
			}
		}
		return dataCoordinate;
	}

	/**
	 * Sets the number of risers per category.
	 * 
	 * @param numRisers
	 *            the number of risers per category
	 */
	public void setNumRisers(int numRisers) {

		this.numRisers = numRisers;
	}

	/**
	 * Gets the number of risers per category.
	 * 
	 * @return number of riser per category
	 */
	public int getNumRisers() {

		return numRisers;
	}

	/**
	 * Checks if the axis is horizontal. X axis is not always horizontal. Y axis
	 * can be horizontal with <tt>Chart.setOrientation(SWT.VERTICAL)</tt>.
	 * 
	 * @return true if the axis is horizontal
	 */
	public boolean isHorizontalAxis() {

		int orientation = chart.getOrientation();
		return (direction == Direction.X && orientation == SWT.HORIZONTAL) || (direction == Direction.Y && orientation == SWT.VERTICAL);
	}

	/**
	 * Disposes the resources.
	 */
	protected void dispose() {

		tick.getAxisTickLabels().dispose();
		tick.getAxisTickMarks().dispose();
		title.dispose();
		for(IDisposeListener listener : listeners) {
			listener.disposed(new Event());
		}
	}

	@Override
	public void addDisposeListener(IDisposeListener listener) {

		listeners.add(listener);
	}

	/**
	 * Updates the layout data.
	 */
	public void updateLayoutData() {

		title.updateLayoutData();
		tick.updateLayoutData();
	}

	/**
	 * Refreshes the cache.
	 */
	public void refresh() {

		int orientation = chart.getOrientation();
		isHorizontalAxis = (direction == Direction.X && orientation == SWT.HORIZONTAL) || (direction == Direction.Y && orientation == SWT.VERTICAL);
		if(width != chart.getPlotArea().getSize().x) {
			width = chart.getPlotArea().getSize().x;
			if(isHorizontalAxis) {
				tick.updateTick(width);
			}
		}
		if(height != chart.getPlotArea().getSize().y) {
			height = chart.getPlotArea().getSize().y;
			if(!isHorizontalAxis) {
				tick.updateTick(height);
			}
		}
	}
}
