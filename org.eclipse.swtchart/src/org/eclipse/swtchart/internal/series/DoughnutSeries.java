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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.model.Node;

public class DoughnutSeries extends CircularSeries {

	@SuppressWarnings("unchecked")
	protected DoughnutSeries(Chart chart, String id) {

		super(chart, id);
		type = SeriesType.DOUGHNUT;
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
		//
		if(node.isVisible() == false) {
			return;
		}
		//
		int level = node.getLevel() - getRootPointer().getLevel() + 1;
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
		int angleStart = node.getAngleBounds().x;
		int angleWidth = node.getAngleBounds().y;
		/*
		 * Slice/Bounds
		 */
		gc.setBackground(node.getSliceColor());
		gc.fillArc(xStart, yStart, xWidth, yWidth, angleStart, angleWidth);
		gc.drawArc(xStart, yStart, xWidth, yWidth, angleStart, angleWidth);
		/*
		 * drawing the start boundary
		 */
		double xStartCoordinate = level * Math.cos(Math.toRadians(angleStart));
		double yStartCoordinate = level * Math.sin(Math.toRadians(angleStart));
		int xStartPixelCoordinate = xAxis.getPixelCoordinate(xStartCoordinate);
		int yStartPixelCoordinate = yAxis.getPixelCoordinate(yStartCoordinate);
		//
		if(node != getRootPointer()) {
			gc.drawLine(xZero, yZero, xStartPixelCoordinate, yStartPixelCoordinate);
		}
		/*
		 * drawing the end boundary
		 */
		double xEndCoordinate = level * Math.cos(Math.toRadians(angleStart + angleWidth));
		double yEndCoordinate = level * Math.sin(Math.toRadians(angleStart + angleWidth));
		int xEndPixelCoordinate = xAxis.getPixelCoordinate(xEndCoordinate);
		int yEndPixelCoordinate = yAxis.getPixelCoordinate(yEndCoordinate);
		//
		if(node != getRootPointer()) {
			gc.drawLine(xZero, yZero, xEndPixelCoordinate, yEndPixelCoordinate);
		}
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

		setMaxTreeDepth(getRootPointer().getMaxSubTreeDepth() - 1);
		// keeps the chart boundaries from overflowing the borders.
		double rangeMax = (getMaxTreeDepth() + 1) * 1.05;
		xAxis.setRange(new Range(-rangeMax, rangeMax));
		yAxis.setRange(new Range(-rangeMax, rangeMax));
		if(width > height) {
			if(xAxis.isHorizontalAxis()) {
				double ratio = 2 * rangeMax * width / (double)height;
				xAxis.setRange(new Range(-ratio / 2, ratio / 2));
			} else {
				double ratio = 2 * rangeMax * width / (double)height;
				yAxis.setRange(new Range(ratio / 2, ratio / 2));
			}
		} else {
			if(xAxis.isHorizontalAxis()) {
				double ratio = 2 * rangeMax * height / (double)width;
				yAxis.setRange(new Range(-ratio / 2, ratio / 2));
			} else {
				double ratio = 2 * rangeMax * height / (double)width;
				xAxis.setRange(new Range(-ratio / 2, ratio / 2));
			}
		}
	}

	@Override
	public Range getAdjustedRange(Axis axis, int length) {

		setMaxTreeDepth(getRootNode().getMaxSubTreeDepth() - 1);
		return new Range(-getMaxTreeDepth() - 1, getMaxTreeDepth() + 1);
	}

	public Node getPieSliceFromPosition(double primaryValueX, double primaryValueY) {

		double radius = Math.sqrt(primaryValueX * primaryValueX + primaryValueY * primaryValueY);
		int level = ((int)radius);
		Node node = null;
		double angleOfInspection = Math.atan2(primaryValueY, primaryValueX);
		if(angleOfInspection < 0.0) {
			angleOfInspection += 2 * Math.PI;
		}
		//
		if(level < getNodeDataModel().getNodes().length) {
			for(Node nodeX : getNodeDataModel().getNodes()[level]) {
				double lowerBound = (nodeX.getAngleBounds().x * Math.PI) / (double)180.0;
				double upperBound = ((nodeX.getAngleBounds().x + nodeX.getAngleBounds().y) * Math.PI) / (double)180.0;
				if((lowerBound <= angleOfInspection) && (upperBound >= angleOfInspection)) {
					node = nodeX;
					break;
				}
			}
		}
		//
		return node;
	}
}