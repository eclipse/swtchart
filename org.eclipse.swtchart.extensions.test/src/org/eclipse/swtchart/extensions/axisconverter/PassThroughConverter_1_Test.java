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
package org.eclipse.swtchart.extensions.axisconverter;

import junit.framework.TestCase;

public class PassThroughConverter_1_Test extends TestCase {

	private PassThroughConverter passThroughConverter;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		passThroughConverter = new PassThroughConverter();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals(-1.0d, passThroughConverter.convertToSecondaryUnit(-1.0d));
	}

	public void test2() {

		assertEquals(0.0d, passThroughConverter.convertToSecondaryUnit(0.0d));
	}

	public void test3() {

		assertEquals(1.0d, passThroughConverter.convertToSecondaryUnit(1.0d));
	}
}
