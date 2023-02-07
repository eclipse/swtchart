/*******************************************************************************
 * * Copyright (c) 2017, 2023 Lablicate GmbH.
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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swtchart.export.core.AbstractSeparatedValueHandler;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class CSVExportHandler extends AbstractSeparatedValueHandler implements IChartMenuEntry {

	private static final String FILE_EXTENSION = "*.csv"; //$NON-NLS-1$
	public static final String NAME = MessageFormat.format(Messages.COMMA_SEPARATED_VALUES, FILE_EXTENSION);
	//
	private static final String TITLE = Messages.SAVE_AS_COMMA_SEPARATED;
	private static final String DELIMITER = ","; //$NON-NLS-1$

	@Override
	public String getName() {

		return NAME;
	}

	@Override
	public Image getIcon() {

		return ResourceSupport.getImage(ResourceSupport.ICON_CSV);
	}

	public CSVExportHandler() {

		super(TITLE, FILE_EXTENSION, DELIMITER);
	}
}
