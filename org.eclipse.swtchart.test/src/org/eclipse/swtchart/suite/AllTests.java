/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.suite;

import org.eclipse.swtchart.AxisSetTest;
import org.eclipse.swtchart.AxisTest;
import org.eclipse.swtchart.AxisTickTest;
import org.eclipse.swtchart.AxisTitleTest;
import org.eclipse.swtchart.BarSeriesTest;
import org.eclipse.swtchart.ChartTest;
import org.eclipse.swtchart.ChartTitleTest;
import org.eclipse.swtchart.ErrorBarTest;
import org.eclipse.swtchart.GridTest;
import org.eclipse.swtchart.LegendTest;
import org.eclipse.swtchart.LineSeriesTest;
import org.eclipse.swtchart.SeriesLabelTest;
import org.eclipse.swtchart.SeriesSetTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({
	ChartTest.class,
	ChartTitleTest.class,
	LegendTest.class,
	AxisSetTest.class,
	AxisTest.class,
	AxisTickTest.class,
	AxisTitleTest.class,
	GridTest.class,
	SeriesLabelTest.class,
	SeriesSetTest.class,
	LineSeriesTest.class,
	BarSeriesTest.class,
	ErrorBarTest.class,
})

public class AllTests {
	//
}
