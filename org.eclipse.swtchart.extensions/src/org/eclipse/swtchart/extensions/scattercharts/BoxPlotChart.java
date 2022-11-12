/*******************************************************************************
 * Copyright (c) 2022 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.scattercharts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.extensions.marker.BoxPlotMarker;

public class BoxPlotChart extends ScatterChart {

	public BoxPlotChart() {

		super();
		initialize();
	}

	public BoxPlotChart(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	private void initialize() {

		IPlotArea plotArea = getBaseChart().getPlotArea();
		BoxPlotMarker boxPlotMarker = new BoxPlotMarker(getBaseChart());
		plotArea.addCustomPaintListener(boxPlotMarker);
	}
}