/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.Resources;

public abstract class AbstractAxisSettings implements IAxisSettings {

	private String title = ""; // Chart Title //$NON-NLS-1$
	private boolean titleVisible;
	private String description = ""; // e.g. DropDown RangeSelector //$NON-NLS-1$
	private DecimalFormat decimalFormat;
	private Color color;
	private Font titleFont;
	private boolean visible;
	private Position position;
	private Color gridColor;
	private LineStyle gridLineStyle;
	private boolean enableLogScale;
	private double logScaleBase;
	private boolean reversed;
	private boolean drawAxisLine;
	private boolean drawPositionMarker;
	private int extraSpaceTitle;
	private boolean integerDataPointAxis;
	/*
	 * The default font is only used if no font is set.
	 */
	private final Font defaultFont = Resources.getFont("Tahoma", Resources.MEDIUM_FONT_SIZE, SWT.BOLD); //$NON-NLS-1$

	public AbstractAxisSettings(String title) {

		/*
		 * In this case, the title is used also as
		 * the description.
		 */
		this(title, title);
	}

	public AbstractAxisSettings(String title, String description) {

		this.title = title;
		this.description = description;
		titleVisible = true;
		decimalFormat = new DecimalFormat();
		if(Display.isSystemDarkTheme()) {
			color = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		} else {
			color = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		}
		titleFont = defaultFont;
		visible = true;
		position = Position.Primary;
		if(Display.isSystemDarkTheme()) {
			gridColor = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
		} else {
			gridColor = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		}
		gridLineStyle = LineStyle.DOT;
		enableLogScale = false;
		logScaleBase = 10.0d;
		reversed = false;
		drawAxisLine = true;
		drawPositionMarker = false;
		extraSpaceTitle = 25;
		integerDataPointAxis = false;
	}

	@Override
	public String getLabel() {

		String label = ""; //$NON-NLS-1$
		if(title.equals("")) { //$NON-NLS-1$
			/*
			 * Title is not set.
			 * Use the description instead or
			 * print a note that no label is available.
			 */
			if(description.equals("")) { //$NON-NLS-1$
				label = Messages.LABEL_NOT_SET;
			} else {
				label = description;
			}
		} else {
			/*
			 * Title is set.
			 * Use description if available
			 * otherwise the title.
			 */
			if(description.equals("")) { //$NON-NLS-1$
				label = title;
			} else {
				label = description;
				/*
				 * Handle the primary axes differently,
				 * as the description not necessarily needs to be set.
				 * If it's the default value, then return the title.
				 */
				if(this instanceof IPrimaryAxisSettings) {
					if(description.equals(BaseChart.DEFAULT_TITLE_X_AXIS) || description.equals(BaseChart.DEFAULT_TITLE_Y_AXIS)) {
						label = title;
					}
				}
			}
		}
		//
		return label;
	}

	@Override
	public String getTitle() {

		return title;
	}

	@Override
	public void setTitle(String title) {

		this.title = title;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public boolean isTitleVisible() {

		return titleVisible;
	}

	@Override
	public void setTitleVisible(boolean titleVisible) {

		this.titleVisible = titleVisible;
	}

	@Override
	public DecimalFormat getDecimalFormat() {

		return decimalFormat;
	}

	@Override
	public void setDecimalFormat(DecimalFormat decimalFormat) {

		this.decimalFormat = decimalFormat;
	}

	@Override
	public Color getColor() {

		return color;
	}

	@Override
	public void setColor(Color color) {

		this.color = color;
	}

	@Override
	public Font getTitleFont() {

		return titleFont;
	}

	@Override
	public void setTitleFont(Font titleFont) {

		this.titleFont = titleFont;
	}

	@Override
	public boolean isVisible() {

		return visible;
	}

	@Override
	public void setVisible(boolean visible) {

		this.visible = visible;
	}

	@Override
	public Position getPosition() {

		return position;
	}

	@Override
	public void setPosition(Position position) {

		this.position = position;
	}

	@Override
	public Color getGridColor() {

		return gridColor;
	}

	@Override
	public void setGridColor(Color gridColor) {

		this.gridColor = gridColor;
	}

	@Override
	public LineStyle getGridLineStyle() {

		return gridLineStyle;
	}

	@Override
	public void setGridLineStyle(LineStyle gridLineStyle) {

		this.gridLineStyle = gridLineStyle;
	}

	@Override
	public boolean isEnableLogScale() {

		return enableLogScale;
	}

	@Override
	public void setEnableLogScale(boolean enableLogScale) {

		this.enableLogScale = enableLogScale;
	}

	@Override
	public void setLogScaleBase(double base) {

		logScaleBase = base;
	}

	@Override
	public double getLogScaleBase() {

		return logScaleBase;
	}

	@Override
	public boolean isReversed() {

		return reversed;
	}

	@Override
	public void setReversed(boolean reversed) {

		this.reversed = reversed;
	}

	@Override
	public boolean isDrawAxisLine() {

		return drawAxisLine;
	}

	@Override
	public void setDrawAxisLine(boolean drawAxisLine) {

		this.drawAxisLine = drawAxisLine;
	}

	@Override
	public boolean isDrawPositionMarker() {

		return drawPositionMarker;
	}

	@Override
	public void setDrawPositionMarker(boolean drawPositionMarker) {

		this.drawPositionMarker = drawPositionMarker;
	}

	@Override
	public int getExtraSpaceTitle() {

		return extraSpaceTitle;
	}

	@Override
	public void setExtraSpaceTitle(int extraSpaceTitle) {

		this.extraSpaceTitle = extraSpaceTitle;
	}

	@Override
	public boolean isIntegerDataPointAxis() {

		return integerDataPointAxis;
	}

	@Override
	public void setIntegerDataPointAxis(boolean isIntegerDataPointAxis) {

		this.integerDataPointAxis = isIntegerDataPointAxis;
	}
}