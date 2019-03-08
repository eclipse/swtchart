/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
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
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.extensions.TestPathHelper;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.customcharts.PCAChart;
import org.eclipse.swtchart.extensions.images.ImageFactory;
import org.eclipse.swtchart.extensions.menu.SeriesConverter;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesData;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.ScatterSeriesData;

import junit.framework.TestCase;

public class ImageFactory_3_UITest extends TestCase {

	private int SYMBOL_SIZE = 8;

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
			ImageFactory<PCAChart> imageFactory = new ImageFactory<PCAChart>(PCAChart.class, 800, 600);
			/*
			 * Modify the chart.
			 */
			PCAChart pcaChart = imageFactory.getChart();
			BaseChart baseChart = pcaChart.getBaseChart();
			pcaChart.setBackground(baseChart.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			List<ISeriesData> scatterSeriesList = SeriesConverter.getSeriesScatter(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_SCATTER_SERIES_1));
			List<IScatterSeriesData> scatterSeriesDataList = new ArrayList<IScatterSeriesData>();
			//
			for(ISeriesData seriesData : scatterSeriesList) {
				IScatterSeriesData scatterSeriesData = new ScatterSeriesData(seriesData);
				IScatterSeriesSettings scatterSeriesSettings = scatterSeriesData.getSettings();
				/*
				 * Set the color and symbol type.
				 */
				double x = seriesData.getXSeries()[0];
				double y = seriesData.getYSeries()[0];
				scatterSeriesSettings.setSymbolSize(SYMBOL_SIZE);
				//
				if(x > 0 && y > 0) {
					scatterSeriesSettings.setSymbolColor(baseChart.getDisplay().getSystemColor(SWT.COLOR_RED));
					scatterSeriesSettings.setSymbolType(PlotSymbolType.SQUARE);
				} else if(x > 0 && y < 0) {
					scatterSeriesSettings.setSymbolColor(baseChart.getDisplay().getSystemColor(SWT.COLOR_BLUE));
					scatterSeriesSettings.setSymbolType(PlotSymbolType.TRIANGLE);
				} else if(x < 0 && y > 0) {
					scatterSeriesSettings.setSymbolColor(baseChart.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
					scatterSeriesSettings.setSymbolType(PlotSymbolType.DIAMOND);
				} else if(x < 0 && y < 0) {
					scatterSeriesSettings.setSymbolColor(baseChart.getDisplay().getSystemColor(SWT.COLOR_CYAN));
					scatterSeriesSettings.setSymbolType(PlotSymbolType.INVERTED_TRIANGLE);
				} else {
					scatterSeriesSettings.setSymbolColor(baseChart.getDisplay().getSystemColor(SWT.COLOR_GRAY));
					scatterSeriesSettings.setSymbolType(PlotSymbolType.CIRCLE);
				}
				//
				scatterSeriesDataList.add(scatterSeriesData);
			}
			pcaChart.addSeriesData(scatterSeriesDataList);
			/*
			 * Export the images.
			 */
			String exportFolder = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFOLDER_EXPORT);
			String prefix = "ScatterSeries1";
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
