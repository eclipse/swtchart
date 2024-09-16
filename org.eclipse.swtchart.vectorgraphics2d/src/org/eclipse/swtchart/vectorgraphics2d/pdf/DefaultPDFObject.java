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
package org.eclipse.swtchart.vectorgraphics2d.pdf;

import java.util.LinkedHashMap;
import java.util.Map;

class DefaultPDFObject implements PDFObject {

	public final Map<String, Object> dict;
	public final Payload payload;
	public final boolean stream;

	public DefaultPDFObject(Map<String, Object> dict, Payload payload, boolean stream) {

		this.dict = new LinkedHashMap<>();
		this.payload = payload;
		this.stream = stream;
		if(dict != null) {
			this.dict.putAll(dict);
		}
	}

	public String getType() {

		return (String)dict.get("Type");
	}
}
