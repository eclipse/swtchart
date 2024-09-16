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

import java.awt.Image;
import java.util.Locale;

public class DrawImageCommand extends Command<Image> {

	private final int imageWidth;
	private final int imageHeight;
	private final double x;
	private final double y;
	private final double width;
	private final double height;

	public DrawImageCommand(Image image, int imageWidth, int imageHeight, double x, double y, double width, double height) {

		super(image);
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getImageWidth() {

		return imageWidth;
	}

	public int getImageHeight() {

		return imageHeight;
	}

	public double getX() {

		return x;
	}

	public double getY() {

		return y;
	}

	public double getWidth() {

		return width;
	}

	public double getHeight() {

		return height;
	}

	@Override
	public String toString() {

		return String.format((Locale)null, "%s[value=%s, imageWidth=%d, imageHeight=%d, x=%f, y=%f, width=%f, height=%f]", getClass().getName(), getValue(), getImageWidth(), getImageHeight(), getX(), getY(), getWidth(), getHeight());
	}
}
