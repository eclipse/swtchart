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
package org.eclipse.swtchart.vectorgraphics2d.svg;

import org.eclipse.swtchart.vectorgraphics2d.core.Document;
import org.eclipse.swtchart.vectorgraphics2d.core.Processor;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.filters.FillPaintedShapeAsImageFilter;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.filters.StateChangeGroupingFilter;
import org.eclipse.swtchart.vectorgraphics2d.util.PageSize;

/**
 * {@code Processor} implementation that translates {@link CommandSequence}s to
 * a {@code Document} in the <i>Scaled Vector Graphics</i> (SVG) format.
 */
public class SVGProcessor implements Processor {

	/**
	 * Initializes an {@code SVGProcessor}.
	 */
	public SVGProcessor() {

	}

	@Override
	public Document getDocument(CommandSequence commands, PageSize pageSize) {

		FillPaintedShapeAsImageFilter shapesAsImages = new FillPaintedShapeAsImageFilter(commands);
		CommandSequence filtered = new StateChangeGroupingFilter(shapesAsImages);
		return new SVGDocument(filtered, pageSize);
	}
}
