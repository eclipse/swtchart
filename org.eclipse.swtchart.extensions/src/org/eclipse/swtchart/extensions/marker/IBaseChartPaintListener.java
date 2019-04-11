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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.ICustomPaintListener;

public interface IBaseChartPaintListener extends ICustomPaintListener {

	BaseChart getBaseChart();

	void setForegroundColor(Color foregroundColor);

	boolean isDraw();

	void setDraw(boolean draw);
}
