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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IExtendedChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.MappingsSupport;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.SeriesListUI;
import org.eclipse.swtchart.extensions.dialogs.AbstractSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.BarSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.CircularSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.LineSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.ScatterSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.widgets.ExtendedCombo;

public class VectorExportSettingsDialog extends TitleAreaDialog {

	private AtomicReference<SeriesListUI> listControl = new AtomicReference<>();
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
	/*
	 * The use might modify the current settings.
	 * Hence, cache the original settings.
	 */
	private Map<String, ISeriesSettings> cachedSeriesSettings = new HashMap<>();
	//
	private Combo comboScaleX;
	private Combo comboScaleY;
	//
	private int indexAxisX;
	private int indexAxisY;
	//
	private BaseChart baseChart = null;

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
				ISeriesSettings seriesSettingsCopy = MappingsSupport.copySettings(seriesSettings);
				if(seriesSettingsCopy != null) {
					cachedSeriesSettings.put(id, seriesSettingsCopy);
				}
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
		seriesListUI.setBaseChart(baseChart);
		//
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				Object object = seriesListUI.getStructuredSelection().getFirstElement();
				if(object instanceof ISeries<?>) {
					/*
					 * Series
					 */
					ISeries<?> series = (ISeries<?>)object;
					ISeriesSettings seriesSettings = baseChart.getSeriesSettings(series.getId());
					Shell shell = e.display.getActiveShell();
					AbstractSeriesSettingsDialog<?> settingsDialog = null;
					/*
					 * Dialog
					 */
					if(seriesSettings instanceof IBarSeriesSettings) {
						IBarSeriesSettings settings = (IBarSeriesSettings)seriesSettings;
						settingsDialog = new BarSeriesSettingsDialog(shell, settings);
					} else if(seriesSettings instanceof ICircularSeriesSettings) {
						ICircularSeriesSettings settings = (ICircularSeriesSettings)seriesSettings;
						settingsDialog = new CircularSeriesSettingsDialog(shell, settings);
					} else if(seriesSettings instanceof ILineSeriesSettings) {
						ILineSeriesSettings settings = (ILineSeriesSettings)seriesSettings;
						settingsDialog = new LineSeriesSettingsDialog(shell, settings);
					} else if(seriesSettings instanceof IScatterSeriesSettings) {
						IScatterSeriesSettings settings = (IScatterSeriesSettings)seriesSettings;
						settingsDialog = new ScatterSeriesSettingsDialog(shell, settings);
					}
					/*
					 * Apply
					 */
					if(settingsDialog != null) {
						if(settingsDialog.open() == Window.OK) {
							applySettings(baseChart, series, seriesSettings, true);
						}
					}
				}
			}
		});
		//
		listControl.set(seriesListUI);
	}

	private void applySettings(BaseChart baseChart, ISeries<?> series, ISeriesSettings seriesSettingsSource, boolean refresh) {

		baseChart.applySeriesSettings(series, seriesSettingsSource, true);
		if(refresh) {
			baseChart.redraw();
			listControl.get().refresh();
		}
	}

	private void createSelectionAxisX(Composite container) {

		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString(Messages.X_AXIS));
		//
		String[] axisLabelsX = baseChart.getAxisLabels(IExtendedChart.X_AXIS);
		comboScaleX = ExtendedCombo.create(container, SWT.READ_ONLY);
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
		comboScaleY = ExtendedCombo.create(container, SWT.READ_ONLY);
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

	public void reset() {

		ISeries<?>[] seriesArray = baseChart.getSeriesSet().getSeries();
		for(ISeries<?> series : seriesArray) {
			String id = series.getId();
			ISeriesSettings seriesSettingsCache = cachedSeriesSettings.get(id);
			if(seriesSettingsCache != null) {
				ISeriesSettings seriesSettings = baseChart.getSeriesSettings(id);
				if(seriesSettings != null) {
					MappingsSupport.transferSettings(seriesSettingsCache, seriesSettings);
					baseChart.applySeriesSettings(series, seriesSettings, true);
				}
			}
		}
		baseChart.redraw();
	}
}