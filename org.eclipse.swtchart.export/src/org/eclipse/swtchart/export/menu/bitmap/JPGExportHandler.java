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
package org.eclipse.swtchart.export.menu.bitmap;

import java.text.MessageFormat;

import org.eclipse.swt.SWT;

public class JPGExportHandler extends AbstractBitmapExportHandler {

	private static final String FILE_EXTENSION = "*.jpg"; //$NON-NLS-1$
	public static final String NAME = MessageFormat.format(Messages.getString(Messages.IMAGE), FILE_EXTENSION);
	private static final String TITLE = Messages.getString(Messages.SAVE_AS_IMAGE);
	private static final String[] FILTER_EXTENSIONS = new String[]{"*.jpeg", "*.jpg"}; //$NON-NLS-1$ //$NON-NLS-2$

	public JPGExportHandler() {

		super(NAME, TITLE, FILTER_EXTENSIONS, SWT.IMAGE_JPEG);
	}
}
