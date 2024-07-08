/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.support;

import org.eclipse.swtchart.Range;

import junit.framework.TestCase;

public class ElementSupport_2_Test extends TestCase {
	/*
	 * lower=5414.0, upper=959989.0
	 * lower=-2312099.75, upper=2318955.25
	 * PaintEvent{PlotArea {} [layout=null] time=47333234 data=null gc=GC {131519087328800} x=0 y=0 width=1661 height=785 count=1}
	 */

	private ElementSupport elementSupport = new ElementSupport(new Range(5414.0, 959989.0), new Range(-2312099.75, 2318955.25), 1661, 785);

	public void test1() {

		RectanglePrimary rectanglePrimary = elementSupport.convertRectangle(196, 15, 40, 25);
		assertEquals(118054.99939795304, rectanglePrimary.getX());
		assertEquals(2274644.0031847134, rectanglePrimary.getY());
		assertEquals(22987.959060806752, rectanglePrimary.getWidth());
		assertEquals(147485.82802547817, rectanglePrimary.getHeight());
	}

	public void test2() {

		RectanglePrimary rectanglePrimary = elementSupport.convertRectangle(196, 745, 40, 25);
		assertEquals(118054.99939795304, rectanglePrimary.getX());
		assertEquals(-2076122.4251592355, rectanglePrimary.getY());
		assertEquals(22987.959060806752, rectanglePrimary.getWidth());
		assertEquals(147485.82802547794, rectanglePrimary.getHeight());
	}
}