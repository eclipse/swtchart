/*******************************************************************************
 * Copyright (c) 2008, 2019 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.examples;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.examples.advanced.AxisTickBoundsExample;
import org.eclipse.swtchart.examples.advanced.BarBoundsExample;
import org.eclipse.swtchart.examples.advanced.CustomPaintListenerExample;
import org.eclipse.swtchart.examples.advanced.DataToPixelConversionExample;
import org.eclipse.swtchart.examples.advanced.LegendBoundsExample;
import org.eclipse.swtchart.examples.advanced.PxielToDataConversionExample;
import org.eclipse.swtchart.examples.advanced.SymbolBoundsExample;
/**
 * The class to run all examples.
 */
public class RunAllExamples {

	static Vector<Chart> basicCharts;
	static Vector<Chart> advancedCharts;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Examples");
		shell.setSize(750, 400);
		shell.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(shell, SWT.HORIZONTAL);
		TabFolder tabFolder = new TabFolder(sashForm, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		tabFolder.setLayout(new FillLayout());
		final Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		final StackLayout layout = new StackLayout();
		composite.setLayout(layout);
		sashForm.setWeights(new int[]{1, 2});
		createList("Basic", createBasicCharts(composite), tabFolder, layout, composite);
		createList("Advanced", createAdvancedCharts(composite), tabFolder, layout, composite);
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void createList(String tabName, final Vector<Chart> charts, TabFolder tabFolder, final StackLayout layout, final Composite composite) {

		final List list = new List(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		TabItem basicTabItem = new TabItem(tabFolder, SWT.NONE);
		basicTabItem.setText(tabName);
		basicTabItem.setControl(list);
		for(Chart chart : charts) {
			list.add(chart.getTitle().getText());
		}
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {

				Chart chart = charts.get(list.getSelectionIndex());
				chart.getAxisSet().adjustRange();
				layout.topControl = chart;
				composite.layout();
			}
		});
	}

	private static Vector<Chart> createBasicCharts(Composite parent) {

		basicCharts = new Vector<Chart>();
		basicCharts.add(LineChartExample.createChart(parent));
		basicCharts.add(BarChartExample.createChart(parent));
		basicCharts.add(ScatterChartExample.createChart(parent));
		basicCharts.add(AreaChartExample.createChart(parent));
		basicCharts.add(StepChartExample.createChart(parent));
		basicCharts.add(StackSeriesExample.createChart(parent));
		basicCharts.add(LogScaleExample.createChart(parent));
		basicCharts.add(OrientationExample.createChart(parent));
		basicCharts.add(CategoryExample.createChart(parent));
		basicCharts.add(SeriesLabelExample.createChart(parent));
		basicCharts.add(ErrorBarsExample.createChart(parent));
		basicCharts.add(MultipleAxesExample.createChart(parent));
		basicCharts.add(LargeSeriesExample.createChart(parent));
		basicCharts.add(AngledAxisTickLabelsExample.createChart(parent));
		return basicCharts;
	}

	private static Vector<Chart> createAdvancedCharts(Composite parent) {

		basicCharts = new Vector<Chart>();
		basicCharts.add(PxielToDataConversionExample.createChart(parent));
		basicCharts.add(DataToPixelConversionExample.createChart(parent));
		basicCharts.add(SymbolBoundsExample.createChart(parent));
		basicCharts.add(BarBoundsExample.createChart(parent));
		basicCharts.add(AxisTickBoundsExample.createChart(parent));
		basicCharts.add(LegendBoundsExample.createChart(parent));
		basicCharts.add(CustomPaintListenerExample.createChart(parent));
		return basicCharts;
	}
}
