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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageTest extends TestCase {

	public ImageTest() throws IOException {

	}

	@Override
	public void draw(Graphics2D g) {

		// Draw an image
		BufferedImage image = new BufferedImage(4, 3, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gImage = (Graphics2D)image.getGraphics();
		gImage.setPaint(new GradientPaint(new Point2D.Double(0.0, 0.0), Color.RED, new Point2D.Double(3.0, 2.0), Color.BLUE));
		gImage.fill(new Rectangle2D.Double(0.0, 0.0, 4.0, 3.0));
		g.drawImage(image, 0, 0, (int)getPageSize().getWidth(), (int)(0.5 * getPageSize().getHeight()), null);
		g.rotate(-10.0 / 180.0 * Math.PI, 2.0, 1.5);
		g.drawImage(image, (int)(0.1 * getPageSize().getWidth()), (int)(0.6 * getPageSize().getHeight()), (int)(0.33 * getPageSize().getWidth()), (int)(0.33 * getPageSize().getHeight()), null);
	}
}
