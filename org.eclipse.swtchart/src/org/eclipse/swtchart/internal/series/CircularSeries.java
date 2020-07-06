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
import org.eclipse.swtchart.Range;
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
	protected int maxTreeDepth;
	protected Color borderColor;
	protected int borderWidth;
	protected int borderStyle;

	@SuppressWarnings("unchecked")
	public CircularSeries(Chart chart, String id) {

		super(chart, id);
		this.chart = chart;
		initialise();
		model = new IdNodeDataModel(id, this);
		rootNode = model.getRootNode();
		compressor = new CompressCircularSeries(model);
		borderColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		borderWidth = 1;
		borderStyle = SWT.LINE_SOLID;
	}

	@Override
	public Range getAdjustedRange(Axis axis, int length) {

		return null;
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
	public Compress getCompressor() {

		return (Compress)compressor;
	}

	public Node getRootNode() {

		return rootNode;
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
		update();
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
		drawNode(rootNode, gc, xAxis, yAxis);
	}

	/**
	 * sets the range of the axis such that the chart drawn is always circular.
	 * 
	 * @param width
	 * @param height
	 * @param xAxis
	 * @param yAxis
	 */
	protected void setBothAxisRange(int width, int height, Axis xAxis, Axis yAxis) {

		maxTreeDepth = getMaxTreeDepth();
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

	protected abstract void drawNode(Node rootNode2, GC gc, Axis xAxis, Axis yAxis);

	@Override
	protected void setCompressor() {

		((CompressCircularSeries)compressor).update();
	}

	private void initialise() {

		IAxis[] axes = chart.getAxisSet().getAxes();
		for(IAxis axis : axes) {
			axis.getTick().setVisible(false);
			axis.getGrid().setVisible(false);
			axis.getTitle().setVisible(false);
		}
	}
}
