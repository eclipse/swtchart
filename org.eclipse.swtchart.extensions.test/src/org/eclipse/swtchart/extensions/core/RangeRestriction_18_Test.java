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

public class RangeRestriction_18_Test extends TestCase {

	private RangeRestriction rangeRestriction;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		rangeRestriction = new RangeRestriction();
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtend(2.98d);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals(2.98d, rangeRestriction.getExtendMinX());
	}

	public void test2() {

		assertEquals(2.98d, rangeRestriction.getExtendMaxX());
	}

	public void test3() {

		assertEquals(2.98d, rangeRestriction.getExtendMinY());
	}

	public void test4() {

		assertEquals(2.98d, rangeRestriction.getExtendMaxY());
	}
}
