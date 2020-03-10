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

public class RangeRestriction_4_Test extends TestCase {

	private RangeRestriction rangeRestriction;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		rangeRestriction = new RangeRestriction(RangeRestriction.ZERO_Y);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertFalse(rangeRestriction.isZeroX());
	}

	public void test2() {

		assertTrue(rangeRestriction.isZeroY());
	}

	public void test3() {

		assertFalse(rangeRestriction.isRestrictZoom());
	}

	public void test4() {

		assertFalse(rangeRestriction.isXZoomOnly());
	}

	public void test5() {

		assertFalse(rangeRestriction.isYZoomOnly());
	}

	public void test6() {

		assertFalse(rangeRestriction.isForceZeroMinY());
	}
}
