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
package org.eclipse.swtchart.extensions.core;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.LineStyle;
import org.junit.Ignore;

import junit.framework.TestCase;

@Ignore
public class PrimaryAxisSettings_1_UITest extends TestCase {

	private PrimaryAxisSettings settings;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		settings = new PrimaryAxisSettings("");
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals("", settings.getTitle());
	}

	public void test2() {

		assertEquals("", settings.getDescription());
	}

	public void test3() {

		assertEquals(new DecimalFormat(), settings.getDecimalFormat());
	}

	public void test4() {

		assertEquals(Display.getDefault().getSystemColor(SWT.COLOR_BLACK), settings.getColor());
	}

	public void test5() {

		assertEquals(true, settings.isVisible());
	}

	public void test6() {

		assertEquals(Position.Primary, settings.getPosition());
	}

	public void test7() {

		assertEquals(Display.getDefault().getSystemColor(SWT.COLOR_GRAY), settings.getGridColor());
	}

	public void test8() {

		assertEquals(LineStyle.DOT, settings.getGridLineStyle());
	}

	public void test9() {

		assertEquals(false, settings.isEnableLogScale());
	}

	public void test10() {

		assertEquals(false, settings.isReversed());
	}

	public void test11() {

		assertEquals(25, settings.getExtraSpaceTitle());
	}

	public void test12() {

		assertEquals(false, settings.isEnableCategory());
	}

	public void test13() {

		assertEquals(0, settings.getCategorySeries().length);
	}

	public void test14() {

		assertEquals(false, settings.isReversed());
	}
}
