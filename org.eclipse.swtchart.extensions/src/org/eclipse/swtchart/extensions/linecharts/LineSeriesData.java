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
package org.eclipse.swtchart.extensions.linecharts;

import org.eclipse.swtchart.extensions.core.AbstractChartSeriesData;
import org.eclipse.swtchart.extensions.core.ISeriesData;

public class LineSeriesData extends AbstractChartSeriesData implements ILineSeriesData {

	private ILineSeriesSettings lineSeriesSettings;

	public LineSeriesData(ISeriesData seriesData) {
		super(seriesData);
		this.lineSeriesSettings = new LineSeriesSettings();
		/*
		 * Set the default description.
		 */
		this.lineSeriesSettings.setDescription(seriesData.getId());
	}

	@Override
	public ILineSeriesSettings getLineSeriesSettings() {

		return lineSeriesSettings;
	}
}
