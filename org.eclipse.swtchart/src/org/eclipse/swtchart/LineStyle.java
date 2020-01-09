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
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart;

/**
 * A line style.
 */
public enum LineStyle {
	/** none */
	NONE("None"), //$NON-NLS-1$
	/** solid */
	SOLID("Solid"), //$NON-NLS-1$
	/** dash */
	DASH("Dash"), //$NON-NLS-1$
	/** dot */
	DOT("Dot"), //$NON-NLS-1$
	/** dash dot */
	DASHDOT("Dash Dot"), //$NON-NLS-1$
	/** dash dot dot */
	DASHDOTDOT("Dash Dot Dot"); //$NON-NLS-1$

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