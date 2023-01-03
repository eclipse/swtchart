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
 * Philip Wenig - series data model
 *******************************************************************************/
package org.eclipse.swtchart.extensions.piecharts;

import java.util.List;

import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.model.NodeDataModel;
import org.eclipse.swtchart.model.Node;

public interface ICircularSeriesData extends ISeriesData {

	public NodeDataModel getDataModel();

	public void setDataModel(NodeDataModel data);

	public Node getRootNode();

	public void setSeries(String[] labels, double[] values);

	public Node getNodeById(String id);

	public List<Node> getSeries();

	public String getTitle();

	public void setTitle(String id);

	public ICircularSeriesSettings getSettings();

	public String getNodeClass();

	public void setNodeClass(String nodeClass);

	public String getValueClass();

	public void setValueClass(String valueClass);
}