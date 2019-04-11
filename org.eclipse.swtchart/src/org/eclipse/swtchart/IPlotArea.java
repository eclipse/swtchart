/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
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
