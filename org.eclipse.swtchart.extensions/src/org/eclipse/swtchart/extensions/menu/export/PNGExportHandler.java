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
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu.export;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.images.ImageSupplier;

public class PNGExportHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = "*.png";
	public static final String NAME = "Image (" + FILE_EXTENSION + ")";
	private static final String TITLE = "Save As Image";

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setOverwrite(true);
		fileDialog.setText(NAME);
		fileDialog.setFilterExtensions(new String[]{"*.png"});
		//
		String fileName = fileDialog.open();
		if(fileName != null) {
			/*
			 * Select the format.
			 */
			ImageSupplier imageSupplier = new ImageSupplier();
			ImageData imageData = imageSupplier.getImageData(scrollableChart.getBaseChart());
			imageSupplier.saveImage(imageData, fileName, SWT.IMAGE_PNG);
			MessageDialog.openInformation(shell, TITLE, MESSAGE_OK);
		}
	}
}
