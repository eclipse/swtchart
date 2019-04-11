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
package org.eclipse.swtchart.extensions.menu.toggle;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.menu.AbstractChartMenuEntry;
import org.eclipse.swtchart.extensions.menu.IChartMenuCategories;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class ToggleRangeSelectorHandler extends AbstractChartMenuEntry implements IChartMenuEntry {

	@Override
	public String getCategory() {

		return IChartMenuCategories.TOGGLE_VISIBILITY;
	}

	@Override
	public String getName() {

		return "Range Selector";
	}

	@Override
	public boolean isEnabled(ScrollableChart scrollableChart) {

		IChartSettings chartSettings = scrollableChart.getChartSettings();
		if(chartSettings.isEnableRangeSelector()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		scrollableChart.toggleRangeSelectorVisibility();
	}
}
