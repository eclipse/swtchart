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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an intermediate node in the page tree of a PDF document.
 */
class PageTreeNode implements PDFObject {

	private final PageTreeNode parent;
	private final List<Page> children;

	/**
	 * Initializes a {@code PageTreeNode} with the specified parent node.
	 * 
	 * @param parent
	 *            Parent node or {@code null} to create a root node.
	 */
	public PageTreeNode(PageTreeNode parent) {

		this.parent = parent;
		this.children = new LinkedList<>();
	}

	/**
	 * Returns the type of this object.
	 * The return value is always {@literal Pages}.
	 * 
	 * @return The String {@literal Pages}.
	 */
	public String getType() {

		return "Pages";
	}

	/**
	 * Returns the parent of this node.
	 * If this node is a root node, the method returns {@code null}.
	 * 
	 * @return Parent node or {@code null}, if this is a root node.
	 */
	public PageTreeNode getParent() {

		return parent;
	}

	/**
	 * Adds the specified {@code Page} to the node's children.
	 * 
	 * @param page
	 *            {@code Page} to be added.
	 */
	public void add(Page page) {

		page.setParent(this);
		children.add(page);
	}

	/**
	 * Returns all {@code Page} objects that are immediate children of this node.
	 * 
	 * @return List of child pages.
	 */
	public List<Page> getKids() {

		return Collections.unmodifiableList(children);
	}

	/**
	 * Returns the total number of {@code Page} objects in this subtree.
	 * 
	 * @return Number of pages that are direct or indirect children.
	 */
	public int getCount() {

		return children.size();
	}
}
