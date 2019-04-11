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
package org.eclipse.swtchart.extensions.exceptions;

public class SeriesException extends RuntimeException {

	/**
	 * Renew this UUID on change.
	 */
	private static final long serialVersionUID = 2625126351854161968L;

	public SeriesException() {
		super();
	}

	public SeriesException(String message) {
		super(message);
	}
}
