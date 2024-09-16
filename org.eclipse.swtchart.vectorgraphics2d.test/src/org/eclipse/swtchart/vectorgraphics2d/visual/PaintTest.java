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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class PaintTest extends TestCase {

	public PaintTest() throws IOException {

	}

	@Override
	public void draw(Graphics2D g) {

		// Draw multiple rotated rectangles
		final int steps = 25;
		final int cols = 5;
		final int rows = steps / cols;
		final double tileWidth = getPageSize().getWidth() / cols;
		final double tileHeight = getPageSize().getHeight() / rows;
		g.translate(tileWidth / 2, tileHeight / 2);
		final double rectWidth = tileWidth * 0.8;
		final double rectHeight = tileHeight * 0.8;
		Rectangle2D rect = new Rectangle2D.Double(-rectWidth / 2, -rectHeight / 2, rectWidth, rectHeight);
		g.setPaint(new GradientPaint(0f, (float)(-rectHeight / 2), Color.RED, 0f, (float)(rectHeight / 2), Color.BLUE));
		for(int i = 0; i < steps; i++) {
			AffineTransform txOld = g.getTransform();
			AffineTransform tx = new AffineTransform(txOld);
			int col = i % 5;
			int row = i / 5;
			tx.translate(col * tileWidth, row * tileHeight);
			tx.rotate(i * Math.toRadians(360.0 / steps));
			g.setTransform(tx);
			g.fill(rect);
			g.setTransform(txOld);
		}
	}
}
