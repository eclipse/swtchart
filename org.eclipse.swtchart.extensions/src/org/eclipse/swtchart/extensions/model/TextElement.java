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

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class TextElement implements ITextElement {

	private String label = "";
	private Color color = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private int rotation = -90;
	private double x = 0.0d;
	private double y = 0.0d;

	@Override
	public String getLabel() {

		return label;
	}

	@Override
	public void setLabel(String label) {

		this.label = label;
	}

	@Override
	public Color getColor() {

		return color;
	}

	@Override
	public void setColor(Color color) {

		this.color = color;
	}

	@Override
	public int getRotation() {

		return rotation;
	}

	@Override
	public void setRotation(int rotation) {

		this.rotation = rotation;
	}

	@Override
	public double getX() {

		return x;
	}

	@Override
	public void setX(double x) {

		this.x = x;
	}

	@Override
	public double getY() {

		return y;
	}

	@Override
	public void setY(double y) {

		this.y = y;
	}

	@Override
	public int hashCode() {

		return Objects.hash(label, x, y);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		TextElement other = (TextElement)obj;
		return Objects.equals(label, other.label) && Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
	}

	@Override
	public String toString() {

		return "TextLabel [label=" + label + ", color=" + color + ", x=" + x + ", y=" + y + ", rotation=" + rotation + "]";
	}
}