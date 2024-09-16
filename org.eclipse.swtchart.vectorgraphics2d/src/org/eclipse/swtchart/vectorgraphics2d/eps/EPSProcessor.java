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
package org.eclipse.swtchart.vectorgraphics2d.eps;

import org.eclipse.swtchart.vectorgraphics2d.core.Document;
import org.eclipse.swtchart.vectorgraphics2d.core.Processor;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.filters.FillPaintedShapeAsImageFilter;
import org.eclipse.swtchart.vectorgraphics2d.util.PageSize;

/**
 * {@code Processor} implementation that translates {@link CommandSequence}s to
 * a {@code Document} in the <i>Encapsulated PostScript&reg;</i> (EPS) format.
 */
public class EPSProcessor implements Processor {

	/**
	 * Initializes an {@code EPSProcessor}.
	 */
	public EPSProcessor() {

	}

	@Override
	public Document getDocument(CommandSequence commands, PageSize pageSize) {

		// TODO Apply rotate(theta,x,y) => translate-rotate-translate filter
		// TODO Apply image transparency => image mask filter
		// TODO Apply optimization filter
		FillPaintedShapeAsImageFilter paintedShapeAsImageFilter = new FillPaintedShapeAsImageFilter(commands);
		return new EPSDocument(paintedShapeAsImageFilter, pageSize);
	}
}
