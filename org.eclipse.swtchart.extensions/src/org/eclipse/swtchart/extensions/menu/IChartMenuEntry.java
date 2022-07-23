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
 * Christoph Läubrich - extend for tooltip
 * Matthias Mailänder - add optional icon
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public interface IChartMenuEntry {

	String getCategory();

	String getName();

	default String getToolTipText() {

		return "";
	}
	
	default Image getIcon() {

		return null;
	}

	default boolean isEnabled(ScrollableChart scrollableChart) {

		return true;
	}

	void execute(Shell shell, ScrollableChart scrollableChart);
}
