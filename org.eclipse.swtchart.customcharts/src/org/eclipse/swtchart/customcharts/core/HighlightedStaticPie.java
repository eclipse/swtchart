/*******************************************************************************
 * Copyright (c) 2020, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 * Philip Wenig - series settings
 *******************************************************************************/
package org.eclipse.swtchart.customcharts.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.PieChart;

public class HighlightedStaticPie extends PieChart {

	public HighlightedStaticPie(Composite parent) {

		super(parent, SWT.NONE);
		setData("org.eclipse.e4.ui.css.CssClassName", "HighlightedStaticPie");
	}

	public HighlightedStaticPie(Composite parent, int style) {

		super(parent, style);
		setData("org.eclipse.e4.ui.css.CssClassName", "HighlightedStaticPie");
	}

	@Override
	public void addSeriesData(ICircularSeriesData model) {

		ICircularSeriesSettings seriesSettings = model.getSettings();
		seriesSettings.setRedrawOnClick(false);
		seriesSettings.setSliceColor(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		ISeriesSettings seriesSettingsHighlight = seriesSettings.getSeriesSettingsHighlight();
		if(seriesSettingsHighlight instanceof ICircularSeriesSettings) {
			((ICircularSeriesSettings)seriesSettingsHighlight).setBorderWidth(3);
		}
		//
		super.addSeriesData(model);
	}
}