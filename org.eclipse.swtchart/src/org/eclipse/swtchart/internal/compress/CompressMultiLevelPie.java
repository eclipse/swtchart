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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.model.IdNodeDataModel;
import org.eclipse.swtchart.model.Node;

public class CompressMultiLevelPie extends Compress {

	private IdNodeDataModel model;
	private HashMap<String, Node> data;
	private List<Map<Integer, Node>> levelNodes;
	private int maxTreeDepth;
	private int colorQuantity[];
	private HashMap<Node, Node[]> connections;
	private int[] colours = {SWT.COLOR_DARK_YELLOW, SWT.COLOR_GREEN, SWT.COLOR_CYAN, SWT.COLOR_RED, SWT.COLOR_DARK_BLUE, SWT.COLOR_DARK_RED, SWT.COLOR_DARK_CYAN, SWT.COLOR_MAGENTA, SWT.COLOR_DARK_GRAY, SWT.COLOR_DARK_MAGENTA, SWT.COLOR_YELLOW, SWT.COLOR_WIDGET_NORMAL_SHADOW, SWT.COLOR_BLUE, SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW};
	public CompressMultiLevelPie(IdNodeDataModel model) {

		this.model = model;
		data = model.getTree();
		colorQuantity = new int[colours.length];
	}

	public void setColors() {

		int pointer = 0;
		maxTreeDepth = model.getRootNode().getMaxSubTreeDepth() - 1;
		// has to be changed, as this method does not ensure that first parent color be
		// assigned before children.
		for(Node node : data.values()) {
			for(int i = 0; i <= 2; i++) {
				if(node.getConnections()[i] != null) {
					if(node.getConnections()[i].getColor() == Display.getDefault().getSystemColor(colours[pointer]))
						pointer = (pointer + 1) % colours.length;
				}
			}
			node.setColor(Display.getDefault().getSystemColor(colours[pointer]));
		}
	}

	@Override
	protected void addNecessaryPlots(ArrayList<Double> xList, ArrayList<Double> yList, ArrayList<Integer> indexList) {

	}

	public void update() {

		maxTreeDepth = model.getRootNode().getMaxSubTreeDepth() - 1;
		levelNodes = new ArrayList<Map<Integer, Node>>();
		// initialising the list with non null Maps
		for(int i = 0; i != maxTreeDepth + 1; i++) {
			Map<Integer, Node> mp = new HashMap<Integer, Node>();
			levelNodes.add(i, mp);
		}
		// nodes contain all the nodes in the tree.
		Iterable<Node> nodes = data.values();
		// putting the node corresponding to it's start angle in a map where all are of the same level.
		for(Node node : nodes) {
			int level = node.getLevel();
			(levelNodes.get(level)).put(node.getAngleBounds().x, node);
		}
		for(Map<Integer, Node> nodesAtLevel : levelNodes) {
			// for a given level, all nodes are here.
			Iterable<Integer> startAngles = nodesAtLevel.keySet();
			for(Integer startAngle : startAngles) {
				Node associatedNode = nodesAtLevel.get(startAngle);
				int endAngle = startAngle + associatedNode.getAngleBounds().y;
				endAngle = (endAngle == 360) ? 0 : endAngle;
				Node adjecentNode = nodesAtLevel.get(endAngle);
				if(adjecentNode != null) {
					associatedNode.getConnections()[1] = adjecentNode;
					adjecentNode.getConnections()[2] = associatedNode;
				}
			}
		}
		setColors();
	}
}
