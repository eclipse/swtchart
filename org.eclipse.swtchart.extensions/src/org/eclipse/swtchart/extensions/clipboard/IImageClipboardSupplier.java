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
package org.eclipse.swtchart.extensions.clipboard;

import org.eclipse.swtchart.extensions.core.BaseChart;

public interface IImageClipboardSupplier {

	/**
	 * Return a MIME type, e.g.: image/svg+xml
	 * 
	 * @return String
	 */
	String getTypeName();

	/**
	 * Creates the transfer object.
	 * 
	 * @param baseChart
	 * @return Object
	 */
	Object createData(BaseChart baseChart);

	/**
	 * Transfer the given object to a byte array.
	 * 
	 * @param object
	 * @return byte[]
	 */
	byte[] getData(Object object);
}