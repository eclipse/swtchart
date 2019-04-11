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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;

public abstract class AbstractPointSeriesSettings extends AbstractSeriesSettings implements IPointSeriesSettings {

	private PlotSymbolType symbolType;
	private int symbolSize;
	private Color symbolColor;

	public AbstractPointSeriesSettings() {
		symbolType = PlotSymbolType.NONE;
		symbolSize = 8;
		symbolColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	public PlotSymbolType getSymbolType() {

		return symbolType;
	}

	@Override
	public void setSymbolType(PlotSymbolType symbolType) {

		this.symbolType = symbolType;
	}

	@Override
	public int getSymbolSize() {

		return symbolSize;
	}

	@Override
	public void setSymbolSize(int symbolSize) {

		this.symbolSize = symbolSize;
	}

	@Override
	public Color getSymbolColor() {

		return symbolColor;
	}

	@Override
	public void setSymbolColor(Color symbolColor) {

		this.symbolColor = symbolColor;
	}
}
