/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.export.menu.vector;

import org.eclipse.swtchart.export.core.AxisSettings;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public interface IVectorDataExport {

	String generate(ScrollableChart scrollableChart, AxisSettings axisSettings) throws Exception;
}