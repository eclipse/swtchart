/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.internal.axis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.Constants;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.internal.Title;

/**
 * An Axis title.
 */
public class AxisTitle extends Title {

	/** the default text for X Axis */
	private static final String DEFAULT_TEXT_FOR_XAXIS = Messages.getString(Messages.X_AXIS);
	/** the default text for X Axis */
	private static final String DEFAULT_TEXT_FOR_YAXIS = Messages.getString(Messages.Y_AXIS);
	/** the default color */
	private static final int DEFAULT_FONT_SIZE = Constants.MEDIUM_FONT_SIZE;
	/** the default font */
	private final Font defaultFont;
	/** the axis */
	private final Axis axis;
	/** the direction of axis */
	private final Direction direction;

	/**
	 * Constructor.
	 *
	 * @param chart
	 *            the chart
	 * @param style
	 *            the style
	 * @param axis
	 *            the axis
	 * @param direction
	 *            the direction
	 */
	public AxisTitle(Chart chart, int style, Axis axis, Direction direction) {
		super(chart);
		this.axis = axis;
		this.direction = direction;
		defaultFont = new Font(Display.getDefault(), "Tahoma", DEFAULT_FONT_SIZE, SWT.BOLD); //$NON-NLS-1$
		setFont(defaultFont);
		setText(getDefaultText());
	}

	@Override
	protected String getDefaultText() {

		if(direction == Direction.X) {
			return DEFAULT_TEXT_FOR_XAXIS;
		}
		return DEFAULT_TEXT_FOR_YAXIS;
	}

	@Override
	protected boolean isHorizontal() {

		return axis.isHorizontalAxis();
	}

	@Override
	public void dispose() {

		super.dispose();
		if(!defaultFont.isDisposed()) {
			defaultFont.dispose();
		}
	}
}
