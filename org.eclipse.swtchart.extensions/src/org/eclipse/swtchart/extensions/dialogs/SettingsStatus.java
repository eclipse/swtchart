/*******************************************************************************
 * Copyright (c) 2022, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.dialogs;

import org.eclipse.swtchart.IEnumLabel;

public enum SettingsStatus implements IEnumLabel {

	NORMAL("Normal"), //
	HIGHLIGHT("Highlight");

	private String label = "";

	private SettingsStatus(String label) {

		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}
}