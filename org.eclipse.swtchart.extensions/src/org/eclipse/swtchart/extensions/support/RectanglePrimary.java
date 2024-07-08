/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.support;

import java.util.Objects;

public class RectanglePrimary extends PointPrimary {

	private double width;
	private double height;

	public RectanglePrimary(double x, double y, double width, double height) {

		super(x, y);
		this.width = width;
		this.height = height;
	}

	public double getWidth() {

		return width;
	}

	public double getHeight() {

		return height;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(height, width);
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(!super.equals(obj))
			return false;
		if(getClass() != obj.getClass())
			return false;
		RectanglePrimary other = (RectanglePrimary)obj;
		return Double.doubleToLongBits(height) == Double.doubleToLongBits(other.height) && Double.doubleToLongBits(width) == Double.doubleToLongBits(other.width);
	}

	@Override
	public String toString() {

		return "RectanglePrimary [width=" + width + ", height=" + height + ", getX()=" + getX() + ", getY()=" + getY() + "]";
	}
}