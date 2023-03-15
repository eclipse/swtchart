/*******************************************************************************
 * Copyright (c) 2019, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Sanatt Abrol - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.export.extended.awt;

import javax.swing.JPanel;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;

public class SwtToAwtUtils {

	/** Dummy string value to get font metrics */
	private final static String DUMMY_STRING = "ABCxyz"; //$NON-NLS-1$

	/**
	 * Method to convert org.eclipse.swt.Font to java.awt.Font
	 * 
	 * @param device
	 *            the device where font is to be displayed
	 * @param font
	 *            the org.eclipse.swt.Font to be converted to java.awt.Font
	 */
	public static java.awt.Font toAwtFont(Device device, Font font) {

		FontData fontData = font.getFontData()[0];
		return toAwtFont(device, fontData);
	}

	/**
	 * Method to convert org.eclipse.swt.FontData to java.awt.Font
	 *
	 * @param device
	 *            the device where font is to be displayed
	 * @param fontData
	 *            the org.eclipse.swt.FontData to be converted to java.awt.Font
	 */
	public static java.awt.Font toAwtFont(Device device, FontData fontData) {

		int fontHeight = (int)Math.round(fontData.getHeight() * device.getDPI().y / 72.0);
		GC tempGC = new GC(device);
		Font tempFont = new Font(device, fontData);
		tempGC.setFont(tempFont);
		JPanel dummyPanel = new JPanel();
		java.awt.Font tempAWTFont = new java.awt.Font(fontData.getName(), fontData.getStyle(), fontHeight);
		if(dummyPanel.getFontMetrics(tempAWTFont).stringWidth(DUMMY_STRING) > tempGC.textExtent(DUMMY_STRING).x) {
			while(dummyPanel.getFontMetrics(tempAWTFont).stringWidth(DUMMY_STRING) > tempGC.textExtent(DUMMY_STRING).x) {
				fontHeight--;
				tempAWTFont = new java.awt.Font(fontData.getName(), fontData.getStyle(), fontHeight);
			}
		} else if(dummyPanel.getFontMetrics(tempAWTFont).stringWidth(DUMMY_STRING) < tempGC.textExtent(DUMMY_STRING).x) {
			while(dummyPanel.getFontMetrics(tempAWTFont).stringWidth(DUMMY_STRING) < tempGC.textExtent(DUMMY_STRING).x) {
				fontHeight++;
				tempAWTFont = new java.awt.Font(fontData.getName(), fontData.getStyle(), fontHeight);
			}
		}
		tempFont.dispose();
		tempGC.dispose();
		return new java.awt.Font(fontData.getName(), fontData.getStyle(), fontHeight);
	}

	/**
	 * Creates an AWT color instance to match the RGB values of the specified SWT
	 * color.
	 *
	 * @param color
	 *            The SWT color to match.
	 * @return an AWT color abject.
	 */
	public static java.awt.Color toAwtColor(org.eclipse.swt.graphics.Color color) {

		return new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());
	}
}
