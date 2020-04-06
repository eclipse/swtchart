/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
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
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swtchart.extensions.internal.support.SeriesComparator;
import org.eclipse.swtchart.extensions.internal.support.SeriesEditingSupport;
import org.eclipse.swtchart.extensions.internal.support.SeriesFilter;
import org.eclipse.swtchart.extensions.internal.support.SeriesLabelProvider;

public class SeriesListUI extends TableViewer {

	private static final String[] TITLES = SeriesLabelProvider.TITLES;
	private static final int[] BOUNDS = SeriesLabelProvider.BOUNDS;
	//
	private ILabelProvider labelProvider = new SeriesLabelProvider();
	private IContentProvider contentProvider = ArrayContentProvider.getInstance();
	private SeriesComparator comparator = new SeriesComparator();
	private SeriesFilter filter = new SeriesFilter();
	//
	private List<TableViewerColumn> columns = new ArrayList<>();

	public SeriesListUI(Composite parent, int style) {
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
		setCellColorAndEditSupport();
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
		tableColumn.setMoveable(true);
		tableColumn.addListener(SWT.Move, new Listener() {

			@Override
			public void handleEvent(Event event) {

				// fireColumnMoved();
			}
		});
		return tableViewerColumn;
	}

	private void setCellColorAndEditSupport() {

		for(TableViewerColumn tableViewerColumn : columns) {
			/*
			 * Cell Color Provider
			 */
			String title = tableViewerColumn.getColumn().getText();
			switch(title) {
				case SeriesLabelProvider.COLOR:
					setColorColumnProvider(tableViewerColumn);
					break;
			}
			/*
			 * Edit Support
			 */
			tableViewerColumn.setEditingSupport(new SeriesEditingSupport(this, title));
		}
	}

	private void setColorColumnProvider(TableViewerColumn tableViewerColumn) {

		tableViewerColumn.setLabelProvider(new StyledCellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {

				if(cell != null) {
					String text = cell.getText();
					Object object = cell.getElement();
					Color color = SeriesLabelProvider.getColor(object);
					cell.setBackground(color);
					cell.setText(text);
					super.update(cell);
				}
			}
		});
	}
}
