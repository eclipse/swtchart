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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class CompressPieSeries extends Compress {

	private String[] labels;
	private double[] values;
	private int[] colours = {SWT.COLOR_BLUE, SWT.COLOR_DARK_YELLOW, SWT.COLOR_GREEN, SWT.COLOR_CYAN, SWT.COLOR_RED, SWT.COLOR_DARK_BLUE, SWT.COLOR_DARK_RED, SWT.COLOR_DARK_CYAN, SWT.COLOR_MAGENTA, SWT.COLOR_DARK_GRAY, SWT.COLOR_DARK_MAGENTA, SWT.COLOR_GRAY, SWT.COLOR_WIDGET_NORMAL_SHADOW, SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW};
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

	public String[] getLabelSeries() {

		String[] ids = new String[labels.length];
		System.arraycopy(labels, 0, ids, 0, labels.length);
		return ids;
	}

	public double[] getValueSeries() {

		double[] val = new double[values.length];
		System.arraycopy(values, 0, val, 0, values.length);
		return val;
	}

	public Color[] getColors() {

		Color[] color = new Color[colors.length];
		System.arraycopy(colors, 0, color, 0, colors.length);
		return color;
	}

	private void setColor() {

		colors = new Color[labels.length];
		int color = colours.length;
		colors[0] = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
		for(int i = 1; i != labels.length; i++) {
			int colour = i % color;
			colors[i] = Display.getDefault().getSystemColor(colours[i]);
		}
	}
}
