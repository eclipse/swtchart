/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 * Christoph Läubrich - add save fallbacks in case the plot area is not a Control
 * Frank Buloup - internationalization
 *******************************************************************************/
package org.eclipse.swtchart.extensions.charts;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.IAxis.Direction;
import org.eclipse.swtchart.IPlotArea;
import org.eclipse.swtchart.Range;
import org.eclipse.swtchart.extensions.properties.AxisPage;
import org.eclipse.swtchart.extensions.properties.AxisTickPage;
import org.eclipse.swtchart.extensions.properties.ChartPage;
import org.eclipse.swtchart.extensions.properties.GridPage;
import org.eclipse.swtchart.extensions.properties.LegendPage;
import org.eclipse.swtchart.extensions.properties.SeriesLabelPage;
import org.eclipse.swtchart.extensions.properties.SeriesPage;

/**
 * An interactive chart which provides the following abilities.
 * <ul>
 * <li>scroll with arrow keys</li>
 * <li>zoom in and out with ctrl + arrow up/down keys</li>
 * <li>context menus for adjusting axis range and zooming in/out.</li>
 * <li>file selector dialog to save chart to image file.</li>
 * <li>properties dialog to configure the chart settings</li>
 * </ul>
 */
public class InteractiveChart extends Chart implements PaintListener {

