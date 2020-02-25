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

public class MillisecondsToScanNumberConverter_1_Test extends TestCase {

	private MillisecondsToScanNumberConverter millisecondsToScanNumberConverter;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		millisecondsToScanNumberConverter = new MillisecondsToScanNumberConverter(500, 1000);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals(0.0d, millisecondsToScanNumberConverter.convertToSecondaryUnit(0.0d));
	}

	public void test2() {

		assertEquals(0.0d, millisecondsToScanNumberConverter.convertToSecondaryUnit(499.0d));
	}

	public void test3() {

		assertEquals(1.0d, millisecondsToScanNumberConverter.convertToSecondaryUnit(500.0d));
	}

	public void test4() {

		assertEquals(2.0d, millisecondsToScanNumberConverter.convertToSecondaryUnit(1500.0d));
	}

	public void test5() {

		assertEquals(2.0d, millisecondsToScanNumberConverter.convertToSecondaryUnit(1501.0d));
	}

	public void test6() {

		assertEquals(2.0d, millisecondsToScanNumberConverter.convertToSecondaryUnit(2499.0d));
	}

	public void test7() {

		assertEquals(3.0d, millisecondsToScanNumberConverter.convertToSecondaryUnit(2500.0d));
	}

	public void test8() {

		assertEquals(0.0d, millisecondsToScanNumberConverter.convertToPrimaryUnit(0.0d));
	}

	public void test9() {

		assertEquals(500.0d, millisecondsToScanNumberConverter.convertToPrimaryUnit(1.0d));
	}

	public void test10() {

		assertEquals(1500.0d, millisecondsToScanNumberConverter.convertToPrimaryUnit(2.0d));
	}

	public void test11() {

		assertEquals(2500.0d, millisecondsToScanNumberConverter.convertToPrimaryUnit(3.0d));
	}
}
