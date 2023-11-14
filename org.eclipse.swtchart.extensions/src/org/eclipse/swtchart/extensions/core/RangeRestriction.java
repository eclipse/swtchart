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
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

public class RangeRestriction {

	public enum ExtendType {
		RELATIVE, // A percentage values is used.
		ABSOLUTE // The absolute primary axis value is used.
	}

	public static final int NONE = 1; // No flag is set.
	public static final int ZERO_X = 1 << 1; // Only values x >= 0 are displayed.
	public static final int ZERO_Y = 1 << 2; // Only values y >= 0 are displayed.
	public static final int RESTRICT_FRAME = 1 << 3; // It's not possible to zoom outside of the min/max values.
	public static final int RESTRICT_SELECT_X = 1 << 4; // When doing a user selection, only do the selection on the x axis.
	public static final int RESTRICT_SELECT_Y = 1 << 5; // When doing a user selection, only do the selection on the y axis.
	public static final int FORCE_ZERO_MIN_Y = 1 << 6; // Instead of starting with the lowest Y value, 0 is set.
	public static final int REFERENCE_ZOOM_ZERO_Y = 1 << 7; // 0 is the base when doing an y zoomIn/zoomOut action.
	public static final int REFERENCE_ZOOM_ZERO_X = 1 << 8; // 0 is the base when doing an x zoomIn/zoomOut action.
	public static final int RESTRICT_ZOOM_X = 1 << 9; // When doing a zoom action, only do the zoom on the x axis.
	public static final int RESTRICT_ZOOM_Y = 1 << 10; // When doing a zoom action, only do the zoom on the y axis.
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

	public void setZeroX(boolean zeroX) {

		flagSelection(zeroX, ZERO_X);
	}

	public boolean isZeroY() {

		return isFlagSet(ZERO_Y);
	}

	public void setZeroY(boolean zeroY) {

		flagSelection(zeroY, ZERO_Y);
	}

	/**
	 * Use isRestrictFrame(); instead
	 * 
	 * @return boolean
	 */
	@Deprecated
	public boolean isRestrictZoom() {

		return isRestrictFrame();
	}

	public boolean isRestrictFrame() {

		return isFlagSet(RESTRICT_FRAME);
	}

	/**
	 * Use setRestrictFrame(restrictZoom); instead.
	 * 
	 * @param restrictZoom
	 */
	@Deprecated
	public void setRestrictZoom(boolean restrictZoom) {

		setRestrictFrame(restrictZoom);
	}

	public void setRestrictFrame(boolean restrictFrame) {

		flagSelection(restrictFrame, RESTRICT_FRAME);
	}

	/**
	 * Use isRestrictSelectX(); instead.
	 * 
	 * @return boolean
	 */
	@Deprecated
	public boolean isXZoomOnly() {

		return isRestrictSelectX();
	}

	public boolean isRestrictSelectX() {

		return isFlagSet(RESTRICT_SELECT_X);
	}

	/**
	 * Use setRestrictSelectX(xZoomOnly); instead.
	 * 
	 * @param xZoomOnly
	 */
	@Deprecated
	public void setXZoomOnly(boolean xZoomOnly) {

		setRestrictSelectX(xZoomOnly);
	}

	public void setRestrictSelectX(boolean restrictSelectX) {

		flagSelection(restrictSelectX, RESTRICT_SELECT_X);
	}

	/**
	 * Use isRestrictSelectY(); instead.
	 * 
	 * @return boolean
	 */
	@Deprecated
	public boolean isYZoomOnly() {

		return isRestrictSelectY();
	}

	public boolean isRestrictSelectY() {

		return isFlagSet(RESTRICT_SELECT_Y);
	}

	/**
	 * Use setRestrictSelectY(yZoomOnly); instead.
	 * 
	 * @param yZoomOnly
	 */
	@Deprecated
	public void setYZoomOnly(boolean yZoomOnly) {

		setRestrictSelectY(yZoomOnly);
	}

	public void setRestrictSelectY(boolean restrictSelectY) {

		flagSelection(restrictSelectY, RESTRICT_SELECT_Y);
	}

	public boolean isForceZeroMinY() {

		return isFlagSet(FORCE_ZERO_MIN_Y);
	}

	public void setForceZeroMinY(boolean forceZeroMinY) {

		flagSelection(forceZeroMinY, FORCE_ZERO_MIN_Y);
	}

	public boolean isReferenceZoomZeroY() {

		return isFlagSet(REFERENCE_ZOOM_ZERO_Y);
	}

	public void setReferenceZoomZeroY(boolean referenceZoomZeroY) {

		flagSelection(referenceZoomZeroY, REFERENCE_ZOOM_ZERO_Y);
	}

	public boolean isReferenceZoomZeroX() {

		return isFlagSet(REFERENCE_ZOOM_ZERO_X);
	}

	public void setReferenceZoomZeroX(boolean referenceZoomZeroX) {

		flagSelection(referenceZoomZeroX, REFERENCE_ZOOM_ZERO_X);
	}

	public boolean isRestrictZoomX() {

		return isFlagSet(RESTRICT_ZOOM_X);
	}

	public void setRestrictZoomX(boolean restrictZoomX) {

		flagSelection(restrictZoomX, RESTRICT_ZOOM_X);
	}

	public boolean isRestrictZoomY() {

		return isFlagSet(RESTRICT_ZOOM_Y);
	}

	public void setRestrictZoomY(boolean restrictZoomY) {

		flagSelection(restrictZoomY, RESTRICT_ZOOM_Y);
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
