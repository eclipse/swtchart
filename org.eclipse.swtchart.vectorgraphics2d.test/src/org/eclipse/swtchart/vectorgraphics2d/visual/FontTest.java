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
package org.eclipse.swtchart.vectorgraphics2d.visual;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;

import org.eclipse.swtchart.vectorgraphics2d.core.GraphicsState;

public class FontTest extends TestCase {

	private static final Font DEFAULT_FONT = GraphicsState.DEFAULT_FONT;

	public FontTest() throws IOException {

	}

	@Override
	public void draw(Graphics2D g) {

		final int tileCountH = 4;
		final int tileCountV = 8;
		final double wTile = getPageSize().getWidth() / tileCountH;
		final double hTile = getPageSize().getHeight() / tileCountV;
		final double xOrigin = (getPageSize().getWidth() - tileCountH * wTile) / 2.0;
		final double yOrigin = (getPageSize().getHeight() - tileCountV * hTile) / 2.0;
		double x = xOrigin;
		double y = yOrigin;
		final float[] sizes = {DEFAULT_FONT.getSize2D(), DEFAULT_FONT.getSize2D() / 2f};
		final String[] names = {DEFAULT_FONT.getName(), Font.SERIF, Font.MONOSPACED, "Arial"};
		final int[] styles = {Font.PLAIN, Font.ITALIC, Font.BOLD, Font.BOLD | Font.ITALIC};
		for(float size : sizes) {
			for(String name : names) {
				for(int style : styles) {
					Font font = new Font(name, style, 10).deriveFont(size);
					g.setFont(font);
					g.drawString("vg2d", (float)x, (float)y);
					x += wTile;
					if(x >= tileCountH * wTile) {
						x = xOrigin;
						y += hTile;
					}
				}
			}
		}
	}
}
