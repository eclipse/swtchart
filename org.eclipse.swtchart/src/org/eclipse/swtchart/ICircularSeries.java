/*******************************************************************************
 * Copyright (c) 2020, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 * Philip Wenig - improvement series data model
 *******************************************************************************/
package org.eclipse.swtchart;

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.internal.compress.Compress;
import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.model.NodeDataModel;

/**
 * Contains methods to be implemented that are common to both PieSeries and Doughnut Series
 */
public interface ICircularSeries<T> extends ISeries<T> {

	Color getSliceColor();

	void setSliceColor(Color sliceColor);

	int getBorderWidth();

	void setBorderWidth(int width);

	int getBorderStyle();

	void setBorderStyle(int borderStyle);

	Color getSliceColorHighlight();

	void setSliceColorHighlight(Color sliceColor);

	int getBorderWidthHighlight();

	void setBorderWidthHighlight(int width);

	int getBorderStyleHighlight();

	void setBorderStyleHighlight(int borderStyle);

	/**
	 * gets the label series
	 * 
	 * @return labels that will be visible
	 */
	String[] getLabels();

	/**
	 * each element of this array is the color that the corresponding data
	 * are present with same index in labelSeries and valueSeries
	 * 
	 * @return color[]
	 */
	Color[] getColors();

	/**
	 * sets the circular series.
	 * 
	 * @param labels
	 * @param values
	 */
	void setSeries(String[] labels, double[] values);

	/**
	 * allows user to change color of given label to required color.
	 * This must be called after adding all the series, else it will be overridden.
	 * 
	 * @param label
	 * @param color
	 */
	void setColor(String label, Color color);

	/**
	 * allows user to set multiple colors together. The colors are set corresponding to the
	 * the labels in the label series.
	 * This must be called after adding all the series, else it will be overridden.
	 * 
	 * @param colors
	 */
	void setColor(Color[] colors);

	Compress getCompressor();

	/**
	 * This method is not to be used by the user.
	 * 
	 * @return gets the rootNode of the Series.
	 */
	Node getRootNode();

	/**
	 * fetches a node with it's Id.
	 * 
	 * @param id
	 * @return
	 */
	Node getNodeById(String id);

	/**
	 * gets the MultiLevelPie Series as Series.
	 * 
	 * @return
	 */
	List<Node> getSeries();

	/**
	 * adds a node to the primary series.
	 * 
	 * @param id
	 * @param val
	 */
	void addNode(String id, double val);

	/**
	 *
	 * @return data model used in the series.
	 */
	NodeDataModel getNodeDataModel();

	/**
	 * sets the given data model, and updates the related data in the series
	 * 
	 * @param nodeDataModel
	 */
	void setNodeDataModel(NodeDataModel nodeDataModel);

	/**
	 * 
	 * @return the number of levels the data model has.
	 */
	int getMaxTreeDepth();

	/**
	 * Call to this function happens when the node where an
	 * event fired does not have to redraw the entire chart.
	 */
	void setHighlightedNode(Node highlightedNode);

	/** gets the node to highlight */
	Node getHighlightedNode();

	/**
	 * 
	 * @param primaryValueX
	 * @param primaryValueY
	 * @return
	 */
	Node getPieSliceFromPosition(double primaryValueX, double primaryValueY);

	/**
	 * 
	 * @param id
	 * @return the percent that the pie slice is compared to the rootPointer
	 */
	double getSlicePercent(String id);

	/**
	 * x and y are in pixels
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	Node getPieSliceFromPosition(int x, int y);
}