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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.awt.geom.AffineTransform;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.MutableCommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.CreateCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DisposeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.TransformCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.TranslateCommand;
import org.junit.Test;

public class AbsoluteToRelativeTransformsFilterTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testSetTransformCommandReplaced() {

		AffineTransform absoluteTransform = new AffineTransform();
		absoluteTransform.rotate(42.0);
		absoluteTransform.translate(4.0, 2.0);
		CommandSequence commands = wrapCommands(new SetTransformCommand(absoluteTransform));
		AbsoluteToRelativeTransformsFilter filter = new AbsoluteToRelativeTransformsFilter(commands);
		assertThat(filter, not(hasItem(any(SetTransformCommand.class))));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAbsoluteAndRelativeTransformsIdentical() {

		AffineTransform absoluteTransform = new AffineTransform();
		absoluteTransform.rotate(42.0);
		absoluteTransform.translate(4.0, 2.0);
		CommandSequence commands = wrapCommands(new SetTransformCommand(absoluteTransform));
		AbsoluteToRelativeTransformsFilter filter = new AbsoluteToRelativeTransformsFilter(commands);
		filter.next();
		AffineTransform relativeTransform = ((TransformCommand)filter.next()).getValue();
		assertThat(relativeTransform, is(absoluteTransform));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testTranslateCorrect() {

		AffineTransform absoluteTransform = new AffineTransform();
		absoluteTransform.scale(2.0, 2.0);
		absoluteTransform.translate(4.2, 4.2); // (8.4, 8.4)
		CommandSequence commands = wrapCommands(new TranslateCommand(4.0, 2.0), new SetTransformCommand(absoluteTransform));
		AbsoluteToRelativeTransformsFilter filter = new AbsoluteToRelativeTransformsFilter(commands);
		TransformCommand transformCommand = null;
		while(filter.hasNext()) {
			Command<?> filteredCommand = filter.next();
			if(filteredCommand instanceof TransformCommand) {
				transformCommand = (TransformCommand)filteredCommand;
			}
		}
		AffineTransform relativeTransform = transformCommand.getValue();
		assertThat(relativeTransform.getTranslateX(), is(4.4));
		assertThat(relativeTransform.getTranslateY(), is(6.4));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRelativeTransformAfterDispose() {

		AffineTransform absoluteTransform = new AffineTransform();
		absoluteTransform.rotate(42.0);
		absoluteTransform.translate(4.0, 2.0);
		CommandSequence commands = wrapCommands(new CreateCommand(null), new TransformCommand(absoluteTransform), new DisposeCommand(null), new SetTransformCommand(absoluteTransform));
		AbsoluteToRelativeTransformsFilter filter = new AbsoluteToRelativeTransformsFilter(commands);
		TransformCommand lastTransformCommand = null;
		for(Command<?> filteredCommand : filter) {
			if(filteredCommand instanceof TransformCommand) {
				lastTransformCommand = (TransformCommand)filteredCommand;
			}
		}
		assertThat(lastTransformCommand.getValue(), is(absoluteTransform));
	}

	private CommandSequence wrapCommands(Command<?>... commands) {

		MutableCommandSequence commandSequence = new MutableCommandSequence();
		commandSequence.add(new CreateCommand(null));
		for(Command<?> command : commands) {
			commandSequence.add(command);
		}
		commandSequence.add(new DisposeCommand(null));
		return commandSequence;
	}
}
