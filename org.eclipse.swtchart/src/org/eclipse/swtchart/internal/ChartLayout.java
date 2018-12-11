/*******************************************************************************
 * Copyright (c) 2008, 2018 SWTChart project.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.internal.axis.AxisTickLabels;
import org.eclipse.swtchart.internal.axis.AxisTickMarks;
import org.eclipse.swtchart.internal.axis.AxisTitle;

/**
 * Manages the layout on plot chart panel.
 */
public class ChartLayout extends Layout {

	/** the title height */
	private int titleHeight;
	/** the title width */
	private int titleWidth;
	/** the legend width */
	private int legendWidth;
	/** the legend height */
	private int legendHeight;
	/** the bottom axis height */
	private int bottomAxisHeight;
	/** the top axis height */
	private int topAxisHeight;
	/** the left axis width */
	private int leftAxisWidth;
	/** the right axis width */
	private int rightAxisWidth;
	/** the plot area width */
	private int plotAreaWidth;
	/** the plot area height */
	private int plotAreaHeight;
	/** the chart title */
	private ChartTitle title;
	/** the legend */
	private Legend legend;
	/** the plot area */
	private PlotArea plot;
	/** the axes */
	private Axis[] axes;
	/** the horizontal axes */
	private Axis[] horizontalAxes;
	/** the vertical axes */
	private Axis[] verticalAxes;
	/** the map between axis and axis layout data */
	private Map<Axis, AxisLayoutData> axisLayoutDataMap;
	/** the offset for bottom axis */
	private int bottomAxisOffset = 0;
	/** the offset for top axis */
	private int topAxisOffset = 0;
	/** the offset for left axis */
	private int leftAxisOffset = 0;
	/** the offset for right axis */
	private int rightAxisOffset = 0;
	/** the margin */
	public static final int MARGIN = 5;
	/** the padding */
	public static final int PADDING = 5;

	/**
	 * Constructor.
	 */
	public ChartLayout() {
		initWidgetSizeVariables();
		axisLayoutDataMap = new HashMap<Axis, AxisLayoutData>();
	}

	/**
	 * Initializes the size variables of widgets.
	 */
	private void initWidgetSizeVariables() {

		titleHeight = 0;
		titleWidth = 0;
		bottomAxisHeight = 0;
		topAxisHeight = 0;
		leftAxisWidth = 0;
		rightAxisWidth = 0;
		legendWidth = 0;
		legendHeight = 0;
		plotAreaWidth = 0;
		plotAreaHeight = 0;
	}

	/*
	 * @see Layout#computeSize(Composite , int, int, boolean)
	 */
	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {

		return new Point(wHint, hHint);
	}

	/*
	 * @see Layout#layout(Composite, boolean)
	 */
	@Override
	protected void layout(Composite composite, boolean flushCache) {

		if(!parseControls(composite)) {
			return;
		}
		Rectangle r = composite.getClientArea();
		initWidgetSizeVariables();
		initTitleAndLegendSize();
		initAxisSize();
		computePlotAreaSize(r);
		computeAxisSize(r);
		adjustForRotatedTickLabels(r);
		adjustForMostLeftRightTickLabel(r);
		layoutTitle(r);
		layoutLegend(r);
		layoutPlot(r);
		layoutAxes(r);
	}

	/**
	 * Parses the controls on given composite.
	 * 
	 * @param composite
	 *            the composite
	 * @return true if all children found
	 */
	private boolean parseControls(Composite composite) {

		for(Control child : composite.getChildren()) {
			if(child instanceof Legend) {
				legend = (Legend)child;
			} else if(child instanceof PlotArea) {
				plot = (PlotArea)child;
			}
		}
		if(composite instanceof Chart) {
			IAxisSet axisSet = ((Chart)composite).getAxisSet();
			if(axisSet != null) {
				axes = (Axis[])axisSet.getAxes();
				if(((Chart)composite).getOrientation() == SWT.HORIZONTAL) {
					horizontalAxes = (Axis[])axisSet.getXAxes();
					verticalAxes = (Axis[])axisSet.getYAxes();
				} else {
					verticalAxes = (Axis[])axisSet.getXAxes();
					horizontalAxes = (Axis[])axisSet.getYAxes();
				}
			}
			title = (ChartTitle)((Chart)composite).getTitle();
		}
		if(title == null || legend == null || plot == null || axes == null) {
			// the initialization of chart is not completed yet
			return false;
		}
		return true;
	}

