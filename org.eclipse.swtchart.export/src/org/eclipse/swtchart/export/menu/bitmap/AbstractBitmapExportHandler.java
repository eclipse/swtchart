/*******************************************************************************
 * Copyright (c) 2017, 2021 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.export.menu.bitmap;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.export.core.AbstractSeriesExportHandler;
import org.eclipse.swtchart.export.core.BitmapExportSettingsDialog;
import org.eclipse.swtchart.export.core.ISeriesExportConverter;
import org.eclipse.swtchart.export.images.ImageSupplier;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public abstract class AbstractBitmapExportHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	private String name;
	@SuppressWarnings("unused")
	private String title;
	private String[] filterExtensions;
	private int format;

	public AbstractBitmapExportHandler(String name, String title, String[] filterExtensions, int format) {

		this.name = name;
		this.title = title;
		this.filterExtensions = filterExtensions;
		this.format = format;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText(name);
		fileDialog.setFilterExtensions(filterExtensions); // $NON-NLS-1$ //$NON-NLS-2$
		//
		String fileName = fileDialog.open();
		if(fileName != null) {
			/*
			 * Select the export size.
			 */
			BitmapExportSettingsDialog bitmapSettingsDialog = new BitmapExportSettingsDialog(fileDialog.getParent());
			bitmapSettingsDialog.create();
			if(bitmapSettingsDialog.open() == Window.OK) {
				/*
				 * Base Chart
				 */
				BaseChart baseChart = scrollableChart.getBaseChart();
				if(bitmapSettingsDialog.isCustomSize()) {
					int width = bitmapSettingsDialog.getCustomWidth();
					int height = bitmapSettingsDialog.getCustomHeight();
					exportCustomSize(baseChart, fileName, width, height);
				} else {
					exportNormal(baseChart, fileName);
				}
			}
		}
	}

	private void exportNormal(BaseChart baseChart, String fileName) {

		ImageSupplier imageSupplier = new ImageSupplier();
		ImageData imageData = imageSupplier.getImageData(baseChart);
		imageSupplier.saveImage(imageData, fileName, format);
	}

	private void exportCustomSize(BaseChart baseChart, String fileName, int width, int height) {

		Composite parent = baseChart.getParent();
		Rectangle bounds = baseChart.getBounds();
		/*
		 * Size Image / Save
		 */
		Display display = Display.getDefault();
		Shell imageShell = new Shell(display);
		Rectangle imageBounds = imageShell.computeTrim(0, 0, width, height);
		imageShell.setSize(imageBounds.width, imageBounds.height);
		imageShell.setLocation(0, 0);
		imageShell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		imageShell.addListener(SWT.Paint, new Listener() {

			@Override
			public void handleEvent(Event event) {

				baseChart.setParent(imageShell);
				baseChart.setBounds(0, 0, width, height);
				baseChart.updateLayout();
			}
		});
		imageShell.open();
		while(!imageShell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
				ImageSupplier imageSupplier = new ImageSupplier();
				ImageData imageData = imageSupplier.getImageData(baseChart);
				imageSupplier.saveImage(imageData, fileName, format);
				baseChart.setParent(parent);
				baseChart.setBounds(bounds);
				baseChart.updateLayout();
				imageShell.close();
			}
		}
	}
}