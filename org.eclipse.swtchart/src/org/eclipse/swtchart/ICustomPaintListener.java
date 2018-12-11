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
