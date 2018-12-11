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
package org.eclipse.swtchart.extensions.barcharts;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;

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
}
