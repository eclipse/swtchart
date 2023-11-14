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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.Resources;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.SeriesLabelProvider;
import org.eclipse.swtchart.extensions.core.SeriesListUI;

public class SetColorAction extends AbstractMenuListener {

	public SetColorAction(SeriesListUI seriesListUI) {

		super(seriesListUI);
	}

	@Override
	public void menuAboutToShow(IMenuManager menuManager) {

		menuManager.add(new Action() {

			@Override
			public String getText() {

				return Messages.getString(Messages.setColor);
			}

			@Override
			public String getToolTipText() {

				return Messages.getString(Messages.adjustColorOfSelectedSeries);
			}

			@Override
			public void run() {

				BaseChart baseChart = getBaseChart();
				SeriesListUI seriesListUI = getSeriesListUI();
				Table table = seriesListUI.getTable();
				List<ISeries<?>> selectedSeries = getSelectedSeries();
				//
				if(!selectedSeries.isEmpty()) {
					ColorDialog colorDialog = new ColorDialog(table.getShell());
					colorDialog.setText(Messages.getString(Messages.setSeriesColor));
					RGB rgbNew = colorDialog.open();
					if(rgbNew != null) {
						for(ISeries<?> series : selectedSeries) {
							ISeriesSettings seriesSettings = baseChart.getSeriesSettings(series.getId());
							Color color = Resources.getColor(rgbNew);
							SeriesLabelProvider.setColor(seriesSettings, color);
							baseChart.applySeriesSettings(series, seriesSettings, true);
						}
						refresh();
					}
				}
			}
		});
	}
}