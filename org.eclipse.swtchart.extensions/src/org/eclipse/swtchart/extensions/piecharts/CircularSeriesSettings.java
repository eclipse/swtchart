/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.piecharts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.extensions.core.AbstractSeriesSettings;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;

public class CircularSeriesSettings extends AbstractSeriesSettings implements ICircularSeriesSettings {

	private Color borderColor;
	private int borderWidth;
	private int borderStyle;
	private SeriesType type;

	public CircularSeriesSettings() {

		borderColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		borderWidth = 1;
		borderStyle = SWT.LINE_SOLID;
		type = SeriesType.PIE;
	}

	@Override
	public ISeriesSettings getSeriesSettingsHighlight() {

		return null;
	}

	@Override
	public Color getBorderColor() {

		return borderColor;
	}

	@Override
	public int getBorderWidth() {

		return borderWidth;
	}

	@Override
	public int getBorderStyle() {

		return borderStyle;
	}

	@Override
	public void setBorderColor(Color color) {

		this.borderColor = color;
	}

	@Override
	public void setBorderWidth(int borderWidth) {

		this.borderWidth = borderWidth;
	}

	@Override
	public void setBorderStyle(int borderStyle) {

		this.borderStyle = borderStyle;
	}

	@Override
	public void setSeriesType(SeriesType type) {

		this.type = type;
	}

	@Override
	public SeriesType getSeriesType() {

		return type;
	}
}
