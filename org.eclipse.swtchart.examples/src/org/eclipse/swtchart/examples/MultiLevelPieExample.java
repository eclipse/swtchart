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
package org.eclipse.swtchart.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.internal.series.MultiLevelPie;

/**
 * An example for pie chart.
 */
public class MultiLevelPieExample {

	private static final double[] continentValues = {17212000, 11608000, 9365000, 6880000, 5100000, 3837000, 2968000};
	private static final String[] continentLabels = {"Asia", "Africa", "North America", "South America", "Antarctica", "Europe", "Australia"};
	private static final String[] IndianStatesLabels = {"Maharashtra", "Rajasthan", "Uttar Pradesh", "Madhya Pradesh"};
	private static final double[] IndianStateValues = {742238, 708350, 707713, 54329};
	private static final double[] asianCountriesValues = {3746887, 5083540, 1269010};
	private static final String[] asianCountriesLabels = {"China", "Russia", "India"};
	private static final String[] EuropeCountriesLabels = {"France", "Ukraine", "Germany", "Spain"};
	private static final double[] EuropeanCountriesValues = {212954, 232951, 137846, 195313};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Pie Chart");
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
		// set titles
		chart.getTitle().setText("Landmass Distribution across continents and contries");
		// create pie series
		MultiLevelPie multiLevelPie = (MultiLevelPie)chart.getSeriesSet().createSeries(SeriesType.MULTI_LEVEL_PIE, "countries");
		// sets the series.
		multiLevelPie.addSeries(continentLabels, continentValues);
		multiLevelPie.getNodeById("Asia").addChildren(asianCountriesLabels, asianCountriesValues);
		multiLevelPie.getNodeById("India").addChildren(IndianStatesLabels, IndianStateValues);
		multiLevelPie.getNodeById("Europe").addChildren(EuropeCountriesLabels, EuropeanCountriesValues);
		// ((CompressMultiLevelPie)multiLevelPie.getCompressor()).update();
		// new Node("mea", 45600, multiLevelPie.getNodeById("Goa"));
		// System.out.println(multiLevelPie.getNodeById("Odisha").getValue());
		return chart;
	}
}
