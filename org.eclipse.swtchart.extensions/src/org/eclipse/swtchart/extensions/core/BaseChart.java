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
 * Philip Wenig - initial API and implementation
 * Frank Buloup - Internationalization
 * Himanshu Balasamanta - Circular Charts
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ICircularSeries;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.clipboard.IImageClipboardSupplier;
import org.eclipse.swtchart.extensions.dialogs.ClickBindingHelpDialog;
import org.eclipse.swtchart.extensions.events.CircularMouseDownEvent;
import org.eclipse.swtchart.extensions.events.IEventProcessor;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.model.CustomSeries;
import org.eclipse.swtchart.extensions.model.ICustomSeries;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.model.Node;
import org.eclipse.swtchart.model.NodeDataModel;

public class BaseChart extends AbstractExtendedChart implements IChartDataCoordinates, IRangeSupport, IExtendedChart, IKeyboardSupport {

	public static final int ID_PRIMARY_X_AXIS = 0;
	public static final int ID_PRIMARY_Y_AXIS = 0;
	public static final String DEFAULT_TITLE_X_AXIS = Messages.getString(Messages.X_AXIS);
	public static final String DEFAULT_TITLE_Y_AXIS = Messages.getString(Messages.Y_AXIS);
	//
	public static final String SELECTED_SERIES_NONE = Messages.getString(Messages.NONE);
	/*
	 * see: IHandledEventProcessor
	 * Map<Integer, Map<Integer, Map<Integer, List<IEventProcessor>>>>
	 * Map<Event, Map<Button, Map<StateMask, List<IEventProcessor>>>>
	 * e.g.:
	 * Integer: EVENT_MOUSE_DOUBLE_CLICK (Event)
	 * Integer: BUTTON_NONE (Button)
	 * Integer: SWT.MOD1 (StateMask)
	 */
	private Map<Integer, Map<Integer, Map<Integer, List<IEventProcessor>>>> registeredEvents;
	/*
	 * Settings
	 */
	private IChartSettings chartSettings;
	/*
	 * Prevent accidental zooming.
	 * At least 30% of the chart width or height needs to be selected.
	 */
	private static final int MIN_SELECTION_PERCENTAGE = 30;
	public static final long DELTA_CLICK_TIME = 100;
	/*
	 * To prevent that the data is redrawn on mouse events too
	 * often, a trigger determines e.g. that the redraw event
	 * is called only at every xth event.
	 */
	private int redrawFrequency = 1;
	private int redrawCounter = 0;
	//
	private List<ICustomSelectionHandler> customRangeSelectionHandlers;
	private List<ICustomSelectionHandler> customPointSelectionHandlers;
	private List<ISeriesModificationListener> seriesModificationListeners;
	private List<ISeriesStatusListener> seriesStatusListeners;
	//
	private UserSelection userSelection;
	private long clickStartTime;
	private Set<String> selectedSeriesIds;
	/*
	 * Do/Undo -1
	 */
	private Stack<double[]> handledSelectionEvents;
	private double[] redoSelection;
	/*
	 * Shift series
	 */
	public static final int SHIFT_CONSTRAINT_NONE = 0;
	public static final int SHIFT_CONSTRAINT_RANGE_SELECTION = 1 << 0;
	public static final int SHIFT_CONSTRAINT_DELETE_X = 1 << 1;
	public static final int SHIFT_CONSTRAINT_DELETE_Y = 1 << 2;
	public static final int SHIFT_CONSTRAINT_CLINCH_X = 1 << 3;
	public static final int SHIFT_CONSTRAINT_STRETCH_X = 1 << 4;
	public static final int SHIFT_CONSTRAINT_BROADEN_X = 1 << 5;
	public static final int SHIFT_CONSTRAINT_NARROW_X = 1 << 6;
	//
	private int shiftConstraints = SHIFT_CONSTRAINT_NONE;
	//
	public static final long DELTA_MOVE_TIME = 350;
	private long moveStartTime = 0;
	private int xMoveStart = 0;
	private int yMoveStart = 0;
	private Map<String, List<double[]>> dataShiftHistory;
	/*
	 * This supplier returns a specific Copy & Paste clipboard content on demand.
	 */
	private IImageClipboardSupplier imageClipboardSupplier = null;
	/*
	 * Custom Paint Series (Experimental)
	 */
	private List<ICustomSeries> customSeriesList = new ArrayList<>();
	//
	private IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
	//
	private ClickBindingHelpDialog clickBindingPopup;

	public BaseChart(Composite parent, int style) {

		super(parent, style);
		//
		chartSettings = new ChartSettings();
		/*
		 * Rectangle range selection.
		 */
		userSelection = new UserSelection();
		customRangeSelectionHandlers = new ArrayList<>();
		customPointSelectionHandlers = new ArrayList<>();
		seriesModificationListeners = new ArrayList<>();
		seriesStatusListeners = new ArrayList<>();
		selectedSeriesIds = new HashSet<>();
		initializeEventProcessors();
		/*
		 * Create the default x and y axis.
		 */
		IAxisSet axisSet = getAxisSet();
		//
		IAxis xAxisPrimary = axisSet.getXAxis(ID_PRIMARY_X_AXIS);
		ITitle titleX = xAxisPrimary.getTitle();
		titleX.setText(DEFAULT_TITLE_X_AXIS);
		titleX.setVisible(true);
		xAxisPrimary.setPosition(Position.Primary);
		xAxisPrimary.getTick().setFormat(new DecimalFormat());
		xAxisPrimary.enableLogScale(false);
		xAxisPrimary.enableCategory(false);
		xAxisPrimary.setReversed(false);
		xAxisPrimary.setDrawAxisLine(true);
		xAxisPrimary.setCategorySeries(new String[]{});
		xAxisPrimary.setIntegerDataPointAxis(false);
		//
		IAxis yAxisPrimary = axisSet.getYAxis(ID_PRIMARY_Y_AXIS);
		ITitle titleY = yAxisPrimary.getTitle();
		titleY.setText(DEFAULT_TITLE_Y_AXIS);
		titleY.setVisible(true);
		yAxisPrimary.setPosition(Position.Primary);
		yAxisPrimary.getTick().setFormat(new DecimalFormat());
		yAxisPrimary.enableLogScale(false);
		yAxisPrimary.enableCategory(false);
		yAxisPrimary.setReversed(false);
		yAxisPrimary.setDrawAxisLine(true);
		yAxisPrimary.setIntegerDataPointAxis(false);
		//
		handledSelectionEvents = new Stack<>();
		redoSelection = null;
		//
		dataShiftHistory = new HashMap<>();
		//
		setData("org.eclipse.e4.ui.css.CssClassName", "BaseChart"); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * Draw the custom paint series elements (Experimental).
		 * Not yet implemented.
		 */
		// addPaintListener(new CustomSeriesMarker(this));
	}

