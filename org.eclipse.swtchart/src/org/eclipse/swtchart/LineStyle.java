/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
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
 * Philip Wenig - line style handling
 *******************************************************************************/
package org.eclipse.swtchart;

import org.eclipse.swt.SWT;

public enum LineStyle implements IEnumLabel {

	/** none */
	NONE("None", SWT.NONE), //$NON-NLS-1$
	/** solid */
	SOLID("Solid", SWT.LINE_SOLID), //$NON-NLS-1$
	/** dash */
	DASH("Dash", SWT.LINE_DASH), //$NON-NLS-1$
	/** dot */
	DOT("Dot", SWT.LINE_DOT), //$NON-NLS-1$
	/** dash dot */
	DASHDOT("Dash Dot", SWT.LINE_DASHDOT), //$NON-NLS-1$
	/** dash dot dot */
	DASHDOTDOT("Dash Dot Dot", SWT.LINE_DASHDOTDOT); //$NON-NLS-1$

	private String label;
	private int value;

	private LineStyle(String label, int value) {

		this.label = label;
		this.value = value;
	}

	@Override
	public String label() {

		return label;
	}

	public int value() {

		return value;
	}
}