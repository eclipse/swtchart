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
package org.eclipse.swtchart.vectorgraphics2d.intermediate;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;

/**
 * Mutable implementation of {@code CommandSequence}.
 * It is possible to add commands to this sequence.
 */
public class MutableCommandSequence implements CommandSequence {

	private final List<Command<?>> commands;

	/**
	 * Initializes a new {@code CommandSequence} object.
	 */
	public MutableCommandSequence() {

		this.commands = new LinkedList<>();
	}

	public void add(Command<?> command) {

		commands.add(command);
	}

	@Override
	public Iterator<Command<?>> iterator() {

		return commands.iterator();
	}
}
