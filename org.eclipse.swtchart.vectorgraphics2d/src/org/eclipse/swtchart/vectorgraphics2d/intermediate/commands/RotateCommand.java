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

public class RotateCommand extends AffineTransformCommand {

	private final double theta;
	private final double centerX;
	private final double centerY;

	public RotateCommand(double theta, double centerX, double centerY) {

		super(AffineTransform.getRotateInstance(theta, centerX, centerY));
		this.theta = theta;
		this.centerX = centerX;
		this.centerY = centerY;
	}

	public double getTheta() {

		return theta;
	}

	public double getCenterX() {

		return centerX;
	}

	public double getCenterY() {

		return centerY;
	}

	@Override
	public String toString() {

		return String.format((Locale)null, "%s[theta=%f, centerX=%f, centerY=%f, value=%s]", getClass().getName(), getTheta(), getCenterX(), getCenterY(), getValue());
	}
}
