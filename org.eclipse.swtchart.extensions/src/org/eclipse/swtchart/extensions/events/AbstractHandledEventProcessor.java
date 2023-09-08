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
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IMouseSupport;

public abstract class AbstractHandledEventProcessor implements IHandledEventProcessor {

	@Override
	public int getButton() {

		return IMouseSupport.MOUSE_BUTTON_NONE;
	}

	protected boolean isSingleClick(Event event) {

		return event.count == 1;
	}

	public void showClickbindingHelp(BaseChart chart, String name, String description) {

		chart.openShortcutPopup(getFormattedShortcut(), name, description);
	}

	private String getFormattedShortcut() {

		String modifier = "";
		if((getStateMask() & SWT.MOD1) == SWT.MOD1) {
			modifier = isMac() ? "CMD" : "CTRL";
		} else if((getStateMask() & SWT.MOD2) == SWT.MOD2) {
			modifier = "SHIFT";
		} else if((getStateMask() & SWT.MOD3) == SWT.MOD3) {
			modifier = "ALT";
		}
		//
		String button = "";
		switch(getButton()) {
			case IMouseSupport.MOUSE_BUTTON_LEFT:
				button = "Left";
				break;
			case IMouseSupport.MOUSE_BUTTON_MIDDLE:
				button = "Middle";
				break;
			case IMouseSupport.MOUSE_BUTTON_RIGHT:
				button = "Right";
				break;
		}
		//
		String event = "";
		switch(getEvent()) {
			case IMouseSupport.EVENT_MOUSE_DOUBLE_CLICK:
				event = "Double Click";
				break;
			case IMouseSupport.EVENT_MOUSE_DOWN:
			case IMouseSupport.EVENT_MOUSE_UP:
				event = "Click";
				break;
		}
		StringBuilder builder = new StringBuilder();
		if(!modifier.isEmpty()) {
			builder.append(modifier);
		}
		if(!button.isEmpty()) {
			if(!modifier.isEmpty()) {
				builder.append(" + " + button);
			} else {
				builder.append(button);
			}
		}
		if(!event.isEmpty()) {
			builder.append(" " + event);
		}
		return builder.toString();
	}

	private static boolean isMac() {

		return (getOperatingSystem().indexOf("mac") >= 0);
	}

	private static String getOperatingSystem() {

		return System.getProperty("os.name").toLowerCase();
	}
}
