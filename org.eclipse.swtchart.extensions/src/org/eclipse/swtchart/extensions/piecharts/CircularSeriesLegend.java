/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.piecharts;

import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.model.NodeDataModel;

public class CircularSeriesLegend<T> extends AbstractCircularSeriesLegend<T> {

	private Node node;
	private NodeDataModel nodeDataModel;

	public CircularSeriesLegend(Node node, NodeDataModel nodeDataModel) {

		this.node = node;
		this.nodeDataModel = nodeDataModel;
	}

	@Override
	public SeriesType getType() {

		return SeriesType.PIE;
	}

	@Override
	public String getId() {

		return node.getId();
	}

	@Override
	public void setVisible(boolean visible) {

		node.setVisible(visible);
	}

	@Override
	public boolean isVisible() {

		return node.isVisible();
	}

	@Override
	public void setVisibleInLegend(boolean visibleInLegend) {

		node.setVisibleInLegend(visibleInLegend);
	}

	@Override
	public boolean isVisibleInLegend() {

		return node.isVisibleInLegend();
	}

	@Override
	public void setDescription(String description) {

		node.setDescription(description);
	}

	@Override
	public String getDescription() {

		return node.getDescription();
	}

	@Override
	public NodeDataModel getNodeDataModel() {

		return nodeDataModel;
	}

	@Override
	public void setNodeDataModel(NodeDataModel nodeDataModel) {

		this.nodeDataModel = nodeDataModel;
	}
}