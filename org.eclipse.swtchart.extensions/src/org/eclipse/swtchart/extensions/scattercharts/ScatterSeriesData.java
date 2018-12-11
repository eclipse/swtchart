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
