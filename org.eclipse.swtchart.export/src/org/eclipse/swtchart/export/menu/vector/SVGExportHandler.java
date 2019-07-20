/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Sanatt Abrol - SVG export code
 *******************************************************************************/
package org.eclipse.swtchart.export.menu.vector;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.export.core.AbstractSeriesExportHandler;
import org.eclipse.swtchart.export.core.ExportSettingsDialog;
import org.eclipse.swtchart.export.core.ISeriesExportConverter;
import org.eclipse.swtchart.export.svg.SVGFactory;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public class SVGExportHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = "*.svg";
	public static final String NAME = "Vector Graphic (" + FILE_EXTENSION + ")";
	private static final String TITLE = "Save As Scalable Vector Graphic";

	@Override
	public String getName() {

		return NAME;
	}

	public void paint(Graphics2D g2d) {
		g2d.setPaint(Color.red);
		g2d.fill(new Rectangle(10, 10, 100, 100));
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText(NAME);
		fileDialog.setFilterExtensions(new String[] { "*.svg" });
		//
		String fileName = fileDialog.open();
		if (fileName != null) {
			try {
				BaseChart baseChart = scrollableChart.getBaseChart();
				ExportSettingsDialog exportSettingsDialog = new ExportSettingsDialog(shell, baseChart);
				exportSettingsDialog.create();
				if (exportSettingsDialog.open() == Window.OK) {
					int indexAxisX = exportSettingsDialog.getIndexAxisSelectionX();
					int indexAxisY = exportSettingsDialog.getIndexAxisSelectionY();
					if (indexAxisX >= 0 && indexAxisY >= 0) {
						boolean useCSS = true;
						Writer output = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
						SVGFactory svgFactory = new SVGFactory();
						svgFactory.createSvg(baseChart, indexAxisX, indexAxisY);
						if (svgFactory.stream(output, useCSS)) {
							MessageDialog.openInformation(shell, TITLE, MESSAGE_OK);
						} else {
							MessageDialog.openInformation(shell, TITLE, MESSAGE_ERROR);
						}
					}
				}

			} catch (Exception e) {
				MessageDialog.openInformation(shell, TITLE, MESSAGE_ERROR);
				e.printStackTrace();
			}
		}
	}
}
