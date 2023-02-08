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
package org.eclipse.swtchart.export.menu.text;

import java.text.MessageFormat;

import org.eclipse.swtchart.export.core.AbstractSeparatedValueHandler;
import org.eclipse.swtchart.export.core.ISeriesExportConverter;

public class TSVExportHandler extends AbstractSeparatedValueHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = "*.tsv"; //$NON-NLS-1$
	public static final String NAME = MessageFormat.format(Messages.getString(Messages.TAB_SEPARATED_VALUES), FILE_EXTENSION);
	//
	private static final String TITLE = Messages.getString(Messages.SAVE_AS_TAB_SEPARATED); // $NON-NLS-1$
	private static final String DELIMITER = "\t"; //$NON-NLS-1$

	@Override
	public String getName() {

		return NAME;
	}

	public TSVExportHandler() {

		super(TITLE, FILE_EXTENSION, DELIMITER);
	}
}
