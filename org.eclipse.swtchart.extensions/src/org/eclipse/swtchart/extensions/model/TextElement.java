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
package org.eclipse.swtchart.extensions.model;

public class TextElement extends AbstractElement implements ITextElement {

	private String label = "";
	private int rotation = -90;

	@Override
	public String getLabel() {

		return label;
	}

	@Override
	public void setLabel(String label) {

		this.label = label;
	}

	@Override
	public int getRotation() {

		return rotation;
	}

	@Override
	public void setRotation(int rotation) {

		this.rotation = rotation;
	}
}