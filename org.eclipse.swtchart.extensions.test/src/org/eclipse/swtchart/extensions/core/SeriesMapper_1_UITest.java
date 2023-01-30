/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesSettings;

import junit.framework.TestCase;

public class SeriesMapper_1_UITest extends TestCase {

	private ISeriesSettings seriesSettingsBar = new BarSeriesSettings();
	private ISeriesSettings seriesSettingsLine = new LineSeriesSettings();

	@Override
	protected void setUp() throws Exception {

		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		seriesSettingsLine.setDescription("Regex Settings Line");
		seriesSettingsLine.setVisible(false);
		//
		SeriesMapper.clear();
		assertEquals(0, SeriesMapper.getMappings().size());
		SeriesMapper.put(new MappingsKey(MappingsType.BAR, "418_Chromatogram_Bar_MSD"), seriesSettingsBar);
		// SeriesMapper.put(new MappingsKey(MappingsType.LINE, "(418_)(.*)(_Line_MSD)"), seriesSettingsLine);
		assertEquals(1, SeriesMapper.getMappings().size());
		//
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setText("DemoChart");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		/*
		 * Test
		 */
		ScrollableChart scrollableChart = new ScrollableChart(shell, SWT.BORDER);
		BaseChart baseChart = scrollableChart.getBaseChart();
		shell.open();
		//
		String id = "418_Wood_Line_MSD";
		ISeriesData seriesData = new SeriesData(new double[]{50, 100, 50}, id);
		IChartSeriesData chartSeriesData = new LineSeriesData(seriesData);
		scrollableChart.addSeries(chartSeriesData);
		//
		ISeries<?> series = baseChart.getSeriesSet().getSeries(id);
		ISeriesSettings seriesSettings = SeriesMapper.get(series, baseChart);
		assertNull(seriesSettings);
		//
		shell.close();
	}

	public void test2() {

		seriesSettingsLine.setDescription("Regex Settings Line");
		seriesSettingsLine.setVisible(false);
		//
		SeriesMapper.clear();
		assertEquals(0, SeriesMapper.getMappings().size());
		SeriesMapper.put(new MappingsKey(MappingsType.BAR, "418_Chromatogram_Bar_MSD"), seriesSettingsBar);
		SeriesMapper.put(new MappingsKey(MappingsType.LINE, "(418_)(.*)(_Line_MSD)"), seriesSettingsLine);
		assertEquals(2, SeriesMapper.getMappings().size());
		//
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setText("DemoChart");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		/*
		 * Test
		 */
		ScrollableChart scrollableChart = new ScrollableChart(shell, SWT.BORDER);
		BaseChart baseChart = scrollableChart.getBaseChart();
		shell.open();
		//
		String id = "418_Wood_Line_MSD";
		ISeriesData seriesData = new SeriesData(new double[]{50, 100, 50}, id);
		IChartSeriesData chartSeriesData = new LineSeriesData(seriesData);
		scrollableChart.addSeries(chartSeriesData);
		//
		ISeries<?> series = baseChart.getSeriesSet().getSeries(id);
		ISeriesSettings seriesSettings = SeriesMapper.get(series, baseChart);
		assertNotNull(seriesSettings);
		assertEquals("Regex Settings Line", seriesSettings.getDescription());
		assertEquals(false, seriesSettings.isVisible());
		//
		shell.close();
	}
}