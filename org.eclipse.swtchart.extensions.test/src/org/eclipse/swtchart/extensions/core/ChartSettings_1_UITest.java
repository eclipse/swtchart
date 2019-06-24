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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import junit.framework.TestCase;

public class ChartSettings_1_UITest extends TestCase {

	private ChartSettings settings;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		settings = new ChartSettings();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertNotNull(settings.getTitleFont());
	}
}
