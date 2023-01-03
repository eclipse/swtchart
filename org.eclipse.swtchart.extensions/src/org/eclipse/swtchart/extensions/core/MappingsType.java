/*******************************************************************************
 * Copyright (c) 2021, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.core;

import org.eclipse.swtchart.IEnumLabel;

public enum MappingsType implements IEnumLabel {

	NONE("None"), //
	BAR("Bar"), //
	LINE("Line"), //
	SCATTER("Scatter"), //
	CIRCULAR("Circular"); //

	private String label;

	private MappingsType(String label) {

		this.label = label;
	}

	@Override
	public String label() {

		return label;
	}
}