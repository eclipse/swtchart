package org.eclipse.swtchart;

import org.eclipse.swt.graphics.Color;

public interface IPieSeries {
	
	public String[] getLabelSeries();
	
	public double[] getValueSeries();
	
	public Color[] getColors();
}
