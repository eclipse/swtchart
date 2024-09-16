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
import java.util.List;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Group;

public abstract class GroupingFilter extends StreamingFilter {

	private Group group;

	public GroupingFilter(CommandSequence stream) {

		super(stream);
	}

	@Override
	public boolean hasNext() {

		return group != null || super.hasNext();
	}

	@Override
	public Command<?> next() {

		if(group == null) {
			return super.next();
		}
		Group g = group;
		group = null;
		return g;
	}

	@Override
	protected List<Command<?>> filter(Command<?> command) {

		boolean grouped = isGrouped(command);
		if(grouped) {
			if(group == null) {
				group = new Group();
			}
			group.add(command);
			return null;
		}
		return Collections.singletonList(command);
	}

	protected abstract boolean isGrouped(Command<?> command);
}
