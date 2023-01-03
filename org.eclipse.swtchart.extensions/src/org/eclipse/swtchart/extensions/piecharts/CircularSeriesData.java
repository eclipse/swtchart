/*******************************************************************************
 * Copyright (c) 2020, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.piecharts;

import java.util.List;

import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.model.NodeDataModel;

public class CircularSeriesData extends SeriesData implements ICircularSeriesData {

	public static final String ID = "Circular Series";
	//
	private NodeDataModel nodeDataModel;
	private Node rootNode;
	private ICircularSeriesSettings seriesSettings;
	private String nodeClass;
	private String valueClass;

	public CircularSeriesData() {

		super(new double[0], new double[0], ID);
		nodeDataModel = new NodeDataModel();
		rootNode = nodeDataModel.getRootNode();
		this.seriesSettings = new CircularSeriesSettings();
		nodeClass = "Node";
		valueClass = "Value";
	}

	public CircularSeriesData(ICircularSeriesData seriesData) {

		super(new double[0], new double[0], ID);
		this.seriesSettings = new CircularSeriesSettings();
		this.seriesSettings.setDescription(seriesData.getTitle());
	}

	public String getTitle() {

		return nodeDataModel.getId();
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
		nodeDataModel.update();
	}

	@Override
	public List<Node> getSeries() {

		return rootNode.getChildren();
	}

	@Override
	public NodeDataModel getDataModel() {

		return nodeDataModel;
	}

	public void setDataModel(NodeDataModel data) {

		this.nodeDataModel = data;
	}

	@Override
	public Node getRootNode() {

		return rootNode;
	}

	@Override
	public Node getNodeById(String id) {

		return nodeDataModel.getNodeById(id);
	}

	@Override
	public ICircularSeriesSettings getSettings() {

		return seriesSettings;
	}

	@Override
	public void setTitle(String id) {

		nodeDataModel.setId(id);
	}

	@Override
	public String getNodeClass() {

		return nodeClass;
	}

	@Override
	public void setNodeClass(String nodeClass) {

		this.nodeClass = nodeClass;
	}

	@Override
	public String getValueClass() {

		return valueClass;
	}

	@Override
	public void setValueClass(String valueClass) {

		this.valueClass = valueClass;
	}
}