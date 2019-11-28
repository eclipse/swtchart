/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
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

public interface IMouseSupport {

	int EVENT_MOUSE_DOUBLE_CLICK = 1;
	int EVENT_MOUSE_WHEEL = 2;
	int EVENT_MOUSE_DOWN = 3;
	int EVENT_MOUSE_MOVE = 4;
	int EVENT_MOUSE_UP = 5;
	//
	int MOUSE_BUTTON_LEFT = 1;
	int MOUSE_BUTTON_MIDDLE = 2;
	int MOUSE_BUTTON_RIGHT = 3; // Used by the menu
	int MOUSE_BUTTON_WHEEL = 4;
	int MOUSE_BUTTON_NONE = 5;
}