	/**
	 * (Experimental)
	 * Returns an unmodifiable list of the custom paint series.
	 * 
	 * @return List<ICustomPaintSeries>
	 */
	public List<ICustomSeries> getCustomSeries() {

		return Collections.unmodifiableList(customSeriesList);
	}

	/**
	 * (Experimental)
	 * Create a custom series.
	 * 
	 * @param label
	 * @param description
	 * @return
	 */
	public ICustomSeries createCustomSeries(String label, String description) {

		ICustomSeries customSeries = new CustomSeries();
		customSeries.setLabel(label);
		customSeries.setDescription(description);
		customSeriesList.add(customSeries);
		//
		return customSeries;
	}

	/**
	 * (Experimental)
	 * Delete the custom series.
	 * 
	 * @param id
	 */
	public void deleteCustomSeries(String id) {

		ICustomSeries customSeriesDelete = null;
		exitloop:
		for(ICustomSeries customSeries : customSeriesList) {
			if(customSeries.getId().equals(id)) {
				customSeriesDelete = customSeries;
				break exitloop;
			}
		}
		//
		if(customSeriesDelete != null) {
			customSeriesList.remove(customSeriesDelete);
		}
	}

	private void initializeEventProcessors() {

		registeredEvents = new HashMap<>();
		initializeEvents();
	}

	private void initializeEvents() {

		registeredEvents.put(EVENT_MOUSE_DOUBLE_CLICK, new HashMap<>());
		registeredEvents.put(EVENT_MOUSE_WHEEL, new HashMap<>());
		registeredEvents.put(EVENT_MOUSE_DOWN, new HashMap<>());
		registeredEvents.put(EVENT_MOUSE_MOVE, new HashMap<>());
		registeredEvents.put(EVENT_MOUSE_UP, new HashMap<>());
		registeredEvents.put(EVENT_KEY_DOWN, new HashMap<>());
		registeredEvents.put(EVENT_KEY_UP, new HashMap<>());
	}

	public void clearEventProcessors() {

		registeredEvents.clear();
		initializeEvents();
	}

	public void resetSeriesSettings(ISeries<?> series) {

		resetSeriesSettings(series.getId());
	}

	public void addEventProcessor(IHandledEventProcessor handledEventProcessor) {

		/*
		 * Example
		 * Event.: EVENT_MOUSE_DOUBLE_CLICK
		 */
		Map<Integer, Map<Integer, List<IEventProcessor>>> eventProcessors = registeredEvents.get(handledEventProcessor.getEvent());
		/*
		 * Example
		 * Button: BUTTON_LEFT
		 */
		int button = handledEventProcessor.getButton();
		Map<Integer, List<IEventProcessor>> buttonEventProcessors = eventProcessors.get(button);
		if(buttonEventProcessors == null) {
			buttonEventProcessors = new HashMap<>();
			eventProcessors.put(button, buttonEventProcessors);
		}
		/*
		 * Example
		 * StateMask: SWT.MOD1
		 */
		int stateMask = handledEventProcessor.getStateMask();
		List<IEventProcessor> handledEventProcessors = buttonEventProcessors.get(stateMask);
		if(handledEventProcessors == null) {
			handledEventProcessors = new ArrayList<>();
		}
		handledEventProcessors.add(handledEventProcessor);
		buttonEventProcessors.put(stateMask, handledEventProcessors);
	}

	public void setChartSettings(IChartSettings chartSettings) {

		this.chartSettings = chartSettings;
	}

	public IChartSettings getChartSettings() {

		return chartSettings;
	}

	public long getMoveStartTime() {

		return moveStartTime;
	}

	public void setMoveStartTime(long moveStartTime) {

		this.moveStartTime = moveStartTime;
	}

	public int getXMoveStart() {

		return xMoveStart;
	}

	public void setXMoveStart(int xMoveStart) {

		this.xMoveStart = xMoveStart;
	}

	public int getYMoveStart() {

		return yMoveStart;
	}

	public void setYMoveStart(int yMoveStart) {

		this.yMoveStart = yMoveStart;
	}

	public UserSelection getUserSelection() {

		return userSelection;
	}

	public void increaseRedrawCounter() {

		redrawCounter++;
	}

	public void resetRedrawCounter() {

		redrawCounter = 0;
	}

	public long getClickStartTime() {

		return clickStartTime;
	}

	public void setClickStartTime(long clickStartTime) {

		this.clickStartTime = clickStartTime;
	}

	public boolean isRedraw() {

		return (redrawCounter >= redrawFrequency);
	}

	public double getShiftValue(int positionStart, int positionStop, String orientation) {

		double shiftValue = 0.0d;
		double start;
		double stop;
		int length;
		/*
		 * Get the axis.
		 */
		if(orientation.equals(IExtendedChart.X_AXIS)) {
			IAxis axis = getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
			start = axis.getRange().lower;
			stop = axis.getRange().upper;
			length = getPlotArea().getSize().x;
		} else {
			IAxis axis = getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
			start = axis.getRange().lower;
			stop = axis.getRange().upper;
			length = getPlotArea().getSize().y;
		}
		//
		if(positionStart > 0 && positionStop > 0 && positionStart < length && positionStop < length) {
			//
			double delta = stop - start;
			double percentageStart;
			double percentageStop;
			//
			if(orientation.equals(IExtendedChart.X_AXIS)) {
				percentageStart = ((100.0d / length) * positionStart) / 100.0d;
				percentageStop = ((100.0d / length) * positionStop) / 100.0d;
			} else {
				percentageStart = (100.0d - ((100.0d / length) * positionStart)) / 100.0d;
				percentageStop = (100.0d - ((100.0d / length) * positionStop)) / 100.0d;
			}
			//
			shiftValue = (start + delta * percentageStop) - (start + delta * percentageStart);
		}
		return shiftValue;
	}

	@Override
	public ISeries<?> createSeries(ISeriesData seriesData, ISeriesSettings seriesSettings) throws SeriesException {

		ISeries<?> series = super.createSeries(seriesData, seriesSettings);
		calculateRedrawFrequency();
		return series;
	}

	@Override
	public void deleteSeries(String id) {

		super.deleteSeries(id);
		calculateRedrawFrequency();
		dataShiftHistory.remove(id);
	}

	@Override
	public void deleteSeries() {

		super.deleteSeries();
		calculateRedrawFrequency();
		dataShiftHistory.clear();
	}

	@Override
	public void appendSeries(ISeriesData seriesData) {

		super.appendSeries(seriesData);
		calculateRedrawFrequency();
	}

