/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.support.RangeSupport;

public class MouseWheelSlideYEvent extends AbstractMouseEvent {

	@Override
	public int getStateMask() {

		return SWT.MOD2;
	}

	protected void runAction(BaseChart baseChart, Event event) {

		RangeSupport.applyVerticalSlide(baseChart, 0.1d, event.count < 0);
	}
}