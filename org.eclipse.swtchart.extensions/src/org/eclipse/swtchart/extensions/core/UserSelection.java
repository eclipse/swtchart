/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
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

public class UserSelection {

	private static final int NO_SELECTION = 0;
	//
	private int startX = NO_SELECTION;
	private int startY = NO_SELECTION;
	private int stopX = NO_SELECTION;
	private int stopY = NO_SELECTION;
	private boolean singleClick = true;
	/*
	 * Active is only handled internally.
	 */
	private boolean active = false;

	public UserSelection() {

		reset();
	}

	public int getStartX() {

		return startX;
	}

	public void setStartX(int startX) {

		this.startX = startX;
	}

	public int getStartY() {

		return startY;
	}

	public void setStartY(int startY) {

		this.startY = startY;
	}

	public int getStopX() {

		return stopX;
	}

	public void setStopX(int stopX) {

		this.stopX = stopX;
	}

	public int getStopY() {

		return stopY;
	}

	public void setStopY(int stopY) {

		this.stopY = stopY;
	}

	public void setSingleClick(boolean singleClick) {

		this.singleClick = singleClick;
	}

	/**
	 * Returns if the current user selection is activated.
	 * 
	 * @return boolean
	 */
	public boolean isActive() {

		return active && singleClick;
	}

	/**
	 * Sets the start coordinate, but don't activates the selection.
	 * An activation could lead to unwanted drawings of the rectangle.
	 * 
	 * @param startX
	 * @param startY
	 */
	public void setStartCoordinate(int startX, int startY) {

		active = false;
		//
		this.startX = startX;
		this.startY = startY;
	}

	/**
	 * Sets the stop coordinate and activates the selection.
	 * 
	 * @param stopX
	 * @param stopY
	 */
	public void setStopCoordinate(int stopX, int stopY) {

		active = true;
		//
		this.stopX = stopX;
		this.stopY = stopY;
	}

	public void reset() {

		active = false;
		//
		startX = NO_SELECTION;
		stopX = NO_SELECTION;
		startY = NO_SELECTION;
		stopY = NO_SELECTION;
	}
}
