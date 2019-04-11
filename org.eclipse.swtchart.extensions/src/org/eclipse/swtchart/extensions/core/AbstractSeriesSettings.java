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

public abstract class AbstractSeriesSettings implements ISeriesSettings {

	private String description;
	private boolean visible;
	private boolean visibleInLegend;

	public AbstractSeriesSettings() {
		description = "";
		visible = true;
		visibleInLegend = true;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public boolean isVisible() {

		return visible;
	}

	@Override
	public void setVisible(boolean visible) {

		this.visible = visible;
	}

	@Override
	public boolean isVisibleInLegend() {

		return visibleInLegend;
	}

	@Override
	public void setVisibleInLegend(boolean visibleInLegend) {

		this.visibleInLegend = visibleInLegend;
	}
}
