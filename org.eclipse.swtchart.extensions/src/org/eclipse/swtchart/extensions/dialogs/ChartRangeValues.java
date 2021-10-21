/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.dialogs;

public class ChartRangeValues {

	private int axisX = -1;
	private String startX = "";
	private String stopX = "";
	private int axisY = -1;
	private String startY = "";
	private String stopY = "";

	public int getAxisX() {

		return axisX;
	}

	public ChartRangeValues setAxisX(int axisX) {

		this.axisX = axisX;
		return this;
	}

	public String getStartX() {

		return startX;
	}

	public ChartRangeValues setStartX(String startX) {

		this.startX = startX;
		return this;
	}

	public String getStopX() {

		return stopX;
	}

	public ChartRangeValues setStopX(String stopX) {

		this.stopX = stopX;
		return this;
	}

	public int getAxisY() {

		return axisY;
	}

	public ChartRangeValues setAxisY(int axisY) {

		this.axisY = axisY;
		return this;
	}

	public String getStartY() {

		return startY;
	}

	public ChartRangeValues setStartY(String startY) {

		this.startY = startY;
		return this;
	}

	public String getStopY() {

		return stopY;
	}

	public ChartRangeValues setStopY(String stopY) {

		this.stopY = stopY;
		return this;
	}
}