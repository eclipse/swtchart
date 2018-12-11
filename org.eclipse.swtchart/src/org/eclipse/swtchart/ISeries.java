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
package org.eclipse.swtchart;

import java.util.Date;

import org.eclipse.swt.graphics.Point;

/**
 * Series.
 */
public interface ISeries {

	/**
	 * A Series type.
	 */
	public enum SeriesType {
		/** the line */
		LINE("Line"),
		/** the bar */
		BAR("Bar");

		/** the label for series type */
		public final String label;

		/**
		 * Constructor.
		 * 
		 * @param label
		 *            the label for series type
		 */
		private SeriesType(String label) {
			this.label = label;
		}
	}

	/**
	 * Gets the series id.
	 * 
	 * @return the series id
	 */
	String getId();

	/**
	 * Sets the visibility state.
	 * 
	 * @param visible
	 *            the visibility state
	 */
	void setVisible(boolean visible);

	/**
	 * Gets the visibility state.
	 * 
	 * @return true if series is visible
	 */
	boolean isVisible();

	/**
	 * Gets the series type.
	 * 
	 * @return the series type
	 */
	SeriesType getType();

	/**
	 * Enables the stack series. The series has to contain only positive values.
	 * 
	 * @param enabled
	 *            true if enabling stack series
	 * @throws IllegalStateException
	 *             if series contains negative values.
	 */
	void enableStack(boolean enabled);

	/**
	 * Gets the state indicating if stack is enabled.
	 * 
	 * @return the state indicating if stack is enabled
	 */
	boolean isStackEnabled();

	/**
	 * Sets the X series.
	 * 
	 * @param series
	 *            the X series
	 */
	void setXSeries(double[] series);

	/**
	 * Sets the Y series.
	 * 
	 * @param series
	 *            the Y series
	 */
	void setYSeries(double[] series);

	/**
	 * Gets the X series. If the X series is not set, empty array will be returned.
	 * 
	 * @return the X series
	 */
	double[] getXSeries();

	/**
	 * Gets the Y series. If the Y series haven't been set yet, empty array will be
	 * returned.
	 * 
	 * @return the Y series
	 */
	double[] getYSeries();

	/**
	 * Sets the X date series.
	 * <p>
	 * X series and X date series are exclusive. X date series will be cleared by setting
	 * X series, and vice versa.
	 * 
	 * @param series
	 *            the X date series
	 */
	void setXDateSeries(Date[] series);

	/**
	 * Gets the X date series.
	 * 
	 * @return the X date series, or empty array if X date series is not set.
	 */
	Date[] getXDateSeries();

	/**
	 * Gets the X axis id.
	 * 
	 * @return the X axis id
	 */
	int getXAxisId();

	/**
	 * Sets the X axis id.
	 * 
	 * @param id
	 *            the X axis id.
	 */
	void setXAxisId(int id);

	/**
	 * Gets the Y axis id.
	 * 
	 * @return the Y axis id
	 */
	int getYAxisId();

	/**
	 * Sets the Y axis id.
	 * 
	 * @param id
	 *            the Y axis id.
	 */
	void setYAxisId(int id);

	/**
	 * Gets the X error bar. This is typically used for scatter chart.
	 * 
	 * @return the X error bar
	 */
	IErrorBar getXErrorBar();

	/**
	 * Gets the Y error bar.
	 * 
	 * @return the Y error bar
	 */
	IErrorBar getYErrorBar();

	/**
	 * Gets the series label.
	 * 
	 * @return the series label
	 */
	ISeriesLabel getLabel();

	/**
	 * Sets the visibility state in legend.
	 * 
	 * @param visible
	 *            the visibility state in legend
	 */
	void setVisibleInLegend(boolean visible);

	/**
	 * Gets the visibility state in legend.
	 * 
	 * @return true if series is visible in legend
	 */
	boolean isVisibleInLegend();

	/**
	 * Sets the series description.
	 * <p>
	 * For example, you may store the description explaining what this series
	 * is, and display it on tool tip with mouse hover on the series.
	 * <p>
	 * By default, legend displays the description, when it is set.
	 * 
	 * @param description
	 *            the series description, or <tt>null</tt> to clear it
	 */
	void setDescription(String description);

	/**
	 * Gets the series description
	 * 
	 * @return the series description, or <tt>null<tt> if not set
	 */
	String getDescription();

	/**
	 * Gets the pixel coordinates corresponding to the given series index.
	 * 
	 * @param index
	 *            the series index
	 * @return the pixel coordinates
	 */
	Point getPixelCoordinates(int index);

	/**
	 * Adds the dispose listener. The newly created color or font for series can be
	 * disposed with the dispose listener when they are no longer needed.
	 * 
	 * @param listener
	 *            the dispose listener
	 */
	void addDisposeListener(IDisposeListener listener);
}