	private void calculateRedrawFrequency() {

		/*
		 * The frequency might be calculated here to increase the performance.
		 * I've not found a smart solution yet to improve the speed when
		 * displaying large data sets.
		 */
		redrawFrequency = 2;
	}

	public double getSelectedPrimaryAxisValue(int position, String orientation) {

		double primaryValue;
		double start;
		double stop;
		int length;
		//
		if(orientation.equals(IExtendedChart.X_AXIS)) {
			IAxis axis = getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
			if(axis.isReversed()) {
				start = axis.getRange().upper;
				stop = axis.getRange().lower;
			} else {
				start = axis.getRange().lower;
				stop = axis.getRange().upper;
			}
			length = getPlotArea().getSize().x;
		} else {
			IAxis axis = getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
			if(axis.isReversed()) {
				start = axis.getRange().upper;
				stop = axis.getRange().lower;
			} else {
				start = axis.getRange().lower;
				stop = axis.getRange().upper;
			}
			length = getPlotArea().getSize().y;
		}
		//
		if(position <= 0) {
			primaryValue = start;
		} else if(position > length) {
			primaryValue = stop;
		} else {
			double delta = stop - start;
			double percentage;
			if(orientation.equals(IExtendedChart.X_AXIS)) {
				percentage = ((100.0d / length) * position) / 100.0d;
			} else {
				percentage = (100.0d - ((100.0d / length) * position)) / 100.0d;
			}
			primaryValue = start + delta * percentage;
		}
		return primaryValue;
	}

	public boolean addCustomRangeSelectionHandler(ICustomSelectionHandler customSelectionHandler) {

		return customRangeSelectionHandlers.add(customSelectionHandler);
	}

	public boolean removeCustomRangeSelectionHandler(ICustomSelectionHandler customSelectionHandler) {

		return customRangeSelectionHandlers.remove(customSelectionHandler);
	}

	public boolean addCustomPointSelectionHandler(ICustomSelectionHandler customSelectionHandler) {

		return customPointSelectionHandlers.add(customSelectionHandler);
	}

	public boolean removeCustomPointSelectionHandler(ICustomSelectionHandler customSelectionHandler) {

		return customPointSelectionHandlers.remove(customSelectionHandler);
	}

	public boolean addSeriesModificationListener(ISeriesModificationListener seriesModificationListener) {

		return seriesModificationListeners.add(seriesModificationListener);
	}

	public boolean removeSeriesModificationListener(ISeriesModificationListener seriesModificationListener) {

		return seriesModificationListeners.remove(seriesModificationListener);
	}

	public boolean addSeriesStatusListener(ISeriesStatusListener seriesSelectionListener) {

		return seriesStatusListeners.add(seriesSelectionListener);
	}

	public boolean removeSeriesStatusListener(ISeriesStatusListener seriesSelectionListener) {

		return seriesStatusListeners.remove(seriesSelectionListener);
	}

	/**
	 * Returns the set of selected series ids.
	 * The list is unmodifiable.
	 * 
	 * @return Set<String>
	 */
	public Set<String> getSelectedSeriesIds() {

		return Collections.unmodifiableSet(selectedSeriesIds);
	}

	/**
	 * Returns a set of all series ids.
	 * 
	 * @return Set<String>
	 */
	public Set<String> getSeriesIds() {

		Set<String> seriesIds = new HashSet<>();
		ISeries<?>[] series = getSeriesSet().getSeries();
		for(ISeries<?> serie : series) {
			seriesIds.add(serie.getId());
		}
		//
		return seriesIds;
	}

	public boolean isSeriesContained(String seriesId) {

		return getSeriesSet().getSeries(seriesId) != null;
	}

	@Override
	public void paintControl(PaintEvent e) {

		if(userSelection.isActive()) {
			/*
			 * Draw the rectangle of the user selection.
			 */
			int currentLineStyle = e.gc.getLineStyle();
			e.gc.setLineStyle(SWT.LINE_DOT);
			//
			int xMin = Math.min(userSelection.getStartX(), userSelection.getStopX());
			int xMax = Math.max(userSelection.getStartX(), userSelection.getStopX());
			int yMin = Math.min(userSelection.getStartY(), userSelection.getStopY());
			int yMax = Math.max(userSelection.getStartY(), userSelection.getStopY());
			//
			RangeRestriction rangeRestriction = getRangeRestriction();
			if(isSelectXY(rangeRestriction)) {
				/*
				 * X and Y zoom.
				 */
				e.gc.drawRectangle(xMin, yMin, xMax - xMin, yMax - yMin);
			} else {
				/*
				 * X or Y zoom.
				 */
				if(rangeRestriction.isRestrictSelectX()) {
					e.gc.drawLine(xMin, yMin, xMax, yMin);
				} else if(rangeRestriction.isRestrictSelectY()) {
					e.gc.drawLine(xMin, yMin, xMin, yMax);
				}
			}
			//
			e.gc.setLineStyle(currentLineStyle);
		}
	}

	@Override
	public void handleMouseDownEvent(Event event) {

		handleEvent(registeredEvents.get(EVENT_MOUSE_DOWN).get(event.button), event);
	}

	@Override
	public void handleMouseMoveEvent(Event event) {

		handleEvent(registeredEvents.get(EVENT_MOUSE_MOVE).get(MOUSE_BUTTON_NONE), event);
	}

	@Override
	public void handleMouseUpEvent(Event event) {

		handleEvent(registeredEvents.get(EVENT_MOUSE_UP).get(event.button), event);
	}

	@Override
	public void handleMouseWheel(Event event) {

		handleEvent(registeredEvents.get(EVENT_MOUSE_WHEEL).get(MOUSE_BUTTON_WHEEL), event);
	}

	@Override
	public void handleMouseDoubleClick(Event event) {

		handleEvent(registeredEvents.get(EVENT_MOUSE_DOUBLE_CLICK).get(event.button), event);
	}

	@Override
	public void handleKeyDownEvent(Event event) {

		handleEvent(registeredEvents.get(EVENT_KEY_DOWN).get(event.keyCode), event);
	}

	@Override
	public void handleKeyUpEvent(Event event) {

		handleEvent(registeredEvents.get(EVENT_KEY_UP).get(event.keyCode), event);
	}

	private void handleEvent(Map<Integer, List<IEventProcessor>> eventProcessorMap, Event event) {

		if(eventProcessorMap != null) {
			if(event.stateMask == SWT.NONE) {
				/*
				 * Default processor.
				 * The stateMask == 0 is handled differently.
				 */
				List<IEventProcessor> eventProcessors = eventProcessorMap.get(SWT.NONE);
				handleEventProcessors(eventProcessors, event);
			} else {
				/*
				 * Handle all other stateMasks.
				 */
				exitloop:
				for(int eventMask : eventProcessorMap.keySet()) {
					/*
					 * Skip the default processor.
					 */
					if(eventMask == SWT.NONE) {
						continue;
					}
					//
					if((event.stateMask & eventMask) == eventMask) {
						List<IEventProcessor> eventProcessors = eventProcessorMap.get(eventMask);
						handleEventProcessors(eventProcessors, event);
						break exitloop;
					}
				}
			}
		}
	}

