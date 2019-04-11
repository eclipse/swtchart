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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IAxisTick;
import org.eclipse.swtchart.IGrid;
import org.eclipse.swtchart.ILegend;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.ITitle;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;
import org.eclipse.swtchart.extensions.exceptions.SeriesException;
import org.eclipse.swtchart.extensions.internal.marker.AxisZeroMarker;
import org.eclipse.swtchart.extensions.internal.marker.LegendMarker;
import org.eclipse.swtchart.extensions.internal.marker.PlotCenterMarker;
import org.eclipse.swtchart.extensions.internal.marker.PositionMarker;
import org.eclipse.swtchart.extensions.internal.marker.SeriesLabelMarker;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public class ScrollableChart extends Composite implements IScrollableChart, IEventHandler, IExtendedChart {

	public static final int NO_COMPRESS_TO_LENGTH = Integer.MAX_VALUE;
	/*
	 * Menu extensions via Equinox.
	 */
	private static final String EXTENSION_POINT_MENU_ITEMS = "org.eclipse.swtchart.extensions.menuitems";
	private static final String EXTENSION_POINT_MENU_ENTRY = "MenuEntry";
	//
	private Map<String, Set<IChartMenuEntry>> categoryMenuEntriesMap;
	private Map<String, IChartMenuEntry> menuEntryMap;
	//
	private Slider sliderVertical;
	private Slider sliderHorizontal;
	private RangeSelector rangeSelector;
	private BaseChart baseChart;
	/*
	 * With enableRangeSelectorHint the gc draws an info
	 * that the range selector can be activated by using a
	 * mouse double click. It's implemented and works fine,
	 * but is a bit crappy when a chart title is used. So, it is
	 * deactivated for now, but not removed.
	 */
	private boolean enableRangeSelectorHint = false;
	private static final int MILLISECONDS_SHOW_RANGE_INFO_HINT = 1000;
	private boolean showRangeSelectorHint = enableRangeSelectorHint;
	private RangeHintPaintListener rangeHintPaintListener;
	/*
	 * This list contains all scrollable charts
	 * that are linked with the current editor.
	 */
	private List<ScrollableChart> linkedScrollableCharts;
	//
	private PositionMarker positionMarker;
	private PlotCenterMarker plotCenterMarker;
	private LegendMarker legendMarker;
	private AxisZeroMarker axisZeroMarker;
	private SeriesLabelMarker seriesLabelMarker;
	private static final int HORIZONTAL_SCROLL_LENGTH = Integer.MAX_VALUE - 1;
	private static final int VERTICAL_SCROLL_LENGTH = Integer.MAX_VALUE - 1;

	/**
	 * This constructor is used, when clazz.newInstance() is needed.
	 */
	public ScrollableChart() {
		this(getSeparateShell(), SWT.NONE);
	}

	public ScrollableChart(Composite parent, int style) {
		super(parent, style);
		//
		categoryMenuEntriesMap = new HashMap<String, Set<IChartMenuEntry>>();
		menuEntryMap = new HashMap<String, IChartMenuEntry>();
		linkedScrollableCharts = new ArrayList<ScrollableChart>();
		//
		initialize();
	}

	private class RangeHintPaintListener implements PaintListener {

		@Override
		public void paintControl(PaintEvent e) {

			/*
			 * Rectangle (Double Click -> show Range Info)
			 */
			IChartSettings chartSettings = baseChart.getChartSettings();
			if(!rangeSelector.isVisible() && chartSettings.isEnableRangeSelector()) {
				if(showRangeSelectorHint) {
					int lineWidth = 1;
					Rectangle rectangle = baseChart.getBounds();
					int width = rectangle.width - lineWidth;
					e.gc.setForeground(chartSettings.getColorHintRangeSelector());
					e.gc.setLineWidth(lineWidth);
					Rectangle rectangleInfo = new Rectangle(0, 0, width, 26);
					e.gc.drawRectangle(rectangleInfo);
					//
					ITitle title = getBaseChart().getTitle();
					if(title.getForeground().equals(baseChart.getBackground())) {
						/*
						 * Draw the message.
						 */
						String label = "Double click to show range info.";
						Point labelSize = e.gc.textExtent(label);
						e.gc.drawText(label, (int)(width / 2.0d - labelSize.x / 2.0d), 5, true);
					}
					/*
					 * Hide the rectangle after x milliseconds.
					 */
					getBaseChart().getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {

							try {
								Thread.sleep(MILLISECONDS_SHOW_RANGE_INFO_HINT);
							} catch(InterruptedException e) {
								System.out.println(e);
							}
							showRangeSelectorHint = false;
							baseChart.redraw();
						}
					});
				}
			}
		}
	}

	@Override
	public IChartSettings getChartSettings() {

		return baseChart.getChartSettings();
	}

	public void addLinkedScrollableChart(ScrollableChart scrollableChart) {

		linkedScrollableCharts.add(scrollableChart);
	}

	public void removeLinkedScrollableChart(ScrollableChart scrollableChart) {

		linkedScrollableCharts.remove(scrollableChart);
	}

	@Override
	public void applySettings(IChartSettings chartSettings) {

		/*
		 * Modify the chart and adjust the series.
		 */
		baseChart.setChartSettings(chartSettings);
		baseChart.suspendUpdate(true);
		modifyChart();
		adjustSeries();
		baseChart.suspendUpdate(false);
		baseChart.redraw();
	}

	@Override
	public BaseChart getBaseChart() {

		return baseChart;
	}

	@Override
	public ISeries createSeries(ISeriesData seriesData, ISeriesSettings seriesSettings) throws SeriesException {

		ISeries series = baseChart.createSeries(seriesData, seriesSettings);
		resetSlider();
		return series;
	}

	@Override
	public void redraw() {

		resetSlider();
		super.redraw();
	}

	@Override
	public void deleteSeries() {

		baseChart.suspendUpdate(true);
		for(ISeries series : baseChart.getSeriesSet().getSeries()) {
			baseChart.deleteSeries(series.getId());
		}
		baseChart.suspendUpdate(false);
		redraw();
	}

	@Override
	public void deleteSeries(String id) {

		baseChart.deleteSeries(id);
		redraw();
	}

	@Override
	public void appendSeries(ISeriesData seriesData) {

		baseChart.appendSeries(seriesData);
		adjustRange(true);
	}

	@Override
	public void setRange(String axis, Range range) {

		if(axis != null && range != null) {
			setRange(axis, range.lower, range.upper);
		}
	}

	@Override
	public void setRange(String axis, double start, double stop) {

		baseChart.setRange(axis, start, stop);
		setSliderSelection(false);
		updateLinkedCharts();
	}

	@Override
	public void adjustRange(boolean adjustMinMax) {

		baseChart.adjustRange(adjustMinMax);
		resetSlider();
	}

	public void adjustXAxis() {

		for(IAxis axis : baseChart.getAxisSet().getXAxes()) {
			axis.adjustRange();
		}
		resetSlider();
	}

	public void adjustYAxis() {

		for(IAxis axis : baseChart.getAxisSet().getYAxes()) {
			axis.adjustRange();
		}
		resetSlider();
	}

	public void zoomIn() {

		baseChart.getAxisSet().zoomIn();
		resetSlider();
	}

	public void zoomOut() {

		baseChart.getAxisSet().zoomOut();
		resetSlider();
	}

	@Override
	public void adjustSecondaryXAxes() {

		baseChart.adjustSecondaryXAxes();
	}

	@Override
	public void adjustSecondaryYAxes() {

		baseChart.adjustSecondaryYAxes();
	}

	@Override
	public void handleMouseDownEvent(Event event) {

		baseChart.handleMouseDownEvent(event);
	}

	@Override
	public void handleMouseMoveEvent(Event event) {

		baseChart.handleMouseMoveEvent(event);
		//
		if(positionMarker.isDraw()) {
			positionMarker.setActualPosition(event.x, event.y);
			getBaseChart().getPlotArea().redraw();
		}
		//
		if(legendMarker.isDraw()) {
			legendMarker.setActualPosition(event.x, event.y);
			getBaseChart().getPlotArea().redraw();
		}
	}

	@Override
	public void handleMouseUpEvent(Event event) {

		baseChart.handleMouseUpEvent(event);
		updateLinkedCharts();
	}

	@Override
	public void handleMouseWheel(Event event) {

		baseChart.handleMouseWheel(event);
	}

	@Override
	public void handleMouseDoubleClick(Event event) {

		baseChart.handleMouseDoubleClick(event);
	}

	@Override
	public void handleKeyDownEvent(Event event) {

		baseChart.handleKeyDownEvent(event);
	}

	@Override
	public void handleKeyUpEvent(Event event) {

		baseChart.handleKeyUpEvent(event);
		resetSlider();
	}

	@Override
	public void handleSelectionEvent(Event event) {

		baseChart.handleSelectionEvent(event);
		widgetSelected(event);
	}

	@Override
	public void paintControl(PaintEvent e) {

		baseChart.paintControl(e);
	}

	public void toggleRangeSelectorVisibility() {

		showRangeSelector(!rangeSelector.isVisible());
	}

	public void togglePositionMarkerVisibility() {

		positionMarker.setDraw(!positionMarker.isDraw());
		baseChart.getPlotArea().redraw();
	}

	public void toggleCenterMarkerVisibility() {

		plotCenterMarker.setDraw(!plotCenterMarker.isDraw());
		super.redraw();
	}

	public void togglePositionLegendVisibility() {

		legendMarker.setDraw(!legendMarker.isDraw());
		super.redraw();
	}

	public void toggleSeriesLegendVisibility() {

		ILegend legend = baseChart.getLegend();
		legend.setVisible(!legend.isVisible());
		baseChart.redraw();
	}

	public void toggleAxisZeroVisibility() {

		axisZeroMarker.setDraw(!axisZeroMarker.isDraw());
		baseChart.redraw();
	}

	public void toggleSeriesLabelVisibility() {

		seriesLabelMarker.setDraw(!seriesLabelMarker.isDraw());
		baseChart.redraw();
	}

	public RangeSelector getRangeSelector() {

		return rangeSelector;
	}

	protected ISeriesData calculateSeries(ISeriesData seriesData) {

		return calculateSeries(seriesData, NO_COMPRESS_TO_LENGTH); // No compression.
	}

	/**
	 * Use compress series only if it's absolutely necessary.
	 * 
	 * @param seriesData
	 * @param compressToLength
	 * @return ISeriesData
	 */
	protected ISeriesData calculateSeries(ISeriesData seriesData, int compressToLength) {

		double[] xSeries = seriesData.getXSeries();
		double[] ySeries = seriesData.getYSeries();
		int seriesLength = ySeries.length;
		//
		if(seriesLength > compressToLength) {
			/*
			 * Capture the compressed data.
			 * The final size is not known yet.
			 */
			List<Double> xSeriesCompressed = new ArrayList<Double>();
			List<Double> ySeriesCompressed = new ArrayList<Double>();
			/*
			 * First x,y value.
			 */
			xSeriesCompressed.add(xSeries[0]);
			ySeriesCompressed.add(ySeries[0]);
			//
			int moduloValue = seriesLength / compressToLength;
			for(int i = 1; i < ySeries.length - 1; i++) {
				/*
				 * Filter the values.
				 */
				double y = ySeries[i];
				boolean addValue = false;
				//
				if(moduloValue > 0 && i % moduloValue == 0) {
					addValue = true;
				}
				//
				if(addValue) {
					xSeriesCompressed.add(xSeries[i]);
					ySeriesCompressed.add(y);
				}
			}
			/*
			 * Last x,y value.
			 */
			xSeriesCompressed.add(xSeries[xSeries.length - 1]);
			ySeriesCompressed.add(ySeries[ySeries.length - 1]);
			/*
			 * Compression
			 */
			double[] xCompressed = xSeriesCompressed.stream().mapToDouble(d -> d).toArray();
			double[] yCompressed = ySeriesCompressed.stream().mapToDouble(d -> d).toArray();
			//
			return new SeriesData(xCompressed, yCompressed, seriesData.getId());
		} else {
			/*
			 * No compression.
			 */
			return seriesData;
		}
	}

	/**
	 * Returns whether the series exceeds the given length hint or not.
	 * 
	 * @param xSeries
	 * @param ySeries
	 * @param lengthHintDataPoints
	 * @return boolean
	 */
	protected boolean isLargeDataSet(double[] xSeries, double[] ySeries, int lengthHintDataPoints) {

		boolean isLargeDataSet = false;
		if(xSeries.length == ySeries.length) {
			if(xSeries.length > lengthHintDataPoints) {
				isLargeDataSet = true;
			}
		}
		return isLargeDataSet;
	}

	/**
	 * Create a shell with max width and height.
	 * 
	 * @return Shell
	 */
	private static Shell getSeparateShell() {

		Display display = Display.getDefault();
		Rectangle bounds = display.getBounds();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(bounds.width, bounds.height);
		shell.setLocation(0, 0);
		return shell;
	}

	private void modifyChart() {

		IChartSettings chartSettings = baseChart.getChartSettings();
		setSliderVisibility(chartSettings);
		setRangeInfoVisibility(chartSettings);
		//
		ITitle title = baseChart.getTitle();
		title.setText(chartSettings.getTitle());
		title.setVisible(chartSettings.isTitleVisible());
		title.setForeground(chartSettings.getTitleColor());
		//
		ILegend legend = baseChart.getLegend();
		legend.setPosition(chartSettings.getLegendPosition());
		legend.setVisible(chartSettings.isLegendVisible());
		//
		setBackground(chartSettings.getBackground());
		baseChart.setOrientation(chartSettings.getOrientation());
		baseChart.setBackground(chartSettings.getBackgroundChart());
		baseChart.setBackgroundInPlotArea(chartSettings.getBackgroundPlotArea());
		baseChart.enableCompress(chartSettings.isEnableCompress());
		baseChart.setRangeRestriction(chartSettings.getRangeRestriction());
		/*
		 * Primary and Secondary axes
		 */
		addPrimaryAxisX(chartSettings);
		addPrimaryAxisY(chartSettings);
		addSecondaryAxesX(chartSettings);
		addSecondaryAxesY(chartSettings);
		/*
		 * Range Info
		 */
		rangeSelector.resetRanges();
		/*
		 * Additional actions.
		 */
		setCustomPaintListener();
		updateRangeHintPaintListener();
		setMenuItems();
		setEventProcessors();
	}

	private void adjustSeries() {

		ISeriesSet seriesSet = baseChart.getSeriesSet();
		if(seriesSet.getSeries().length > 0) {
			adjustRange(true);
		}
	}

	private void setCustomPaintListener() {

		setPositionMarker();
		setPlotCenterMarker();
		setLegendMarker();
		setAxisZeroMarker();
		setSeriesLabelMarker();
	}

	private void setPositionMarker() {

		IPlotArea plotArea = (IPlotArea)baseChart.getPlotArea();
		IChartSettings chartSettings = baseChart.getChartSettings();
		//
		if(positionMarker != null) {
			plotArea.removeCustomPaintListener(positionMarker);
		}
		//
		positionMarker = new PositionMarker(baseChart);
		positionMarker.setForegroundColor(chartSettings.getColorPositionMarker());
		plotArea.addCustomPaintListener(positionMarker);
		//
		if(chartSettings.isShowPositionMarker()) {
			positionMarker.setDraw(true);
		} else {
			positionMarker.setDraw(false);
		}
	}

	private void setPlotCenterMarker() {

		IPlotArea plotArea = (IPlotArea)baseChart.getPlotArea();
		IChartSettings chartSettings = baseChart.getChartSettings();
		//
		if(plotCenterMarker != null) {
			plotArea.removeCustomPaintListener(plotCenterMarker);
		}
		//
		plotCenterMarker = new PlotCenterMarker(baseChart);
		plotCenterMarker.setForegroundColor(chartSettings.getColorPlotCenterMarker());
		plotArea.addCustomPaintListener(plotCenterMarker);
		//
		if(chartSettings.isShowPlotCenterMarker()) {
			plotCenterMarker.setDraw(true);
		} else {
			plotCenterMarker.setDraw(false);
		}
	}

	private void setLegendMarker() {

		IPlotArea plotArea = (IPlotArea)baseChart.getPlotArea();
		IChartSettings chartSettings = baseChart.getChartSettings();
		//
		if(legendMarker != null) {
			plotArea.removeCustomPaintListener(legendMarker);
		}
		//
		legendMarker = new LegendMarker(baseChart);
		legendMarker.setForegroundColor(chartSettings.getColorLegendMarker());
		plotArea.addCustomPaintListener(legendMarker);
		//
		if(chartSettings.isShowLegendMarker()) {
			legendMarker.setDraw(true);
		} else {
			legendMarker.setDraw(false);
		}
	}

	private void setAxisZeroMarker() {

		IPlotArea plotArea = (IPlotArea)baseChart.getPlotArea();
		IChartSettings chartSettings = baseChart.getChartSettings();
		//
		if(axisZeroMarker != null) {
			plotArea.removeCustomPaintListener(axisZeroMarker);
		}
		//
		axisZeroMarker = new AxisZeroMarker(baseChart);
		axisZeroMarker.setForegroundColor(chartSettings.getColorAxisZeroMarker());
		plotArea.addCustomPaintListener(axisZeroMarker);
		//
		if(chartSettings.isShowAxisZeroMarker()) {
			axisZeroMarker.setDraw(true);
		} else {
			axisZeroMarker.setDraw(false);
		}
	}

	private void setSeriesLabelMarker() {

		IPlotArea plotArea = (IPlotArea)baseChart.getPlotArea();
		IChartSettings chartSettings = baseChart.getChartSettings();
		//
		if(seriesLabelMarker != null) {
			plotArea.removeCustomPaintListener(seriesLabelMarker);
		}
		//
		seriesLabelMarker = new SeriesLabelMarker(baseChart);
		seriesLabelMarker.setForegroundColor(chartSettings.getColorSeriesLabelMarker());
		plotArea.addCustomPaintListener(seriesLabelMarker);
		//
		if(chartSettings.isShowSeriesLabelMarker()) {
			seriesLabelMarker.setDraw(true);
		} else {
			seriesLabelMarker.setDraw(false);
		}
	}

	private void setMenuItems() {

		/*
		 * Clear the existing entries.
		 */
		categoryMenuEntriesMap.clear();
		menuEntryMap.clear();
		/*
		 * Create the menu if requested.
		 */
		IChartSettings chartSettings = baseChart.getChartSettings();
		if(chartSettings.isCreateMenu()) {
			addMenuItemsFromChartSettings();
			addMenuItemsFromExtensionPoint();
			createPopupMenu();
		} else {
			/*
			 * No menu
			 */
			baseChart.getPlotArea().setMenu(null);
		}
	}

	private void addMenuItemsFromChartSettings() {

		IChartSettings chartSettings = baseChart.getChartSettings();
		for(IChartMenuEntry menuEntry : chartSettings.getMenuEntries()) {
			addMenuEntry(menuEntry);
		}
	}

	private void setEventProcessors() {

		baseChart.clearEventProcessors();
		addHandledEventProcessorsFromChartSettings();
	}

	private void addHandledEventProcessorsFromChartSettings() {

		IChartSettings chartSettings = baseChart.getChartSettings();
		for(IHandledEventProcessor handledEventProcessor : chartSettings.getHandledEventProcessors()) {
			baseChart.addEventProcessor(handledEventProcessor);
		}
	}

	private void addMenuItemsFromExtensionPoint() {

		/*
		 * The extension registry is null if the bundle has not been started in OSGi/Equinox modus.
		 */
		IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		if(extensionRegistry != null) {
			IConfigurationElement[] configurationElements = extensionRegistry.getConfigurationElementsFor(EXTENSION_POINT_MENU_ITEMS);
			for(IConfigurationElement element : configurationElements) {
				try {
					IChartMenuEntry menuEntry = (IChartMenuEntry)element.createExecutableExtension(EXTENSION_POINT_MENU_ENTRY);
					addMenuEntry(menuEntry);
				} catch(CoreException e) {
					System.out.println(e);
				}
			}
		}
	}

	private void setSliderVisibility(IChartSettings chartSettings) {

		/*
		 * pack(); doesn't work???!!! Why? It should!
		 * Exclude and layout did the trick.
		 */
		GridData gridDataVertical = (GridData)sliderVertical.getLayoutData();
		gridDataVertical.exclude = !chartSettings.isVerticalSliderVisible();
		sliderVertical.setVisible(chartSettings.isVerticalSliderVisible());
		//
		GridData gridDataHorizontal = (GridData)sliderHorizontal.getLayoutData();
		gridDataHorizontal.exclude = !chartSettings.isHorizontalSliderVisible();
		sliderHorizontal.setVisible(chartSettings.isHorizontalSliderVisible());
		//
		layout(false);
	}

	private void updateRangeHintPaintListener() {

		if(rangeHintPaintListener != null) {
			baseChart.removePaintListener(rangeHintPaintListener);
		}
		//
		rangeHintPaintListener = new RangeHintPaintListener();
		baseChart.addPaintListener(rangeHintPaintListener);
	}

	private void showRangeSelector(boolean showRangeSelector) {

		GridData gridData = (GridData)rangeSelector.getLayoutData();
		gridData.exclude = !showRangeSelector;
		rangeSelector.setVisible(showRangeSelector);
		Composite parent = rangeSelector.getParent();
		parent.layout(false);
		parent.redraw();
	}

	private void setRangeInfoVisibility(IChartSettings chartSettings) {

		boolean isVisible = chartSettings.isEnableRangeSelector() && chartSettings.isShowRangeSelectorInitially();
		GridData gridData = (GridData)rangeSelector.getLayoutData();
		gridData.exclude = !isVisible;
		rangeSelector.setVisible(isVisible);
		layout(true);
	}

	private void resetSlider() {

		setSliderSelection(true);
		updateLinkedCharts();
	}

	private void setSliderSelection(boolean calculateIncrement) {

		IAxis xAxis = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		IAxis yAxis = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		//
		if(xAxis != null && yAxis != null) {
			/*
			 * Take care of Horizontal or Vertical orientation.
			 */
			int minX = 0;
			int maxX = minX + HORIZONTAL_SCROLL_LENGTH;
			int minY = 0;
			int maxY = minY + VERTICAL_SCROLL_LENGTH;
			/*
			 * Coefficients for relative binding
			 * between axes data and scroll bar parameters.
			 * We have linear dependence formula z = coeff*w + shift
			 * where w - point of chart, z - point of scroll bar
			 * and coeff and shift - some coefficients
			 */
			double coeffX = (maxX - minX) / (baseChart.getMaxX() - baseChart.getMinX());
			double coeffY = (maxY - minY) / (baseChart.getMaxY() - baseChart.getMinY());
			//
			double shiftX = minX - coeffX * baseChart.getMinX();
			double shiftY = minY - coeffY * baseChart.getMinY();
			//
			int minSelectionX = (int)(xAxis.getRange().lower * coeffX + shiftX);
			int maxSelectionX = (int)(xAxis.getRange().upper * coeffX + shiftX);
			int thumbSelectionX = maxSelectionX - minSelectionX;
			//
			int minSelectionY = (int)(yAxis.getRange().lower * coeffY + shiftY);
			int maxSelectionY = (int)(yAxis.getRange().upper * coeffY + shiftY);
			int thumbSelectionY = maxSelectionY - minSelectionY;
			//
			boolean isHorizontal = isOrientationHorizontal();
			//
			sliderVertical.setMinimum((isHorizontal) ? minY : minX);
			sliderVertical.setMaximum((isHorizontal) ? maxY : maxX);
			sliderVertical.setThumb((isHorizontal) ? thumbSelectionY : thumbSelectionX);
			sliderVertical.setSelection((isHorizontal) ? minSelectionY : minSelectionX);
			//
			sliderHorizontal.setMinimum((isHorizontal) ? minX : minY);
			sliderHorizontal.setMaximum((isHorizontal) ? maxX : maxY);
			sliderHorizontal.setThumb((isHorizontal) ? thumbSelectionX : thumbSelectionY);
			sliderHorizontal.setSelection((isHorizontal) ? minSelectionX : minSelectionY);
			/*
			 * Calculate the increment.
			 */
			if(calculateIncrement) {
				int thumbX = maxX - minX;
				int thumbY = maxY - minY;
				int incrementX = calculateIncrement(thumbX, baseChart.getSeriesMaxDataPoints());
				int incrementY = calculateIncrement(thumbY, baseChart.getSeriesMaxDataPoints());
				sliderHorizontal.setIncrement((isHorizontal) ? incrementX : incrementY);
				sliderVertical.setIncrement((isHorizontal) ? incrementY : incrementX);
				sliderHorizontal.setPageIncrement((isHorizontal) ? incrementX : incrementY);
			}
			/*
			 * Set the range info and update linked charts.
			 */
			displayRangeInfo(xAxis, yAxis);
		}
	}

	private boolean isOrientationHorizontal() {

		return (baseChart.getOrientation() == SWT.HORIZONTAL) ? true : false;
	}

	private int calculateIncrement(double selection, double length) {

		if(length == 0) {
			return 0;
		} else {
			int increment = (int)(selection / length);
			return (increment < 1) ? 1 : increment;
		}
	}

	private Range calculateShiftedRange(Range range, Slider slider, int sliderOrientation) {

		int minX = 0;
		int maxX = minX + HORIZONTAL_SCROLL_LENGTH;
		int minY = 0;
		int maxY = minY + VERTICAL_SCROLL_LENGTH;
		/*
		 * Coefficients for relative binding
		 * between axes data and scroll bar parameters.
		 * We have linear dependence formula z = coeff*w + shift
		 * where w - point of chart, z - point of scroll bar
		 * and coeff and shift - some coefficients.
		 * And for inverse dependence we have x = (y-b)/k
		 */
		double coeffX = (maxX - minX) / (baseChart.getMaxX() - baseChart.getMinX());
		double coeffY = (maxY - minY) / (baseChart.getMaxY() - baseChart.getMinY());
		//
		double shiftX = minX - coeffX * baseChart.getMinX();
		double shiftY = minY - coeffY * baseChart.getMinY();
		//
		int selection = slider.getSelection();
		//
		boolean isChartHorizontal = isOrientationHorizontal();
		//
		double min = (sliderOrientation == SWT.HORIZONTAL && isChartHorizontal || sliderOrientation == SWT.VERTICAL && !isChartHorizontal ? (selection - shiftX) / coeffX : (selection - shiftY) / coeffY);
		double max = min + (range.upper - range.lower);
		return new Range(min, max);
	}

	private void addPrimaryAxisX(IChartSettings chartSettings) {

		IAxisSet axisSet = baseChart.getAxisSet();
		IAxis xAxisPrimary = axisSet.getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
		IPrimaryAxisSettings primaryAxisSettings = chartSettings.getPrimaryAxisSettingsX();
		setAxisSettings(xAxisPrimary, primaryAxisSettings);
		baseChart.putXAxisSettings(BaseChart.ID_PRIMARY_X_AXIS, primaryAxisSettings);
	}

	private void addPrimaryAxisY(IChartSettings chartSettings) {

		IAxisSet axisSet = baseChart.getAxisSet();
		IAxis yAxisPrimary = axisSet.getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
		IPrimaryAxisSettings primaryAxisSettings = chartSettings.getPrimaryAxisSettingsY();
		setAxisSettings(yAxisPrimary, primaryAxisSettings);
		baseChart.putYAxisSettings(BaseChart.ID_PRIMARY_Y_AXIS, primaryAxisSettings);
	}

	private void addSecondaryAxesX(IChartSettings chartSettings) {

		IAxisSet axisSet = baseChart.getAxisSet();
		for(int id : axisSet.getXAxisIds()) {
			if(id != BaseChart.ID_PRIMARY_X_AXIS) {
				axisSet.deleteXAxis(id);
			}
		}
		/*
		 * Remove all items except the primary axis settings.
		 */
		baseChart.removeXAxisSettings();
		/*
		 * Add the axis settings.
		 */
		for(ISecondaryAxisSettings secondaryAxisSettings : chartSettings.getSecondaryAxisSettingsListX()) {
			int xAxisId = axisSet.createXAxis();
			IAxis xAxisSecondary = axisSet.getXAxis(xAxisId);
			setAxisSettings(xAxisSecondary, secondaryAxisSettings);
			baseChart.putXAxisSettings(xAxisId, secondaryAxisSettings);
		}
	}

	private void addSecondaryAxesY(IChartSettings chartSettings) {

		IAxisSet axisSet = baseChart.getAxisSet();
		for(int id : axisSet.getYAxisIds()) {
			if(id != BaseChart.ID_PRIMARY_Y_AXIS) {
				axisSet.deleteYAxis(id);
			}
		}
		/*
		 * Remove all items except the primary axis settings.
		 */
		baseChart.removeYAxisSettings();
		/*
		 * Add the axis settings.
		 */
		for(ISecondaryAxisSettings secondaryAxisSettings : chartSettings.getSecondaryAxisSettingsListY()) {
			int yAxisId = axisSet.createYAxis();
			IAxis yAxisSecondary = axisSet.getYAxis(yAxisId);
			setAxisSettings(yAxisSecondary, secondaryAxisSettings);
			baseChart.putYAxisSettings(yAxisId, secondaryAxisSettings);
		}
	}

	private void setAxisSettings(IAxis axis, IAxisSettings axisSettings) {

		if(axis != null && axisSettings != null) {
			//
			String axisText = axisSettings.getTitle();
			ITitle title = axis.getTitle();
			title.setText(axisText);
			title.setVisible(axisSettings.isVisible() && axisSettings.isTitleVisible());
			//
			IAxisTick axisTick = axis.getTick();
			axisTick.setFormat(axisSettings.getDecimalFormat());
			axisTick.setVisible(axisSettings.isVisible());
			//
			IGrid grid = axis.getGrid();
			grid.setForeground(axisSettings.getGridColor());
			grid.setStyle(axisSettings.getGridLineStyle());
			//
			axis.setPosition(axisSettings.getPosition());
			/*
			 * Set the color on demand.
			 */
			Color color = axisSettings.getColor();
			if(color != null) {
				title.setForeground(color);
				axisTick.setForeground(color);
			}
			/*
			 * Add a space between the scale and the label.
			 */
			Font font = title.getFont();
			int length = axisText.length() - 1;
			StyleRange styleRange = new StyleRange();
			styleRange.length = (length > 0) ? length : 0;
			styleRange.background = baseChart.getBackground();
			styleRange.foreground = (color != null) ? color : baseChart.getForeground();
			styleRange.font = font;
			styleRange.rise = getAxisExtraSpaceTitle(axis, axisSettings);
			title.setStyleRanges(new StyleRange[]{styleRange});
			//
			axis.enableLogScale(axisSettings.isEnableLogScale());
			axis.setReversed(axisSettings.isReversed());
			/*
			 * Apply primary axis specific settings.
			 */
			if(axisSettings instanceof IPrimaryAxisSettings) {
				IPrimaryAxisSettings primaryAxisSettings = (IPrimaryAxisSettings)axisSettings;
				axis.enableLogScale(primaryAxisSettings.isEnableLogScale());
				/*
				 * Category is only valid for the X-Axis.
				 */
				if(axis.getDirection() == Direction.X) {
					axis.enableCategory(primaryAxisSettings.isEnableCategory());
					axis.setCategorySeries(primaryAxisSettings.getCategorySeries());
				}
			}
		}
	}

	private int getAxisExtraSpaceTitle(IAxis axis, IAxisSettings axisSettings) {

		int extraSpaceTitle = axisSettings.getExtraSpaceTitle();
		int orientation = getChartSettings().getOrientation();
		Direction direction = axis.getDirection();
		/*
		 * Default orientation == SWT.HORIZONTAL
		 */
		if(direction.equals(Direction.X)) {
			/*
			 * X-Axis
			 * Primary = bottom side
			 * Secondary = top side
			 */
			if(axisSettings.getPosition().equals(Position.Primary)) {
				extraSpaceTitle *= -1;
			}
		} else {
			/*
			 * Y-Axis
			 * Primary = left side
			 * Secondary = right side
			 */
			if(axisSettings.getPosition().equals(Position.Secondary)) {
				extraSpaceTitle *= -1;
			}
		}
		/*
		 * Switch the side of the margin.
		 */
		if(orientation == SWT.VERTICAL) {
			extraSpaceTitle *= -1;
		}
		//
		return extraSpaceTitle;
	}

	private void fireUpdateCustomRangeSelectionHandlers(Event event) {

		baseChart.fireUpdateCustomRangeSelectionHandlers(event);
		updateLinkedCharts();
	}

	private void displayRangeInfo(IAxis xAxis, IAxis yAxis) {

		rangeSelector.adjustRanges(true);
	}

	private void initialize() {

		this.setLayout(new FillLayout());
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		/*
		 * Composite
		 */
		createSliderVertical(composite);
		createChart(composite);
		createSliderHorizontal(composite);
	}

	private void createSliderVertical(Composite parent) {

		sliderVertical = new Slider(parent, SWT.VERTICAL);
		sliderVertical.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		sliderVertical.setOrientation(SWT.RIGHT_TO_LEFT); // See Bug #511257
		sliderVertical.setVisible(true);
		sliderVertical.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {

				IAxis xAxis = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
				IAxis yAxis = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
				//
				if(xAxis != null && yAxis != null) {
					Range range = calculateShiftedRange(yAxis.getRange(), sliderVertical, SWT.VERTICAL);
					if(isOrientationHorizontal()) {
						yAxis.setRange(range);
						baseChart.adjustMinMaxRange(yAxis);
						adjustSecondaryYAxes();
					} else {
						xAxis.setRange(range);
						baseChart.adjustMinMaxRange(xAxis);
						adjustSecondaryXAxes();
					}
					//
					displayRangeInfo(xAxis, yAxis);
					fireUpdateCustomRangeSelectionHandlers(event);
					baseChart.redraw();
				}
			}
		});
	}

	private void createChart(Composite parent) {

		/*
		 * Chart Area
		 */
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(1, true));
		//
		createRangeInfoUI(composite);
		createBaseChart(composite);
	}

	private void createRangeInfoUI(Composite parent) {

		rangeSelector = new RangeSelector(parent, SWT.NONE, this);
		rangeSelector.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private void createBaseChart(Composite parent) {

		/*
		 * Chart Plot
		 */
		baseChart = new BaseChart(parent, SWT.NONE);
		baseChart.setLayoutData(new GridData(GridData.FILL_BOTH));
		/*
		 * Set the slider range.
		 */
		baseChart.addCustomRangeSelectionHandler(new ICustomSelectionHandler() {

			@Override
			public void handleUserSelection(Event event) {

				setSliderSelection(false);
				if(getChartSettings().isEnableRangeSelector()) {
					rangeSelector.adjustRanges(false);
				}
				updateLinkedCharts();
			}
		});
		/*
		 * Activate the range info UI on double click.
		 */
		baseChart.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				IChartSettings chartSettings = baseChart.getChartSettings();
				if(chartSettings.isEnableRangeSelector()) {
					if(!rangeSelector.isVisible()) {
						if(e.y <= 47) {
							/*
							 * Show the range info composite.
							 */
							showRangeSelectorHint = enableRangeSelectorHint;
							showRangeSelector(showRangeSelectorHint);
						}
					}
				}
			}
		});
		/*
		 * Show the range info hint.
		 */
		rangeHintPaintListener = new RangeHintPaintListener();
		baseChart.addPaintListener(rangeHintPaintListener);
		/*
		 * Add the listeners.
		 */
		Composite plotArea = baseChart.getPlotArea();
		plotArea.addListener(SWT.KeyDown, this);
		plotArea.addListener(SWT.KeyUp, this);
		plotArea.addListener(SWT.MouseMove, this);
		plotArea.addListener(SWT.MouseDown, this);
		plotArea.addListener(SWT.MouseUp, this);
		plotArea.addListener(SWT.MouseWheel, this);
		plotArea.addListener(SWT.MouseDoubleClick, this);
		plotArea.addListener(SWT.Resize, this);
		plotArea.addPaintListener(this);
	}

	private void createSliderHorizontal(Composite parent) {

		sliderHorizontal = new Slider(parent, SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		sliderHorizontal.setLayoutData(gridData);
		sliderHorizontal.setOrientation(SWT.LEFT_TO_RIGHT);
		sliderHorizontal.setVisible(true);
		sliderHorizontal.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {

				IAxis xAxis = baseChart.getAxisSet().getXAxis(BaseChart.ID_PRIMARY_X_AXIS);
				IAxis yAxis = baseChart.getAxisSet().getYAxis(BaseChart.ID_PRIMARY_Y_AXIS);
				//
				if(xAxis != null && yAxis != null) {
					Range range = calculateShiftedRange(xAxis.getRange(), sliderHorizontal, SWT.HORIZONTAL);
					if(isOrientationHorizontal()) {
						xAxis.setRange(range);
						baseChart.adjustMinMaxRange(xAxis);
						adjustSecondaryXAxes();
					} else {
						yAxis.setRange(range);
						baseChart.adjustMinMaxRange(yAxis);
						adjustSecondaryYAxes();
					}
					//
					displayRangeInfo(xAxis, yAxis);
					fireUpdateCustomRangeSelectionHandlers(event);
					baseChart.redraw();
				}
			}
		});
	}

	private void updateLinkedCharts() {

		IAxisSet axisSet = baseChart.getAxisSet();
		Range rangeX = axisSet.getXAxis(BaseChart.ID_PRIMARY_X_AXIS).getRange();
		Range rangeY = axisSet.getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).getRange();
		/*
		 * Adjust the range of the linked charts.
		 */
		for(ScrollableChart linkedScrollableChart : linkedScrollableCharts) {
			IAxisSet axisSetLinked = linkedScrollableChart.getBaseChart().getAxisSet();
			axisSetLinked.getXAxis(BaseChart.ID_PRIMARY_X_AXIS).setRange(rangeX);
			axisSetLinked.getYAxis(BaseChart.ID_PRIMARY_Y_AXIS).setRange(rangeY);
			linkedScrollableChart.getBaseChart().adjustSecondaryAxes();
			linkedScrollableChart.getBaseChart().redraw();
			/*
			 * The method setSliderSelection(...) is private.
			 * But as we are in the same class, it works.
			 * Funny, I assumed that only protected and public
			 * methods are accessible.
			 */
			linkedScrollableChart.setSliderSelection(false);
			if(linkedScrollableChart.getChartSettings().isEnableRangeSelector()) {
				linkedScrollableChart.getRangeSelector().adjustRanges(false);
			}
		}
	}

	private void widgetSelected(Event e) {

		if(!(e.widget instanceof MenuItem)) {
			return;
		}
		/*
		 * Get the entry and execute it.
		 */
		MenuItem menuItem = (MenuItem)e.widget;
		IChartMenuEntry menuEntry = menuEntryMap.get(menuItem.getText());
		if(menuEntry != null) {
			menuEntry.execute(getShell(), this);
		}
	}

	private void createPopupMenu() {

		Composite plotArea = baseChart.getPlotArea();
		Menu menu = new Menu(plotArea);
		Menu m = plotArea.getMenu();
		if(m != null) {
			m.dispose();
		}
		plotArea.setMenu(menu);
		createMenuItems(menu);
	}

	private void addMenuEntry(IChartMenuEntry menuEntry) {

		if(menuEntry != null) {
			String category = menuEntry.getCategory();
			Set<IChartMenuEntry> menuEntries = categoryMenuEntriesMap.get(category);
			/*
			 * Create set if not existent.
			 */
			if(menuEntries == null) {
				menuEntries = new HashSet<IChartMenuEntry>();
				categoryMenuEntriesMap.put(category, menuEntries);
			}
			/*
			 * Add the entry.
			 */
			menuEntries.add(menuEntry);
			menuEntryMap.put(menuEntry.getName(), menuEntry);
		}
	}

	private void createMenuItems(Menu menu) {

		List<String> categories = new ArrayList<String>(categoryMenuEntriesMap.keySet());
		Collections.sort(categories);
		Iterator<String> iterator = categories.iterator();
		while(iterator.hasNext()) {
			String category = iterator.next();
			createMenuCategory(menu, category, categoryMenuEntriesMap.get(category));
			if(iterator.hasNext()) {
				new MenuItem(menu, SWT.SEPARATOR);
			}
		}
	}

	private void createMenuCategory(Menu menu, String category, Set<IChartMenuEntry> menuEntries) {

		Menu subMenu;
		MenuItem menuItem;
		/*
		 * Get the menu.
		 */
		if(category.equals("")) {
			subMenu = menu;
		} else {
			menuItem = new MenuItem(menu, SWT.CASCADE);
			menuItem.setText(category);
			subMenu = new Menu(menuItem);
			menuItem.setMenu(subMenu);
		}
		/*
		 * Add the items.
		 */
		List<String> names = getSortedNames(menuEntries);
		for(String name : names) {
			menuItem = new MenuItem(subMenu, SWT.PUSH);
			menuItem.setText(name);
			menuItem.addListener(SWT.Selection, this);
		}
	}

	private List<String> getSortedNames(Set<IChartMenuEntry> menuEntries) {

		List<String> names = new ArrayList<String>();
		for(IChartMenuEntry menuEntry : menuEntries) {
			if(menuEntry.isEnabled(this)) {
				names.add(menuEntry.getName());
			}
		}
		//
		Collections.sort(names);
		return names;
	}
}