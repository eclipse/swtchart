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
import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.model.NodeDataModel;

@SuppressWarnings("rawtypes")
public abstract class CircularSeries extends Series implements ICircularSeries {

	private Color sliceColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private Color borderColor = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	private int borderWidth = 1;
	private int borderStyle = SWT.LINE_SOLID;
	//
	private Color sliceColorHighlight = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private Color borderColorHighlight = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	private int borderWidthHighlight = 3;
	private int borderStyleHighlight = SWT.LINE_SOLID;
	//
	private Chart chart;
	private NodeDataModel nodeDataModel;
	private Node rootNode;
	private Node rootPointer;
	private int maxTreeDepth = 1;
	private Node highlightedNode;

	@SuppressWarnings("unchecked")
	public CircularSeries(Chart chart, String id) {

		super(chart, id);
		this.chart = chart;
		initialize();
		//
		nodeDataModel = new NodeDataModel(id);
		rootNode = nodeDataModel.getRootNode();
		rootPointer = nodeDataModel.getRootPointer();
		compressor = nodeDataModel.getCompressor();
	}

	@Override
	public Color getSliceColor() {

		return sliceColor;
	}

	@Override
	public void setSliceColor(Color sliceColor) {

		this.sliceColor = sliceColor;
	}

	@Override
	public Color getBorderColor() {

		return borderColor;
	}

	@Override
	public void setBorderColor(Color borderColor) {

		this.borderColor = borderColor;
	}

	@Override
	public int getBorderWidth() {

		return borderWidth;
	}

	@Override
	public void setBorderWidth(int borderWidth) {

		this.borderWidth = borderWidth;
	}

	@Override
	public int getBorderStyle() {

		return borderStyle;
	}

	@Override
	public void setBorderStyle(int borderStyle) {

		this.borderStyle = borderStyle;
	}

	@Override
	public Color getSliceColorHighlight() {

		return sliceColorHighlight;
	}

	@Override
	public void setSliceColorHighlight(Color sliceColor) {

		this.sliceColorHighlight = sliceColor;
	}

	@Override
	public Color getBorderColorHighlight() {

		return borderColorHighlight;
	}

	@Override
	public void setBorderColorHighlight(Color borderColor) {

		this.borderColorHighlight = borderColor;
	}

	@Override
	public int getBorderWidthHighlight() {

		return borderWidthHighlight;
	}

	@Override
	public void setBorderWidthHighlight(int borderWidth) {

		this.borderWidthHighlight = borderWidth;
	}

	@Override
	public int getBorderStyleHighlight() {

		return borderStyleHighlight;
	}

	@Override
	public void setBorderStyleHighlight(int borderStyle) {

		this.borderStyleHighlight = borderStyle;
	}

	@Override
	public Compress getCompressor() {

		return (Compress)compressor;
	}

	public Node getRootNode() {

		return rootNode;
	}

	public Node getRootPointer() {

		return nodeDataModel.getRootPointer();
	}

	@Override
	public Node getNodeById(String id) {

		return nodeDataModel.getTree().get(id);
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

		List<Node>[] nodes = nodeDataModel.getNodes();
		int tot = 0, index = 0;
		maxTreeDepth = getRootPointer().getMaxSubTreeDepth() - 1;
		for(int i = 1; i <= maxTreeDepth; i++) {
			tot += nodes[i].size();
		}
		//
		String[] labels = new String[tot];
		for(int i = 1; i <= maxTreeDepth; i++) {
			int len = nodes[i].size();
			for(int j = 0; j != len; j++) {
				labels[index] = nodes[i].get(j).getId();
				index++;
			}
		}
		//
		return labels;
	}

	@Override
	public Color[] getColors() {

		List<Node>[] nodes = nodeDataModel.getNodes();
		int tot = 0, ind = 0;
		for(int i = 1; i <= maxTreeDepth; i++) {
			tot += nodes[i].size();
		}
		//
		Color[] colors = new Color[tot];
		for(int i = 1; i <= maxTreeDepth; i++) {
			int len = nodes[i].size();
			for(int j = 0; j != len; j++) {
				colors[ind] = nodes[i].get(j).getSliceColor();
				ind++;
			}
		}
		//
		return colors;
	}

