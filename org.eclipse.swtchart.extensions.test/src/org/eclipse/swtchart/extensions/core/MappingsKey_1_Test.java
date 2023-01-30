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

import static org.junit.Assert.assertNotEquals;

import junit.framework.TestCase;

public class MappingsKey_1_Test extends TestCase {

	private MappingsType mappingsType = MappingsType.LINE;
	private String seriesId = "418_01_EditorTab#8_(Extracted Ion Chromatogram,NONE)	418_01 [MSD]";

	public void test1a() {

		String key = MappingsKey.getKey(mappingsType, seriesId);
		MappingsKey mappingsKey = new MappingsKey(key);
		assertEquals(mappingsType, mappingsKey.getMappingsType());
		assertEquals(seriesId, mappingsKey.getId());
		assertEquals(key, mappingsKey.getKey());
		assertNotEquals(MappingsType.NONE, mappingsKey.getMappingsType());
		assertNotEquals(MappingsType.BAR, mappingsKey.getMappingsType());
		assertNotEquals(MappingsType.CIRCULAR, mappingsKey.getMappingsType());
		assertNotEquals(MappingsType.SCATTER, mappingsKey.getMappingsType());
	}

	public void test1() {

		MappingsKey mappingsKey = new MappingsKey(mappingsType, seriesId);
		assertEquals(mappingsType, mappingsKey.getMappingsType());
		assertEquals(seriesId, mappingsKey.getId());
		assertEquals(MappingsKey.getKey(mappingsType, seriesId), mappingsKey.getKey());
		assertNotEquals(MappingsType.NONE, mappingsKey.getMappingsType());
		assertNotEquals(MappingsType.BAR, mappingsKey.getMappingsType());
		assertNotEquals(MappingsType.CIRCULAR, mappingsKey.getMappingsType());
		assertNotEquals(MappingsType.SCATTER, mappingsKey.getMappingsType());
	}
}