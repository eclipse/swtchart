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

import java.util.Objects;

public class MappingsKey {

	private static final String KEY_DELIMITER = "_"; //$NON-NLS-1$
	//
	private MappingsType mappingsType = MappingsType.NONE;
	private String id = ""; //$NON-NLS-1$
	private String key = ""; //$NON-NLS-1$

	/*
	 * E.g.
	 * LINE_418_Chromatogram_TIC
	 */
	public MappingsKey(String key) {

		this.key = key;
		parseKey();
	}

	/*
	 * E.g.
	 * LINE
	 * 418_Chromatogram_TIC
	 */
	public MappingsKey(MappingsType mappingsType, String id) {

		this.mappingsType = mappingsType;
		this.id = id;
		this.key = getKey(mappingsType, id);
	}

	public MappingsType getMappingsType() {

		return mappingsType;
	}

	public String getId() {

		return id;
	}

	public String getKey() {

		return key;
	}

	public static String getKey(MappingsType mappingsType, String id) {

		return mappingsType.name() + KEY_DELIMITER + id;
	}

	/*
	 * If possible, call this method only once.
	 */
	private void parseKey() {

		exitloop:
		for(MappingsType mappingsType : MappingsType.values()) {
			if(key.startsWith(mappingsType.name())) {
				this.mappingsType = mappingsType;
				this.id = key.substring(mappingsType.name().length() + 1, key.length());
				break exitloop;
			}
		}
	}

	@Override
	public int hashCode() {

		return Objects.hash(key);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		MappingsKey other = (MappingsKey)obj;
		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {

		return "MappingsKey [key=" + key + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}