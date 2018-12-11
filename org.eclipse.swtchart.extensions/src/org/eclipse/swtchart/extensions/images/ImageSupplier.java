/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.images;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swtchart.extensions.core.BaseChart;

public class ImageSupplier {

	public void saveImage(ImageData imageData, String fileName, int format) {

		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[]{imageData};
		imageLoader.save(fileName, format);
	}

	public ImageData getImageData(BaseChart baseChart) {

		Image image = new Image(baseChart.getDisplay(), baseChart.getBounds());
		GC gc = new GC(image);
		baseChart.print(gc);
		gc.dispose();
		ImageData imageData = image.getImageData();
		image.dispose();
		return imageData;
	}
}
