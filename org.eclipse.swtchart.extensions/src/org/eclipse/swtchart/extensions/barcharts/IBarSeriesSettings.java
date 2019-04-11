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
package org.eclipse.swtchart.extensions.barcharts;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;

public interface IBarSeriesSettings extends ISeriesSettings {

	Color getBarColor();

	void setBarColor(Color barColor);

	int getBarPadding();

	void setBarPadding(int barPadding);

	int getBarWidth();

	void setBarWidth(int barWidth);

	BarWidthStyle getBarWidthStyle();

	/**
	 * BarWidthStyle.FIXED
	 * BarWidthStyle.STRETCHED
	 * 
	 * @param barWidthStyle
	 */
	void setBarWidthStyle(BarWidthStyle barWidthStyle);

	boolean isBarOverlay();

	void setBarOverlay(boolean barOverlay);

	boolean isEnableStack();

	void setEnableStack(boolean enableStack);
}
