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
package org.eclipse.swtchart.extensions.examples.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.extensions.examples.support.SeriesConverter;

public class CustomLinkedLineSeries1 extends Composite {

	private CustomLineChart1 sampleChart;
	private CustomLineChart1 referenceChart;

	public CustomLinkedLineSeries1(Composite parent, int style) {
		super(parent, style);
		createControl();
	}

	public void setChartInfo(String trace, String sample, String reference) {

		ITitle title;
		//
		title = sampleChart.getBaseChart().getTitle();
		title.setText(sample + " (" + trace + ")");
		title.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		//
		title = referenceChart.getBaseChart().getTitle();
		title.setText(reference + " (" + trace + ")");
		title.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		sampleChart = new CustomLineChart1(this, SWT.NONE, true, false, false, SeriesConverter.LINE_SERIES_4_3);
		sampleChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		referenceChart = new CustomLineChart1(this, SWT.NONE, false, true, true, SeriesConverter.LINE_SERIES_4_4);
		referenceChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		/*
		 * Link both charts.
		 */
		sampleChart.addLinkedScrollableChart(referenceChart);
		referenceChart.addLinkedScrollableChart(sampleChart);
	}
}
