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

import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.util.PageSize;

/**
 * Translates {@link Command} objects into a {@link Document}.
 */
public interface Processor {

	/**
	 * Constructs a {@code Document} from the specified commands.
	 * 
	 * @param commands
	 *            Commands used to create the {@code Document}.
	 * @param pageSize
	 *            Size of the resulting {@code Document}.
	 * @return {@code Document} representation of the commands.
	 */
	Document getDocument(CommandSequence commands, PageSize pageSize);
}
