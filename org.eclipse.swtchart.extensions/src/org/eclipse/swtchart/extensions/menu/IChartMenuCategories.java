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
package org.eclipse.swtchart.extensions.menu;

public interface IChartMenuCategories {

	String STANDARD_OPERATION = ""; // Must be empty to be placed on the main menu level. //$NON-NLS-1$
	String RANGE_SELECTION = Messages.getString(Messages.RANGE_SELECTION); 
	String TOGGLE_VISIBILITY = Messages.getString(Messages.TOGGLE_VISIBILITY); 
	String EXPORT_CONVERTER = Messages.getString(Messages.EXPORT_CHART_SELECTION); 
}
