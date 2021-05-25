/*******************************************************************************
 * Copyright (c) 2020, 2021 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - original API and implementation
 * Philip Wenig - extends series settings
 *******************************************************************************/
package org.eclipse.swtchart.extensions.piecharts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.extensions.core.AbstractSeriesSettings;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;

public class CircularSeriesSettings extends AbstractSeriesSettings implements ICircularSeriesSettings {

	private Color borderColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private int borderWidth = 1;
	private int highlightLineWidth = 2;
	private int borderStyle = SWT.LINE_SOLID;
	private SeriesType seriesType = SeriesType.PIE;
	private boolean redrawOnClick = true;
	private boolean fillEntireSpace = false;
	private ICircularSeriesSettings seriesSettingsHighlight = null;

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

		this.seriesType = type;
	}

	@Override
	public SeriesType getSeriesType() {

		return seriesType;
	}

	@Override
	public void setRedrawOnClick(boolean redraw) {

		this.redrawOnClick = redraw;
	}

	@Override
	public boolean isRedrawOnClick() {

		return redrawOnClick;
	}

	@Override
	public void setHighlightLineWidth(int width) {

		this.highlightLineWidth = width;
	}

	@Override
	public int getHighlightLineWidth() {

		return highlightLineWidth;
	}

	@Override
	public void setFillEntireSpace(boolean fillEntireSpace) {

		this.fillEntireSpace = fillEntireSpace;
	}

	@Override
	public boolean isEntireSpaceFilled() {

		return fillEntireSpace;
	}

	@Override
	public ISeriesSettings getSeriesSettingsHighlight() {

		if(seriesSettingsHighlight == null) {
			try {
				seriesSettingsHighlight = (ICircularSeriesSettings)this.clone();
			} catch(CloneNotSupportedException e) {
				seriesSettingsHighlight = new CircularSeriesSettings();
			}
		}
		return seriesSettingsHighlight;
	}

	@Override
	public ISeriesSettings makeDeepCopy() {

		ICircularSeriesSettings circularSeriesSettings = new CircularSeriesSettings();
		transfer(circularSeriesSettings);
		return circularSeriesSettings;
	}

	@Override
	public boolean transfer(ISeriesSettings seriesSettingsSink) {

		boolean success = false;
		if(seriesSettingsSink instanceof ICircularSeriesSettings) {
			ICircularSeriesSettings source = this;
			ICircularSeriesSettings sink = (ICircularSeriesSettings)seriesSettingsSink;
			sink.setDescription(source.getDescription());
			sink.setBorderColor(source.getBorderColor());
			sink.setBorderWidth(source.getBorderWidth());
			sink.setHighlightLineWidth(source.getHighlightLineWidth());
			sink.setBorderStyle(source.getBorderStyle());
			sink.setSeriesType(source.getSeriesType());
			sink.setRedrawOnClick(source.isRedrawOnClick());
			sink.setFillEntireSpace(source.isEntireSpaceFilled());
			success = true;
		}
		//
		return success;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		ICircularSeriesSettings circularSeriesSettings = new CircularSeriesSettings();
		transfer(circularSeriesSettings);
		return circularSeriesSettings;
	}
}
