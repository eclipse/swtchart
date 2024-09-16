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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.geom.Rectangle2D;

import org.junit.Test;

public class PageTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testTypeIsPage() {

		Page page = new Page(null, null, null);
		String type = page.getType();
		assertThat(type, is("Page"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testConstructorSetsMediaBox() {

		Rectangle2D mediaBox = new Rectangle2D.Double(2, 4, 24, 42);
		Page page = new Page(null, mediaBox, null);
		assertThat(page.getMediaBox(), is(mediaBox));
	}
}
