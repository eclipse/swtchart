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
package org.eclipse.swtchart.extensions.marker;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.extensions.core.BaseChart;

public abstract class AbstractBaseChartPaintListener implements IBaseChartPaintListener {

	private BaseChart baseChart;
	private Color foregroundColor;
	private boolean draw = true;

	public AbstractBaseChartPaintListener(BaseChart baseChart) {
		this.baseChart = baseChart;
		foregroundColor = baseChart.getDisplay().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	public BaseChart getBaseChart() {

		return baseChart;
	}

	@Override
	public boolean drawBehindSeries() {

		return false;
	}

	@Override
	public void setForegroundColor(Color foregroundColor) {

		this.foregroundColor = foregroundColor;
	}

	protected Color getForegroundColor() {

		return foregroundColor;
	}

	@Override
	public boolean isDraw() {

		return draw;
	}

	@Override
	public void setDraw(boolean draw) {

		this.draw = draw;
	}
}
