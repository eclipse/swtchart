/*******************************************************************************
 * Copyright (c) 2020, 2021 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Yash Bharatiya - initial API and implementation
 * Philip Wenig - menu demo extension
 *******************************************************************************/
package org.eclipse.swtchart.extensions.examples.charts.macos;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.IChartSettings;

public class DemoChart {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("DemoChart (macOS)");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		//
		LineChartX lineChartX = new LineChartX(shell);
		shell.open();
		/*
		 * Test macOS settings.
		 */
		IChartSettings chartSettings = lineChartX.getChartSettings();
		chartSettings.setLegendExtendedVisible(false); // Don't show the legend.
		chartSettings.setBufferSelection(false); // Doesn't work on macOS.
		chartSettings.clearMenuEntries(); // Menu entries
		chartSettings.addMenuEntry(new ResetChartHandlerX());
		chartSettings.clearHandledEventProcessors(); // Keyboard/mouse events
		chartSettings.addHandledEventProcessor(new MouseDownEventX());
		chartSettings.addHandledEventProcessor(new MouseMoveSelectionEventX());
		chartSettings.addHandledEventProcessor(new MouseMoveShiftEventX());
		chartSettings.addHandledEventProcessor(new MouseUpEventX());
		lineChartX.applySettings(chartSettings);
		//
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}