/*******************************************************************************
 * Copyright (c) 2020, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 * Philip Wenig - series settings edit support
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.parts;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
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
	//
	private boolean doughnut = true;
	private boolean highlightSeries = false;

	@Inject
	public SimplePieChart(Composite parent, boolean doughnut, boolean highlightSeries) {

		super(parent, SWT.NONE);
		try {
			this.doughnut = doughnut;
			this.highlightSeries = highlightSeries;
			initialize();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {

		/*
		 * Create series.
		 */
		ICircularSeriesData circularSeriesData = new CircularSeriesData();
		//
		circularSeriesData.setTitle("World");
		circularSeriesData.setNodeClass("Landmass Name");
		circularSeriesData.setValueClass("Area in square miles");
		//
		circularSeriesData.setSeries(continentLabels, continentValues);
		circularSeriesData.getNodeById("Asia").addChildren(AsianCountriesLabels, AsianCountriesValues);
		circularSeriesData.getNodeById("Africa").addChildren(AfricanCountriesLabels, AfricanCountriesValues);
		circularSeriesData.getNodeById("North America").addChildren(NorthAmericanCountriesLabels, NorthAmericanCountriesValues);
		circularSeriesData.getNodeById("India").addChildren(IndianStatesLabels, IndianStateValues);
		circularSeriesData.getNodeById("Europe").addChild("Germany", 137847);
		//
		ICircularSeriesSettings settings = circularSeriesData.getSettings();
		settings.setDescription("Landmass Distribution");
		settings.setSliceColor(null);
		settings.setBorderWidth(1);
		settings.setBorderStyle(LineStyle.SOLID);
		settings.setSeriesType(doughnut ? SeriesType.DOUGHNUT : SeriesType.PIE);
		//
		if(highlightSeries) {
			settings.setRedrawOnClick(false);
			ISeriesSettings seriesSettingsHighlight = settings.getSeriesSettingsHighlight();
			if(seriesSettingsHighlight instanceof ICircularSeriesSettings) {
				ICircularSeriesSettings settingsHighlight = (ICircularSeriesSettings)seriesSettingsHighlight;
				settingsHighlight.setBorderColor(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));
				settingsHighlight.setBorderWidth(5);
				settingsHighlight.setBorderStyle(LineStyle.DOT);
			}
		}
		/*
		 * Set series.
		 * ICircularSeriesData
		 */
		addSeriesData(circularSeriesData);
	}
}