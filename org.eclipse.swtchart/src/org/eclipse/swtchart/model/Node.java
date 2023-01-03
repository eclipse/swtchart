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
package org.eclipse.swtchart.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

/**
 * Each Object of this class represents a slice of the MultiLevel pie chart
 * A node contains
 * 1) Id (This, as for now is same as Label of the node)
 * 2) level (the distance from rootNode at which it is situated.)
 * 3) Color of the Pie Slice
 * 4) the angular bounds between which the Pie is drawn.
 * 5) it's parent
 * 6) it's children
 * 7) other data required for extensions package are added for now, shall be implemented later.
 * 
 * If the user tries to declare a node by his own, without using the methods,
 * He has to use new Node(String id, double value, Node parent).
 */
public class Node {

	private double value;
	/** stores at which level the pie slice is */
	private int level;
	private String id;
	private String description = "";
	/** the data model that the node is part of */
	private NodeDataModel model;
	private List<Node> children;
	/** the angle extremities between which the Pie "slice" is drawn. */
	private Point angleBounds;
	/** this color is just the color constant from SWT */
	private Color sliceColor;
	private boolean isVisible;
	private boolean isVisibleInLegend;
	/** parent of the given node */
	private Node parent;
	/** the depth of the tree starting from this node. The tree includes node also */
	private int maxSubTreeDepth;
	/** application defined data associated with the slice */
	private Object data;

	/**
	 * This will be used only for rootNode, and maybe for rootPointerNode
	 * after some time.
	 * 
	 * @param id
	 * @param value
	 */
	public Node(String id, double value, NodeDataModel dataModel) {

		this.id = id;
		this.value = value;
		this.model = dataModel;
		this.level = 0;
		children = new ArrayList<>();
		this.isVisible = true;
		this.isVisibleInLegend = true;
	}

	/**
	 * To be used for generating nodes. As of now, if the user calls this
	 * method to create node, then will have to update the pie data manually.
	 * So not advisable for user to use it directly. Should use
	 * addChild()
	 * 
	 * @param id
	 * @param value
	 * @param parent
	 */
	public Node(String id, double value, Node parent) {

		this.id = id;
		this.value = value;
		children = new ArrayList<>();
		this.isVisible = parent.isVisible;
		this.isVisibleInLegend = parent.isVisibleInLegend;
		this.parent = parent;
		this.level = parent.level + 1;
		this.model = parent.model;
		setSliceColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		model.getTree().put(id, this);
		this.getParent().getChildren().add(this);
	}

	/**
	 * the value of the node
	 * 
	 * @return
	 */
	public double getValue() {

		return value;
	}

	/**
	 * the id of the node.
	 * 
	 * @return
	 */
	public String getId() {

		return id;
	}

