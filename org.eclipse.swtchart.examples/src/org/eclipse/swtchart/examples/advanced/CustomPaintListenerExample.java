/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.examples.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.ICustomPaintListener;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example for custom paint listener.
 */
public class CustomPaintListenerExample {

	private static final double[] ySeries = {0.1, 0.38, 0.41, 0.92, 1.0};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Custom Paint Listener");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		createChart(shell);
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * create the chart.
	 * 
	 * @param parent
	 *            The parent composite
	 * @return The created chart
	 */
	static public Chart createChart(Composite parent) {

		// create a chart
		Chart chart = new Chart(parent, SWT.NONE);
		chart.getTitle().setText("Custom Paint Listener");
		chart.getPlotArea().setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		chart.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		ISeries<?> lineSeries = chart.getSeriesSet().createSeries(SeriesType.LINE, "line series");
		lineSeries.setYSeries(ySeries);
		// add paint listeners
		IPlotArea plotArea = (IPlotArea)chart.getPlotArea();
		plotArea.addCustomPaintListener(new FrontPaintListener());
		plotArea.addCustomPaintListener(new BehindPaintListener());
		// adjust the axis range
		chart.getAxisSet().adjustRange();
		return chart;
	}

	static class FrontPaintListener implements ICustomPaintListener {

		@Override
		public void paintControl(PaintEvent e) {

			e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));
			e.gc.fillRectangle(0, e.height / 2, e.width, 20);
		}

		@Override
		public boolean drawBehindSeries() {

			return false;
		}
	}

	static class BehindPaintListener implements ICustomPaintListener {

		@Override
		public void paintControl(PaintEvent e) {

			e.gc.fillGradientRectangle(e.x, e.y, e.width, e.height, true);
		}

		@Override
		public boolean drawBehindSeries() {

			return true;
		}
	}
}