	/**
	 * Initializes the size of title and legend.
	 */
	private void initTitleAndLegendSize() {

		titleWidth = ((ChartLayoutData)title.getLayoutData()).widthHint;
		titleHeight = ((ChartLayoutData)title.getLayoutData()).heightHint;
		legendWidth = ((ChartLayoutData)legend.getLayoutData()).widthHint;
		legendHeight = ((ChartLayoutData)legend.getLayoutData()).heightHint;
	}

	/**
	 * Initializes the size of axes.
	 */
	private void initAxisSize() {

		axisLayoutDataMap.clear();
		for(Axis axis : axes) {
			AxisLayoutData layoutData = new AxisLayoutData(axis);
			if(layoutData.titleLayoutdata == null || layoutData.tickLabelsLayoutdata == null || layoutData.tickMarksLayoutdata == null) {
				continue;
			}
			axisLayoutDataMap.put(axis, layoutData);
			Position position = axis.getPosition();
			if(position == Position.Primary && axis.isHorizontalAxis()) {
				bottomAxisHeight += layoutData.titleLayoutdata.heightHint + layoutData.tickLabelsLayoutdata.heightHint + layoutData.tickMarksLayoutdata.heightHint;
			} else if(position == Position.Secondary && axis.isHorizontalAxis()) {
				topAxisHeight += layoutData.titleLayoutdata.heightHint + layoutData.tickLabelsLayoutdata.heightHint + layoutData.tickMarksLayoutdata.heightHint;
			} else if(position == Position.Primary && !axis.isHorizontalAxis()) {
				leftAxisWidth += layoutData.titleLayoutdata.widthHint + layoutData.tickLabelsLayoutdata.widthHint + layoutData.tickMarksLayoutdata.widthHint;
			} else if(position == Position.Secondary && !axis.isHorizontalAxis()) {
				rightAxisWidth += layoutData.titleLayoutdata.widthHint + layoutData.tickLabelsLayoutdata.widthHint + layoutData.tickMarksLayoutdata.widthHint;
			}
		}
	}

	/**
	 * Computes the size of plot area.
	 * 
	 * @param r
	 *            the rectangle to layout
	 */
	private void computePlotAreaSize(Rectangle r) {

		int legendPosition = legend.getPosition();
		plotAreaWidth = r.width - leftAxisWidth - rightAxisWidth - (legendPosition == SWT.LEFT || legendPosition == SWT.RIGHT ? legendWidth + (legendWidth == 0 ? 0 : PADDING) : 0) - MARGIN * 2;
		plotAreaHeight = r.height - bottomAxisHeight - topAxisHeight - titleHeight - MARGIN * 2 - (titleHeight == 0 ? 0 : PADDING) - (legendPosition == SWT.TOP || legendPosition == SWT.BOTTOM ? legendHeight + (legendHeight == 0 ? 0 : PADDING) : 0);
	}

	/**
	 * Computes the size of axes updating tick labels.
	 * 
	 * @param r
	 *            the rectangle to layout
	 */
	private void computeAxisSize(Rectangle r) {

		// update vertical axis tick labels
		updateVerticalAxisTick();
		// compute axis width
		for(IAxis axis : verticalAxes) {
			int tickAreaWidth = Axis.MARGIN;
			if(axis.getTick().isVisible()) {
				tickAreaWidth = ((Axis)axis).getTick().getAxisTickLabels().getTickLabelMaxLength();
			}
			AxisLayoutData axisLayout = axisLayoutDataMap.get(axis);
			axisLayout.tickLabelsLayoutdata.widthHint += tickAreaWidth;
			if(axis.getPosition() == Position.Primary) {
				leftAxisWidth += tickAreaWidth;
			} else {
				rightAxisWidth += tickAreaWidth;
			}
		}
		// compute axis height
		for(IAxis axis : horizontalAxes) {
			if(axis.getTick().isVisible()) {
				continue;
			}
			if(axis.getPosition() == Position.Primary) {
				bottomAxisHeight += Axis.MARGIN;
			} else {
				topAxisHeight += Axis.MARGIN;
			}
		}
		// update plot area width
		computePlotAreaSize(r);
		// update horizontal axis tick labels
		updateHorizontalAxisTick();
	}

