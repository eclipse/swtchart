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

import org.eclipse.swtchart.extensions.core.AbstractPointSeriesSettings;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;

public class ScatterSeriesSettings extends AbstractPointSeriesSettings implements IScatterSeriesSettings {

	private IScatterSeriesSettings seriesSettingsHighlight = null;

	@Override
	public ISeriesSettings getSeriesSettingsHighlight() {

		if(seriesSettingsHighlight == null) {
			try {
				seriesSettingsHighlight = (IScatterSeriesSettings)this.clone();
			} catch(CloneNotSupportedException e) {
				seriesSettingsHighlight = new ScatterSeriesSettings();
			}
		}
		return seriesSettingsHighlight;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		IScatterSeriesSettings scatterSeriesSettings = new ScatterSeriesSettings();
		scatterSeriesSettings.setDescription(this.getDescription());
		scatterSeriesSettings.setVisible(this.isVisible());
		scatterSeriesSettings.setVisibleInLegend(this.isVisibleInLegend());
		scatterSeriesSettings.setSymbolType(this.getSymbolType());
		scatterSeriesSettings.setSymbolSize(this.getSymbolSize());
		scatterSeriesSettings.setSymbolColor(this.getSymbolColor());
		return scatterSeriesSettings;
	}
}