	private void handleEventProcessors(List<IEventProcessor> eventProcessors, Event event) {

		if(eventProcessors != null) {
			for(IEventProcessor eventProcessor : eventProcessors) {
				/*
				 * Handle the event.
				 */
				if(eventProcessor != null) {
					eventProcessor.handleEvent(this, event);
				}
			}
		}
	}

	public void openShortcutPopup(String shortcut, String name, String description) {

		if(!preferenceStore.getBoolean(PreferenceConstants.P_SHOW_HELP_FOR_EVENTS)) {
			return;
		}
		closeShortcutPopup();
		int timeToClose = preferenceStore.getInt(PreferenceConstants.P_HELP_POPUP_TIME_TO_CLOSE);
		clickBindingPopup = new ClickBindingHelpDialog(getShell(), timeToClose);
		clickBindingPopup.setShortcut(shortcut, name, description);
		clickBindingPopup.open();
	}

	private void closeShortcutPopup() {

		if(clickBindingPopup != null) {
			clickBindingPopup.close();
			clickBindingPopup = null;
		}
	}

	public void selectSeries(String selectedSeriesId) {

		selectSeries(selectedSeriesId, true);
	}

	public void selectSeries(String selectedSeriesId, boolean fireUpdate) {

		if(isSeriesContained(selectedSeriesId)) {
			ISeries<?> dataSeries = getSeriesSet().getSeries(selectedSeriesId);
			ISeriesSettings seriesSettings = getSeriesSettings(selectedSeriesId);
			selectedSeriesIds.add(selectedSeriesId);
			applySeriesSettings(dataSeries, seriesSettings.getSeriesSettingsHighlight());
			if(fireUpdate) {
				fireSeriesStatusEvent(selectedSeriesId, ISeriesStatusListener.SELECT);
			}
		}
	}

	public void hideSeries(String selectedSeriesId) {

		hideSeries(selectedSeriesId, true);
	}

	public void hideSeries(String selectedSeriesId, boolean fireUpdate) {

		ISeries<?> dataSeries = getSeriesSet().getSeries(selectedSeriesId);
		if(dataSeries != null) {
			selectedSeriesIds.remove(selectedSeriesId);
			dataSeries.setVisible(false);
			dataSeries.setVisibleInLegend(false);
			if(fireUpdate) {
				fireSeriesStatusEvent(selectedSeriesId, ISeriesStatusListener.HIDE);
			}
		}
	}

	public void resetSeriesSettings() {

		resetSeriesSettings(true);
	}

	public void resetSeriesSettings(boolean fireUpdate) {

		applySeriesSettings();
		selectedSeriesIds.clear();
		redraw();
		if(fireUpdate) {
			fireSeriesStatusEvent(SELECTED_SERIES_NONE, ISeriesStatusListener.RESET);
		}
	}

	public void showSeries(String selectedSeriesId) {

		ISeries<?> dataSeries = getSeriesSet().getSeries(selectedSeriesId);
		if(dataSeries != null) {
			dataSeries.setVisible(true);
			dataSeries.setVisibleInLegend(true);
		}
	}

	public void applySeriesSettings() {

		ISeries<?>[] seriesSet = getSeriesSet().getSeries();
		for(ISeries<?> series : seriesSet) {
			ISeriesSettings seriesSettings = getSeriesSettings(series.getId());
			applySeriesSettings(series, seriesSettings);
		}
	}

	public void applySeriesSettings(ISeries<?> series, ISeriesSettings seriesSettings) {

		applySeriesSettings(series, seriesSettings, false);
	}

	/**
	 * If updateAvailableMapping is true, an existing mapping will be updated by the given series settings.
	 * Otherwise, the series settings will be replaced by a mapped settings if it exists.
	 * 
	 * @param series
	 * @param seriesSettings
	 * @param updateAvailableMapping
	 */
	public void applySeriesSettings(ISeries<?> series, ISeriesSettings seriesSettings, boolean updateAvailableMapping) {

		/*
		 * Get the mapped series settings.
		 */
		ISeriesSettings seriesSettingsMapping = SeriesMapper.get(series, this);
		if(seriesSettingsMapping != null) {
			/*
			 * Original Description
			 */
			String originalDescriptionSeries = seriesSettings.getDescription();
			String originalDescriptionMapping = seriesSettingsMapping.getDescription();
			//
			if(seriesSettings.isHighlight()) {
				/*
				 * Highlight Series Settings
				 */
				if(updateAvailableMapping) {
					MappingsSupport.transferSettings(seriesSettings, seriesSettingsMapping.getSeriesSettingsHighlight());
				} else {
					MappingsSupport.transferSettings(seriesSettingsMapping.getSeriesSettingsHighlight(), seriesSettings);
				}
			} else {
				/*
				 * Normal Series Settings
				 */
				if(updateAvailableMapping) {
					MappingsSupport.transferSettings(seriesSettings, seriesSettingsMapping);
				} else {
					MappingsSupport.transferSettings(seriesSettingsMapping, seriesSettings);
				}
			}
			/*
			 * Keep the original description on demand.
			 */
			if(preferenceStore != null) {
				if(preferenceStore.getBoolean(PreferenceConstants.P_KEEP_SERIES_DESCRIPTION)) {
					if(updateAvailableMapping) {
						seriesSettingsMapping.setDescription(originalDescriptionMapping);
					} else {
						seriesSettings.setDescription(originalDescriptionSeries);
					}
				}
			}
		}
		/*
		 * Apply series settings.
		 */
		if(series instanceof ILineSeries) {
			ILineSeries<?> lineSeries = (ILineSeries<?>)series;
			if(seriesSettings instanceof ILineSeriesSettings lineSeriesSettings) {
				/*
				 * Line Series
				 */
				applyLineSeriesSettings(lineSeries, lineSeriesSettings);
			} else if(seriesSettings instanceof IScatterSeriesSettings scatterSeriesSettings) {
				/*
				 * Scatter Series
				 */
				applyScatterSeriesSettings(lineSeries, scatterSeriesSettings);
			}
		} else if(series instanceof IBarSeries) {
			IBarSeries<?> barSeries = (IBarSeries<?>)series;
			if(seriesSettings instanceof IBarSeriesSettings barSeriesSettings) {
				/*
				 * Bar Series
				 */
				applyBarSeriesSettings(barSeries, barSeriesSettings);
			}
		} else if(series instanceof ICircularSeries) {
			ICircularSeries<?> circularSeries = (ICircularSeries<?>)series;
			if(seriesSettings instanceof ICircularSeriesSettings circularSeriesSettings) {
				/*
				 * Pie Series
				 */
				applyCircularSeriesSettings(circularSeries, circularSeriesSettings);
				//
				String id = circularSeries.getId();
				NodeDataModel nodeDataModel = circularSeries.getNodeDataModel();
				if(nodeDataModel != null) {
					Node node = nodeDataModel.getNodeById(id);
					if(node != null) {
						Node highlightedNode = circularSeries.getHighlightedNode();
						if(node == highlightedNode) {
							ICircularSeriesSettings circularSeriesSettingsHighlight = (ICircularSeriesSettings)seriesSettings.getSeriesSettingsHighlight();
							node.setVisible(circularSeriesSettingsHighlight.isVisible());
							node.setVisibleInLegend(circularSeriesSettingsHighlight.isVisibleInLegend());
							node.setSliceColor(circularSeriesSettingsHighlight.getSliceColor());
							node.setDescription(circularSeriesSettingsHighlight.getDescription());
						} else {
							node.setVisible(circularSeriesSettings.isVisible());
							node.setVisibleInLegend(circularSeriesSettings.isVisibleInLegend());
							node.setSliceColor(circularSeriesSettings.getSliceColor());
							node.setDescription(circularSeriesSettings.getDescription());
						}
					}
				}
			}
		}
	}

