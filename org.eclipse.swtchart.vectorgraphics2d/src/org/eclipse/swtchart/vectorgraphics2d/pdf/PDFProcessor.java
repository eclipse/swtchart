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
package org.eclipse.swtchart.vectorgraphics2d.pdf;

import org.eclipse.swtchart.vectorgraphics2d.core.Document;
import org.eclipse.swtchart.vectorgraphics2d.core.Processor;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.filters.AbsoluteToRelativeTransformsFilter;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.filters.FillPaintedShapeAsImageFilter;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.filters.StateChangeGroupingFilter;
import org.eclipse.swtchart.vectorgraphics2d.util.PageSize;

/**
 * {@code Processor} implementation that translates {@link CommandSequence}s to
 * a {@code Document} in the <i>Portable Document Format</i> (PDF).
 */
public class PDFProcessor implements Processor {

	private final boolean compressed;

	/**
	 * Initializes a {@code PDFProcessor} for compressed PDF documents.
	 */
	public PDFProcessor() {

		this(true);
	}

	/**
	 * Initializes a {@code PDFProcessor} with the specified compression settings.
	 * 
	 * @param compressed
	 *            {@code true} if compression is enabled, {@code false} otherwise.
	 */
	public PDFProcessor(boolean compressed) {

		this.compressed = compressed;
	}

	/**
	 * Returns whether the current PDF document is compressed.
	 * 
	 * @return {@code true} if the document is compressed, {@code false} otherwise.
	 */
	public boolean isCompressed() {

		return compressed;
	}

	@Override
	public Document getDocument(CommandSequence commands, PageSize pageSize) {

		AbsoluteToRelativeTransformsFilter absoluteToRelativeTransformsFilter = new AbsoluteToRelativeTransformsFilter(commands);
		FillPaintedShapeAsImageFilter paintedShapeAsImageFilter = new FillPaintedShapeAsImageFilter(absoluteToRelativeTransformsFilter);
		CommandSequence filtered = new StateChangeGroupingFilter(paintedShapeAsImageFilter);
		return new PDFDocument(filtered, pageSize, isCompressed());
	}
}
