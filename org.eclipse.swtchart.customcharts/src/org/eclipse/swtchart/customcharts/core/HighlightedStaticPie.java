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
