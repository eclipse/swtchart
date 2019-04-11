/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
