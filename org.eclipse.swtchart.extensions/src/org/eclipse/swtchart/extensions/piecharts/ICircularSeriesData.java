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

	public ICircularSeriesSettings getSettings();
}