	/**
	 * Adjust the axis size for rotated tick labels.
	 * 
	 * @param r
	 *            the rectangle to layout
	 */
	private void adjustForRotatedTickLabels(Rectangle r) {

		for(IAxis axis : horizontalAxes) {
			double angle = axis.getTick().getTickLabelAngle();
			if(angle == 0 || !axis.getTick().isVisible()) {
				continue;
			}
			// update tick label height
			int tickLabelMaxLength = ((Axis)axis).getTick().getAxisTickLabels().getTickLabelMaxLength();
			AxisLayoutData layoutData = axisLayoutDataMap.get(axis);
			int height = Axis.MARGIN + (int)(tickLabelMaxLength * Math.sin(Math.toRadians(angle)) + Util.getExtentInGC(layoutData.axisTickLabels.getFont(), null).y * Math.cos(Math.toRadians(angle)));
			int delta = height - layoutData.tickLabelsLayoutdata.heightHint;
			layoutData.tickLabelsLayoutdata.heightHint = height;
			// update axis height
			if(axis.getPosition() == Position.Primary) {
				bottomAxisHeight += delta;
			} else {
				topAxisHeight += delta;
			}
			// update plot area height
			computePlotAreaSize(r);
			updateVerticalAxisTick();
		}
	}

	/**
	 * Adjust the axis size for most left/right tick label.
	 * 
	 * @param r
	 *            the rectangle to layout
	 */
	private void adjustForMostLeftRightTickLabel(Rectangle r) {

		// get axis margin hint
		int rightAxisMarginHint = 0;
		int leftAxisMarginHint = 0;
		for(IAxis axis : horizontalAxes) {
			if(!axis.getTick().isVisible()) {
				continue;
			}
			rightAxisMarginHint = Math.max(rightAxisMarginHint, ((Axis)axis).getTick().getAxisTickLabels().getRightMarginHint(plotAreaWidth));
			leftAxisMarginHint = Math.max(leftAxisMarginHint, ((Axis)axis).getTick().getAxisTickLabels().getLeftMarginHint(plotAreaWidth));
		}
		// have space to draw most right tick label on horizontal axis
		if((legendWidth == 0 || legend.getPosition() != SWT.RIGHT) && rightAxisWidth < rightAxisMarginHint) {
			rightAxisWidth = rightAxisMarginHint;
			computePlotAreaSize(r);
			updateHorizontalAxisTick();
		}
		// have space to draw most left tick label on horizontal axis
		if((legendWidth == 0 || legend.getPosition() != SWT.LEFT) && leftAxisWidth < leftAxisMarginHint) {
			leftAxisWidth = leftAxisMarginHint;
			computePlotAreaSize(r);
			updateHorizontalAxisTick();
		}
	}

	/**
	 * Updates the horizontal axis tick.
	 */
	private void updateHorizontalAxisTick() {

		for(IAxis axis : horizontalAxes) {
			((Axis)axis).getTick().updateTick(plotAreaWidth);
		}
	}

	/**
	 * Updates the vertical axis tick.
	 */
	private void updateVerticalAxisTick() {

		for(IAxis axis : verticalAxes) {
			((Axis)axis).getTick().updateTick(plotAreaHeight);
		}
	}

	/**
	 * Layouts the title.
	 * 
	 * @param r
	 *            the rectangle to layout
	 */
	private void layoutTitle(Rectangle r) {

		int x = (int)((r.width - titleWidth) / 2d);
		int y = MARGIN;
		int width = titleWidth;
		int height = titleHeight;
		title.setBounds(x, y, width, height);
	}

	/**
	 * Layouts the legend.
	 * 
	 * @param r
	 *            the rectangle to layout
	 */
	private void layoutLegend(Rectangle r) {

		int legendPosition = legend.getPosition();
		int tHeight = titleHeight + ((titleHeight == 0) ? 0 : PADDING);
		int x;
		int y;
		if(legendPosition == SWT.RIGHT) {
			x = r.width - legendWidth - MARGIN;
			y = (tHeight + r.height - legendHeight) / 2;
		} else if(legendPosition == SWT.LEFT) {
			x = MARGIN;
			y = (tHeight + r.height - legendHeight) / 2;
		} else if(legendPosition == SWT.TOP) {
			x = (r.width - legendWidth) / 2;
			y = tHeight + MARGIN;
		} else if(legendPosition == SWT.BOTTOM) {
			x = (r.width - legendWidth) / 2;
			y = r.height - legendHeight - MARGIN;
		} else {
			throw new IllegalStateException();
		}
		int width = legendWidth;
		int height = legendHeight;
		if(y < tHeight) {
			y = tHeight;
		}
		legend.setBounds(x, y, width, height);
	}

