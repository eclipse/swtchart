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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class CharacterTest extends TestCase {

	public CharacterTest() throws IOException {

	}

	@Override
	public void draw(Graphics2D g) {

		double w = getPageSize().getWidth();
		double h = getPageSize().getHeight();
		Charset latin1 = Charset.forName("ISO-8859-1");
		CharsetEncoder latin1Encoder = latin1.newEncoder();
		List<String> charactersInCharset = new ArrayList<>();
		for(char char_ = Character.MIN_VALUE; char_ < Character.MAX_VALUE; char_++) {
			String javaString = String.valueOf(char_);
			if(latin1Encoder.canEncode(char_)) {
				charactersInCharset.add(javaString);
			}
		}
		final int colCount = (int)Math.ceil(Math.sqrt(charactersInCharset.size()));
		final int rowCount = colCount;
		double tileWidth = w / colCount;
		double tileHeight = h / rowCount;
		int charIndex = 0;
		for(double y = 0.0; y < h; y += tileHeight) {
			for(double x = 0.0; x < w; x += tileWidth) {
				String c = charactersInCharset.get(charIndex);
				double tileCenterX = x + tileWidth / 2.0;
				double tileCenterY = y + tileHeight / 2.0;
				g.drawString(c, (float)tileCenterX, (float)tileCenterY);
				charIndex++;
			}
		}
	}
}
