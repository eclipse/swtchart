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
package org.eclipse.swtchart.vectorgraphics2d.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

public class FormattingWriterTest {

	private static final String DEFAULT_ENCODING = "ISO-8859-1";
	private static final String DEFAULT_EOL = "\n";
	private ByteArrayOutputStream stream;

	@Before
	public void setUp() {

		stream = new ByteArrayOutputStream();
	}

	@SuppressWarnings("resource")
	@Test(expected = IllegalArgumentException.class)
	public void constructorFailsWithoutStream() throws UnsupportedEncodingException {

		new FormattingWriter(null, DEFAULT_ENCODING, DEFAULT_EOL);
	}

	@SuppressWarnings("resource")
	@Test(expected = UnsupportedEncodingException.class)
	public void constructorFailsWithUnknownEncoding() throws UnsupportedEncodingException {

		new FormattingWriter(stream, "<unknown>", DEFAULT_EOL);
	}

	@SuppressWarnings("resource")
	@Test(expected = IllegalArgumentException.class)
	public void constructorFailsWithEmptyEOL() throws UnsupportedEncodingException {

		new FormattingWriter(stream, DEFAULT_ENCODING, "");
	}

	@SuppressWarnings("resource")
	@Test
	public void writeBytesEmitsBytesToStream() throws IOException {

		FormattingWriter writer = new FormattingWriter(stream, DEFAULT_ENCODING, DEFAULT_EOL);
		byte[] bytes = {86, 71, 50, 68};
		writer.write(bytes);
		byte[] expected = bytes;
		assertArrayEquals(expected, stream.toByteArray());
	}

	@SuppressWarnings("resource")
	@Test
	public void writelnBytesEmitsBytesAndEOLToStream() throws IOException {

		FormattingWriter writer = new FormattingWriter(stream, DEFAULT_ENCODING, DEFAULT_EOL);
		byte[] eolBytes = DEFAULT_EOL.getBytes(DEFAULT_ENCODING);
		byte[] bytes = {86, 71, 50, 68};
		writer.writeln(bytes);
		byte[] expected = new byte[bytes.length + eolBytes.length];
		System.arraycopy(bytes, 0, expected, 0, bytes.length);
		System.arraycopy(eolBytes, 0, expected, bytes.length, eolBytes.length);
		assertArrayEquals(expected, stream.toByteArray());
	}

	@SuppressWarnings("resource")
	@Test
	public void writeStringHasCorrectEncoding() throws IOException {

		FormattingWriter writer = new FormattingWriter(stream, DEFAULT_ENCODING, DEFAULT_EOL);
		String string = "f\\u00F6\\u00F6bar";
		writer.write(string);
		byte[] expected = string.getBytes(DEFAULT_ENCODING);
		assertArrayEquals(expected, stream.toByteArray());
	}

	@SuppressWarnings("resource")
	@Test
	public void writeStringEmitsCorrectEOLs() throws IOException {

		FormattingWriter writer = new FormattingWriter(stream, DEFAULT_ENCODING, "\r\n");
		writer.writeln("foo").writeln("bar");
		byte[] expected = "foo\r\nbar\r\n".getBytes(DEFAULT_ENCODING);
		assertArrayEquals(expected, stream.toByteArray());
	}

	@SuppressWarnings("resource")
	@Test
	public void writeDoubleOutputsAFormattedNumber() throws IOException {

		FormattingWriter writer = new FormattingWriter(stream, DEFAULT_ENCODING, DEFAULT_EOL);
		writer.write(4.2);
		byte[] expected = "4.2".getBytes(DEFAULT_ENCODING);
		assertArrayEquals(expected, stream.toByteArray());
	}

	@SuppressWarnings("resource")
	@Test
	public void writeDoubleOutputsAFormattedNumberAndAppendsAnEOL() throws IOException {

		FormattingWriter writer = new FormattingWriter(stream, DEFAULT_ENCODING, DEFAULT_EOL);
		writer.writeln(4.2);
		byte[] expected = ("4.2" + DEFAULT_EOL).getBytes(DEFAULT_ENCODING);
		assertArrayEquals(expected, stream.toByteArray());
	}

	@SuppressWarnings("resource")
	@Test
	public void writeFormatsStringWithParameters() throws IOException {

		FormattingWriter writer = new FormattingWriter(stream, DEFAULT_ENCODING, DEFAULT_EOL);
		writer.write("%.02f => %s", 4.2, "foo");
		byte[] expected = "4.20 => foo".getBytes(DEFAULT_ENCODING);
		assertArrayEquals(expected, stream.toByteArray());
	}

	@SuppressWarnings("resource")
	@Test
	public void writelnFormatsStringWithParametersAndAppendsAnEOL() throws IOException {

		FormattingWriter writer = new FormattingWriter(stream, DEFAULT_ENCODING, DEFAULT_EOL);
		writer.writeln("%.02f => %s", 4.2, "foo");
		byte[] expected = ("4.20 => foo" + DEFAULT_EOL).getBytes(DEFAULT_ENCODING);
		assertArrayEquals(expected, stream.toByteArray());
	}

	private final static class MockOutputStream extends OutputStream {

		private boolean flushed;
		private boolean closed;

		@Override
		public void write(int b) throws IOException {

		}

		@Override
		public void flush() throws IOException {

			flushed = true;
		}

		@Override
		public void close() throws IOException {

			closed = true;
		}
	}

	@Test
	public void closeClosesOutputStream() throws IOException {

		MockOutputStream mockStream = new MockOutputStream();
		FormattingWriter writer = new FormattingWriter(mockStream, DEFAULT_ENCODING, DEFAULT_EOL);
		writer.close();
		assertTrue(mockStream.closed);
	}

	@SuppressWarnings("resource")
	@Test
	public void flushFlushesOutputStream() throws IOException {

		MockOutputStream mockStream = new MockOutputStream();
		FormattingWriter writer = new FormattingWriter(mockStream, DEFAULT_ENCODING, DEFAULT_EOL);
		writer.flush();
		assertTrue(mockStream.flushed);
	}

	@SuppressWarnings("resource")
	@Test
	public void tellReturnsCorrectPosition() throws IOException {

		FormattingWriter writer = new FormattingWriter(stream, DEFAULT_ENCODING, DEFAULT_EOL);
		byte[] bytes = {86, 71, 50, 68};
		writer.write(bytes);
		assertEquals(bytes.length, writer.tell());
	}
}
