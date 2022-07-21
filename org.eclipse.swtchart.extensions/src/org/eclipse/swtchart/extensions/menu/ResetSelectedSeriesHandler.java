/*******************************************************************************
 * Copyright (c) 2017, 2022 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.linecharts.LineChart;

public class ResetSelectedSeriesHandler extends AbstractChartMenuEntry implements IChartMenuEntry {

	@Override
	public String getCategory() {

		return IChartMenuCategories.RANGE_SELECTION;
	}

	@Override
	public String getName() {

		return Messages.getString(Messages.RESET_SELECTED_SERIES);
	}

	@Override
	public Image getIcon() {

		return ResourceSupport.getImage(ResourceSupport.ICON_RESET_SELECTED);
	}

	@Override
	public boolean isEnabled(ScrollableChart scrollableChart) {

		if(scrollableChart instanceof LineChart) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		BaseChart baseChart = scrollableChart.getBaseChart();
		baseChart.resetSeriesSettings();
	}
}
