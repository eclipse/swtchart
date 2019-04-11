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
