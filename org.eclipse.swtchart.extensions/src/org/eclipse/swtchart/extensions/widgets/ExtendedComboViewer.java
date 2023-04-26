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
package org.eclipse.swtchart.extensions.widgets;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.extensions.internal.support.OS;

public class ExtendedComboViewer extends ComboViewer {

	public ExtendedComboViewer(Composite parent) {

		super(parent);
		initialize();
	}

	public ExtendedComboViewer(Composite parent, int style) {

		super(parent, style);
		initialize();
	}

	private void initialize() {

		/*
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=567652
		 */
		if(OS.isLinux()) {
			Combo combo = getCombo();
			combo.setBackground(combo.getBackground());
		}
	}
}