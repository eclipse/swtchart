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
 * Christoph Läubrich - refactor for API changes of Plot area
 * Philip Wenig - added the background image option
 *******************************************************************************/
package org.eclipse.swtchart.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.ICustomPaintListener;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.internal.series.Series;
import org.eclipse.swtchart.internal.series.SeriesSet;

/**
 * Plot area to draw series and grids.
 */
public class PlotArea extends Composite implements PaintListener, IPlotArea {

	/** the chart */
	private Chart chart;
	/** the custom paint listeners */
	private List<ICustomPaintListener> paintListeners;
	/** the default background color */
	private static final int DEFAULT_BACKGROUND = SWT.COLOR_WHITE;
	private DisposeListener disposeListener;
	private Image image = null;
	private boolean buffered = false;

	/**
	 * Constructor.
	 *
	 * @param chart
	 *            the chart
	 * @param style
	 *            the style
	 */
	public PlotArea(Chart chart, int style) {

		super(chart, style | SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
		this.chart = chart;
		paintListeners = new ArrayList<ICustomPaintListener>();
		setBackground(Display.getDefault().getSystemColor(DEFAULT_BACKGROUND));
		addPaintListener(this);
		disposeListener = new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {

				dispose();
			}
		};
		chart.addDisposeListener(disposeListener);
		chart.setPlotArea(this);
	}

	@Override
	public void dispose() {

		super.dispose();
		chart.removeDisposeListener(disposeListener);
	}

	@Override
	public Chart getChart() {

		return chart;
	}

	/**
	 * Gets the set of series.
	 * 
	 * @deprecated use the chart directly
	 * @return the set of series
	 */
	@Deprecated
	public ISeriesSet getSeriesSet() {

		// TODO if not used elsewhere we can make this private or even remove completely since it is only used in one place
		return chart.getSeriesSet();
	}

	@Override
	public Control getControl() {

		return this;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {

		super.setBounds(x, y, width, height);
		((SeriesSet)getSeriesSet()).compressAllSeries();
	}

	@Override
	public void setBackground(Color color) {

		if(color == null) {
			super.setBackground(Display.getDefault().getSystemColor(DEFAULT_BACKGROUND));
		} else {
			super.setBackground(color);
		}
	}

	@Override
	public void setBackgroundImage(Image image) {

		this.image = image;
	}

	@Override
	public void addCustomPaintListener(ICustomPaintListener listener) {

		paintListeners.add(listener);
	}

	@Override
	public void removeCustomPaintListener(ICustomPaintListener listener) {

		paintListeners.remove(listener);
	}

	@Override
	public void paintControl(PaintEvent e) {

		Point p = getSize();
		GC gc = e.gc;
		// draw the plot area background
		Color oldBackground = gc.getBackground();
		gc.setBackground(getBackground());
		gc.fillRectangle(0, 0, p.x, p.y);
		/*
		 * Draw the image centered if available.
		 */
		if(image != null) {
			e.gc.drawImage(image, 0, 0);
		}
		// draw grid
		for(IAxis axis : chart.getAxisSet().getAxes()) {
			((Grid)axis.getGrid()).draw(gc, p.x, p.y);
		}
		// draw behind series
		for(ICustomPaintListener listener : paintListeners) {
			if(listener.drawBehindSeries()) {
				listener.paintControl(e);
			}
		}
		// draw series. The line series should be drawn on bar series.
		for(ISeries<?> series : chart.getSeriesSet().getSeries()) {
			if(series instanceof IBarSeries) {
				((Series<?>)series).draw(gc, p.x, p.y);
			}
		}
		//
		for(ISeries<?> series : chart.getSeriesSet().getSeries()) {
			if(series instanceof ILineSeries) {
				((Series<?>)series).draw(gc, p.x, p.y);
			}
		}
		//
		for(ISeries<?> series : chart.getSeriesSet().getSeries()) {
			if(series instanceof ICircularSeries) {
				((Series<?>)series).draw(gc, p.x, p.y);
			}
		}
		// draw over series
		for(ICustomPaintListener listener : paintListeners) {
			if(!listener.drawBehindSeries()) {
				listener.paintControl(e);
			}
		}
		e.gc.setBackground(oldBackground);
	}

	@Override
	public ImageData getImageData() {

		ImageData imageData = null;
		Point chartSize = getSize();
		Image image = null;
		GC gc = null;
		//
		try {
			image = new Image(getDisplay(), new ImageData(chartSize.x, chartSize.y, 32, new PaletteData(0xFF, 0xFF00, 0xFF0000)));
			gc = new GC(this);
			gc.copyArea(image, 0, 0);
			imageData = image.getImageData();
		} finally {
			/*
			 * Dispose GC
			 */
			if(gc != null && !gc.isDisposed()) {
				gc.dispose();
			}
			/*
			 * Dispose Image
			 */
			if(image != null && !image.isDisposed()) {
				image.dispose();
			}
		}
		//
		return imageData;
	}

	@Override
	public boolean isBuffered() {

		return buffered;
	}

	@Override
	public void setBuffered(boolean buffered) {

		this.buffered = buffered;
	}
}
