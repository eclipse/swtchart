/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.linecharts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.core.AbstractPointSeriesSettings;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;

public class LineSeriesSettings extends AbstractPointSeriesSettings implements ILineSeriesSettings {

	private int antialias = SWT.DEFAULT;
	private boolean enableArea = true;
	private boolean areaStrict = false; // Experimental
	private Color lineColor = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private int lineWidth = 1;
	private boolean enableStack = false;
	private boolean enableStep = false;
	private LineStyle lineStyle = LineStyle.SOLID;
	private ILineSeriesSettings seriesSettingsHighlight = null;

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
	public boolean isAreaStrict() {

		return areaStrict;
	}

	@Override
	public void setAreaStrict(boolean areaStrict) {

		this.areaStrict = areaStrict;
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
				seriesSettingsHighlight.setHighlight(true);
			} catch(CloneNotSupportedException e) {
				seriesSettingsHighlight = new LineSeriesSettings();
			}
		}
		return seriesSettingsHighlight;
	}

	@Override
	public ISeriesSettings makeDeepCopy() {

		ILineSeriesSettings lineSeriesSettings = new LineSeriesSettings();
		transfer(lineSeriesSettings);
		return lineSeriesSettings;
	}

	@Override
	public boolean transfer(ISeriesSettings seriesSettingsSink) {

		boolean success = false;
		if(seriesSettingsSink instanceof ILineSeriesSettings) {
			ILineSeriesSettings source = this;
			ILineSeriesSettings sink = (ILineSeriesSettings)seriesSettingsSink;
			sink.setDescription(source.getDescription());
			sink.setVisible(source.isVisible());
			sink.setVisibleInLegend(source.isVisibleInLegend());
			sink.setAntialias(source.getAntialias());
			sink.setEnableArea(source.isEnableArea());
			sink.setAreaStrict(source.isAreaStrict());
			sink.setSymbolType(source.getSymbolType());
			sink.setSymbolSize(source.getSymbolSize());
			sink.setSymbolColor(source.getSymbolColor());
			sink.setLineColor(source.getLineColor());
			sink.setLineWidth(source.getLineWidth());
			sink.setEnableStack(source.isEnableStack());
			sink.setEnableStep(source.isEnableStep());
			sink.setLineStyle(source.getLineStyle());
			sink.setHighlight(source.isHighlight());
			success = true;
		}
		//
		return success;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		ILineSeriesSettings lineSeriesSettings = new LineSeriesSettings();
		transfer(lineSeriesSettings);
		return lineSeriesSettings;
	}
}