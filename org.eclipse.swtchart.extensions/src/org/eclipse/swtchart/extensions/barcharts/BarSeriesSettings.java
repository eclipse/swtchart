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
package org.eclipse.swtchart.extensions.barcharts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.core.AbstractSeriesSettings;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;

public class BarSeriesSettings extends AbstractSeriesSettings implements IBarSeriesSettings {

	private Color barColor;
	private int barPadding;
	private int barWidth;
	private BarWidthStyle barWidthStyle;
	private IBarSeriesSettings seriesSettingsHighlight = null;

	public BarSeriesSettings() {
		barColor = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		barPadding = 20;
		barWidth = 1;
		barWidthStyle = BarWidthStyle.FIXED;
	}

	@Override
	public Color getBarColor() {

		return barColor;
	}

	@Override
	public void setBarColor(Color barColor) {

		this.barColor = barColor;
	}

	@Override
	public int getBarPadding() {

		return barPadding;
	}

	@Override
	public void setBarPadding(int barPadding) {

		this.barPadding = barPadding;
	}

	@Override
	public int getBarWidth() {

		return barWidth;
	}

	@Override
	public void setBarWidth(int barWidth) {

		this.barWidth = barWidth;
	}

	@Override
	public BarWidthStyle getBarWidthStyle() {

		return barWidthStyle;
	}

	@Override
	public void setBarWidthStyle(BarWidthStyle barWidthStyle) {

		this.barWidthStyle = barWidthStyle;
	}

	@Override
	public ISeriesSettings getSeriesSettingsHighlight() {

		if(seriesSettingsHighlight == null) {
			try {
				seriesSettingsHighlight = (IBarSeriesSettings)this.clone();
			} catch(CloneNotSupportedException e) {
				seriesSettingsHighlight = new BarSeriesSettings();
			}
		}
		return seriesSettingsHighlight;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		IBarSeriesSettings barSeriesSettings = new BarSeriesSettings();
		barSeriesSettings.setDescription(this.getDescription());
		barSeriesSettings.setVisible(this.isVisible());
		barSeriesSettings.setVisibleInLegend(this.isVisibleInLegend());
		barSeriesSettings.setBarColor(this.getBarColor());
		barSeriesSettings.setBarPadding(this.getBarPadding());
		barSeriesSettings.setBarWidth(this.getBarWidth());
		return barSeriesSettings;
	}
}