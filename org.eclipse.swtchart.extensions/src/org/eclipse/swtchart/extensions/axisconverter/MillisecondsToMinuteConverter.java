/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
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

public class MillisecondsToMinuteConverter extends AbstractAxisScaleConverter implements IAxisScaleConverter {

	private static final double MINUTE_CORRELATION_FACTOR = 60000.0d;

	@Override
	public double convertToSecondaryUnit(double primaryValue) {

		return primaryValue / MINUTE_CORRELATION_FACTOR;
	}

	@Override
	public double convertToPrimaryUnit(double secondaryValue) {

		return secondaryValue * MINUTE_CORRELATION_FACTOR;
	}
}
