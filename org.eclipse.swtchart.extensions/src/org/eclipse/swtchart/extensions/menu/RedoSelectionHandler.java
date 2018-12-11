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
package org.eclipse.swtchart.extensions.menu;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public class RedoSelectionHandler extends AbstractChartMenuEntry implements IChartMenuEntry {

	public static final String NAME = "Redo Selection";

	@Override
	public String getCategory() {

		return IChartMenuCategories.RANGE_SELECTION;
	}

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		scrollableChart.getBaseChart().redoSelection();
		scrollableChart.redraw();
	}
}
