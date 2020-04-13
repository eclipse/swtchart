/*******************************************************************************
 * Copyright (c) 2008, 2020 SWTChart project.
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
	public Chart getChart();

	/**
	 * Adds the custom paint listener.
	 *
	 * @param listener
	 *            the custom paint listener
	 */
	public void addCustomPaintListener(ICustomPaintListener listener);

	/**
	 * Removes the custom paint listener
	 *
	 * @param listener
	 *            the custom paint listener
	 */
	public void removeCustomPaintListener(ICustomPaintListener listener);

	/**
	 * 
	 * @return the current Background color
	 */
	public Color getBackground();

	/**
	 * set the color of the plot area
	 * 
	 * @param color
	 */
	public void setBackground(Color color);

	/**
	 * Draws the image centered in the plot area.
	 * 
	 * @param image
	 */
	public void setBackgroundImage(Image image);

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
	public Point getSize();

	/**
	 * 
	 * @return the control that represents thsi plot area
	 */
	public Control getControl();

	/**
	 * @deprecated use {@link #getSize()} instead
	 * @return
	 */
	@Deprecated
	public Rectangle getBounds();

	/**
	 * Set the text that should be shown as a tooltip
	 * 
	 * @param tootlTipText
	 */
	public void setToolTipText(String tootlTipText);

	/**
	 * Register a listener that is notified about mous moves
	 * 
	 * @param mouseMoveListener
	 */
	public void addMouseMoveListener(MouseMoveListener mouseMoveListener);
}
