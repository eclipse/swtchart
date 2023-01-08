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
package org.eclipse.swtchart.extensions.examples.parts;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.extensions.examples.swt.CustomLinkedLineSeries1;

public class LineSeriesLinked_1_Part {

	@Inject
	public LineSeriesLinked_1_Part(Composite parent) {

		try {
			initialize(parent);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize(Composite parent) throws Exception {

		parent.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		//
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		//
		CustomLinkedLineSeries1 customLinkedLineSeries1 = new CustomLinkedLineSeries1(composite, SWT.NONE);
		customLinkedLineSeries1.setLayoutData(new GridData(GridData.FILL_BOTH));
		customLinkedLineSeries1.setChartInfo("Signal Z", "Sample", "Reference");
	}
}
