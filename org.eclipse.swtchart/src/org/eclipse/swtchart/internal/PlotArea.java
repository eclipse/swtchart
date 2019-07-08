/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IBarSeries;
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
	protected Chart chart;
	/** the custom paint listeners */
	List<ICustomPaintListener> paintListeners;
	/** the default background color */
	private static final int DEFAULT_BACKGROUND = SWT.COLOR_WHITE;
	private DisposeListener disposeListener;

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

	/*
	 * @see Control#setBounds(int, int, int, int)
	 */
	@Override
	public void setBounds(int x, int y, int width, int height) {

		super.setBounds(x, y, width, height);
		((SeriesSet)getSeriesSet()).compressAllSeries();
	}

	/*
	 * @see Control#setBackground(Color)
	 */
	@Override
	public void setBackground(Color color) {

		if(color == null) {
			super.setBackground(Display.getDefault().getSystemColor(DEFAULT_BACKGROUND));
		} else {
			super.setBackground(color);
		}
	}

	/*
	 * @see IPlotArea#addCustomPaintListener(ICustomPaintListener)
	 */
	@Override
	public void addCustomPaintListener(ICustomPaintListener listener) {

		paintListeners.add(listener);
	}

	/*
	 * @see IPlotArea#removeCustomPaintListener(ICustomPaintListener)
	 */
	@Override
	public void removeCustomPaintListener(ICustomPaintListener listener) {

		paintListeners.remove(listener);
	}

	/*
	 * @see PaintListener#paintControl(PaintEvent)
	 */
	@Override
	public void paintControl(PaintEvent e) {

		Point p = getSize();
		GC gc = e.gc;
		// draw the plot area background
		Color oldBackground = gc.getBackground();
		gc.setBackground(getBackground());
		gc.fillRectangle(0, 0, p.x, p.y);
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
		for(ISeries series : chart.getSeriesSet().getSeries()) {
			if(series instanceof IBarSeries) {
				((Series)series).draw(gc, p.x, p.y);
			}
		}
		for(ISeries series : chart.getSeriesSet().getSeries()) {
			if(series instanceof ILineSeries) {
				((Series)series).draw(gc, p.x, p.y);
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
}
