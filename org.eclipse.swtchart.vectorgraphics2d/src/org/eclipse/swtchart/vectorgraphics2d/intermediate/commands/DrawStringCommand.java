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

import java.util.Locale;

public class DrawStringCommand extends Command<String> {

	private final double x;
	private final double y;

	public DrawStringCommand(String string, double x, double y) {

		super(string);
		this.x = x;
		this.y = y;
	}

	public double getX() {

		return x;
	}

	public double getY() {

		return y;
	}

	@Override
	public String toString() {

		return String.format((Locale)null, "%s[value=%s, x=%f, y=%f]", getClass().getName(), getValue(), getX(), getY());
	}
}
