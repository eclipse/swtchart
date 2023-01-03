/*******************************************************************************
 * Copyright (c) 2020, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 * Philip Wenig - circular series extended legend
 *******************************************************************************/
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.piecharts.CircularSeriesLegend;
import org.eclipse.swtchart.internal.series.CircularSeries;
import org.eclipse.swtchart.model.Node;

public class CircularMouseDownEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	private ScrollableChart scrollableChart;
	private boolean redrawOnClick;
	private boolean fillEntireSpace;

	public CircularMouseDownEvent(ScrollableChart scrollableChart) {

		redrawOnClick = true;
		fillEntireSpace = false;
		this.scrollableChart = scrollableChart;
	}

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

		for(ISeries<?> series : baseChart.getSeriesSet().getSeries()) {
			if(series instanceof CircularSeries) {
				CircularSeries circularSeries = (CircularSeries)series;
				double primaryValueX = baseChart.getSelectedPrimaryAxisValue(event.x, IExtendedChart.X_AXIS);
				double primaryValueY = baseChart.getSelectedPrimaryAxisValue(event.y, IExtendedChart.Y_AXIS);
				Node node = circularSeries.getPieSliceFromPosition(primaryValueX, primaryValueY);
				//
				if(!redrawOnClick) {
					circularSeries.setHighlightedNode(node);
					if(!scrollableChart.getLinkedScrollableCharts().isEmpty()) {
						String nodeId = null;
						if(node != null) {
							nodeId = node.getId();
						}
						//
						for(ScrollableChart linkedChart : scrollableChart.getLinkedScrollableCharts()) {
							for(ISeries<?> linkedSeries : (ISeries<?>[])linkedChart.getBaseChart().getSeriesSet().getSeries()) {
								if(linkedSeries instanceof CircularSeries) {
									Node correspondingNode = ((CircularSeries)linkedSeries).getNodeById(nodeId);
									((CircularSeries)linkedSeries).setHighlightedNode(correspondingNode);
								}
							}
						}
					}
					break;
				} else {
					/*
					 * Case when redrawOnClick is true
					 */
					if(node != null) {
						/*
						 * Redraw from parent node, if clicked on the center of the Doughnut Chart.
						 */
						if(circularSeries.getRootPointer() == node) {
							if(!fillEntireSpace) {
								circularSeries.getNodeDataModel().setRootPointer(node.getParent());
							} else {
								circularSeries.setRootPointer(node.getParent());
							}
						} else {
							/*
							 * Redraw form the node where it is clicked on.
							 */
							if(!fillEntireSpace) {
								circularSeries.getNodeDataModel().setRootPointer(node);
							} else {
								circularSeries.setRootPointer(node);
							}
						}
					} else {
						/*
						 * redraw from rootNode if clicked elsewhere.
						 * (The only way to redraw a pieChart from an ancestor node.)
						 * Works for Doughnut chart as well.
						 */
						circularSeries.setRootPointer(((CircularSeries)series).getRootNode());
					}
					// handling the linked charts
					if(!scrollableChart.getLinkedScrollableCharts().isEmpty()) {
						// when the node is selected.
						String nodeId = null;
						if(node != null) {
							nodeId = node.getId();
							for(ScrollableChart linkedChart : scrollableChart.getLinkedScrollableCharts()) {
								for(ISeries<?> linkedSeries : (ISeries<?>[])linkedChart.getBaseChart().getSeriesSet().getSeries()) {
									if(linkedSeries instanceof CircularSeries) {
										CircularSeries circularSeriesLinked = (CircularSeries)linkedSeries;
										Node correspondingNode = circularSeriesLinked.getNodeById(nodeId);
										if(circularSeriesLinked.getRootPointer() == correspondingNode) {
											if(!fillEntireSpace) {
												circularSeriesLinked.getNodeDataModel().setRootPointer(correspondingNode.getParent());
											} else {
												circularSeriesLinked.setRootPointer(correspondingNode.getParent());
											}
										} else {
											/*
											 * redraw form the node where it is clicked on.
											 */
											if(!fillEntireSpace) {
												circularSeriesLinked.getNodeDataModel().setRootPointer(correspondingNode);
											} else {
												circularSeriesLinked.setRootPointer(correspondingNode);
											}
										}
									}
								}
							}
						} else {
							/*
							 * When node is not selected, undo everything to rootNode
							 */
							for(ScrollableChart linkedChart : scrollableChart.getLinkedScrollableCharts()) {
								for(ISeries<?> linkedSeries : (ISeries<?>[])linkedChart.getBaseChart().getSeriesSet().getSeries()) {
									if(linkedSeries instanceof CircularSeries) {
										CircularSeries circularSeriesLinked = (CircularSeries)linkedSeries;
										circularSeries.setRootPointer(circularSeriesLinked.getRootNode());
									}
								}
							}
						}
					}
					/*
					 * Adjust the colors because setRootPointer(...) updates the colors via the CircularSeriesCompressor.
					 */
					String[] labels = circularSeries.getLabels();
					if(labels != null) {
						for(String label : labels) {
							ISeriesSettings seriesSettings = baseChart.getSeriesSettings(label);
							baseChart.applySeriesSettings(new CircularSeriesLegend<>(circularSeries.getNodeById(label), circularSeries.getNodeDataModel()), seriesSettings);
						}
					}
				}
			}
		}
	}
}