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

import java.util.HashMap;
import java.util.List;

import org.eclipse.swtchart.model.IdNodeDataModel;
import org.eclipse.swtchart.model.Node;

public class CircularSeriesData implements ICircularSeriesData {

	private IdNodeDataModel model;
	private Node rootNode;
	private ICircularSeriesSettings pieSeriesSettings;
	private HashMap<String, Node> tree;

	public CircularSeriesData() {

		model = new IdNodeDataModel();
		rootNode = model.getRootNode();
		this.pieSeriesSettings = new CircularSeriesSettings();
	}

	public CircularSeriesData(ICircularSeriesData pieSeriesData) {

		this.pieSeriesSettings = new CircularSeriesSettings();
		/*
		 * Set the default description.
		 */
		this.pieSeriesSettings.setDescription(pieSeriesData.getId());
	}

	public String getId() {

		return rootNode.getId();
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
}
