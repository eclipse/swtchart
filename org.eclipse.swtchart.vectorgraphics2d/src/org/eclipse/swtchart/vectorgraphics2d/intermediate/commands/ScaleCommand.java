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

public class ScaleCommand extends AffineTransformCommand {

	private final double scaleX;
	private final double scaleY;

	public ScaleCommand(double scaleX, double scaleY) {

		super(AffineTransform.getScaleInstance(scaleX, scaleY));
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public double getScaleX() {

		return scaleX;
	}

	public double getScaleY() {

		return scaleY;
	}

	@Override
	public String toString() {

		return String.format((Locale)null, "%s[scaleX=%f, scaleY=%f, value=%s]", getClass().getName(), getScaleX(), getScaleY(), getValue());
	}
}
