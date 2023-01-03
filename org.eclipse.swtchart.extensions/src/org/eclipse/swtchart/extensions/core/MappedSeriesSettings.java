/*******************************************************************************
 * Copyright (c) 2020, 2023 Lablicate GmbH.
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

	public static final String DESCRIPTION = "Mapped Series Settings";
	public static final String FILE_EXTENSION = ".mss";
	public static final String FILE_NAME = DESCRIPTION.replaceAll("\\s", "") + FILE_EXTENSION;
	public static final String FILTER_EXTENSION = "*" + FILE_EXTENSION;
	public static final String FILTER_NAME = DESCRIPTION + " (*" + FILE_EXTENSION + ")";
	//
	private MappingsType mappingsType = null;
	private String identifier = "";
	private ISeriesSettings seriesSettings = null;

	public MappedSeriesSettings(String identifier, ISeriesSettings seriesSettings) {

		this(MappingsType.NONE, identifier, seriesSettings);
	}

	/**
	 * The series settings could be null.
	 * 
	 * @param identifier
	 * @param seriesSettings
	 */
	public MappedSeriesSettings(MappingsType mappingsType, String identifier, ISeriesSettings seriesSettings) {

		this.mappingsType = mappingsType;
		this.identifier = identifier != null ? identifier : "";
		this.seriesSettings = seriesSettings;
	}

	public MappingsType getMappingsType() {

		return mappingsType;
	}

	public String getIdentifier() {

		return identifier;
	}

	public String getDescription() {

		return seriesSettings != null ? seriesSettings.getDescription() : "";
	}

	/**
	 * Returns the series settings.
	 * It could be null.
	 * 
	 * @return {@link ISeriesSettings}
	 */
	public ISeriesSettings getSeriesSettings() {

		return seriesSettings;
	}
}