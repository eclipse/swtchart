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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Default methods are used as BaseChart and ScrollableChart
 * both have to support the same methods, but inheritance is
 * not possible.
 *
 */
public interface IEventHandler extends Listener, PaintListener {

	default void handleEvent(Event event) {

		switch(event.type) {
			case SWT.KeyDown:
				handleKeyDownEvent(event);
				break;
			case SWT.KeyUp:
				handleKeyUpEvent(event);
				break;
			case SWT.MouseMove:
				handleMouseMoveEvent(event);
				break;
			case SWT.MouseDown:
				handleMouseDownEvent(event);
				break;
			case SWT.MouseUp:
				handleMouseUpEvent(event);
				break;
			case SWT.MouseWheel:
				handleMouseWheel(event);
				break;
			case SWT.MouseDoubleClick:
				handleMouseDoubleClick(event);
				break;
			case SWT.Selection:
				handleSelectionEvent(event);
				break;
		}
	}

	default void paintControl(PaintEvent e) {

	}

	default void handleMouseMoveEvent(Event event) {

	}

	default void handleMouseDownEvent(Event event) {

	}

	default void handleMouseUpEvent(Event event) {

	}

	default void handleMouseWheel(Event event) {

	}

	default void handleMouseDoubleClick(Event event) {

	}

	default void handleKeyDownEvent(Event event) {

	}

	default void handleKeyUpEvent(Event event) {

	}

	default void handleSelectionEvent(Event event) {

	}
}
