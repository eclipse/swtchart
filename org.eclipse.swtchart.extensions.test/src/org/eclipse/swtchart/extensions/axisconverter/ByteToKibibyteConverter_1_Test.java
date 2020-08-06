/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
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

public class ByteToKibibyteConverter_1_Test extends TestCase {

	private ByteToKibibyteConverter converter;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		converter = new ByteToKibibyteConverter();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals(1.0d, converter.convertToSecondaryUnit(1024.0d));
	}

	public void test2() {

		assertEquals(1024.0d, converter.convertToPrimaryUnit(1.0d));
	}
}
