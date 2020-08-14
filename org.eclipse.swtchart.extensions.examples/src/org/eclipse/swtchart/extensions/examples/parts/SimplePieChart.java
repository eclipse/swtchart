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
 * Himanshu Balasamanta Orignal API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.parts;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.extensions.piecharts.CircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesData;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.PieChart;

public class SimplePieChart extends PieChart {

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

	@Inject
	public SimplePieChart(Composite parent) {

		super(parent, SWT.NONE);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		try {
			initialize();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	private void initialize() throws Exception {

		/*
		 * Create series.
		 */
		ICircularSeriesData multiLevelDoughnut = new CircularSeriesData();
		//
		multiLevelDoughnut.setTitle("World");
		multiLevelDoughnut.setNodeClass("Landmass Name");
		multiLevelDoughnut.setValueClass("Area in sq miles");
		//
		multiLevelDoughnut.setSeries(continentLabels, continentValues);
		// adding Asian countries. These go in as second level
		multiLevelDoughnut.getNodeById("Asia").addChildren(AsianCountriesLabels, AsianCountriesValues);
		//
		multiLevelDoughnut.getNodeById("Africa").addChildren(AfricanCountriesLabels, AfricanCountriesValues);
		//
		multiLevelDoughnut.getNodeById("North America").addChildren(NorthAmericanCountriesLabels, NorthAmericanCountriesValues);
		/*
		 * Adding Indian states. These go as third level.
		 * Added to show that those too small for 1 degree, are also made visible
		 */
		multiLevelDoughnut.getNodeById("India").addChildren(IndianStatesLabels, IndianStateValues);
		// Another API
		multiLevelDoughnut.getNodeById("Europe").addChild("Germany", 137847);
		//
		//
		ICircularSeriesSettings settings = multiLevelDoughnut.getSettings();
		settings.setDescription("Landmass Distribultion");
		settings.setBorderStyle(SWT.LINE_SOLID);
		//
		multiLevelDoughnut.getSettings().setSeriesType(SeriesType.DOUGHNUT);
		/*
		 * Set series.
		 * ICircularSeriesData
		 */
		addSeriesData(multiLevelDoughnut);
	}
}
