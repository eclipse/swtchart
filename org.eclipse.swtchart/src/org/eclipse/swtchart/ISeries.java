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
 * Christoph Läubrich - add support for datamodel
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart;

import java.util.Date;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swtchart.model.CartesianSeriesModel;

/**
 * Series.
 */
public interface ISeries<DataType> {

	/**
	 * A Series type.
	 */
	public enum SeriesType {

		/** the line */
		LINE("Line"), //$NON-NLS-1$
		/** the bar */
		BAR("Bar"), //$NON-NLS-1$
		/** the pie */
		PIE("Pie"), //$NON-NLS-1$
		/** the doughnut */
		DOUGHNUT("Doughnut"); // $NON-NLS-1$

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
	 * Sets the visibility buffer state.
	 * 
	 * @param visible
	 *            the visibility buffer state
	 */
	void setVisibleBuffered(boolean visible);

	/**
	 * Gets the visibility state.
	 * 
	 * @return true if series is visible
	 */
	boolean isVisible();

	/**
	 * Gets the visibility buffer state.
	 * 
	 * @return true if series is visible in buffer
	 */
	boolean isVisibleBuffered();

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
	 * This is a legacy/convenient method to create series.
	 * Sets the X series.
	 * A DoubleArraySeriesModel will be created.
	 * 
	 * @param series
	 *            the X series
	 */
	void setXSeries(double[] series);

	/**
	 * This is a legacy/convenient method to create series.
	 * Sets the Y series.
	 * A DoubleArraySeriesModel will be created.
	 * 
	 * @param series
	 *            the Y series
	 */
	void setYSeries(double[] series);

	@Deprecated
	void setXDateSeries(Date[] series);

	@Deprecated
	Date[] getXDateSeries();

	/**
	 * This is a legacy/convenient method.
	 * Gets the X series. If the X series is not set, empty array will be returned.
	 * Better use getDataModel() to retrieve the x array.
	 * 
	 * @return the X series
	 */
	double[] getXSeries();

	/**
	 * This is a legacy/convenient method.
	 * Gets the Y series. If the Y series haven't been set yet, empty array will be
	 * returned.
	 * Better use getDataModel() to retrieve the y array.
	 * 
	 * @return the Y series
	 */
	double[] getYSeries();

	/**
	 * Set the model for this series
	 * 
	 * @param model
	 */
	void setDataModel(CartesianSeriesModel<DataType> model);

	/**
	 * 
	 * @return the current model for this series
	 */
	CartesianSeriesModel<DataType> getDataModel();

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
