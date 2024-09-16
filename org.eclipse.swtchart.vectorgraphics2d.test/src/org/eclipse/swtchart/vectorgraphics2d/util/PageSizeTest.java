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

import static org.junit.Assert.assertEquals;

import java.awt.geom.Rectangle2D;

import org.junit.Test;

public class PageSizeTest {

	public static final double DELTA = 1e-15;

	@Test
	public void pageSizeGetsInitializedCorrectlyWithXYWidthHeight() {

		PageSize pageSize = new PageSize(1.0, 2.0, 3.0, 4.0);
		assertEquals(1.0, pageSize.getX(), DELTA);
		assertEquals(2.0, pageSize.getY(), DELTA);
		assertEquals(3.0, pageSize.getWidth(), DELTA);
		assertEquals(4.0, pageSize.getHeight(), DELTA);
	}

	@Test
	public void pageSizeGetsInitializedCorrectlyWithWidthHeight() {

		PageSize pageSize = new PageSize(3.0, 4.0);
		assertEquals(0.0, pageSize.getX(), DELTA);
		assertEquals(0.0, pageSize.getY(), DELTA);
		assertEquals(3.0, pageSize.getWidth(), DELTA);
		assertEquals(4.0, pageSize.getHeight(), DELTA);
	}

	@Test
	public void pageSizeGetsInitializedCorrectlyWithRectangle() {

		PageSize pageSize = new PageSize(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
		assertEquals(1.0, pageSize.getX(), DELTA);
		assertEquals(2.0, pageSize.getY(), DELTA);
		assertEquals(3.0, pageSize.getWidth(), DELTA);
		assertEquals(4.0, pageSize.getHeight(), DELTA);
	}

	@Test
	public void getPortraitTurnsALandscapeFormatIntoPortrait() {

		PageSize portrait = new PageSize(3.0, 4.0);
		PageSize landscape = new PageSize(4.0, 3.0);
		assertEquals(portrait.getWidth(), landscape.getPortrait().getWidth(), DELTA);
		assertEquals(portrait.getHeight(), landscape.getPortrait().getHeight(), DELTA);
	}

	@Test
	public void getPortraitDoesNotChangeAPortraitFormat() {

		PageSize portrait = new PageSize(3.0, 4.0);
		assertEquals(portrait.getWidth(), portrait.getPortrait().getWidth(), DELTA);
		assertEquals(portrait.getHeight(), portrait.getPortrait().getHeight(), DELTA);
	}

	@Test
	public void getLandscapeTurnsAPortraitFormatIntoLandscape() {

		PageSize portrait = new PageSize(3.0, 4.0);
		PageSize landscape = new PageSize(4.0, 3.0);
		assertEquals(landscape.getWidth(), portrait.getLandscape().getWidth(), DELTA);
		assertEquals(landscape.getHeight(), portrait.getLandscape().getHeight(), DELTA);
	}

	@Test
	public void getLandscapeDoesNotChangeALandscapeFormat() {

		PageSize landscape = new PageSize(4.0, 3.0);
		assertEquals(landscape.getWidth(), landscape.getLandscape().getWidth(), DELTA);
		assertEquals(landscape.getHeight(), landscape.getLandscape().getHeight(), DELTA);
	}
}
