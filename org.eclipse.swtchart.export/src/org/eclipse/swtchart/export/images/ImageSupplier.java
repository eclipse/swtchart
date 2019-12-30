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
import org.eclipse.swtchart.extensions.core.BaseChart;

public class ImageSupplier {

	/**
	 * Save image data representation to a file with specific format (BMP, PNG or JPG)
	 * @param imageData the image data representation
	 * @param fileName the file (relative or full path)
	 * @param format SWT.IMAGE_BMP, SWT.IMAGE_PNG or SWT.IMAGE_JPEG 
	 */
	public void saveImage(ImageData imageData, String fileName, int format) {
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[]{imageData};
		imageLoader.save(fileName, format);
	}

	/**
	 * Return image data representation of provided chart
	 * @param baseChart the chart from which is create image data representation
	 * @return the image data representation
	 */
	public ImageData getImageData(BaseChart baseChart) {
		// Force to redraw chart immediately to be sure that any
		// previous Shell dialog won't be a part of the copied image
		baseChart.redraw();
		baseChart.update();
		// Chart size
		int width = baseChart.getSize().x;
		int height = baseChart.getSize().y;
		// Get image data provider
		ImageDataProvider imageDataProvider = getImageDataProvider(width, height);
		// Copy chart into the image
		Image image = new Image(baseChart.getDisplay(), imageDataProvider);
		GC gc = new GC(baseChart);
		gc.copyArea(image, 0, 0);
		// Retrieve image data
		ImageData imageData = image.getImageData(100);
		// If generated image created from copy area has not same size as chart, scale image data
		if(width != imageData.width || height != imageData.height) 
			imageData = resize(image, width, height);
		// Dispose
		gc.dispose();
		image.dispose();
		return imageData;
	}

	// Create image data provider from provided size
	private ImageDataProvider getImageDataProvider(int width, int height) {
		// Create the image provider
		ImageDataProvider imageDataProvider = new ImageDataProvider() {
			@Override
			public ImageData getImageData(int zoom) {
				if(zoom != 100) return null;
				PaletteData palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
				ImageData imageData = new ImageData(width, height, 24, palette);
				return imageData;
			}
		};
		return imageDataProvider;
	}
	
	// Resize srcImage to width*height
	private ImageData resize(Image srcImage, int width, int height) {
		  Image scaledImage = new Image(srcImage.getDevice(), width, height);
		  GC gc = new GC(scaledImage);
		  gc.drawImage(srcImage, 0, 0, srcImage.getBounds().width, srcImage.getBounds().height, 0, 0, width, height);
		  gc.dispose();
		  return scaledImage.getImageData();
		}

}
