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
package org.eclipse.swtchart.extensions.linecharts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.core.AbstractPointSeriesSettings;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.LineStyle;

public class LineSeriesSettings extends AbstractPointSeriesSettings implements ILineSeriesSettings {

	private int antialias;
	private boolean enableArea;
	private Color lineColor;
	private int lineWidth;
	private boolean enableStack;
	private boolean enableStep;
	private LineStyle lineStyle;
	private ILineSeriesSettings seriesSettingsHighlight = null;

	public LineSeriesSettings() {
		antialias = SWT.DEFAULT;
		enableArea = true;
		lineColor = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		lineWidth = 1;
		enableStack = false;
		enableStep = false;
		lineStyle = LineStyle.SOLID;
	}

	@Override
	public int getAntialias() {

		return antialias;
	}

	@Override
	public void setAntialias(int antialias) {

		this.antialias = antialias;
	}

	@Override
	public boolean isEnableArea() {

		return enableArea;
	}

	@Override
	public void setEnableArea(boolean enableArea) {

		this.enableArea = enableArea;
	}

	@Override
	public Color getLineColor() {

		return lineColor;
	}

	@Override
	public void setLineColor(Color lineColor) {

		this.lineColor = lineColor;
	}

	@Override
	public int getLineWidth() {

		return lineWidth;
	}

	@Override
	public void setLineWidth(int lineWidth) {

		this.lineWidth = lineWidth;
	}

	@Override
	public boolean isEnableStack() {

		return enableStack;
	}

	@Override
	public void setEnableStack(boolean enableStack) {

		this.enableStack = enableStack;
	}

	@Override
	public boolean isEnableStep() {

		return enableStep;
	}

	@Override
	public void setEnableStep(boolean enableStep) {

		this.enableStep = enableStep;
	}

	@Override
	public LineStyle getLineStyle() {

		return lineStyle;
	}

	@Override
	public void setLineStyle(LineStyle lineStyle) {

		this.lineStyle = lineStyle;
	}

	@Override
	public ISeriesSettings getSeriesSettingsHighlight() {

		if(seriesSettingsHighlight == null) {
			try {
				seriesSettingsHighlight = (ILineSeriesSettings)this.clone();
			} catch(CloneNotSupportedException e) {
				seriesSettingsHighlight = new LineSeriesSettings();
			}
		}
		return seriesSettingsHighlight;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		ILineSeriesSettings lineSeriesSettings = new LineSeriesSettings();
		lineSeriesSettings.setDescription(this.getDescription());
		lineSeriesSettings.setVisible(this.isVisible());
		lineSeriesSettings.setVisibleInLegend(this.isVisibleInLegend());
		lineSeriesSettings.setAntialias(this.getAntialias());
		lineSeriesSettings.setEnableArea(this.isEnableArea());
		lineSeriesSettings.setSymbolType(this.getSymbolType());
		lineSeriesSettings.setSymbolSize(this.getSymbolSize());
		lineSeriesSettings.setSymbolColor(this.getSymbolColor());
		lineSeriesSettings.setLineColor(this.getLineColor());
		lineSeriesSettings.setLineWidth(this.getLineWidth());
		lineSeriesSettings.setEnableStack(this.isEnableStack());
		lineSeriesSettings.setEnableStep(this.isEnableStep());
		lineSeriesSettings.setLineStyle(this.getLineStyle());
		return lineSeriesSettings;
	}
}
