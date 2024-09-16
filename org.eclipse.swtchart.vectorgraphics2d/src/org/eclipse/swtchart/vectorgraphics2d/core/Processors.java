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

import org.eclipse.swtchart.vectorgraphics2d.eps.EPSProcessor;
import org.eclipse.swtchart.vectorgraphics2d.pdf.PDFProcessor;
import org.eclipse.swtchart.vectorgraphics2d.svg.SVGProcessor;

/**
 * <p>
 * Utility class that provides simplified access to processors for different
 * file formats. At the moment three implementations of processors are available:
 * {@code "eps"}, {@code "pdf"}, and {@code "svg"}
 * </p>
 * <p>
 * A new processor can be retrieved by calling the {@link #get(String)}
 * method with the format name:
 * </p>
 * <pre>Processor pdfProcessor = Processors.get("pdf");</pre>
 */
public abstract class Processors {

	/**
	 * Default constructor that prevents creation of class.
	 */
	Processors() {

		throw new UnsupportedOperationException();
	}

	public static Processor get(String format) {

		if(format == null) {
			throw new NullPointerException("Format cannot be null.");
		}
		switch(format) {
			case "eps":
				return new EPSProcessor();
			case "pdf":
				return new PDFProcessor(true);
			case "svg":
				return new SVGProcessor();
			default:
				throw new IllegalArgumentException("Unknown format \"" + format + "\"");
		}
	}
}