	private void applyLineSeriesSettings(ILineSeries<?> lineSeries, ILineSeriesSettings lineSeriesSettings) {

		applyBaseSeriesSettings(lineSeries, lineSeriesSettings);
		lineSeries.setAntialias(lineSeriesSettings.getAntialias());
		lineSeries.enableArea(lineSeriesSettings.isEnableArea());
		lineSeries.setSymbolType(lineSeriesSettings.getSymbolType());
		lineSeries.setSymbolSize(lineSeriesSettings.getSymbolSize());
		lineSeries.setSymbolColor(lineSeriesSettings.getSymbolColor());
		lineSeries.setLineColor(lineSeriesSettings.getLineColor());
		lineSeries.setLineWidth(lineSeriesSettings.getLineWidth());
		lineSeries.enableStack(lineSeriesSettings.isEnableStack());
		lineSeries.enableStep(lineSeriesSettings.isEnableStep());
		lineSeries.setLineStyle(lineSeriesSettings.getLineStyle());
	}

	private void applyScatterSeriesSettings(ILineSeries<?> scatterSeries, IScatterSeriesSettings scatterSeriesSettings) {

		applyBaseSeriesSettings(scatterSeries, scatterSeriesSettings);
		scatterSeries.enableArea(false);
		scatterSeries.setSymbolType(scatterSeriesSettings.getSymbolType());
		scatterSeries.setSymbolSize(scatterSeriesSettings.getSymbolSize());
		scatterSeries.setSymbolColor(scatterSeriesSettings.getSymbolColor());
		scatterSeries.setLineStyle(LineStyle.NONE);
	}

	private void applyBarSeriesSettings(IBarSeries<?> barSeries, IBarSeriesSettings barSeriesSettings) {

		applyBaseSeriesSettings(barSeries, barSeriesSettings);
		barSeries.setBarColor(barSeriesSettings.getBarColor());
		barSeries.setBarPadding(barSeriesSettings.getBarPadding());
		barSeries.setBarWidth(barSeriesSettings.getBarWidth());
		barSeries.setBarOverlay(barSeriesSettings.isBarOverlay());
		barSeries.enableStack(barSeriesSettings.isEnableStack());
	}

	private void applyCircularSeriesSettings(ICircularSeries<?> circularSeries, ICircularSeriesSettings circularSeriesSettings) {

		applyBaseSeriesSettings(circularSeries, circularSeriesSettings);
		this.getTitle().setText(circularSeriesSettings.getDescription());
		//
		circularSeries.setSliceColor(circularSeriesSettings.getSliceColor());
		circularSeries.setBorderColor(circularSeriesSettings.getBorderColor());
		circularSeries.setBorderWidth(circularSeriesSettings.getBorderWidth());
		circularSeries.setBorderStyle(circularSeriesSettings.getBorderStyle().value());
		//
		ISeriesSettings seriesSettingsHighlight = circularSeriesSettings.getSeriesSettingsHighlight();
		if(seriesSettingsHighlight instanceof ICircularSeriesSettings circularSeriesSettingsHighlight) {
			circularSeries.setSliceColorHighlight(circularSeriesSettingsHighlight.getSliceColor());
			circularSeries.setBorderColorHighlight(circularSeriesSettingsHighlight.getBorderColor());
			circularSeries.setBorderWidthHighlight(circularSeriesSettingsHighlight.getBorderWidth());
			circularSeries.setBorderStyleHighlight(circularSeriesSettingsHighlight.getBorderStyle().value());
		}
		/*
		 * Handle the slice selection.
		 */
		IHandledEventProcessor processor = (IHandledEventProcessor)registeredEvents.get(EVENT_MOUSE_DOWN).get(1).get(SWT.NONE).get(0);
		if(processor instanceof CircularMouseDownEvent) {
			CircularMouseDownEvent mouseDownEvent = ((CircularMouseDownEvent)registeredEvents.get(EVENT_MOUSE_DOWN).get(1).get(SWT.NONE).get(0));
			mouseDownEvent.setRedrawOnClick(circularSeriesSettings.isRedrawOnClick());
			mouseDownEvent.setFillEntireSpace(circularSeriesSettings.isEntireSpaceFilled());
		}
	}

	private void applyBaseSeriesSettings(ISeries<?> series, ISeriesSettings seriesSettings) {

		series.setDescription(seriesSettings.getDescription());
		series.setVisible(seriesSettings.isVisible());
		series.setVisibleInLegend(seriesSettings.isVisibleInLegend());
	}

	public List<double[]> getDataShiftHistory(String selectedSeriesId) {

		List<double[]> dataShifts = dataShiftHistory.get(selectedSeriesId);
		if(dataShifts != null) {
			return Collections.unmodifiableList(dataShifts);
		} else {
			return Collections.emptyList();
		}
	}

	public boolean isDataShifted() {

		for(Collection<double[]> dataShifts : dataShiftHistory.values()) {
			if(dataShifts != null) {
				return true;
			}
		}
		return false;
	}

	public void setShiftConstraints(int shiftConstraints) {

		this.shiftConstraints = shiftConstraints;
	}

