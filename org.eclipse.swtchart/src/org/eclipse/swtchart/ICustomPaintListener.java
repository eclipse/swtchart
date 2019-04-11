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

import org.eclipse.swt.events.PaintListener;

/**
 * The paint listener to paint on plot area.
 */
public interface ICustomPaintListener extends PaintListener {

	/**
	 * Gets the state indicating if painting behind series.
	 *
	 * @return True if painting behind series
	 */
	boolean drawBehindSeries();
}
