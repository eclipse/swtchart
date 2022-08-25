/*******************************************************************************
 * Copyright (c) 2017, 2022 Lablicate GmbH.
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
import org.eclipse.swtchart.ICustomPaintListener;
import org.eclipse.swtchart.extensions.core.IMouseSupport;

public interface IBaseChartPaintListener extends ICustomPaintListener {

	IMouseSupport getBaseChart();

	void setForegroundColor(Color foregroundColor);

	void setBackgroundColor(Color backgroundColor);

	boolean isDraw();

	void setDraw(boolean draw);
}