	public void shiftSeries(String selectedSeriesId, double shiftX, double shiftY) {

		ISeries<?> dataSeries = getSeriesSet().getSeries(selectedSeriesId);
		if(dataSeries != null) {
			//
			if(shiftX != 0.0d || shiftY != 0.0d) {
				//
				double seriesMinX = Double.MAX_VALUE;
				double seriesMaxX = Double.MIN_VALUE;
				double seriesMinY = Double.MAX_VALUE;
				double seriesMaxY = Double.MIN_VALUE;
				//
				if(shiftX != 0.0d) {
					/*
					 * Shift X
					 */
					double[] xSeriesShifted = adjustArray(dataSeries.getXSeries(), shiftX, IExtendedChart.X_AXIS);
					dataSeries.setXSeries(xSeriesShifted);
					seriesMinX = xSeriesShifted[0];
					seriesMaxX = xSeriesShifted[xSeriesShifted.length - 1];
				}
				//
				if(shiftY != 0.0d) {
					/*
					 * Shift Y
					 */
					double[] ySeriesShifted = adjustArray(dataSeries.getYSeries(), shiftY, IExtendedChart.Y_AXIS);
					dataSeries.setYSeries(ySeriesShifted);
					seriesMinY = ySeriesShifted[0];
					seriesMaxY = ySeriesShifted[ySeriesShifted.length - 1];
				}
				/*
				 * Track the shifts.
				 */
				Range rangeX = getAxisSet().getXAxis(ID_PRIMARY_X_AXIS).getRange();
				Range rangeY = getAxisSet().getYAxis(ID_PRIMARY_Y_AXIS).getRange();
				List<double[]> shiftRecord = getShiftRecord(selectedSeriesId);
				shiftRecord.add(new double[]{rangeX.lower, rangeX.upper, shiftX, rangeY.lower, rangeY.upper, shiftY, shiftConstraints});
				//
				updateCoordinates(seriesMinX, seriesMaxX, seriesMinY, seriesMaxY);
				fireSeriesModificationEvent();
			}
		}
	}

	private List<double[]> getShiftRecord(String selectedSeriesId) {

		List<double[]> shiftRecord = dataShiftHistory.get(selectedSeriesId);
		if(shiftRecord == null) {
			shiftRecord = new ArrayList<>();
			dataShiftHistory.put(selectedSeriesId, shiftRecord);
		}
		return shiftRecord;
	}

	private double[] adjustArray(double[] series, double shift, String axisOrientation) {

		if(shiftConstraints == SHIFT_CONSTRAINT_NONE) {
			series = adjustArrayWithoutConstraints(series, shift);
		} else {
			/*
			 * Take the constraints into consideration.
			 */
			boolean deleteShiftedData = isDeleteShiftedData(axisOrientation);
			if(deleteShiftedData) {
				series = adjustArrayWithConstraints(series, shift);
			} else {
				series = adjustArrayWithoutConstraints(series, shift);
			}
		}
		return series;
	}

	private double[] adjustArrayWithoutConstraints(double[] series, double shift) {

		for(int i = 0; i < series.length; i++) {
			series[i] += shift;
		}
		return series;
	}

	private double[] adjustArrayWithConstraints(double[] series, double shift) {

		double lowerBorder = 0.0d;
		/*
		 * Shift the data.
		 */
		for(int i = 0; i < series.length; i++) {
			double valueShifted = series[i] + shift;
			if(valueShifted >= lowerBorder) {
				/*
				 * Valid
				 */
				series[i] = valueShifted;
			} else {
				/*
				 * Invalid
				 */
				series[i] = 0;
			}
		}
		//
		return series;
	}

	private boolean isDeleteShiftedData(String axisOrientation) {

		if(axisOrientation.equals(IExtendedChart.X_AXIS)) {
			return ((shiftConstraints & SHIFT_CONSTRAINT_DELETE_X) == SHIFT_CONSTRAINT_DELETE_X) && chartSettings.getRangeRestriction().isZeroX();
		} else {
			return ((shiftConstraints & SHIFT_CONSTRAINT_DELETE_Y) == SHIFT_CONSTRAINT_DELETE_Y) && chartSettings.getRangeRestriction().isZeroY();
		}
	}

	/**
	 * axisId = IExtendedChart.X_AXIS or IExtendedChart.Y_AXIS.
	 * 
	 * @param selectedSeriesId
	 * @param axisId
	 * @param factor
	 */
	public void multiplySeries(String selectedSeriesId, String axisId, double factor) {

		ISeries<?> dataSeries = getSeriesSet().getSeries(selectedSeriesId);
		if(dataSeries != null) {
			//
			double[] xSeries = dataSeries.getXSeries();
			double[] ySeries = dataSeries.getYSeries();
			//
			if(IExtendedChart.X_AXIS.equals(axisId)) {
				dataSeries.setXSeries(multiplySeries(xSeries, factor));
			} else if(IExtendedChart.Y_AXIS.equals(axisId)) {
				dataSeries.setYSeries(multiplySeries(ySeries, factor));
			}
			//
			double seriesMinX = Arrays.stream(xSeries).min().getAsDouble();
			double seriesMaxX = Arrays.stream(xSeries).max().getAsDouble();
			double seriesMinY = Arrays.stream(ySeries).min().getAsDouble();
			double seriesMaxY = Arrays.stream(ySeries).max().getAsDouble();
			//
			updateCoordinates(seriesMinX, seriesMaxX, seriesMinY, seriesMaxY);
			fireSeriesModificationEvent();
		}
	}

	private double[] multiplySeries(double[] series, double factor) {

		for(int i = 0; i < series.length; i++) {
			series[i] *= factor;
		}
		return series;
	}

	public String[] getAxisLabels(String axisOrientation) {

		List<IAxisSettings> axisSettingsList = getAxisSettings(axisOrientation);
		int size = axisSettingsList.size();
		String[] items = new String[size];
		//
		for(int i = 0; i < size; i++) {
			IAxisSettings axisSettings = axisSettingsList.get(i);
			String label;
			if(axisSettings != null) {
				label = axisSettings.getLabel();
			} else {
				label = Messages.getString(Messages.NOT_SET);
			}
			items[i] = label;
		}
		//
		return items;
	}

	public List<IAxisSettings> getAxisSettings(String axisOrientation) {

		List<IAxisSettings> axisSettingsList = new ArrayList<>();
		IAxis[] axes = getAxes(axisOrientation);
		//
		for(int i = 0; i < axes.length; i++) {
			IAxisSettings axisSettings = getAxisSettings(axisOrientation, i);
			if(axisSettings != null) {
				axisSettingsList.add(axisSettings);
			}
		}
		//
		return axisSettingsList;
	}

