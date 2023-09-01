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

public class ElementRectangle extends AbstractElement implements IGraphicElement {

	private double width = 0.0d;
	private double height = 0.0d;

	public double getWidth() {

		return width;
	}

	public void setWidth(double width) {

		this.width = width;
	}

	public double getHeight() {

		return height;
	}

	public void setHeight(double height) {

		this.height = height;
	}
}