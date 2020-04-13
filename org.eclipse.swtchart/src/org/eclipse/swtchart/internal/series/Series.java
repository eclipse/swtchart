/*******************************************************************************
 * Copyright (c) 2008, 2020 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 * Christoph Läubrich - add support for datamodel
 * Frank Buloup = Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.internal.series;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.IDisposeListener;
import org.eclipse.swtchart.IErrorBar;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesLabel;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.internal.compress.ICompress;
import org.eclipse.swtchart.model.CartesianSeriesModel;
import org.eclipse.swtchart.model.DateArraySeriesModel;
import org.eclipse.swtchart.model.DoubleArraySeriesModel;
import org.eclipse.swtchart.model.IndexedSeriesModel;

/**
 * Series.
 */
abstract public class Series<T> implements ISeries<T> {

	/** the default series type */
	protected static final SeriesType DEFAULT_SERIES_TYPE = SeriesType.LINE;
	/** the minimum value of x series */
	protected String id;
	/** the compressor */
	protected ICompress compressor;
	/** the x axis id */
	protected int xAxisId;
	/** the y axis id */
	protected int yAxisId;
	/** the visibility of series */
	protected boolean visible;
	/** the visibility buffer status of series */
	protected boolean visibleBufferStatus;
	/** the series type */
	protected SeriesType type;
	/** the series label */
	protected SeriesLabel seriesLabel;
	/** the x error bar */
	protected ErrorBar xErrorBar;
	/** the y error bar */
	protected ErrorBar yErrorBar;
	/** the chart */
	protected Chart chart;
	/** the state indicating if the series is a stacked type */
	protected boolean stackEnabled;
	/** the stack series */
	protected double[] stackSeries;
	/** the state indicating if the series is visible in legend */
	private boolean visibleInLegend;
	/** the series description */
	private String description;
	/** the list of dispose listeners */
	private List<IDisposeListener> listeners;
	private CartesianSeriesModel<T> model;

	/**
	 * Constructor.
	 *
	 * @param chart
	 *            the chart
	 * @param id
	 *            the series id
	 */
	protected Series(Chart chart, String id) {

		super();
		this.chart = chart;
		this.id = id;
		xAxisId = 0;
		yAxisId = 0;
		visible = true;
		visibleBufferStatus = false;
		type = DEFAULT_SERIES_TYPE;
		stackEnabled = false;
		// isXMonotoneIncreasing = true;
		seriesLabel = new SeriesLabel();
		xErrorBar = new ErrorBar();
		yErrorBar = new ErrorBar();
		visibleInLegend = true;
		listeners = new ArrayList<IDisposeListener>();
	}

	@Override
	public String getId() {

		return id;
	}

	@Override
	public void setVisible(boolean visible) {

		if(this.visible == visible) {
			return;
		}
		this.visible = visible;
		((SeriesSet)chart.getSeriesSet()).updateStackAndRiserData();
	}

	@Override
	public void setVisibleBuffered(boolean visible) {

		visibleBufferStatus = visible;
	}

	@Override
	public boolean isVisible() {

		return visible;
	}

	@Override
	public boolean isVisibleBuffered() {

		return visibleBufferStatus;
	}

	@Override
	public SeriesType getType() {

		return type;
	}

	@Override
	public boolean isStackEnabled() {

		return stackEnabled;
	}

	@Override
	public CartesianSeriesModel<T> getDataModel() {

		return model;
	}

	@Override
	public void setDataModel(CartesianSeriesModel<T> model) {

		this.model = model;
		setCompressor();
		compressor.setXSeries(getXSeries());
		compressor.setYSeries(getYSeries());
		Range xRange = getXRange();
		if(xRange.lower <= 0) {
			IAxis axis = chart.getAxisSet().getXAxis(xAxisId);
			if(axis != null) {
				axis.enableLogScale(false);
			}
		}
		Range yRange = getYRange();
		if(yRange.lower <= 0) {
			IAxis axis = chart.getAxisSet().getYAxis(yAxisId);
			if(axis != null) {
				axis.enableLogScale(false);
			}
			stackEnabled = false;
		}
	}

