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
			//axis.getTick().setForeground(backgroundColor);
			axis.getGrid().setForeground(backgroundColor);
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
		int total =0;
		for(double val:values)total+=val;
		for(int i=0;i!=bounds.length;i++) {
			gc.setBackground(colors[i]);
			System.out.println(bounds[i].y/(double)360 + " " + values[i]/(double)total);
			gc.fillArc(xStart, yStart, xWidth, yWidth, bounds[i].x, bounds[i].y);
		}
	}

	private Point[] getAngleBounds(double[] values) {

		int start = 0, width;
		double total =0;
		double frac;
		Point[]bounds = new Point[values.length];
		for(double val : values) {
			total+=val;
		}
		System.out.println(total);
		for(int i=0;i!=values.length;i++) {
			width = (int)((values[i]/total)*360);
			Point p = new Point(start,width);
			start += width;
			bounds[i] = p;
		}
		return bounds;
	}

	private void setBothAxisRange(int width, int height, Axis xAxis, Axis yAxis) {

		xAxis.setRange(new Range(-1,1));
		yAxis.setRange(new Range(-1,1));
		if(width>height) {
			if(xAxis.isHorizontalAxis()) {
				double ratio = (double)(width/(double)height);
				xAxis.setRange(new Range(-1,ratio));
			} else {
				double ratio = (double)(width/(double)height);
				yAxis.setRange(new Range(-1,ratio));
			}
		}else {
			if(xAxis.isHorizontalAxis()) {
				double ratio = (double)(height/(double)width);
				yAxis.setRange(new Range(-1*ratio,1));
			} else {
				double ratio = (double)(height/(double)width);
				xAxis.setRange(new Range(-1*ratio,1));
			}
		}
	}
}
