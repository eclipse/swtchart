/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorsSupport {

	private static final Map<RGB, Color> COLOR_MAP = new HashMap<>();

	private ColorsSupport() {
	}

	/**
	 * The color is mapped and disposed by this color support.
	 * Hence, it doesn't need to be disposed manually.
	 * 
	 * @param rgb
	 * @return color
	 */
	public static Color getColor(RGB rgb) {

		Color color = COLOR_MAP.get(rgb);
		if(color == null) {
			color = new Color(Display.getDefault(), rgb);
			COLOR_MAP.put(rgb, color);
		}
		return color;
	}

	@Override
	protected void finalize() throws Throwable {

		for(Color color : COLOR_MAP.values()) {
			if(color != null) {
				color.dispose();
			}
		}
		super.finalize();
	}
}
