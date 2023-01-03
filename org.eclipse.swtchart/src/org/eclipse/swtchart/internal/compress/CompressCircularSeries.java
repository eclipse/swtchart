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
package org.eclipse.swtchart.internal.compress;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.model.NodeDataModel;

public class CompressCircularSeries extends Compress {

	private NodeDataModel nodeDataModel;
	private int maxTreeDepth;

	public CompressCircularSeries(NodeDataModel nodeDataModel) {

		this.nodeDataModel = nodeDataModel;
	}

	@Override
	protected void addNecessaryPlots(ArrayList<Double> xList, ArrayList<Double> yList, ArrayList<Integer> indexList) {

	}

	public void update() {

		updateColors();
	}

	/**
	 * sets the color series of the multiLevel Pie chart.
	 * Uses HSB color model to enable smooth transition of colors across the chart.
	 * Brightness decreases as level of node increases.
	 */
	private void updateColors() {

		Device device = Display.getDefault();
		maxTreeDepth = nodeDataModel.getRootPointer().getMaxSubTreeDepth() - 1;
		List<Node>[] nodes = nodeDataModel.getNodes();
		/*
		 * Traversing each level
		 */
		for(int i = 1; i <= maxTreeDepth; i++) {
			int length = nodes[i].size();
			float anglePerNode = 360.0f / length;
			float brightness = Math.max(0, (i - 1) / ((float)maxTreeDepth));
			for(int j = 0; j != length; j++) {
				RGB rgb = new RGB(anglePerNode * j, 1, 1 - brightness);
				Color color = new Color(device, rgb);
				nodes[i].get(j).setSliceColor(color);
			}
		}
	}
}