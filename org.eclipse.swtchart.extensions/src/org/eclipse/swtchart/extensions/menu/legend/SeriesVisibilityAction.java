/*******************************************************************************
 * Copyright (c) 2021, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu.legend;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.SeriesListUI;

public class SeriesVisibilityAction extends AbstractMenuListener {

	private boolean setVisible;
	private boolean inLegend;

	public SeriesVisibilityAction(SeriesListUI seriesListUI, boolean setVisible, boolean inLegend) {

		super(seriesListUI);
		this.setVisible = setVisible;
		this.inLegend = inLegend;
	}

	@Override
	public void menuAboutToShow(IMenuManager menuManager) {

		menuManager.add(new Action() {

			@Override
			public String getText() {

				return setVisible //
						? (inLegend ? Messages.getString(Messages.showSeriesInLegend) : Messages.getString(Messages.showSeries)) //
						: (inLegend ? Messages.getString(Messages.hideSeriesInLegend) : Messages.getString(Messages.hideSeries));
			}

			@Override
			public String getToolTipText() {

				return setVisible //
						? (inLegend ? Messages.getString(Messages.showSelectedSeriesInLegend) : Messages.getString(Messages.showSelectedSeries)) //
						: (inLegend ? Messages.getString(Messages.hideSelectedSeriesInLegend) : Messages.getString(Messages.hideSelectedSeries));
			}

			@Override
			public void run() {

				BaseChart baseChart = getBaseChart();
				//
				List<ISeries<?>> selectedSeries = getSelectedSeries();
				for(ISeries<?> series : selectedSeries) {
					ISeriesSettings seriesSettings = baseChart.getSeriesSettings(series.getId());
					if(inLegend) {
						seriesSettings.setVisibleInLegend(setVisible);
					} else {
						seriesSettings.setVisible(setVisible);
					}
					baseChart.applySeriesSettings(series, seriesSettings, true);
				}
				//
				refresh();
			}
		});
	}
}