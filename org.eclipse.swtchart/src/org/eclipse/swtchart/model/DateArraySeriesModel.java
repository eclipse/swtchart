/*******************************************************************************
 * Copyright (c) 2019, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Matthias Mailänder - time zone support
 *******************************************************************************/
package org.eclipse.swtchart.model;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class DateArraySeriesModel extends DoubleArraySeriesModel {

	private LocalDate[] localDates;
	private Date[] dates;

	public DateArraySeriesModel(Date[] dates, double[] ySeries) {

		super(toXSeriesArray(dates), ySeries);
		this.dates = dates;
	}

	public DateArraySeriesModel(LocalDate[] dates, double[] ySeries, ZoneOffset zoneOffset) {

		super(toXSeriesArray(dates, zoneOffset), ySeries);
		this.localDates = dates;
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

	private static double[] toXSeriesArray(LocalDate[] dates, ZoneOffset zoneOffset) {

		double[] xSeries = new double[dates.length];
		for(int i = 0; i < xSeries.length; i++) {
			xSeries[i] = dates[i].atStartOfDay().toEpochSecond(zoneOffset);
		}
		return xSeries;
	}

	public LocalDate localDateAt(int index) {

		return localDates[index];
	}
}
