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
package org.eclipse.swtchart.extensions.internal.marker;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.marker.AbstractPositionPaintListener;
import org.eclipse.swtchart.extensions.marker.IPositionPaintListener;

public class PositionMarker extends AbstractPositionPaintListener implements IPositionPaintListener {

	public PositionMarker(BaseChart baseChart) {
		super(baseChart);
	}

	@Override
	public void paintControl(PaintEvent e) {

		/*
		 * Plots a vertical/horizontal line in the plot area at the x position of the mouse.
		 */
		if(isDraw()) {
			int x = getX();
			int y = getY();
			e.gc.setForeground(getForegroundColor());
			if(x > 0 && x < e.width && y > 0 && y < e.height) {
				e.gc.drawLine(x, 0, x, e.height);
				e.gc.drawLine(0, y, e.width, y);
			}
		}
	}
}
