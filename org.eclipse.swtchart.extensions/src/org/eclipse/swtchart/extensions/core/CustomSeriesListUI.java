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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swtchart.extensions.internal.support.CustomSeriesComparator;
import org.eclipse.swtchart.extensions.internal.support.CustomSeriesEditingSupport;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;

public class CustomSeriesListUI extends AbstractSeriesListUI {

	private static final String[] TITLES = CustomSeriesLabelProvider.TITLES;
	private static final int[] BOUNDS = CustomSeriesLabelProvider.BOUNDS;
	//
	private static final String COLUMN_DELIMITER = " "; //$NON-NLS-1$
	//
	private CustomSeriesLabelProvider labelProvider = new CustomSeriesLabelProvider();
	private IContentProvider contentProvider = ArrayContentProvider.getInstance();
	private CustomSeriesComparator comparator = new CustomSeriesComparator();
	private List<TableViewerColumn> columns = new ArrayList<>();
	//
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();

	public CustomSeriesListUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setTableSortable(boolean enable) {

		if(enable) {
			setComparator(comparator);
		} else {
			setComparator(null);
		}
	}

	private void createControl() {

		createColumns(TITLES, BOUNDS);
		setLabelProvider(labelProvider);
		setContentProvider(contentProvider);
		setComparator(null);
		setEditSupport();
		setColumnOrder(getTable());
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
				tableColumn.addSelectionListener(createSelectionAdapter(i));
				columns.add(tableViewerColumn);
			}
		}
		/*
		 * Set header and lines visible
		 */
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	private SelectionAdapter createSelectionAdapter(final int index) {

		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				comparator.setColumn(index);
				int direction = comparator.getDirection();
				getTable().setSortDirection(direction);
				refresh();
			}
		};
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

				String columnOrder = getColumnOrder(getTable());
				if(preferenceStore != null) {
					preferenceStore.setValue(PreferenceConstants.P_CUSTOM_SERIES_COLUMN_ORDER, columnOrder);
					ResourceSupport.savePreferenceStore();
				}
			}
		});
		return tableViewerColumn;
	}

	private void setEditSupport() {

		for(int i = 0; i < columns.size(); i++) {
			TableViewerColumn tableViewerColumn = columns.get(i);
			tableViewerColumn.setEditingSupport(new CustomSeriesEditingSupport(this, i));
		}
	}

	private String getColumnOrder(Table table) {

		return convertColumnOrder(table.getColumnOrder());
	}

	private void setColumnOrder(Table table) {

		if(preferenceStore != null) {
			try {
				String columnOrder = preferenceStore.getString(PreferenceConstants.P_CUSTOM_SERIES_COLUMN_ORDER);
				if(!columnOrder.isEmpty()) {
					int[] columns = convertColumnOrder(columnOrder);
					table.setColumnOrder(columns);
				}
			} catch(SWTException | IllegalArgumentException e) {
				/*
				 * On exception, default order will be used.
				 */
			}
		}
	}

	private int[] convertColumnOrder(String columnOrder) {

		String[] values = columnOrder.split(COLUMN_DELIMITER);
		int size = values.length;
		int[] columns = new int[size];
		for(int i = 0; i < size; i++) {
			try {
				columns[i] = Integer.parseInt(values[i]);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		//
		return columns;
	}

	private String convertColumnOrder(int[] columnOrder) {

		StringBuilder builder = new StringBuilder();
		for(int i : columnOrder) {
			builder.append(i);
			builder.append(COLUMN_DELIMITER);
		}
		//
		return builder.toString().trim();
	}
}