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
 * Himanshu Balasamanta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.internal.series;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IPieSeries;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.internal.compress.CompressPieSeries;
import org.eclipse.swtchart.model.CartesianSeriesModel;
import org.eclipse.swtchart.model.StringArraySeriesModel;

public class PieSeries extends Series implements IPieSeries {

	private Chart chart;
	private StringArraySeriesModel model;

	public PieSeries(Chart chart, String id) {

		super(chart, id);
		this.chart = chart;
		type = SeriesType.PIE;
		initialise();
		compressor = new CompressPieSeries();
	}

	@Override
	public Range getAdjustedRange(Axis axis, int length) {

		return null;
	}

	@Override
	public CartesianSeriesModel getDataModel() {

		return model;
	}

	@Override
	public String[] getLabelSeries() {

		StringArraySeriesModel stringArraySeriesModel = (StringArraySeriesModel)getDataModel();
		if(stringArraySeriesModel == null)
			return null;
		String[] labels = stringArraySeriesModel.getLabels();
		String[] ids = new String[labels.length];
		System.arraycopy(labels, 0, ids, 0, labels.length);
		return ids;
	}

	@Override
	public double[] getValueSeries() {

		StringArraySeriesModel stringArraySeriesModel = (StringArraySeriesModel)getDataModel();
		double[] values = stringArraySeriesModel.getValues();
		double[] val = new double[values.length];
		System.arraycopy(values, 0, val, 0, values.length);
		return val;
	}

	@Override
	public Color[] getColors() {

		return ((CompressPieSeries)compressor).getColors();
	}

	@Override
	public void setColor(String label, Color color) {

		Color[] colors = ((CompressPieSeries)compressor).getColors();
		String[] labels = getLabelSeries();
		for(int i = 0; i != labels.length; i++) {
			if(labels[i] == label) {
				colors[i] = color;
				break;
			}
		}
		((CompressPieSeries)compressor).setColors(colors);
	}

	public void setColor(Color[] colors) {

		((CompressPieSeries)compressor).setColors(colors);
	}

	@Override
	public void setSeries(String[] labels, double[] values) {

		if(labels == null || values == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
			return; // to suppress warning...
		}
		String[] ids = new String[labels.length];
		System.arraycopy(labels, 0, ids, 0, labels.length);
		double[] val = new double[values.length];
		System.arraycopy(values, 0, val, 0, values.length);
		StringArraySeriesModel data = new StringArraySeriesModel(ids, val);
		this.setDataModel(data);
	}

	@Override
	protected void setCompressor() {

		compressor = new CompressPieSeries();
	}

	@Override
	protected void draw(GC gc, int width, int height, Axis xAxis, Axis yAxis) {

		setBothAxisRange(width, height, xAxis, yAxis);
		int xStart = xAxis.getPixelCoordinate(-1);
		int yStart = yAxis.getPixelCoordinate(1);
		int xWidth = xAxis.getPixelCoordinate(1) - xStart;
		int yWidth = yAxis.getPixelCoordinate(-1) - yStart;
		int xZero = xAxis.getPixelCoordinate(0);
		int yZero = yAxis.getPixelCoordinate(0);
		double[] values = ((CompressPieSeries)compressor).getValueSeries();
		Color[] colors = ((CompressPieSeries)compressor).getColors();
		Point[] bounds = getAngleBounds(values);
		gc.setLineWidth(1);
		for(int i = 0; i != bounds.length; i++) {
			// filling the background
			gc.setBackground(colors[i]);
			gc.fillArc(xStart + 1, yStart + 1, xWidth - 1, yWidth - 1, bounds[i].x, bounds[i].y);
			// setting border between data elements
			double xCoordinate = Math.cos(Math.toRadians(bounds[i].x));
			double yCoordinate = -1 * Math.sin(Math.toRadians(bounds[i].x));
			int xPixelCoordinate = xAxis.getPixelCoordinate(xCoordinate);
			int yPixelCoordinate = xAxis.getPixelCoordinate(yCoordinate);
			gc.drawLine(xZero, yZero, xPixelCoordinate, yPixelCoordinate);
		}
		// draw boundary of PieChart
		gc.setLineWidth(2);
		gc.drawArc(xStart + 1, yStart + 1, xStart + xWidth - 1, yStart + yWidth - 1, 0, 360);
	}

	/**
	 * gets the start angle and angle width for each value in series.
	 * 
	 * @param values
	 * @return Point[], where x is the start angle, y is the anglular width in degrees.
	 */
	private Point[] getAngleBounds(double[] values) {

		int start = 0, width;
		double total = 0, required = 0;
		Point[] bounds = new Point[values.length];
		for(double val : values) {
			total += val;
		}
		for(int i = 0; i != values.length; i++) {
			width = (int)(((values[i] * 360) / total));
			if(width == 0) {
				width = 1;
				required -= 1;
			} else {
				double diff = ((values[i] * 360) / total) - width;
				required += diff;
				if(required > 0.999) {
					width++;
					required -= 1.0;
				}
			}
			Point p = new Point(start, width);
			start += width;
			bounds[i] = p;
		}
		return bounds;
	}

	/**
	 * sets the range of the axis so that the pie to be drawn is drawn within the range
	 * -1 to 1 in both X and Y axis.
	 * This shall be changed when multi-level pie-chart will be implemented to enable
	 * ease in multi-layer drawing.
	 * 
	 * @param width
	 * @param height
	 * @param xAxis
	 * @param yAxis
	 */
	private void setBothAxisRange(int width, int height, Axis xAxis, Axis yAxis) {

		xAxis.setRange(new Range(-1, 1));
		yAxis.setRange(new Range(-1, 1));
		if(width > height) {
			if(xAxis.isHorizontalAxis()) {
				double ratio = (double)(2 * width / (double)height);
				xAxis.setRange(new Range(-1, ratio - 1));
			} else {
				double ratio = (double)(2 * width / (double)height);
				yAxis.setRange(new Range(-1, ratio - 1));
			}
		} else {
			if(xAxis.isHorizontalAxis()) {
				double ratio = (double)(2 * height / (double)width);
				yAxis.setRange(new Range(1 - ratio, 1));
			} else {
				double ratio = (double)(2 * height / (double)width);
				xAxis.setRange(new Range(1 - ratio, 1));
			}
		}
	}

	@Override
	public void setDataModel(CartesianSeriesModel model) {

		this.model = (StringArraySeriesModel)model;
		setCompressor();
		if(compressor instanceof CompressPieSeries) {
			((CompressPieSeries)compressor).setLabelSeries(getLabelSeries());
			((CompressPieSeries)compressor).setValueSeries(getValueSeries());
		}
	}

	private void initialise() {

		IAxis[] axes = chart.getAxisSet().getAxes();
		for(IAxis axis : axes) {
			axis.getTick().setVisible(false);
			axis.getGrid().setVisible(false);
			axis.getTitle().setVisible(false);
		}
	}
}
