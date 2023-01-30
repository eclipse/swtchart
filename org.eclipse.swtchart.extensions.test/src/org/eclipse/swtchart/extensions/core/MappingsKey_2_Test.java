/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
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

public class MappingsKey_2_Test extends TestCase {

	private MappingsType mappingsType = MappingsType.LINE;
	private String seriesId = "(.*)(57)";

	public void test1a() {

		String key = MappingsKey.getKey(mappingsType, seriesId);
		MappingsKey mappingsKey = new MappingsKey(key);
		assertEquals(mappingsType, mappingsKey.getMappingsType());
		assertEquals(seriesId, mappingsKey.getId());
		assertEquals(key, mappingsKey.getKey());
	}

	public void test1() {

		MappingsKey mappingsKey = new MappingsKey(mappingsType, seriesId);
		assertEquals(mappingsType, mappingsKey.getMappingsType());
		assertEquals(seriesId, mappingsKey.getId());
		assertEquals(MappingsKey.getKey(mappingsType, seriesId), mappingsKey.getKey());
	}
}