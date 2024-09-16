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
package org.eclipse.swtchart.vectorgraphics2d.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class ProcessorsTest {

	@Test(expected = NullPointerException.class)
	public void testGetThrowsNullPointerExceptionWhenNullIsPassed() {

		Processors.get(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetThrowsIllegalArgumentExceptionWhenFormatIsUnknown() {

		Processors.get("UnknownFormat");
	}

	@DataPoints
	public static List<String> KNOWN_FORMATS = Arrays.asList("eps", "pdf", "svg");

	@SuppressWarnings("deprecation")
	@Theory
	public void testGetReturnsNonNullWhenFormatIsKnown(String format) {

		Processor processor = Processors.get(format);
		assertThat(processor, is(notNullValue()));
	}
}
