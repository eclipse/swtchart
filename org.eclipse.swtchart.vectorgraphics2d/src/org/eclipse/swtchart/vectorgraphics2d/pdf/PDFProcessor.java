/*******************************************************************************
 * Copyright (c) 2010, 2024 VectorGraphics2D project.
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
 * Philip Wenig - fixed PDF output
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.pdf;

import java.io.IOException;

import org.eclipse.swtchart.vectorgraphics2d.core.Document;
import org.eclipse.swtchart.vectorgraphics2d.core.Processor;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.util.PageSize;

/**
 * {@code Processor} implementation that translates {@link CommandSequence}s to
 * a {@code Document} in the <i>Portable Document Format</i> (PDF).
 */
public class PDFProcessor implements Processor {

	private final boolean compressed;

	public PDFProcessor() {

		this(true);
	}

	public PDFProcessor(boolean compressed) {

		this.compressed = compressed;
	}

	public boolean isCompressed() {

		return compressed;
	}

	@Override
	public Document getDocument(CommandSequence commands, PageSize pageSize) throws IOException {

		return new PDFDocument(commands, pageSize, isCompressed());
	}
}