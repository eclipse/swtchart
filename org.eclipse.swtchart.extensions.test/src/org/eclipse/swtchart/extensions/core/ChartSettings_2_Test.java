/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.core;

import org.eclipse.swtchart.extensions.menu.toggle.TogglePlotCenterMarkerHandler;
import org.eclipse.swtchart.extensions.menu.toggle.TogglePositionMarkerHandler;
import org.eclipse.swtchart.extensions.menu.toggle.ToggleSeriesLegendHandler;

import junit.framework.TestCase;

public class ChartSettings_2_Test extends TestCase {

	private ChartSettings chartSettings = new ChartSettings();

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		chartSettings.clearMenuEntries();
		chartSettings.addMenuEntry(new TogglePositionMarkerHandler());
		chartSettings.addMenuEntry(new ToggleSeriesLegendHandler());
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertNotNull(chartSettings.getChartMenuEntryByClass(TogglePositionMarkerHandler.class));
	}

	public void test2() {

		assertNotNull(chartSettings.getChartMenuEntryByClass(ToggleSeriesLegendHandler.class));
	}

	public void test3() {

		assertNull(chartSettings.getChartMenuEntryByClass(TogglePlotCenterMarkerHandler.class));
	}
}
