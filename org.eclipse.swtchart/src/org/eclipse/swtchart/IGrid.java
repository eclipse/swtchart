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
package org.eclipse.swtchart;

import org.eclipse.swt.graphics.Color;

/**
 * A grid.
 */
public interface IGrid {

	/**
	 * Gets the foreground color.
	 * 
	 * @return the foreground color
	 */
	Color getForeground();

	/**
	 * Sets the foreground color.
	 * 
	 * @param color
	 *            the foreground color
	 */
	void setForeground(Color color);

	/**
	 * Gets the line style.
	 * 
	 * @return the line style.
	 */
	LineStyle getStyle();

	/**
	 * Sets the line style.
	 * 
	 * @param style
	 *            the line style
	 */
	void setStyle(LineStyle style);
}