/*******************************************************************************
 * Copyright (c) 2024 VectorGraphics2D project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class NonClosingFormattingWriter extends FormattingWriter {

	public NonClosingFormattingWriter(OutputStream out, Charset charset, String eol) {

		super(out, charset, eol);
	}

	@Override
	public void close() throws IOException {

		this.flush();
	}
}
