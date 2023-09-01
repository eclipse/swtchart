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

public abstract class AbstractElement implements IElement {

	private double x = 0.0d;
	private double y = 0.0d;
	private Color color = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private int alpha = 255;

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
	public Color getColor() {

		return color;
	}

	@Override
	public void setColor(Color color) {

		this.color = color;
	}

	@Override
	public int getAlpha() {

		return alpha;
	}

	@Override
	public void setAlpha(int alpha) {

		this.alpha = alpha;
	}

	@Override
	public int hashCode() {

		return Objects.hash(color, x, y);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		AbstractElement other = (AbstractElement)obj;
		return Objects.equals(color, other.color) && Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
	}

	@Override
	public String toString() {

		return "AbstractElement [x=" + x + ", y=" + y + ", color=" + color + "]";
	}
}