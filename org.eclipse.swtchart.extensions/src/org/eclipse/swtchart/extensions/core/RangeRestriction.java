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

public class RangeRestriction {

	public enum ExtendType {
		RELATIVE, // A percentage values is used.
		ABSOLUTE // The absolute primary axis value is used.
	}

	public static final int NONE = 1; // No flag is set.
	public static final int ZERO_X = 2; // Only values x >= 0 are displayed.
	public static final int ZERO_Y = 4; // Only values y >= 0 are displayed.
	public static final int RESTRICT_ZOOM = 8; // It's not possible to zoom outside of the min/max values.
	public static final int X_ZOOM_ONLY = 16; // When doing a user selection, only zoom x.
	public static final int Y_ZOOM_ONLY = 32; // When doing a user selection, only zoom y.
	public static final int FORCE_ZERO_MIN_Y = 64; // Instead of starting with the lowest Y values, 0 is set.
	//
	private ExtendType extendTypeX;
	private double extendMinX;
	private double extendMaxX;
	private ExtendType extendTypeY;
	private double extendMinY;
	private double extendMaxY;
	//
	private int selection;

	public RangeRestriction() {
		this(NONE);
	}

	public RangeRestriction(int selection) {
		this.selection = selection;
		extendTypeX = ExtendType.RELATIVE;
		extendMinX = 0.0d;
		extendMaxX = 0.0d;
		extendTypeY = ExtendType.RELATIVE;
		extendMinY = 0.0d;
		extendMaxY = 0.0d;
	}

	public boolean isZeroX() {

		return isFlagSet(ZERO_X);
	}

	/**
	 * 0 is the lowest x value.
	 * Otherwise, the lowest x values of the series is used.
	 * 
	 * @param zeroX
	 */
	public void setZeroX(boolean zeroX) {

		flagSelection(zeroX, ZERO_X);
	}

	public boolean isZeroY() {

		return isFlagSet(ZERO_Y);
	}

	/**
	 * 0 is the lowest y value.
	 * Otherwise, the lowest y values of the series is used.
	 * 
	 * @param zeroY
	 */
	public void setZeroY(boolean zeroY) {

		flagSelection(zeroY, ZERO_Y);
	}

	public boolean isRestrictZoom() {

		return isFlagSet(RESTRICT_ZOOM);
	}

	/**
	 * Set true if zooming shall not exceed the min/max values.
	 * 
	 * @param restrictZoom
	 */
	public void setRestrictZoom(boolean restrictZoom) {

		flagSelection(restrictZoom, RESTRICT_ZOOM);
	}

	public boolean isXZoomOnly() {

		return isFlagSet(X_ZOOM_ONLY);
	}

	/**
	 * Set true if only the x-Axis shall be zoomed.
	 * 
	 * @param xZoomOnly
	 */
	public void setXZoomOnly(boolean xZoomOnly) {

		flagSelection(xZoomOnly, X_ZOOM_ONLY);
	}

	public boolean isYZoomOnly() {

		return isFlagSet(Y_ZOOM_ONLY);
	}

	/**
	 * Set true if only the y-Axis shall be zoomed.
	 * 
	 * @param yZoomOnly
	 */
	public void setYZoomOnly(boolean yZoomOnly) {

		flagSelection(yZoomOnly, Y_ZOOM_ONLY);
	}

	public boolean isForceZeroMinY() {

		return isFlagSet(FORCE_ZERO_MIN_Y);
	}

	/**
	 * Set true if only lowest y value starts at 0 in any case.
	 * 
	 * @param forceZeroMinY
	 */
	public void setForceZeroMinY(boolean forceZeroMinY) {

		flagSelection(forceZeroMinY, FORCE_ZERO_MIN_Y);
	}

	public ExtendType getExtendTypeX() {

		return extendTypeX;
	}

	public void setExtendTypeX(ExtendType extendTypeX) {

		this.extendTypeX = extendTypeX;
	}

	public double getExtendMinX() {

		return extendMinX;
	}

	public void setExtendMinX(double extendMinX) {

		this.extendMinX = extendMinX;
	}

	public double getExtendMaxX() {

		return extendMaxX;
	}

	public void setExtendMaxX(double extendMaxX) {

		this.extendMaxX = extendMaxX;
	}

	public ExtendType getExtendTypeY() {

		return extendTypeY;
	}

	public void setExtendTypeY(ExtendType extendTypeY) {

		this.extendTypeY = extendTypeY;
	}

	public double getExtendMinY() {

		return extendMinY;
	}

	public void setExtendMinY(double extendMinY) {

		this.extendMinY = extendMinY;
	}

	public double getExtendMaxY() {

		return extendMaxY;
	}

	public void setExtendMaxY(double extendMaxY) {

		this.extendMaxY = extendMaxY;
	}

	/**
	 * Have a look at the extend types.
	 * 
	 * @param extend
	 */
	public void setExtend(double extend) {

		this.extendMinX = extend;
		this.extendMaxX = extend;
		this.extendMinY = extend;
		this.extendMaxY = extend;
	}

	private boolean isFlagSet(int constant) {

		return (selection & constant) == constant;
	}

	private void flagSelection(boolean flag, int constant) {

		if(flag) {
			selection |= constant;
		} else {
			selection &= Integer.MAX_VALUE - constant;
		}
	}
}
