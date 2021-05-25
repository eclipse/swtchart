/*******************************************************************************
 * Copyright (c) 2017, 2021 Lablicate GmbH.
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
	public ISeriesSettings makeDeepCopy() {

		IScatterSeriesSettings scatterSeriesSettings = new ScatterSeriesSettings();
		transfer(scatterSeriesSettings);
		return scatterSeriesSettings;
	}

	@Override
	public boolean transfer(ISeriesSettings seriesSettingsSink) {

		boolean success = false;
		if(seriesSettingsSink instanceof IScatterSeriesSettings) {
			IScatterSeriesSettings source = this;
			IScatterSeriesSettings sink = (IScatterSeriesSettings)seriesSettingsSink;
			sink.setDescription(source.getDescription());
			sink.setVisible(source.isVisible());
			sink.setVisibleInLegend(source.isVisibleInLegend());
			sink.setSymbolType(source.getSymbolType());
			sink.setSymbolSize(source.getSymbolSize());
			sink.setSymbolColor(source.getSymbolColor());
			success = true;
		}
		//
		return success;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		IScatterSeriesSettings scatterSeriesSettings = new ScatterSeriesSettings();
		transfer(scatterSeriesSettings);
		return scatterSeriesSettings;
	}
}
