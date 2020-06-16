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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IMultiLevelPie;
import org.eclipse.swtchart.IPieSeries;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.internal.compress.Compress;
import org.eclipse.swtchart.internal.compress.CompressMultiLevelPie;
import org.eclipse.swtchart.model.IdNodeDataModel;
import org.eclipse.swtchart.model.Node;

@SuppressWarnings("rawtypes")
public class MultiLevelPie extends Series implements IMultiLevelPie, IPieSeries {

	private Chart chart;
	private IdNodeDataModel model;
	private Node rootNode;
	private int maxTreeDepth;

	@SuppressWarnings("unchecked")
	protected MultiLevelPie(Chart chart, String id) {

		super(chart, id);
		this.chart = chart;
		type = SeriesType.MULTI_LEVEL_PIE;
		initialise();
		model = new IdNodeDataModel(id, this);
		rootNode = model.getRootNode();
		compressor = new CompressMultiLevelPie(model);
	}

	@Override
	public Compress getCompressor() {

		return (Compress)compressor;
	}

	@Override
	public Node getRootNode() {

		return rootNode;
	}

	@Override
	public Node getNodeById(String id) {

		return model.getTree().get(id);
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
		update();
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

		String[] labels = new String[model.getTree().size() - 1];
		List<Node>[] nodes = model.getNodes();
		int index = 0;
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

		Color[] colors = new Color[model.getTree().size() - 1];
		List<Node>[] nodes = model.getNodes();
		int ind = 0;
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
	public void setColor(String label, Color color) {

		Node node = model.getNodeById(label);
		if(node == null) {
			// throw error
			return;
		}
		node.setColor(color);
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
	public Range getAdjustedRange(Axis axis, int length) {

		return null;
	}

	@Override
	protected void setCompressor() {

		((CompressMultiLevelPie)compressor).update();
	}

	@Override
	protected void draw(GC gc, int width, int height, Axis xAxis, Axis yAxis) {

		/*
		 * Sets the x and y range of the axes so that the pie slices are perfectly circular.
		 */
		setBothAxisRange(width, height, xAxis, yAxis);
		/*
		 * Black color for drawing the outline
		 */
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		/*
		 * A DFS function which draws the node after drawing it's children.
		 */
		drawNode(rootNode, gc, xAxis, yAxis);
	}

	/**
	 * A DFS function that draws children first and then the parent overrides
	 * over the section it is drawn over.
	 * Only Visible, nodes are drawn
	 * 
	 * @param node
	 * @param gc
	 * @param xAxis
	 * @param yAxis
	 */
	private void drawNode(Node node, GC gc, Axis xAxis, Axis yAxis) {

		// children drawn first as parent overrides it's section of drawing
		if(!node.getChildren().isEmpty()) {
			for(Node nodes : node.getChildren()) {
				drawNode(nodes, gc, xAxis, yAxis);
			}
		}
		if(node.isVisible() == false)
			return;
		int level = node.getLevel();
		/*
		 * the center of the chart is (0,0). The x and y axis are set such that
		 * a node at level = i, will be drawn starting from (-level,level), till (level,-level).
		 */
		int xStart = xAxis.getPixelCoordinate(-level);
		int yStart = yAxis.getPixelCoordinate(level);
		int xWidth = xAxis.getPixelCoordinate(level) - xStart;
		int yWidth = yAxis.getPixelCoordinate(-level) - yStart;
		int xZero = xAxis.getPixelCoordinate(0);
		int yZero = yAxis.getPixelCoordinate(0);
		int angleStart = node.getAngleBounds().x,
				angleWidth = node.getAngleBounds().y;
		gc.setBackground(node.getColor());

		// coloring the pie "slice"
		gc.fillArc(xStart, yStart, xWidth, yWidth, angleStart, angleWidth);
		// drawing the arc boundary
		gc.drawArc(xStart, yStart, xWidth, yWidth, angleStart, angleWidth);
		/*
		 * drawing the start boundary
		 */
		double xStartCoordinate = level * Math.cos(Math.toRadians(angleStart));
		double yStartCoordinate = level * Math.sin(Math.toRadians(angleStart));
		int xStartPixelCoordinate = xAxis.getPixelCoordinate(xStartCoordinate);
		int yStartPixelCoordinate = yAxis.getPixelCoordinate(yStartCoordinate);
		//
		gc.drawLine(xZero, yZero, xStartPixelCoordinate, yStartPixelCoordinate);
		/*
		 * drawing the end boundary
		 */
		double xEndCoordinate = level * Math.cos(Math.toRadians(angleStart + angleWidth));
		double yEndCoordinate = level * Math.sin(Math.toRadians(angleStart + angleWidth));
		int xEndPixelCoordinate = xAxis.getPixelCoordinate(xEndCoordinate);
		int yEndPixelCoordinate = yAxis.getPixelCoordinate(yEndCoordinate);
		//
		gc.drawLine(xZero, yZero, xEndPixelCoordinate, yEndPixelCoordinate);
	}

	/**
	 * sets the range of the axis such that the chart drawn is always circular.
	 * 
	 * @param width
	 * @param height
	 * @param xAxis
	 * @param yAxis
	 */
	private void setBothAxisRange(int width, int height, Axis xAxis, Axis yAxis) {

		maxTreeDepth = rootNode.getMaxSubTreeDepth() - 1;
		xAxis.setRange(new Range(-maxTreeDepth, maxTreeDepth));
		yAxis.setRange(new Range(-maxTreeDepth, maxTreeDepth));
		if(width > height) {
			if(xAxis.isHorizontalAxis()) {
				double ratio = 2 * maxTreeDepth * width / (double)height;
				xAxis.setRange(new Range(-maxTreeDepth, ratio - maxTreeDepth));
			} else {
				double ratio = 2 * maxTreeDepth * width / (double)height;
				yAxis.setRange(new Range(-maxTreeDepth, ratio - maxTreeDepth));
			}
		} else {
			if(xAxis.isHorizontalAxis()) {
				double ratio = 2 * maxTreeDepth * height / (double)width;
				yAxis.setRange(new Range(maxTreeDepth - ratio, maxTreeDepth));
			} else {
				double ratio = 2 * maxTreeDepth * height / (double)width;
				xAxis.setRange(new Range(maxTreeDepth - ratio, maxTreeDepth));
			}
		}
	}

	/**
	 * Initialize the chart make it fit for drawing a pie chart.
	 */
	private void initialise() {

		IAxis[] axes = chart.getAxisSet().getAxes();
		for(IAxis axis : axes) {
			axis.getTick().setVisible(false);
			axis.getGrid().setVisible(false);
			axis.getTitle().setVisible(false);
		}
	}

	/**
	 * update functions that ensures the changes made by user do make sense, and
	 * handles those which do not make sense. If changes can't be made, throws error.
	 */
	@SuppressWarnings("unchecked")
	public void update() {

		model.getRootNode().updateValues();
		/*
		 * update nodes length
		 */
		maxTreeDepth = model.getRootNode().getMaxSubTreeDepth() - 1;
		model.setNodes(new ArrayList[maxTreeDepth + 1]);
		//
		ArrayList<Node>[] node = (ArrayList<Node>[])model.getNodes();
		for(int i = 1; i <= maxTreeDepth; i++) {
			node[i] = new ArrayList<Node>();
		}
		model.setNodes(node);
		/*
		 * angular bounds
		 */
		model.getRootNode().updateAngularBounds();
		//
		model.getRootNode().setVisibility(true);
		//
		setCompressor();
	}

}
