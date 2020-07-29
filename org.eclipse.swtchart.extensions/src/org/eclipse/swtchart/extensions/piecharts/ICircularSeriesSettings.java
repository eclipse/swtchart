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
 * Himanshu Balasamanta Orignal API and implementation
 *******************************************************************************/
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

	public void setRedrawOnClick(boolean redraw);

	public boolean isRedrawOnClick();

	public void setHighlightLineWidth(int width);

	public int getHighlightLineWidth();
}
