package org.eclipse.swtchart;

import java.util.function.Function;

import org.eclipse.swtchart.model.Node;

public interface ICircularSeriesLabel extends ISeriesLabel {

	public enum Position {
		Inside, Outside
	}

	void setPosition(Position position);

	Position getPosition();

	void setLabelProvider(Function<Node, String> labelProvider);
}
