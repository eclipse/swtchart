/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 * Christoph Läubrich - extend API so it is reusable
 * Philip Wenig - added the background image option
 *******************************************************************************/
package org.eclipse.swtchart;

import java.util.List;

import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * The plot area.
 */
public interface IPlotArea {

	/**
	 * 
	 * @return the chart this plot area belongs to
	 */
	Chart getChart();

	/**
	 * Adds the custom paint listener.
	 *
	 * @param listener
	 *            the custom paint listener
	 */
	void addCustomPaintListener(ICustomPaintListener listener);

	/**
	 * Removes the custom paint listener
	 *
	 * @param listener
	 *            the custom paint listener
	 */
	void removeCustomPaintListener(ICustomPaintListener listener);

	/**
	 * Returns the registered custom paint listeners as an
	 * unmodifiable list.
	 * 
	 * @return {@link List}
	 */
	List<ICustomPaintListener> getCustomPaintListener();

	/**
	 * 
	 * @return the current Background color
	 */
	Color getBackground();

	/**
	 * set the color of the plot area
	 * 
	 * @param color
	 */
	void setBackground(Color color);

	/**
	 * Returns if the buffer is currently active.
	 * 
	 * @return boolean
	 */
	boolean isBuffered();

	/**
	 * Set the buffer status. Normally, the background image is set
	 * as part of the buffer process. But it's not possible to
	 * return the status dependent on the availability of the
	 * background image as one could also set a background image
	 * manually.
	 * 
	 * @param buffered
	 */
	void setBuffered(boolean buffered);

	/**
	 * When buffering the chart to speed up performance, an image of
	 * the chart is created on the fly and set as the background.
	 * Images must be disposed, but the image can't be disposed when
	 * setting the background image, as it would lead to a blank chart.
	 * Hence,the image is temporarily stored in the plotArea.getControl()
	 * data section. When releasing the user selection, the image shall
	 * be disposed.
	 */
	String KEY_BUFFERED_BACKGROUND_IMAGE = "BUFFERED_BACKGROUND_IMAGE";

	/**
	 * Draws the image centered in the plot area.
	 * 
	 * @param image
	 */
	void setBackgroundImage(Image image);

	/**
	 * Returns the image data of the plot area.
	 * 
	 * @return {@link ImageData}
	 */
	ImageData getImageData();

	/**
	 * Returns a point describing the size in points.
	 * <ul>
	 * <li>The x coordinate is the width.</li>
	 * <li>The y coordinate is the height.</li>
	 * </ul>
	 * 
	 * @return the current size of this area
	 */
	Point getSize();

	/**
	 * 
	 * @return the control that represents thsi plot area
	 */
	Control getControl();

	/**
	 * Returns the rectangle.
	 * 
	 * @return {@link Rectangle}
	 */
	Rectangle getBounds();

	/**
	 * Set the text that should be shown as a tooltip
	 * 
	 * @param tootlTipText
	 */
	void setToolTipText(String tootlTipText);

	/**
	 * Register a listener that is notified about mous moves
	 * 
	 * @param mouseMoveListener
	 */
	void addMouseMoveListener(MouseMoveListener mouseMoveListener);
}