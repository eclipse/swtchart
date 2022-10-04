/*******************************************************************************
 * Copyright (c) 2022 Lablicate GmbH.
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
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.extensions.core.BaseChart;

public class ImageClipboardSupport {

	public static void transfer(Display display, BaseChart baseChart) {

		/*
		 * Image Data
		 */
		ImageSupplier imageSupplier = new ImageSupplier();
		ImageData imageData = imageSupplier.getImageData(baseChart);
		//
		Clipboard clipboard = new Clipboard(display);
		try {
			if(isWindows()) {
				clipboard.setContents(new Object[]{imageData, imageData}, new Transfer[]{ImageTransfer.getInstance(), ImageArrayTransfer.getInstanceWindows()});
			} else if(isLinux()) {
				clipboard.setContents(new Object[]{imageData}, new Transfer[]{ImageArrayTransfer.getInstanceLinux()});
			} else if(isMac() || isUnix()) {
				clipboard.setContents(new Object[]{imageData}, new Transfer[]{ImageTransfer.getInstance()});
			}
		} finally {
			if(clipboard != null && !clipboard.isDisposed()) {
				clipboard.dispose();
			}
		}
	}

	private static boolean isWindows() {

		return (getOperatingSystem().indexOf("win") >= 0);
	}

	private static boolean isLinux() {

		return (getOperatingSystem().indexOf("linux") >= 0);
	}

	private static boolean isMac() {

		return (getOperatingSystem().indexOf("mac") >= 0);
	}

	private static boolean isUnix() {

		return (getOperatingSystem().indexOf("unix") >= 0);
	}

	private static String getOperatingSystem() {

		return System.getProperty("os.name").toLowerCase();
	}
}