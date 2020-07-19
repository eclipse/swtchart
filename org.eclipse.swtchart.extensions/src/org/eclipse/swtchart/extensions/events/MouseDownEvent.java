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
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.internal.series.CircularSeries;
import org.eclipse.swtchart.internal.series.Pie;
import org.eclipse.swtchart.model.Node;

public class MouseDownEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

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

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		/*
		 * To recognise the node where the click event occured, and redraw.
		 */
		if(baseChart.isCircularChart()) {
			for(ISeries series : baseChart.getSeriesSet().getSeries()) {
				if(series instanceof CircularSeries) {
					double primaryValueX = baseChart.getSelectedPrimaryAxisValue(event.x, IExtendedChart.X_AXIS);
					double primaryValueY = baseChart.getSelectedPrimaryAxisValue(event.y, IExtendedChart.Y_AXIS);
					double radius = Math.sqrt(primaryValueX * primaryValueX + primaryValueY * primaryValueY);
					int level = ((int)radius) + 1 - (series instanceof Pie ? 0 : 1);
					Node node = null;
					double angleOfInspection = Math.atan2(primaryValueY, primaryValueX);
					if(angleOfInspection < 0.0)
						angleOfInspection += 2 * Math.PI;
					if(level < ((CircularSeries)series).getModel().getNodes().length)
						for(Node noda : ((CircularSeries)series).getModel().getNodes()[level]) {
							double lowerBound = (noda.getAngleBounds().x * Math.PI) / (double)180.0;
							double upperBound = ((noda.getAngleBounds().x + noda.getAngleBounds().y) * Math.PI) / (double)180.0;
							if((lowerBound <= angleOfInspection) && (upperBound >= angleOfInspection)) {
								node = noda;
								break;
							}
						}
					if(node != null) {
						// redraw from parent node, if clicked on the center of the Doughnut Chart.
						if(((CircularSeries)series).getModel().getRootPointer() == node) {
							((CircularSeries)series).getModel().setRootPointer(node.getParent());
						}
						// redraw form the node where it is clicked on.
						else {
							((CircularSeries)series).getModel().setRootPointer(node);
						}
					}
					/*
					 * redraw from rootNode if clicked elsewhere.
					 * (The only way to redraw a pieChart from an ancestor node.)
					 * Works for Doughnut chart as well.
					 */
					else {
						((CircularSeries)series).getModel().setRootPointer(((CircularSeries)series).getRootNode());
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
