/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;

public interface IPointSeriesSettings extends ISeriesSettings {

	PlotSymbolType getSymbolType();

	/**
	 * PlotSymbolType.CIRCLE
	 * PlotSymbolType.SQUARE
	 * PlotSymbolType.DIAMOND
	 * PlotSymbolType.TRIANGLE
	 * PlotSymbolType.INVERTED_TRIANGLE
	 * PlotSymbolType.CROSS
	 * PlotSymbolType.PLUS
	 * PlotSymbolType.NONE
	 * 
	 * @param symbolType
	 * @return
	 */
	void setSymbolType(PlotSymbolType symbolType);

	int getSymbolSize();

	void setSymbolSize(int symbolSize);

	Color getSymbolColor();

	void setSymbolColor(Color symbolColor);
}
