/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 * Christoph Läubrich - adjust to ne PlotArea API
 *******************************************************************************/
package org.eclipse.swtchart.examples.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.ICustomPaintListener;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example to convert data coordinate into pixel coordinate.
 */
public class DataToPixelConversionExample {

	private static final int MARGIN = 5;
	private static final double[] ySeries = {0.0, 0.38, 0.71, 0.92, 1.0, 0.92, 0.71, 0.38, 0.0, -0.38, -0.71, -0.92, -1.0, -0.92, -0.71, -0.38};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Data To Pixel Conversion");
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
		chart.getTitle().setText("Data To Pixel Conversion");
		// get Y axis
		final IAxis yAxis = chart.getAxisSet().getYAxis(0);
		// create line series
		ILineSeries series = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "line series");
		series.setYSeries(ySeries);
		// adjust the axis range
		chart.getAxisSet().adjustRange();
		// add paint listener to draw threshold
		chart.getPlotArea().addCustomPaintListener(new ICustomPaintListener() {

			@Override
			public void paintControl(PaintEvent e) {

				int y = yAxis.getPixelCoordinate(0.65);
				e.gc.drawLine(0, y, e.width, y);
				e.gc.drawText("y=0.65", MARGIN, y + MARGIN);
			}

			@Override
			public boolean drawBehindSeries() {

				return false;
			}
		});
		return chart;
	}
}