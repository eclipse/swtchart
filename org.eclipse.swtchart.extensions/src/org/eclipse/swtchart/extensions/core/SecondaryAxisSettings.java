/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

public class SecondaryAxisSettings extends AbstractAxisSettings implements ISecondaryAxisSettings {

	private IAxisScaleConverter axisScaleConverter;

	public SecondaryAxisSettings(String title, IAxisScaleConverter axisScaleConverter) {
		super(title);
		this.axisScaleConverter = axisScaleConverter;
	}

	public SecondaryAxisSettings(String title, String description, IAxisScaleConverter axisScaleConverter) {
		super(title, description);
		this.axisScaleConverter = axisScaleConverter;
	}

	@Override
	public IAxisScaleConverter getAxisScaleConverter() {

		return axisScaleConverter;
	}
}
