/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.model;

import org.eclipse.swt.graphics.Color;

public interface IElement {

	double POSITION_TOP_Y = Double.NEGATIVE_INFINITY;
	double POSITION_BOTTOM_Y = Double.POSITIVE_INFINITY;
	double POSITION_LEFT_X = Double.NEGATIVE_INFINITY;
	double POSITION_RIGHT_X = Double.POSITIVE_INFINITY;
	double MAX_HEIGHT = Double.POSITIVE_INFINITY;
	double MAX_WIDTH = Double.POSITIVE_INFINITY;

	double getX();

	void setX(double x);

	double getY();

	void setY(double y);

	Color getColor();

	void setColor(Color color);

	int getAlpha();

	void setAlpha(int alpha);
}