	/**
	 * Layouts the plot.
	 * 
	 * @param r
	 *            the rectangle to layout
	 */
	private void layoutPlot(Rectangle r) {

		int legendPosition = legend.getPosition();
		int x = leftAxisWidth + MARGIN + (legendPosition == SWT.LEFT ? legendWidth + (legendWidth == 0 ? 0 : PADDING) : 0);
		int y = titleHeight + topAxisHeight + MARGIN + (titleHeight == 0 ? 0 : PADDING) + (legendPosition == SWT.TOP ? legendHeight + (legendHeight == 0 ? 0 : PADDING) : 0);
		plot.setBounds(x, y, plotAreaWidth, plotAreaHeight);
	}

	/**
	 * Layouts the axes.
	 * 
	 * @param r
	 *            the rectangle to layout
	 */
	private void layoutAxes(Rectangle r) {

		bottomAxisOffset = 0;
		topAxisOffset = 0;
		leftAxisOffset = 0;
		rightAxisOffset = 0;
		for(Axis axis : axes) {
			AxisLayoutData layoutData = axisLayoutDataMap.get(axis);
			Position position = axis.getPosition();
			if(position == Position.Primary && axis.isHorizontalAxis()) {
				layoutBottomAxis(r, layoutData);
			} else if(position == Position.Secondary && axis.isHorizontalAxis()) {
				layoutTopAxis(r, layoutData);
			} else if(position == Position.Primary && !axis.isHorizontalAxis()) {
				layoutLeftAxis(r, layoutData);
			} else if(position == Position.Secondary && !axis.isHorizontalAxis()) {
				layoutRightAxis(r, layoutData);
			}
		}
	}

	/**
	 * Layouts the bottom axis.
	 * 
	 * @param r
	 *            the rectangle
	 * @param layoutData
	 *            the layout data
	 */
	private void layoutBottomAxis(Rectangle r, AxisLayoutData layoutData) {

		int legendPosition = legend.getPosition();
		int height = layoutData.titleLayoutdata.heightHint;
		int x = leftAxisWidth + MARGIN + (legendPosition == SWT.LEFT ? legendWidth + (legendWidth == 0 ? 0 : PADDING) : 0);
		int y = r.height - height - bottomAxisOffset - MARGIN - (legendPosition == SWT.BOTTOM ? legendHeight + (legendHeight == 0 ? 0 : PADDING) : 0);
		bottomAxisOffset += height;
		if(y - layoutData.tickLabelsLayoutdata.heightHint - layoutData.tickMarksLayoutdata.heightHint < titleHeight + (titleHeight == 0 ? 0 : PADDING)) {
			y = titleHeight + (titleHeight == 0 ? 0 : PADDING) + layoutData.tickLabelsLayoutdata.heightHint + layoutData.tickMarksLayoutdata.heightHint;
		}
		int width = layoutData.titleLayoutdata.widthHint;
		int titleX = x + (plotAreaWidth - width) / 2;
		layoutData.axisTitle.setBounds(titleX, y, width, height);
		height = layoutData.tickLabelsLayoutdata.heightHint;
		y -= height;
		bottomAxisOffset += height;
		layoutData.axisTickLabels.setBounds(0, y, r.width, height);
		height = layoutData.tickMarksLayoutdata.heightHint;
		y -= height;
		bottomAxisOffset += height;
		layoutData.axisTickMarks.setBounds(x, y, plotAreaWidth, height);
	}

	/**
	 * Layouts the top axis.
	 * 
	 * @param r
	 *            the rectangle
	 * @param layoutData
	 *            the layout data
	 */
	private void layoutTopAxis(Rectangle r, AxisLayoutData layoutData) {

		int legendPosition = legend.getPosition();
		int height = layoutData.titleLayoutdata.heightHint;
		int x = leftAxisWidth + MARGIN + (legendPosition == SWT.LEFT ? legendWidth + (legendWidth == 0 ? 0 : PADDING) : 0);
		int y = titleHeight + topAxisOffset + MARGIN + ((titleHeight == 0) ? 0 : PADDING);
		topAxisOffset += height;
		int width = layoutData.titleLayoutdata.widthHint;
		int titleX = x + (plotAreaWidth - width) / 2;
		layoutData.axisTitle.setBounds(titleX, y, width, height);
		y += height;
		height = layoutData.tickLabelsLayoutdata.heightHint;
		topAxisOffset += height;
		layoutData.axisTickLabels.setBounds(0, y, r.width, height);
		y += height;
		height = layoutData.tickMarksLayoutdata.heightHint;
		topAxisOffset += height;
		layoutData.axisTickMarks.setBounds(x, y, plotAreaWidth, height);
	}

