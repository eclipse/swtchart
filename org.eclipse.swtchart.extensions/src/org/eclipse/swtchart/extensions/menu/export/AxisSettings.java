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
package org.eclipse.swtchart.extensions.menu.export;

import org.eclipse.swtchart.extensions.core.IAxisScaleConverter;
import org.eclipse.swtchart.extensions.core.IAxisSettings;

public class AxisSettings {

	private int indexAxisX;
	private int indexAxisY;
	private IAxisSettings axisSettingsX;
	private IAxisScaleConverter axisScaleConverterX;
	private IAxisSettings axisSettingsY;
	private IAxisScaleConverter axisScaleConverterY;
	private boolean exportVisibleOnly;

	public int getIndexAxisX() {

		return indexAxisX;
	}

	public void setIndexAxisX(int indexAxisX) {

		this.indexAxisX = indexAxisX;
	}

	public int getIndexAxisY() {

		return indexAxisY;
	}

	public void setIndexAxisY(int indexAxisY) {

		this.indexAxisY = indexAxisY;
	}

	public IAxisSettings getAxisSettingsX() {

		return axisSettingsX;
	}

	public void setAxisSettingsX(IAxisSettings axisSettingsX) {

		this.axisSettingsX = axisSettingsX;
	}

	public IAxisScaleConverter getAxisScaleConverterX() {

		return axisScaleConverterX;
	}

	public void setAxisScaleConverterX(IAxisScaleConverter axisScaleConverterX) {

		this.axisScaleConverterX = axisScaleConverterX;
	}

	public IAxisSettings getAxisSettingsY() {

		return axisSettingsY;
	}

	public void setAxisSettingsY(IAxisSettings axisSettingsY) {

		this.axisSettingsY = axisSettingsY;
	}

	public IAxisScaleConverter getAxisScaleConverterY() {

		return axisScaleConverterY;
	}

	public void setAxisScaleConverterY(IAxisScaleConverter axisScaleConverterY) {

		this.axisScaleConverterY = axisScaleConverterY;
	}

	public boolean isExportVisibleOnly() {

		return exportVisibleOnly;
	}

	public void setExportVisibleOnly(boolean exportVisibleOnly) {

		this.exportVisibleOnly = exportVisibleOnly;
	}
}
