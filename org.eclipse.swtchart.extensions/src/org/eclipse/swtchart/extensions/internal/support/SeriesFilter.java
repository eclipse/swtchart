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
package org.eclipse.swtchart.extensions.internal.support;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swtchart.ISeries;

public class SeriesFilter extends ViewerFilter {

	private String searchText;
	private boolean caseSensitive;

	public void setSearchText(String searchText, boolean caseSensitive) {

		this.searchText = searchText;
		this.caseSensitive = caseSensitive;
		/*
		 * Search all items in lower case if not case-sensitive
		 */
		if(!caseSensitive) {
			searchText = searchText.toLowerCase();
		}
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if(searchText == null || searchText.equals("")) {
			return true;
		}
		//
		if(element instanceof ISeries<?>) {
			ISeries<?> series = (ISeries<?>)element;
			//
			if(preparedTerm(series.getId()).contains(searchText)) {
				return true;
			} else if(preparedTerm(series.getDescription()).contains(searchText)) {
				return true;
			}
		}
		//
		return false;
	}

	private String preparedTerm(String term) {

		if(!caseSensitive) {
			return term.toLowerCase();
		} else {
			return term;
		}
	}
}
