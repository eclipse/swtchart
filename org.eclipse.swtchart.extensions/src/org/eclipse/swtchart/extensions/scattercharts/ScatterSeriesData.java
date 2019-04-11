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

import org.eclipse.swtchart.extensions.core.AbstractChartSeriesData;
import org.eclipse.swtchart.extensions.core.ISeriesData;

public class ScatterSeriesData extends AbstractChartSeriesData implements IScatterSeriesData {

	private IScatterSeriesSettings scatterSeriesSettings;

	public ScatterSeriesData(ISeriesData seriesData) {
		super(seriesData);
		this.scatterSeriesSettings = new ScatterSeriesSettings();
		/*
		 * Set the default description.
		 */
		this.scatterSeriesSettings.setDescription(seriesData.getId());
	}

	@Override
	public IScatterSeriesSettings getScatterSeriesSettings() {

		return scatterSeriesSettings;
	}
}
