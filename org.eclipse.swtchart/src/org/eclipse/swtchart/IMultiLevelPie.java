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
package org.eclipse.swtchart;

import java.util.List;

import org.eclipse.swtchart.model.Node;

public interface IMultiLevelPie {

	/**
	 * This method is not to be used by the suer.
	 * 
	 * @return gets the rootNode of the Multi-Level-Pie Series.
	 */
	public Node getRootNode();
	
	/**
	 * fetches a node with it's Id.
	 * 
	 * @param id
	 * @return
	 */
	public Node getNodeById(String id);
	
	/**
	 * gets the MultiLevelPie Series as Series.
	 * 
	 * @return
	 */
	public List<Node> getSeries();

	/**
	 * adds a node to the primary series.
	 * 
	 * @param id
	 * @param val
	 */
	public void addNode(String id, double val);

}
