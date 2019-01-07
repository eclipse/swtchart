/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart;

import junit.framework.TestCase;

public class Range_1_Test extends TestCase {

	private Range range;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		range = new Range(20.2, 40.6);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals(20.2, range.lower);
	}

	public void test2() {

		assertEquals(40.6, range.upper);
	}
}