	public DecimalFormat getDecimalFormat(String axisOrientation, int id) {

		DecimalFormat decimalFormat;
		IAxisSettings axisSettings = getAxisSettings(axisOrientation, id);
		//
		if(axisSettings != null) {
			decimalFormat = axisSettings.getDecimalFormat();
		} else {
			decimalFormat = new DecimalFormat();
		}
		return decimalFormat;
	}

	/**
	 * May return null.
	 * 
	 * axis =
	 * IExtendedChart.X_AXIS
	 * or
	 * IExtendedChart.Y_AXIS
	 * 
	 * @param axisOrientation
	 * @param id
	 * @return IAxisScaleConverter
	 */
	public IAxisScaleConverter getAxisScaleConverter(String axisOrientation, int id) {

		IAxisScaleConverter axisScaleConverter = null;
		IAxisSettings axisSettings = null;
		//
		if(axisOrientation.equals(IExtendedChart.X_AXIS)) {
			axisSettings = getXAxisSettings(id);
		} else {
			axisSettings = getYAxisSettings(id);
		}
		//
		if(axisSettings instanceof ISecondaryAxisSettings secondaryAxisSettings) {
			axisScaleConverter = secondaryAxisSettings.getAxisScaleConverter();
		}
		//
		return axisScaleConverter;
	}

	/**
	 * A dummy event is created.
	 * If possible, use the method fireUpdateCustomRangeSelectionHandlers(Event event).
	 */
	protected void fireUpdateCustomRangeSelectionHandlers() {

		Event event = new Event();
		fireUpdateCustomRangeSelectionHandlers(event);
	}

