/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.export.images;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtchart.extensions.core.BaseChart;

public class ImageSupplier {

	// Image data provider used locally to represent chart image
	private class ChartImageDataProvider implements ImageDataProvider {

		private ImageData imageData;

		public ChartImageDataProvider(int width, int height) {

			PaletteData palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
			imageData = new ImageData(width, height, 32, palette);
		}

		@Override
		public ImageData getImageData(int zoom) {

			if(zoom != 100)
				return null;
			return imageData;
		}
	}

	/**
	 * Save image data representation to a file with specific format (BMP, PNG or JPG)
	 * 
	 * @param imageData
	 *            the image data representation
	 * @param fileName
	 *            the file (relative or full path)
	 * @param format
	 *            SWT.IMAGE_BMP, SWT.IMAGE_PNG or SWT.IMAGE_JPEG
	 */
	public void saveImage(ImageData imageData, String fileName, int format) {

		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[]{imageData};
		imageLoader.save(fileName, format);
	}

	/**
	 * Return image data representation of provided chart
	 * 
	 * @param baseChart
	 *            the chart from which is create image data representation
	 * @return image data representation
	 */
	public ImageData getImageData(BaseChart baseChart) {

		// Force to redraw chart immediately to be sure that any
		// previous Shell dialog won't be a part of the copied image
		baseChart.redraw();
		baseChart.update();
		// Chart size
		Point baseChartSize = baseChart.getSize();
		// Create the image provider
		ChartImageDataProvider chartImageDataProvider = new ChartImageDataProvider(baseChartSize.x, baseChartSize.y);
		// Copy chart into the image
		Image image = new Image(baseChart.getDisplay(), chartImageDataProvider);
		GC gc = new GC(baseChart);
		gc.copyArea(image, 0, 0);
		// Retrieve image data
		ImageData imageData = image.getImageData();
		gc.dispose();
		image.dispose();
		return imageData;
	}
}
