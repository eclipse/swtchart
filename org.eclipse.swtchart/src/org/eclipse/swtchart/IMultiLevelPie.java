package org.eclipse.swtchart;

import java.util.HashMap;

import org.eclipse.swt.graphics.Color;
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
	 * creates a series with given parameter data.
	 * Nodes are created with corresponding values.
	 * 
	 * @param labels
	 * @param values
	 */
	public void addSeries(String[] labels, double[] values);
	
	/**
	 * gets the MultiLevelPie Series as Series.
	 * 
	 * @return
	 */
	public HashMap<String, Node> getSeries();

	/**
	 * adds a node to the primary series.
	 * 
	 * @param id
	 * @param val
	 */
	public void addNode(String id, double val);

	/**
	 * @return labels for the legend to draw from
	 */
	public String[] getLabels();

	/**
	 * @return The colors of the nodes so that the legend can be drawn successfully.
	 */
	public Color[] getColors();

}
