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
package org.eclipse.swtchart;

/**
 * A range.
 */
public class Range {

	/** the lower value of range */
	public double lower;
	/** the upper value of range */
	public double upper;

	/**
	 * Constructor.
	 * 
	 * @param start
	 *            the start value of range
	 * @param end
	 *            the end value of range
	 */
	public Range(double start, double end) {
		this.lower = (end > start) ? start : end;
		this.upper = (end > start) ? end : start;
	}

	/**
	 * Constructor.
	 * 
	 * @param range
	 *            the range
	 */
	public Range(Range range) {
		lower = (range.upper > range.lower) ? range.lower : range.upper;
		upper = (range.upper > range.lower) ? range.upper : range.lower;
	}

	@Override
	public String toString() {

		return "lower=" + lower + ", upper=" + upper; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
