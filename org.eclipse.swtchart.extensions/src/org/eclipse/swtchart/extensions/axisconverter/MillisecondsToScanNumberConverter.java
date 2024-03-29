/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.axisconverter;

import java.security.InvalidParameterException;

import org.eclipse.swtchart.extensions.core.AbstractAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;

public class MillisecondsToScanNumberConverter extends AbstractAxisScaleConverter implements IAxisScaleConverter {

	private int scanDelay;
	private int scanInterval;

	/**
	 * Set the parameters to convert to the data.
	 * 
	 * @param scanDelay
	 * @param scanInterval
	 */
	public MillisecondsToScanNumberConverter(int scanDelay, int scanInterval) throws InvalidParameterException {

		/*
		 * Validations.
		 */
		if(scanDelay < 0) {
			throw new InvalidParameterException(Messages.getString(Messages.SCAN_MUST_BE_GE_0_KEY));
		}
		//
		if(scanInterval <= 0) {
			throw new InvalidParameterException(Messages.getString(Messages.SCAN_MUST_BE_G_0_KEY));
		}
		//
		this.scanDelay = scanDelay;
		this.scanInterval = scanInterval;
	}

	@Override
	public double convertToSecondaryUnit(double primaryValue) {

		/*
		 * Milliseconds -> Scan Number
		 */
		if(primaryValue < scanDelay) {
			return 0;
		} else {
			return (int)((primaryValue - scanDelay) / scanInterval) + 1;
		}
	}

	@Override
	public double convertToPrimaryUnit(double secondaryValue) {

		/*
		 * Scan Number -> Milliseconds
		 */
		if(secondaryValue < 1.0d) {
			return 0;
		} else {
			return scanDelay + (int)(secondaryValue - 1) * scanInterval;
		}
	}
}
