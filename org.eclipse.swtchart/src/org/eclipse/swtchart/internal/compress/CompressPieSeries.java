package org.eclipse.swtchart.internal.compress;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class CompressPieSeries extends Compress{

	String[] labels;
	double[] values;
	private int[] colours = {SWT.COLOR_BLUE,SWT.COLOR_CYAN,SWT.COLOR_DARK_BLUE,SWT.COLOR_DARK_CYAN
			,SWT.COLOR_DARK_GRAY,SWT.COLOR_DARK_MAGENTA,SWT.COLOR_DARK_RED,SWT.COLOR_DARK_YELLOW
			,SWT.COLOR_GRAY,SWT.COLOR_GREEN,SWT.COLOR_MAGENTA,SWT.COLOR_RED,SWT.COLOR_YELLOW
			,SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW};
	private Color[] colors ;
	
	@Override
	protected void addNecessaryPlots(ArrayList<Double> xList, ArrayList<Double> yList, ArrayList<Integer> indexList) {

	}
	
	public void setLabelSeries(String[] labels) {
		String[] ids = new String[labels.length];
		System.arraycopy(labels, 0, ids, 0, labels.length);
		this.labels = ids;
		setColors();
	}
	
	public void setValueSeries(double[] values) {
		double[] val = new double[values.length];
		System.arraycopy(values, 0, val, 0, values.length);
		this.values = val;
	}
	
	public void setColors() {
		colors = new Color[labels.length];
		int color = colours.length;
		colors[0] = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		for(int i =1;i!=labels.length;i++) {
			int colour = i%color;
			colors[i] = Display.getDefault().getSystemColor(colours[i]);
		}
	}
	
	public String[] getLabelSeries() {
		return labels;
	}
	
	public double[] getValueSeries() {
		return values;
	}
	
	public Color[] getColors() {
		return colors;
	}
}
