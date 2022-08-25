/*******************************************************************************
 * Copyright (c) 2017, 2022 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Constants;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;
import org.eclipse.swtchart.extensions.events.MouseDownEvent;
import org.eclipse.swtchart.extensions.events.MouseMoveCursorEvent;
import org.eclipse.swtchart.extensions.events.MouseMoveSelectionEvent;
import org.eclipse.swtchart.extensions.events.MouseMoveShiftEvent;
import org.eclipse.swtchart.extensions.events.MouseUpEvent;
import org.eclipse.swtchart.extensions.events.MouseWheelEvent;
import org.eclipse.swtchart.extensions.events.ResetSeriesEvent;
import org.eclipse.swtchart.extensions.events.SelectDataPointEvent;
import org.eclipse.swtchart.extensions.events.SelectHideSeriesEvent;
import org.eclipse.swtchart.extensions.events.UndoRedoEvent;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;
import org.eclipse.swtchart.extensions.menu.RedoSelectionHandler;
import org.eclipse.swtchart.extensions.menu.ResetChartHandler;
import org.eclipse.swtchart.extensions.menu.ResetSelectedSeriesHandler;
import org.eclipse.swtchart.extensions.menu.SetRangeChartHandler;
import org.eclipse.swtchart.extensions.menu.UndoSelectionHandler;
import org.eclipse.swtchart.extensions.menu.toggle.ToggleAxisZeroMarkerHandler;
import org.eclipse.swtchart.extensions.menu.toggle.ToggleLabelTooltipsHandler;
import org.eclipse.swtchart.extensions.menu.toggle.ToggleLegendMarkerHandler;
import org.eclipse.swtchart.extensions.menu.toggle.TogglePlotCenterMarkerHandler;
import org.eclipse.swtchart.extensions.menu.toggle.TogglePositionMarkerHandler;
import org.eclipse.swtchart.extensions.menu.toggle.ToggleRangeSelectorHandler;
import org.eclipse.swtchart.extensions.menu.toggle.ToggleSeriesLabelMarkerHandler;
import org.eclipse.swtchart.extensions.menu.toggle.ToggleSeriesLegendHandler;
import org.eclipse.swtchart.extensions.preferences.PreferenceConstants;

public class ChartSettings implements IChartSettings {

	/*
	 * As it turned out, that buffered rendering under macOS fails somehow,
	 * the bufferSelection option is deactivated by default. Charts shall
	 * enable buffering via an option, so that it can be tested in several
	 * environments, e.g. macOS, GTK3, ... .
	 */
	private boolean bufferSelection = false;
	//
	private boolean enableRangeSelector = false;
	private boolean showRangeSelectorInitially = true;
	private Color colorHintRangeSelector;
	private int rangeSelectorDefaultAxisX = 0;
	private int rangeSelectorDefaultAxisY = 0;
	//
	private boolean verticalSliderVisible = false;
	private boolean horizontalSliderVisible = true;
	/*
	 * If the title is empty, it won't be displayed.
	 * To display a space on top of the chart, a default
	 * title is set and WHITE is used to hide it.
	 */
	private String title = Messages.getString(Messages.CHART_TITLE);
	private boolean titleVisible = true;
	private Color titleColor;
	private Font titleFont;
	//
	private int legendPosition = SWT.RIGHT;
	private boolean legendVisible = false;
	private boolean legendExtendedVisible = false;
	//
	private IPrimaryAxisSettings primaryAxisSettingsX = new PrimaryAxisSettings(BaseChart.DEFAULT_TITLE_X_AXIS);
	private IPrimaryAxisSettings primaryAxisSettingsY = new PrimaryAxisSettings(BaseChart.DEFAULT_TITLE_Y_AXIS);
	private List<ISecondaryAxisSettings> secondaryAxisSettingsListX = new ArrayList<ISecondaryAxisSettings>();
	private List<ISecondaryAxisSettings> secondaryAxisSettingsListY = new ArrayList<ISecondaryAxisSettings>();
	//
	private int orientation = SWT.HORIZONTAL;
	private Color background;
	private Color backgroundChart;
	private Color backgroundPlotArea;
	private boolean enableCompress = true;
	private RangeRestriction rangeRestriction = new RangeRestriction();
	//
	private boolean showPositionMarker = false;
	private Color colorPositionMarker;
	private boolean showPlotCenterMarker = false;
	private Color colorPlotCenterMarker;
	private boolean showLegendMarker = false;
	private Color colorLegendMarker;
	private boolean showAxisZeroMarker = false;
	private Color colorAxisZeroMarker;
	private boolean showSeriesLabelMarker = false;
	private Color colorSeriesLabelMarker;
	//
	private boolean createMenu = true;
	private Set<IChartMenuEntry> menuEntries;
	private Set<IHandledEventProcessor> handledEventProcessors;
	//
	private boolean supportDataShift = false;
	private boolean enableTooltips = false; // It was set to true before, but this leads to some conflicts when performing selection operations.
	/*
	 * The default font is only used if no font is set.
	 */
	private final Font defaultFont = new Font(Display.getDefault(), "Tahoma", Constants.MEDIUM_FONT_SIZE, SWT.BOLD); //$NON-NLS-1$

	public ChartSettings() {

		Display display = Display.getDefault();
		//
		IPreferenceStore preferenceStore = ResourceSupport.getPreferenceStore();
		if(preferenceStore != null) {
			setBufferSelection(preferenceStore.getBoolean(PreferenceConstants.P_BUFFER_SELECTION));
		}
		//
		colorHintRangeSelector = display.getSystemColor(SWT.COLOR_RED);
		//
		titleColor = display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
		titleFont = defaultFont;
		//
		background = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		backgroundChart = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		backgroundPlotArea = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		rangeRestriction.setZeroX(true);
		rangeRestriction.setZeroY(true);
		rangeRestriction.setRestrictFrame(true);
		//
		if(Display.isSystemDarkTheme()) {
			colorPositionMarker = display.getSystemColor(SWT.COLOR_GRAY);
			colorPlotCenterMarker = display.getSystemColor(SWT.COLOR_GRAY);
			colorLegendMarker = display.getSystemColor(SWT.COLOR_GRAY);
			colorAxisZeroMarker = display.getSystemColor(SWT.COLOR_GRAY);
			colorSeriesLabelMarker = display.getSystemColor(SWT.COLOR_GRAY);
		} else {
			colorPositionMarker = display.getSystemColor(SWT.COLOR_DARK_GRAY);
			colorPlotCenterMarker = display.getSystemColor(SWT.COLOR_DARK_GRAY);
			colorLegendMarker = display.getSystemColor(SWT.COLOR_DARK_GRAY);
			colorAxisZeroMarker = display.getSystemColor(SWT.COLOR_DARK_GRAY);
			colorSeriesLabelMarker = display.getSystemColor(SWT.COLOR_DARK_GRAY);
		}
		/*
		 * Default menu entries.
		 */
		menuEntries = new HashSet<>();
		menuEntries.add(new ResetChartHandler());
		menuEntries.add(new SetRangeChartHandler());
		menuEntries.add(new ResetSelectedSeriesHandler());
		menuEntries.add(new ToggleRangeSelectorHandler());
		menuEntries.add(new ToggleLegendMarkerHandler());
		menuEntries.add(new TogglePositionMarkerHandler());
		menuEntries.add(new ToggleSeriesLegendHandler());
		menuEntries.add(new TogglePlotCenterMarkerHandler());
		menuEntries.add(new ToggleAxisZeroMarkerHandler());
		menuEntries.add(new ToggleSeriesLabelMarkerHandler());
		menuEntries.add(new ToggleLabelTooltipsHandler());
		menuEntries.add(new UndoSelectionHandler());
		menuEntries.add(new RedoSelectionHandler());
		/*
		 * Export options have been moved to the bundle:
		 * org.eclipse.swtchart.export
		 * They are added via the extension point:
		 * org.eclipse.swtchart.extensions.menuitems
		 */
		/*
		 * Events processors ... Mouse Move, Key Up ...
		 */
		handledEventProcessors = new HashSet<>();
		handledEventProcessors.add(new SelectHideSeriesEvent());
		handledEventProcessors.add(new ResetSeriesEvent());
		handledEventProcessors.add(new SelectDataPointEvent());
		handledEventProcessors.add(new MouseWheelEvent());
		handledEventProcessors.add(new MouseDownEvent());
		handledEventProcessors.add(new MouseMoveSelectionEvent());
		handledEventProcessors.add(new MouseMoveShiftEvent());
		handledEventProcessors.add(new MouseMoveCursorEvent());
		handledEventProcessors.add(new MouseUpEvent());
		handledEventProcessors.add(new UndoRedoEvent());
	}

	@Override
	public IChartMenuEntry getChartMenuEntryByClass(Class<?> clazz) {

		for(IChartMenuEntry menuEntry : menuEntries) {
			if(menuEntry.getClass().equals(clazz)) {
				return menuEntry;
			}
		}
		return null;
	}

	@Override
	public IHandledEventProcessor getHandledEventProcessorByClass(Class<?> clazz) {

		for(IHandledEventProcessor handledEventProcessor : handledEventProcessors) {
			if(handledEventProcessor.getClass().equals(clazz)) {
				return handledEventProcessor;
			}
		}
		return null;
	}

	@Override
	public boolean isBufferSelection() {

		return bufferSelection;
	}

	@Override
	public void setBufferSelection(boolean bufferSelection) {

		/*
		 * Limitations
		 * macOS - https://github.com/eclipse/swtchart/issues/150
		 * GTK3 - https://github.com/eclipse/swtchart/issues/166
		 */
		if(isMacOS()) {
			System.out.println("Can't set buffer selection on macOS true, see: https://github.com/eclipse/swtchart/issues/150");
			this.bufferSelection = false;
		} else {
			this.bufferSelection = bufferSelection;
		}
	}

	@Override
	public boolean isEnableRangeSelector() {

		return enableRangeSelector;
	}

	@Override
	public void setEnableRangeSelector(boolean enableRangeSelector) {

		this.enableRangeSelector = enableRangeSelector;
	}

	@Override
	public boolean isShowRangeSelectorInitially() {

		return showRangeSelectorInitially;
	}

	@Override
	public void setShowRangeSelectorInitially(boolean showRangeSelectorInitially) {

		this.showRangeSelectorInitially = showRangeSelectorInitially;
	}

	@Override
	public Color getColorHintRangeSelector() {

		return colorHintRangeSelector;
	}

	@Override
	public void setColorHintRangeSelector(Color colorHintRangeSelector) {

		this.colorHintRangeSelector = colorHintRangeSelector;
	}

	@Override
	public int getRangeSelectorDefaultAxisX() {

		return rangeSelectorDefaultAxisX;
	}

	@Override
	public void setRangeSelectorDefaultAxisX(int rangeSelectorDefaultAxisX) {

		this.rangeSelectorDefaultAxisX = rangeSelectorDefaultAxisX;
	}

	@Override
	public int getRangeSelectorDefaultAxisY() {

		return rangeSelectorDefaultAxisY;
	}

	@Override
	public void setRangeSelectorDefaultAxisY(int rangeSelectorDefaultAxisY) {

		this.rangeSelectorDefaultAxisY = rangeSelectorDefaultAxisY;
	}

	@Override
	public boolean isVerticalSliderVisible() {

		return verticalSliderVisible;
	}

	@Override
	public void setVerticalSliderVisible(boolean verticalSliderVisible) {

		this.verticalSliderVisible = verticalSliderVisible;
	}

	@Override
	public boolean isHorizontalSliderVisible() {

		return horizontalSliderVisible;
	}

	@Override
	public void setHorizontalSliderVisible(boolean horizontalSliderVisible) {

		this.horizontalSliderVisible = horizontalSliderVisible;
	}

	@Override
	public String getTitle() {

		return title;
	}

	@Override
	public void setTitle(String title) {

		if(title != null) {
			this.title = title;
		} else {
			this.title = ""; //$NON-NLS-1$
		}
	}

	@Override
	public boolean isTitleVisible() {

		return titleVisible;
	}

	@Override
	public void setTitleVisible(boolean titleVisible) {

		this.titleVisible = titleVisible;
	}

	@Override
	public Color getTitleColor() {

		return titleColor;
	}

	@Override
	public void setTitleColor(Color titleColor) {

		this.titleColor = titleColor;
	}

	@Override
	public Font getTitleFont() {

		return titleFont;
	}

	@Override
	public void setTitleFont(Font titleFont) {

		this.titleFont = titleFont;
	}

	@Override
	public int getLegendPosition() {

		return legendPosition;
	}

	@Override
	public void setLegendPosition(int legendPosition) {

		this.legendPosition = legendPosition;
	}

	@Override
	public boolean isLegendVisible() {

		return legendVisible;
	}

	@Override
	public void setLegendVisible(boolean legendVisible) {

		this.legendVisible = legendVisible;
	}

	@Override
	public boolean isLegendExtendedVisible() {

		return legendExtendedVisible;
	}

	@Override
	public void setLegendExtendedVisible(boolean legendExtendedVisible) {

		this.legendExtendedVisible = legendExtendedVisible;
	}

	@Override
	public IPrimaryAxisSettings getPrimaryAxisSettingsX() {

		return primaryAxisSettingsX;
	}

	@Override
	public IPrimaryAxisSettings getPrimaryAxisSettingsY() {

		return primaryAxisSettingsY;
	}

	@Override
	public List<ISecondaryAxisSettings> getSecondaryAxisSettingsListX() {

		return secondaryAxisSettingsListX;
	}

	@Override
	public List<ISecondaryAxisSettings> getSecondaryAxisSettingsListY() {

		return secondaryAxisSettingsListY;
	}

	@Override
	public int getOrientation() {

		return orientation;
	}

	/**
	 * SWT.HORIZONTAL or SWT.VERTICAL
	 * See:http://www.swtchart.org/doc/index.html#Chart_Orientation
	 * 
	 * @param orientation
	 */
	@Override
	public void setOrientation(int orientation) {

		this.orientation = orientation;
	}

	@Override
	public Color getBackground() {

		return background;
	}

	@Override
	public void setBackground(Color background) {

		this.background = background;
	}

	@Override
	public Color getBackgroundChart() {

		return backgroundChart;
	}

	@Override
	public void setBackgroundChart(Color backgroundChart) {

		this.backgroundChart = backgroundChart;
	}

	@Override
	public Color getBackgroundPlotArea() {

		return backgroundPlotArea;
	}

	@Override
	public void setBackgroundPlotArea(Color backgroundPlotArea) {

		this.backgroundPlotArea = backgroundPlotArea;
	}

	@Override
	public boolean isEnableCompress() {

		return enableCompress;
	}

	@Override
	public void setEnableCompress(boolean enableCompress) {

		this.enableCompress = enableCompress;
	}

	@Override
	public RangeRestriction getRangeRestriction() {

		return rangeRestriction;
	}

	@Override
	public boolean isShowPositionMarker() {

		return showPositionMarker;
	}

	@Override
	public void setShowPositionMarker(boolean showPositionMarker) {

		this.showPositionMarker = showPositionMarker;
	}

	@Override
	public Color getColorPositionMarker() {

		return colorPositionMarker;
	}

	@Override
	public void setColorPositionMarker(Color colorPositionMarker) {

		this.colorPositionMarker = colorPositionMarker;
	}

	@Override
	public boolean isShowPlotCenterMarker() {

		return showPlotCenterMarker;
	}

	@Override
	public void setShowPlotCenterMarker(boolean showPlotCenterMarker) {

		this.showPlotCenterMarker = showPlotCenterMarker;
	}

	@Override
	public Color getColorPlotCenterMarker() {

		return colorPlotCenterMarker;
	}

	@Override
	public void setColorPlotCenterMarker(Color colorPlotCenterMarker) {

		this.colorPlotCenterMarker = colorPlotCenterMarker;
	}

	@Override
	public boolean isShowLegendMarker() {

		return showLegendMarker;
	}

	@Override
	public void setShowLegendMarker(boolean showLegendMarker) {

		this.showLegendMarker = showLegendMarker;
	}

	@Override
	public Color getColorLegendMarker() {

		return colorLegendMarker;
	}

	@Override
	public void setColorLegendMarker(Color colorLegendMarker) {

		this.colorLegendMarker = colorLegendMarker;
	}

	@Override
	public boolean isShowAxisZeroMarker() {

		return showAxisZeroMarker;
	}

	@Override
	public void setShowAxisZeroMarker(boolean showAxisZeroMarker) {

		this.showAxisZeroMarker = showAxisZeroMarker;
	}

	@Override
	public Color getColorAxisZeroMarker() {

		return colorAxisZeroMarker;
	}

	@Override
	public void setColorAxisZeroMarker(Color colorAxisZeroMarker) {

		this.colorAxisZeroMarker = colorAxisZeroMarker;
	}

	@Override
	public boolean isShowSeriesLabelMarker() {

		return showSeriesLabelMarker;
	}

	@Override
	public void setShowSeriesLabelMarker(boolean showSeriesLabelMarker) {

		this.showSeriesLabelMarker = showSeriesLabelMarker;
	}

	@Override
	public Color getColorSeriesLabelMarker() {

		return colorSeriesLabelMarker;
	}

	@Override
	public void setColorSeriesLabelMarker(Color colorSeriesLabelMarker) {

		this.colorSeriesLabelMarker = colorSeriesLabelMarker;
	}

	@Override
	public boolean isCreateMenu() {

		return createMenu;
	}

	@Override
	public void setCreateMenu(boolean createMenu) {

		this.createMenu = createMenu;
	}

	@Override
	public void addMenuEntry(IChartMenuEntry menuEntry) {

		menuEntries.add(menuEntry);
	}

	@Override
	public void removeMenuEntry(IChartMenuEntry menuEntry) {

		menuEntries.remove(menuEntry);
	}

	@Override
	public Set<IChartMenuEntry> getMenuEntries() {

		return Collections.unmodifiableSet(menuEntries);
	}

	@Override
	public IChartMenuEntry getChartMenuEntry(String name) {

		for(IChartMenuEntry chartMenuEntry : menuEntries) {
			if(chartMenuEntry.getName().equals(name)) {
				return chartMenuEntry;
			}
		}
		//
		return null;
	}

	@Override
	public void clearMenuEntries() {

		menuEntries.clear();
	}

	@Override
	public boolean isSupportDataShift() {

		return supportDataShift;
	}

	@Override
	public void setSupportDataShift(boolean supportDataShift) {

		this.supportDataShift = supportDataShift;
	}

	@Override
	public void addHandledEventProcessor(IHandledEventProcessor handledEventProcessor) {

		handledEventProcessors.add(handledEventProcessor);
	}

	@Override
	public void removeHandledEventProcessor(IHandledEventProcessor handledEventProcessor) {

		handledEventProcessors.remove(handledEventProcessor);
	}

	@Override
	public Set<IHandledEventProcessor> getHandledEventProcessors() {

		return Collections.unmodifiableSet(handledEventProcessors);
	}

	@Override
	public void clearHandledEventProcessors() {

		handledEventProcessors.clear();
	}

	@Override
	public void setEnableTooltips(boolean enable) {

		enableTooltips = enable;
	}

	@Override
	public boolean isEnableTooltips() {

		return enableTooltips;
	}

	@Override
	protected void finalize() throws Throwable {

		if(!defaultFont.isDisposed()) {
			defaultFont.dispose();
		}
		if(!titleFont.isDisposed()) {
			titleFont.dispose();
		}
	}

	private boolean isMacOS() {

		String os = System.getProperty("os.name");
		if(os != null) {
			return os.toLowerCase().indexOf("mac") >= 0;
		}
		return false;
	}
}
