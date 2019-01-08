/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu.export;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swtchart.extensions.TestPathHelper;
import org.eclipse.swtchart.extensions.barcharts.BarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesData;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.customcharts.MassSpectrumChart;
import org.eclipse.swtchart.extensions.images.ImageFactory;
import org.eclipse.swtchart.extensions.menu.SeriesConverter;
import org.junit.Ignore;

import junit.framework.TestCase;

public class ImageFactory_2_UITest extends TestCase {

	@Override
	protected void setUp() throws Exception {

		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertTrue("UI tests can't be executed on a headless build server.", true);
	}

	@Ignore
	public void test2() {

		try {
			/*
			 * Create the factory.
			 */
			ImageFactory<MassSpectrumChart> imageFactory = new ImageFactory<MassSpectrumChart>(MassSpectrumChart.class, 800, 600);
			/*
			 * Modify the chart.
			 */
			MassSpectrumChart massSpectrumChart = imageFactory.getChart();
			massSpectrumChart.setBackground(massSpectrumChart.getBaseChart().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			List<IBarSeriesData> barSeriesDataList = new ArrayList<IBarSeriesData>();
			ISeriesData seriesData = SeriesConverter.getSeriesXY(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_BAR_SERIES_1));
			//
			IBarSeriesData barSeriesData = new BarSeriesData(seriesData);
			IBarSeriesSettings barSeriesSettings = barSeriesData.getBarSeriesSettings();
			barSeriesSettings.setDescription("");
			barSeriesDataList.add(barSeriesData);
			massSpectrumChart.addSeriesData(barSeriesDataList);
			/*
			 * Export the images.
			 */
			String exportFolder = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFOLDER_EXPORT);
			String prefix = "BarSeries1";
			//
			String png = exportFolder + File.separator + prefix + ".png";
			imageFactory.saveImage(png, SWT.IMAGE_PNG);
			File filePng = new File(png);
			assertTrue(filePng.exists());
			filePng.delete();
			//
			String jpg = exportFolder + File.separator + prefix + ".jpg";
			imageFactory.saveImage(jpg, SWT.IMAGE_JPEG);
			File fileJpg = new File(jpg);
			assertTrue(fileJpg.exists());
			fileJpg.delete();
			//
			String bmp = exportFolder + File.separator + prefix + ".bmp";
			imageFactory.saveImage(bmp, SWT.IMAGE_BMP);
			File fileBmp = new File(bmp);
			assertTrue(fileBmp.exists());
			fileBmp.delete();
			//
			imageFactory.closeShell();
			//
		} catch(InstantiationException e) {
			System.out.println(e);
		} catch(IllegalAccessException e) {
			System.out.println(e);
		}
	}
}
