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
package org.eclipse.swtchart.internal;

/**
 * The chart layout data
 */
public class ChartLayoutData {

	/** the width hint */
	public int widthHint;
	/** the height hint */
	public int heightHint;

	/**
	 * Constructor.
	 * 
	 * @param widthHint
	 *            the width hint
	 * @param heightHint
	 *            the height hint
	 */
	public ChartLayoutData(int widthHint, int heightHint) {
		this.widthHint = widthHint;
		this.heightHint = heightHint;
	}
}
