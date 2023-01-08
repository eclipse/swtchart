/*******************************************************************************
 * Copyright (c) 2020, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - original API and implementation
 * Philip Wenig - extends series settings
 *******************************************************************************/
package org.eclipse.swtchart.extensions.piecharts;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;

public interface ICircularSeriesSettings extends ISeriesSettings {

	Color getSliceColor();

	void setSliceColor(Color sliceColor);

	Color getBorderColor();

	void setBorderColor(Color borderColor);

	int getBorderWidth();

	void setBorderWidth(int borderWidth);

	LineStyle getBorderStyle();

	void setBorderStyle(LineStyle borderStyle);

	void setSeriesType(SeriesType type);

	SeriesType getSeriesType();

	void setRedrawOnClick(boolean redraw);

	boolean isRedrawOnClick();

	void setFillEntireSpace(boolean fillEntireSpace);

	boolean isEntireSpaceFilled();
}