	@Override
	public void setColor(Color[] colors) {

		int length = getLabels().length;
		if(colors.length != length) {
			// Throw Error
		}
		//
		for(int i = 0; i != length; i++) {
			if(colors[i] != null)
				nodeDataModel.getNodeById(getLabels()[i]).setSliceColor(colors[i]);
		}
	}

	@Override
	public void setColor(String label, Color color) {

		Node node = nodeDataModel.getNodeById(label);
		if(node != null) {
			node.setSliceColor(color);
		}
	}

	@Override
	public void setSeries(String[] labels, double[] values) {

		int length = labels.length;
		if(values.length != length) {
			// throw error
		}
		//
		for(int i = 0; i != length; i++) {
			new Node(labels[i], values[i], rootNode);
		}
		//
		nodeDataModel.update();
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
		//
		Node node = highlightedNode;
		while(node != getRootPointer() && node != getRootNode()) {
			node = node.getParent();
		}
		//
		if(node != getRootPointer()) {
			return;
		}
		//
		borderColorHighlight = (borderColorHighlight == null) ? Display.getDefault().getSystemColor(SWT.COLOR_BLACK) : borderColorHighlight;
		//
		this.highlightedNode = highlightedNode;
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
		gc.setForeground(sliceColor != null ? sliceColor : Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		gc.setLineStyle(borderStyle);
		gc.setLineWidth(borderWidth);
		//
		/*
		 * A DFS function which draws the node after drawing it's children.
		 */
		drawNode(getRootPointer(), gc, xAxis, yAxis);
		/*
		 * highlight just the required node.
		 */
		if(highlightedNode != null && borderColorHighlight != null) {
			//
			gc.setForeground(borderColorHighlight);
			gc.setLineStyle(borderStyleHighlight);
			gc.setLineWidth(borderWidthHighlight);
			// sets the level of the highlighted node.
			int level = highlightedNode.getLevel() - getRootPointer().getLevel() + (this instanceof PieSeries ? 0 : 1);
			// the top-left most coordinates of the square where the highlighted node is drawn.
			int xStart = xAxis.getPixelCoordinate(-level);
			int yStart = yAxis.getPixelCoordinate(level);
			int xWidth = xAxis.getPixelCoordinate(level) - xStart;
			int yWidth = yAxis.getPixelCoordinate(-level) - yStart;
			//
			int angleStart = highlightedNode.getAngleBounds().x;
			int angleWidth = highlightedNode.getAngleBounds().y;
			// drawing the inner and outer arcs of the highlighted node.
			gc.drawArc(xStart, yStart, xWidth, yWidth, angleStart, angleWidth);
			if(highlightedNode == getRootPointer()) {
				return;
			}
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

	protected abstract void drawNode(Node rootNode, GC gc, Axis xAxis, Axis yAxis);

	@Override
	protected void setCompressor() {

		((CompressCircularSeries)compressor).update();
	}

	@Override
	public int getMaxTreeDepth() {

		return rootPointer.getMaxSubTreeDepth() - 1;
	}

	public void setMaxTreeDepth(int maxTreeDepth) {

		this.maxTreeDepth = maxTreeDepth;
	}

	private void initialize() {

		IAxis[] axes = chart.getAxisSet().getAxes();
		for(IAxis axis : axes) {
			axis.getTick().setVisible(false);
			axis.getGrid().setVisible(false);
			axis.getTitle().setVisible(false);
		}
	}

	@Override
	public NodeDataModel getNodeDataModel() {

		return nodeDataModel;
	}

	public void setRootPointer(Node rootPointer) {

		if(highlightedNode != null) {
			if(highlightedNode == getRootPointer()) {
				highlightedNode = null;
			}
			Node node = highlightedNode;
			while(node != getRootPointer() && node != getRootNode()) {
				node = node.getParent();
			}
			if(node != getRootPointer()) {
				highlightedNode = null;
			}
		}
		//
		this.rootPointer = rootPointer;
		nodeDataModel.setRootPointer(rootPointer);
	}

	@Override
	public void setNodeDataModel(NodeDataModel nodeDataModel) {

		this.nodeDataModel = nodeDataModel;
		this.rootNode = this.nodeDataModel.getRootNode();
		this.rootPointer = this.nodeDataModel.getRootPointer();
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
		double primaryY = chart.getAxisSet().getYAxis(0).getDataCoordinate(y);
		return getPieSliceFromPosition(primaryX, primaryY);
	}
}