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

public class ElementSupport_1_Test extends TestCase {
	/*
	 * lower=5422.0, upper=959989.0
	 * lower=6475.72705078125, upper=3478432.875
	 * PaintEvent{PlotArea {} [layout=null] time=42120491 data=null gc=GC {134928621411616} x=0 y=0 width=1178 height=383 count=1}
	 */

	private ElementSupport elementSupport = new ElementSupport(new Range(5422.0, 959989.0), new Range(6475.72705078125, 3478432.875), 1178, 383);

	public void test1() {

		PointPrimary pointPrimary = elementSupport.convertPoint(8, 19);
		assertEquals(11904.62818336163, pointPrimary.getX());
		assertEquals(3306194.791942467, pointPrimary.getY());
	}

	public void test2() {

		RectanglePrimary rectanglePrimary = elementSupport.convertRectangle(44, 15, 40, 25);
		assertEquals(41076.45500848896, rectanglePrimary.getX());
		assertEquals(3342455.441007211, rectanglePrimary.getY());
		assertEquals(32413.140916808145, rectanglePrimary.getWidth());
		assertEquals(226629.05665464886, rectanglePrimary.getHeight());
	}
}