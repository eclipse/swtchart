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
package org.eclipse.swtchart.vectorgraphics2d.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.junit.Test;

public class GraphicsStateTest {

	@Test
	public void testInitialStateIsEqualToGraphics2D() {

		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		GraphicsState state = new GraphicsState();
		assertEquals(state.getBackground(), g2d.getBackground());
		assertEquals(state.getColor(), g2d.getColor());
		assertEquals(state.getClip(), g2d.getClip());
		assertEquals(state.getComposite(), g2d.getComposite());
		assertEquals(state.getFont(), g2d.getFont());
		assertEquals(state.getPaint(), g2d.getPaint());
		assertEquals(state.getStroke(), g2d.getStroke());
		assertEquals(state.getTransform(), g2d.getTransform());
	}

	@Test
	public void testEquals() {

		GraphicsState state1 = new GraphicsState();
		state1.setBackground(Color.WHITE);
		state1.setColor(Color.BLACK);
		state1.setClip(new Rectangle2D.Double(0, 0, 10, 10));
		GraphicsState state2 = new GraphicsState();
		state2.setBackground(Color.WHITE);
		state2.setColor(Color.BLACK);
		state2.setClip(new Rectangle2D.Double(0, 0, 10, 10));
		assertEquals(state1, state2);
		state2.setTransform(AffineTransform.getTranslateInstance(5, 5));
		assertFalse(state1.equals(state2));
	}

	@Test
	public void testClone() throws CloneNotSupportedException {

		GraphicsState state = new GraphicsState();
		state.setBackground(Color.BLUE);
		state.setColor(Color.GREEN);
		state.setClip(new Rectangle2D.Double(2, 3, 4, 2));
		GraphicsState clone = (GraphicsState)state.clone();
		assertNotSame(state, clone);
		assertEquals(state, clone);
	}
}
