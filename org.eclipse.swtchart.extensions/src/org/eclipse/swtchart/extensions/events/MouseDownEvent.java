/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
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

public class MouseDownEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	private boolean redrawOnClick = true;

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

		this.redrawOnClick = redraw;
	}

	public boolean isRedrawOnClick() {

		return redrawOnClick;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		/*
		 * To recognise the node where the click event occurred, and redraw.
		 */
		if(baseChart.isCircularChart()) {
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
							((CircularSeries)series).setRootPointer(node.getParent());
						}
						// redraw form the node where it is clicked on.
						else {
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
		/*
		 * Activate the selection if the user made a single click.
		 */
		else if(isSingleClick(event)) {
			baseChart.getUserSelection().setStartCoordinate(event.x, event.y);
			baseChart.setClickStartTime(System.currentTimeMillis());
		} else {
			baseChart.getUserSelection().reset();
		}
	}
}
