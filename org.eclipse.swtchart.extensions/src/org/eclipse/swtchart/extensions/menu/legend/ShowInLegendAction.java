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
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.MappingsSupport;
import org.eclipse.swtchart.extensions.core.SeriesLabelProvider;
import org.eclipse.swtchart.extensions.core.SeriesListUI;

public class ShowInLegendAction extends AbstractMenuListener {

	public ShowInLegendAction(SeriesListUI seriesListUI) {

		super(seriesListUI);
	}

	@Override
	public void menuAboutToShow(IMenuManager menuManager) {

		menuManager.add(new Action() {

			@Override
			public String getText() {

				return "Show In Legend";
			}

			@Override
			public String getToolTipText() {

				return "Show the selected series in the embedded chart legend.";
			}

			@Override
			public void run() {

				List<ISeries<?>> selectedSeries = getSelectedSeries();
				for(ISeries<?> series : selectedSeries) {
					MappingsSupport.mapSettings(series, SeriesLabelProvider.VISIBLE_IN_LEGEND, true, getScrollableChart());
				}
				refresh();
			}
		});
	}
}
