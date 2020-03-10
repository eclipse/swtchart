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
 * Christoph Läubrich - refactor for usage with a generic plot area, add method to print vectors to GC
 *******************************************************************************/
package org.eclipse.swtchart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swtchart.internal.ChartLayout;
import org.eclipse.swtchart.internal.ChartLayoutData;
import org.eclipse.swtchart.internal.ChartTitle;
import org.eclipse.swtchart.internal.Legend;
import org.eclipse.swtchart.internal.PlotArea;
import org.eclipse.swtchart.internal.Title;
import org.eclipse.swtchart.internal.axis.AxisSet;
import org.eclipse.swtchart.internal.series.SeriesSet;

/**
 * A chart which are composed of title, legend, axes and plot area.
 */
public class Chart extends Composite implements Listener {

	/** the title */
	private final Title title;
	/** the legend */
	private final Legend legend;
	/** the set of axes */
	private final AxisSet axisSet;
	/** the plot area */
	private IPlotArea plotArea;
	/** the orientation of chart which can be horizontal or vertical */
	private int orientation;
	/** the state indicating if compressing series is enabled */
	private boolean compressEnabled;
	/** the state indicating if the update of chart appearance is suspended */
	private boolean updateSuspended;
	/** the set of plots */
	protected SeriesSet seriesSet;
	private final List<PaintListener> paintListener = new ArrayList<>();

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the parent composite on which chart is placed
	 * @param style
	 *            the style of widget to construct
	 */
	public Chart(Composite parent, int style) {
		this(parent, style, null);
		new PlotArea(this, SWT.NONE);
	}

