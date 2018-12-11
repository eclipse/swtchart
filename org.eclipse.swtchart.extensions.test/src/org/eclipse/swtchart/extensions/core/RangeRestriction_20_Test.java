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

public class RangeRestriction_20_Test extends TestCase {

	private RangeRestriction rangeRestriction;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		rangeRestriction = new RangeRestriction(RangeRestriction.X_ZOOM_ONLY | RangeRestriction.Y_ZOOM_ONLY);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertTrue(rangeRestriction.isXZoomOnly());
		assertTrue(rangeRestriction.isYZoomOnly());
	}

	public void test2() {

		rangeRestriction.setXZoomOnly(false);
		assertFalse(rangeRestriction.isXZoomOnly());
		assertTrue(rangeRestriction.isYZoomOnly());
	}

	public void test3() {

		rangeRestriction.setYZoomOnly(false);
		assertTrue(rangeRestriction.isXZoomOnly());
		assertFalse(rangeRestriction.isYZoomOnly());
	}
}
