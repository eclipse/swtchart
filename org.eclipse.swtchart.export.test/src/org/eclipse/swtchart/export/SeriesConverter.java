/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.export;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.SeriesData;

public class SeriesConverter {

	public static ISeriesData getSeriesXY(String file) {

		int size = getNumberOfLines(file);
		double[] xSeries = new double[size];
		double[] ySeries = new double[size];
		//
		BufferedReader bufferedReader = null;
		try {
			String line;
			int i = 0;
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("#")) {
					String[] values = line.split("\t");
					xSeries[i] = Double.parseDouble(values[0].trim());
					ySeries[i] = Double.parseDouble(values[1].trim());
					i++;
				}
			}
		} catch(Exception e) {
			//
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch(IOException e) {
					//
				}
			}
		}
		//
		ISeriesData seriesData = new SeriesData(xSeries, ySeries, file);
		return seriesData;
	}

	public static ISeriesData getSeriesFromY(String fileName) {

		int size = getNumberOfLines(fileName);
		double[] ySeries = new double[size];
		//
		BufferedReader bufferedReader = null;
		try {
			String line;
			int i = 0;
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			while((line = bufferedReader.readLine()) != null) {
				ySeries[i++] = Double.parseDouble(line.trim());
			}
		} catch(Exception e) {
			//
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch(IOException e) {
					//
				}
			}
		}
		//
		ISeriesData seriesData = new SeriesData(ySeries, fileName);
		return seriesData;
	}

	public static List<ISeriesData> getSeriesScatter(String fileName) {

		List<ISeriesData> scatterSeriesList = new ArrayList<ISeriesData>();
		//
		BufferedReader bufferedReader = null;
		try {
			String line;
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			while((line = bufferedReader.readLine()) != null) {
				String[] values = line.split("\t");
				String id = values[0].trim();
				double[] xSeries = new double[]{Double.parseDouble(values[1].trim())};
				double[] ySeries = new double[]{Double.parseDouble(values[2].trim())};
				ISeriesData seriesData = new SeriesData(xSeries, ySeries, id);
				scatterSeriesList.add(seriesData);
			}
		} catch(Exception e) {
			//
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch(IOException e) {
					//
				}
			}
		}
		return scatterSeriesList;
	}

	private static int getNumberOfLines(String file) {

		int i = 0;
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while((bufferedReader.readLine()) != null) {
				i++;
			}
		} catch(Exception e) {
			//
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch(IOException e) {
					//
				}
			}
		}
		return i;
	}
}
