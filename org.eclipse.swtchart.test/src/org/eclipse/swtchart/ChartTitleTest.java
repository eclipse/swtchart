/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.internal.Title;
import org.eclipse.swtchart.util.ChartTestCase;
import org.junit.Test;

/**
 * Test case for chart title.
 */
public class ChartTitleTest extends ChartTestCase {

	private ITitle title;

	@Override
	public void setUp() {

		super.setUp();
		title = chart.getTitle();
	}

	/**
	 * Test for title text.
	 */
	@Test
	public void testText()  {

		// set null
		title.setText(null);
		String text = title.getText();
		assertEquals("Chart Title", text);
		showChart();
		// set text
		title.setText("foo");
		text = title.getText();
		assertEquals("foo", text);
		showChart();
		// set long text
		String LONG_TEXT = "fooooooooooooooooooooooooooooooooooooooooooooooooooooooo";
		title.setText(LONG_TEXT);
		text = title.getText();
		assertEquals(LONG_TEXT, text);
		showChart();
		// set blank
		title.setText("");
		text = title.getText();
		assertEquals("", text);
		int height = ((Title)title).getBounds().height;
		assertEquals(0, height);
		showChart();
	}

	/**
	 * Test for foreground.
	 */
	@Test
	public void testForeground()  {

		// set null
		title.setForeground(null);
		Color color = title.getForeground();
		assertEquals(new RGB(0, 0, 255), color.getRGB());
		showChart();
		// set color
		Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		title.setText("foo");
		title.setForeground(black);
		color = title.getForeground();
		assertEquals(black.getRGB(), color.getRGB());
		showChart();
		// set the disposed color
		color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			title.setForeground(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		color = title.getForeground();
		assertEquals(new RGB(0, 0, 0), color.getRGB());
	}

	/**
	 * Test for title font.
	 */
	@Test
	public void testFont()  {

		// set null
		title.setFont(null);
		Font font = title.getFont();
		FontData fontData = font.getFontData()[0];
		FontData defaultFontData = new Font(Display.getDefault(), "Tahoma", 13, SWT.BOLD).getFontData()[0];
		assertEquals(defaultFontData.getName(), fontData.getName());
		assertEquals(defaultFontData.getHeight(), fontData.getHeight());
		assertEquals(defaultFontData.getStyle(), fontData.getStyle());
		// set font
		title.setText("foo");
		font = new Font(Display.getCurrent(), "Tahoma", 18, SWT.ITALIC);
		title.setFont(font);
		font = title.getFont();
		fontData = font.getFontData()[0];
		assertEquals("Tahoma", fontData.getName());
		assertEquals(18, fontData.getHeight());
		assertEquals(SWT.ITALIC, fontData.getStyle());
		showChart();
		// set the disposed font
		font.dispose();
		try {
			title.setFont(font);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		// set large font size
		title.setFont(new Font(Display.getCurrent(), "Tahoma", 64, SWT.ITALIC));
		font = title.getFont();
		fontData = font.getFontData()[0];
		assertEquals(64, fontData.getHeight());
		showChart();
		// set tiny font size
		title.setFont(new Font(Display.getCurrent(), "Tahoma", 4, SWT.ITALIC));
		font = title.getFont();
		fontData = font.getFontData()[0];
		assertEquals(4, fontData.getHeight());
		showChart();
	}

	/**
	 * Test for title visibility.
	 */
	@Test
	public void testVisibility()  {

		// set visibility
		title.setText("foo");
		title.setVisible(false);
		assertFalse(title.isVisible());
		showChart();
		title.setText("foo");
		title.setVisible(true);
		assertTrue(title.isVisible());
		showChart();
		// set visibility with blank
		title.setText("");
		title.setVisible(true);
		assertTrue(title.isVisible());
		showChart();
		int height = ((Title)title).getBounds().height;
		assertEquals(0, height);
	}
}
