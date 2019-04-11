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

import org.eclipse.swtchart.extensions.core.BaseChart;

public abstract class AbstractPositionPaintListener extends AbstractBaseChartPaintListener implements IPositionPaintListener {

	private int x = -1;
	private int y = -1;

	public AbstractPositionPaintListener(BaseChart baseChart) {
		super(baseChart);
	}

	@Override
	public void setActualPosition(int x, int y) {

		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {

		return x;
	}

	@Override
	public int getY() {

		return y;
	}
}