	/**
	 * Layouts the left axis.
	 * 
	 * @param r
	 *            the rectangle
	 * @param layoutData
	 *            the layout data
	 */
	private void layoutLeftAxis(Rectangle r, AxisLayoutData layoutData) {

		int legendPosition = legend.getPosition();
		int yAxisMargin = Axis.MARGIN + AxisTickMarks.TICK_LENGTH;
		int width = layoutData.titleLayoutdata.widthHint;
		int x = MARGIN + leftAxisOffset + (legendPosition == SWT.LEFT ? legendWidth + (legendWidth == 0 ? 0 : PADDING) : 0);
		int y = titleHeight + topAxisHeight + MARGIN + ((titleHeight == 0) ? 0 : PADDING) + (legendPosition == SWT.TOP ? legendHeight + (legendHeight == 0 ? 0 : PADDING) : 0);
		leftAxisOffset += width;
		int height = layoutData.titleLayoutdata.heightHint;
		int titleY = y + (plotAreaHeight - height) / 2;
		layoutData.axisTitle.setBounds(x, titleY, width, height);
		x += width;
		width = layoutData.tickLabelsLayoutdata.widthHint;
		leftAxisOffset += width;
		layoutData.axisTickLabels.setBounds(x, y - yAxisMargin, width, plotAreaHeight + yAxisMargin * 2);
		x += width;
		width = layoutData.tickMarksLayoutdata.widthHint;
		leftAxisOffset += width;
		layoutData.axisTickMarks.setBounds(x, y, width, plotAreaHeight);
	}

	/**
	 * Layouts the right axis.
	 * 
	 * @param r
	 *            the rectangle
	 * @param layoutData
	 *            the layout data
	 */
	private void layoutRightAxis(Rectangle r, AxisLayoutData layoutData) {

		int legendPosition = legend.getPosition();
		int yAxisMargin = Axis.MARGIN + AxisTickMarks.TICK_LENGTH;
		int width = layoutData.titleLayoutdata.widthHint;
		int x = r.width - width - rightAxisOffset - MARGIN - (legendPosition == SWT.RIGHT ? legendWidth + (legendWidth == 0 ? 0 : PADDING) : 0);
		int y = titleHeight + topAxisHeight + MARGIN + ((titleHeight == 0) ? 0 : PADDING) + (legendPosition == SWT.TOP ? legendHeight + (legendHeight == 0 ? 0 : PADDING) : 0);
		rightAxisOffset += width;
		int height = layoutData.titleLayoutdata.heightHint;
		int titleY = y + (plotAreaHeight - height) / 2;
		layoutData.axisTitle.setBounds(x, titleY, width, height);
		width = layoutData.tickLabelsLayoutdata.widthHint;
		x -= width;
		rightAxisOffset += width;
		layoutData.axisTickLabels.setBounds(x, y - yAxisMargin, width, plotAreaHeight + yAxisMargin * 2);
		width = layoutData.tickMarksLayoutdata.widthHint;
		x -= width;
		rightAxisOffset += width;
		layoutData.axisTickMarks.setBounds(x, y, width, plotAreaHeight);
	}

	/**
	 * Axis layout data.
	 */
	private static class AxisLayoutData {

		/** the axis tick marks */
		public AxisTickMarks axisTickMarks;
		/** the axis tick labels */
		public AxisTickLabels axisTickLabels;
		/** the axis title */
		public AxisTitle axisTitle;
		/** the axis title layout data */
		public ChartLayoutData titleLayoutdata;
		/** the tick label layout data */
		public ChartLayoutData tickLabelsLayoutdata;
		/** the tick marks layout data */
		public ChartLayoutData tickMarksLayoutdata;

		/**
		 * Constructor.
		 * 
		 * @param axis
		 *            the axis
		 */
		public AxisLayoutData(Axis axis) {
			axisTickMarks = axis.getTick().getAxisTickMarks();
			axisTickLabels = axis.getTick().getAxisTickLabels();
			axisTitle = (AxisTitle)axis.getTitle();
			titleLayoutdata = (ChartLayoutData)axisTitle.getLayoutData();
			tickLabelsLayoutdata = axisTickLabels.getLayoutData();
			tickMarksLayoutdata = axisTickMarks.getLayoutData();
		}
	}
}
