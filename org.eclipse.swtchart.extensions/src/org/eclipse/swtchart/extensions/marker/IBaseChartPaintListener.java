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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.ICustomPaintListener;

public interface IBaseChartPaintListener extends ICustomPaintListener {

	BaseChart getBaseChart();

	void setForegroundColor(Color foregroundColor);

	boolean isDraw();

	void setDraw(boolean draw);
}
