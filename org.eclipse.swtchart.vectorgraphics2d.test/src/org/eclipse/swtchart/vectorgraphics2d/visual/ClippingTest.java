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
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.IOException;

public class ClippingTest extends TestCase {

	public ClippingTest() throws IOException {

	}

	@Override
	public void draw(Graphics2D g) {

		double w = getPageSize().getWidth();
		double h = getPageSize().getHeight();
		AffineTransform txOrig = g.getTransform();
		g.translate(w / 2.0, h / 2.0);
		g.setClip(new Ellipse2D.Double(-0.6 * w / 2.0, -h / 2.0, 0.6 * w, h));
		for(double x = -w / 2.0; x < w / 2.0; x += 4.0) {
			g.draw(new Line2D.Double(x, -h / 2.0, x, h / 2.0));
		}
		g.rotate(Math.toRadians(-90.0));
		g.clip(new Ellipse2D.Double(-0.6 * w / 2.0, -h / 2.0, 0.6 * w, h));
		for(double x = -h / 2.0; x < h / 2.0; x += 4.0) {
			g.draw(new Line2D.Double(x, -w / 2.0, x, w / 2.0));
		}
		g.setTransform(txOrig);
		g.setClip(null);
		g.draw(new Line2D.Double(0.0, 0.0, w, h));
	}
}
