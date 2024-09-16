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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;

public abstract class StreamingFilter implements Iterator<Command<?>>, Filter {

	private final Queue<Command<?>> buffer;
	private final Iterator<Command<?>> iterator;

	public StreamingFilter(CommandSequence stream) {

		buffer = new LinkedList<>();
		iterator = stream.iterator();
	}

	public Iterator<Command<?>> iterator() {

		return this;
	}

	public boolean hasNext() {

		findNextCommand();
		return !buffer.isEmpty();
	}

	private void findNextCommand() {

		while(buffer.isEmpty() && iterator.hasNext()) {
			Command<?> command = iterator.next();
			List<Command<?>> commands = filter(command);
			if(commands != null) {
				buffer.addAll(commands);
			}
		}
	}

	public Command<?> next() {

		findNextCommand();
		return buffer.poll();
	}

	public void remove() {

	}

	protected abstract List<Command<?>> filter(Command<?> command);
}
