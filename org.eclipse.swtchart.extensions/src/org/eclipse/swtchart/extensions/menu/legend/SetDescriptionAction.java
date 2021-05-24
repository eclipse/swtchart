/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.menu.legend;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.extensions.core.SeriesListUI;
import org.eclipse.swtchart.extensions.internal.support.MappingsSupport;
import org.eclipse.swtchart.extensions.internal.support.SeriesLabelProvider;

public class SetDescriptionAction extends AbstractMenuListener {

	public SetDescriptionAction(SeriesListUI seriesListUI) {

		super(seriesListUI);
	}

	@Override
	public void menuAboutToShow(IMenuManager menuManager) {

		menuManager.add(new Action() {

			@Override
			public String getText() {

				return "Set Description";
			}

			@Override
			public String getToolTipText() {

				return "Set a description for the selected series.";
			}

			@Override
			public void run() {

				SeriesListUI seriesListUI = getSeriesListUI();
				Table table = seriesListUI.getTable();
				List<ISeries<?>> selectedSeries = getSelectedSeries();
				//
				if(selectedSeries.size() > 0) {
					String firstDescription = selectedSeries.get(0).getDescription();
					InputDialog dialog = new InputDialog(table.getShell(), "Description", "Set a description.", firstDescription, new IInputValidator() {

						@Override
						public String isValid(String input) {

							if(input == null) {
								return "Please set a description.";
							} else {
								input = input.trim();
								if(input.isEmpty()) {
									return "The description must be not empty.";
								}
							}
							//
							return null;
						}
					});
					//
					if(IDialogConstants.OK_ID == dialog.open()) {
						Object object = dialog.getValue();
						for(ISeries<?> series : selectedSeries) {
							MappingsSupport.mapSettings(series, SeriesLabelProvider.DESCRIPTION, object, getScrollableChart());
						}
						refresh();
					}
				}
			}
		});
	}
}
