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
 * Christoph Läubrich - use getSize since we only want width/height, add support for datamodel
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.internal.axis;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.internal.ChartLayoutData;
import org.eclipse.swtchart.internal.Util;

/**
 * Axis tick labels.
 */
public class AxisTickLabels implements PaintListener {

	/** the chart */
	private final Chart chart;
	/** the axis */
	private final Axis axis;
	/** the foreground color */
	private Color foreground;
	/** the width hint of tick labels area */
	private int widthHint;
	/** the height hint of tick labels area */
	private int heightHint;
	/** the bounds of tick labels area */
	private Rectangle bounds;
	/** the array of tick label vales */
	private final ArrayList<Double> tickLabelValues;
	/** the array of tick label */
	private final ArrayList<String> tickLabels;
	/** the array of tick label position in pixels */
	private final ArrayList<Integer> tickLabelPositions;
	/** the array of visibility state of tick label */
	private final ArrayList<Boolean> tickVisibilities;
	/** the format for tick labels */
	private Format format;
	/** the default foreground */
	private static final int DEFAULT_FOREGROUND = SWT.COLOR_BLUE;
	/** the default font */
	private static final Font DEFAULT_FONT = Display.getDefault().getSystemFont();
	/** the default label format */
	private static final String DEFAULT_DECIMAL_FORMAT = "#.###########"; //$NON-NLS-1$
	/** the possible tick steps */
	private Map<Integer, Integer[]> possibleTickSteps;
	/** the font */
	private Font font;

	/**
	 * Constructor.
	 * 
	 * @param chart
	 *            the chart
	 * @param axis
	 *            the axis
	 */
	protected AxisTickLabels(Chart chart, Axis axis) {

		this.chart = chart;
		this.axis = axis;
		tickLabelValues = new ArrayList<Double>();
		tickLabels = new ArrayList<String>();
		tickLabelPositions = new ArrayList<Integer>();
		tickVisibilities = new ArrayList<Boolean>();
		initializePossibleTickSteps();
		font = DEFAULT_FONT;
		foreground = Display.getDefault().getSystemColor(DEFAULT_FOREGROUND);
		chart.addPaintListener(this);
	}

