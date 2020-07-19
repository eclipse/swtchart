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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.internal.compress.CompressCircularSeries;

/**
 * This shall be called as soon as the Series.Type is set to be MULTI_LEVEL_PIE.
 * The data model shall remain the same for a pie chart, and modifications
 * shall be made with the calls to methods of this and Node class.
 * A rootNode is initialized that shall not be visible, but will be the ancestor
 * of all nodes of the pie chart.
 */
public class IdNodeDataModel {

	private String Id;
	/** this node is the parent of all nodes, shall not be kept visible */
	private Node rootNode;
	/** this is the pointer node that will point to the node from wheere the drawing is to occur */
	private Node rootPointer;
	/** data structure that holds all the nodes and allows access in O(1) time. */
	private HashMap<String, Node> tree;
	/** stores nodes in order of the levels they are in. */
	private List<Node> nodesAtLevels[];
	private CompressCircularSeries compress;

	public IdNodeDataModel() {

		this("Circular Chart");
	}

	public IdNodeDataModel(String Id) {

		this.Id = Id;
		this.rootNode = new Node(Id, -1, this);
		tree = new HashMap<String, Node>();
		initialiseRootNode();
		rootPointer = rootNode;
		compress = new CompressCircularSeries(this);
	}

	private void initialiseRootNode() {

		rootNode.changeParent(rootNode);
		rootNode.setVisibility(true);
		rootNode.setAngleBounds(new Point(0, 360));
		rootNode.setDataModel(this);
		rootNode.setColor(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		tree.put(Id, rootNode);
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

	public List<Node>[] getNodes() {

		return nodesAtLevels;
	}

	public void setNodes(ArrayList<Node>[] arrayList) {

		this.nodesAtLevels = arrayList;
	}

	public Node getRootPointer() {

		return rootPointer;
	}

	public void setRootPointer(Node pointer) {

		this.rootPointer = pointer;
		rootPointer.setColor(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		rootPointer.setAngleBounds(new Point(0, 360));
		update();
	}

	public String getId() {

		return Id;
	}

	public void setId(String id) {

		this.Id = id;
		rootNode.setId(id);
	}

	/**
	 * update functions that ensures the changes made by user do make sense, and
	 * handles those which do not make sense. If changes can't be made, throws error.
	 */
	@SuppressWarnings("unchecked")
	public void update() {

		getRootPointer().updateValues();
		/*
		 * update nodes length
		 */
		int maxTreeDepth = rootPointer.getMaxSubTreeDepth() - 1;
		setNodes(new ArrayList[maxTreeDepth + 1]);
		//
		ArrayList<Node>[] node = (ArrayList<Node>[])getNodes();
		for(int i = 0; i <= maxTreeDepth; i++) {
			node[i] = new ArrayList<Node>();
		}
		setNodes(node);
		/*
		 * angular bounds
		 */
		node[0].add(rootPointer);
		getRootPointer().updateAngularBounds();
		//
		getRootPointer().setVisibility(true);
		//
		compress.update();
	}

	public CompressCircularSeries getCompressor() {

		return compress;
	}
}
