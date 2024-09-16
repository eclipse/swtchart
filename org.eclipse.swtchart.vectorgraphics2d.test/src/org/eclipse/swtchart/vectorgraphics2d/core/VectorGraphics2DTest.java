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
package org.eclipse.swtchart.vectorgraphics2d.core;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.CreateCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DisposeCommand;
import org.eclipse.swtchart.vectorgraphics2d.util.GraphicsUtils;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class VectorGraphics2DTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testVectorGraphics2DEmitsCreateCommand() {

		VectorGraphics2D g = new VectorGraphics2D();
		CommandSequence commands = g.getCommands();
		Iterator<Command<?>> commandIterator = commands.iterator();
		assertTrue(commandIterator.hasNext());
		Command<?> firstCommand = commandIterator.next();
		assertThat(firstCommand, instanceOf(CreateCommand.class));
		// TODO: Move this assertion into a separate test case
		assertEquals(g, ((CreateCommand)firstCommand).getValue());
	}

	@Test
	public void testCreateEmitsCreateCommand() {

		VectorGraphics2D g = new VectorGraphics2D();
		CommandSequence gCommands = g.getCommands();
		Iterator<Command<?>> gCommandIterator = gCommands.iterator();
		CreateCommand gCreateCommand = (CreateCommand)gCommandIterator.next();
		VectorGraphics2D g2 = (VectorGraphics2D)g.create();
		CreateCommand g2CreateCommand = null;
		for(Command<?> g2Command : g2.getCommands()) {
			if(g2Command instanceof CreateCommand) {
				g2CreateCommand = (CreateCommand)g2Command;
			}
		}
		assertNotEquals(gCreateCommand, g2CreateCommand);
		assertEquals(g2, g2CreateCommand.getValue());
	}

	@Test
	public void testDisposeCommandEmitted() {

		VectorGraphics2D g = new VectorGraphics2D();
		g.setColor(Color.RED);
		VectorGraphics2D g2 = (VectorGraphics2D)g.create();
		g2.setColor(Color.BLUE);
		g2.dispose();
		CommandSequence commands = g.getCommands();
		Command<?> lastCommand = null;
		for(Command<?> command : commands) {
			lastCommand = command;
		}
		assertTrue(lastCommand instanceof DisposeCommand);
		assertEquals(Color.BLUE, ((DisposeCommand)lastCommand).getValue().getColor());
	}

	@Test
	public void testClipIntersectsClipRectangle() {

		VectorGraphics2D vg2d = new VectorGraphics2D();
		Rectangle2D currentClipShape = new Rectangle2D.Double(5, 10, 20, 30);
		vg2d.setClip(currentClipShape);
		Rectangle2D newClipShape = new Rectangle2D.Double(10, 20, 30, 40);
		vg2d.clip(newClipShape);
		Rectangle2D intersection = currentClipShape.createIntersection(newClipShape);
		assertTrue(GraphicsUtils.equals(vg2d.getClip(), intersection));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testClipClearsClippingShapeWhenNullIsPassed() {

		VectorGraphics2D vg2d = new VectorGraphics2D();
		Rectangle2D clipShape = new Rectangle2D.Double(5, 10, 20, 30);
		vg2d.setClip(clipShape);
		vg2d.clip(null);
		assertThat(vg2d.getClip(), is(nullValue()));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetBackgroundSetsBackgroundColor() {

		VectorGraphics2D vg2d = new VectorGraphics2D();
		Color backgroundColor = Color.DARK_GRAY;
		vg2d.setBackground(backgroundColor);
		assertThat(vg2d.getBackground(), is(backgroundColor));
	}
}
