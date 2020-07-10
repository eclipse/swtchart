package org.eclipse.swtchart.extensions.piecharts;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.ISeries.SeriesType;

public interface ICircularSeriesSettings {

	public void setBorderColor(Color color);

	public void setBorderWidth(int width);

	public void setBorderStyle(int borderStyle);

	public Color getBorderColor();

	public int getBorderWidth();

	public int getBorderStyle();

	public void setSeriesType(SeriesType type);

	public SeriesType getSeriesType();

	public void setDescription(String id);

	public String getDescription();
}
