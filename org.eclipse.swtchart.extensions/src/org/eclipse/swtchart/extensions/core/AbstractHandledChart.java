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
