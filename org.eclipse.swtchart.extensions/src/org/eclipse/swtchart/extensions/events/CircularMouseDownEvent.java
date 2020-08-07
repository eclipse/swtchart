/*******************************************************************************
 * Copyright (c) 2020 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta: Orignal API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.internal.series.CircularSeries;
import org.eclipse.swtchart.model.Node;

public class CircularMouseDownEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	private boolean redrawOnClick = true;
	private boolean fillEntireSpace = false;

	@Override
	public int getEvent() {

		return IMouseSupport.EVENT_MOUSE_DOWN;
	}

	@Override
	public int getButton() {

		return IMouseSupport.MOUSE_BUTTON_LEFT;
	}

	@Override
	public int getStateMask() {

		return SWT.NONE;
	}

	public void setRedrawOnClick(boolean redraw) {

		redrawOnClick = redraw;
	}

	public void setFillEntireSpace(boolean fillEntireSpace) {

		this.fillEntireSpace = fillEntireSpace;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		for(ISeries series : baseChart.getSeriesSet().getSeries()) {
			if(series instanceof CircularSeries) {
				double primaryValueX = baseChart.getSelectedPrimaryAxisValue(event.x, IExtendedChart.X_AXIS);
				double primaryValueY = baseChart.getSelectedPrimaryAxisValue(event.y, IExtendedChart.Y_AXIS);
				Node node = ((ICircularSeries)series).getPieSliceFromPosition(primaryValueX, primaryValueY);
				if(!redrawOnClick) {
					((CircularSeries)series).setHighlightedNode(node);
					break;
				}
				if(node != null) {
					// redraw from parent node, if clicked on the center of the Doughnut Chart.
					if(((CircularSeries)series).getRootPointer() == node) {
						if(!fillEntireSpace)
							((CircularSeries)series).getModel().setRootPointer(node.getParent());
						else
							((CircularSeries)series).setRootPointer(node.getParent());
					}
					// redraw form the node where it is clicked on.
					else {
						if(!fillEntireSpace)
							((CircularSeries)series).getModel().setRootPointer(node);
						else
							((CircularSeries)series).setRootPointer(node);
					}
				}
				/*
				 * redraw from rootNode if clicked elsewhere.
				 * (The only way to redraw a pieChart from an ancestor node.)
				 * Works for Doughnut chart as well.
				 */
				else {
					((CircularSeries)series).setRootPointer(((CircularSeries)series).getRootNode());
				}
			}
		}
	}
}