	/** the filter extensions */
	private static final String[] EXTENSIONS = new String[]{"*.png", "*.jpeg", "*.jpg"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	/** the selection rectangle for zoom in/out */
	protected SelectionRectangle selection;
	/** the clicked time in milliseconds */
	private long clickedTime;
	/** the initial proposed filename in save as dialog */
	private String saveAsFilename;

	/**
	 * Constru.ctor.
	 * 
	 * @param parent
	 *            the parent composite
	 * @param style
	 *            the style
	 */
	public InteractiveChart(Composite parent, int style) {

		super(parent, style);
		init();
	}

	/**
	 * Initializes.
	 */
	private void init() {

		selection = new SelectionRectangle();
		IPlotArea area = getPlotArea();
		if(area instanceof Control) {
			Control plot = (Control)area;
			plot.addListener(SWT.Resize, this);
			plot.addListener(SWT.MouseMove, this);
			plot.addListener(SWT.MouseDown, this);
			plot.addListener(SWT.MouseUp, this);
			plot.addListener(SWT.MouseWheel, this);
			plot.addListener(SWT.KeyDown, this);
			plot.addPaintListener(this);
			createMenuItems(plot);
		}
	}

	/**
	 * Creates menu items.
	 * 
	 * @param plot
	 */
	private void createMenuItems(Control plot) {

		Menu menu = new Menu(plot);
		plot.setMenu(menu);
		// adjust axis range menu group
		MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText(Messages.ADJUST_AXIS_RANGE);
		Menu adjustAxisRangeMenu = new Menu(menuItem);
		menuItem.setMenu(adjustAxisRangeMenu);
		// adjust axis range
		menuItem = new MenuItem(adjustAxisRangeMenu, SWT.PUSH);
		menuItem.setText(Messages.ADJUST_AXIS_RANGE);
		menuItem.addListener(SWT.Selection, this);
		// adjust X axis range
		menuItem = new MenuItem(adjustAxisRangeMenu, SWT.PUSH);
		menuItem.setText(Messages.ADJUST_X_AXIS_RANGE);
		menuItem.addListener(SWT.Selection, this);
		// adjust Y axis range
		menuItem = new MenuItem(adjustAxisRangeMenu, SWT.PUSH);
		menuItem.setText(Messages.ADJUST_Y_AXIS_RANGE);
		menuItem.addListener(SWT.Selection, this);
		menuItem = new MenuItem(menu, SWT.SEPARATOR);
		// zoom in menu group
		menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText(Messages.ZOOM_IN);
		Menu zoomInMenu = new Menu(menuItem);
		menuItem.setMenu(zoomInMenu);
		// zoom in both axes
		menuItem = new MenuItem(zoomInMenu, SWT.PUSH);
		menuItem.setText(Messages.ZOOM_IN_CTRL);
		menuItem.addListener(SWT.Selection, this);
		// zoom in X axis
		menuItem = new MenuItem(zoomInMenu, SWT.PUSH);
		menuItem.setText(Messages.ZOOM_IN_X_AXIS);
		menuItem.addListener(SWT.Selection, this);
		// zoom in Y axis
		menuItem = new MenuItem(zoomInMenu, SWT.PUSH);
		menuItem.setText(Messages.ZOOM_IN_Y_AXIS);
		menuItem.addListener(SWT.Selection, this);
		// zoom out menu group
		menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText(Messages.ZOOM_OUT);
		Menu zoomOutMenu = new Menu(menuItem);
		menuItem.setMenu(zoomOutMenu);
		// zoom out both axes
		menuItem = new MenuItem(zoomOutMenu, SWT.PUSH);
		menuItem.setText(Messages.ZOOM_OUT_CTRL);
		menuItem.addListener(SWT.Selection, this);
		// zoom out X axis
		menuItem = new MenuItem(zoomOutMenu, SWT.PUSH);
		menuItem.setText(Messages.ZOOM_OUT_X_AXIS);
		menuItem.addListener(SWT.Selection, this);
		// zoom out Y axis
		menuItem = new MenuItem(zoomOutMenu, SWT.PUSH);
		menuItem.setText(Messages.ZOOM_OUT_Y_AXIS);
		menuItem.addListener(SWT.Selection, this);
		menuItem = new MenuItem(menu, SWT.SEPARATOR);
		// save as
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText(Messages.SAVE_AS);
		menuItem.addListener(SWT.Selection, this);
		menuItem = new MenuItem(menu, SWT.SEPARATOR);
		// properties
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText(Messages.PROPERTIES);
		menuItem.addListener(SWT.Selection, this);
	}

	@Override
	public void paintControl(PaintEvent e) {

		selection.draw(e.gc);
	}

	@Override
	public void handleEvent(Event event) {

		super.handleEvent(event);
		switch(event.type) {
			case SWT.MouseMove:
				handleMouseMoveEvent(event);
				break;
			case SWT.MouseDown:
				handleMouseDownEvent(event);
				break;
			case SWT.MouseUp:
				handleMouseUpEvent(event);
				break;
			case SWT.MouseWheel:
				handleMouseWheel(event);
				break;
			case SWT.KeyDown:
				handleKeyDownEvent(event);
				break;
			case SWT.Selection:
				handleSelectionEvent(event);
				break;
			default:
				break;
		}
	}

	/**
	 * Sets the name to be proposed as default when running "Save As".
	 * 
	 * @param saveAsFilename
	 *            The file name to be proposed in the file dialog.
	 */
	public void setProposedSaveAsFilename(String saveAsFilename) {

		this.saveAsFilename = saveAsFilename;
	}

	/**
	 * Handles mouse move event.
	 * 
	 * @param event
	 *            the mouse move event
	 */
	private void handleMouseMoveEvent(Event event) {

		if(!selection.isDisposed()) {
			selection.setEndPoint(event.x, event.y);
			redraw();
		}
	}

	/**
	 * Handles the mouse down event.
	 * 
	 * @param event
	 *            the mouse down event
	 */
	private void handleMouseDownEvent(Event event) {

		if(event.button == 1) {
			selection.setStartPoint(event.x, event.y);
			clickedTime = System.currentTimeMillis();
		}
	}

	/**
	 * Handles the mouse up event.
	 * 
	 * @param event
	 *            the mouse up event
	 */
	private void handleMouseUpEvent(Event event) {

		if(event.button == 1 && System.currentTimeMillis() - clickedTime > 100) {
			for(IAxis axis : getAxisSet().getAxes()) {
				Point range = null;
				if((getOrientation() == SWT.HORIZONTAL && axis.getDirection() == Direction.X) || (getOrientation() == SWT.VERTICAL && axis.getDirection() == Direction.Y)) {
					range = selection.getHorizontalRange();
				} else {
					range = selection.getVerticalRange();
				}
				if(range != null && range.x != range.y) {
					setRange(range, axis);
				}
			}
		}
		//
		selection.dispose();
		redraw();
	}

	/**
	 * Handles mouse wheel event.
	 * 
	 * @param event
	 *            the mouse wheel event
	 */
	private void handleMouseWheel(Event event) {

		for(IAxis axis : getAxes(SWT.HORIZONTAL)) {
			double coordinate = axis.getDataCoordinate(event.x);
			if(event.count > 0) {
				axis.zoomIn(coordinate);
			} else {
				axis.zoomOut(coordinate);
			}
		}
		for(IAxis axis : getAxes(SWT.VERTICAL)) {
			double coordinate = axis.getDataCoordinate(event.y);
			if(event.count > 0) {
				axis.zoomIn(coordinate);
			} else {
				axis.zoomOut(coordinate);
			}
		}
		redraw();
	}

	/**
	 * Handles the key down event.
	 * 
	 * @param event
	 *            the key down event
	 */
	private void handleKeyDownEvent(Event event) {

		if(event.keyCode == SWT.ARROW_DOWN) {
			if(event.stateMask == SWT.MOD1) {
				getAxisSet().zoomOut();
			} else {
				for(IAxis axis : getAxes(SWT.VERTICAL)) {
					axis.scrollDown();
				}
			}
			redraw();
		} else if(event.keyCode == SWT.ARROW_UP) {
			if(event.stateMask == SWT.MOD1) {
				getAxisSet().zoomIn();
			} else {
				for(IAxis axis : getAxes(SWT.VERTICAL)) {
					axis.scrollUp();
				}
			}
			redraw();
		} else if(event.keyCode == SWT.ARROW_LEFT) {
			for(IAxis axis : getAxes(SWT.HORIZONTAL)) {
				axis.scrollDown();
			}
			redraw();
		} else if(event.keyCode == SWT.ARROW_RIGHT) {
			for(IAxis axis : getAxes(SWT.HORIZONTAL)) {
				axis.scrollUp();
			}
			redraw();
		}
	}

	/**
	 * Gets the axes for given orientation.
	 * 
	 * @param orientation
	 *            the orientation
	 * @return the axes
	 */
	private IAxis[] getAxes(int orientation) {

		IAxis[] axes;
		if(getOrientation() == orientation) {
			axes = getAxisSet().getXAxes();
		} else {
			axes = getAxisSet().getYAxes();
		}
		return axes;
	}

	/**
	 * Handles the selection event.
	 * 
	 * @param event
	 *            the event
	 */
	private void handleSelectionEvent(Event event) {

		if(!(event.widget instanceof MenuItem)) {
			return;
		}
		MenuItem menuItem = (MenuItem)event.widget;
		if(menuItem.getText().equals(Messages.ADJUST_AXIS_RANGE)) {
			getAxisSet().adjustRange();
		} else if(menuItem.getText().equals(Messages.ADJUST_X_AXIS_RANGE)) {
			for(IAxis axis : getAxisSet().getXAxes()) {
				axis.adjustRange();
			}
		} else if(menuItem.getText().equals(Messages.ADJUST_Y_AXIS_RANGE)) {
			for(IAxis axis : getAxisSet().getYAxes()) {
				axis.adjustRange();
			}
		} else if(menuItem.getText().equals(Messages.ZOOM_IN_CTRL)) {
			getAxisSet().zoomIn();
		} else if(menuItem.getText().equals(Messages.ZOOM_IN_X_AXIS)) {
			for(IAxis axis : getAxisSet().getXAxes()) {
				axis.zoomIn();
			}
		} else if(menuItem.getText().equals(Messages.ZOOM_IN_Y_AXIS)) {
			for(IAxis axis : getAxisSet().getYAxes()) {
				axis.zoomIn();
			}
		} else if(menuItem.getText().equals(Messages.ZOOM_OUT)) {
			getAxisSet().zoomOut();
		} else if(menuItem.getText().equals(Messages.ZOOM_OUT_X_AXIS)) {
			for(IAxis axis : getAxisSet().getXAxes()) {
				axis.zoomOut();
			}
		} else if(menuItem.getText().equals(Messages.ZOOM_OUT_Y_AXIS)) {
			for(IAxis axis : getAxisSet().getYAxes()) {
				axis.zoomOut();
			}
		} else if(menuItem.getText().equals(Messages.SAVE_AS)) {
			openSaveAsDialog();
		} else if(menuItem.getText().equals(Messages.PROPERTIES)) {
			openPropertiesDialog();
		}
		redraw();
	}

	/**
	 * Opens the Save As dialog.
	 */
	private void openSaveAsDialog() {

		FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
		dialog.setText(Messages.SAVE_AS_DIALOG);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setFileName(saveAsFilename);
		String filename = dialog.open();
		if(filename == null) {
			return;
		}
		int format;
		if(filename.endsWith(".jpg") || filename.endsWith(".jpeg")) { //$NON-NLS-1$ //$NON-NLS-2$
			format = SWT.IMAGE_JPEG;
		} else if(filename.endsWith(".png")) { //$NON-NLS-1$
			format = SWT.IMAGE_PNG;
		} else {
			format = SWT.IMAGE_UNDEFINED;
		}
		if(format != SWT.IMAGE_UNDEFINED) {
			save(filename, format);
		}
	}

	/**
	 * Opens the properties dialog.
	 */
	private void openPropertiesDialog() {

		PreferenceManager manager = new PreferenceManager();
		final String chartTitle = Messages.CHART;
		PreferenceNode chartNode = new PreferenceNode(chartTitle);
		chartNode.setPage(new ChartPage(this, chartTitle));
		manager.addToRoot(chartNode);
		final String legendTitle = Messages.LEGEND;
		PreferenceNode legendNode = new PreferenceNode(legendTitle);
		legendNode.setPage(new LegendPage(this, legendTitle));
		manager.addTo(chartTitle, legendNode);
		final String xAxisTitle = Messages.X_AXIS;
		PreferenceNode xAxisNode = new PreferenceNode(xAxisTitle);
		xAxisNode.setPage(new AxisPage(this, Direction.X, xAxisTitle));
		manager.addTo(chartTitle, xAxisNode);
		final String gridTitle = Messages.GRID;
		PreferenceNode xGridNode = new PreferenceNode(gridTitle);
		xGridNode.setPage(new GridPage(this, Direction.X, gridTitle));
		manager.addTo(chartTitle + "." + xAxisTitle, xGridNode); //$NON-NLS-1$
		final String tickTitle = Messages.TICK;
		PreferenceNode xTickNode = new PreferenceNode(tickTitle);
		xTickNode.setPage(new AxisTickPage(this, Direction.X, tickTitle));
		manager.addTo(chartTitle + "." + xAxisTitle, xTickNode); //$NON-NLS-1$
		final String yAxisTitle = Messages.Y_AXIS;
		PreferenceNode yAxisNode = new PreferenceNode(yAxisTitle);
		yAxisNode.setPage(new AxisPage(this, Direction.Y, yAxisTitle));
		manager.addTo(chartTitle, yAxisNode);
		PreferenceNode yGridNode = new PreferenceNode(gridTitle);
		yGridNode.setPage(new GridPage(this, Direction.Y, gridTitle));
		manager.addTo(chartTitle + "." + yAxisTitle, yGridNode); //$NON-NLS-1$
		PreferenceNode yTickNode = new PreferenceNode(tickTitle);
		yTickNode.setPage(new AxisTickPage(this, Direction.Y, tickTitle));
		manager.addTo(chartTitle + "." + yAxisTitle, yTickNode); //$NON-NLS-1$
		final String seriesTitle = Messages.SERIES;
		PreferenceNode plotNode = new PreferenceNode(seriesTitle);
		plotNode.setPage(new SeriesPage(this, seriesTitle));
		manager.addTo(chartTitle, plotNode);
		final String labelTitle = Messages.LABEL;
		PreferenceNode labelNode = new PreferenceNode(labelTitle);
		labelNode.setPage(new SeriesLabelPage(this, labelTitle));
		manager.addTo(chartTitle + "." + seriesTitle, labelNode); //$NON-NLS-1$
		PreferenceDialog dialog = new PreferenceDialog(getShell(), manager);
		dialog.create();
		dialog.getShell().setText(Messages.PROPERTIES);
		dialog.getTreeViewer().expandAll();
		dialog.open();
	}

	/**
	 * Sets the axis range.
	 * 
	 * @param range
	 *            the axis range in pixels
	 * @param axis
	 *            the axis to set range
	 */
	private void setRange(Point range, IAxis axis) {

		if(range == null) {
			return;
		}
		//
		double min = axis.getDataCoordinate(range.x);
		double max = axis.getDataCoordinate(range.y);
		axis.setRange(new Range(min, max));
	}
}