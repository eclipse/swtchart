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
package org.eclipse.swtchart.extensions.axisconverter;

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
	public MillisecondsToScanNumberConverter(int scanDelay, int scanInterval) throws Exception {
		/*
		 * Validations.
		 */
		if(scanDelay < 0) {
			throw new Exception("The scan delay must be >= 0.");
		}
		//
		if(scanInterval <= 0) {
			throw new Exception("The scan interval must be > 0.");
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
