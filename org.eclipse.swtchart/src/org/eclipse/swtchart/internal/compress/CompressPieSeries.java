/*******************************************************************************
 * Copyright (c) 2020 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Himanshu Balasamanta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.internal.compress;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class CompressPieSeries extends Compress {

	private String[] labels;
	private double[] values;
	private Color[] colors;

	@Override
	protected void addNecessaryPlots(ArrayList<Double> xList, ArrayList<Double> yList, ArrayList<Integer> indexList) {

	}

	public void setLabelSeries(String[] labels) {

		String[] ids = new String[labels.length];
		System.arraycopy(labels, 0, ids, 0, labels.length);
		this.labels = ids;
		setColor();
	}

	public void setValueSeries(double[] values) {

		double[] val = new double[values.length];
		System.arraycopy(values, 0, val, 0, values.length);
		this.values = val;
	}

	public void setColors(Color[] colors) {

		Color[] color = new Color[colors.length];
		System.arraycopy(colors, 0, color, 0, colors.length);
		this.colors = color;
	}

	/**
	 * 
	 * @return copy of the label series
	 */
	public String[] getLabelSeries() {

		String[] ids = new String[labels.length];
		System.arraycopy(labels, 0, ids, 0, labels.length);
		return ids;
	}

	/**
	 * 
	 * @return a copy of the value series
	 */
	public double[] getValueSeries() {

		double[] val = new double[values.length];
		System.arraycopy(values, 0, val, 0, values.length);
		return val;
	}

	/**
	 * 
	 * @return copy of colors array that is required to draw the pie chart
	 */
	public Color[] getColors() {

		Color[] color = new Color[colors.length];
		System.arraycopy(colors, 0, color, 0, colors.length);
		return color;
	}

	/**
	 * sets the color using HSB color model.
	 * This model allows a smooth color transition across the pie chart.
	 */
	private void setColor() {

		int colour = labels.length;
		float anglePerColor = 360 / colour;
		colors = new Color[colour];
		for(int i = 0; i != colour; i++) {
			Device device = Display.getDefault();
			colors[i] = new Color(device, new RGB(anglePerColor * i, 1, 1));
		}
	}
}
