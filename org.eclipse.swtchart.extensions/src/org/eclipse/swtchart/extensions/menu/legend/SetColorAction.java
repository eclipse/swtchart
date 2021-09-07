/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
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
import org.eclipse.swtchart.extensions.core.MappingsSupport;
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

				return "Set Color";
			}

			@Override
			public String getToolTipText() {

				return "Adjust the color of the selected series.";
			}

			@Override
			public void run() {

				SeriesListUI seriesListUI = getSeriesListUI();
				Table table = seriesListUI.getTable();
				List<ISeries<?>> selectedSeries = getSelectedSeries();
				//
				if(selectedSeries.size() > 0) {
					Color colorActive = SeriesLabelProvider.getColor(selectedSeries.get(0));
					RGB rgbActive = (colorActive == null) ? new RGB(255, 0, 0) : colorActive.getRGB();
					ColorDialog colorDialog = new ColorDialog(table.getShell());
					colorDialog.setText("Set Series Color");
					colorDialog.setRGB(rgbActive);
					RGB rgbNew = colorDialog.open();
					if(rgbNew != null) {
						for(ISeries<?> series : selectedSeries) {
							MappingsSupport.mapSettings(series, SeriesLabelProvider.COLOR, rgbNew, getScrollableChart());
						}
						refresh();
					}
				}
			}
		});
	}
}
