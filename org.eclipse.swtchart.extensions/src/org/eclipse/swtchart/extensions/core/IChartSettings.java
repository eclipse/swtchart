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
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swtchart.extensions.events.IHandledEventProcessor;
import org.eclipse.swtchart.extensions.menu.IChartMenuEntry;

public interface IChartSettings {

	/**
	 * This method returns null if the class instance is not available.
	 * 
	 * @param clazz
	 * @return {@link IChartMenuEntry>
	 */
	IChartMenuEntry getChartMenuEntryByClass(Class<?> clazz);

	/**
	 * This method returns null if the class instance is not available.
	 * 
	 * @param clazz
	 * @return {@link IHandledEventProcessor}
	 */
	IHandledEventProcessor getHandledEventProcessorByClass(Class<?> clazz);

	void setEnableTooltips(boolean enable);

	boolean isEnableTooltips();

	boolean isEnableRangeSelector();

	void setEnableRangeSelector(boolean enableRangeSelector);

	boolean isShowRangeSelectorInitially();

	void setShowRangeSelectorInitially(boolean showRangeSelectorInitially);

	int getRangeSelectorDefaultAxisX();

	void setRangeSelectorDefaultAxisX(int rangeSelectorDefaultAxisX);

	int getRangeSelectorDefaultAxisY();

	void setRangeSelectorDefaultAxisY(int rangeSelectorDefaultAxisY);

	Color getColorHintRangeSelector();

	void setColorHintRangeSelector(Color colorHintRangeSelector);

	boolean isVerticalSliderVisible();

	void setVerticalSliderVisible(boolean verticalSliderVisible);

	boolean isHorizontalSliderVisible();

	void setHorizontalSliderVisible(boolean horizontalSliderVisible);

	String getTitle();

	void setTitle(String title);

	boolean isTitleVisible();

	void setTitleVisible(boolean titleVisible);

	Color getTitleColor();

	void setTitleColor(Color titleColor);

	Font getTitleFont();

	void setTitleFont(Font titleFont);

	boolean isLegendVisible();

	void setLegendVisible(boolean legendVisible);

	int getLegendPosition();

	/**
	 * Use:
	 * SWT.LEFT
	 * SWT.RIGHT
	 * SWT.TOP
	 * SWT.BOTTOM
	 * 
	 * @param legendPosition
	 */
	void setLegendPosition(int legendPosition);

	boolean isLegendExtendedVisible();

	void setLegendExtendedVisible(boolean legendExtendedVisible);

	IPrimaryAxisSettings getPrimaryAxisSettingsX();

	IPrimaryAxisSettings getPrimaryAxisSettingsY();

	List<ISecondaryAxisSettings> getSecondaryAxisSettingsListX();

	List<ISecondaryAxisSettings> getSecondaryAxisSettingsListY();

	int getOrientation();

	/**
	 * SWT.HORIZONTAL or SWT.VERTICAL
	 * See:http://www.swtchart.org/doc/index.html#Chart_Orientation
	 * 
	 * @param orientation
	 */
	void setOrientation(int orientation);

	Color getBackground();

	void setBackground(Color background);

	Color getBackgroundChart();

	void setBackgroundChart(Color backgroundChart);

	Color getBackgroundPlotArea();

	void setBackgroundPlotArea(Color backgroundPlotArea);

	boolean isEnableCompress();

	void setEnableCompress(boolean enableCompress);

	RangeRestriction getRangeRestriction();

	boolean isShowPositionMarker();

	void setShowPositionMarker(boolean showPositionMarker);

	Color getColorPositionMarker();

	void setColorPositionMarker(Color colorPositionMarker);

	boolean isShowPlotCenterMarker();

	void setShowPlotCenterMarker(boolean showPlotCenterMarker);

	Color getColorPlotCenterMarker();

	void setColorPlotCenterMarker(Color colorPlotCenterMarker);

	boolean isShowLegendMarker();

	void setShowLegendMarker(boolean showLegendMarker);

	Color getColorLegendMarker();

	void setColorLegendMarker(Color colorLegendMarker);

	boolean isShowAxisZeroMarker();

	void setShowAxisZeroMarker(boolean showAxisZeroMarker);

	Color getColorAxisZeroMarker();

	void setColorAxisZeroMarker(Color colorAxisZeroMarker);

	boolean isShowSeriesLabelMarker();

	void setShowSeriesLabelMarker(boolean showSeriesLabelMarker);

	boolean isUseSeriesLabelDescription();

	void setUseSeriesLabelDescription(boolean useSeriesLabelDescription);

	Color getColorSeriesLabelMarker();

	void setColorSeriesLabelMarker(Color colorSeriesLabelMarker);

	boolean isCreateMenu();

	void setCreateMenu(boolean createMenu);

	void addMenuEntry(IChartMenuEntry menuEntry);

	void removeMenuEntry(IChartMenuEntry menuEntry);

	Set<IChartMenuEntry> getMenuEntries();

	IChartMenuEntry getChartMenuEntry(String name);

	void clearMenuEntries();

	boolean isSupportDataShift();

	void setSupportDataShift(boolean supportDataShift);

	void addHandledEventProcessor(IHandledEventProcessor handledEventProcessor);

	void removeHandledEventProcessor(IHandledEventProcessor handledEventProcessor);

	Set<IHandledEventProcessor> getHandledEventProcessors();

	void clearHandledEventProcessors();

	boolean isBufferSelection();

	void setBufferSelection(boolean bufferSelection);
}