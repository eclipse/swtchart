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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PageTreeNodeTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testTypeIsPages() {

		PageTreeNode pages = new PageTreeNode(null);
		String type = pages.getType();
		assertThat(type, is("Pages"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testConstructorSetsParent() {

		PageTreeNode parent = new PageTreeNode(null);
		PageTreeNode pages = new PageTreeNode(parent);
		assertThat(pages.getParent(), is(parent));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAddInsertsPage() {

		PageTreeNode pages = new PageTreeNode(null);
		Page child = new Page(null, null, null);
		pages.add(child);
		assertThat(pages.getKids(), hasItem(child));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAddSetsParentOfAddedPage() {

		PageTreeNode pages = new PageTreeNode(null);
		Page child = new Page(null, null, null);
		pages.add(child);
		assertThat(child.getParent(), is(pages));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCountReturnsZeroWhenEmpty() {

		PageTreeNode pages = new PageTreeNode(null);
		int count = pages.getCount();
		assertThat(count, is(0));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCountReturnsNumberOfDirectChildrenWhenOnlyDirectChildrenArePresent() {

		PageTreeNode pages = new PageTreeNode(null);
		Page child1 = new Page(null, null, null);
		Page child2 = new Page(null, null, null);
		Page child3 = new Page(null, null, null);
		pages.add(child1);
		pages.add(child2);
		pages.add(child3);
		int count = pages.getCount();
		assertThat(count, is(3));
	}
}
