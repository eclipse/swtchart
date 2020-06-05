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
package org.eclipse.swtchart.model;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.internal.series.Series;

/**
 * This shall be called as soon as the Series.Type is set to be MULTI_LEVEL_PIE.
 * The data model shall remain the same for a pie chart, and modifications
 * shall be made with the calls to methods of this and Node class.
 * A rootNode is initialized that shall not be visible, but will be the ancestor
 * of all nodes of the pie chart.
 */
public class IdNodeDataModel {

	private String Id;
	/** the number of levels of the chart */
	private int maxTreeDepth;
	/** this node is the parent of all nodes, shall not be kept visible */
	private Node rootNode;
	private Series series;
	private HashMap<String, Node> tree;

	public IdNodeDataModel(String Id, Series series) {

		this.Id = Id;
		maxTreeDepth = 0;
		this.series = series;
		this.rootNode = new Node(Id, -1);
		tree = new HashMap<String, Node>();
		initialiseRootNode();
	}

	private void initialiseRootNode() {

		rootNode.changeParent(rootNode);
		rootNode.setVisibility(true);
		rootNode.setAngleBounds(new Point(0, 360));
		rootNode.setDataModel(this);
		rootNode.setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		tree.put(Id, rootNode);
	}

	public Series getSeries() {

		return series;
	}

	public Node getRootNode() {

		return rootNode;
	}

	public HashMap<String, Node> getTree() {

		return tree;
	}

	/**
	 * @param id
	 * @return the node with the given id.
	 */
	public Node getNodeById(String id) {

		return tree.get(id);
	}
}
