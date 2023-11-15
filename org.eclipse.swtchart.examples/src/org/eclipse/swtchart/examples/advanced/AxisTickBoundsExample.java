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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example to get bounds of axis tick.
 */
public class AxisTickBoundsExample {

	private static final double[] ySeries = {0.1, 0.1, 0.2, 0.2, 0.3};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Axis Tick Bounds");
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
	public static Chart createChart(Composite parent) {

		// create a chart
		final Chart chart = new Chart(parent, SWT.NONE);
		chart.getTitle().setText("Axis Tick Bounds");
		chart.getPlotArea().setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		chart.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		// create bar series
		IBarSeries<?> series1 = (IBarSeries<?>)chart.getSeriesSet().createSeries(SeriesType.BAR, "series");
		series1.setYSeries(ySeries);
		// adjust the axis range
		chart.getAxisSet().adjustRange();
		// add mouse move listener to chart
		chart.addMouseMoveListener(e -> {
			for(IAxis axis : chart.getAxisSet().getAxes()) {
				Rectangle r = axis.getTick().getBounds();
				// check if mouse cursor is on axis tick
				if(r.x < e.x && e.x < r.x + r.width && r.y < e.y && e.y < r.y + r.height) {
					// get pixel coordinate on axis tick
					int pixelCoord;
					if(axis.getDirection() == Direction.X) {
						pixelCoord = e.x - r.x;
					} else {
						pixelCoord = e.y - r.y;
					}
					// get data coordinate
					double dataCoord = axis.getDataCoordinate(pixelCoord);
					// show tool-tip
					chart.setToolTipText(String.valueOf(dataCoord));
					return;
				}
			}
			chart.setToolTipText(null);
		});
		return chart;
	}
}
