/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
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

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_X | RangeRestriction.ZERO_Y | RangeRestriction.RESTRICT_FRAME | RangeRestriction.RESTRICT_SELECT_X | RangeRestriction.RESTRICT_SELECT_Y);
		assertTrue(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictFrame());
		assertTrue(rangeRestriction.isRestrictSelectX());
		assertTrue(rangeRestriction.isRestrictSelectY());
	}

	public void test2() {

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_X | RangeRestriction.RESTRICT_FRAME);
		assertTrue(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictFrame());
		assertFalse(rangeRestriction.isRestrictSelectX());
		assertFalse(rangeRestriction.isRestrictSelectY());
	}

	public void test3() {

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_Y | RangeRestriction.RESTRICT_FRAME);
		assertFalse(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictFrame());
		assertFalse(rangeRestriction.isRestrictSelectX());
		assertFalse(rangeRestriction.isRestrictSelectY());
	}

	public void test4() {

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_X | RangeRestriction.ZERO_Y);
		assertTrue(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictFrame());
		assertFalse(rangeRestriction.isRestrictSelectX());
		assertFalse(rangeRestriction.isRestrictSelectY());
	}

	public void test5() {

		rangeRestriction = new RangeRestriction(RangeRestriction.RESTRICT_SELECT_X | RangeRestriction.RESTRICT_SELECT_Y);
		assertFalse(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictFrame());
		assertTrue(rangeRestriction.isRestrictSelectX());
		assertTrue(rangeRestriction.isRestrictSelectY());
	}

	public void test6() {

		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_X | RangeRestriction.RESTRICT_SELECT_X);
		assertTrue(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictFrame());
		assertTrue(rangeRestriction.isRestrictSelectX());
		assertFalse(rangeRestriction.isRestrictSelectY());
	}
}
