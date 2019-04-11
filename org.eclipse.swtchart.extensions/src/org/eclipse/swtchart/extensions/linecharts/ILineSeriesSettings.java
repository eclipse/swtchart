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
package org.eclipse.swtchart.extensions.linecharts;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.IPointSeriesSettings;

public interface ILineSeriesSettings extends IPointSeriesSettings {

	int getAntialias();

	/**
	 * SWT.DEFAULT, SWT.ON, SWT.OFF
	 * 
	 * @param antialias
	 */
	void setAntialias(int antialias);

	boolean isEnableArea();

	void setEnableArea(boolean enableArea);

	Color getLineColor();

	void setLineColor(Color lineColor);

	int getLineWidth();

	void setLineWidth(int lineWidth);

	boolean isEnableStack();

	void setEnableStack(boolean enableStack);

	boolean isEnableStep();

	void setEnableStep(boolean enableStep);

	LineStyle getLineStyle();

	void setLineStyle(LineStyle lineStyle);
}