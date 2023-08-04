/*******************************************************************************
 * Copyright (c) 2019, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Sanatt Abrol - SVG export code
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.export.menu.vector;

import org.eclipse.swt.graphics.Color;

public abstract class AbstractInkscapeTemplate implements IVectorDataExport {

	protected static final String LINE_DELIMITER = "\n";
	protected static final String SPLIT_LINE_DELIMITER = "\\n";
	//
	protected static final String AXIS_X = "x"; //$NON-NLS-1$
	protected static final String AXIS_Y = "y"; //$NON-NLS-1$

	/**
	 * E.g.: #ffffff
	 * 
	 * @param color
	 * @return
	 */
	protected String getColor(Color color) {

		StringBuilder builder = new StringBuilder("#");
		//
		double r = color.getRed();
		double g = color.getGreen();
		double b = color.getBlue();
		double[] rgb = new double[]{r, g, b};
		//
		for(double x : rgb) {
			double hex = 16.0d;
			double div = x / hex;
			int count = (int)div;
			double remainder = div - count;
			remainder = (int)(remainder * hex);
			char first, second;
			//
			if(count >= 10) {
				first = (char)('a' + (count - 10));
			} else {
				first = (char)('0' + count);
			}
			//
			if(remainder >= 10) {
				second = (char)('a' + (remainder - 10));
			} else {
				second = (char)('0' + remainder);
			}
			//
			builder.append(first);
			builder.append(second);
		}
		//
		return builder.toString();
	}

	protected String getRegularExpression(String searchTerm) {

		return ".*" + searchTerm + ".*";
	}
}