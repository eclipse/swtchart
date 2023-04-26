/*******************************************************************************
 * Copyright (c) 2022, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.internal.support;

public class OS {

	public static boolean isWindows() {

		return (getOperatingSystem().indexOf("win") >= 0);
	}

	public static boolean isLinux() {

		return (getOperatingSystem().indexOf("linux") >= 0);
	}

	public static boolean isMac() {

		return (getOperatingSystem().indexOf("mac") >= 0);
	}

	public static boolean isUnix() {

		return (getOperatingSystem().indexOf("unix") >= 0);
	}

	private static String getOperatingSystem() {

		return System.getProperty("os.name").toLowerCase();
	}
}