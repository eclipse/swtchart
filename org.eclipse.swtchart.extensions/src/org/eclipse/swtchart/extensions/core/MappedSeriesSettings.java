/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

public class MappedSeriesSettings {

	private String identifier = "";
	private ISeriesSettings seriesSettings = null;

	/**
	 * The series settings could be null.
	 * 
	 * @param identifier
	 * @param seriesSettings
	 */
	public MappedSeriesSettings(String identifier, ISeriesSettings seriesSettings) {
		this.identifier = identifier != null ? identifier : "";
		this.seriesSettings = seriesSettings;
	}

	public String getIdentifier() {

		return identifier;
	}

	/**
	 * Returns the series settings. This could be null.
	 * 
	 * @return {@link ISeriesSettings}
	 */
	public ISeriesSettings getSeriesSettings() {

		return seriesSettings;
	}
}
