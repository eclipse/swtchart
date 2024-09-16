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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetHintCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.StateCommand;

public class OptimizeFilter extends StreamingFilter {

	private final Queue<Command<?>> buffer;

	public OptimizeFilter(CommandSequence stream) {

		super(stream);
		buffer = new LinkedList<>();
	}

	@Override
	public boolean hasNext() {

		return super.hasNext();
	}

	@Override
	public Command<?> next() {

		if(buffer.isEmpty()) {
			return super.next();
		}
		return buffer.poll();
	}

	@Override
	protected List<Command<?>> filter(Command<?> command) {

		if(!isStateChange(command)) {
			return Collections.singletonList(command);
		}
		Iterator<Command<?>> i = buffer.iterator();
		Class<?> cls = command.getClass();
		while(i.hasNext()) {
			if(cls.equals(i.next().getClass())) {
				i.remove();
			}
		}
		buffer.add(command);
		return null;
	}

	private static boolean isStateChange(Command<?> command) {

		return (command instanceof StateCommand) && !(command instanceof AffineTransformCommand) && !(command instanceof SetHintCommand);
	}
}
