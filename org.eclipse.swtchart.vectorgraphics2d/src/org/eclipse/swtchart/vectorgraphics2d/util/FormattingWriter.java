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
package org.eclipse.swtchart.vectorgraphics2d.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class FormattingWriter implements Closeable, Flushable, AutoCloseable {

	private final OutputStream out;
	private final Charset encoding;
	private final byte[] eolBytes;
	private long position;

	public FormattingWriter(OutputStream out, Charset encoding, String eol) {

		if(out == null) {
			throw new IllegalArgumentException("Output stream cannot be null.");
		}
		if(eol == null || eol.isEmpty()) {
			throw new IllegalArgumentException("End-of-line string cannot be empty.");
		}
		this.out = out;
		this.encoding = encoding;
		this.eolBytes = eol.getBytes(encoding);
	}

	public FormattingWriter write(byte[] bytes) throws IOException {

		out.write(bytes, 0, bytes.length);
		position += bytes.length;
		return this;
	}

	public FormattingWriter write(String str) throws IOException {

		byte[] bytes = str.getBytes(encoding);
		return write(bytes);
	}

	public FormattingWriter write(String format, Object... args) throws IOException {

		return write(String.format(null, format, args));
	}

	public FormattingWriter write(Number number) throws IOException {

		return write(DataUtils.format(number));
	}

	public FormattingWriter writeln() throws IOException {

		return write(eolBytes);
	}

	public FormattingWriter writeln(byte[] bytes) throws IOException {

		write(bytes);
		return writeln();
	}

	public FormattingWriter writeln(String string) throws IOException {

		write(string);
		return writeln();
	}

	public FormattingWriter writeln(String format, Object... args) throws IOException {

		write(String.format(null, format, args));
		return writeln();
	}

	public FormattingWriter writeln(Number number) throws IOException {

		write(number);
		return writeln();
	}

	@Override
	public void flush() throws IOException {

		out.flush();
	}

	@Override
	public void close() throws IOException {

		out.close();
	}

	public long tell() {

		return position;
	}
}
