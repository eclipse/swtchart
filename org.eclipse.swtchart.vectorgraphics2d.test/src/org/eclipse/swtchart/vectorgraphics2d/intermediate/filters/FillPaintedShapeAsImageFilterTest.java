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

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.MutableCommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DrawImageCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.FillShapeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.RotateCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetPaintCommand;
import org.junit.Test;

public class FillPaintedShapeAsImageFilterTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testFillShapeReplacedWithDrawImage() {

		MutableCommandSequence commands = new MutableCommandSequence();
		commands.add(new SetPaintCommand(new GradientPaint(0.0f, 0.0f, Color.BLACK, 100.0f, 100.0f, Color.WHITE)));
		commands.add(new RotateCommand(10.0, 4.0, 2.0));
		commands.add(new FillShapeCommand(new Rectangle2D.Double(10.0, 10.0, 100.0, 100.0)));
		FillPaintedShapeAsImageFilter filter = new FillPaintedShapeAsImageFilter(commands);
		assertThat(filter, hasItem(any(DrawImageCommand.class)));
		assertThat(filter, not(hasItem(any(FillShapeCommand.class))));
	}

	@Test
	public void testFillShapeNotReplacedWithoutPaintCommand() {

		MutableCommandSequence commands = new MutableCommandSequence();
		commands.add(new RotateCommand(10.0, 4.0, 2.0));
		commands.add(new FillShapeCommand(new Rectangle2D.Double(10.0, 10.0, 100.0, 100.0)));
		FillPaintedShapeAsImageFilter filter = new FillPaintedShapeAsImageFilter(commands);
		Iterator<Command<?>> filterIterator = filter.iterator();
		for(Command<?> command : commands) {
			assertEquals(command, filterIterator.next());
		}
		assertFalse(filterIterator.hasNext());
	}
}
