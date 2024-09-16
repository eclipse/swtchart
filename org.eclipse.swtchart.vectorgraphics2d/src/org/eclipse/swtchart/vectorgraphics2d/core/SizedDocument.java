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

import org.eclipse.swtchart.vectorgraphics2d.util.PageSize;

/**
 * Abstract base for documents that are restricted to a specified page size.
 */
public abstract class SizedDocument implements Document {

	private final PageSize pageSize;
	private final boolean compressed;

	public SizedDocument(PageSize pageSize, boolean compressed) {

		this.pageSize = pageSize;
		this.compressed = compressed;
	}

	public PageSize getPageSize() {

		return pageSize;
	}

	@Override
	public boolean isCompressed() {

		return this.compressed;
	}
}
