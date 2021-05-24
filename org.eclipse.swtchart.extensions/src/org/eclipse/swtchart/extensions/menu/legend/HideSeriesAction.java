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
import org.eclipse.swtchart.extensions.core.SeriesListUI;
import org.eclipse.swtchart.extensions.internal.support.MappingsSupport;
import org.eclipse.swtchart.extensions.internal.support.SeriesLabelProvider;

public class HideSeriesAction extends AbstractMenuListener {

	public HideSeriesAction(SeriesListUI seriesListUI) {

		super(seriesListUI);
	}

	@Override
	public void menuAboutToShow(IMenuManager menuManager) {

		menuManager.add(new Action() {

			@Override
			public String getText() {

				return "Hide Series";
			}

			@Override
			public String getToolTipText() {

				return "Hide the selected series.";
			}

			@Override
			public void run() {

				List<ISeries<?>> selectedSeries = getSelectedSeries();
				for(ISeries<?> series : selectedSeries) {
					MappingsSupport.mapSettings(series, SeriesLabelProvider.VISIBLE, false, getScrollableChart());
				}
				refresh();
			}
		});
	}
}
