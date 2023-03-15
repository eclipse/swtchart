/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.export.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swtchart.customcharts.core.ChromatogramChart;
import org.eclipse.swtchart.export.PathResolver;
import org.eclipse.swtchart.export.SeriesConverter;
import org.eclipse.swtchart.export.TestPathHelper;
import org.eclipse.swtchart.export.images.ImageFactory;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

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

	public void test2() {

		try {
			/*
			 * Create the factory.
			 */
			ImageFactory<ChromatogramChart> imageFactory = new ImageFactory<>(ChromatogramChart.class, 800, 600);
			/*
			 * Modify the chart.
			 */
			ChromatogramChart chromatogramChart = imageFactory.getChart();
			chromatogramChart.setBackground(chromatogramChart.getBaseChart().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			List<ILineSeriesData> lineSeriesDataList = new ArrayList<>();
			//
			ISeriesData seriesData = SeriesConverter.getSeriesXY(PathResolver.getAbsolutePath(TestPathHelper.TESTFILE_LINE_SERIES_1));
			ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
			ILineSeriesSettings lineSerieSettings = lineSeriesData.getSettings();
			lineSerieSettings.setEnableArea(true);
			lineSeriesDataList.add(lineSeriesData);
			chromatogramChart.addSeriesData(lineSeriesDataList);
			/*
			 * Export the images.
			 */
			String exportFolder = PathResolver.getAbsolutePath(TestPathHelper.TESTFOLDER_EXPORT);
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
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
