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
package org.eclipse.swtchart.examples.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.ISeries.SeriesType;

/**
 * An example for MultiLevel pie chart.
 */
public class HighlightMultiLevelPie {

	private static final String[] continentLabels = {"Asia", "Africa", "North America", "South America", "Antarctica", "Europe", "Australia"};
	private static final double[] continentValues = {17212000, 11608000, 9365000, 6880000, 5100000, 3837000, 2968000};
	private static final String[] AsianCountriesLabels = {"China", "Russia", "India"};
	private static final double[] AsianCountriesValues = {3746887, 5083540, 1269219};
	private static final String[] AfricanCountriesLabels = {"Algeria", "Congo"};
	private static final double[] AfricanCountriesValues = {919595, 905355};
	private static final String[] NorthAmericanCountriesLabels = {"Canada", "USA"};
	private static final double[] NorthAmericanCountriesValues = {3900261, 3761363};
	private static final String[] IndianStatesLabels = {"Maharashtra", "Rajasthan", "Uttar Pradesh", "Madhya Pradesh"};
	private static final double[] IndianStateValues = {92320, 213900, 150580, 192718};

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Multi Level Pie Chart");
		shell.setSize(800, 500);
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
		chart.getTitle().setText("Multi Level Pie Chart");
		// create pie series
		// Doughnut multiLevelPie = (Doughnut)chart.getSeriesSet().createSeries(SeriesType.DOUGHNUT, "countries");
		ICircularSeries multiLevelPie = (ICircularSeries)chart.getSeriesSet().createSeries(SeriesType.PIE, "countries");
		// sets the series.
		multiLevelPie.setSeries(continentLabels, continentValues);
		// adding Asian countries. These go in as second level
		multiLevelPie.getNodeById("Asia").addChildren(AsianCountriesLabels, AsianCountriesValues);
		//
		multiLevelPie.getNodeById("Africa").addChildren(AfricanCountriesLabels, AfricanCountriesValues);
		//
		multiLevelPie.getNodeById("North America").addChildren(NorthAmericanCountriesLabels, NorthAmericanCountriesValues);
		/*
		 * Adding Indian states. These go as third level.
		 * Added to show that those too small for 1 degree, are also made visible
		 */
		multiLevelPie.getNodeById("India").addChildren(IndianStatesLabels, IndianStateValues);
		// Another API
		multiLevelPie.getNodeById("Europe").addChild("Germany", 137847);
		// setting the border color
		multiLevelPie.setBorderColor(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		// setting the Highlight Color
		multiLevelPie.setHighlightColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		// highlighting the node
		multiLevelPie.setHighlightedNode(multiLevelPie.getNodeById("Russia"));
		//
		return chart;
	}
}
