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
package org.eclipse.swtchart.extensions.images;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public class ImageFactory<T extends ScrollableChart> {

	private T t;
	private ImageSupplier imageSupplier;

	/**
	 * The width and height of the current display is allowed.
	 * If width or height are greater, then the display bounds are used.
	 * 
	 * @param width
	 * @param height
	 * @return Shell
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ImageFactory(Class<T> clazz, int width, int height) throws InstantiationException, IllegalAccessException {
		//
		t = clazz.newInstance();
		imageSupplier = new ImageSupplier();
		//
		Display display = ((ScrollableChart)t).getBaseChart().getDisplay();
		if(display != null) {
			width = (width > display.getBounds().width) ? display.getBounds().width : width;
			height = (height > display.getBounds().height) ? display.getBounds().height : height;
			t.getShell().setSize(width, height);
		}
	}

	public T getChart() {

		return t;
	}

	public void closeShell() {

		t.getShell().close();
	}

	/**
	 * Returns the image data of the chart.
	 * Don't forget to call closeShell() if the shell is not needed anymore.
	 * 
	 * @return ImageData.
	 */
	public ImageData getImageData() {

		t.getShell().open();
		return imageSupplier.getImageData(t.getBaseChart());
	}

	/**
	 * Path to the file and the format.
	 * Don't forget to call closeShell() if the shell is not needed anymore.
	 * e.g.: SWT.IMAGE_PNG
	 * 
	 * @param fileName
	 * @param format
	 */
	public void saveImage(String fileName, int format) {

		ImageData imageData = getImageData();
		imageSupplier.saveImage(imageData, fileName, format);
	}
}
