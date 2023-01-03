/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart;

import org.eclipse.swt.SWT;

public enum Antialias implements IEnumLabel {

	DEFAULT("Default", SWT.DEFAULT), //
	ON("On", SWT.ON), //
	OFF("Off", SWT.OFF);

	private String label;
	private int value;

	private Antialias(String label, int value) {

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