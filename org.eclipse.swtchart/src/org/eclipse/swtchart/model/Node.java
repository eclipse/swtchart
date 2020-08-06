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

	private double val;
	/** stores at which level the pie slice is */
	private int level;
	private String id;
	/** the data model that the node is part of */
	private IdNodeDataModel data;
	private List<Node> children;
	/** the angle extremities between which the Pie "slice" is drawn. */
	private Point angleBounds;
	/** this color is just the color constant from SWT */
	private Color color;
	private boolean isVisible;
	/** parent of the given node */
	private Node parent;
	/** the depth of the tree starting from this node. The tree includes node also */
	private int maxSubTreeDepth;

	/**
	 * This will be used only for rootNode, and maybe for rootPointerNode
	 * after some time.
	 * 
	 * @param id
	 * @param val
	 */
	Node(String id, double val, IdNodeDataModel data) {

		this.id = id;
		this.val = val;
		this.data = data;
		this.level = 0;
		children = new ArrayList<Node>();
		this.isVisible = true;
	}

	/**
	 * To be used for generating nodes. As of now, if the user calls this
	 * method to create node, then will have to update the pie data manually.
	 * So not advisable for user to use it directly. Should use
	 * addChild()
	 * 
	 * @param id
	 * @param val
	 * @param parent
	 */
	public Node(String id, double val, Node parent) {

		this.id = id;
		this.val = val;
		children = new ArrayList<Node>();
		this.isVisible = parent.isVisible;
		this.parent = parent;
		this.level = parent.level + 1;
		this.data = parent.data;
		setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		data.getTree().put(id, this);
		this.getParent().getChildren().add(this);
	}

	/**
	 * the value of the node
	 * 
	 * @return
	 */
	public double getValue() {

		return val;
	}

	/**
	 * the id of the node.
	 * 
	 * @return
	 */
	public String getId() {

		return id;
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

	/**
	 * color with which node shall be drawn with
	 * 
	 * @return
	 */
	public Color getColor() {

		return color;
	}

	/**
	 * @return visibility of the node.
	 */
	public boolean isVisible() {

		return isVisible;
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

	public IdNodeDataModel getDataModel() {

		return data;
	}

	public void setValue(double value) {

		this.val = value;
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

	public void setDataModel(IdNodeDataModel model) {

		this.data = model;
	}

	/**
	 * sets the color of the pie slice.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {

		this.color = color;
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
			double value = node.val;
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
		data.getTree().remove(child);
		update();
		return node;
	}

	/**
	 * set or updates the visibility of a particular element. If the parent is not
	 * visible, the children are set to be invisible too, but not vice -versa.
	 * 
	 * @param visibility
	 */
	public void setVisibility(boolean visibility) {

		this.isVisible = visibility;
		if(!parent.isVisible) {
			this.isVisible = false;
			if(visibility) {
				// throw error.
			}
		}
		Iterable<Node> nodes = children;
		// DFS function call to children.
		// update nodes visibility only for visible nodes
		for(Node node : nodes) {
			node.setVisibility(node.isVisible ? this.isVisible : false);
		}
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
			if(this.val <= 0) {
				// throw error
			}
			return;
		}
		double total = 0;
		Iterable<Node> nodes = children;
		for(Node node : nodes) {
			node.updateValues();
			total += node.val;
			maxSubTreeDepth = Math.max(maxSubTreeDepth, node.maxSubTreeDepth + 1);
		}
		// updating only if children cannot be drawn.
		if(total > this.val) {
			this.val = total;
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
				diff = ((node.getValue() * angleBounds.y) / this.val) - angleCovered;
				required += diff;
				// so that errors in double value calculations do not lead
				// to less than 360 degree total angle.
				if(required > 0.999) {
					required -= 1.0;
					angleCovered++;
				}
			}
			node.setAngleBounds(new Point(start, angleCovered));
			data.getNodes()[node.getLevel() - data.getRootPointer().getLevel()].add(node);
			// the DFS call to children after this node data is set.
			node.updateAngularBounds();
			// updating for the next child node.
			start += angleCovered;
		}
	}

	public void update() {

		data.update();
	}
}