	/**
	 * This is a constructor allows to opt in for not creating the default plot area and is the recommend way for creating charts
	 * <pre>Chart c = new Chart(..., null)
	 * new PlotArea(this, SWT.NONE);
	 * </pre>
	 * because that way we have full control over the creation of the charts Plot area and there is no risk of constructor init order problems
	 * 
	 * @param parent
	 * @param style
	 * @param justNull
	 *            simply provide <code>null</code> here
	 */
	public Chart(Composite parent, int style, Void justNull) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		orientation = SWT.HORIZONTAL;
		compressEnabled = true;
		updateSuspended = false;
		parent.layout();
		setLayout(new ChartLayout());
		seriesSet = new SeriesSet(this);
		title = new ChartTitle(this);
		title.setLayoutData(new ChartLayoutData(SWT.DEFAULT, 100));
		legend = new Legend(this, SWT.NONE);
		legend.setLayoutData(new ChartLayoutData(200, SWT.DEFAULT));
		axisSet = new AxisSet(this);
		addListener(SWT.Resize, this);
	}

	/**
	 * Gets the chart title.
	 * 
	 * @return the chart title
	 */
	public ITitle getTitle() {

		return title;
	}

	/**
	 * Gets the legend.
	 * 
	 * @return the legend
	 */
	public ILegend getLegend() {

		return legend;
	}

	/**
	 * Gets the set of axes.
	 * 
	 * @return the set of axes
	 */
	public IAxisSet getAxisSet() {

		return axisSet;
	}

	/**
	 * Gets the plot area.
	 * 
	 * @return the plot area
	 */
	public IPlotArea getPlotArea() {

		return plotArea;
	}

	public void setPlotArea(IPlotArea area) {

		plotArea = area;
		updateLayout();
	}

	/**
	 * Gets the set of series.
	 * 
	 * @return the set of series
	 */
	public ISeriesSet getSeriesSet() {

		return seriesSet;
	}

	@Override
	public void setBackground(Color color) {

		super.setBackground(color);
		for(Control child : getChildren()) {
			if(!(child instanceof IPlotArea) && !(child instanceof Legend)) {
				child.setBackground(color);
			}
		}
	}

	/**
	 * Gets the background color in plot area. This method is identical with
	 * <tt>getPlotArea().getBackground()</tt>.
	 * 
	 * @deprecated use {@link #getPlotArea()} instead to access the plot area directly
	 * @return the background color in plot area
	 */
	@Deprecated
	public Color getBackgroundInPlotArea() {

		return getPlotArea().getBackground();
	}

	/**
	 * Sets the background color in plot area.
	 * 
	 * @param color
	 *            the background color in plot area. If <tt>null</tt> is given,
	 *            default background color will be set.
	 * @deprecated use {@link #getPlotArea()} instead to access the plot area directly
	 * @exception IllegalArgumentException
	 *                if given color is disposed
	 */
	@Deprecated
	public void setBackgroundInPlotArea(Color color) {

		if(color != null && color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		getPlotArea().setBackground(color);
	}

	/**
	 * Sets the state of chart orientation. The horizontal orientation means
	 * that X axis is horizontal as usual, while the vertical orientation means
	 * that Y axis is horizontal.
	 * 
	 * @param orientation
	 *            the orientation which can be SWT.HORIZONTAL or SWT.VERTICAL
	 */
	@Override
	public void setOrientation(int orientation) {

		if(orientation == SWT.HORIZONTAL || orientation == SWT.VERTICAL) {
			this.orientation = orientation;
		}
		updateLayout();
	}

	/**
	 * Gets the state of chart orientation. The horizontal orientation means
	 * that X axis is horizontal as usual, while the vertical orientation means
	 * that Y axis is horizontal.
	 * 
	 * @return the orientation which can be SWT.HORIZONTAL or SWT.VERTICAL
	 */
	@Override
	public int getOrientation() {

		return orientation;
	}

	/**
	 * Enables compressing series. By default, compressing series is enabled,
	 * and normally there should be no usecase to disable it. However, if you
	 * suspect that something is wrong in compressing series, you can disable it
	 * to isolate the issue.
	 * 
	 * @param enabled
	 *            true if enabling compressing series
	 */
	public void enableCompress(boolean enabled) {

		compressEnabled = enabled;
	}

	/**
	 * Gets the state indicating if compressing series is enabled.
	 * 
	 * @return true if compressing series is enabled
	 */
	public boolean isCompressEnabled() {

		return compressEnabled;
	}

	/**
	 * Suspends the update of chart appearance.
	 * 
	 * <p>
	 * By default, when the chart model is changed (e.g. adding new series or
	 * changing chart properties), the chart appearance is updated accordingly.
	 * <p>
	 * However, when doing a lot of changes in the chart model at once, it is
	 * inefficient that the update happens many times unnecessarily. In this
	 * case, you may want to defer the update until completing whole model
	 * changes in order to have better performance.
	 * <p>
	 * For example, suppose there is a chart having a large number of series,
	 * the following example code disables the update during changing the model.
	 * 
	 * <pre>
	 * try {
	 *     // suspend update
	 *     chart.suspendUpdate(true);
	 * 
	 *     // do some changes for a large number of series
	 *     for (ISeries series : chart.getSeriesSet().getSeries()) {
	 *         series.enableStack(true);
	 *     }
	 * } finally {
	 *     // resume update
	 *     chart.suspendUpdate(false);
	 * }
	 * </pre>
	 * 
	 * Note that the update has to be resumed right after completing the model
	 * changes in order to avoid showing an incompletely updated chart.
	 * 
	 * @param suspend
	 *            true to suspend the update of chart appearance
	 */
	public void suspendUpdate(boolean suspend) {

		if(updateSuspended == suspend) {
			return;
		}
		updateSuspended = suspend;
		// make sure that chart is updated
		if(!suspend) {
			updateLayout();
			((SeriesSet)getSeriesSet()).updateStackAndRiserData();
		}
	}

	/**
	 * Gets the state indicating if the update of chart appearance is suspended.
	 * 
	 * @return true if the update of chart appearance is suspended
	 */
	public boolean isUpdateSuspended() {

		return updateSuspended;
	}

	@Override
	public void handleEvent(Event event) {

		switch(event.type) {
			case SWT.Resize:
				updateLayout();
				redraw();
				break;
			default:
				break;
		}
	}

	/**
	 * Updates the layout of chart elements.
	 */
	public void updateLayout() {

		if(updateSuspended) {
			return;
		}
		if(legend != null) {
			legend.updateLayoutData();
		}
		if(title != null) {
			title.updateLayoutData();
		}
		if(axisSet != null) {
			axisSet.updateLayoutData();
		}
		layout();
		if(axisSet != null) {
			axisSet.refresh();
		}
	}

	@Override
	public void update() {

		super.update();
		for(Control child : getChildren()) {
			child.update();
		}
	}

	@Override
	public void dispose() {

		title.dispose();
		legend.dispose();
		axisSet.dispose();
		super.dispose();
	}

	@Override
	public void redraw() {

		super.redraw();
		for(Control child : getChildren()) {
			child.redraw();
		}
	}

	/**
	 * Saves to file with given format.
	 * 
	 * @param filename
	 *            the file name
	 * @param format
	 *            the format (SWT.IMAGE_*). The supported formats depend on OS.
	 */
	public void save(String filename, int format) {

		Image image = new Image(Display.getDefault(), getBounds());
		GC gc = new GC(image);
		print(gc);
		gc.dispose();
		ImageData data = image.getImageData();
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[]{data};
		loader.save(filename, format);
		image.dispose();
	}

	/**
	 * Renders off-screen image.
	 * 
	 * @param image
	 *            The image to render off-screen
	 */
	public void renderOffscreenImage(Image image) {

		GC gc = new GC(image);
		print(gc);
		gc.dispose();
	}

	public void printChart(GC gc, Rectangle clientArea) {

		Transform oldTransform = new Transform(gc.getDevice());
		try {
			gc.getTransform(oldTransform);
			Point oldSize = getSize();
			try {
				setSize(clientArea.width, clientArea.height);
				Event e = new Event();
				e.gc = gc;
				e.widget = this;
				PaintEvent paintEvent = new PaintEvent(e);
				for(Control child : getChildren()) {
					Rectangle bounds = child.getBounds();
					if(child instanceof PaintListener) {
						Event subEvent = new Event();
						subEvent.gc = gc;
						subEvent.widget = child;
						Rectangle oldClipping = gc.getClipping();
						try {
							gc.setClipping(bounds);
							PaintEvent subPaint = new PaintEvent(subEvent);
							Transform transform = new Transform(gc.getDevice());
							try {
								transform.translate(bounds.x, bounds.y);
								gc.setTransform(transform);
								((PaintListener)child).paintControl(subPaint);
							} finally {
								transform.dispose();
								gc.setTransform(oldTransform);
							}
						} finally {
							gc.setClipping(oldClipping);
						}
					}
				}
				for(PaintListener listener : paintListener) {
					listener.paintControl(paintEvent);
				}
			} finally {
				setSize(oldSize);
			}
		} finally {
			oldTransform.dispose();
		}
	}

	@Override
	public void addPaintListener(PaintListener listener) {

		paintListener.add(listener);
		super.addPaintListener(listener);
	}

	@Override
	public void removePaintListener(PaintListener listener) {

		paintListener.remove(listener);
		super.removePaintListener(listener);
	}
}
