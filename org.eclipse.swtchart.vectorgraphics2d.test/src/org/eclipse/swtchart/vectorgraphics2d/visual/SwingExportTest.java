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

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;

public class SwingExportTest extends TestCase {

	public SwingExportTest() throws IOException {

	}

	@Override
	public void draw(Graphics2D g) {

		JFrame frame = new JFrame();
		frame.getContentPane().add(new JButton("Hello Swing!"), BorderLayout.CENTER);
		frame.getContentPane().add(new JSlider(), BorderLayout.NORTH);
		frame.setSize(200, 250);
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		frame.setVisible(true);
		frame.printAll(g);
		frame.setVisible(false);
		frame.dispose();
	}
}
