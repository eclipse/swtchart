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
package org.eclipse.swtchart.extensions.core;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.Chart;

public abstract class AbstractHandledChart extends Chart implements IEventHandler {

	public AbstractHandledChart(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void handleEvent(Event event) {

		super.handleEvent(event);
		IEventHandler.super.handleEvent(event);
	}
}