	/**
	 * Initialized the possible tick steps.
	 */
	private void initializePossibleTickSteps() {

		final Integer[] milliseconds = {1, 2, 5, 10, 20, 50, 100, 200, 500, 999};
		final Integer[] seconds = {1, 2, 5, 10, 15, 20, 30, 59};
		final Integer[] minutes = {1, 2, 3, 5, 10, 15, 20, 30, 59};
		final Integer[] hours = {1, 2, 3, 4, 6, 12, 22};
		final Integer[] dates = {1, 7, 14, 28};
		final Integer[] months = {1, 2, 3, 4, 6, 11};
		final Integer[] years = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000};
		possibleTickSteps = new HashMap<Integer, Integer[]>();
		possibleTickSteps.put(Calendar.MILLISECOND, milliseconds);
		possibleTickSteps.put(Calendar.SECOND, seconds);
		possibleTickSteps.put(Calendar.MINUTE, minutes);
		possibleTickSteps.put(Calendar.HOUR_OF_DAY, hours);
		possibleTickSteps.put(Calendar.DATE, dates);
		possibleTickSteps.put(Calendar.MONTH, months);
		possibleTickSteps.put(Calendar.YEAR, years);
	}

	/**
	 * Sets the foreground color.
	 * 
	 * @param color
	 *            the foreground color
	 */
	public void setForeground(Color color) {

		if(color == null) {
			foreground = Display.getDefault().getSystemColor(DEFAULT_FOREGROUND);
		} else {
			foreground = color;
		}
	}

	/**
	 * Gets the foreground color.
	 * 
	 * @return the foreground color
	 */
	protected Color getForeground() {

		if(foreground.isDisposed()) {
			foreground = Display.getDefault().getSystemColor(DEFAULT_FOREGROUND);
		}
		return foreground;
	}

	/**
	 * Updates the tick labels.
	 * 
	 * @param length
	 *            the axis length
	 */
	protected void update(int length) {

		tickLabelValues.clear();
		tickLabels.clear();
		tickLabelPositions.clear();
		if(axis.isValidCategoryAxis()) {
			updateTickLabelForCategoryAxis(length);
		} else if(axis.isLogScaleEnabled()) {
			updateTickLabelForLogScale(length);
		} else {
			updateTickLabelForLinearScale(length);
		}
		updateTickVisibility();
	}

	/**
	 * Updates tick label for category axis.
	 * 
	 * @param length
	 *            the length of axis
	 */
	private void updateTickLabelForCategoryAxis(int length) {

		String[] series = axis.getCategorySeries();
		if(series == null) {
			return;
		}
		int min = (int)axis.getRange().lower;
		int max = (int)axis.getRange().upper;
		int sizeOfTickLabels = (series.length < max - min + 1) ? series.length : max - min + 1;
		int initialIndex = (min < 0) ? 0 : min;
		for(int i = 0; i < sizeOfTickLabels; i++) {
			tickLabels.add(series[i + initialIndex]);
			int tickLabelPosition = (int)(length * (i + 0.5) / sizeOfTickLabels);
			if(axis.isReversed()) {
				tickLabelPosition = correctPositionInReversedAxis(tickLabelPosition);
			}
			tickLabelPositions.add(tickLabelPosition);
		}
	}

	/**
	 * Updates tick label for log scale.
	 * 
	 * @param length
	 *            the length of axis
	 */
	private void updateTickLabelForLogScale(int length) {

		double min = axis.getRange().lower;
		double max = axis.getRange().upper;
		int digitMin = (int)Math.ceil(Math.log10(min));
		int digitMax = (int)Math.ceil(Math.log10(max));
		final BigDecimal MIN = BigDecimal.valueOf(min);
		BigDecimal tickStep = pow(10, digitMin - 1);
		BigDecimal firstPosition;
		if(MIN.remainder(tickStep).doubleValue() <= 0) {
			firstPosition = MIN.subtract(MIN.remainder(tickStep));
		} else {
			firstPosition = MIN.subtract(MIN.remainder(tickStep)).add(tickStep);
		}
		for(int i = digitMin; i <= digitMax; i++) {
			for(BigDecimal j = firstPosition; j.doubleValue() <= pow(10, i).doubleValue(); j = j.add(tickStep)) {
				if(j.doubleValue() > max) {
					break;
				}
				tickLabels.add(format(j.doubleValue()));
				tickLabelValues.add(j.doubleValue());
				int tickLabelPosition = (int)((Math.log10(j.doubleValue()) - Math.log10(min)) / (Math.log10(max) - Math.log10(min)) * length);
				if(axis.isReversed()) {
					tickLabelPosition = correctPositionInReversedAxis(tickLabelPosition);
				}
				tickLabelPositions.add(tickLabelPosition);
			}
			tickStep = tickStep.multiply(pow(10, 1));
			firstPosition = tickStep.add(pow(10, i));
		}
	}

	/**
	 * Updates tick label for normal scale.
	 * 
	 * @param length
	 *            axis length (>0)
	 */
	private void updateTickLabelForLinearScale(int length) {

		double min = axis.getRange().lower;
		double max = axis.getRange().upper;
		updateTickLabelForLinearScale(length, getGridStep(length, min, max));
	}

	/**
	 * Updates tick label for normal scale.
	 * 
	 * @param length
	 *            axis length (>0)
	 * @param tickStep
	 *            the tick step
	 */
	private void updateTickLabelForLinearScale(int length, BigDecimal tickStep) {

		double min = axis.getRange().lower;
		double max = axis.getRange().upper;
		final BigDecimal MIN = BigDecimal.valueOf(min);
		BigDecimal firstPosition;
		/* if (min % tickStep <= 0) */
		if(MIN.remainder(tickStep).doubleValue() <= 0) {
			/* firstPosition = min - min % tickStep */
			firstPosition = MIN.subtract(MIN.remainder(tickStep));
		} else {
			/* firstPosition = min - min % tickStep + tickStep */
			firstPosition = MIN.subtract(MIN.remainder(tickStep)).add(tickStep);
		}
		for(BigDecimal b = firstPosition; b.doubleValue() <= max; b = b.add(tickStep)) {
			tickLabels.add(format(b.doubleValue()));
			tickLabelValues.add(b.doubleValue());
			int tickLabelPosition = (int)((b.doubleValue() - min) / (max - min) * length);
			if(axis.isReversed()) {
				tickLabelPosition = correctPositionInReversedAxis(tickLabelPosition);
			}
			tickLabelPositions.add(tickLabelPosition);
		}
	}

	private int correctPositionInReversedAxis(int position) {

		Point plotAreaBounds = chart.getPlotArea().getSize();
		if(axis.isHorizontalAxis()) {
			return plotAreaBounds.x - position - 1;
		}
		return plotAreaBounds.y - position - 1;
	}

	/**
	 * Updates the visibility of tick labels.
	 */
	private void updateTickVisibility() {

		// initialize the array of tick label visibility state
		tickVisibilities.clear();
		for(int i = 0; i < tickLabelPositions.size(); i++) {
			tickVisibilities.add(Boolean.TRUE);
		}
		if(tickLabelPositions.size() == 0 || axis.getTick().getTickLabelAngle() != 0) {
			return;
		}
		// set the tick label visibility
		int previousPosition = 0;
		for(int i = 0; i < tickLabelPositions.size(); i++) {
			// check if there is enough space to draw tick label
			boolean hasSpaceToDraw = true;
			if(i != 0) {
				hasSpaceToDraw = hasSpaceToDraw(previousPosition, tickLabelPositions.get(i), tickLabels.get(i));
			}
			// check if the tick label value is major
			boolean isMajorTick = true;
			if(!axis.isValidCategoryAxis()) {
				if(axis.isLogScaleEnabled()) {
					isMajorTick = isMajorTick(tickLabelValues.get(i));
				}
				// check if the same tick label is repeated
				String currentLabel = tickLabels.get(i);
				try {
					/*
					 * Check if the value is close to the tick label, then it is a major tick
					 * Patch by MatthewKhouzam
					 * https://github.com/eclipse/swtchart/pull/215/commits/b8214bd422205386e5470af2498dbd8227f87d8c
					 */
					double value = parse(currentLabel);
					double diff = Math.abs((value - tickLabelValues.get(i)) / value);
					double maximumDelta = 0.01;
					isMajorTick = (diff <= maximumDelta);
				} catch(ParseException e) {
					// label is not decimal value but string
				}
			}
			if(hasSpaceToDraw && isMajorTick) {
				previousPosition = tickLabelPositions.get(i);
			} else {
				tickVisibilities.set(i, Boolean.FALSE);
			}
		}
	}

	/**
	 * Formats the given object.
	 * 
	 * @param obj
	 *            the object
	 * @return the formatted string
	 */
	private String format(Object obj) {

		if(format == null) {
			return new DecimalFormat(DEFAULT_DECIMAL_FORMAT).format(obj);
		}
		return format.format(obj);
	}

	private double parse(String label) throws ParseException {

		if(format == null) {
			return new DecimalFormat(DEFAULT_DECIMAL_FORMAT).parse(label).doubleValue();
		}
		Object parsed = format.parseObject(label);
		if(!(parsed instanceof Number))
			throw new ParseException(label, 0);
		return ((Number)parsed).doubleValue();
	}

	/**
	 * Checks if the tick label is major (...,0.01,0.1,1,10,100,...).
	 * 
	 * @param tickValue
	 *            the tick label value
	 * @return true if the tick label is major
	 */
	private boolean isMajorTick(double tickValue) {

		if(!axis.isLogScaleEnabled()) {
			return true;
		}
		if(Math.log10(tickValue) % 1 == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the state indicating if there is a space to draw tick label.
	 * 
	 * @param previousPosition
	 *            the previously drawn tick label position.
	 * @param tickLabelPosition
	 *            the tick label position.
	 * @param tickLabel
	 *            the tick label text
	 * @return true if there is a space to draw tick label
	 */
	private boolean hasSpaceToDraw(int previousPosition, int tickLabelPosition, String tickLabel) {

		Point p = Util.getExtentInGC(axis.getTick().getFont(), tickLabel);
		int interval = Math.abs(tickLabelPosition - previousPosition);
		int textLength = axis.isHorizontalAxis() ? p.x : p.y;
		int padding = 3;
		return interval > textLength + padding;
	}

	/**
	 * Gets the right margin hint.
	 * 
	 * @param length
	 *            the axis length
	 * @return the right margin hint
	 */
	public int getRightMarginHint(int length) {

		// search the most right tick label
		int mostRightLabelIndex = -1;
		for(int i = tickLabels.size() - 1; i >= 0; i--) {
			if(tickVisibilities.size() > i && tickVisibilities.get(i)) {
				mostRightLabelIndex = i;
				break;
			}
		}
		// calculate right margin hint
		int rightMarginHint = 0;
		if(mostRightLabelIndex != -1) {
			int position = tickLabelPositions.get(mostRightLabelIndex);
			double angle = axis.getTick().getTickLabelAngle();
			int textWidth = Util.getExtentInGC(axis.getTick().getFont(), tickLabels.get(mostRightLabelIndex)).x;
			if(angle == 0) {
				rightMarginHint = Math.max(0, position - length + (int)(textWidth / 2d));
			} else if(axis.getPosition() == Position.Secondary) {
				rightMarginHint = Math.max(0, position - length + (int)(textWidth * Math.cos(Math.toRadians(angle))));
			}
		}
		return rightMarginHint;
	}

	/**
	 * Updates the left margin hint.
	 * 
	 * @param length
	 *            the axis length
	 * @return the left margin hint
	 */
	public int getLeftMarginHint(int length) {

		// search the most left tick label
		int mostLeftLabelIndex = -1;
		for(int i = 0; i < tickLabels.size(); i++) {
			if(tickVisibilities.size() > i && tickVisibilities.get(i)) {
				mostLeftLabelIndex = i;
				break;
			}
		}
		// calculate left margin hint
		int leftMarginHint = 0;
		if(mostLeftLabelIndex != -1) {
			int position = tickLabelPositions.get(mostLeftLabelIndex);
			double angle = axis.getTick().getTickLabelAngle();
			int textWidth = Util.getExtentInGC(axis.getTick().getFont(), tickLabels.get(mostLeftLabelIndex)).x;
			if(angle == 0) {
				leftMarginHint = Math.max(0, (int)(textWidth / 2d) - position);
			} else if(axis.getPosition() == Position.Primary) {
				leftMarginHint = Math.max(0, (int)(textWidth * Math.cos(Math.toRadians(angle))) - position);
			}
		}
		return leftMarginHint;
	}

	/**
	 * Gets the max length of tick label.
	 * 
	 * @return the max length of tick label
	 */
	public int getTickLabelMaxLength() {

		int maxLength = 0;
		for(int i = 0; i < tickLabels.size(); i++) {
			if(tickVisibilities.size() > i && tickVisibilities.get(i) == true) {
				Point p = Util.getExtentInGC(axis.getTick().getFont(), tickLabels.get(i));
				if(p.x > maxLength) {
					maxLength = p.x;
				}
			}
		}
		return maxLength;
	}

	/**
	 * Calculates the value of the first argument raised to the power of the
	 * second argument.
	 * 
	 * @param base
	 *            the base
	 * @param exponent
	 *            the exponent
	 * @return the value <tt>a<sup>b</sup></tt> in <tt>BigDecimal</tt>
	 */
	private static BigDecimal pow(double base, int exponent) {

		BigDecimal value;
		if(exponent > 0) {
			value = BigDecimal.valueOf(base).pow(exponent);
		} else {
			value = BigDecimal.ONE.divide(BigDecimal.valueOf(base).pow(-exponent));
		}
		return value;
	}

	/**
	 * Gets the grid step.
	 * 
	 * @param lengthInPixels
	 *            axis length in pixels
	 * @param min
	 *            minimum value
	 * @param max
	 *            maximum value
	 * @return rounded value.
	 */
	@SuppressWarnings({"rawtypes"})
	private BigDecimal getGridStep(int lengthInPixels, double min, double max) {

		if(lengthInPixels <= 0) {
			throw new IllegalArgumentException(Messages.getString(Messages.LENGTH_MUST_BE_POSITIVE));
		}
		//
		if(min >= max) {
			throw new IllegalArgumentException(Messages.getString(Messages.MUST_BE_LESS_MAX));
		}
		/*
		 * BigDecimal gridStep calculation.
		 */
		double length = Math.abs(max - min);
		double gridStepHint = length / lengthInPixels * axis.getTick().getTickMarkStepHint();
		// gridStepHint --> mantissa * 10 ** exponent
		// e.g. 724.1 --> 7.241 * 10 ** 2
		double mantissa = gridStepHint;
		int exponent = 0;
		if(mantissa < 1) {
			while(mantissa < 1) {
				mantissa *= 10.0;
				exponent--;
			}
		} else {
			while(mantissa >= 10) {
				mantissa /= 10.0;
				exponent++;
			}
		}
		// calculate the grid step with hint.
		BigDecimal gridStep;
		if(mantissa > 7.5) {
			// gridStep = 10.0 * 10 ** exponent
			gridStep = BigDecimal.TEN.multiply(pow(10, exponent));
		} else if(mantissa > 3.5) {
			// gridStep = 5.0 * 10 ** exponent
			gridStep = BigDecimal.valueOf(5).multiply(pow(10, exponent));
		} else if(mantissa > 1.5) {
			// gridStep = 2.0 * 10 ** exponent
			gridStep = BigDecimal.valueOf(2).multiply(pow(10, exponent));
		} else {
			// gridStep = 1.0 * 10 ** exponent
			gridStep = pow(10, exponent);
		}
		/*
		 * Advanced calculation.
		 */
		if(axis.isIntegerDataPointAxis()) {
			for(ISeries series : (ISeries[])chart.getSeriesSet().getSeries()) {
				if(axis.getDirection() == Direction.X) {
					if(series.getXAxisId() == axis.getId() && series.getXSeries().length != 0) {
						int xSeriesLength = series.getXSeries().length;
						double upper = series.getXSeries()[xSeriesLength - 1],
								lower = series.getXSeries()[0];
						gridStep = BigDecimal.valueOf((upper - lower) / (xSeriesLength - 1));
					}
				} else {
					if(series.getYAxisId() == axis.getId() && series.getYSeries().length != 0) {
						gridStep = BigDecimal.valueOf(1.0);
					}
				}
			}
		}
		//
		return gridStep;
	}

	/**
	 * Gets the tick label positions.
	 * 
	 * @return the tick label positions
	 */
	public ArrayList<Integer> getTickLabelPositions() {

		return tickLabelPositions;
	}

	/**
	 * Gets the tick label values.
	 * 
	 * @return the tick label values
	 */
	protected ArrayList<Double> getTickLabelValues() {

		return tickLabelValues;
	}

	/**
	 * Sets the font.
	 * 
	 * @param font
	 *            the font
	 */
	protected void setFont(Font font) {

		if(font == null) {
			this.font = DEFAULT_FONT;
		} else {
			this.font = font;
		}
	}

	/**
	 * Gets the font.
	 * 
	 * @return the font
	 */
	public Font getFont() {

		if(font.isDisposed()) {
			font = DEFAULT_FONT;
		}
		return font;
	}

	/**
	 * Gets the layout data.
	 * 
	 * @return the layout data
	 */
	public ChartLayoutData getLayoutData() {

		return new ChartLayoutData(widthHint, heightHint);
	}

	/**
	 * Sets the bounds on chart panel.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void setBounds(int x, int y, int width, int height) {

		bounds = new Rectangle(x, y, width, height);
	}

	/**
	 * Gets the bounds on chart panel.
	 * 
	 * @return the bounds on chart panel
	 */
	public Rectangle getBounds() {

		return bounds;
	}

	/**
	 * Disposes the resources.
	 */
	protected void dispose() {

		if(!chart.isDisposed()) {
			chart.removePaintListener(this);
		}
	}

	/**
	 * Updates the tick labels layout.
	 */
	protected void updateLayoutData() {

		widthHint = SWT.DEFAULT;
		heightHint = SWT.DEFAULT;
		if(!axis.getTick().isVisible()) {
			widthHint = 0;
			heightHint = 0;
		} else {
			if(axis.isHorizontalAxis()) {
				heightHint = Axis.MARGIN + Util.getExtentInGC(getFont(), null).y;
			} else {
				widthHint = Axis.MARGIN;
			}
		}
	}

	@Override
	public void paintControl(PaintEvent e) {

		if(!axis.getTick().isVisible()) {
			return;
		}
		Color oldBackground = e.gc.getBackground();
		e.gc.setBackground(chart.getBackground());
		Color oldForeground = e.gc.getForeground();
		e.gc.setForeground(getForeground());
		if(axis.isHorizontalAxis()) {
			drawXTick(e.gc);
		} else {
			drawYTick(e.gc);
		}
		e.gc.setBackground(oldBackground);
		e.gc.setForeground(oldForeground);
	}

	/**
	 * Draw the X tick.
	 * 
	 * @param gc
	 *            the graphics context
	 */
	private void drawXTick(GC gc) {

		int offset = axis.getTick().getAxisTickMarks().getBounds().x;
		// draw tick labels
		gc.setFont(axis.getTick().getFont());
		int angle = axis.getTick().getTickLabelAngle();
		for(int i = 0; i < tickLabelPositions.size(); i++) {
			if(axis.isValidCategoryAxis() || tickVisibilities.get(i) == true) {
				String text = tickLabels.get(i);
				int textWidth = gc.textExtent(text).x;
				int textHeight = gc.textExtent(text).y;
				if(angle == 0) {
					int x = (int)(tickLabelPositions.get(i) - textWidth / 2d + offset);
					gc.drawText(text, bounds.x + x, bounds.y);
					continue;
				}
				float x, y;
				if(axis.getPosition() == Position.Primary) {
					x = (float)(offset + bounds.x + tickLabelPositions.get(i) - textWidth * Math.cos(Math.toRadians(angle)) - textHeight / 2d * Math.sin(Math.toRadians(angle)));
					y = (float)(bounds.y + textWidth * Math.sin(Math.toRadians(angle)));
				} else {
					x = (float)(offset + bounds.x + tickLabelPositions.get(i) - textHeight / 2d * Math.sin(Math.toRadians(angle)));
					y = (float)(bounds.y + bounds.height * Math.sin(Math.toRadians(angle)));
				}
				drawRotatedText(gc, text, x, y, angle);
			}
		}
	}

	/**
	 * Draws the rotated text.
	 * 
	 * @param gc
	 *            the graphics context
	 * @param text
	 *            the text
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param angle
	 *            the angle
	 */
	private static void drawRotatedText(GC gc, String text, float x, float y, int angle) {

		// set transform to rotate
		Transform transform = new Transform(gc.getDevice());
		transform.translate(x, y);
		transform.rotate(360 - angle);
		gc.setTransform(transform);
		gc.drawText(text, 0, 0);
		// clear transform
		transform.dispose();
		gc.setTransform(null);
	}

	/**
	 * Draw the Y tick.
	 * 
	 * @param gc
	 *            the graphics context
	 */
	private void drawYTick(GC gc) {

		int margin = Axis.MARGIN + AxisTickMarks.TICK_LENGTH;
		// draw tick labels
		gc.setFont(axis.getTick().getFont());
		int figureHeight = gc.textExtent("dummy").y; //$NON-NLS-1$
		for(int i = 0; i < tickLabelPositions.size(); i++) {
			if(tickVisibilities.size() == 0 || tickLabels.size() == 0) {
				break;
			}
			if(tickVisibilities.get(i) == true) {
				String text = tickLabels.get(i);
				int x = Axis.MARGIN;
				if(tickLabels.get(0).startsWith("-") && !text.startsWith("-")) { //$NON-NLS-1$ //$NON-NLS-2$
					x += gc.textExtent("-").x; //$NON-NLS-1$
				}
				int y = (int)(bounds.height - 1 - tickLabelPositions.get(i) - figureHeight / 2.0 - margin);
				gc.drawText(text, bounds.x + x, bounds.y + y);
			}
		}
	}

	/**
	 * Sets the format for axis tick label. <tt>DecimalFormat</tt> and
	 * <tt>DateFormat</tt> should be used for <tt>double[]</tt> series and
	 * <tt>Data[]</tt> series respectively.
	 * <p>
	 * If <tt>null</tt> is set, default format will be used.
	 * 
	 * @param format
	 *            the format
	 */
	protected void setFormat(Format format) {

		this.format = format;
	}

	/**
	 * Gets the format for axis tick label.
	 * 
	 * @return the format
	 */
	protected Format getFormat() {

		return format;
	}

	public ArrayList<Boolean> getTickVisibilities() {

		return tickVisibilities;
	}

	public ArrayList<String> getTickLabels() {

		return tickLabels;
	}
}
