/*******************************************************************************
 * Copyright (c) 2021, 2023 Lablicate GmbH.
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
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.SeriesListUI;

public class SetDescriptionAction extends AbstractMenuListener {

	public SetDescriptionAction(SeriesListUI seriesListUI) {

		super(seriesListUI);
	}

	@Override
	public void menuAboutToShow(IMenuManager menuManager) {

		menuManager.add(new Action() {

			@Override
			public String getText() {

				return Messages.getString(Messages.setDescription);
			}

			@Override
			public String getToolTipText() {

				return Messages.getString(Messages.setDescriptionForSelectedSeries);
			}

			@Override
			public void run() {

				BaseChart baseChart = getBaseChart();
				SeriesListUI seriesListUI = getSeriesListUI();
				Table table = seriesListUI.getTable();
				List<ISeries<?>> selectedSeries = getSelectedSeries();
				//
				if(!selectedSeries.isEmpty()) {
					String firstDescription = selectedSeries.get(0).getDescription();
					InputDialog dialog = new InputDialog(table.getShell(), Messages.getString(Messages.description), Messages.getString(Messages.setDescription), firstDescription, new IInputValidator() {

						@Override
						public String isValid(String input) {

							if(input == null) {
								return Messages.forgotToSetDescription;
							} else {
								input = input.trim();
								if(input.isEmpty()) {
									return Messages.descriptionMustNotBeEmpty;
								}
							}
							//
							return null;
						}
					});
					//
					if(IDialogConstants.OK_ID == dialog.open()) {
						String description = dialog.getValue();
						for(ISeries<?> series : selectedSeries) {
							ISeriesSettings seriesSettings = baseChart.getSeriesSettings(series.getId());
							seriesSettings.setDescription(description);
							baseChart.applySeriesSettings(series, seriesSettings, true);
						}
						refresh();
					}
				}
			}
		});
	}
}