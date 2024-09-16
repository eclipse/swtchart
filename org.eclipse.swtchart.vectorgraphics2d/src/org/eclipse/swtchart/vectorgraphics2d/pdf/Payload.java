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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.swtchart.vectorgraphics2d.util.FlateEncodeStream;

class Payload extends OutputStream {

	private final ByteArrayOutputStream byteStream;
	private OutputStream filteredStream;
	private boolean empty;

	public Payload() {

		byteStream = new ByteArrayOutputStream();
		filteredStream = byteStream;
		empty = true;
	}

	public byte[] getBytes() {

		return byteStream.toByteArray();
	}

	@Override
	public void write(int b) throws IOException {

		filteredStream.write(b);
		empty = false;
	}

	public boolean isEmpty() {

		return empty;
	}

	@Override
	public void close() throws IOException {

		super.close();
		filteredStream.close();
	}

	public void addFilter(Class<FlateEncodeStream> filterClass) {

		if(!empty) {
			throw new IllegalStateException("Cannot add filter after writing to payload.");
		}
		try {
			filteredStream = filterClass.getConstructor(OutputStream.class).newInstance(filteredStream);
		} catch(InstantiationException | NoSuchMethodException
				| InvocationTargetException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
