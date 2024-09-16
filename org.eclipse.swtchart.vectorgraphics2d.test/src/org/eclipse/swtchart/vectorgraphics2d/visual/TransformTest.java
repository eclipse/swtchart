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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class TransformTest extends TestCase {

	public TransformTest() throws IOException {

	}

	@SuppressWarnings("unused")
	@Override
	public void draw(Graphics2D g) {

		final int rowCount = 2;
		final int colCount = 4;
		double wTile = getPageSize().getWidth() / colCount;
		double hTile = wTile;
		g.translate(0.5 * wTile, 0.5 * hTile);
		AffineTransform txOrig = g.getTransform();
		Shape s = new Rectangle2D.Double(0.0, 0.0, 0.5 * wTile, 0.75 * hTile);
		// Row 1
		g.draw(s);
		g.translate(wTile, 0.0);
		g.draw(s);
		g.translate(wTile, 0.0);
		{
			Graphics2D g2 = (Graphics2D)g.create();
			g2.scale(0.5, 0.5);
			g2.draw(s);
			g2.dispose();
		}
		g.translate(wTile, 0.0);
		{
			Graphics2D g2 = (Graphics2D)g.create();
			g2.rotate(Math.toRadians(30.0));
			g2.draw(s);
			g2.dispose();
		}
		// Row 2
		g.setTransform(txOrig);
		g.translate(0.0, hTile);
		g.shear(0.5, 0.0);
		g.draw(s);
		g.shear(-0.5, 0.0);
		g.translate(wTile, 0.0);
		g.shear(0.0, 0.5);
		g.draw(s);
		g.shear(0.0, -0.5);
	}
}
