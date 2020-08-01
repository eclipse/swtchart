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
package org.eclipse.swtchart.internal.series;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.internal.compress.Compress;
import org.eclipse.swtchart.internal.compress.CompressCircularSeries;
import org.eclipse.swtchart.model.IdNodeDataModel;
import org.eclipse.swtchart.model.Node;

@SuppressWarnings("rawtypes")
public abstract class CircularSeries extends Series implements ICircularSeries {

	protected Chart chart;
	protected IdNodeDataModel model;
	protected Node rootNode;
	protected Node rootPointer;
	protected int maxTreeDepth;
	protected Color borderColor;
	protected int borderWidth;
	protected int highlightLineWidth;
	protected int borderStyle;
	protected Node highlightedNode;
	protected Color highlightColor;

	@SuppressWarnings("unchecked")
	public CircularSeries(Chart chart, String id) {

		super(chart, id);
		this.chart = chart;
		initialise();
		model = new IdNodeDataModel(id);
		rootNode = model.getRootNode();
		rootPointer = model.getRootPointer();
		compressor = model.getCompressor();
		borderColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		borderWidth = 1;
		highlightLineWidth = 3;
		borderStyle = SWT.LINE_SOLID;
	}

	@Override
	public Color getBorderColor() {

		return borderColor;
	}

	@Override
	public int getBorderWidth() {

		return borderWidth;
	}

	@Override
	public int getBorderStyle() {

		return borderStyle;
	}

	@Override
	public void setBorderColor(Color color) {

		this.borderColor = color;
	}

	@Override
	public void setBorderWidth(int borderWidth) {

		this.borderWidth = borderWidth;
	}

	@Override
	public void setBorderStyle(int borderStyle) {

		this.borderStyle = borderStyle;
	}

	@Override
	public void setHighlightLineWidth(int width) {

		highlightLineWidth = width;
	}

	@Override
	public Compress getCompressor() {

		return (Compress)compressor;
	}

	public Node getRootNode() {

		return rootNode;
	}

	public Node getRootPointer() {

		return model.getRootPointer();
	}

	@Override
	public Node getNodeById(String id) {

		return model.getTree().get(id);
	}

	/**
	 * This returns the original series that we provided as input.
	 * This is added here so as to provide the functionalities of the simple pie chart
	 * 
	 * @return
	 */
	@Override
	public List<Node> getSeries() {

		return rootNode.getChildren();
	}

	@Override
	public void addNode(String id, double val) {

		rootNode.addChild(id, val);
	}

	@Override
	public String[] getLabels() {

		List<Node>[] nodes = model.getNodes();
		int tot = 0, index = 0;
		maxTreeDepth = getRootPointer().getMaxSubTreeDepth() - 1;
		for(int i = 1; i <= maxTreeDepth; i++) {
			tot += nodes[i].size();
		}
		String[] labels = new String[tot];
		for(int i = 1; i <= maxTreeDepth; i++) {
			int len = nodes[i].size();
			for(int j = 0; j != len; j++) {
				labels[index] = nodes[i].get(j).getId();
				index++;
			}
		}
		return labels;
	}

	@Override
	public Color[] getColors() {

		List<Node>[] nodes = model.getNodes();
		int tot = 0, ind = 0;
		for(int i = 1; i <= maxTreeDepth; i++) {
			tot += nodes[i].size();
		}
		Color[] colors = new Color[tot];
		for(int i = 1; i <= maxTreeDepth; i++) {
			int len = nodes[i].size();
			for(int j = 0; j != len; j++) {
				colors[ind] = nodes[i].get(j).getColor();
				ind++;
			}
		}
		return colors;
	}

	@Override
	public void setColor(Color[] colors) {

		int length = getLabels().length;
		if(colors.length != length) {
			// Throw Error
		}
		for(int i = 0; i != length; i++) {
			if(colors[i] != null)
				model.getNodeById(getLabels()[i]).setColor(colors[i]);
		}
	}

	@Override
	public void setColor(String label, Color color) {

		Node node = model.getNodeById(label);
		if(node == null) {
			// throw error
			return;
		}
		node.setColor(color);
	}

	@Override
	public void setSeries(String[] labels, double[] values) {

		int length = labels.length;
		if(values.length != length) {
			// throw error
		}
		for(int i = 0; i != length; i++) {
			new Node(labels[i], values[i], rootNode);
		}
		model.update();
	}

	@Override
	public Node getHighlightedNode() {

		return highlightedNode;
	}

	@Override
	public void setHighlightedNode(Node highlightedNode) {

		// can't draw the highlighted node if it does't have rootNode as ancestor
		if(highlightedNode == null) {
			this.highlightedNode = null;
			return;
		}
		Node ptr = highlightedNode;
		while(ptr != getRootPointer() && ptr != getRootNode())
			ptr = ptr.getParent();
		if(ptr != getRootPointer())
			return;
		if(highlightColor == null)
			highlightColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		this.highlightedNode = highlightedNode;
	}

	@Override
	public void setHighlightColor(Color color) {

		this.highlightColor = color;
	}

