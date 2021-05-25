/*******************************************************************************
 * Copyright (c) 2020, 2021 SWTChart project.
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
import org.eclipse.swtchart.extensions.core.ISeriesSettings;

public interface ICircularSeriesSettings extends ISeriesSettings {

	void setBorderColor(Color color);

	void setBorderWidth(int width);

	void setBorderStyle(int borderStyle);

	Color getBorderColor();

	int getBorderWidth();

	int getBorderStyle();

	void setSeriesType(SeriesType type);

	SeriesType getSeriesType();

	void setRedrawOnClick(boolean redraw);

	boolean isRedrawOnClick();

	void setFillEntireSpace(boolean fillEntireSpace);

	boolean isEntireSpaceFilled();

	void setHighlightLineWidth(int width);

	int getHighlightLineWidth();
}
