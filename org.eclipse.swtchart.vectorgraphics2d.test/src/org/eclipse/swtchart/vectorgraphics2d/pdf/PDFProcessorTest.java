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

import static org.eclipse.swtchart.vectorgraphics2d.core.TestUtils.assertTemplateEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.eclipse.swtchart.vectorgraphics2d.core.Document;
import org.eclipse.swtchart.vectorgraphics2d.core.TestUtils.Template;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.MutableCommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.util.PageSize;
import org.junit.Ignore;
import org.junit.Test;

public class PDFProcessorTest {

	private static final String EOL = "\n";
	private static final String HEADER = "%PDF-1.4";
	private static final String FOOTER = "%%EOF";
	private static final PageSize PAGE_SIZE = new PageSize(0.0, 10.0, 20.0, 30.0);
	private final PDFProcessor pdfProcessor = new PDFProcessor(false);
	private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

	private String process(Command<?>... commands) throws IOException {

		MutableCommandSequence sequence = new MutableCommandSequence();
		for(Command<?> command : commands) {
			sequence.add(command);
		}
		Document processed = pdfProcessor.getDocument(sequence, PAGE_SIZE);
		processed.writeTo(bytes);
		return bytes.toString(StandardCharsets.ISO_8859_1);
	}

	@Test
	public void pdfProcessorCreatesCompressedDocumentByDefault() {

		PDFProcessor pdfProcessor = new PDFProcessor();
		assertTrue(pdfProcessor.isCompressed());
	}

	@Ignore
	public void envelopeForEmptyDocument() throws IOException {

		/*
		 * TODO
		 * Unable to write to closed stream.
		 */
		String result = process();
		Template actual = new Template((Object[])result.split(EOL));
		Template expected = new Template(HEADER, "1 0 obj", "<<", "/Type /Catalog", "/Pages 2 0 R", ">>", "endobj", "2 0 obj", "<<", "/Type /Pages", "/Kids [3 0 R]", "/Count 1", ">>", "endobj", "3 0 obj", "<<", "/Type /Page", "/Parent 2 0 R", "/MediaBox [0 0 56.69291338582678 85.03937007874016]", "/Contents 4 0 R", "/Resources 5 0 R", ">>", "endobj", "4 0 obj", "<<", "/Length 99", ">>", "stream", "q", "1 1 1 rg 1 1 1 RG", "2.834645669291339 0 0 -2.834645669291339 -0 113.38582677165356 cm", "/Fnt0 12 Tf", "Q", "endstream", "endobj", "5 0 obj", "<<", "/ProcSet [/PDF /Text /ImageB /ImageC /ImageI]", "/Font <<", "/Fnt0 <<", "/Type /Font", "/Subtype /TrueType", "/Encoding /WinAnsiEncoding", Pattern.compile("/BaseFont /\\S+"), ">>", ">>", ">>", "endobj", "xref", "0 6", "0000000000 65535 f ", Pattern.compile("\\d{10} 00000 n "), Pattern.compile("\\d{10} 00000 n "), Pattern.compile("\\d{10} 00000 n "), Pattern.compile("\\d{10} 00000 n "), Pattern.compile("\\d{10} 00000 n "), "trailer", "<<", "/Size 6", "/Root 1 0 R", ">>", "startxref", Pattern.compile("[1-9]\\d*"), FOOTER);
		assertTemplateEquals(expected, actual);
	}
}
