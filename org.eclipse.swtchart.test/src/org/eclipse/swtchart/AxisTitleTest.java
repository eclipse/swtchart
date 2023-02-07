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
 * Test case for axis title.
 */
public class AxisTitleTest extends ChartTestCase {

	private ITitle xAxisTitle;
	private ITitle yAxisTitle;

	@Override
	public void setUp() {

		super.setUp();
		xAxisTitle = chart.getAxisSet().getXAxis(0).getTitle();
		yAxisTitle = chart.getAxisSet().getYAxis(0).getTitle();
	}

	/**
	 * Test for title text.
	 */
	@Test
	public void testText() throws Exception {

		// set null
		xAxisTitle.setText(null);
		String text = xAxisTitle.getText();
		assertEquals("X Axis", text);
		showChart();
		yAxisTitle.setText(null);
		text = yAxisTitle.getText();
		assertEquals("Y Axis", text);
		showChart();
		// set text
		xAxisTitle.setText("foo");
		text = xAxisTitle.getText();
		assertEquals("foo", text);
		showChart();
		yAxisTitle.setText("foo");
		text = yAxisTitle.getText();
		assertEquals("foo", text);
		showChart();
		// set long text
		final String LONG_TITLE = "fooooooooooooooooooooooooooooooooooooooooooooooooooooooo";
		xAxisTitle.setText(LONG_TITLE);
		text = xAxisTitle.getText();
		assertEquals(LONG_TITLE, text);
		showChart();
		yAxisTitle.setText(LONG_TITLE);
		text = yAxisTitle.getText();
		assertEquals(LONG_TITLE, text);
		showChart();
		// set blank
		xAxisTitle.setText("");
		text = xAxisTitle.getText();
		assertEquals("", text);
		int height = ((Title)xAxisTitle).getBounds().height;
		assertEquals(0, height);
		showChart();
		yAxisTitle.setText("");
		text = yAxisTitle.getText();
		assertEquals("", text);
		height = ((Title)yAxisTitle).getBounds().width;
		assertEquals(0, height);
		showChart();
	}

	/**
	 * Test for foreground.
	 */
	@Test
	public void testForeground() {

		// set null
		xAxisTitle.setForeground(null);
		Color color = xAxisTitle.getForeground();
		assertEquals(new RGB(0, 0, 255), color.getRGB());
		showChart();
		yAxisTitle.setForeground(null);
		color = yAxisTitle.getForeground();
		assertEquals(new RGB(0, 0, 255), color.getRGB());
		showChart();
		// set color
		Color syan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
		xAxisTitle.setForeground(syan);
		color = xAxisTitle.getForeground();
		assertEquals(syan.getRGB(), color.getRGB());
		showChart();
		yAxisTitle.setForeground(syan);
		color = yAxisTitle.getForeground();
		assertEquals(syan.getRGB(), color.getRGB());
		showChart();
		// set the disposed color
		color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			xAxisTitle.setForeground(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		color = xAxisTitle.getForeground();
		assertEquals(syan.getRGB(), color.getRGB());
		color = new Color(Display.getDefault(), 0, 0, 0);
		color.dispose();
		try {
			yAxisTitle.setForeground(color);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		color = yAxisTitle.getForeground();
		assertEquals(syan.getRGB(), color.getRGB());
	}

	/**
	 * Test for axis title font.
	 */
	@Test
	public void testFont() {

		// set null
		xAxisTitle.setFont(null);
		FontData fontData = xAxisTitle.getFont().getFontData()[0];
		Font font = new Font(Display.getCurrent(), "Tahoma", 13, SWT.BOLD);
		FontData defaultFontData = font.getFontData()[0];
		assertEquals(defaultFontData.getName(), fontData.getName());
		assertEquals(defaultFontData.getHeight(), fontData.getHeight());
		assertEquals(defaultFontData.getStyle(), fontData.getStyle());
		yAxisTitle.setFont(null);
		fontData = yAxisTitle.getFont().getFontData()[0];
		assertEquals(defaultFontData.getName(), fontData.getName());
		assertEquals(defaultFontData.getHeight(), fontData.getHeight());
		assertEquals(defaultFontData.getStyle(), fontData.getStyle());
		font.dispose();
		// set font
		font = new Font(Display.getCurrent(), "Tahoma", 15, SWT.ITALIC);
		xAxisTitle.setFont(font);
		fontData = xAxisTitle.getFont().getFontData()[0];
		assertEquals("Tahoma", fontData.getName());
		assertEquals(15, fontData.getHeight());
		assertEquals(SWT.ITALIC, fontData.getStyle());
		showChart();
		yAxisTitle.setFont(font);
		fontData = yAxisTitle.getFont().getFontData()[0];
		assertEquals("Tahoma", fontData.getName());
		assertEquals(15, fontData.getHeight());
		assertEquals(SWT.ITALIC, fontData.getStyle());
		showChart();
		// set the disposed font
		font.dispose();
		try {
			xAxisTitle.setFont(font);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		try {
			yAxisTitle.setFont(font);
			fail();
		} catch(IllegalArgumentException e) {
			// expected to reach here
		}
		// set large font size
		font = new Font(Display.getCurrent(), "Tahoma", 64, SWT.ITALIC);
		xAxisTitle.setFont(font);
		fontData = xAxisTitle.getFont().getFontData()[0];
		assertEquals(64, fontData.getHeight());
		showChart();
		font.dispose();
		font = new Font(Display.getCurrent(), "Tahoma", 64, SWT.ITALIC);
		yAxisTitle.setFont(font);
		fontData = yAxisTitle.getFont().getFontData()[0];
		assertEquals(64, fontData.getHeight());
		showChart();
		font.dispose();
		// set tiny font size
		font = new Font(Display.getCurrent(), "Tahoma", 4, SWT.ITALIC);
		xAxisTitle.setFont(font);
		fontData = xAxisTitle.getFont().getFontData()[0];
		assertEquals(4, fontData.getHeight());
		showChart();
		font.dispose();
		font = new Font(Display.getCurrent(), "Tahoma", 4, SWT.ITALIC);
		yAxisTitle.setFont(font);
		font = yAxisTitle.getFont();
		fontData = font.getFontData()[0];
		assertEquals(4, fontData.getHeight());
		showChart();
		font.dispose();
	}

	/**
	 * Test for title visibility.
	 */
	@Test
	public void testVisibility() {

		// set visibility
		xAxisTitle.setVisible(false);
		assertFalse(xAxisTitle.isVisible());
		showChart();
		xAxisTitle.setVisible(true);
		assertTrue(xAxisTitle.isVisible());
		showChart();
		yAxisTitle.setVisible(false);
		assertFalse(yAxisTitle.isVisible());
		showChart();
		yAxisTitle.setVisible(true);
		assertTrue(yAxisTitle.isVisible());
		showChart();
	}
}
