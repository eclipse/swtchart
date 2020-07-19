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
package org.eclipse.swtchart.internal.compress;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.model.IdNodeDataModel;
import org.eclipse.swtchart.model.Node;

public class CompressCircularSeries extends Compress {

	private IdNodeDataModel model;
	private int maxTreeDepth;

	public CompressCircularSeries(IdNodeDataModel model) {

		this.model = model;
	}

	/**
	 * sets the color series of the multiLevel Pie chart.
	 * Uses HSB color model to enable smooth transition of colors across the chart.
	 * Brightness decreases as level of node increases.
	 */
	public void setColors() {

		// int rootNodeLevel = 0;
		Device device = Display.getDefault();
		maxTreeDepth = model.getRootPointer().getMaxSubTreeDepth() - 1;
		List<Node>[] nodes = model.getNodes();
		// traversing each level
		for(int i = 1; i <= maxTreeDepth; i++) {
			// number of nodes in each level
			int len = nodes[i].size();
			// angle allocated to each node.
			float anglePerNode = 360.0f / len;
			// decreasing the brightness linearly as level increases.
			float brightness = Math.max(0, (i - 1) / ((float)maxTreeDepth));
			for(int j = 0; j != len; j++) {
				RGB rgb = new RGB(anglePerNode * j, 1, 1 - brightness);
				Color color = new Color(device, rgb);
				nodes[i].get(j).setColor(color);
			}
		}
	}

	@Override
	protected void addNecessaryPlots(ArrayList<Double> xList, ArrayList<Double> yList, ArrayList<Integer> indexList) {

	}

	public void update() {

		setColors();
	}
}
