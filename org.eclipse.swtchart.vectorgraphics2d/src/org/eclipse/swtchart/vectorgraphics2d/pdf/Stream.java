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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

/**
 * Represents a stream object in the sense of the PDF specification.
 * The {@code Stream} has a defined length.
 */
class Stream extends OutputStream implements PDFObject {

	public enum Filter {
		FLATE
	}

	private final ByteArrayOutputStream data;
	private final List<Filter> filters;
	private OutputStream filteredData;
	private boolean closed;

	/**
	 * Initializes a new {@code Stream}.
	 */
	public Stream(Filter... filters) {

		data = new ByteArrayOutputStream();
		this.filters = new ArrayList<>(filters.length);
		this.filters.addAll(Arrays.asList(filters));
		filteredData = data;
		for(Filter filter : filters) {
			if(filter == Filter.FLATE) {
				filteredData = new DeflaterOutputStream(filteredData);
			}
		}
	}

	@Override
	public void write(int b) throws IOException {

		if(isClosed()) {
			throw new IOException("Unable to write to closed stream.");
		}
		this.filteredData.write(b);
	}

	/**
	 * Appends the specified byte array to the {@code Stream}.
	 * 
	 * @param data
	 *            Data to be appended.
	 */
	@Override
	public void write(byte[] data) throws IOException {

		if(isClosed()) {
			throw new IOException("Unable to write to closed stream.");
		}
		this.filteredData.write(data);
	}

	/**
	 * Returns the size of the stream contents in bytes.
	 * 
	 * @return Number of bytes.
	 * @throws IllegalStateException
	 *             if the stream is still open.
	 */
	public int getLength() {

		if(!isClosed()) {
			throw new IllegalStateException("Unable to determine the length of an open Stream. Close the stream first.");
		}
		return data.size();
	}

	/**
	 * Returns the content that has been written to this {@code Stream}.
	 * 
	 * @return Stream content.
	 * @throws IllegalStateException
	 *             if the stream is still open.
	 */
	public byte[] getContent() {

		if(!isClosed()) {
			throw new IllegalStateException("Unable to retrieve the content of an open Stream. Close the stream first.");
		}
		return data.toByteArray();
	}

	private boolean isClosed() {

		return closed;
	}

	@Override
	public void close() throws IOException {

		closed = true;
		filteredData.close();
	}

	public List<Filter> getFilters() {

		return Collections.unmodifiableList(filters);
	}
}
