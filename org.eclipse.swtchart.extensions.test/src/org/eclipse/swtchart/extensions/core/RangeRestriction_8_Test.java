/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import junit.framework.TestCase;

public class RangeRestriction_8_Test extends TestCase {

	private RangeRestriction rangeRestriction;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		rangeRestriction = new RangeRestriction(RangeRestriction.NONE);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		rangeRestriction.setZeroX(true);
		rangeRestriction.setZeroY(true);
		rangeRestriction.setRestrictZoom(true);
		rangeRestriction.setXZoomOnly(true);
		rangeRestriction.setYZoomOnly(true);
		rangeRestriction.setForceZeroMinY(true);
		assertTrue(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictZoom());
		assertTrue(rangeRestriction.isXZoomOnly());
		assertTrue(rangeRestriction.isYZoomOnly());
		assertTrue(rangeRestriction.isForceZeroMinY());
	}

	public void test2() {

		rangeRestriction.setZeroX(true);
		rangeRestriction.setRestrictZoom(true);
		assertTrue(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictZoom());
		assertFalse(rangeRestriction.isXZoomOnly());
		assertFalse(rangeRestriction.isYZoomOnly());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test3() {

		rangeRestriction.setZeroY(true);
		rangeRestriction.setRestrictZoom(true);
		assertFalse(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictZoom());
		assertFalse(rangeRestriction.isXZoomOnly());
		assertFalse(rangeRestriction.isYZoomOnly());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test4() {

		rangeRestriction.setZeroX(true);
		rangeRestriction.setZeroY(true);
		assertTrue(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictZoom());
		assertFalse(rangeRestriction.isXZoomOnly());
		assertFalse(rangeRestriction.isYZoomOnly());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test5() {

		rangeRestriction.setXZoomOnly(true);
		assertFalse(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictZoom());
		assertTrue(rangeRestriction.isXZoomOnly());
		assertFalse(rangeRestriction.isYZoomOnly());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test6() {

		rangeRestriction.setYZoomOnly(true);
		assertFalse(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictZoom());
		assertFalse(rangeRestriction.isXZoomOnly());
		assertTrue(rangeRestriction.isYZoomOnly());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test7() {

		rangeRestriction.setYZoomOnly(true);
		rangeRestriction.setForceZeroMinY(true);
		assertFalse(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictZoom());
		assertFalse(rangeRestriction.isXZoomOnly());
		assertTrue(rangeRestriction.isYZoomOnly());
		assertTrue(rangeRestriction.isForceZeroMinY());
	}
}
