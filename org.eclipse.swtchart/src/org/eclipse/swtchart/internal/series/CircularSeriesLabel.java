package org.eclipse.swtchart.internal.series;

import java.util.function.Function;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.ICircularSeriesLabel;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.model.Node;

public class CircularSeriesLabel extends SeriesLabel implements ICircularSeriesLabel {

	private Position position = Position.Inside;
	private Function<Node, String> labelProvider = Node::getId;

	public CircularSeriesLabel() {

	}

	@Override
	public void setPosition(Position position) {

		this.position = position;
	}

	@Override
	public Position getPosition() {

		return position;
	}

	@Override
	public void setLabelProvider(Function<Node, String> labelProvider) {

		this.labelProvider = labelProvider;
	}

	/* package */ void draw(GC gc, Node node, int level, Axis xAxis, Axis yAxis) {

		if(!isVisible())
			return;
		switch(position) {
			case Inside:
				drawInside(gc, node, level, xAxis, yAxis);
				break;
			case Outside:
				drawOutside(gc, node, level, xAxis, yAxis);
				break;
			default:
				throw new IllegalArgumentException();
		}
	}

	private void drawInside(GC gc, Node node, int level, Axis xAxis, Axis yAxis) {

		String label = labelProvider.apply(node);
		if(label == null)
			return;

		Point angleBounds = node.getAngleBounds();
		// check if pie chart (inner bound) is big enough to draw label
		gc.setFont(getFont());
		Point textSize = gc.textExtent(label);
		Point start = getPixelCoordinate(xAxis, yAxis, (level - 1), angleBounds.x);
		Point end = getPixelCoordinate(xAxis, yAxis, (level - 1), angleBounds.x + angleBounds.y);
		int size = (int)Math.sqrt(Math.pow((end.x - start.x), 2) + Math.pow((end.y - start.y), 2));
		if(size < textSize.y - 4)
			return;
		// calculate text angle
		int angle = angleBounds.x + angleBounds.y / 2;
		Point innerBound = getPixelCoordinate(xAxis, yAxis, (level - 1), angle);
		Point outerBound = getPixelCoordinate(xAxis, yAxis, level, angle);
		Transform t = new Transform(gc.getDevice());
		Point textPosition;
		if(angle >= 90 && angle <= 270) {
			textPosition = outerBound;
			t.translate(textPosition.x, textPosition.y);
			t.rotate((-angle + 180));
		} else {
			textPosition = innerBound;
			t.translate(textPosition.x, textPosition.y);
			t.rotate(-angle);
		}
		gc.setTransform(t);
		gc.setForeground(getForeground());
		int length = (int)Math.sqrt(Math.pow((outerBound.x - innerBound.x), 2) + Math.pow((outerBound.y - innerBound.y), 2));
		gc.setClipping(0, 0 - (textSize.y / 2), length - 3, textSize.y);
		gc.drawString(label, 2, 0 - (textSize.y / 2), true);
		gc.setClipping((Rectangle)null);
		gc.setTransform(null);
	}

	private void drawOutside(GC gc, Node node, int level, Axis xAxis, Axis yAxis) {

		String label = labelProvider.apply(node);
		if(label == null)
			return;

		Point angleBounds = node.getAngleBounds();
		int angle = angleBounds.x + angleBounds.y / 2;
		Point textSize = gc.textExtent(label);
		// some heuristic to check if there is enough space to render a
		// label
		Point start = getPixelCoordinate(xAxis, yAxis, (level - 1), angleBounds.x);
		Point end = getPixelCoordinate(xAxis, yAxis, (level - 1), angleBounds.x + angleBounds.y);
		if(Math.abs(start.y - end.y) < 4)
			return;
		Point point = getPixelCoordinate(xAxis, yAxis, level * 1.03, angle);
		int x = angle >= 90 && angle <= 270 ? point.x - textSize.x : point.x;
		Font oldFont = gc.getFont();
		gc.setForeground(getForeground());
		gc.setFont(getFont());
		gc.drawString(label, x, point.y - (textSize.y / 2), true);
		gc.setFont(oldFont);
	}

	protected final Point getPixelCoordinate(IAxis xAxis, IAxis yAxis, double pieLevel, int angle) {

		double xCoordinate = pieLevel * Math.cos(Math.toRadians(angle));
		double yCoordinate = pieLevel * Math.sin(Math.toRadians(angle));
		return new Point(xAxis.getPixelCoordinate(xCoordinate), yAxis.getPixelCoordinate(yCoordinate));
	}
}
