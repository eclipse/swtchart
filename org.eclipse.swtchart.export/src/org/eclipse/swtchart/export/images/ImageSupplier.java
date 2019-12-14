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
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
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
		/*
		 * Under macOS, the image is flipped.
		 */
		if(isMacOS()) {
			Transform transform = new Transform(gc.getDevice());
			transform.rotate(-180);
			Rectangle bounds = image.getBounds();
			transform.translate(-bounds.width, -bounds.height);
			gc.setTransform(transform);
			baseChart.print(gc);
			transform.dispose();
		} else {
			baseChart.print(gc);
		}
		//
		gc.dispose();
		ImageData imageData = image.getImageData();
		image.dispose();
		return imageData;
	}

	private boolean isMacOS() {

		return (getOperatingSystem().indexOf("mac") >= 0);
	}

	private String getOperatingSystem() {

		return System.getProperty("os.name").toLowerCase();
	}
}
