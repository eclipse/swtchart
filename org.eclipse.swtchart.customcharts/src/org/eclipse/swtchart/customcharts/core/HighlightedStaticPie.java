/*******************************************************************************
 * Copyright (c) 2020 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.customcharts.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.PieChart;

public class HighlightedStaticPie extends PieChart {

	public HighlightedStaticPie(Composite parent) {
		super(parent, SWT.NONE);
	}

	public HighlightedStaticPie(Composite parent, int none) {
		super(parent, none);
	}

	public void addSeriesData(ICircularSeriesData model) {
		ICircularSeriesSettings pieSeriesSettings = (ICircularSeriesSettings) model.getSettings();
		pieSeriesSettings.setRedrawOnClick(false);
		pieSeriesSettings.setBorderColor(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		pieSeriesSettings.setHighlightLineWidth(3);
		super.addSeriesData(model);
	}
}
