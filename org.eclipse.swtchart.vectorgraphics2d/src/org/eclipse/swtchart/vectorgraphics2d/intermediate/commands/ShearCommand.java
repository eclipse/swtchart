/*******************************************************************************
 * Copyright (c) 2010, 2019 VectorGraphics2D project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Erich Seifert - initial API and implementation
 * Michael Seifert - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.intermediate.commands;

import java.awt.geom.AffineTransform;
import java.util.Locale;

public class ShearCommand extends AffineTransformCommand {

	private final double shearX;
	private final double shearY;

	public ShearCommand(double shearX, double shearY) {

		super(AffineTransform.getShearInstance(shearX, shearY));
		this.shearX = shearX;
		this.shearY = shearY;
	}

	public double getShearX() {

		return shearX;
	}

	public double getShearY() {

		return shearY;
	}

	@Override
	public String toString() {

		return String.format((Locale)null, "%s[shearX=%f, shearY=%f, value=%s]", getClass().getName(), getShearX(), getShearY(), getValue());
	}
}
