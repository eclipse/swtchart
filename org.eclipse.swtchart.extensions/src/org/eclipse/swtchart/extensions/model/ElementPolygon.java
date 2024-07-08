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
package org.eclipse.swtchart.extensions.model;

public class ElementPolygon extends AbstractElement implements IGraphicElement {

	private double[] polygon = new double[2];

	@Override
	public double getX() {

		return polygon[0];
	}

	@Override
	public void setX(double x) {

		polygon[0] = x;
	}

	@Override
	public double getY() {

		return polygon[1];
	}

	@Override
	public void setY(double y) {

		polygon[1] = y;
	}

	public boolean hasData() {

		return polygon.length >= 6;
	}

	public double[] getPolygon() {

		return polygon;
	}

	public void setPolygon(double[] polygon) {

		/*
		 * Ensure that it contains at least the x|y value.
		 */
		if(polygon == null || polygon.length < 2) {
			this.polygon = new double[2];
		} else {
			this.polygon = polygon;
		}
	}
}