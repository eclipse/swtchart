/*******************************************************************************
 * Copyright (c) 2008, 2018 SWTChart project.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.internal;

import org.eclipse.swtchart.Chart;

/**
 * A chart title.
 */
public class ChartTitle extends Title {

	/** the default text */
	private static final String DEFAULT_TEXT = "Chart Title";

	/**
	 * Constructor.
	 *
	 * @param chart
	 *            the plot chart
	 */
	public ChartTitle(Chart chart) {
		super(chart);
		setText(getDefaultText());
	}

	/*
	 * @see Title#getDefaultText()
	 */
	@Override
	protected String getDefaultText() {

		return DEFAULT_TEXT;
	}
}
