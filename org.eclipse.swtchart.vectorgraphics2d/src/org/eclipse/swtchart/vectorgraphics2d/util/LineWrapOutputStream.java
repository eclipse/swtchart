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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LineWrapOutputStream extends FilterOutputStream {

	public static final String STANDARD_EOL = "\r\n";
	private final int lineWidth;
	private final byte[] eolBytes;
	private int written;

	public LineWrapOutputStream(OutputStream sink, int lineWidth, String eol) {

		super(sink);
		this.lineWidth = lineWidth;
		this.eolBytes = eol.getBytes();
		if(lineWidth <= 0) {
			throw new IllegalArgumentException("Width must be at least 0.");
		}
	}

	public LineWrapOutputStream(OutputStream sink, int lineWidth) {

		this(sink, lineWidth, STANDARD_EOL);
	}

	@Override
	public void write(int b) throws IOException {

		if(written == lineWidth) {
			out.write(eolBytes);
			written = 0;
		}
		out.write(b);
		written++;
	}
}
