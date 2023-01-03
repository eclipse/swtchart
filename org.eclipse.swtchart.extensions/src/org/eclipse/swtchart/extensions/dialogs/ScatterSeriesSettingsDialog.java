/*******************************************************************************
 * Copyright (c) 2022, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.dialogs;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;

public class ScatterSeriesSettingsDialog extends AbstractPointSeriesSettingsDialog<IScatterSeriesSettings> {

	public ScatterSeriesSettingsDialog(Shell parentShell, IScatterSeriesSettings settings) {

		super(parentShell, settings);
	}
}