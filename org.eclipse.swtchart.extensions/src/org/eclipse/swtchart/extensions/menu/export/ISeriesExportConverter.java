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

import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public interface ISeriesExportConverter extends IChartMenuEntry {

	String MESSAGE_OK = "The data has been exported successully.";
	String MESSAGE_ERROR = "Sorry, something has gone wrong to export the data.";
}
