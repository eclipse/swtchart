/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.piecharts;

import java.util.List;

import org.eclipse.swtchart.model.IdNodeDataModel;
import org.eclipse.swtchart.model.Node;

public class CircularSeriesData implements ICircularSeriesData {

	private IdNodeDataModel model;
	private Node rootNode;
	private ICircularSeriesSettings pieSeriesSettings;
	private String nodeClass;
	private String valueClass;

	public CircularSeriesData() {

		model = new IdNodeDataModel();
		rootNode = model.getRootNode();
		this.pieSeriesSettings = new CircularSeriesSettings();
		nodeClass = "Node";
		valueClass = "Value";
	}

	public CircularSeriesData(ICircularSeriesData pieSeriesData) {

		this.pieSeriesSettings = new CircularSeriesSettings();
		/*
		 * Set the default description.
		 */
		this.pieSeriesSettings.setDescription(pieSeriesData.getTitle());
	}

	public String getTitle() {

		return model.getId();
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
		model.update();
	}

	@Override
	public List<Node> getSeries() {

		return rootNode.getChildren();
	}

	@Override
	public IdNodeDataModel getDataModel() {

		return model;
	}

	public void setDataModel(IdNodeDataModel data) {

		this.model = data;
	}

	@Override
	public Node getRootNode() {

		return rootNode;
	}

	@Override
	public Node getNodeById(String id) {

		return model.getNodeById(id);
	}

	@Override
	public ICircularSeriesSettings getSettings() {

		return pieSeriesSettings;
	}

	@Override
	public void setTitle(String id) {

		model.setId(id);
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
