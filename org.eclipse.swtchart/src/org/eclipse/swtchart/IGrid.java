/*******************************************************************************
 * Copyright (c) 2008, 2020 SWTChart project.
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

import org.eclipse.swt.graphics.Color;

/**
 * A grid.
 */
public interface IGrid {

	/**
	 * returns the visibility of Grid
	 * 
	 * @returns if Grid is Visible
	 */
	public boolean isVisible();

	/**
	 * sets visibility of the grid.
	 * 
	 * @param isVisible
	 */
	public void setVisible(boolean isVisible);

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