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

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.CreateCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DisposeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.TransformCommand;

public class AbsoluteToRelativeTransformsFilter extends StreamingFilter {

	private final Stack<AffineTransform> transforms;

	public AbsoluteToRelativeTransformsFilter(CommandSequence stream) {

		super(stream);
		transforms = new Stack<>();
	}

	@Override
	public Command<?> next() {

		Command<?> nextCommand = super.next();
		if(nextCommand instanceof AffineTransformCommand) {
			AffineTransformCommand affineTransformCommand = (AffineTransformCommand)nextCommand;
			getCurrentTransform().concatenate(affineTransformCommand.getValue());
		} else if(nextCommand instanceof CreateCommand) {
			AffineTransform newTransform = transforms.isEmpty() ? new AffineTransform() : new AffineTransform(getCurrentTransform());
			transforms.push(newTransform);
		} else if(nextCommand instanceof DisposeCommand) {
			transforms.pop();
		}
		return nextCommand;
	}

	@Override
	protected List<Command<?>> filter(Command<?> command) {

		if(command instanceof SetTransformCommand) {
			SetTransformCommand setTransformCommand = (SetTransformCommand)command;
			AffineTransform absoluteTransform = setTransformCommand.getValue();
			AffineTransform relativeTransform = new AffineTransform();
			try {
				AffineTransform invertedOldTransformation = getCurrentTransform().createInverse();
				relativeTransform.concatenate(invertedOldTransformation);
			} catch(NoninvertibleTransformException e) {
				e.printStackTrace();
			}
			relativeTransform.concatenate(absoluteTransform);
			TransformCommand transformCommand = new TransformCommand(relativeTransform);
			return Collections.singletonList(transformCommand);
		}
		return Collections.singletonList(command);
	}

	private AffineTransform getCurrentTransform() {

		return transforms.peek();
	}
}
