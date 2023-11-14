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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.internal.support.SeriesComparator;
import org.eclipse.swtchart.extensions.internal.support.SeriesContentProvider;
import org.eclipse.swtchart.extensions.internal.support.SeriesEditingSupport;
import org.eclipse.swtchart.extensions.internal.support.SeriesFilter;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;

public class SeriesListUI extends AbstractSeriesListUI {

	private static final String[] TITLES = SeriesLabelProvider.TITLES;
	private static final int[] BOUNDS = SeriesLabelProvider.BOUNDS;
	//
	private static final String COLUMN_DELIMITER = " "; //$NON-NLS-1$
	//
	private SeriesLabelProvider labelProvider = new SeriesLabelProvider();
	private IContentProvider contentProvider = new SeriesContentProvider();
	private SeriesComparator comparator = new SeriesComparator();
	private SeriesFilter filter = new SeriesFilter();
	private List<TableViewerColumn> columns = new ArrayList<>();
	//
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
	private BaseChart baseChart = null;

	public SeriesListUI(Composite parent, int style) {

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

	public void setSearchText(String searchText, boolean caseSensitive) {

		filter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	public void setBaseChart(BaseChart baseChart) {

		this.baseChart = baseChart;
		labelProvider.setBaseChart(baseChart);
		refresh();
	}

	public BaseChart getBaseChart() {

		return baseChart;
	}

	private void createControl() {

		createColumns(TITLES, BOUNDS);
		setLabelProvider(labelProvider);
		setContentProvider(contentProvider);
		setComparator(null);
		setFilters(filter);
		setCellColorAndEditSupport();
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
					preferenceStore.setValue(PreferenceConstants.P_LEGEND_COLUMN_ORDER, columnOrder);
					ResourceSupport.savePreferenceStore();
				}
			}
		});
		return tableViewerColumn;
	}

	private void setCellColorAndEditSupport() {

		for(int index = 0; index < columns.size(); index++) {
			TableViewerColumn tableViewerColumn = columns.get(index);
			/*
			 * Cell Color Provider
			 */
			if(index == SeriesLabelProvider.INDEX_COLOR) {
				setColorColumnProvider(tableViewerColumn);
			}
			/*
			 * Edit Support
			 */
			tableViewerColumn.setEditingSupport(new SeriesEditingSupport(this, index));
		}
	}

	private void setColorColumnProvider(TableViewerColumn tableViewerColumn) {

		tableViewerColumn.setLabelProvider(new StyledCellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {

				if(cell != null) {
					Object object = cell.getElement();
					if(object instanceof ISeries<?> series && baseChart != null) {
						ISeriesSettings seriesSettings = baseChart.getSeriesSettings(series.getId());
						Color color = SeriesLabelProvider.getColor(seriesSettings);
						cell.setBackground(color);
						cell.setText(""); // No text //$NON-NLS-1$
						super.update(cell);
					}
				}
			}
		});
	}

	private void setColumnOrder(Table table) {

		if(preferenceStore != null) {
			try {
				String columnOrder = preferenceStore.getString(PreferenceConstants.P_LEGEND_COLUMN_ORDER);
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

	private String getColumnOrder(Table table) {

		return convertColumnOrder(table.getColumnOrder());
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