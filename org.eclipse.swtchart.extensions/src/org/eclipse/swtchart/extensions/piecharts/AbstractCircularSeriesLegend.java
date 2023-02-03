/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.piecharts;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.IDisposeListener;
import org.eclipse.swtchart.IErrorBar;
import org.eclipse.swtchart.ISeriesLabel;
import org.eclipse.swtchart.internal.compress.Compress;
import org.eclipse.swtchart.model.CartesianSeriesModel;
import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.model.NodeDataModel;

public abstract class AbstractCircularSeriesLegend<T> implements ICircularSeries<T> {

	@Override
	public String getId() {

		return null;
	}

	@Override
	public void setVisible(boolean visible) {

	}

	@Override
	public void setVisibleBuffered(boolean visible) {

	}

	@Override
	public boolean isVisible() {

		return false;
	}

	@Override
	public boolean isVisibleBuffered() {

		return false;
	}

	@Override
	public SeriesType getType() {

		return null;
	}

	@Override
	public void enableStack(boolean enabled) {

	}

	@Override
	public boolean isStackEnabled() {

		return false;
	}

	@Override
	public void setXSeries(double[] series) {

	}

	@Override
	public void setYSeries(double[] series) {

	}

	@Override
	@Deprecated
	public void setXDateSeries(Date[] series) {

	}

	@Override
	@Deprecated
	public Date[] getXDateSeries() {

		return null;
	}

	@Override
	public void setXLocalDateSeries(LocalDate[] series, ZoneOffset zoneOffset) {

	}

	@Override
	public LocalDate[] getXLocalDateSeries(ZoneOffset zoneOffset) {

		return new LocalDate[0];
	}

	@Override
	public double[] getXSeries() {

		return null;
	}

	@Override
	public double[] getYSeries() {

		return null;
	}

	@Override
	public void setDataModel(CartesianSeriesModel<T> model) {

	}

	@Override
	public CartesianSeriesModel<T> getDataModel() {

		return null;
	}

	@Override
	public int getXAxisId() {

		return 0;
	}

	@Override
	public void setXAxisId(int id) {

	}

	@Override
	public int getYAxisId() {

		return 0;
	}

	@Override
	public void setYAxisId(int id) {

	}

	@Override
	public IErrorBar getXErrorBar() {

		return null;
	}

	@Override
	public IErrorBar getYErrorBar() {

		return null;
	}

	@Override
	public ISeriesLabel getLabel() {

		return null;
	}

	@Override
	public void setVisibleInLegend(boolean visible) {

	}

	@Override
	public boolean isVisibleInLegend() {

		return false;
	}

	@Override
	public void setDescription(String description) {

	}

	@Override
	public String getDescription() {

		return null;
	}

	@Override
	public Point getPixelCoordinates(int index) {

		return null;
	}

	@Override
	public void addDisposeListener(IDisposeListener listener) {

	}

	@Override
	public Color getSliceColor() {

		return null;
	}

	@Override
	public void setSliceColor(Color sliceColor) {

	}

	@Override
	public Color getBorderColor() {

		return null;
	}

	@Override
	public void setBorderColor(Color borderColor) {

	}

	@Override
	public int getBorderWidth() {

		return 0;
	}

	@Override
	public void setBorderWidth(int width) {

	}

	@Override
	public int getBorderStyle() {

		return 0;
	}

	@Override
	public void setBorderStyle(int borderStyle) {

	}

	@Override
	public Color getSliceColorHighlight() {

		return null;
	}

	@Override
	public void setSliceColorHighlight(Color sliceColor) {

	}

	@Override
	public Color getBorderColorHighlight() {

		return null;
	}

	@Override
	public void setBorderColorHighlight(Color borderColor) {

	}

	@Override
	public int getBorderWidthHighlight() {

		return 0;
	}

	@Override
	public void setBorderWidthHighlight(int width) {

	}

	@Override
	public int getBorderStyleHighlight() {

		return 0;
	}

	@Override
	public void setBorderStyleHighlight(int borderStyle) {

	}

	@Override
	public String[] getLabels() {

		return null;
	}

	@Override
	public Color[] getColors() {

		return null;
	}

	@Override
	public void setSeries(String[] labels, double[] values) {

	}

	@Override
	public void setColor(String label, Color color) {

	}

	@Override
	public void setColor(Color[] colors) {

	}

	@Override
	public Compress getCompressor() {

		return null;
	}

	@Override
	public Node getRootNode() {

		return null;
	}

	@Override
	public Node getNodeById(String id) {

		return null;
	}

	@Override
	public List<Node> getSeries() {

		return null;
	}

	@Override
	public void addNode(String id, double val) {

	}

	@Override
	public NodeDataModel getNodeDataModel() {

		return null;
	}

	@Override
	public void setNodeDataModel(NodeDataModel nodeDataModel) {

	}

	@Override
	public int getMaxTreeDepth() {

		return 0;
	}

	@Override
	public void setHighlightedNode(Node highlightedNode) {

	}

	@Override
	public Node getHighlightedNode() {

		return null;
	}

	@Override
	public Node getPieSliceFromPosition(double primaryValueX, double primaryValueY) {

		return null;
	}

	@Override
	public double getSlicePercent(String id) {

		return 0;
	}

	@Override
	public Node getPieSliceFromPosition(int x, int y) {

		return null;
	}
}