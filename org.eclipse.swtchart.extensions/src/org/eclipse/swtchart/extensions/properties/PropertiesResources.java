/*******************************************************************************
 * Copyright (c) 2008, 2018 SWTChart project.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * SWT resources created with properties dialog.
 */
public class PropertiesResources {

	/** the fonts */
	private Map<String, Font> fonts;
	/** the colors */
	private Map<String, Color> colors;

	/**
	 * The constructor.
	 */
	public PropertiesResources() {
		fonts = new HashMap<String, Font>();
		colors = new HashMap<String, Color>();
	}

	/**
	 * Gets the font associated with the given key.
	 * 
	 * @param key
	 *            the key associated with the font to get
	 * @return the font
	 */
	public Font getFont(String key) {

		return fonts.get(key);
	}

	/**
	 * Gets the color associated with the given key.
	 * 
	 * @param key
	 *            the key associated with the color to get
	 * @return the color
	 */
	public Color getColor(String key) {

		return colors.get(key);
	}

	/**
	 * Puts the given font. If a font associated with the given key already
	 * exists, the existing font will be disposed. When chart is disposed, all
	 * stored fonts in this object will be disposed.
	 * <p>
	 * When the resource won't be used, the resource should be disposed and
	 * removed.
	 * 
	 * @param key
	 *            the key for given font
	 * @param font
	 *            the font to be stored
	 */
	public void put(String key, Font font) {

		Font oldFont = fonts.get(key);
		if(oldFont != null) {
			oldFont.dispose();
		}
		fonts.put(key, font);
	}

	/**
	 * Puts the given color. If a font associated with the given key already
	 * exists, the existing color will be disposed. When chart is disposed, all
	 * stored colors in this object will be disposed.
	 * 
	 * @param key
	 *            the key for given color
	 * @param color
	 *            the color to be stored
	 */
	public void put(String key, Color color) {

		Color oldColor = colors.get(key);
		if(oldColor != null) {
			oldColor.dispose();
		}
		colors.put(key, color);
	}

	/**
	 * Removes the font associated with the given key. This method will be
	 * invoked typically when removing series object or axis object.
	 * 
	 * @param key
	 *            the key associated with the font to be removed
	 */
	public void removeFont(String key) {

		Font font = fonts.get(key);
		if(font != null) {
			fonts.remove(key);
			font.dispose();
		}
	}

	/**
	 * Removes the color associated with the given key. This method will be
	 * invoked typically when removing series object or axis object.
	 * 
	 * @param key
	 *            the key associated with the color to be removed
	 */
	public void removeColor(String key) {

		Color color = colors.get(key);
		if(color != null) {
			colors.remove(key);
			color.dispose();
		}
	}

	/**
	 * Disposes the SWT resources.
	 */
	public void dispose() {

		for(Entry<String, Font> entry : fonts.entrySet()) {
			entry.getValue().dispose();
		}
		for(Entry<String, Color> entry : colors.entrySet()) {
			entry.getValue().dispose();
		}
	}
}