	public String getDescription() {

		return description.isEmpty() ? id : description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	/**
	 * The angleBounds.x is the min angle boundary.
	 * The angleBounds.y is the angular width the node will occupy.
	 * 
	 * @return angleBounds of the node
	 */
	public Point getAngleBounds() {

		return angleBounds;
	}

	/**
	 * @return List of children nodes.
	 */
	public List<Node> getChildren() {

		return children;
	}

	/**
	 * @return the parent of the node.
	 */
	public Node getParent() {

		return parent;
	}

	public Color getSliceColor() {

		return sliceColor;
	}

	public void setSliceColor(Color sliceColor) {

		this.sliceColor = sliceColor;
	}

	public boolean isVisible() {

		return isVisible;
	}

	public void setVisible(boolean isVisible) {

		this.isVisible = isVisible;
	}

	public boolean isVisibleInLegend() {

		return isVisibleInLegend;
	}

	public void setVisibleInLegend(boolean isVisibleInLegend) {

		this.isVisibleInLegend = isVisibleInLegend;
	}

	/**
	 * the distance from the rootNode is level of the node.
	 * 
	 * @return
	 */
	public int getLevel() {

		return level;
	}

	public int getMaxSubTreeDepth() {

		return maxSubTreeDepth;
	}

	public NodeDataModel getDataModel() {

		return model;
	}

	public void setValue(double value) {

		this.value = value;
		update();
	}

	public void setId(String label) {

		this.id = label;
	}

	public void setAngleBounds(Point point) {

		this.angleBounds = point;
	}

	public void changeParent(Node parent) {

		this.parent = parent;
	}

	public void setDataModel(NodeDataModel model) {

		this.model = model;
	}

	/**
	 * 
	 * @param labels
	 * @param vals
	 * @return the String-node map with each node having containing data
	 *         corresponding to the variables passed
	 */
	public void addChildren(String[] labels, double[] vals) {

		int length = labels.length;
		if(vals.length != length) {
			// To Do Throw error
		}
		for(int i = 0; i < length; i++) {
			new Node(labels[i], vals[i], this);
		}
		update();
	}

	/**
	 * helps in shifting nodes from one parent to another,
	 * in case a mistake is made.
	 * The nodes added are not the same as nodes provided in input.
	 * 
	 * @param nodes
	 * @return
	 */
	public void addChildren(Node[] nodes) {

		for(Node node : nodes) {
			String label = node.id;
			double value = node.value;
			new Node(label, value, this);
		}
		update();
	}

	/**
	 * adds a child to this node with label and value as specified.
	 * 
	 * @param label
	 * @param value
	 * @return
	 */
	public Node addChild(String label, double value) {

		Node node = new Node(label, value, this);
		update();
		return node;
	}

	/**
	 * 
	 * @param child
	 * @return the removed child, so that if user needs to add the child, he may.
	 */
	Node removeChild(String child) {

		Node node = null;
		for(Node nodes : children) {
			if(nodes.getId() == child)
				node = nodes;
		}
		if(node == null) {
			// throw error
			return null;
		}
		children.remove(node);
		model.getTree().remove(child);
		update();
		return node;
	}

	/**
	 * The DFS function. It updates the value of the each element of the tree
	 * and the maxTreeDepth. Updates starting at the leaves and then backwards.
	 * Have to shift the updating maxSubTreeDepth just before drawing the chart.
	 */
	public void updateValues() {

		// update the value of parent if children's value total exceeds it's value.
		// first go to leaves, then come towards the rootNode.
		if(this.children.isEmpty()) {
			maxSubTreeDepth = 1;
			if(this.value <= 0) {
				// throw error
			}
			return;
		}
		double total = 0;
		Iterable<Node> nodes = children;
		for(Node node : nodes) {
			node.updateValues();
			total += node.value;
			maxSubTreeDepth = Math.max(maxSubTreeDepth, node.maxSubTreeDepth + 1);
		}
		// updating only if children cannot be drawn.
		if(total > this.value) {
			this.value = total;
		}
	}

	/**
	 * It is to be called after every method that introduces a change in the node.
	 * This function is called after calling updateValues() method.
	 * It sets the lower angular extremity and width of a pie "slice".
	 * Each node's angular bounds are of the form of a Point.
	 * Point.x is minimum, Point.y is width
	 */
	public void updateAngularBounds() {

		Iterable<Node> nodes = children;
		if(nodes == null)
			return;
		//
		int start = angleBounds.x;
		double diff = 0, required = 0;
		for(Node node : nodes) {
			// calculates the angle covered
			int angleCovered = (int)((node.getValue() * angleBounds.y) / this.getValue());
			// ensures that no non zero node value goes without being drawn
			if(angleCovered == 0 && node.getValue() != 0) {
				angleCovered = 1;
				required -= 1.0;
			} else {
				diff = ((node.getValue() * angleBounds.y) / this.value) - angleCovered;
				required += diff;
				// so that errors in double value calculations do not lead
				// to less than 360 degree total angle.
				if(required > 0.999) {
					required -= 1.0;
					angleCovered++;
				}
			}
			node.setAngleBounds(new Point(start, angleCovered));
			model.getNodes()[node.getLevel() - model.getRootPointer().getLevel()].add(node);
			// the DFS call to children after this node data is set.
			node.updateAngularBounds();
			// updating for the next child node.
			start += angleCovered;
		}
	}

	public void update() {

		model.update();
	}

	/**
	 * Returns the application defined data associated with the slice.
	 * 
	 * @return the node data
	 *
	 * @see #setData(Object)
	 */
	public Object getData() {

		return data;
	}

	/**
	 * Sets the application defined data associated with the slice.
	 *
	 * @param data
	 *            the node data
	 *
	 * @see #getData()
	 */
	public void setData(Object data) {

		this.data = data;
	}
}
