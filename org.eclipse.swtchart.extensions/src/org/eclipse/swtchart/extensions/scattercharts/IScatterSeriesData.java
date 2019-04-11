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
package org.eclipse.swtchart.extensions.scattercharts;

import org.eclipse.swtchart.extensions.core.IChartSeriesData;

public interface IScatterSeriesData extends IChartSeriesData {

	/**
	 * @deprecated use {@link #getSettings()} instead
	 * @return
	 */
	@Deprecated
	IScatterSeriesSettings getScatterSeriesSettings();

	@Override
	default IScatterSeriesSettings getSettings() {

		return getScatterSeriesSettings();
	}
}
