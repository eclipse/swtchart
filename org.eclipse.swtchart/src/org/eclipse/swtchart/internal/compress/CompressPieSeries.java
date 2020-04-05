package org.eclipse.swtchart.internal.compress;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class CompressPieSeries extends Compress{

	String[] labels;
	double[] values;
	Color[] colors;
	
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
		for(int i =0;i!=labels.length;i++) {
			int colorNo = i%11;
			switch (colorNo) {
				case 0:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
					break;
				case 1:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW);
					break;
				case 2:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_RED);
					break;
				case 3:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_DARK_CYAN);
					break;
				case 4:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE);
					break;
				case 5:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
					break;
				case 6:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_DARK_MAGENTA);
					break;
				case 7:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
					break;
				case 8:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
					break;
				case 9:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
					break;
				case 10:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
					break;
				default:
					colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
					break;
			}
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
