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

import java.awt.geom.Rectangle2D;

/**
 * Represents a page of a PDF document.
 */
class Page implements PDFObject {

	private final Resources resources;
	private final Rectangle2D mediaBox;
	private final DefaultPDFObject contents;
	private PageTreeNode parent;

	/**
	 * Initializes a {@code Page} with the specified parent node, MediaBox and contents.
	 * 
	 * @param resources
	 *            Page resources
	 * @param mediaBox
	 *            Boundaries of the page.
	 * @param contents
	 *            Contents of the page.
	 */
	public Page(Resources resources, Rectangle2D mediaBox, DefaultPDFObject contents) {

		this.resources = resources;
		this.mediaBox = mediaBox;
		this.contents = contents;
	}

	/**
	 * Returns the type of this object.
	 * The return value is always {@literal Page}.
	 * 
	 * @return The String {@literal Page}.
	 */
	public String getType() {

		return "Page";
	}

	public Rectangle2D getMediaBox() {

		return mediaBox;
	}

	/**
	 * Returns the immediate parent of this {@code Page}.
	 * 
	 * @return Parent node.
	 */
	public PageTreeNode getParent() {

		return parent;
	}

	/**
	 * Sets the parent of this {@code Page} to the specified node.
	 * 
	 * @param parent
	 *            Immediate parent.
	 */
	protected void setParent(PageTreeNode parent) {

		this.parent = parent;
	}

	/**
	 * Returns the {@code Resources} object associated with this {@code Page}.
	 * 
	 * @return Page resources.
	 */
	public Resources getResources() {

		return resources;
	}

	/**
	 * Returns the contents of this {@code Page}.
	 * 
	 * @return Page contents.
	 */
	public DefaultPDFObject getContents() {

		return contents;
	}
}
