/*******************************************************************************
 * Copyright (c) 2010, 2019 VectorGraphics2D project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Erich Seifert - initial API and implementation
 * Michael Seifert - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.intermediate.filters;

import static org.junit.Assert.assertEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.MutableCommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DrawShapeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Group;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetColorCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetStrokeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.StateCommand;
import org.junit.Test;

public class GroupingFilterTest {

	@Test
	public void filtered() {

		MutableCommandSequence resultStream = new MutableCommandSequence();
		resultStream.add(new SetColorCommand(Color.BLACK));
		resultStream.add(new SetStrokeCommand(new BasicStroke(1f)));
		resultStream.add(new DrawShapeCommand(new Line2D.Double(0.0, 1.0, 10.0, 11.0)));
		resultStream.add(new SetTransformCommand(AffineTransform.getTranslateInstance(5.0, 5.0)));
		resultStream.add(new DrawShapeCommand(new Line2D.Double(0.0, 1.0, 5.0, 6.0)));
		List<Command<?>> expectedStream = new LinkedList<>();
		Iterator<Command<?>> resultCloneIterator = resultStream.iterator();
		Group group1 = new Group();
		group1.add(resultCloneIterator.next());
		group1.add(resultCloneIterator.next());
		expectedStream.add(group1);
		expectedStream.add(resultCloneIterator.next());
		Group group2 = new Group();
		group2.add(resultCloneIterator.next());
		expectedStream.add(group2);
		expectedStream.add(resultCloneIterator.next());
		Iterator<Command<?>> expectedIterator = expectedStream.iterator();
		StreamingFilter resultIterator = new GroupingFilter(resultStream) {

			@Override
			protected boolean isGrouped(Command<?> command) {

				return command instanceof StateCommand;
			}
		};
		for(; resultIterator.hasNext() || expectedIterator.hasNext();) {
			Command<?> result = resultIterator.next();
			Command<?> expected = expectedIterator.next();
			assertEquals(expected, result);
		}
	}
}
