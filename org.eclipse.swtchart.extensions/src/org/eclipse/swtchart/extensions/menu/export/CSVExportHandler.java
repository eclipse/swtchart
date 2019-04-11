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

public class CSVExportHandler extends AbstractSeparatedValueHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = "*.csv";
	public static final String NAME = "Comma Separated Values (" + FILE_EXTENSION + ")";
	//
	private static final String TITLE = "Save As Comma Separated Text";
	private static final String DELIMITER = ",";

	@Override
	public String getName() {

		return NAME;
	}

	public CSVExportHandler() {
		super(TITLE, FILE_EXTENSION, DELIMITER);
	}
}
