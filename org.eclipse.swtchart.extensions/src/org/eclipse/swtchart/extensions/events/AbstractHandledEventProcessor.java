/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.IMouseSupport;

public abstract class AbstractHandledEventProcessor implements IHandledEventProcessor {

	@Override
	public int getButton() {

		return IMouseSupport.MOUSE_BUTTON_NONE;
	}

	protected boolean isSingleClick(Event event) {

		return event.count == 1;
	}
}