	@Override
	protected void draw(GC gc, int width, int height, Axis xAxis, Axis yAxis) {

		/*
		 * Sets the x and y range of the axes so that the pie slices are perfectly circular.
		 */
		setBothAxisRange(width, height, xAxis, yAxis);
		/*
		 * Setting the styles for border
		 */
		gc.setForeground(borderColor);
		//
		gc.setLineStyle(borderStyle);
		//
		gc.setLineWidth(borderWidth);
		/*
		 * A DFS function which draws the node after drawing it's children.
		 */
		drawNode(getRootPointer(), gc, xAxis, yAxis);
		/*
		 * highlight just the required node.
		 */
		if(highlightedNode != null && highlightColor != null) {
			//
			gc.setForeground(highlightColor);
			gc.setLineWidth(highlightLineWidth);
			// sets the level of the highlighted node.
			int level = highlightedNode.getLevel() - getRootPointer().getLevel() + (this instanceof Pie ? 0 : 1);
			// the top-left most coordinates of the square where the highlighted node is drawn.
			int xStart = xAxis.getPixelCoordinate(-level);
			int yStart = yAxis.getPixelCoordinate(level);
			int xWidth = xAxis.getPixelCoordinate(level) - xStart;
			int yWidth = yAxis.getPixelCoordinate(-level) - yStart;
			//
			int angleStart = highlightedNode.getAngleBounds().x,
					angleWidth = highlightedNode.getAngleBounds().y;
			// drawing the inner and outer arcs of the highlighted node.
			gc.drawArc(xStart, yStart, xWidth, yWidth, angleStart, angleWidth);
			if(highlightedNode == getRootPointer())
				return;
			// the top-left most coordinates of the square where the parent is drawn.
			int xParentStart = xAxis.getPixelCoordinate(-level + 1);
			int yParentStart = yAxis.getPixelCoordinate(level - 1);
			int xParentWidth = xAxis.getPixelCoordinate(level - 1) - xParentStart;
			int yParentWidth = yAxis.getPixelCoordinate(-level + 1) - yParentStart;
			gc.drawArc(xParentStart, yParentStart, xParentWidth, yParentWidth, angleStart, angleWidth);
			// the coordinates where the inner arc begins
			double xParentStartCoordinate = (level - 1) * Math.cos(Math.toRadians(angleStart));
			double yParentStartCoordinate = (level - 1) * Math.sin(Math.toRadians(angleStart));
			int xParentStartPixelCoordinate = xAxis.getPixelCoordinate(xParentStartCoordinate);
			int yParentStartPixelCoordinate = yAxis.getPixelCoordinate(yParentStartCoordinate);
			// the coordinates where the outer arc begins
			double xStartCoordinate = level * Math.cos(Math.toRadians(angleStart));
			double yStartCoordinate = level * Math.sin(Math.toRadians(angleStart));
			int xStartPixelCoordinate = xAxis.getPixelCoordinate(xStartCoordinate);
			int yStartPixelCoordinate = yAxis.getPixelCoordinate(yStartCoordinate);
			// drawing line from inner arc to outer arc on the start boundary
			gc.drawLine(xParentStartPixelCoordinate, yParentStartPixelCoordinate, xStartPixelCoordinate, yStartPixelCoordinate);
			// the coordinates where the outer arc ends
			double xEndCoordinate = level * Math.cos(Math.toRadians(angleStart + angleWidth));
			double yEndCoordinate = level * Math.sin(Math.toRadians(angleStart + angleWidth));
			int xEndPixelCoordinate = xAxis.getPixelCoordinate(xEndCoordinate);
			int yEndPixelCoordinate = yAxis.getPixelCoordinate(yEndCoordinate);
			// the coordinates where the inner arc ends
			double xParentEndCoordinate = (level - 1) * Math.cos(Math.toRadians(angleStart + angleWidth));
			double yParentEndCoordinate = (level - 1) * Math.sin(Math.toRadians(angleStart + angleWidth));
			int xParentEndPixelCoordinate = xAxis.getPixelCoordinate(xParentEndCoordinate);
			int yParentEndPixelCoordinate = yAxis.getPixelCoordinate(yParentEndCoordinate);
			// drawing line from inner end to outer end of the end boundary
			gc.drawLine(xParentEndPixelCoordinate, yParentEndPixelCoordinate, xEndPixelCoordinate, yEndPixelCoordinate);
		}
	}

	protected abstract void setBothAxisRange(int width, int height, Axis xAxis, Axis yAxis);

	protected abstract void drawNode(Node rootNode2, GC gc, Axis xAxis, Axis yAxis);

	@Override
	protected void setCompressor() {

		((CompressCircularSeries)compressor).update();
	}

	@Override
	public int getMaxTreeDepth() {

		return rootPointer.getMaxSubTreeDepth() - 1;
	}

	private void initialise() {

		IAxis[] axes = chart.getAxisSet().getAxes();
		for(IAxis axis : axes) {
			axis.getTick().setVisible(false);
			axis.getGrid().setVisible(false);
			axis.getTitle().setVisible(false);
		}
	}

	@Override
	public IdNodeDataModel getModel() {

		return model;
	}

	public void setRootPointer(Node rootPointer) {

		if(highlightedNode != null) {
			if(highlightedNode == getRootPointer())
				highlightedNode = null;
			Node ptr = highlightedNode;
			while(ptr != getRootPointer() && ptr != getRootNode())
				ptr = ptr.getParent();
			if(ptr != getRootPointer())
				highlightedNode = null;
		}
		this.rootPointer = rootPointer;
		model.setRootPointer(rootPointer);
	}

	@Override
	public void setDataModel(IdNodeDataModel data) {

		this.model = data;
		this.rootNode = model.getRootNode();
		this.rootPointer = model.getRootPointer();
		maxTreeDepth = rootPointer.getMaxSubTreeDepth() - 1;
	}

	@Override
	public double getSlicePercent(String id) {

		Node node = getNodeById(id);
		double percent = (node.getValue() * 100) / getRootPointer().getValue();
		return percent;
	}

	@Override
	public Node getPieSliceFromPosition(int x, int y) {

		double primaryX = chart.getAxisSet().getXAxis(0).getDataCoordinate(x);
		double primaryY = chart.getAxisSet().getXAxis(0).getDataCoordinate(y);
		return getPieSliceFromPosition(primaryX, primaryY);
	}
}
