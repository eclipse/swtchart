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
package org.eclipse.swtchart.internal;

import org.eclipse.swtchart.Chart;

/**
 * A chart title.
 */
public class ChartTitle extends Title {

	/** the default text */
	private static final String DEFAULT_TEXT = Messages.getString(Messages.CHART_TITLE); 

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

	@Override
	protected String getDefaultText() {

		return DEFAULT_TEXT;
	}
}
