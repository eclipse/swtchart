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

import org.eclipse.swt.SWT;

public class BMPExportHandler extends AbstractBitmapExportHandler {

	private static final String FILE_EXTENSION = "*.bmp"; //$NON-NLS-1$
	public static final String NAME = Messages.getString(Messages.IMAGE) + FILE_EXTENSION + ")"; //$NON-NLS-1$
	private static final String TITLE = Messages.getString(Messages.SAVE_AS_IMAGE);
	private static final String[] FILTER_EXTENSIONS = new String[]{"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$

	public BMPExportHandler() {

		super(NAME, TITLE, FILTER_EXTENSIONS, SWT.IMAGE_BMP);
	}
}