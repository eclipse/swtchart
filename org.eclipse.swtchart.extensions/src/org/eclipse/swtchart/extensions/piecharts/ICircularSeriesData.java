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
 * Himanshu Balasamanta Orignal API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.piecharts;

import java.util.List;

import org.eclipse.swtchart.model.IdNodeDataModel;
import org.eclipse.swtchart.model.Node;

public interface ICircularSeriesData {

	public IdNodeDataModel getDataModel();

	public void setDataModel(IdNodeDataModel data);

	public Node getRootNode();

	public void setSeries(String[] labels, double[] values);

	public Node getNodeById(String id);

	public List<Node> getSeries();

	public String getId();

	public void setId(String id);

	public ICircularSeriesSettings getSettings();

	public String getNodeClass();

	public void setNodeClass(String nodeClass);

	public String getValueClass();

	public void setValueClass(String valueClass);
}
