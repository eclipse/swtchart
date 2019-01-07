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
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.customcharts.ChromatogramChart;
import org.eclipse.swtchart.extensions.images.ImageFactory;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;
import org.eclipse.swtchart.extensions.menu.SeriesConverter;

import junit.framework.TestCase;

public class ImageFactory_1_UITest extends TestCase {

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

	@SuppressWarnings("unused")
	private void demo() {

		try {
			/*
			 * Create the factory.
			 */
			ImageFactory<ChromatogramChart> imageFactory = new ImageFactory<ChromatogramChart>(ChromatogramChart.class, 800, 600);
			/*
			 * Modify the chart.
			 */
			ChromatogramChart chromatogramChart = imageFactory.getChart();
			chromatogramChart.setBackground(chromatogramChart.getBaseChart().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			List<ILineSeriesData> lineSeriesDataList = new ArrayList<ILineSeriesData>();
			//
			ISeriesData seriesData = SeriesConverter.getSeriesXY(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_LINE_SERIES_1));
			ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
			ILineSeriesSettings lineSerieSettings = lineSeriesData.getLineSeriesSettings();
			lineSerieSettings.setEnableArea(true);
			lineSeriesDataList.add(lineSeriesData);
			chromatogramChart.addSeriesData(lineSeriesDataList);
			/*
			 * Export the images.
			 */
			String exportFolder = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFOLDER_EXPORT);
			String prefix = "LineSeries1";
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
