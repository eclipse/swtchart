/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.axisconverter;

import org.eclipse.swtchart.extensions.core.AbstractAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;

public class MillisecondsToSecondsConverter extends AbstractAxisScaleConverter implements IAxisScaleConverter {

	private static final double SECOND_CORRELATION_FACTOR = 1000.0d;

	@Override
	public double convertToSecondaryUnit(double primaryValue) {

		return primaryValue / SECOND_CORRELATION_FACTOR;
	}

	@Override
	public double convertToPrimaryUnit(double secondaryValue) {

		return secondaryValue * SECOND_CORRELATION_FACTOR;
	}
}
