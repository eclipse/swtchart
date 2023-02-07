/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.examples.menu;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.menu.AbstractChartMenuEntry;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;
import org.eclipse.swtchart.extensions.menu.Messages;

public class ZoomOutHandler extends AbstractChartMenuEntry implements IChartMenuEntry {

	@Override
	public String getCategory() {

		return Messages.RANGE_SELECTION;
	}

	@Override
	public String getName() {

		return Messages.ZOOM_OUT;
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		scrollableChart.zoomOut();
	}
}
