/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.export.menu;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.export.core.AbstractSeriesExportHandler;
import org.eclipse.swtchart.extensions.clipboard.ImageSupplier;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.ScrollableChart;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class PrinterExportHandler extends AbstractSeriesExportHandler implements IChartMenuEntry {

	private static final String TITLE = Messages.SAVE_SELECTION;

	@Override
	public String getName() {

		return Messages.PRINT;
	}

	@Override
	public Image getIcon() {

		return ResourceSupport.getImage(ResourceSupport.ICON_PRINT);
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		/*
		 * Print using the installed printer dialog.
		 */
		PrintDialog dialog = new PrintDialog(shell);
		PrinterData printerData = dialog.open();
		if(printerData != null) {
			Printer printer = new Printer(printerData);
			if(printer.startJob(Messages.PRINTER_EXPORT)) {
				/*
				 * Create a page
				 */
				GC gc = new GC(printer);
				printer.startPage();
				/*
				 * Print the data.
				 */
				Rectangle trim = printer.computeTrim(0, 0, 0, 0);
				Rectangle clientArea = printer.getClientArea();
				ImageSupplier imageSupplier = new ImageSupplier();
				ImageData imageData = imageSupplier.getImageData(scrollableChart.getBaseChart());
				Image image = new Image(printer, imageData);
				int srcWidth = imageData.width;
				int srcHeight = imageData.height;
				if(srcWidth > 0) {
					double scalingFactor = srcHeight / (srcWidth * 1.0d);
					int destWidth = clientArea.width + trim.x; // trim is negative
					int destHeight = (int)(clientArea.width * scalingFactor) + trim.y; // trim is negative
					gc.drawImage(image, 0, 0, srcWidth, srcHeight, -trim.x, -trim.y, destWidth, destHeight);
				}
				/*
				 * Dispose the elements.
				 */
				image.dispose();
				gc.dispose();
				printer.endPage();
				printer.endJob();
				//
				MessageDialog.openInformation(shell, TITLE, org.eclipse.swtchart.export.core.Messages.DATA_EXPORT_SUCCESS);
			}
			printer.dispose();
		}
	}
}
