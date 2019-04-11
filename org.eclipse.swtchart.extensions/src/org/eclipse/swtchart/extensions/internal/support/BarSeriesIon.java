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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.internal.support;

import org.eclipse.swt.graphics.Point;

public class BarSeriesIon {

	private double mz;
	private double intensity;
	private Point point;

	public BarSeriesIon(double mz, double intensity, Point point) {
		this.mz = mz;
		this.intensity = intensity;
		this.point = point;
	}

	public double getMz() {

		return mz;
	}

	public double getIntensity() {

		return intensity;
	}

	public Point getPoint() {

		return point;
	}
}
