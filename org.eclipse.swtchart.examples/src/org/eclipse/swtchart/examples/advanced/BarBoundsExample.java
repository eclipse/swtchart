/*******************************************************************************
 * Copyright (c) 2008, 2018 SWTChart project.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.examples.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example to get bounds of bars.
 */
public class BarBoundsExample {

	private static final double[] ySeries1 = {3.0, 2.1, 1.9, 2.3, 3.2};
	private static final double[] ySeries2 = {2.0, 3.1, 0.9, 1.3, 2.2};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Bar Bounds");
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
		final Chart chart = new Chart(parent, SWT.NONE);
		chart.getTitle().setText("Bar Bounds");
		// create bar series
		IBarSeries series1 = (IBarSeries)chart.getSeriesSet().createSeries(SeriesType.BAR, "series 1");
		series1.setYSeries(ySeries1);
		IBarSeries series2 = (IBarSeries)chart.getSeriesSet().createSeries(SeriesType.BAR, "series 2");
		series2.setYSeries(ySeries2);
		series2.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		// adjust the axis range
		chart.getAxisSet().adjustRange();
		// add mouse move listener to open tooltip on data point
		chart.getPlotArea().addMouseMoveListener(new MouseMoveListener() {

			public void mouseMove(MouseEvent e) {

				for(ISeries series : chart.getSeriesSet().getSeries()) {
					Rectangle[] rs = ((IBarSeries)series).getBounds();
					for(int i = 0; i < rs.length; i++) {
						if(rs[i] != null) {
							if(rs[i].x < e.x && e.x < rs[i].x + rs[i].width && rs[i].y < e.y && e.y < rs[i].y + rs[i].height) {
								setToolTipText(series, i);
								return;
							}
						}
					}
				}
				chart.getPlotArea().setToolTipText(null);
			}

			private void setToolTipText(ISeries series, int index) {

				chart.getPlotArea().setToolTipText("Series: " + series.getId() + "\nValue: " + series.getYSeries()[index]);
			}
		});
		return chart;
	}
}