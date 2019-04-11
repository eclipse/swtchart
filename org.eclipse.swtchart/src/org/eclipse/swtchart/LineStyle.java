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
 * A line style.
 */
public enum LineStyle {
	/** none */
	NONE("None"),
	/** solid */
	SOLID("Solid"),
	/** dash */
	DASH("Dash"),
	/** dot */
	DOT("Dot"),
	/** dash dot */
	DASHDOT("Dash Dot"),
	/** dash dot dot */
	DASHDOTDOT("Dash Dot Dot");

	/** the label for line style */
	public final String label;

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            the label for line style
	 */
	private LineStyle(String label) {
		this.label = label;
	}
}