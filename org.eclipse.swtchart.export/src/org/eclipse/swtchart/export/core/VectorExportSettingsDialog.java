/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 *******************************************************************************/
package org.eclipse.swtchart.export.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.MappingsSupport;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.SeriesListUI;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;

public class VectorExportSettingsDialog extends TitleAreaDialog {

	private BaseChart baseChart;
	private Map<String, ISeriesSettings> cachedSeriesSettings = new HashMap<String, ISeriesSettings>();
	private AtomicReference<SeriesListUI> tableViewer = new AtomicReference<>();
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
	//
	private Combo comboScaleX;
	private Combo comboScaleY;
	//
	private int indexAxisX;
	private int indexAxisY;

	public VectorExportSettingsDialog(Shell parent, BaseChart baseChart) {

		super(parent);
		this.baseChart = baseChart;
		/*
		 * Cache the settings an make a deep copy
		 * as the user may modify the settings interactively.
		 */
		ISeries<?>[] seriesArray = baseChart.getSeriesSet().getSeries();
		for(ISeries<?> series : seriesArray) {
			String id = series.getId();
			ISeriesSettings seriesSettings = baseChart.getSeriesSettings(id);
			if(seriesSettings != null) {
				cachedSeriesSettings.put(id, MappingsSupport.copySettings(seriesSettings));
			}
		}
	}

	@Override
	public void create() {

		super.create();
		setTitle(Messages.getString(Messages.EXPORT_AXIS_SELECTION));
		setMessage(Messages.getString(Messages.SELECT_X_Y_TO_EXPORT), IMessageProvider.INFORMATION); // $NON-NLS-1$
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite)super.createDialogArea(parent);
		Composite container = new Composite(composite, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		//
		createSelectionAxisX(container);
		createSelectionAxisY(container);
		createSeriesList(container);
		//
		return composite;
	}

	private void createSeriesList(Composite container) {

		SeriesListUI seriesListUI = new SeriesListUI(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		seriesListUI.setTableSortable(preferenceStore.getBoolean(PreferenceConstants.P_SORT_LEGEND_TABLE));
		Table table = seriesListUI.getTable();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		table.setLayoutData(gridData);
		seriesListUI.setInput(baseChart.getSeriesSet());
		//
		tableViewer.set(seriesListUI);
	}

	private void createSelectionAxisX(Composite container) {

		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString(Messages.X_AXIS));
		//
		String[] axisLabelsX = baseChart.getAxisLabels(IExtendedChart.X_AXIS);
		comboScaleX = new Combo(container, SWT.READ_ONLY);
		comboScaleX.setItems(axisLabelsX);
		comboScaleX.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if(axisLabelsX.length > 0) {
			comboScaleX.select(0);
		}
	}

	private void createSelectionAxisY(Composite container) {

		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString(Messages.Y_AXIS));
		//
		String[] axisLabelsY = baseChart.getAxisLabels(IExtendedChart.Y_AXIS);
		comboScaleY = new Combo(container, SWT.READ_ONLY);
		comboScaleY.setItems(axisLabelsY);
		comboScaleY.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if(axisLabelsY.length > 0) {
			comboScaleY.select(0);
		}
	}

	@Override
	protected boolean isResizable() {

		return true;
	}

	private void saveInput() {

		indexAxisX = comboScaleX.getSelectionIndex();
		indexAxisY = comboScaleY.getSelectionIndex();
	}

	@Override
	protected void okPressed() {

		saveInput();
		super.okPressed();
	}

	public int getIndexAxisSelectionX() {

		return indexAxisX;
	}

	public int getIndexAxisSelectionY() {

		return indexAxisY;
	}

	public void reset(BaseChart baseChart) {

		ISeries<?>[] seriesArray = baseChart.getSeriesSet().getSeries();
		for(ISeries<?> series : seriesArray) {
			ISeriesSettings seriesSettings = cachedSeriesSettings.get(series.getId());
			if(seriesSettings != null) {
				baseChart.applySeriesSettings(series, seriesSettings);
			}
		}
	}
}