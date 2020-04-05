package org.eclipse.swtchart.internal.series;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IPieSeries;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.internal.axis.Axis;
import org.eclipse.swtchart.internal.compress.CompressPieSeries;

public class PieSeries extends Series implements IPieSeries{
	Chart chart;
	
	public PieSeries(Chart chart, String id) {
		
		super(chart, id);
		this.chart = chart;
		type = SeriesType.PIE;
		initialise();
		compressor = new CompressPieSeries();
	}

	private void initialise() {

		Color backgroundColor = chart.getPlotArea().getBackground();
		IAxis[] axes = chart.getAxisSet().getAxes();
		for(IAxis axis : axes) {
			axis.getTick().setVisible(false);
			axis.getGrid().setVisible(false);
			axis.getTitle().setVisible(false);
		}
		
	}

	@Override
	public Range getAdjustedRange(Axis axis, int length) {

		return null;
	}

	@Override
	protected void setCompressor() {

		compressor = new CompressPieSeries();
	}

	@Override
	protected void draw(GC gc, int width, int height, Axis xAxis, Axis yAxis) {

		setBothAxisRange(width,height,xAxis,yAxis);
		int xStart = xAxis.getPixelCoordinate(-1), yStart = yAxis.getPixelCoordinate(1);
		int xWidth = xAxis.getPixelCoordinate(1)-xStart, yWidth = yAxis.getPixelCoordinate(-1)-yStart;
		String[] labels = ((CompressPieSeries)compressor).getLabelSeries();
		double[] values = ((CompressPieSeries)compressor).getValueSeries();
		Color[] colors = ((CompressPieSeries)compressor).getColors();
		Point[] bounds = getAngleBounds(values);
		
		gc.drawArc(xStart, yStart, xStart + xWidth -1, yStart + yWidth -1, 0, 360);
		for(int i=0;i!=bounds.length;i++) {
			gc.setBackground(colors[i]);
			
			gc.fillArc(xStart, yStart, xWidth -1, yWidth -1, bounds[i].x, bounds[i].y);
		}
	}

	/** 
	 * gets the start angle and angle width for each value in series.
	 * 
	 * @param values
	 * @return Point[], where x is the start angle, y is the anglular width in degrees.
	 */
	private Point[] getAngleBounds(double[] values) {

		int start = 0, width;
		double total =0,required = 0;
		Point[]bounds = new Point[values.length];
		for(double val : values) {
			total+=val;
		}
		for(int i=0;i!=values.length;i++) {
			width = (int)(((values[i]*360)/total));
			if(width == 0) {
				width = 1;
				required-=1;
			} 
			else {
				double diff = ((values[i]*360)/total)-width;
				required+=diff;
				if(required>0.999) {
					width++;
					required-=1.0;
				}
			}
			Point p = new Point(start,width);
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

		xAxis.setRange(new Range(-1,1));
		yAxis.setRange(new Range(-1,1));
		if(width>height) {
			if(xAxis.isHorizontalAxis()) {
				double ratio = (double)(2*width/(double)height);
				xAxis.setRange(new Range(-1,ratio-1));
			} else {
				double ratio = (double)(2*width/(double)height);
				yAxis.setRange(new Range(-1,ratio-1));
			}
		}else {
			if(xAxis.isHorizontalAxis()) {
				double ratio = (double)(2*height/(double)width);
				yAxis.setRange(new Range(1-ratio,1));
			} else {
				double ratio = (double)(2*height/(double)width);
				xAxis.setRange(new Range(1-ratio,1));
			}
		}
	}
}
