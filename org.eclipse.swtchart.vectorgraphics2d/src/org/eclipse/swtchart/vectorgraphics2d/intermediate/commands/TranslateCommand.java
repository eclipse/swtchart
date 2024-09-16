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

public class TranslateCommand extends AffineTransformCommand {

	private final double deltaX;
	private final double deltaY;

	public TranslateCommand(double x, double y) {

		super(AffineTransform.getTranslateInstance(x, y));
		this.deltaX = x;
		this.deltaY = y;
	}

	public double getDeltaX() {

		return deltaX;
	}

	public double getDeltaY() {

		return deltaY;
	}

	@Override
	public String toString() {

		return String.format((Locale)null, "%s[deltaX=%f, deltaY=%f, value=%s]", getClass().getName(), getDeltaX(), getDeltaY(), getValue());
	}
}
