/*******************************************************************************
 * Copyright (c) 2019, 2023 Lablicate GmbH.
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
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.export.extended.menu.vector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.export.core.AbstractSeriesExportHandler;
import org.eclipse.swtchart.export.core.VectorExportSettingsDialog;
import org.eclipse.swtchart.export.extended.svg.SVGFactory;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class SVGExportHandler extends AbstractSeriesExportHandler implements IChartMenuEntry {

	private static final String FILE_EXTENSION = "*.svg"; //$NON-NLS-1$
	private static final String NAME = MessageFormat.format(Messages.SVG, FILE_EXTENSION);
	private static final String TITLE = Messages.SAVE_AS_SVG;

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText(NAME);
		fileDialog.setFilterExtensions(new String[]{"*.svg"}); //$NON-NLS-1$
		//
		String fileName = fileDialog.open();
		if(fileName != null) {
			try {
				BaseChart baseChart = scrollableChart.getBaseChart();
				VectorExportSettingsDialog exportSettingsDialog = new VectorExportSettingsDialog(fileDialog.getParent(), baseChart);
				exportSettingsDialog.create();
				if(exportSettingsDialog.open() == Window.OK) {
					int indexAxisX = exportSettingsDialog.getIndexAxisSelectionX();
					int indexAxisY = exportSettingsDialog.getIndexAxisSelectionY();
					if(indexAxisX >= 0 && indexAxisY >= 0) {
						try {
							ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(fileDialog.getParent());
							monitorDialog.run(false, false, new IRunnableWithProgress() {

								@Override
								public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

									monitor.beginTask(Messages.EXPORT_TO_SVG, IProgressMonitor.UNKNOWN);
									try (Writer output = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8")) { //$NON-NLS-1$
										boolean useCSS = true;
										SVGFactory svgFactory = new SVGFactory();
										svgFactory.createSvg(baseChart, indexAxisX, indexAxisY);
										if(svgFactory.stream(output, useCSS)) {
											MessageDialog.openInformation(fileDialog.getParent(), TITLE, org.eclipse.swtchart.export.core.Messages.DATA_EXPORT_SUCCESS);
										} else {
											MessageDialog.openInformation(fileDialog.getParent(), TITLE, org.eclipse.swtchart.export.core.Messages.DATA_EXPORT_ERROR);
										}
									} catch(IOException e) {
										e.printStackTrace();
									} finally {
										monitor.done();
									}
								}
							});
						} catch(InterruptedException e) {
							e.printStackTrace();
							Thread.currentThread().interrupt();
						}
					}
				}
			} catch(InvocationTargetException e) {
				MessageDialog.openInformation(shell, TITLE, org.eclipse.swtchart.export.core.Messages.DATA_EXPORT_ERROR);
				e.getCause().printStackTrace();
			}
		}
	}
}
