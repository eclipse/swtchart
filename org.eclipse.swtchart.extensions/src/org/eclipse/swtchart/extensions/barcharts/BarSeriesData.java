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
package org.eclipse.swtchart.extensions.barcharts;

import org.eclipse.swtchart.extensions.core.AbstractChartSeriesData;
import org.eclipse.swtchart.extensions.core.ISeriesData;

public class BarSeriesData extends AbstractChartSeriesData implements IBarSeriesData {

	private IBarSeriesSettings barSeriesSettings;

	public BarSeriesData(ISeriesData seriesData) {
		super(seriesData);
		this.barSeriesSettings = new BarSeriesSettings();
		/*
		 * Set the default description.
		 */
		this.barSeriesSettings.setDescription(seriesData.getId());
	}

	@Override
	public IBarSeriesSettings getBarSeriesSettings() {

		return barSeriesSettings;
	}
}
