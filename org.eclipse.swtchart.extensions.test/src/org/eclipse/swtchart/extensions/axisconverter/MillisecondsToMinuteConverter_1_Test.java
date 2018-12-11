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
package org.eclipse.swtchart.extensions.axisconverter;

import org.eclipse.swtchart.extensions.axisconverter.MillisecondsToMinuteConverter;

import junit.framework.TestCase;

public class MillisecondsToMinuteConverter_1_Test extends TestCase {

	private MillisecondsToMinuteConverter millisecondsToMinuteConverter;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		millisecondsToMinuteConverter = new MillisecondsToMinuteConverter();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals(1.0d, millisecondsToMinuteConverter.convertToSecondaryUnit(60000.0d));
	}

	public void test2() {

		assertEquals(60000.0d, millisecondsToMinuteConverter.convertToPrimaryUnit(1.0d));
	}
}
