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
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.dialogs.AbstractSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.BarSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.CircularSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.LineSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.dialogs.ScatterSeriesSettingsDialog;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.menu.legend.MapSettingsAction;
import org.eclipse.swtchart.extensions.menu.legend.SeriesVisibilityAction;
import org.eclipse.swtchart.extensions.menu.legend.SetColorAction;
import org.eclipse.swtchart.extensions.menu.legend.SetDescriptionAction;
import org.eclipse.swtchart.extensions.piecharts.CircularSeriesLegend;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;
import org.eclipse.swtchart.extensions.preferences.PreferencePage;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.model.NodeDataModel;

public class ExtendedLegendUI extends Composite {

	private static final String MENU_TEXT = "Series PopUp Menu";
	//
	private AtomicReference<Button> sortControl = new AtomicReference<>();
	private AtomicReference<InChartLegendUI> toolbarInChartLegend = new AtomicReference<>();
	private AtomicReference<SeriesListUI> listControl = new AtomicReference<>();
	//
	private ScrollableChart scrollableChart;
	private ISeriesSet seriesSet;
	//
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();

	public ExtendedLegendUI(Composite parent, int style) {

		super(parent, style);
		createControl();
	}

	public void setScrollableChart(ScrollableChart scrollableChart) {

		this.scrollableChart = scrollableChart;
		toolbarInChartLegend.get().setScrollableChart(scrollableChart);
		listControl.get().setBaseChart(scrollableChart.getBaseChart());
	}

	public void setInput(ISeriesSet seriesSet) {

		this.seriesSet = seriesSet;
		updateSeriesList();
	}

	private void createControl() {

		setLayout(new GridLayout(1, true));
		//
		createToolbarMain(this);
		createToolbarInChartLegend(this);
		createListSection(this);
		//
		initialize();
	}

