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

/**
 * Represents a TrueType font in the sense of the PDF specification.
 */
class TrueTypeFont implements PDFObject {

	private final String encoding;
	private final String baseFont;

	/**
	 * Creates a {@code TrueTypeFont} with the specified encoding and base font.
	 * 
	 * @param encoding
	 *            Used encoding.
	 * @param baseFont
	 *            Base font name.
	 */
	public TrueTypeFont(String encoding, String baseFont) {

		this.encoding = encoding;
		this.baseFont = baseFont;
	}

	/**
	 * Returns the encoding of this font.
	 * 
	 * @return Encoding.
	 */
	public String getEncoding() {

		return encoding;
	}

	/**
	 * Returns the type of this object.
	 * Always returns "Font".
	 * 
	 * @return The String "Font".
	 */
	public String getType() {

		return "Font";
	}

	/**
	 * Returns the subtype of this object.
	 * Always returns "TrueType".
	 * 
	 * @return The String "TrueType".
	 */
	public String getSubtype() {

		return "TrueType";
	}

	/**
	 * Returns the name of the base font.
	 * 
	 * @return Base font name.
	 */
	public String getBaseFont() {

		return baseFont;
	}
}
