/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
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

import org.eclipse.swtchart.LineStyle;

public class ElementLine extends AbstractElement implements IGraphicElement {

	private double x2 = 0.0d;
	private double y2 = 0.0d;
	private LineStyle lineStyle = LineStyle.SOLID;
	private int lineWidth = 1;

	public double getX2() {

		return x2;
	}

	public void setX2(double x2) {

		this.x2 = x2;
	}

	public double getY2() {

		return y2;
	}

	public void setY2(double y2) {

		this.y2 = y2;
	}

	public LineStyle getLineStyle() {

		return lineStyle;
	}

	public void setLineStyle(LineStyle lineStyle) {

		this.lineStyle = lineStyle;
	}

	public int getLineWidth() {

		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {

		this.lineWidth = lineWidth;
	}
}