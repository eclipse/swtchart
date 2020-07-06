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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.internal.compress.Compress;
import org.eclipse.swtchart.model.Node;

@SuppressWarnings("rawtypes")
public class Pie extends CircularSeries {

	@SuppressWarnings("unchecked")
	protected Pie(Chart chart, String id) {

		super(chart, id);
		type = SeriesType.PIE;
	}

	@Override
	public Compress getCompressor() {

		return (Compress)compressor;
	}

	@Override
	public Range getAdjustedRange(Axis axis, int length) {

		return null;
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
	 * A DFS function that draws children first and then the parent overrides
	 * over the section it is drawn over.
	 * Only Visible, nodes are drawn
	 * 
	 * @param node
	 * @param gc
	 * @param xAxis
	 * @param yAxis
	 */
	protected void drawNode(Node node, GC gc, Axis xAxis, Axis yAxis) {

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
	 * update functions that ensures the changes made by user do make sense, and
	 * handles those which do not make sense. If changes can't be made, throws error.
	 */
	@Override
	public void update() {

		model.getRootNode().updateValues();
		/*
		 * update nodes length
		 */
		maxTreeDepth = getMaxTreeDepth();
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

	@Override
	public int getRootNodeLevel() {

		return 0;
	}

	@Override
	public int getMaxTreeDepth() {

		return rootNode.getMaxSubTreeDepth() - 1;
	}
}