	@Override
	public void enableStack(boolean enabled) {

		Number minY = getDataModel().getMinY();
		if(enabled && (minY != null && minY.doubleValue() < 0)) {
			throw new IllegalStateException(Messages.getString(Messages.STACKED_SERIES_CANT_CONTAIN_NEGATIVE_VALUES));
		}
		if(stackEnabled == enabled) {
			return;
		}
		stackEnabled = enabled;
		((SeriesSet)chart.getSeriesSet()).updateStackAndRiserData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setXSeries(double[] series) {

		if(series == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
			return; // to suppress warning...
		}
		double[] xSeries = new double[series.length];
		System.arraycopy(series, 0, xSeries, 0, series.length);
		double[] ySeries = getYSeries();
		if(ySeries.length != xSeries.length) {
			ySeries = new double[xSeries.length];
		}
		DoubleArraySeriesModel arraySeriesModel = new DoubleArraySeriesModel(xSeries, ySeries);
		setDataModel((CartesianSeriesModel<T>)arraySeriesModel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setXDateSeries(Date[] series) {

		double[] ySeries = getYSeries();
		if(ySeries.length != series.length) {
			ySeries = new double[series.length];
		}
		setDataModel((CartesianSeriesModel<T>)new DateArraySeriesModel(series, ySeries));
	}

	@Override
	public Date[] getXDateSeries() {

		CartesianSeriesModel<T> dataModel = getDataModel();
		if(dataModel == null) {
			return new Date[0];
		}
		return StreamSupport.stream(dataModel.spliterator(), false).filter(t -> dataModel.getX(t) != null).map(value -> new Date(dataModel.getX(value).longValue())).toArray(Date[]::new);
	}

	@Override
	public double[] getXSeries() {

		CartesianSeriesModel<T> dataModel = getDataModel();
		if(dataModel == null) {
			return new double[0];
		}
		return StreamSupport.stream(dataModel.spliterator(), false).filter(t -> dataModel.getX(t) != null).mapToDouble(value -> dataModel.getX(value).doubleValue()).toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setYSeries(double[] series) {

		if(series == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
			return; // to suppress warning...
		}
		double[] xSeries = getXSeries();
		double[] ySeries = new double[series.length];
		System.arraycopy(series, 0, ySeries, 0, series.length);
		if(ySeries.length != xSeries.length) {
			xSeries = new double[ySeries.length];
			for(int i = 0; i < xSeries.length; i++) {
				xSeries[i] = i;
			}
		}
		DoubleArraySeriesModel arraySeriesModel = new DoubleArraySeriesModel(xSeries, ySeries);
		setDataModel((CartesianSeriesModel<T>)arraySeriesModel);
	}

	@Override
	public double[] getYSeries() {

		CartesianSeriesModel<T> dataModel = getDataModel();
		if(dataModel == null) {
			return new double[0];
		}
		return StreamSupport.stream(dataModel.spliterator(), false).filter(t -> dataModel.getY(t) != null).mapToDouble(value -> dataModel.getY(value).doubleValue()).toArray();
	}

	/**
	 * Gets the state indicating if the series is valid stack series.
	 *
	 * @return true if the series is valid stack series
	 */
	public boolean isValidStackSeries() {

		return stackEnabled && stackSeries != null && stackSeries.length > 0 && !chart.getAxisSet().getYAxis(yAxisId).isLogScaleEnabled() && ((Axis)chart.getAxisSet().getXAxis(xAxisId)).isValidCategoryAxis();
	}

	/**
	 * Gets the X range of series.
	 *
	 * @return the X range of series
	 */
	public Range getXRange() {

		double minX = 0;
		double maxX = 0;
		CartesianSeriesModel<T> dataModel = getDataModel();
		if(dataModel != null) {
			Number number = dataModel.getMinX();
			minX = number == null ? 0 : number.doubleValue();
			number = dataModel.getMaxX();
			maxX = number == null ? 0 : number.doubleValue();
		}
		return new Range(minX, maxX);
	}

	/**
	 * Gets the adjusted range to show all series in screen. This range includes
	 * the size of plot like symbol or bar.
	 *
	 * @param axis
	 *            the axis
	 * @param length
	 *            the axis length in pixels
	 * @return the adjusted range
	 */
	abstract public Range getAdjustedRange(Axis axis, int length);

	/**
	 * Gets the Y range of series.
	 *
	 * @return the Y range of series
	 */
	public Range getYRange() {

		double min = 0;
		double max = 0;
		CartesianSeriesModel<T> dataModel = getDataModel();
		if(dataModel != null) {
			Number number = dataModel.getMinY();
			min = number == null ? 0 : number.doubleValue();
			number = dataModel.getMaxY();
			max = number == null ? 0 : number.doubleValue();
		}
		Axis xAxis = (Axis)chart.getAxisSet().getXAxis(xAxisId);
		if(isValidStackSeries() && xAxis.isValidCategoryAxis()) {
			for(int i = 0; i < stackSeries.length; i++) {
				if(max < stackSeries[i]) {
					max = stackSeries[i];
				}
			}
		}
		return new Range(min, max);
	}

	/**
	 * Gets the compressor.
	 *
	 * @return the compressor
	 */
	protected ICompress getCompressor() {

		return compressor;
	}

	/**
	 * Sets the compressor.
	 */
	abstract protected void setCompressor();

	@Override
	public int getXAxisId() {

		return xAxisId;
	}

	@Override
	public void setXAxisId(int id) {

		if(xAxisId == id) {
			return;
		}
		IAxis axis = chart.getAxisSet().getXAxis(xAxisId);
		if(getXRange().lower <= 0 && axis != null && axis.isLogScaleEnabled()) {
			chart.getAxisSet().getXAxis(xAxisId).enableLogScale(false);
		}
		xAxisId = id;
		((SeriesSet)chart.getSeriesSet()).updateStackAndRiserData();
	}

	@Override
	public int getYAxisId() {

		return yAxisId;
	}

	@Override
	public void setYAxisId(int id) {

		yAxisId = id;
	}

	@Override
	public ISeriesLabel getLabel() {

		return seriesLabel;
	}

	@Override
	public IErrorBar getXErrorBar() {

		return xErrorBar;
	}

	@Override
	public IErrorBar getYErrorBar() {

		return yErrorBar;
	}

	/**
	 * Sets the stack series
	 *
	 * @param stackSeries
	 *            The stack series
	 */
	protected void setStackSeries(double[] stackSeries) {

		this.stackSeries = stackSeries;
	}

	@Override
	public Point getPixelCoordinates(int index) {

		// get the horizontal and vertical axes
		IAxis hAxis;
		IAxis vAxis;
		if(chart.getOrientation() == SWT.HORIZONTAL) {
			hAxis = chart.getAxisSet().getXAxis(xAxisId);
			vAxis = chart.getAxisSet().getYAxis(yAxisId);
		} else if(chart.getOrientation() == SWT.VERTICAL) {
			hAxis = chart.getAxisSet().getYAxis(yAxisId);
			vAxis = chart.getAxisSet().getXAxis(xAxisId);
		} else {
			throw new IllegalStateException("unknown chart orientation"); //$NON-NLS-1$
		}
		// get the pixel coordinates
		return new Point(getPixelCoordinate(hAxis, index), getPixelCoordinate(vAxis, index));
	}

	/**
	 * Gets the pixel coordinates with given axis and series index.
	 *
	 * @param axis
	 *            the axis
	 * @param index
	 *            the series index
	 * @return the pixel coordinates
	 */
	private int getPixelCoordinate(IAxis axis, int index) {

		CartesianSeriesModel<T> dataModel = getDataModel();
		if(dataModel instanceof IndexedSeriesModel<?>) {
			@SuppressWarnings("unchecked")
			IndexedSeriesModel<T> indexedModel = (IndexedSeriesModel<T>)dataModel;
			// get the data coordinate
			double dataCoordinate;
			if(axis.getDirection() == Direction.X) {
				if(axis.isCategoryEnabled()) {
					dataCoordinate = index;
				} else {
					if(index < 0 || indexedModel.size() <= index) {
						throw new IllegalArgumentException("Series index is out of range."); //$NON-NLS-1$
					}
					dataCoordinate = dataModel.getX(indexedModel.itemAt(index)).doubleValue();
				}
			} else if(axis.getDirection() == Direction.Y) {
				if(isValidStackSeries()) {
					if(index < 0 || stackSeries.length <= index) {
						throw new IllegalArgumentException("Series index is out of range."); //$NON-NLS-1$
					}
					dataCoordinate = stackSeries[index];
				} else {
					if(index < 0 || indexedModel.size() <= index) {
						throw new IllegalArgumentException("Series index is out of range."); //$NON-NLS-1$
					}
					dataCoordinate = dataModel.getY(indexedModel.itemAt(index)).doubleValue();
				}
			} else {
				throw new IllegalStateException("unknown axis direction"); //$NON-NLS-1$
			}
			// get the pixel coordinate
			return axis.getPixelCoordinate(dataCoordinate);
		} else {
			throw new IllegalStateException(Messages.getString(Messages.REQUIRES_INDEXED_SERIES_MODEL));
		}
	}

	/**
	 * Gets the range with given margin.
	 *
	 * @param lowerPlotMargin
	 *            the lower margin in pixels
	 * @param upperPlotMargin
	 *            the upper margin in pixels
	 * @param length
	 *            the axis length in pixels
	 * @param axis
	 *            the axis
	 * @param range
	 *            the range
	 * @return the range with margin
	 */
	protected Range getRangeWithMargin(int lowerPlotMargin, int upperPlotMargin, int length, Axis axis, Range range) {

		if(length == 0) {
			return range;
		}
		int lowerPixelCoordinate = axis.getPixelCoordinate(range.lower, range.lower, range.upper) + lowerPlotMargin * (axis.isHorizontalAxis() && !axis.isReversed() ? -1 : 1);
		int upperPixelCoordinate = axis.getPixelCoordinate(range.upper, range.lower, range.upper) + upperPlotMargin * (axis.isHorizontalAxis() && !axis.isReversed() ? 1 : -1);
		double lower = axis.getDataCoordinate(lowerPixelCoordinate, range.lower, range.upper);
		double upper = axis.getDataCoordinate(upperPixelCoordinate, range.lower, range.upper);
		return new Range(lower, upper);
	}

	@Override
	public void setVisibleInLegend(boolean visible) {

		visibleInLegend = visible;
	}

	@Override
	public boolean isVisibleInLegend() {

		return visibleInLegend;
	}

	@Override
	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public String getDescription() {

		return description;
	}

	/**
	 * Disposes SWT resources.
	 */
	protected void dispose() {

		for(IDisposeListener listener : listeners) {
			listener.disposed(new Event());
		}
	}

	@Override
	public void addDisposeListener(IDisposeListener listener) {

		listeners.add(listener);
	}

	/**
	 * Draws series.
	 *
	 * @param gc
	 *            the graphics context
	 * @param width
	 *            the width to draw series
	 * @param height
	 *            the height to draw series
	 */
	public void draw(GC gc, int width, int height) {

		if(!visible || width < 0 || height < 0) {
			return;
		}
		Axis xAxis = (Axis)chart.getAxisSet().getXAxis(getXAxisId());
		Axis yAxis = (Axis)chart.getAxisSet().getYAxis(getYAxisId());
		if(xAxis == null || yAxis == null) {
			return;
		}
		draw(gc, width, height, xAxis, yAxis);
	}

	/**
	 * Draws series.
	 *
	 * @param gc
	 *            the graphics context
	 * @param width
	 *            the width to draw series
	 * @param height
	 *            the height to draw series
	 * @param xAxis
	 *            the x axis
	 * @param yAxis
	 *            the y axis
	 */
	abstract protected void draw(GC gc, int width, int height, Axis xAxis, Axis yAxis);
}
