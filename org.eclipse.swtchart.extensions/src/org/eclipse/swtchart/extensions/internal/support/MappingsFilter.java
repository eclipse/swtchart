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
package org.eclipse.swtchart.extensions.internal.support;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swtchart.extensions.core.MappedSeriesSettings;

public class MappingsFilter extends ViewerFilter {

	private String searchText;
	private boolean caseSensitive;

	public void setSearchText(String searchText, boolean caseSensitive) {

		this.searchText = searchText;
		this.caseSensitive = caseSensitive;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if(searchText == null || searchText.equals("")) {
			return true;
		}
		//
		if(element instanceof MappedSeriesSettings) {
			MappedSeriesSettings mappedSeriesSettings = (MappedSeriesSettings)element;
			String mappingsType = mappedSeriesSettings.getMappingsType().label();
			String identifier = mappedSeriesSettings.getIdentifier();
			String description = mappedSeriesSettings.getDescription();
			//
			if(!caseSensitive) {
				searchText = searchText.toLowerCase();
				mappingsType = mappingsType.toLowerCase();
				identifier = identifier.toLowerCase();
				description = description.toLowerCase();
			}
			//
			if(mappingsType.contains(searchText)) {
				return true;
			}
			//
			if(identifier.contains(searchText)) {
				return true;
			}
			//
			if(description.contains(searchText)) {
				return true;
			}
		}
		//
		return false;
	}
}