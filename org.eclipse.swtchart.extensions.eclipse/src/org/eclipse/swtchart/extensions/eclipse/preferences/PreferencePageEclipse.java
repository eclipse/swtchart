/*******************************************************************************
 * Copyright (c) 2020, 2022 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.eclipse.preferences;

import org.eclipse.swtchart.extensions.preferences.PreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageEclipse extends PreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageEclipse() {

		super(GRID);
	}

	public void init(IWorkbench workbench) {

		// The values are initialized in the ResourceSupport manager.
	}
}