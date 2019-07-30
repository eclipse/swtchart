/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.model;

import java.util.Date;

public class DateArraySeriesModel extends DoubleArraySeriesModel {

	private Date[] dates;

	public DateArraySeriesModel(Date[] dates, double[] ySeries) {
		super(toXSeriesArray(dates), ySeries);
		this.dates = dates;
	}

	private static double[] toXSeriesArray(Date[] dates) {

		double[] xSeries = new double[dates.length];
		for(int i = 0; i < xSeries.length; i++) {
			xSeries[i] = dates[i].getTime();
		}
		return xSeries;
	}

	public Date dateAt(int index) {

		return dates[index];
	}
}