	public void fireUpdateCustomRangeSelectionHandlers(Event event) {

		/*
		 * Handle the custom user selection handlers.
		 */
		for(ICustomSelectionHandler customSelectionHandler : customRangeSelectionHandlers) {
			try {
				customSelectionHandler.handleUserSelection(event);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A dummy event is created.
	 * If possible, use the method fireUpdateCustomPointSelectionHandlers(Event event).
	 */
	protected void fireUpdateCustomPointSelectionHandlers() {

		Event event = new Event();
		fireUpdateCustomPointSelectionHandlers(event);
	}

	public void fireUpdateCustomPointSelectionHandlers(Event event) {

		/*
		 * Handle the custom user selection handlers.
		 */
		for(ICustomSelectionHandler customSelectionHandler : customPointSelectionHandlers) {
			try {
				customSelectionHandler.handleUserSelection(event);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void fireSeriesModificationEvent() {

		for(ISeriesModificationListener seriesModificationListener : seriesModificationListeners) {
			try {
				seriesModificationListener.handleSeriesModificationEvent();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void fireSeriesStatusEvent(String seriesId, int status) {

		for(ISeriesStatusListener seriesStatusListener : seriesStatusListeners) {
			try {
				switch(status) {
					case ISeriesStatusListener.SELECT:
						seriesStatusListener.handleSeriesSelectionEvent(seriesId);
						break;
					case ISeriesStatusListener.HIDE:
						seriesStatusListener.handleSeriesHideEvent(seriesId);
						break;
					case ISeriesStatusListener.RESET:
						seriesStatusListener.handleSeriesResetEvent(seriesId);
						break;
					case ISeriesStatusListener.REDRAW:
						seriesStatusListener.handleRedrawEvent();
						break;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void zoomX(IAxis xAxis, Event event) {

		trackUndoSelection();
		//
		boolean isZoomReferenceX0 = getChartSettings().getRangeRestriction().isReferenceZoomZeroX();
		double coordinateX = isZoomReferenceX0 ? 0.0d : xAxis.getDataCoordinate(event.x);
		//
		if(event.count > 0) {
			xAxis.zoomIn(coordinateX);
		} else {
			if(isZoomReferenceX0) {
				xAxis.zoomOut();
			} else {
				xAxis.zoomOut(coordinateX);
			}
		}
		//
		trackRedoSelection();
	}

	public void zoomY(IAxis yAxis, Event event) {

		trackUndoSelection();
		//
		boolean isZoomReferenceY0 = getChartSettings().getRangeRestriction().isReferenceZoomZeroY();
		double coordinateY = isZoomReferenceY0 ? 0.0d : yAxis.getDataCoordinate(event.y);
		//
		if(event.count > 0) {
			yAxis.zoomIn(coordinateY);
		} else {
			if(isZoomReferenceY0) {
				yAxis.zoomOut();
			} else {
				yAxis.zoomOut(coordinateY);
			}
		}
		//
		trackRedoSelection();
	}

	public String getSelectedseriesId(Event event) {

		ISeries<?>[] series = getSeriesSet().getSeries();
		String selectedSeriesId = ""; //$NON-NLS-1$
		/*
		 * Get the selected series id.
		 */
		exitloop:
		for(ISeries<?> dataSeries : series) {
			if(dataSeries != null && dataSeries.isVisible()) {
				int size = dataSeries.getXSeries().length;
				for(int i = 0; i < size; i++) {
					Point point = dataSeries.getPixelCoordinates(i);
					if(isDataSeriesSelected(point, event, 8)) {
						selectedSeriesId = dataSeries.getId();
						break exitloop;
					}
				}
			}
		}
		//
		return selectedSeriesId;
	}

	private boolean isDataSeriesSelected(Point point, Event event, int delta) {

		if(point.x >= event.x - delta && point.x <= event.x + delta) {
			if(point.y >= event.y - delta && point.y <= event.y + delta) {
				return true;
			}
		}
		return false;
	}

	public void handleUserSelection(Event event) {

		int minSelectedWidth;
		int minSelectedHeight;
		int deltaWidth;
		int deltaHeight;
		//
		Point point = getPlotArea().getSize();
		if((getOrientation() == SWT.HORIZONTAL)) {
			minSelectedWidth = point.x / MIN_SELECTION_PERCENTAGE;
			deltaWidth = Math.abs(userSelection.getStartX() - event.x);
			minSelectedHeight = point.y / MIN_SELECTION_PERCENTAGE;
			deltaHeight = Math.abs(userSelection.getStartY() - event.y);
		} else {
			minSelectedWidth = point.y / MIN_SELECTION_PERCENTAGE;
			deltaWidth = Math.abs(userSelection.getStartY() - event.y);
			minSelectedHeight = point.x / MIN_SELECTION_PERCENTAGE;
			deltaHeight = Math.abs(userSelection.getStartX() - event.x);
		}
		/*
		 * Prevent accidental zooming.
		 */
		RangeRestriction rangeRestriction = getRangeRestriction();
		if(rangeRestriction.isRestrictSelectY()) {
			if(deltaHeight >= minSelectedHeight) {
				handleUserSelectionXY(event);
			}
		} else {
			if(deltaWidth >= minSelectedWidth) {
				handleUserSelectionXY(event);
			}
		}
		//
		userSelection.reset();
		redraw();
	}

	@Override
	public void redraw() {

		super.redraw();
		fireSeriesStatusEvent("", ISeriesStatusListener.REDRAW); //$NON-NLS-1$
	}

	/**
	 * Returns if a buffered action is currently active.
	 * 
	 * @return boolean
	 */
	public boolean isBufferActive() {

		return getPlotArea().isBuffered();
	}

	private void handleUserSelectionXY(Event event) {

		/*
		 * Track the selection before the new range is
		 * selected by the user.
		 */
		trackUndoSelection();
		int xStart = userSelection.getStartX();
		int xStop = userSelection.getStopX();
		int yStart = userSelection.getStartY();
		int yStop = userSelection.getStopY();
		setSelectionXY(xStart, xStop, yStart, yStop);
		trackRedoSelection();
		/*
		 * Inform all registered handlers.
		 * Reset the current selection and redraw the chart.
		 */
		fireUpdateCustomRangeSelectionHandlers(event);
	}

	private void trackUndoSelection() {

		Range xRange = getAxisSet().getXAxis(ID_PRIMARY_X_AXIS).getRange();
		Range yRange = getAxisSet().getYAxis(ID_PRIMARY_Y_AXIS).getRange();
		handledSelectionEvents.push(new double[]{xRange.lower, xRange.upper, yRange.lower, yRange.upper});
	}

	private void trackRedoSelection() {

		Range xRange = getAxisSet().getXAxis(ID_PRIMARY_X_AXIS).getRange();
		Range yRange = getAxisSet().getYAxis(ID_PRIMARY_Y_AXIS).getRange();
		redoSelection = new double[]{xRange.lower, xRange.upper, yRange.lower, yRange.upper};
	}

	public void undoSelection() {

		try {
			double[] undoSelection = handledSelectionEvents.pop();
			handleSelection(undoSelection);
		} catch(EmptyStackException e) {
			e.printStackTrace();
		}
	}

	public void redoSelection() {

		if(redoSelection != null) {
			handleSelection(redoSelection);
			redoSelection = null;
		}
	}

	private void handleSelection(double[] selection) {

		double xStart = selection[0];
		double xStop = selection[1];
		double yStart = selection[2];
		double yStop = selection[3];
		IAxis xAxis = getAxisSet().getXAxis(ID_PRIMARY_X_AXIS);
		IAxis yAxis = getAxisSet().getYAxis(ID_PRIMARY_Y_AXIS);
		setRange(xAxis, xStart, xStop, false);
		setRange(yAxis, yStart, yStop, false);
	}

	private void setSelectionXY(int xStart, int xStop, int yStart, int yStop) {

		IAxis xAxis = getAxisSet().getXAxis(ID_PRIMARY_X_AXIS);
		IAxis yAxis = getAxisSet().getYAxis(ID_PRIMARY_Y_AXIS);
		//
		if((getOrientation() == SWT.HORIZONTAL)) {
			setHorizontalRange(xAxis, yAxis, xStart, xStop, yStart, yStop);
		} else {
			setVerticalRange(xAxis, yAxis, xStart, xStop, yStart, yStop);
		}
	}

	private void setHorizontalRange(IAxis xAxis, IAxis yAxis, int xStart, int xStop, int yStart, int yStop) {

		RangeRestriction rangeRestriction = getRangeRestriction();
		if(isSelectXY(rangeRestriction)) {
			/*
			 * X and Y zoom.
			 */
			setRange(xAxis, xStart, xStop, true);
			setRange(yAxis, yStart, yStop, true);
		} else {
			/*
			 * X or Y zoom.
			 */
			if(rangeRestriction.isRestrictSelectX()) {
				setRange(xAxis, xStart, xStop, true);
			} else if(rangeRestriction.isRestrictSelectY()) {
				setRange(yAxis, yStart, yStop, true);
			}
		}
	}

	private void setVerticalRange(IAxis xAxis, IAxis yAxis, int xStart, int xStop, int yStart, int yStop) {

		RangeRestriction rangeRestriction = getRangeRestriction();
		if(isSelectXY(rangeRestriction)) {
			/*
			 * X and Y zoom.
			 */
			setRange(xAxis, yStart, yStop, true);
			setRange(yAxis, xStart, xStop, true);
		} else {
			/*
			 * X or Y zoom.
			 */
			if(rangeRestriction.isRestrictSelectX()) {
				setRange(xAxis, yStart, yStop, true);
			} else if(rangeRestriction.isRestrictSelectY()) {
				setRange(yAxis, xStart, xStop, true);
			}
		}
	}

	private IAxis[] getAxes(String axisOrientation) {

		IAxisSet axisSet = getAxisSet();
		//
		if(axisOrientation.equals(IExtendedChart.X_AXIS)) {
			return axisSet.getXAxes();
		} else {
			return axisSet.getYAxes();
		}
	}

	private IAxisSettings getAxisSettings(String axisOrientation, int id) {

		IAxisSettings axisSettings = null;
		if(axisOrientation.equals(IExtendedChart.X_AXIS)) {
			axisSettings = getXAxisSettings(id);
		} else {
			axisSettings = getYAxisSettings(id);
		}
		return axisSettings;
	}

	/**
	 * Use isSelectXY(rangeRestriction) instead.
	 * 
	 * @param rangeRestriction
	 * @return boolean
	 */
	@Deprecated
	public boolean isZoomXAndY(RangeRestriction rangeRestriction) {

		return isSelectXY(rangeRestriction);
	}

	public boolean isSelectXY(RangeRestriction rangeRestriction) {

		boolean enableAction = false;
		if(!rangeRestriction.isRestrictSelectX() && !rangeRestriction.isRestrictSelectY()) {
			enableAction = true;
		} else if(rangeRestriction.isRestrictSelectX() && rangeRestriction.isRestrictSelectY()) {
			enableAction = true;
		}
		//
		return enableAction;
	}

	public boolean isZoomXY(RangeRestriction rangeRestriction) {

		boolean enableAction = false;
		if(!rangeRestriction.isRestrictZoomX() && !rangeRestriction.isRestrictZoomY()) {
			enableAction = true;
		} else if(rangeRestriction.isRestrictZoomX() && rangeRestriction.isRestrictZoomY()) {
			enableAction = true;
		}
		//
		return enableAction;
	}

	public IImageClipboardSupplier getImageClipboardSupplier() {

		return imageClipboardSupplier;
	}

	public void setImageClipboardSupplier(IImageClipboardSupplier imageClipboardSupplier) {

		this.imageClipboardSupplier = imageClipboardSupplier;
	}
}