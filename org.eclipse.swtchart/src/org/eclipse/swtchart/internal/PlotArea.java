/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
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
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
	private DisposeListener disposeListener;
	//
	private Image image = null;
	private int imagePositionX = 0;
	private int imagePositionY = 0;
	//
	private String text = "";
	private Font fontText = Display.getDefault().getSystemFont();
	private Color colorText = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private int textPositionX = 0;
	private int textPositionY = 0;
	//
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
		paintListeners = new ArrayList<>();
		addPaintListener(this);
		disposeListener = new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {

				dispose();
			}
		};
		setData("org.eclipse.e4.ui.css.CssClassName", "PlotArea");
		chart.addDisposeListener(disposeListener);
		chart.setPlotArea(this);
	}

	@Override
	public void dispose() {

		chart.removeDisposeListener(disposeListener);
		super.dispose();
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
	public void setBackgroundImage(Image image) {

		setBackgroundImage(image, 0, 0);
	}

	@Override
	public void setBackgroundImage(Image image, int x, int y) {

		this.image = image;
		this.imagePositionX = x;
		this.imagePositionY = y;
	}

	@Override
	public void setBackgroundText(String text, Font font, Color color) {

		setBackgroundText(text, font, color, 0, 0);
	}

	@Override
	public void setBackgroundText(String text, Font font, Color color, int x, int y) {

		this.text = text != null ? text : "";
		this.fontText = font != null ? font : Display.getDefault().getSystemFont();
		this.colorText = color != null ? color : Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		this.textPositionX = x;
		this.textPositionY = y;
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
	public List<ICustomPaintListener> getCustomPaintListener() {

		return Collections.unmodifiableList(paintListeners);
	}

	@Override
	public void paintControl(PaintEvent e) {

		Point sizePlotArea = getSize();
		GC gc = e.gc;
		/*
		 * Draw the plot area background
		 */
		Color oldBackground = gc.getBackground();
		gc.setBackground(getBackground());
		gc.fillRectangle(0, 0, sizePlotArea.x, sizePlotArea.y);
		/*
		 * Background Image / Text
		 */
		Font fontDefault = gc.getFont();
		Color colorDefault = gc.getForeground();
		//
		gc.setFont(fontText);
		gc.setForeground(colorText);
		if(image != null) {
			int offsetY = drawImage(gc, sizePlotArea, image, imagePositionX, imagePositionY);
			drawText(gc, sizePlotArea, text, textPositionX, textPositionY, offsetY);
		} else {
			drawText(gc, sizePlotArea, text, textPositionX, textPositionY, 0);
		}
		//
		gc.setForeground(colorDefault);
		gc.setFont(fontDefault);
		/*
		 * Draw Grid
		 */
		for(IAxis axis : chart.getAxisSet().getAxes()) {
			((Grid)axis.getGrid()).draw(gc, sizePlotArea.x, sizePlotArea.y);
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
				((Series<?>)series).draw(gc, sizePlotArea.x, sizePlotArea.y);
			}
		}
		//
		for(ISeries<?> series : chart.getSeriesSet().getSeries()) {
			if(series instanceof ILineSeries) {
				((Series<?>)series).draw(gc, sizePlotArea.x, sizePlotArea.y);
			}
		}
		//
		for(ISeries<?> series : chart.getSeriesSet().getSeries()) {
			if(series instanceof ICircularSeries) {
				((Series<?>)series).draw(gc, sizePlotArea.x, sizePlotArea.y);
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

	private int drawImage(GC gc, Point sizePlotArea, Image image, int imagePositionX, int imagePositionY) {

		/*
		 * Position exact or center
		 */
		Rectangle rectangle = image.getBounds();
		int x = imagePositionX;
		int y = imagePositionY;
		int offset = rectangle.height;
		//
		if(imagePositionX == POSITION_CENTER_X && imagePositionY == POSITION_CENTER_Y) {
			x = (int)(sizePlotArea.x / 2.0d - rectangle.width / 2.0d);
			y = (int)(sizePlotArea.y / 2.0d - rectangle.height / 2.0d);
			offset = (int)(rectangle.height / 2.0d);
		}
		/*
		 * Draw
		 */
		gc.drawImage(image, x, y);
		return offset;
	}

	private void drawText(GC gc, Point sizePlotArea, String text, int textPositionX, int textPositionY, int offsetY) {

		if(!text.isEmpty()) {
			/*
			 * Position exact or center
			 */
			int x = textPositionX;
			int y = textPositionY;
			Point sizeText = gc.textExtent(text);
			//
			if(textPositionX == POSITION_CENTER_X && textPositionY == POSITION_CENTER_Y) {
				x = (int)(sizePlotArea.x / 2.0d - sizeText.x / 2.0d);
				y = (int)(sizePlotArea.y / 2.0d - sizeText.y);
			}
			/*
			 * Offset
			 */
			if(offsetY != 0) {
				y += offsetY;
				y += (sizeText.y * 1.5d);
			}
			/*
			 * Draw
			 */
			gc.drawString(text, x, y);
		}
	}
}