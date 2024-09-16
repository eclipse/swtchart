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

public abstract class Command<T> {

	private final T value;

	public Command(T value) {

		this.value = value;
	}

	public T getValue() {

		return value;
	}

	@Override
	public String toString() {

		return String.format((Locale)null, "%s[value=%s]", getClass().getName(), getValue());
	}

	@Override
	public boolean equals(Object obj) {

		if(obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}
		Command<?> o = (Command<?>)obj;
		return value == o.value || value.equals(o.value);
	}
}
