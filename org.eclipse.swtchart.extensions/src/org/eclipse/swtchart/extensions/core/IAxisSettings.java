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
package org.eclipse.swtchart.extensions.core;

import java.text.DecimalFormat;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.LineStyle;

public interface IAxisSettings {

	/**
	 * This method returns the label of the axis.
	 * It's calculated by using the title and description.
	 * 
	 * @return String
	 */
	String getLabel();

	String getTitle();

	void setTitle(String title);

	String getDescription();

	void setDescription(String description);

	DecimalFormat getDecimalFormat();

	void setDecimalFormat(DecimalFormat decimalFormat);

	Color getColor();

	void setColor(Color color);

	boolean isVisible();

	void setVisible(boolean visible);

	Position getPosition();

	void setPosition(Position position);

	Color getGridColor();

	void setGridColor(Color gridColor);

	LineStyle getGridLineStyle();

	/**
	 * LineStyle.SOLID
	 * LineStyle.DASH
	 * LineStyle.DOT
	 * LineStyle.DASHDOT
	 * LineStyle.DASHDOTDOT
	 * LineStyle.NONE
	 * 
	 * @param gridLineStyle
	 */
	void setGridLineStyle(LineStyle gridLineStyle);

	boolean isEnableLogScale();

	void setEnableLogScale(boolean enableLogScale);

	int getExtraSpaceTitle();

	void setExtraSpaceTitle(int extraSpaceTitle);
}
