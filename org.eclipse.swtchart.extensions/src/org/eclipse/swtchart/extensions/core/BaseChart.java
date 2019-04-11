/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.events.IEventProcessor;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;

public class BaseChart extends AbstractExtendedChart implements IChartDataCoordinates, IRangeSupport, IExtendedChart {

	public static final int ID_PRIMARY_X_AXIS = 0;
	public static final int ID_PRIMARY_Y_AXIS = 0;
	public static final String DEFAULT_TITLE_X_AXIS = "X-Axis";
	public static final String DEFAULT_TITLE_Y_AXIS = "Y-Axis";
	//
	public static final int EVENT_MOUSE_DOUBLE_CLICK = 1;
	public static final int EVENT_MOUSE_WHEEL = 2;
	public static final int EVENT_MOUSE_DOWN = 3;
	public static final int EVENT_MOUSE_MOVE = 4;
	public static final int EVENT_MOUSE_UP = 5;
	public static final int EVENT_KEY_DOWN = 6;
	public static final int EVENT_KEY_UP = 7;
	//
	public static final int BUTTON_LEFT = 1;
	public static final int BUTTON_MIDDLE = 2;
	public static final int BUTTON_RIGHT = 3; // Used by the menu
	public static final int BUTTON_WHEEL = 4;
	public static final int BUTTON_NONE = 5;
	//
	public static final int KEY_CODE_A = 65;
	public static final int KEY_CODE_B = 66;
	public static final int KEY_CODE_C = 67;
	public static final int KEY_CODE_D = 68;
	public static final int KEY_CODE_E = 69;
	public static final int KEY_CODE_F = 70;
	public static final int KEY_CODE_G = 71;
	public static final int KEY_CODE_H = 72;
	public static final int KEY_CODE_I = 73;
	public static final int KEY_CODE_J = 74;
	public static final int KEY_CODE_K = 75;
	public static final int KEY_CODE_L = 76;
	public static final int KEY_CODE_M = 77;
	public static final int KEY_CODE_N = 78;
	public static final int KEY_CODE_O = 79;
	public static final int KEY_CODE_P = 80;
	public static final int KEY_CODE_Q = 81;
	public static final int KEY_CODE_R = 82;
	public static final int KEY_CODE_S = 83;
	public static final int KEY_CODE_T = 84;
	public static final int KEY_CODE_U = 85;
	public static final int KEY_CODE_V = 86;
	public static final int KEY_CODE_W = 87;
	public static final int KEY_CODE_X = 88;
	public static final int KEY_CODE_Y = 89;
	public static final int KEY_CODE_Z = 90;
	//
	public static final int KEY_CODE_a = 97;
	public static final int KEY_CODE_b = 98;
	public static final int KEY_CODE_c = 99;
	public static final int KEY_CODE_d = 100;
	public static final int KEY_CODE_e = 101;
	public static final int KEY_CODE_f = 102;
	public static final int KEY_CODE_g = 103;
	public static final int KEY_CODE_h = 104;
	public static final int KEY_CODE_i = 105;
	public static final int KEY_CODE_j = 106;
	public static final int KEY_CODE_k = 107;
	public static final int KEY_CODE_l = 108;
	public static final int KEY_CODE_m = 109;
	public static final int KEY_CODE_n = 110;
	public static final int KEY_CODE_o = 111;
	public static final int KEY_CODE_p = 112;
	public static final int KEY_CODE_q = 113;
	public static final int KEY_CODE_r = 114;
	public static final int KEY_CODE_s = 115;
	public static final int KEY_CODE_t = 116;
	public static final int KEY_CODE_u = 117;
	public static final int KEY_CODE_v = 118;
	public static final int KEY_CODE_w = 119;
	public static final int KEY_CODE_x = 120;
	public static final int KEY_CODE_y = 121;
	public static final int KEY_CODE_z = 122;
	//
	public static final String SELECTED_SERIES_NONE = "None";
	/*
	 * see: IHandledEventProcessor
	 * Map<Integer, Map<Integer, Map<Integer, List<IEventProcessor>>>>
	 * Map<Event, Map<Button, Map<StateMask, List<IEventProcessor>>>>
	 * e.g.:
	 * Integer: EVENT_MOUSE_DOUBLE_CLICK (Event)
	 * Integer: BUTTON_NONE (Button)
	 * Integer: SWT.CTRL (StateMask)
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

	public BaseChart(Composite parent, int style) {
		super(parent, style);
		//
		chartSettings = new ChartSettings();
		/*
		 * Rectangle range selection.
		 */
		userSelection = new UserSelection();
		customRangeSelectionHandlers = new ArrayList<ICustomSelectionHandler>();
		customPointSelectionHandlers = new ArrayList<ICustomSelectionHandler>();
		seriesModificationListeners = new ArrayList<ISeriesModificationListener>();
		seriesStatusListeners = new ArrayList<ISeriesStatusListener>();
		selectedSeriesIds = new HashSet<String>();
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
		xAxisPrimary.setCategorySeries(new String[]{});
		//
		IAxis yAxisPrimary = axisSet.getYAxis(ID_PRIMARY_Y_AXIS);
		ITitle titleY = yAxisPrimary.getTitle();
		titleY.setText(DEFAULT_TITLE_Y_AXIS);
		titleY.setVisible(true);
		yAxisPrimary.setPosition(Position.Primary);
		yAxisPrimary.getTick().setFormat(new DecimalFormat());
		yAxisPrimary.enableLogScale(false);
		yAxisPrimary.enableCategory(false);
		xAxisPrimary.setReversed(false);
		//
		handledSelectionEvents = new Stack<double[]>();
		redoSelection = null;
		//
		dataShiftHistory = new HashMap<String, List<double[]>>();
	}

	private void initializeEventProcessors() {

		registeredEvents = new HashMap<Integer, Map<Integer, Map<Integer, List<IEventProcessor>>>>();
		initializeEvents();
	}

	private void initializeEvents() {

		registeredEvents.put(EVENT_MOUSE_DOUBLE_CLICK, new HashMap<Integer, Map<Integer, List<IEventProcessor>>>());
		registeredEvents.put(EVENT_MOUSE_WHEEL, new HashMap<Integer, Map<Integer, List<IEventProcessor>>>());
		registeredEvents.put(EVENT_MOUSE_DOWN, new HashMap<Integer, Map<Integer, List<IEventProcessor>>>());
		registeredEvents.put(EVENT_MOUSE_MOVE, new HashMap<Integer, Map<Integer, List<IEventProcessor>>>());
		registeredEvents.put(EVENT_MOUSE_UP, new HashMap<Integer, Map<Integer, List<IEventProcessor>>>());
		registeredEvents.put(EVENT_KEY_DOWN, new HashMap<Integer, Map<Integer, List<IEventProcessor>>>());
		registeredEvents.put(EVENT_KEY_UP, new HashMap<Integer, Map<Integer, List<IEventProcessor>>>());
	}

	public void clearEventProcessors() {

		registeredEvents.clear();
		initializeEvents();
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
			buttonEventProcessors = new HashMap<Integer, List<IEventProcessor>>();
			eventProcessors.put(button, buttonEventProcessors);
		}
		/*
		 * Example
		 * StateMask: SWT.CTRL
		 */
		int stateMask = handledEventProcessor.getStateMask();
		List<IEventProcessor> handledEventProcessors = buttonEventProcessors.get(stateMask);
		if(handledEventProcessors == null) {
			handledEventProcessors = new ArrayList<IEventProcessor>();
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
			length = getPlotArea().getBounds().width;
		} else {
			IAxis axis = getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
			start = axis.getRange().lower;
			stop = axis.getRange().upper;
			length = getPlotArea().getBounds().height;
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
	public ISeries createSeries(ISeriesData seriesData, ISeriesSettings seriesSettings) throws SeriesException {

		ISeries series = super.createSeries(seriesData, seriesSettings);
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

		double primaryValue = 0.0d;
		double start;
		double stop;
		int length;
		//
		if(orientation.equals(IExtendedChart.X_AXIS)) {
			IAxis axis = getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
			start = axis.getRange().lower;
			stop = axis.getRange().upper;
			length = getPlotArea().getBounds().width;
		} else {
			IAxis axis = getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
			start = axis.getRange().lower;
			stop = axis.getRange().upper;
			length = getPlotArea().getBounds().height;
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

	public boolean isSeriesContained(String seriesId) {

		if(getSeriesSet().getSeries(seriesId) == null) {
			return false;
		} else {
			return true;
		}
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
			if(isZoomXAndY(rangeRestriction)) {
				/*
				 * X and Y zoom.
				 */
				e.gc.drawRectangle(xMin, yMin, xMax - xMin, yMax - yMin);
			} else {
				/*
				 * X or Y zoom.
				 */
				if(rangeRestriction.isXZoomOnly()) {
					e.gc.drawLine(xMin, yMin, xMax, yMin);
				} else if(rangeRestriction.isYZoomOnly()) {
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

		handleEvent(registeredEvents.get(EVENT_MOUSE_MOVE).get(BUTTON_NONE), event);
	}

	@Override
	public void handleMouseUpEvent(Event event) {

		handleEvent(registeredEvents.get(EVENT_MOUSE_UP).get(event.button), event);
	}

	@Override
	public void handleMouseWheel(Event event) {

		handleEvent(registeredEvents.get(EVENT_MOUSE_WHEEL).get(BUTTON_WHEEL), event);
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

	public void selectSeries(String selectedSeriesId) {

		selectSeries(selectedSeriesId, true);
	}

	public void selectSeries(String selectedSeriesId, boolean fireUpdate) {

		if(isSeriesContained(selectedSeriesId)) {
			ISeries dataSeries = getSeriesSet().getSeries(selectedSeriesId);
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

		ISeries dataSeries = getSeriesSet().getSeries(selectedSeriesId);
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

		ISeries[] series = getSeriesSet().getSeries();
		//
		for(ISeries dataSeries : series) {
			ISeriesSettings seriesSettings = getSeriesSettings(dataSeries.getId());
			applySeriesSettings(dataSeries, seriesSettings);
		}
		//
		selectedSeriesIds.clear();
		redraw();
		if(fireUpdate) {
			fireSeriesStatusEvent(SELECTED_SERIES_NONE, ISeriesStatusListener.RESET);
		}
	}

	public void showSeries(String selectedSeriesId) {

		ISeries dataSeries = getSeriesSet().getSeries(selectedSeriesId);
		if(dataSeries != null) {
			dataSeries.setVisible(true);
			dataSeries.setVisibleInLegend(true);
		}
	}

	public void applySeriesSettings(ISeries dataSeries, ISeriesSettings seriesSettings) {

		if(dataSeries instanceof ILineSeries) {
			ILineSeries lineSeries = (ILineSeries)dataSeries;
			if(seriesSettings instanceof ILineSeriesSettings) {
				/*
				 * Line Series
				 */
				ILineSeriesSettings lineSeriesSettings = (ILineSeriesSettings)seriesSettings;
				applyLineSeriesSettings(lineSeries, lineSeriesSettings);
			} else if(seriesSettings instanceof IScatterSeriesSettings) {
				/*
				 * Scatter Series
				 */
				IScatterSeriesSettings scatterSeriesSettings = (IScatterSeriesSettings)seriesSettings;
				applyScatterSeriesSettings(lineSeries, scatterSeriesSettings);
			}
		} else if(dataSeries instanceof IBarSeries) {
			IBarSeries barSeries = (IBarSeries)dataSeries;
			if(seriesSettings instanceof IBarSeriesSettings) {
				/*
				 * Bar Series
				 */
				IBarSeriesSettings barSeriesSettings = (IBarSeriesSettings)seriesSettings;
				applyBarSeriesSettings(barSeries, barSeriesSettings);
			}
		}
	}

	public void applyLineSeriesSettings(ILineSeries lineSeries, ILineSeriesSettings lineSeriesSettings) {

		lineSeries.setDescription(lineSeriesSettings.getDescription());
		lineSeries.setVisible(lineSeriesSettings.isVisible());
		lineSeries.setVisibleInLegend(lineSeriesSettings.isVisibleInLegend());
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

	public void applyScatterSeriesSettings(ILineSeries scatterSeries, IScatterSeriesSettings scatterSeriesSettings) {

		scatterSeries.setDescription(scatterSeriesSettings.getDescription());
		scatterSeries.setVisible(scatterSeriesSettings.isVisible());
		scatterSeries.setVisibleInLegend(scatterSeriesSettings.isVisibleInLegend());
		scatterSeries.enableArea(false);
		scatterSeries.setSymbolType(scatterSeriesSettings.getSymbolType());
		scatterSeries.setSymbolSize(scatterSeriesSettings.getSymbolSize());
		scatterSeries.setSymbolColor(scatterSeriesSettings.getSymbolColor());
		scatterSeries.setLineStyle(LineStyle.NONE);
	}

	public void applyBarSeriesSettings(IBarSeries barSeries, IBarSeriesSettings barSeriesSettings) {

		barSeries.setDescription(barSeriesSettings.getDescription());
		barSeries.setVisible(barSeriesSettings.isVisible());
		barSeries.setVisibleInLegend(barSeriesSettings.isVisibleInLegend());
		barSeries.setBarColor(barSeriesSettings.getBarColor());
		barSeries.setBarPadding(barSeriesSettings.getBarPadding());
		barSeries.setBarWidth(barSeriesSettings.getBarWidth());
		barSeries.setBarOverlay(barSeriesSettings.isBarOverlay());
		barSeries.enableStack(barSeriesSettings.isEnableStack());
	}

	public List<double[]> getDataShiftHistory(String selectedSeriesId) {

		List<double[]> dataShifts = dataShiftHistory.get(selectedSeriesId);
		if(dataShifts != null) {
			return Collections.unmodifiableList(dataShifts);
		} else {
			return null;
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

		ISeries dataSeries = getSeriesSet().getSeries(selectedSeriesId);
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
			shiftRecord = new ArrayList<double[]>();
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
				series = adjustArrayWithConstraints(series, shift, axisOrientation);
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

	private double[] adjustArrayWithConstraints(double[] series, double shift, String axisOrientation) {

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

		ISeries dataSeries = getSeriesSet().getSeries(selectedSeriesId);
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

		IAxis[] axes = getAxes(axisOrientation);
		int size = axes.length;
		String[] items = new String[size];
		//
		for(int i = 0; i < size; i++) {
			/*
			 * Get the label.
			 */
			String label;
			IAxisSettings axisSettings = getAxisSettings(axisOrientation, i);
			if(axisSettings != null) {
				label = axisSettings.getLabel();
			} else {
				label = "not set";
			}
			items[i] = label;
		}
		return items;
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
		if(axisSettings instanceof ISecondaryAxisSettings) {
			axisScaleConverter = ((ISecondaryAxisSettings)axisSettings).getAxisScaleConverter();
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
				System.out.println(e);
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
				System.out.println(e);
			}
		}
	}

	private void fireSeriesModificationEvent() {

		for(ISeriesModificationListener seriesModificationListener : seriesModificationListeners) {
			try {
				seriesModificationListener.handleSeriesModificationEvent();
			} catch(Exception e) {
				System.out.println(e);
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
				}
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}

	public void zoomX(IAxis xAxis, Event event) {

		/*
		 * X Axis
		 */
		trackUndoSelection();
		double coordinateX = xAxis.getDataCoordinate(event.x);
		if(event.count > 0) {
			xAxis.zoomIn(coordinateX);
		} else {
			xAxis.zoomOut(coordinateX);
		}
		trackRedoSelection();
	}

	public void zoomY(IAxis yAxis, Event event) {

		/*
		 * Y Axis
		 */
		trackUndoSelection();
		double coordinateY = yAxis.getDataCoordinate(event.y);
		if(event.count > 0) {
			yAxis.zoomIn(coordinateY);
		} else {
			yAxis.zoomOut(coordinateY);
		}
		trackRedoSelection();
	}

	public String getSelectedseriesId(Event event) {

		ISeries[] series = getSeriesSet().getSeries();
		String selectedSeriesId = "";
		/*
		 * Get the selected series id.
		 */
		exitloop:
		for(ISeries dataSeries : series) {
			if(dataSeries != null) {
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
		Rectangle bounds = getPlotArea().getBounds();
		if((getOrientation() == SWT.HORIZONTAL)) {
			minSelectedWidth = bounds.width / MIN_SELECTION_PERCENTAGE;
			deltaWidth = Math.abs(userSelection.getStartX() - event.x);
			minSelectedHeight = bounds.height / MIN_SELECTION_PERCENTAGE;
			deltaHeight = Math.abs(userSelection.getStartY() - event.y);
		} else {
			minSelectedWidth = bounds.height / MIN_SELECTION_PERCENTAGE;
			deltaWidth = Math.abs(userSelection.getStartY() - event.y);
			minSelectedHeight = bounds.width / MIN_SELECTION_PERCENTAGE;
			deltaHeight = Math.abs(userSelection.getStartX() - event.x);
		}
		/*
		 * Prevent accidental zooming.
		 */
		RangeRestriction rangeRestriction = getRangeRestriction();
		if(rangeRestriction.isYZoomOnly()) {
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
			System.out.println(e);
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
		if(isZoomXAndY(rangeRestriction)) {
			/*
			 * X and Y zoom.
			 */
			setRange(xAxis, xStart, xStop, true);
			setRange(yAxis, yStart, yStop, true);
		} else {
			/*
			 * X or Y zoom.
			 */
			if(rangeRestriction.isXZoomOnly()) {
				setRange(xAxis, xStart, xStop, true);
			} else if(rangeRestriction.isYZoomOnly()) {
				setRange(yAxis, yStart, yStop, true);
			}
		}
	}

	private void setVerticalRange(IAxis xAxis, IAxis yAxis, int xStart, int xStop, int yStart, int yStop) {

		RangeRestriction rangeRestriction = getRangeRestriction();
		if(isZoomXAndY(rangeRestriction)) {
			/*
			 * X and Y zoom.
			 */
			setRange(xAxis, yStart, yStop, true);
			setRange(yAxis, xStart, xStop, true);
		} else {
			/*
			 * X or Y zoom.
			 */
			if(rangeRestriction.isXZoomOnly()) {
				setRange(xAxis, yStart, yStop, true);
			} else if(rangeRestriction.isYZoomOnly()) {
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

	public boolean isZoomXAndY(RangeRestriction rangeRestriction) {

		boolean zoomXAndY = false;
		if(!rangeRestriction.isXZoomOnly() && !rangeRestriction.isYZoomOnly()) {
			zoomXAndY = true;
		} else if(rangeRestriction.isXZoomOnly() && rangeRestriction.isYZoomOnly()) {
			zoomXAndY = true;
		}
		//
		return zoomXAndY;
	}
}
