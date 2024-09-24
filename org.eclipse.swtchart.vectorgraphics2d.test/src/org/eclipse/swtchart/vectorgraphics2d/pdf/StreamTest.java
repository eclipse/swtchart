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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.junit.Test;

public class StreamTest {

	@SuppressWarnings("resource")
	@Test(expected = IllegalStateException.class)
	public void getLengthThrowsExceptionWhenStreamIsOpen() {

		Stream stream = new Stream();
		stream.getLength();
	}

	@SuppressWarnings("resource")
	@Test(expected = IllegalStateException.class)
	public void getContentThrowsExceptionWhenStreamIsOpen() {

		Stream stream = new Stream();
		stream.getContent();
	}

	@Test
	public void writeIntWritesDataToStream() throws IOException {

		Stream stream = new Stream();
		stream.write(42);
		stream.close();
		assertArrayEquals(new byte[]{42}, stream.getContent());
	}

	@Test(expected = IOException.class)
	public void writeIntThrowsExceptionWhenStreamIsClosed() throws IOException {

		Stream stream = new Stream();
		stream.close();
		stream.write(42);
	}

	@Test
	public void writeBytesWritesDataToStream() throws IOException {

		Stream stream = new Stream();
		stream.write(new byte[]{42});
		stream.close();
		assertArrayEquals(new byte[]{42}, stream.getContent());
	}

	@Test(expected = IOException.class)
	public void writeBytesThrowsExceptionWhenStreamIsClosed() throws IOException {

		Stream stream = new Stream();
		stream.close();
		stream.write(new byte[]{42});
	}

	@SuppressWarnings("deprecation")
	@Test
	public void lengthIsZeroOnInitialization() throws IOException {

		Stream stream = new Stream();
		stream.close();
		int length = stream.getLength();
		assertThat(length, is(0));
	}

	@SuppressWarnings({"deprecation"})
	@Test
	public void lengthEqualsByteCountInWrittenDataWhenNoFiltersAreSet() throws IOException {

		byte[] garbage = new byte[]{4, 2, 42, -1, 0};
		Stream stream = new Stream();
		stream.write(garbage);
		stream.close();
		int length = stream.getLength();
		assertThat(length, is(garbage.length));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void writtenDataIsIdenticalToStreamContentWhenNoFiltersAreUsed() throws IOException {

		byte[] data = new byte[]{4, 2, 42, -1, 0};
		Stream stream = new Stream();
		stream.write(data);
		stream.close();
		byte[] content = stream.getContent();
		assertThat(content, is(data));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void contentsAreCompressedWhenFlateFilterIsSet() throws DataFormatException, IOException {

		byte[] inputData = new byte[]{4, 2, 42, -1, 0};
		Stream stream = new Stream(Stream.Filter.FLATE);
		stream.write(inputData);
		stream.close();
		byte[] compressedContent = stream.getContent();
		Inflater decompressor = new Inflater();
		decompressor.setInput(compressedContent);
		byte[] decompressedOutput = new byte[inputData.length];
		decompressor.inflate(decompressedOutput);
		assertThat(decompressedOutput, is(inputData));
	}

	@SuppressWarnings("resource")
	@Test
	public void getFiltersReturnsListOfFilters() {

		Stream stream = new Stream(Stream.Filter.FLATE);
		List<Stream.Filter> filters = stream.getFilters();
		List<Stream.Filter> expected = Collections.singletonList(Stream.Filter.FLATE);
		assertEquals(expected, filters);
	}

	@SuppressWarnings("resource")
	@Test(expected = UnsupportedOperationException.class)
	public void getFiltersResultIsUnmodifiable() {

		Stream stream = new Stream();
		List<Stream.Filter> filters = stream.getFilters();
		filters.add(Stream.Filter.FLATE);
	}
}
