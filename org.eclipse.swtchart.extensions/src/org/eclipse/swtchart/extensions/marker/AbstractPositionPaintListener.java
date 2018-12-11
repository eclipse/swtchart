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
