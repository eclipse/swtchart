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
