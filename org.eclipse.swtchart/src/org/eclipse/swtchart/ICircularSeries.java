/*******************************************************************************
 * Copyright (c) 2020 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart;

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.internal.compress.Compress;
import org.eclipse.swtchart.model.IdNodeDataModel;
import org.eclipse.swtchart.model.Node;

/**
 * Contains methods to be implemented that are common to both PieSeries and Doughnut Series
 */
public interface ICircularSeries<T> extends ISeries<T> {

	/**
	 * gets the label series
	 * 
	 * @return labels that will be visible
	 */
	public String[] getLabels();

	/**
	 * each element of this array is the color that the corresponding data
	 * are present with same index in labelSeries and valueSeries
	 * 
	 * @return color[]
	 */
	public Color[] getColors();

	/**
	 * sets the circular series.
	 * 
	 * @param labels
	 * @param values
	 */
	public void setSeries(String[] labels, double[] values);

	/**
	 * allows user to change color of given label to required color.
	 * This must be called after adding all the series, else it will be overridden.
	 * 
	 * @param label
	 * @param color
	 */
	public void setColor(String label, Color color);

	/**
	 * allows user to set multiple colors together. The colors are set corresponding to the
	 * the labels in the label series.
	 * This must be called after adding all the series, else it will be overridden.
	 * 
	 * @param colors
	 */
	public void setColor(Color[] colors);

	public Compress getCompressor();

	/**
	 * sets the color of the border
	 * 
	 * @param color
	 */
	public void setBorderColor(Color color);

	/**
	 * sets the width of the border
	 * 
	 * @param width
	 */
	public void setBorderWidth(int width);

	/**
	 * sets the style of the border
	 * 
	 * @param borderStyle
	 *            is the SWT line constant for border type.
	 */
	public void setBorderStyle(int borderStyle);

	/**
	 * 
	 * @return the border color
	 */
	public Color getBorderColor();

	/**
	 * @return the border width
	 */
	public int getBorderWidth();

	/**
	 * 
	 * @return the SWT line constant that indicates the style of the border.
	 */
	public int getBorderStyle();

	/**
	 * This method is not to be used by the user.
	 * 
	 * @return gets the rootNode of the Series.
	 */
	public Node getRootNode();

	/**
	 * fetches a node with it's Id.
	 * 
	 * @param id
	 * @return
	 */
	public Node getNodeById(String id);

	/**
	 * gets the MultiLevelPie Series as Series.
	 * 
	 * @return
	 */
	public List<Node> getSeries();

	/**
	 * adds a node to the primary series.
	 * 
	 * @param id
	 * @param val
	 */
	public void addNode(String id, double val);

	/**
	 *
	 * @return data model used in the series.
	 */
	public IdNodeDataModel getModel();

	/**
	 * sets the given data model, and updates the related data in the series
	 * 
	 * @param data
	 */
	public void setDataModel(IdNodeDataModel data);

	/**
	 * 
	 * @return the number of levels the data model has.
	 */
	public int getMaxTreeDepth();

	/**
	 * Call to this function happens when the node where an
	 * event fired does not have to redraw the entire chart.
	 */
	public void setHighlightedNode(Node highlightedNode);

	/** gets the node to highlight */
	public Node getHighlightedNode();

	/**
	 * sets the border color for the node to be highlighted.
	 * 
	 * @param color
	 */
	public void setHighlightColor(Color color);

	/**
	 * 
	 * @param primaryValueX
	 * @param primaryValueY
	 * @return
	 */
	public Node getPieSliceFromPosition(double primaryValueX, double primaryValueY);

	/**
	 * 
	 * @param id
	 * @return the percent that the pie slice is compared to the rootPointer
	 */
	public double getSlicePercent(String id);

	/**
	 * x and y are in pixels
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Node getPieSliceFromPosition(int x, int y);

	void setHighlightLineWidth(int width);
}
