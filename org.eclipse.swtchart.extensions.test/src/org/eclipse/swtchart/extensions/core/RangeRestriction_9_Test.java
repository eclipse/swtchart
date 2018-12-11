/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import org.eclipse.swtchart.extensions.core.RangeRestriction;

import junit.framework.TestCase;

public class RangeRestriction_9_Test extends TestCase {

	private RangeRestriction rangeRestriction;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_X | RangeRestriction.ZERO_Y | RangeRestriction.RESTRICT_ZOOM | RangeRestriction.X_ZOOM_ONLY | RangeRestriction.Y_ZOOM_ONLY);
		assertTrue(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictZoom());
		assertTrue(rangeRestriction.isXZoomOnly());
		assertTrue(rangeRestriction.isYZoomOnly());
	}

	public void test2() {

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_X | RangeRestriction.RESTRICT_ZOOM);
		assertTrue(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictZoom());
		assertFalse(rangeRestriction.isXZoomOnly());
		assertFalse(rangeRestriction.isYZoomOnly());
	}

	public void test3() {

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_Y | RangeRestriction.RESTRICT_ZOOM);
		assertFalse(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictZoom());
		assertFalse(rangeRestriction.isXZoomOnly());
		assertFalse(rangeRestriction.isYZoomOnly());
	}

	public void test4() {

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_X | RangeRestriction.ZERO_Y);
		assertTrue(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictZoom());
		assertFalse(rangeRestriction.isXZoomOnly());
		assertFalse(rangeRestriction.isYZoomOnly());
	}

	public void test5() {

		rangeRestriction = new RangeRestriction(RangeRestriction.X_ZOOM_ONLY | RangeRestriction.Y_ZOOM_ONLY);
		assertFalse(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictZoom());
		assertTrue(rangeRestriction.isXZoomOnly());
		assertTrue(rangeRestriction.isYZoomOnly());
	}

	public void test6() {

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_X | RangeRestriction.X_ZOOM_ONLY);
		assertTrue(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictZoom());
		assertTrue(rangeRestriction.isXZoomOnly());
		assertFalse(rangeRestriction.isYZoomOnly());
	}
}
