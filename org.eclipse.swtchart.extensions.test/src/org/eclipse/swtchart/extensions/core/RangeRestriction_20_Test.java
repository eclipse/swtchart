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

public class RangeRestriction_20_Test extends TestCase {

	private RangeRestriction rangeRestriction;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		rangeRestriction = new RangeRestriction(RangeRestriction.RESTRICT_SELECT_X | RangeRestriction.RESTRICT_SELECT_Y);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertTrue(rangeRestriction.isRestrictSelectX());
		assertTrue(rangeRestriction.isRestrictSelectY());
	}

	public void test2() {

		rangeRestriction.setRestrictSelectX(false);
		assertFalse(rangeRestriction.isRestrictSelectX());
		assertTrue(rangeRestriction.isRestrictSelectY());
	}

	public void test3() {

		rangeRestriction.setRestrictSelectY(false);
		assertTrue(rangeRestriction.isRestrictSelectX());
		assertFalse(rangeRestriction.isRestrictSelectY());
	}
}
