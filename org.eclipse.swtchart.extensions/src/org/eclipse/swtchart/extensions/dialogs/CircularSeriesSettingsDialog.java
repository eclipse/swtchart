/*******************************************************************************
 * Copyright (c) 2022, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.dialogs;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;

public class CircularSeriesSettingsDialog extends AbstractSeriesSettingsDialog<ICircularSeriesSettings> {

	private AtomicReference<Text> sliceColorControl = new AtomicReference<>();

	public CircularSeriesSettingsDialog(Shell parentShell, ICircularSeriesSettings settings) {

		super(parentShell, settings);
	}

	@Override
	protected void createInputSection(Composite parent) {

		super.createInputSection(parent);
		createSliceColorSection(parent);
	}

	@Override
	protected void initialize() {

		super.initialize();
		ICircularSeriesSettings settings = getSettings();
		if(settings != null) {
			sliceColorControl.get().setBackground(settings.getSliceColor());
		}
	}

	private void createSliceColorSection(Composite parent) {

		String title = "Slice Color";
		createSectionLabel(parent, title);
		//
		Text text = createColorChoser(parent, title, getGridData(GridData.FILL_HORIZONTAL, 1), new Consumer<Color>() {

			@Override
			public void accept(Color color) {

				ICircularSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setSliceColor(color);
				}
			}
		});
		//
		sliceColorControl.set(text);
	}
}