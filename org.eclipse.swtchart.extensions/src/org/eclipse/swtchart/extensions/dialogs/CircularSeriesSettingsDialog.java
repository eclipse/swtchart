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

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;

public class CircularSeriesSettingsDialog extends AbstractSeriesSettingsDialog<ICircularSeriesSettings> {

	private AtomicReference<Text> sliceColorControl = new AtomicReference<>();
	private AtomicReference<Text> borderColorControl = new AtomicReference<>();
	private AtomicReference<Spinner> borderWidthControl = new AtomicReference<>();
	private AtomicReference<ComboViewer> borderStyleControl = new AtomicReference<>();

	public CircularSeriesSettingsDialog(Shell parentShell, ICircularSeriesSettings settings) {

		super(parentShell, settings);
	}

	@Override
	protected void createInputSection(Composite parent) {

		super.createInputSection(parent);
		createSliceColorSection(parent);
		createBorderColorSection(parent);
		createBorderWidthSection(parent);
		createBorderStyleSection(parent);
	}

	@Override
	protected void initialize() {

		super.initialize();
		ICircularSeriesSettings settings = getSettings();
		if(settings != null) {
			sliceColorControl.get().setBackground(settings.getSliceColor());
			borderColorControl.get().setBackground(settings.getBorderColor());
			borderWidthControl.get().setSelection(settings.getBorderWidth());
			borderStyleControl.get().setSelection(new StructuredSelection(settings.getBorderStyle()));
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

	private void createBorderColorSection(Composite parent) {

		String title = "Border Color";
		createSectionLabel(parent, title);
		//
		Text text = createColorChoser(parent, title, getGridData(GridData.FILL_HORIZONTAL, 1), new Consumer<Color>() {

			@Override
			public void accept(Color color) {

				ICircularSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setBorderColor(color);
				}
			}
		});
		//
		borderColorControl.set(text);
	}

	private void createBorderWidthSection(Composite parent) {

		String title = "Border Width";
		createSectionLabel(parent, title);
		//
		Spinner spinner = createSpinner(parent, title, 1, 50, 1, getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Integer>() {

			@Override
			public void accept(Integer selection) {

				ICircularSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setBorderWidth(selection);
				}
			}
		});
		//
		borderWidthControl.set(spinner);
	}

	private void createBorderStyleSection(Composite parent) {

		String title = "Border Style";
		createSectionLabel(parent, title);
		//
		ComboViewer comboViewer = createComboViewer(parent, title, LineStyle.values(), LineStyle.NONE, getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Object>() {

			@Override
			public void accept(Object object) {

				ICircularSeriesSettings settings = getSettings();
				if(settings != null) {
					if(object instanceof LineStyle) {
						settings.setBorderStyle((LineStyle)object);
					}
				}
			}
		});
		//
		borderStyleControl.set(comboViewer);
	}
}