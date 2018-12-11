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

import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public interface ISeriesExportConverter extends IChartMenuEntry {

	String MESSAGE_OK = "The data has been exported successully.";
	String MESSAGE_ERROR = "Sorry, something has gone wrong to export the data.";
}
