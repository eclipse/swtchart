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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.MutableCommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DrawShapeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetColorCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetStrokeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import org.junit.Test;

public class StreamingFilterTest {

	@Test
	public void filterNone() {

		MutableCommandSequence stream = new MutableCommandSequence();
		stream.add(new SetColorCommand(Color.BLACK));
		stream.add(new SetStrokeCommand(new BasicStroke(1f)));
		stream.add(new DrawShapeCommand(new Line2D.Double(0.0, 1.0, 10.0, 11.0)));
		stream.add(new SetTransformCommand(AffineTransform.getTranslateInstance(5.0, 5.0)));
		stream.add(new DrawShapeCommand(new Line2D.Double(0.0, 1.0, 5.0, 6.0)));
		Iterator<Command<?>> unfiltered = stream.iterator();
		StreamingFilter filtered = new StreamingFilter(stream) {

			@Override
			protected List<Command<?>> filter(Command<?> command) {

				return Collections.<Command<?>> singletonList(command);
			}
		};
		while(filtered.hasNext() || unfiltered.hasNext()) {
			Command<?> expected = unfiltered.next();
			Command<?> result = filtered.next();
			assertEquals(expected, result);
		}
	}

	@Test
	public void filterAll() {

		MutableCommandSequence stream = new MutableCommandSequence();
		stream.add(new SetColorCommand(Color.BLACK));
		stream.add(new SetStrokeCommand(new BasicStroke(1f)));
		stream.add(new DrawShapeCommand(new Line2D.Double(0.0, 1.0, 10.0, 11.0)));
		stream.add(new SetTransformCommand(AffineTransform.getTranslateInstance(5.0, 5.0)));
		stream.add(new DrawShapeCommand(new Line2D.Double(0.0, 1.0, 5.0, 6.0)));
		Iterator<Command<?>> unfiltered = stream.iterator();
		StreamingFilter filtered = new StreamingFilter(stream) {

			@Override
			protected List<Command<?>> filter(Command<?> command) {

				return null;
			}
		};
		assertTrue(unfiltered.hasNext());
		assertFalse(filtered.hasNext());
	}

	@Test
	public void duplicate() {

		MutableCommandSequence stream = new MutableCommandSequence();
		stream.add(new SetColorCommand(Color.BLACK));
		stream.add(new SetStrokeCommand(new BasicStroke(1f)));
		stream.add(new DrawShapeCommand(new Line2D.Double(0.0, 1.0, 10.0, 11.0)));
		stream.add(new SetTransformCommand(AffineTransform.getTranslateInstance(5.0, 5.0)));
		stream.add(new DrawShapeCommand(new Line2D.Double(0.0, 1.0, 5.0, 6.0)));
		Iterator<Command<?>> unfiltered = stream.iterator();
		StreamingFilter filtered = new StreamingFilter(stream) {

			@Override
			protected List<Command<?>> filter(Command<?> command) {

				return Arrays.asList(command, command);
			}
		};
		while(filtered.hasNext() || unfiltered.hasNext()) {
			Command<?> expected = unfiltered.next();
			Command<?> result1 = filtered.next();
			Command<?> result2 = filtered.next();
			assertEquals(expected, result1);
			assertEquals(expected, result2);
		}
	}
}
