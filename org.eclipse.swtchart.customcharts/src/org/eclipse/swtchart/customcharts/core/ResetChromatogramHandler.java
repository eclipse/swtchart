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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.customcharts.core;

import org.eclipse.swtchart.extensions.menu.ResetChartHandler;

public class ResetChromatogramHandler extends ResetChartHandler {

	@Override
	public String getName() {

		return Messages.getString(Messages.RESET_CHROMATOGRAM);
	}
}
