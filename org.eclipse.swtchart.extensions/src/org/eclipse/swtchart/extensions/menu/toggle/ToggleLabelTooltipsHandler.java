/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu.toggle;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.menu.AbstractChartMenuEntry;
import org.eclipse.swtchart.extensions.menu.IChartMenuCategories;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class ToggleLabelTooltipsHandler extends AbstractChartMenuEntry implements IChartMenuEntry {

	public static final String NAME = "Show Tooltips";

	@Override
	public String getCategory() {

		return IChartMenuCategories.TOGGLE_VISIBILITY;
	}

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		scrollableChart.getChartSettings().setEnableTooltips(!scrollableChart.getChartSettings().isEnableTooltips());
	}
}
