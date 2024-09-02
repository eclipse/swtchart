/*******************************************************************************
 * Copyright (c) 2023, 2024 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Philip Wenig - menu demo extension
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts;

import java.text.DecimalFormat;
import java.text.Format;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.Range;

public class Demo_375_MinorLabels {

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(500, 500);
		shell.setLayout(new FillLayout());
		Chart chart = new Chart(shell, SWT.NONE);
		IAxis axis = chart.getAxisSet().getXAxis(0);
		axis.setRange(new Range(10000, 10000.3));
		Format format = new DecimalFormat("#.0");
		axis.getTick().setFormat(format);
		//
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}