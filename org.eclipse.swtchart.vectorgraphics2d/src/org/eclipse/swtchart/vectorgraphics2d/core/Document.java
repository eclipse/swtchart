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
package org.eclipse.swtchart.vectorgraphics2d.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for documents that are able to output binary files in the
 * implemented file format.
 */
public interface Document {

	void writeTo(OutputStream out) throws IOException;

	/**
	 * Returns whether or not the {@code Document} represents compressed data.
	 * 
	 * @return {@code true} if the contents are compressed, {@code false} otherwise.
	 */
	boolean isCompressed();
}