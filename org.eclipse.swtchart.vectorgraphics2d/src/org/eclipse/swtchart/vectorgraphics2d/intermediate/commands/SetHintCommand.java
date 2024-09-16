/*******************************************************************************
 * Copyright (c) 2010, 2019 VectorGraphics2D project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Erich Seifert - initial API and implementation
 * Michael Seifert - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.intermediate.commands;

import java.util.Locale;

public class SetHintCommand extends StateCommand<Object> {

	private final Object key;

	public SetHintCommand(Object hintKey, Object hintValue) {

		super(hintValue);
		key = hintKey;
	}

	public Object getKey() {

		return key;
	}

	@Override
	public String toString() {

		return String.format((Locale)null, "%s[key=%s, value=%s]", getClass().getName(), getKey(), getValue());
	}
}