	private void initialize() {

		setCompositeVisibility(toolbarInChartLegend.get(), false);
		updateControls();
		applySettings();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(6, false));
		//
		createButtonToggleVisibility(composite);
		createButtonToggleLegend(composite);
		createButtonToggleSort(composite);
		createButtonTransferMappings(composite);
		createButtonShowMappings(composite);
		createButtonSettings(composite);
	}

	private void createToolbarInChartLegend(Composite parent) {

		InChartLegendUI inChartLegendUI = new InChartLegendUI(parent, SWT.NONE);
		inChartLegendUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		toolbarInChartLegend.set(inChartLegendUI);
	}

	private Button createButtonToggleVisibility(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Toggle visibility.");
		button.setImage(getVisibilityIcon(true));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				BaseChart baseChart = scrollableChart.getBaseChart();
				boolean visible = anyVisible(seriesSet);
				for(ISeries<?> series : seriesSet.getSeries()) {
					ISeriesSettings seriesSettings = baseChart.getSeriesSettings(series.getId());
					boolean selection = !visible;
					seriesSettings.setVisible(selection);
					seriesSettings.setVisibleInLegend(selection);
					applySettings(baseChart, series, seriesSettings, false);
				}
				//
				scrollableChart.redraw();
				button.setImage(getVisibilityIcon(!visible));
				listControl.get().refresh();
			}
		});
		//
		return button;
	}

	private boolean anyVisible(ISeriesSet seriesSet) {

		for(ISeries<?> series : seriesSet.getSeries()) {
			if(series.isVisible() || series.isVisibleInLegend()) {
				return true;
			}
		}
		return false;
	}

	private Image getVisibilityIcon(boolean visible) {

		return visible ? ResourceSupport.getImage(ResourceSupport.ICON_UNCHECK_ALL) : ResourceSupport.getImage(ResourceSupport.ICON_CHECK_ALL);
	}

	private Button createButtonToggleLegend(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Toggle the visibility of the embedded legend.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_LEGEND));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(isCircularSeries()) {
					IChartSettings chartSettings = scrollableChart.getChartSettings();
					chartSettings.setLegendVisible(!chartSettings.isLegendVisible());
					scrollableChart.applySettings(chartSettings);
				} else {
					InChartLegendUI inChartLegendUI = toolbarInChartLegend.get();
					setCompositeVisibility(inChartLegendUI, inChartLegendUI.toggleLegend());
				}
			}
		});
		//
		return button;
	}

	private void createButtonToggleSort(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Sort the table.");
		button.setImage(getSortedIcon(preferenceStore.getBoolean(PreferenceConstants.P_SORT_LEGEND_TABLE)));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean sorted = preferenceStore.getBoolean(PreferenceConstants.P_SORT_LEGEND_TABLE);
				preferenceStore.setValue(PreferenceConstants.P_SORT_LEGEND_TABLE, !sorted);
				ResourceSupport.savePreferenceStore();
				updateButtonSortImage();
				updateSeriesTableSortStatus();
			}
		});
		//
		sortControl.set(button);
	}

	private Image getSortedIcon(boolean sorted) {

		return sorted ? ResourceSupport.getImage(ResourceSupport.ICON_SORT_ENABLED) : ResourceSupport.getImage(ResourceSupport.ICON_SORT_DISABLED);
	}

	private Button createButtonTransferMappings(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Transfer the mappings of the selected series.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_TRANSFER));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if(MessageDialog.openQuestion(e.display.getActiveShell(), "Mappings", "Would you like to map all listed series?")) {
					if(seriesSet != null) {
						BaseChart baseChart = scrollableChart.getBaseChart();
						for(ISeries<?> series : seriesSet.getSeries()) {
							SeriesMapper.map(series, baseChart);
						}
					}
				}
			}
		});
		//
		return button;
	}

	private Button createButtonShowMappings(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Display the mappings.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_MAPPINGS));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				MappingsDialog mappingsDialog = new MappingsDialog(e.display.getActiveShell());
				int returnCode = mappingsDialog.open();
				if(returnCode == IDialogConstants.OK_ID) {
					SeriesMapper.update(scrollableChart.getBaseChart());
					updateSeriesList();
				}
			}
		});
		//
		return button;
	}

	private Button createButtonSettings(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Open the settings page.");
		button.setImage(ResourceSupport.getImage(ResourceSupport.ICON_SETTINGS));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				PreferenceManager preferenceManager = new PreferenceManager();
				preferenceManager.addToRoot(new PreferenceNode("1", new PreferencePage()));
				PreferenceDialog preferenceDialog = new PreferenceDialog(e.display.getActiveShell(), preferenceManager);
				preferenceDialog.create();
				preferenceDialog.setMessage("Settings");
				if(preferenceDialog.open() == Window.OK) {
					try {
						applySettings();
					} catch(Exception e1) {
						MessageDialog.openError(e.display.getActiveShell(), "Settings", "Something has gone wrong to apply the settings.");
					}
				}
			}
		});
		//
		return button;
	}

	private void applySettings() {

		updateButtonSortImage();
		updateSeriesTableSortStatus();
		toolbarInChartLegend.get().update();
	}

	private void createListSection(Composite parent) {

		SeriesListUI seriesListUI = new SeriesListUI(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		seriesListUI.setTableSortable(preferenceStore.getBoolean(PreferenceConstants.P_SORT_LEGEND_TABLE));
		Table table = seriesListUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		/*
		 * Pop-Up Menu
		 */
		String menuId = getClass().getCanonicalName();
		MenuManager menuManager = new MenuManager(MENU_TEXT, menuId);
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new SeriesVisibilityAction(seriesListUI, false, false));
		menuManager.addMenuListener(new SeriesVisibilityAction(seriesListUI, true, false));
		menuManager.addMenuListener(new SeriesVisibilityAction(seriesListUI, false, true));
		menuManager.addMenuListener(new SeriesVisibilityAction(seriesListUI, true, true));
		menuManager.addMenuListener(new SetColorAction(seriesListUI));
		menuManager.addMenuListener(new SetDescriptionAction(seriesListUI));
		menuManager.addMenuListener(new MapSettingsAction(seriesListUI, true));
		menuManager.addMenuListener(new MapSettingsAction(seriesListUI, false));
		Menu menu = menuManager.createContextMenu(table);
		table.setMenu(menu);
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
					BaseChart baseChart = scrollableChart.getBaseChart();
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

	private void updateControls() {

		updateButtonSortImage();
		toolbarInChartLegend.get().update();
	}

	private void updateButtonSortImage() {

		sortControl.get().setImage(getSortedIcon(preferenceStore.getBoolean(PreferenceConstants.P_SORT_LEGEND_TABLE)));
	}

	private void updateSeriesTableSortStatus() {

		SeriesListUI seriesListUI = listControl.get();
		seriesListUI.setTableSortable(preferenceStore.getBoolean(PreferenceConstants.P_SORT_LEGEND_TABLE));
		seriesListUI.getTable().redraw();
	}

	private void applySettings(BaseChart baseChart, ISeries<?> series, ISeriesSettings seriesSettingsSource, boolean refresh) {

		baseChart.applySeriesSettings(series, seriesSettingsSource, true);
		if(refresh) {
			baseChart.redraw();
			listControl.get().refresh();
		}
	}

	private void updateSeriesList() {

		if(seriesSet != null) {
			ICircularSeries<?> circularSeries = getCircularSeries();
			if(circularSeries != null) {
				listControl.get().setInput(getCalculatedCircularSeries(circularSeries));
			} else {
				listControl.get().setInput(seriesSet);
			}
		} else {
			listControl.get().clear();
		}
	}

	private List<ISeries<?>> getCalculatedCircularSeries(ICircularSeries<?> circularSeries) {

		List<ISeries<?>> seriesList = new ArrayList<>();
		String[] labels = circularSeries.getLabels();
		if(labels != null) {
			NodeDataModel nodeDataModel = circularSeries.getNodeDataModel();
			if(nodeDataModel != null) {
				for(String label : labels) {
					Node node = nodeDataModel.getNodeById(label);
					if(node != null) {
						seriesList.add(new CircularSeriesLegend<>(node, nodeDataModel));
					}
				}
			}
		}
		//
		return seriesList;
	}

	private ICircularSeries<?> getCircularSeries() {

		ICircularSeries<?> circularSeries = null;
		if(seriesSet != null) {
			if(seriesSet.getSeries().length > 0) {
				ISeries<?> series = seriesSet.getSeries()[0];
				if(series instanceof ICircularSeries<?>) {
					circularSeries = (ICircularSeries<?>)series;
				}
			}
		}
		//
		return circularSeries;
	}

	private boolean isCircularSeries() {

		return getCircularSeries() != null;
	}

	private void setCompositeVisibility(Composite composite, boolean visible) {

		if(composite != null) {
			composite.setVisible(visible);
			Object layoutData = composite.getLayoutData();
			if(layoutData instanceof GridData) {
				GridData gridData = (GridData)layoutData;
				gridData.exclude = !visible;
			}
			Composite parent = composite.getParent();
			parent.layout(true);
			parent.redraw();
		}
	}
}