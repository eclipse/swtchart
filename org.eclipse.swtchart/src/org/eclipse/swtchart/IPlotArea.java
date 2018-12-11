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

/**
 * The plot area.
 */
public interface IPlotArea {

	/**
	 * Adds the custom paint listener.
	 *
	 * @param listener
	 *            the custom paint listener
	 */
	public void addCustomPaintListener(ICustomPaintListener listener);

	/**
	 * Removes the custom paint listener
	 *
	 * @param listener
	 *            the custom paint listener
	 */
	public void removeCustomPaintListener(ICustomPaintListener listener);
}
