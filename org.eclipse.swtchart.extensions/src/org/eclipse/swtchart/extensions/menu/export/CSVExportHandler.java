/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
