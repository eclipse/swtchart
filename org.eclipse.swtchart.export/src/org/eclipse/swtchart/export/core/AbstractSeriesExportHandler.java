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
 *******************************************************************************/
package org.eclipse.swtchart.export.core;

import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.menu.AbstractChartMenuEntry;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;
import org.eclipse.swtchart.extensions.menu.Messages;

public abstract class AbstractSeriesExportHandler extends AbstractChartMenuEntry implements IChartMenuEntry {

	/**
	 * Returns the description if available.
	 * Otherwise, the series id will be returned.
	 * 
	 * @param dataSeries
	 * @return String
	 */
	public String getIdentifier(ISeries<?> dataSeries) {

		String id = dataSeries.getId();
		String description = dataSeries.getDescription();
		return (description != null && !description.isEmpty()) ? description : id;
	}

	@Override
	public String getCategory() {

		return Messages.EXPORT_CHART_SELECTION;
	}
}