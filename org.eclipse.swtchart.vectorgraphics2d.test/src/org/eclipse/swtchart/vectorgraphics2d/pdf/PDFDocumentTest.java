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
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.pdf;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.eclipse.swtchart.vectorgraphics2d.util.FormattingWriter;
import org.junit.Test;

public class PDFDocumentTest {

	private static final String PDF_EOL = "\n";

	@SuppressWarnings("resource")
	@Test
	public void serializeTrueTypeFont() throws IOException {

		String encoding = "CustomEncoding";
		String baseFont = "MyBaseFont";
		TrueTypeFont font = new TrueTypeFont(encoding, baseFont);
		byte[] serialized = PDFDocument.serialize(font);
		ByteArrayOutputStream expected = new ByteArrayOutputStream();
		FormattingWriter expectedString = new FormattingWriter(expected, StandardCharsets.ISO_8859_1, PDF_EOL);
		expectedString.writeln("<<");
		expectedString.writeln("/Type /Font");
		expectedString.writeln("/Subtype /TrueType");
		expectedString.write("/Encoding /").writeln(encoding);
		expectedString.write("/BaseFont /").writeln(baseFont);
		expectedString.write(">>");
		assertArrayEquals(expected.toByteArray(), serialized);
	}

	@SuppressWarnings("resource")
	@Test
	public void serializeStreamWhenStreamIsFiltered() throws IOException {

		Stream stream = new Stream(Stream.Filter.FLATE);
		byte[] inputData = new byte[]{4, 2, 42, -1, 0};
		stream.write(inputData);
		stream.close();
		byte[] serialized = PDFDocument.serialize(stream);
		ByteArrayOutputStream expected = new ByteArrayOutputStream();
		FormattingWriter expectedString = new FormattingWriter(expected, StandardCharsets.ISO_8859_1, PDF_EOL);
		expectedString.writeln("<<");
		expectedString.write("/Length ").writeln(stream.getLength());
		expectedString.writeln("/Filter /FlateDecode");
		expectedString.writeln(">>");
		expectedString.writeln("stream");
		expectedString.writeln(stream.getContent());
		expectedString.write("endstream");
		assertArrayEquals(expected.toByteArray(), serialized);
	}

	@SuppressWarnings("resource")
	@Test
	public void serializeStreamWhenStreamIsNotFiltered() throws IOException {

		Stream stream = new Stream();
		byte[] inputData = new byte[]{4, 2, 42, -1, 0};
		stream.write(inputData);
		stream.close();
		byte[] serialized = PDFDocument.serialize(stream);
		ByteArrayOutputStream expected = new ByteArrayOutputStream();
		FormattingWriter expectedString = new FormattingWriter(expected, StandardCharsets.ISO_8859_1, PDF_EOL);
		expectedString.writeln("<<");
		expectedString.write("/Length ").writeln(stream.getLength());
		expectedString.writeln(">>");
		expectedString.writeln("stream");
		expectedString.writeln(stream.getContent());
		expectedString.write("endstream");
		assertArrayEquals(expected.toByteArray(), serialized);
	}
}
