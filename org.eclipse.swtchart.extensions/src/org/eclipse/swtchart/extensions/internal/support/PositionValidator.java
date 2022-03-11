/*******************************************************************************
 * Copyright (c) 2020, 2022 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.internal.support;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class PositionValidator implements IValidator<String> {

	private static final String ERROR = "Please enter a position as integer, e.g.: 100.";
	//
	private int position = 0;

	public IStatus validate(String value) {

		this.position = 0;
		boolean parseError = true;
		//
		if(value != null) {
			try {
				position = Integer.parseInt(((String)value).trim());
				parseError = false;
			} catch(NumberFormatException e) {
				// Don't catch it here.
			}
		}
		//
		if(parseError) {
			return ValidationStatus.error(ERROR);
		} else {
			return ValidationStatus.ok();
		}
	}

	public int getPosition() {

		return position;
	}
}
