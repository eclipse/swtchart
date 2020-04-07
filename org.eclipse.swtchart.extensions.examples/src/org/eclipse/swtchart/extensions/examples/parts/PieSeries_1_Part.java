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
 * Yash Bharatiya - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.parts;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.extensions.piecharts.PieChart;

public class PieSeries_1_Part extends PieChart {

	@Inject
	public PieSeries_1_Part(Composite parent) {

		super(parent, SWT.NONE);
		// setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//
		try {
			initialize();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	private void initialize() throws Exception {

		double[][] values = {{337309, 131646, 128948}, {100123, 81708, 70478, 58226}, {47806, 4067, 265783}};
		String[] labels = {"Series-A", "Series-B", "Series-C"};
		// setting the color list
		this.getTitle().setText("SAMPLE_DATA");
		addPieChartSeries(labels, values);
	}
}
