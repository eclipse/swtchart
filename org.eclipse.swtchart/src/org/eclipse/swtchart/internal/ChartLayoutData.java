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
