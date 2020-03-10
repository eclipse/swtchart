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
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.export.core;

import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public interface ISeriesExportConverter extends IChartMenuEntry {

	String MESSAGE_OK = Messages.getString(Messages.DATA_EXPORT_SUCCESS);
	String MESSAGE_ERROR = Messages.getString(Messages.DATA_EXPORT_ERROR);
}
