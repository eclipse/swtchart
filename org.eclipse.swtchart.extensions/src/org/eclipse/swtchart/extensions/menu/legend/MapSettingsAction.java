/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
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
import org.eclipse.swtchart.extensions.core.SeriesListUI;
import org.eclipse.swtchart.extensions.core.SeriesMapper;

public class MapSettingsAction extends AbstractMenuListener {

	private boolean addMapping = true;

	public MapSettingsAction(SeriesListUI seriesListUI, boolean addMapping) {

		super(seriesListUI);
		this.addMapping = addMapping;
	}

	@Override
	public void menuAboutToShow(IMenuManager menuManager) {

		menuManager.add(new Action() {

			@Override
			public String getText() {

				return (addMapping ? "Add" : "Remove") + " series mapping";
			}

			@Override
			public String getToolTipText() {

				return "Add/Remove the series settings mapping of the selected entries.";
			}

			@Override
			public void run() {

				BaseChart baseChart = getBaseChart();
				List<ISeries<?>> selectedSeries = getSelectedSeries();
				for(ISeries<?> series : selectedSeries) {
					if(addMapping) {
						SeriesMapper.map(series, baseChart);
					} else {
						SeriesMapper.unmap(series, baseChart);
					}
				}
				//
				refresh();
			}
		});
	}
}