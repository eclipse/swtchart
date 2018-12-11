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

public class TSVExportHandler extends AbstractSeparatedValueHandler implements ISeriesExportConverter {

	private static final String FILE_EXTENSION = "*.tsv";
	public static final String NAME = "Tab Separated Values (" + FILE_EXTENSION + ")";
	//
	private static final String TITLE = "Save As Tab Separated Text";
	private static final String DELIMITER = "\t";

	@Override
	public String getName() {

		return NAME;
	}

	public TSVExportHandler() {
		super(TITLE, FILE_EXTENSION, DELIMITER);
	}
}
