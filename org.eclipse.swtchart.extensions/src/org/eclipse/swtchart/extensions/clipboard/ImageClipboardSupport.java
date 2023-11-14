/*******************************************************************************
 * Copyright (c) 2022, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.clipboard;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.internal.support.OS;

public class ImageClipboardSupport {

	public static void transfer(Display display, BaseChart baseChart) {

		transfer(display, baseChart, false);
	}

	public static void transfer(Display display, BaseChart baseChart, boolean useSpecificSupplier) {

		/*
		 * Image Data / Supplier
		 */
		Object imageData = null;
		IImageClipboardSupplier specificSupplier = null;
		//
		if(useSpecificSupplier) {
			specificSupplier = baseChart.getImageClipboardSupplier();
			if(specificSupplier != null) {
				imageData = specificSupplier.createData(baseChart);
			}
		} else {
			ImageSupplier imageSupplier = new ImageSupplier();
			imageData = imageSupplier.getImageData(baseChart);
		}
		/*
		 * Clipboard
		 */
		if(imageData != null) {
			Clipboard clipboard = new Clipboard(display);
			try {
				ImageArrayTransfer transferSpecific = (specificSupplier != null) ? ImageArrayTransfer.getInstanceSpecific(specificSupplier) : null;
				if(OS.isWindows()) {
					if(transferSpecific != null) {
						clipboard.setContents(new Object[]{imageData, imageData}, new Transfer[]{ImageArrayTransfer.getImageTransferWindows(), transferSpecific});
					} else {
						clipboard.setContents(new Object[]{imageData, imageData}, new Transfer[]{ImageArrayTransfer.getImageTransferWindows(), ImageArrayTransfer.getInstanceWindows()});
					}
				} else if(OS.isLinux()) {
					if(transferSpecific != null) {
						clipboard.setContents(new Object[]{imageData}, new Transfer[]{transferSpecific});
					} else {
						clipboard.setContents(new Object[]{imageData}, new Transfer[]{ImageArrayTransfer.getInstanceLinux()});
					}
				} else if(OS.isMac() || OS.isUnix()) {
					if(transferSpecific != null) {
						clipboard.setContents(new Object[]{imageData}, new Transfer[]{transferSpecific});
					} else {
						clipboard.setContents(new Object[]{imageData}, new Transfer[]{ImageArrayTransfer.getImageTransferMacOS()});
					}
				}
			} finally {
				if(!clipboard.isDisposed()) {
					clipboard.dispose();
				}
			}
		}
	}
}