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
		rangeRestriction.setRestrictFrame(true);
		rangeRestriction.setRestrictSelectX(true);
		rangeRestriction.setRestrictSelectY(true);
		rangeRestriction.setForceZeroMinY(true);
		assertTrue(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictFrame());
		assertTrue(rangeRestriction.isRestrictSelectX());
		assertTrue(rangeRestriction.isRestrictSelectY());
		assertTrue(rangeRestriction.isForceZeroMinY());
	}

	public void test2() {

		rangeRestriction.setZeroX(true);
		rangeRestriction.setRestrictFrame(true);
		assertTrue(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictFrame());
		assertFalse(rangeRestriction.isRestrictSelectX());
		assertFalse(rangeRestriction.isRestrictSelectY());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test3() {

		rangeRestriction.setZeroY(true);
		rangeRestriction.setRestrictFrame(true);
		assertFalse(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertTrue(rangeRestriction.isRestrictFrame());
		assertFalse(rangeRestriction.isRestrictSelectX());
		assertFalse(rangeRestriction.isRestrictSelectY());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test4() {

		rangeRestriction.setZeroX(true);
		rangeRestriction.setZeroY(true);
		assertTrue(rangeRestriction.isZeroX());
		assertTrue(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictFrame());
		assertFalse(rangeRestriction.isRestrictSelectX());
		assertFalse(rangeRestriction.isRestrictSelectY());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test5() {

		rangeRestriction.setRestrictSelectX(true);
		assertFalse(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictFrame());
		assertTrue(rangeRestriction.isRestrictSelectX());
		assertFalse(rangeRestriction.isRestrictSelectY());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test6() {

		rangeRestriction.setRestrictSelectY(true);
		assertFalse(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictFrame());
		assertFalse(rangeRestriction.isRestrictSelectX());
		assertTrue(rangeRestriction.isRestrictSelectY());
		assertFalse(rangeRestriction.isForceZeroMinY());
	}

	public void test7() {

		rangeRestriction.setRestrictSelectY(true);
		rangeRestriction.setForceZeroMinY(true);
		assertFalse(rangeRestriction.isZeroX());
		assertFalse(rangeRestriction.isZeroY());
		assertFalse(rangeRestriction.isRestrictFrame());
		assertFalse(rangeRestriction.isRestrictSelectX());
		assertTrue(rangeRestriction.isRestrictSelectY());
		assertTrue(rangeRestriction.isForceZeroMinY());
	}
}
