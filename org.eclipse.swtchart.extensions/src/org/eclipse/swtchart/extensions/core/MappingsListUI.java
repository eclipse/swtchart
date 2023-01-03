/*******************************************************************************
 * Copyright (c) 2020, 2023 Lablicate GmbH.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swtchart.extensions.internal.support.MappingsComparator;
import org.eclipse.swtchart.extensions.internal.support.MappingsFilter;
import org.eclipse.swtchart.extensions.internal.support.MappingsLabelProvider;

public class MappingsListUI extends TableViewer {

	private static final String[] TITLES = MappingsLabelProvider.TITLES;
	private static final int[] BOUNDS = MappingsLabelProvider.BOUNDS;
	//
	private ILabelProvider labelProvider = new MappingsLabelProvider();
	private IContentProvider contentProvider = ArrayContentProvider.getInstance();
	private MappingsComparator comparator = new MappingsComparator();
	private MappingsFilter filter = new MappingsFilter();
	//
	private List<TableViewerColumn> columns = new ArrayList<>();

	public MappingsListUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		filter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	private void createControl() {

		createColumns(TITLES, BOUNDS);
		setLabelProvider(labelProvider);
		setContentProvider(contentProvider);
		setComparator(comparator);
		setFilters(new ViewerFilter[]{filter});
	}

	private void createColumns(String[] titles, int[] bounds) {

		Table table = getTable();
		table.setRedraw(false);
		/*
		 * Prepare the table
		 */
		table.clearAll();
		while(table.getColumnCount() > 0) {
			table.getColumns()[0].dispose();
		}
		table.setRedraw(true);
		refresh();
		/*
		 * Create the columns
		 */
		if(getLabelProvider() != null) {
			for(int i = 0; i < titles.length; i++) {
				String title = titles[i];
				final TableViewerColumn tableViewerColumn = createTableColumn(title, bounds[i]);
				final TableColumn tableColumn = tableViewerColumn.getColumn();
				tableColumn.addSelectionListener(createSelectionAdapter(tableColumn, i));
				columns.add(tableViewerColumn);
			}
		}
		/*
		 * Set header and lines visible
		 */
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	private SelectionAdapter createSelectionAdapter(final TableColumn column, final int index) {

		SelectionAdapter selectionAdapter = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				comparator.setColumn(index);
				int direction = comparator.getDirection();
				getTable().setSortDirection(direction);
				refresh();
			}
		};
		//
		return selectionAdapter;
	}

	private TableViewerColumn createTableColumn(String title, int width) {

		final TableViewerColumn tableViewerColumn = new TableViewerColumn(this, SWT.NONE);
		final TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setText(title);
		tableColumn.setWidth(width);
		tableColumn.setResizable(true);
		tableColumn.setMoveable(false);
		return tableViewerColumn;
	}
}