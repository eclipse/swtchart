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

public class RangeRestriction_14_Test extends TestCase {

	private RangeRestriction rangeRestriction;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		rangeRestriction = new RangeRestriction();
		rangeRestriction.setExtendTypeX(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtendMinX(2.98d);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals(2.98d, rangeRestriction.getExtendMinX());
	}

	public void test2() {

		assertEquals(0.0d, rangeRestriction.getExtendMaxX());
	}

	public void test3() {

		assertEquals(0.0d, rangeRestriction.getExtendMinY());
	}

	public void test4() {

		assertEquals(0.0d, rangeRestriction.getExtendMaxY());
	}
}
